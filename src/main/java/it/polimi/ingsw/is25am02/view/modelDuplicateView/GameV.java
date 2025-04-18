package it.polimi.ingsw.is25am02.view.modelDuplicateView;



import java.util.List;

public class GameV {
    private String gameName;
    private final List<PlayerV> players;
    private final int level;
    private final GameboardV globalBoard;
    private final StateV currentState;

    public GameV(List<PlayerV> players, int level, GameboardV globalBoard, StateV currentState) {
        this.players = players;
        this.level = level;
        this.globalBoard = globalBoard;
        this.currentState = currentState;
    }

    public StateV getCurrentState() {
        return currentState;
    }

    public GameboardV getGlobalBoard() {
        return globalBoard;
    }

    public int getLevel() {
        return level;
    }

    public List<PlayerV> getPlayers() {
        return players;
    }

    public String getGameName() {
        return gameName;
    }
}


