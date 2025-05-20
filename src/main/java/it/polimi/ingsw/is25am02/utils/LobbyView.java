package it.polimi.ingsw.is25am02.utils;

import java.io.Serializable;
import java.util.List;

public class LobbyView implements Serializable {
    private final int id;
    private final int maxPlayers;
    private final List<String> nicknames;
    private final int level;
    private boolean gameStarted;

    public LobbyView(int id, int maxPlayers, List<String> nicknames, int level) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.gameStarted = false;
        this.nicknames = nicknames;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public int getLevel() {
        return level;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
}
