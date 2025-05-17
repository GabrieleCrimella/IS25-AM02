package it.polimi.ingsw.is25am02.utils;

import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.network.VirtualView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Lobby implements Serializable {
    private final int id;
    private final int maxPlayers;
    private final List<Player> players;
    private final int level;
    private boolean gameStarted;

    public Lobby(int id, int maxPlayers, Player owner, VirtualView client ,int level) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.gameStarted = false;
        this.players = new ArrayList<>();
        players.add(owner);
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public void addPlayer(Player player) {
        if (players.size() < maxPlayers && !players.contains(player)) {
            players.add(player);
        }
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void startGame(){
        this.gameStarted = true;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isFull() { return players.size() == maxPlayers; }

    public List<Player> getPlayers() {
        return players;
    }

}