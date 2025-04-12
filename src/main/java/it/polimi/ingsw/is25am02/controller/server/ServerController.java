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
        if(methodQueue.offer(POISON_PILL)){
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

    public void nicknameRegistration(String nickname, VirtualView client){
        methodQueue.offer(() -> {
            if (!registeredClients.containsKey(nickname)) {
                registeredClients.put(nickname, client);
            } else {
                //todo notifica al client che il nome non è disponibile perchè già occupato
            }
        });
    }

    public void createLobby(VirtualView client, String nickname, int maxPlayers, PlayerColor color, int level) {
        methodQueue.offer(() -> {
            if (registeredClients.containsKey(nickname)) {
                Player p = new Player(new Spaceship(level), nickname, color, client, nextLobbyId++);
                Lobby lobby = new Lobby(p.getLobbyId(), maxPlayers, p, client, level);
                lobbies.put(lobby.getId(), lobby);
                //todo notifica al client che ha creato la lobby con id "lobby.getId()"
            } else {
                //todo notifica al client che nickname non trovato
            }
        });
    }

    public void getLobbies(VirtualView client) {
        methodQueue.offer(() -> {
            //todo notifica al player quali lobby sono disponibili
        });
    }

    public synchronized void joinLobby(VirtualView client, int lobbyId, String nickname, PlayerColor color) {
        methodQueue.offer(() -> {
            if (!lobbies.containsKey(lobbyId)) {
                //todo notifica al client che non è stata trovata la lobby
            }
            if (lobbies.get(lobbyId).getPlayers().stream().anyMatch(player -> player.getColor().equals(color))) {
                //todo notifica al client che il suo colore è già stato preso
            }
            if (!registeredClients.containsKey(nickname)) {
                //todo notifica al client che il suo nickname non è stato trovato
            }
            Lobby lobby = lobbies.get(lobbyId);
            Player p = new Player(new Spaceship(lobby.getLevel()), nickname, color, client, lobbyId);
            lobby.addPlayer(p);
            if (lobby.isFull()) {
                startGame(lobby);
                lobbies.remove(lobbyId);
                //todo notifico ai client che il gioco è iniziato
            } else{
                //todo notifico al client chiamante che si è unito alla lobby
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
            //todo notifica al client se il lobbyId da lui cercato è in gioco o no
        });
    }

    private GameSession getGameFromPlayer(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (!lobbies.get(player.getLobbyId()).isGameStarted() || g == null) {
            //todo notifico al client che "Player not found in any game"
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
                //todo notifico ai client che la partita è stata chiusa
            }
        });
    }
}
