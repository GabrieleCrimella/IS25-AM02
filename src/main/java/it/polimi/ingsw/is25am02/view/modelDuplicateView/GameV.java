package it.polimi.ingsw.is25am02.view.modelDuplicateView;




import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.exception.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalStateException;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateGameType;
import it.polimi.ingsw.is25am02.utils.enumerations.StatePlayerType;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;
import it.polimi.ingsw.is25am02.view.ConsoleClient;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GameV {
    private String gameName;
    private final List<PlayerV> players;
    private final int level;
    private final GameboardV globalBoard;
    private final StateV currentState;
    private HeapTileV heapTilesV;
    private DiceV diceV;
    private boolean buildTimeIsOver; //todo bisogna aggiornarlo in una update
    private HourglassV hourglass;
    private CardDeckV deck;
    private ConsoleClient console;


    public GameV(List<PlayerV> players, int level, GameboardV globalBoard, StateV currentState, boolean buildTimeIsOver, ConsoleClient console, CardDeckV deck) {
        this.players = players;
        this.level = level;
        this.globalBoard = globalBoard;
        this.currentState = currentState;
        this.buildTimeIsOver = buildTimeIsOver;
        this.console = console;
        this.heapTilesV = new HeapTileV();
        this.deck = deck;
        this.buildTimeIsOver = false;
    }

    public void setBuildTimeIsOver(boolean buildTimeIsOver) {
        this.buildTimeIsOver = buildTimeIsOver;
    }


    public HeapTileV getHeapTilesV() {
        return heapTilesV;
    }

    public void setHourglass(HourglassV hourglass) {
        this.hourglass = hourglass;
    }

    public HourglassV getHourglass() {
        return hourglass;
    }

    public DiceV getDiceV() {
        return diceV;
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

    //controlli
    public boolean levelControl(){
        if (level == 0) {
            console.reportError("error.level", null);
            return false;
        }
        return true;
    }

    public boolean buildControl() {
        if (buildTimeIsOver) {
            console.reportError("error.buildTimeIsOver", null);
            return false;
        }
        return true;
    }

    public CardV getCurrentCard() {
        return currentState.getCurrentCard();
    }

    public boolean stateControl(StateGameType stateGame, StatePlayerType statePlayer, StateCardType stateCard, PlayerV playerV) {
        if(playerV!=null){
            if (!playerV.getStatePlayer().equals(statePlayer)) {
                HashMap<String,String> params = new HashMap<>();
                params.put("correctstate",statePlayer.toString());
                params.put("wrongstate", playerV.getStatePlayer().toString());
                console.reportError("error.statePlayer", params);
                return false;
            }
            if (!getCurrentCard().getStateCard().equals(stateCard)) {
                HashMap<String,String> params = new HashMap<>();
                params.put("correctstate",stateCard.toString());
                params.put("wrongstate", getCurrentCard().getStateCard().toString());
                console.reportError("error.stateCard", null);
                return false;
            }
            if (!getCurrentState().getPhase().equals(stateGame)) {
                console.reportError("error.phase", null);
                return false;
            }
            return true;
        }
        console.reportError("error.playernull", null);
        return false;


    }

    public boolean currentTileControl(PlayerV playerV) {
        if(playerV!=null){
            if (!playerV.getCurrentTile().isEmpty()) {
                console.reportError("error.viewing", null);
                return false;
            }
            return true;
        }
        console.reportError("error.playernull", null);
        return false;
    }

    public boolean deckAllowedControl(PlayerV player) {
        if (player!=null){
            if (!player.getDeckAllowed()) {
                console.reportError("error.deckAllowed", null);
                return false;
            }
            return true;
        }
        console.reportError("error.playernull", null);
        return false;
    }

    public boolean cabinControl(PlayerV playerV, Coordinate pos) {
        if (playerV!=null){
            if (playerV.getSpaceshipBoard()[pos.x()][pos.y()].isEmpty()) {
                console.reportError("error.tilePosition", null);
                return false;
            }
            if (!playerV.getSpaceshipBoard()[pos.x()][pos.y()].get().getType().equals(TileType.CABIN)) {
                HashMap<String,String> params = new HashMap<>();
                params.put("tType",TileType.CABIN.toString());
                console.reportError("error.tile", params);
                return false;
            }
            if (!(playerV.getSpaceshipBoard()[pos.x()][pos.y()].get().getNumHumans()==0 && playerV.getSpaceshipBoard()[pos.x()][pos.y()].get().getNumPAliens()==0 && playerV.getSpaceshipBoard()[pos.x()][pos.y()].get().getNumBAliens()==0)) {
                console.reportError("error.fullCabin", null);
                return false;
            }
            return true;
        }
        console.reportError("error.playernull", null);
        return false;
    }

    public boolean currentPlayerControl(PlayerV playerV) {
        if(playerV!=null){
            if (!playerV.equals(getCurrentState().getCurrentPlayer())) {
                console.reportError("error.wrongLeader",null);
                return false;
            }
            return true;
        }
        console.reportError("error.playernull", null);
        return false;
    }

    public boolean typeControl(PlayerV playerV, Coordinate pos, TileType type) {
        if (playerV!=null){
            if (playerV.getSpaceshipBoard()[pos.x()][ pos.y()].isPresent() && !playerV.getSpaceshipBoard()[pos.x()][ pos.y()].get().getType().equals(type)) {
                HashMap<String,String> params = new HashMap<>();
                params.put("tType",type.toString());
                console.reportError("error.tile", null);
                return false;
            } else if (playerV.getSpaceshipBoard()[pos.x()][ pos.y()].isEmpty()) {
                console.reportError("error.tilePosition",null);
                return false;
            }
            return true;
        }
        console.reportError("error.playernull", null);
        return false;
    }

}


