package it.polimi.ingsw.is25am02.controller.server;

import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.controller.server.exception.PlayerNotFoundException;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.Spaceship;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.model.Lobby;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.utils.enumerations.AliveType;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ServerController extends UnicastRemoteObject implements VirtualServer {
    //Gestione Coda
    private final BlockingQueue<Runnable> methodQueue = new LinkedBlockingQueue<>();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(8);
    private static final Runnable POISON_PILL = () -> {};
    private Thread queueProcessor;
    public static final Logger logger = Logger.getLogger(ServerController.class.getName());
    //todo gestire concorrenza logger
    private final PingManager pingManager;


    //Gestione Login, Lobby, Inizio partita
    private final Map<Integer, Lobby> lobbies = new ConcurrentHashMap<>();
    private final Map<Integer, GameSession> activeGames = new ConcurrentHashMap<>();
    private final Map<String, VirtualView> registeredClients = new ConcurrentHashMap<>();
    private int nextLobbyId = 1;

    public ServerController() throws RemoteException {
        super();
        try {
            FileHandler fh = new FileHandler("server.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to setup logger FileHandler", e);
        }
        startQueueProcessor();
        pingManager = new PingManager(this);
    }

    private void startQueueProcessor() {
        queueProcessor = new Thread(() -> {
            while (true) {
                try {
                    Runnable task = methodQueue.take();
                    if (task == POISON_PILL) break;
                    threadPool.submit(task);
                } catch (InterruptedException e) {
                    e.getMessage();
                    break;
                }
            }
        });
        queueProcessor.start();
    }

    public void shutdown() {
        if (methodQueue.offer(POISON_PILL)) {
            try {
                queueProcessor.join();
            } catch (InterruptedException e) {
                e.getMessage();
            }
            threadPool.shutdownNow();
            System.out.println(">> ThreadPool shutdown.");
            System.out.println(">> Thread administrator shutdown.");
            pingManager.stop();
        } else {
            System.out.println(">> ThreadPool shutdown failed.");
        }
    }

    public void ping(String nickname) throws RemoteException {
        synchronized (pingManager) {
            pingManager.ping(nickname);
        }
    }

    public void disconnectClient(String nickname){
        synchronized (registeredClients) {
            //lo cancello dai player attivi
            registeredClients.remove(nickname);
        }

        synchronized (lobbies) {
            //controllo se è in una lobby non ancora cominciata
            for (Lobby lobby : lobbies.values()) {
                for (Player player : lobby.getPlayers()) {
                    if (player.getNickname().equals(nickname)) {
                        if (lobby.getPlayers().getFirst().getNickname().equals(nickname)) {
                            lobbies.remove(lobby.getId());
                            lobby.getPlayers().removeFirst();
                            for (Player player1 : lobby.getPlayers()) {
                                try {
                                    player1.getObserver().displayMessage("disconnect.lobby_owner", null);
                                    player1.getObserver().setMenuState(MenuState.MENU);
                                } catch (Exception e) {
                                    logger.log(Level.SEVERE, "connection problem during client disconnection (case lobby_owner) " + e);
                                }
                            }
                        } else {
                            lobby.getPlayers().remove(player);
                            for (Player player2 : lobby.getPlayers()) {
                                try {
                                    player2.getObserver().displayMessage("disconnect.lobby", Map.of("att", String.valueOf(lobby.getPlayers().size()), "max", String.valueOf(lobby.getMaxPlayers())));
                                } catch (Exception e) {
                                    logger.log(Level.SEVERE, "connection problem during client disconnection (case lobby) " + e);
                                }
                            }
                        }
                        return;
                    }
                }
            }
        }

        synchronized (activeGames) {
            for (GameSession gameSession : activeGames.values()) {
                for (Player player : gameSession.getGame().getPlayers()) {
                    if (player.getNickname().equals(nickname)) {
                        activeGames.remove(gameSession.getLobbyId());
                        gameSession.getGame().getPlayers().remove(player);
                        for (Player player2 : gameSession.getGame().getPlayers()) {
                            try {
                                player2.getObserver().displayMessage("disconnect.game", null);
                                player2.getObserver().setMenuState(MenuState.MENU);
                            } catch (Exception e) {
                                logger.log(Level.SEVERE, "connection problem during client disconnection " + e);
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    public void nicknameRegistration(String nickname, VirtualView client) {
        methodQueue.offer(() -> {
            try {
                synchronized (registeredClients) {
                    if (!registeredClients.containsKey(nickname)) {
                        registeredClients.put(nickname, client);
                        client.setMenuState(MenuState.MENU);
                        client.setNickname(nickname);
                        pingManager.registerClient(nickname);
                    } else {
                        client.reportError("error.menu.nickname.taken", null);
                    }
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "connection problem in method nicknameRegistration with parameter " + nickname, e);
            }
        });
    }

    public synchronized void createLobby(VirtualView client, String nickname, int maxPlayers, PlayerColor color, int level) {
        methodQueue.offer(() -> {
            boolean found = false;
            synchronized (registeredClients) {
                found = registeredClients.containsKey(nickname);
            }
            if (found) {
                synchronized (lobbies) {
                    Player p = new Player(new Spaceship(level), nickname, color, client, nextLobbyId++);
                    Lobby lobby = new Lobby(p.getLobbyId(), maxPlayers, p, client, level);
                    lobbies.put(lobby.getId(), lobby);
                    try {
                        client.displayMessage("lobby.creation", Map.of("num", String.valueOf(lobby.getId())));
                        client.setMenuState(MenuState.WAITING);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "connection problem in method createLobby with parameter: " + nickname + ", " + maxPlayers + ", " + color + ", " + level, e);
                    }
                }
            } else {
                try {
                    client.reportError("error.menu.nickname.notFound", null);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "connection problem in method createLobby with parameter: " + nickname + ", " + maxPlayers + ", " + color + ", " + level, e);
                }
            }
        });
    }

    public void getLobbies(VirtualView client) {
        methodQueue.offer(() -> {
            try {
                client.displayMessage("lobby.available", null);

                synchronized (lobbies) {
                    for (Integer lobbyId : lobbies.keySet()) {
                        client.displayMessage("lobby.show", Map.of("num", String.valueOf(lobbyId), "owner", lobbies.get(lobbyId).getPlayers().getFirst().getNickname(), "att", String.valueOf(lobbies.get(lobbyId).getPlayers().size()), "max", String.valueOf(lobbies.get(lobbyId).getMaxPlayers())));
                    }
                    client.setLobbiesView(convertToLobbyViewMap(lobbies));
                    System.out.println("ricevuta richiesta lsita lobby");
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "connection problem in method getLobbies", e);
            }
        });
    }

    public static Map<Integer, LobbyView> convertToLobbyViewMap(Map<Integer, Lobby> lobbies) {
        Map<Integer, LobbyView> result = new HashMap<>();

        for (Map.Entry<Integer, Lobby> entry : lobbies.entrySet()) {
            Lobby lobby = entry.getValue();

            List<String> nicknames = lobby.getPlayers()
                    .stream()
                    .map(player -> player.getNickname())
                    .collect(Collectors.toList());

            LobbyView view = new LobbyView(
                    lobby.getId(),
                    lobby.getMaxPlayers(),
                    nicknames,
                    lobby.getLevel()
            );

            result.put(entry.getKey(), view);
        }

        return result;
    }


    public synchronized void joinLobby(VirtualView client, int lobbyId, String nickname, PlayerColor color) {
        methodQueue.offer(() -> {
            try {
                synchronized (registeredClients) {
                    synchronized (lobbies) {
                        if (!lobbies.containsKey(lobbyId)) {
                            client.reportError("error.menu.lobby", null);
                        } else if (lobbies.get(lobbyId).getPlayers().stream().anyMatch(player -> player.getColor().equals(color))) {
                            client.reportError("error.menu.color", null);
                        } else if (!registeredClients.containsKey(nickname)) {
                            client.reportError("error.menu.nickname.notFound", null);
                        } else {
                            Lobby lobby = lobbies.get(lobbyId);
                            Player p = new Player(new Spaceship(lobby.getLevel()), nickname, color, client, lobbyId);
                            lobby.addPlayer(p);
                            if (lobby.isFull()) {
                                startGame(lobby);
                                lobbies.remove(lobbyId);

                                for (Player player : lobby.getPlayers()) {
                                    //player.getObserver().displayMessage("start", null);
                                    player.getObserver().setMenuState(MenuState.GAME);
                                    player.getObserver().setBuildView(lobby.getLevel(), player.getColor());
                                }
                            } else {
                                client.displayMessage("lobby.join", Map.of("num", String.valueOf(lobby.getId())));
                                client.setMenuState(MenuState.WAITING);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "connection problem in method joinLobby with parameter: " + lobbyId + ", " + nickname + ", " + color, e);
            }
        });
    }

    private void startGame(Lobby lobby) {
        GameSession gameSession = new GameSession(lobby.getId(), lobby.getPlayers(), lobby.getLevel());
        activeGames.put(lobby.getId(), gameSession);
        lobby.startGame();
    }

    public synchronized void isGameRunning(VirtualView client, int lobbyId) {
        methodQueue.offer(() -> {
            try {
                if (activeGames.containsKey(lobbyId)) {
                    client.displayMessage("lobby.state", Map.of("num", String.valueOf(lobbyId), "state", "is in the game"));
                } else if (lobbies.containsKey(lobbyId)) {
                    client.displayMessage("lobby.state", Map.of("num", String.valueOf(lobbyId), "state", "hasn't started yet"));
                } else {
                    client.displayMessage("lobby.state", Map.of("num", String.valueOf(lobbyId), "state", "is not in the game"));
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "connection problem in method isGameRunning with parameter: " + lobbyId, e);
            }
        });
    }

    private Player getPlayerFromNickname(String nickname) throws PlayerNotFoundException {
        for (GameSession g : activeGames.values()) {
            for (Player p : g.getGame().getPlayers()) {
                if (p.getNickname().equalsIgnoreCase(nickname)) {
                    return p;
                }
            }
        }
        throw new PlayerNotFoundException("Player <" + nickname + "> not found");
    }

    private GameSession getGameFromPlayer(String nickname) throws PlayerNotFoundException {
        Player player = getPlayerFromNickname(nickname);
        GameSession g = activeGames.get(player.getLobbyId());
        if (lobbies.containsKey(player.getLobbyId()) || !activeGames.containsKey(player.getLobbyId())|| g == null) {
            try {
                player.getObserver().reportError("error.player.notFound", null);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method getGameFromPlayer", e);
            }
            return null;
        } else return g;
    }

    public void flipHourglass(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().flipHourglass(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method flipHourglass", e);
            }
        });
    }

    public void hourglass(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().hourglass(player)); //todo questo è il metodo sbagliato
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method flipHourglass", e);
            }
        });
    }


    public void takeTile(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().takeTile(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method takeTile", e);
            }
        });
    }

    //todo sostituire Tile con qualhce tipo primitivo univoco che rappresenti però anche rotazione ecc.
    public void takeTile(String nickname, String tile_imagePath) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().takeTile(player, tile_imagePath));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method takeTile", e);
            }
        });
    }

    public void takeMiniDeck(String nickname, int index) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().takeMiniDeck(player, index));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method takeMiniDeck", e);
            }
        });
    }

    public void returnMiniDeck(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().returnMiniDeck(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method returnMiniDeck", e);
            }
        });
    }

    public void bookTile(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().bookTile(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method bookTile", e);
            }
        });
    }

    public void addBookedTile(String nickname, int index, Coordinate pos, RotationType rotation) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().addBookedTile(player, index, pos, rotation));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method addBookedTile", e);
            }
        });
    }

    public void returnTile(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().returnTile(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method returnTile", e);
            }
        });
    }

    public void addTile(String nickname, Coordinate pos, RotationType rotation) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().addTile(player, pos, rotation));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method addTile", e);
            }
        });
    }

    public void shipFinished(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().shipFinished(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method shipFinished", e);
            }
        });
    }

    public void checkSpaceship(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().checkSpaceship(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method checkSpaceship", e);
            }
        });
    }

    public void removeTile(String nickname, Coordinate pos) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().removeTile(player, pos));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method removeTile", e);
            }
        });
    }

    public void checkWrongSpaceship(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().checkWrongSpaceship(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method checkWrongSpaceship", e);
            }
        });
    }

    public void addCrew(String nickname, Coordinate pos, AliveType type) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().addCrew(player, pos, type));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method addCrew", e);
            }
        });
    }

    public void ready(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().ready(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method ready", e);
            }
        });
    }

    public void playNextCard(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().playNextCard(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method playNextCard", e);
            }
        });
    }

    public void earlyLanding(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().earlyLanding(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method earlyLanding", e);
            }
        });
    }

    public void choice(String nickname, boolean choice) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().choice(player, choice));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method choice", e);
            }
        });
    }

    public void removeCrew(String nickname, Coordinate pos) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().removeCrew(player, pos));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method removeCrew", e);
            }
        });
    }

    public void choiceBox(String nickname, boolean choice) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().choiceBox(player, choice));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method choiceBox", e);
            }
        });
    }

    public void moveBox(String nickname, Coordinate start, Coordinate end, BoxType boxType, boolean on) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().moveBox(player, start, end, boxType, on));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method moveBox", e);
            }
        });
    }

    public void choicePlanet(String nickname, int index) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().choicePlanet(player, index));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method choicePlanet", e);
            }
        });
    }

    public void choiceDoubleMotor(String nickname, List<Coordinate> motors, List<Coordinate> batteries) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().choiceDoubleMotor(player, motors, batteries));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method choiceDoubleMotor", e);
            }
        });
    }

    public void choiceDoubleCannon(String nickname, List<Coordinate> cannons, List<Coordinate> batteries) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().choiceDoubleCannon(player, cannons, batteries));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method choiceDoubleCannon", e);
            }
        });
    }

    public void choiceCrew(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().choiceCrew(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method choiceCrew", e);
            }
        });
    }

    public void removeBox(String nickname, Coordinate pos, BoxType type) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().removeBox(player, pos, type));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method removeBox", e);
            }
        });
    }

    public void removeBattery(String nickname, Coordinate pos) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().removeBattery(player, pos));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method removeBattery", e);
            }
        });
    }

    public void rollDice(String nickname) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().rollDice(player));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method rollDice", e);
            }
        });
    }

    public void calculateDamage(String nickname, Coordinate pos) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().calculateDamage(player, pos));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname "+nickname+" not found in method ");
            }
        });
    }

    public void keepBlock(String nickname, Coordinate pos) {
        methodQueue.offer(() -> {
            try {
                GameSession g = getGameFromPlayer(nickname);
                Player player = getPlayerFromNickname(nickname);
                if (g != null) {
                    g.getQueue().offer(() -> g.getGame().keepBlock(player, pos));
                }
            } catch (PlayerNotFoundException e) {
                logger.log(Level.SEVERE, "nickname " + nickname + " not fount in method keepBlock", e);
            }
        });
    }

    public void Winners(int lobbyId) {
        methodQueue.offer(() -> activeGames.get(lobbyId).getQueue().offer(() -> activeGames.get(lobbyId).getGame().Winners()));
    }

    public synchronized void endGame(int lobbyId) {
        methodQueue.offer(() -> {
            GameSession gameSession = activeGames.remove(lobbyId);
            lobbies.remove(lobbyId);
            if (gameSession != null) {
                gameSession.shutdown();
                for (int i = 0; i < gameSession.getGame().getPlayers().size(); i++) {
                    try {
                        gameSession.getGame().getPlayers().get(i).getObserver().displayMessage("end", null);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "connection problem in method endGame with parameter: " + lobbyId, e);
                    }
                }
            }
        });
    }
}
