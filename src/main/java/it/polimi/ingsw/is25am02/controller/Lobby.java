package it.polimi.ingsw.is25am02.controller;

import it.polimi.ingsw.is25am02.model.Player;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private final int id;
    private final int maxPlayers;
    private final List<Player> players;
    private final Player owner;
    private int level;
    private boolean gameStarted;

    public Lobby(int id, int maxPlayers, Player owner, int level) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.owner = owner;
        this.players.add(owner);
        this.gameStarted = false;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public boolean addPlayer(Player player) {
        if (players.size() < maxPlayers && !players.contains(player)) {
            players.add(player);
            return true;
        }
        return false;
    }

    public void startGame(){
        this.gameStarted = true;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isFull() {
        return players.size() == maxPlayers;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
}