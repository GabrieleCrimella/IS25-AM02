package it.polimi.ingsw.is25am02.view.modelDuplicateView;



import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Hourglass;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.util.List;
import java.util.Set;

public class GameV {
    private String gameName;
    private final List<PlayerV> players;
    private final int level;
    private final GameboardV globalBoard;
    private final StateV currentState;
    private Set<TileV> heapTilesV;
    private int hourglassV;
    private int diceV;

    public GameV(List<PlayerV> players, int level, GameboardV globalBoard, StateV currentState) {
        this.players = players;
        this.level = level;
        this.globalBoard = globalBoard;
        this.currentState = currentState;
    }

    public Set<TileV> getHeapTilesV() {
        return heapTilesV;
    }

    public void setHourglassV(int hourglassV) {
        this.hourglassV = hourglassV;
    }

    public void setDiceV(int diceV) {
        this.diceV = diceV;
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


