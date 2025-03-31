package it.polimi.ingsw.is25am02.controller;

import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameSession implements Runnable {
    private final Game game;
    private final int lobbyId;
    private final ExecutorService executor;

    public GameSession(int lobbyId, List<Player> players, int level) {
        this.lobbyId = lobbyId;
        this.executor = Executors.newSingleThreadExecutor();
        this.game = new Game(players, level); // Avvio immediato del gioco
    }

    public void start() {
        executor.submit(this);
    }

    @Override
    public void run() {

    }

    public void endGame() {
        executor.shutdownNow();
    }

    public Game getGame() {
        return game;
    }

    public int getLobbyId() {
        return lobbyId;
    }
}
