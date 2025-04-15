package it.polimi.ingsw.is25am02.controller.server;

import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.Spaceship;
import it.polimi.ingsw.is25am02.model.Coordinate;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ServerController extends UnicastRemoteObject implements VirtualServer {
    //Gestione Coda
    private final BlockingQueue<Runnable> methodQueue = new LinkedBlockingQueue<>();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(8);
    private static final Runnable POISON_PILL = () -> {};
    private Thread queueProcessor;

    //Gestione Login, Lobby, Inizio partita
    private final Map<Integer, Lobby> lobbies = new ConcurrentHashMap<>();
    private final Map<Integer, GameSession> activeGames = new ConcurrentHashMap<>();
    private final Map<String, VirtualView> registeredClients = new ConcurrentHashMap<>();
    private int nextLobbyId = 1;

    public ServerController() throws RemoteException {
        super();
        startQueueProcessor();
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
        } else {
            System.out.println(">> ThreadPool shutdown failed.");
        }
    }

    public void nicknameRegistration(String nickname, VirtualView client) {
        methodQueue.offer(() -> {
            if (!registeredClients.containsKey(nickname)) {
                registeredClients.put(nickname, client);
            } else {
                try {
                    client.reportError("> The nickname is already taken");
                } catch (Exception e) {
                    reportErrorOnServer("connection problem in method nichknameRegistration with parameter " + nickname);
                }
            }
        });
    }

    //todo più avanti questo può essere scritto non a schermo nel terminale ma in un file di log, con contesto di chiamata e timestamp
    private void reportErrorOnServer(String message) {
        System.err.println(">> " + message);
    }

    public void createLobby(VirtualView client, String nickname, int maxPlayers, PlayerColor color, int level) {
        methodQueue.offer(() -> {
            if (registeredClients.containsKey(nickname)) {
                Player p = new Player(new Spaceship(level), nickname, color, client, nextLobbyId++);
                Lobby lobby = new Lobby(p.getLobbyId(), maxPlayers, p, client, level);
                lobbies.put(lobby.getId(), lobby);
                try {
                    client.displayMessage("> The lobby with id" + lobby.getId() + "has been created!");
                } catch (Exception e) {
                    reportErrorOnServer("connection problem in method createLobby with parameter: " + nickname + ", " + maxPlayers + ", " + color + ", " + level);
                }
            } else {
                try {
                    client.reportError("> Nickname not found :(");
                } catch (Exception e) {
                    reportErrorOnServer("connection problem in method createLobby with parameter: " + nickname + ", " + maxPlayers + ", " + color + ", " + level);
                }
            }
        });
    }

    public void getLobbies(VirtualView client) {
        methodQueue.offer(() -> {
            try {
                client.displayMessage("> Lobbies available:");

                for (Integer lobbyId : lobbies.keySet()) {
                    client.displayMessage("id: " + lobbyId.toString() + ", owner: "+ lobbies.get(lobbyId).getPlayers().get(0).getNickname() + ", players: " + lobbies.get(lobbyId).getPlayers().size() + "/" + lobbies.get(lobbyId).getMaxPlayers());
                }
            } catch (Exception e) {
                reportErrorOnServer("connection problem in method getLobbies");
            }
        });
    }

    public synchronized void joinLobby(VirtualView client, int lobbyId, String nickname, PlayerColor color) {
        methodQueue.offer(() -> {
            try {
                if (!lobbies.containsKey(lobbyId)) {
                    client.reportError("> Lobby not found :(");
                }
                if (lobbies.get(lobbyId).getPlayers().stream().anyMatch(player -> player.getColor().equals(color))) {
                    client.reportError("> Color is already in use");
                }
                if (!registeredClients.containsKey(nickname)) {
                    client.reportError("> Your nickname was not found");
                }
            } catch (Exception e) {
                reportErrorOnServer("connection problem in method joinLobby with parameter: " + lobbyId + ", " + nickname + ", " + color);
            }
            Lobby lobby = lobbies.get(lobbyId);
            Player p = new Player(new Spaceship(lobby.getLevel()), nickname, color, client, lobbyId);
            lobby.addPlayer(p);
            try {
                if (lobby.isFull()) {
                    startGame(lobby);
                    lobbies.remove(lobbyId);

                    for (Player player : lobby.getPlayers()) {
                        player.getObserver().displayMessage("> Game started!");
                    }
                } else {
                    client.displayMessage("> You have been added to the lobby" + lobby.getId());

                }
            } catch (Exception e) {
                reportErrorOnServer("connection problem in method joinLobby with parameter: " + lobbyId + ", " + nickname + ", " + color);
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
                    client.displayMessage("> LobbyId" + lobbyId + "is in the game");
                } else {
                    client.displayMessage("> LobbyId " + lobbyId + " is not in the game");
                }
            } catch (Exception e) {
                reportErrorOnServer("connection problem in method isGameRunning with parameter: " + lobbyId);
            }
        });
    }

    private GameSession getGameFromPlayer(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (!lobbies.get(player.getLobbyId()).isGameStarted() || g == null) {
            try {
                player.getObserver().reportError("> Player not found in any game");
            } catch (Exception e) {
                reportErrorOnServer("connection problem in method getGameFromPlayer with parameter: " + player.getNickname());
            }
            return null;
        } else return g;
    }

    public void flipHourglass(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().flipHourglass(player));
            }
        });
    }

    public void takeTile(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().takeTile(player));
            }
        });
    }

    public void takeTile(Player player, Tile tile) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().takeTile(player, tile));
            }
        });
    }

    public void takeMiniDeck(Player player, int index) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().takeMiniDeck(player, index));
            }
        });
    }

    public void returnMiniDeck(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().returnMiniDeck(player));
            }
        });
    }

    public void bookTile(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().bookTile(player));
            }
        });
    }

    public void addBookedTile(Player player, int index, Coordinate pos, RotationType rotation) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().addBookedTile(player, index, pos, rotation));
            }
        });
    }

    public void returnTile(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().returnTile(player));
            }
        });
    }

    public void addTile(Player player, Coordinate pos, RotationType rotation) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().addTile(player, pos, rotation));
            }
        });
    }

    public void shipFinished(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().shipFinished(player));
            }
        });
    }

    public void checkSpaceship(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().checkSpaceship(player));
            }
        });
    }

    public void removeTile(Player player, Coordinate pos) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().removeTile(player, pos));
            }
        });
    }

    public void checkWrongSpaceship(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().checkWrongSpaceship(player));
            }
        });
    }

    public void addCrew(Player player, Coordinate pos, AliveType type) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().addCrew(player, pos, type));
            }
        });
    }

    public void ready(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().ready(player));
            }
        });
    }

    public void playNextCard(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().playNextCard(player));
            }
        });
    }

    public void earlyLanding(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().earlyLanding(player));
            }
        });
    }

    public void choice(Player player, boolean choice) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().choice(player, choice));
            }
        });
    }

    public void removeCrew(Player player, Coordinate pos) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().removeCrew(player, pos));
            }
        });
    }

    public void choiceBox(Player player, boolean choice) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().choiceBox(player, choice));
            }
        });
    }

    public void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().moveBox(player, start, end, boxType, on));
            }
        });
    }

    public void choicePlanet(Player player, int index) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().choicePlanet(player, index));
            }
        });
    }

    public void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().choiceDoubleMotor(player, motors, batteries));
            }
        });
    }

    public void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().choiceDoubleCannon(player, cannons, batteries));
            }
        });
    }

    public void choiceCrew(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().choiceCrew(player));
            }
        });
    }

    public void removeBox(Player player, Coordinate pos, BoxType type) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().removeBox(player, pos, type));
            }
        });
    }

    public void removeBattery(Player player, Coordinate pos) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().removeBattery(player, pos));
            }
        });
    }

    public void rollDice(Player player) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().rollDice(player));
            }
        });
    }

    public void calculateDamage(Player player, Coordinate pos) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().calculateDamage(player, pos));
            }
        });
    }

    public void keepBlock(Player player, Coordinate pos) {
        methodQueue.offer(() -> {
            GameSession g = getGameFromPlayer(player);
            if (g != null) {
                g.getQueue().offer(() -> g.getGame().keepBlock(player, pos));
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
                        gameSession.getGame().getPlayers().get(i).getObserver().displayMessage("> The match has ended");
                    } catch (Exception e) {
                        reportErrorOnServer("connection problem in method endGame with parameter: " + lobbyId);
                    }
                }
            }
        });
    }
}
