package it.polimi.ingsw.is25am02.controller.server;

import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

public class GameSession {
    private final Game game;
    private final int lobbyId;

    //Attributes for queue management
    private final BlockingQueue<Runnable> gameMethodQueue = new LinkedBlockingQueue<>();
    private final ExecutorService gameThreadPool = Executors.newFixedThreadPool(4);
    private static final Runnable POISON_PILL = () -> {};
    private Thread gameQueueProcessor;

    public GameSession(int lobbyId, List<Player> players, int level) {
        this.lobbyId = lobbyId;
        this.game = new Game(players, level);
        for(Player player : game.getPlayers()) {
            player.onUpdateEverything(game.getlevel(), game.getPlayers(), game.getGameboard(), game.getCurrentCard(), game.getCurrentState(), player.getSpaceship().getSpaceshipIterator().getSpaceshipMask(), game.getGameboard().getStartingPosition(), game.getDeck().getDeck());
        }
        startGameQueueProcessor();
    }

    public Game getGame() { return game; }
    public int getLobbyId() { return lobbyId; }
    public BlockingQueue<Runnable> getQueue() { return gameMethodQueue; }

    //Start single queue for game
    private void startGameQueueProcessor() {
        gameQueueProcessor = new Thread(() -> {
            while (true) {
                try {
                    Runnable task = gameMethodQueue.take();
                    if (task == POISON_PILL) break;
                    gameThreadPool.submit(task);
                } catch (InterruptedException e) {
                    ServerController.logger.log(Level.SEVERE,"Error in game queue processor", e);
                    break;
                }
            }
        });
        gameQueueProcessor.start();
    }

    //Close queue
    public void shutdown() {
        if(gameMethodQueue.offer(POISON_PILL)) {
            try {
                gameQueueProcessor.join();
            } catch (InterruptedException e) {
                ServerController.logger.log(Level.SEVERE,"Error in game queue processor", e);
            }
            gameThreadPool.shutdownNow();
        }
    }
}
