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
    private List<CardV> deck;
    private ConsoleClient console;


    public GameV(List<PlayerV> players, int level, GameboardV globalBoard, StateV currentState, boolean buildTimeIsOver) {
        this.players = players;
        this.level = level;
        this.globalBoard = globalBoard;
        this.currentState = currentState;
        this.buildTimeIsOver = buildTimeIsOver;
    }

    public List<CardV> getDeck() {
        return deck;
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
            //throw new IllegalPhaseException("the time for building is over, call finishedSpaceship");
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
                //throw new IllegalStateException("Wrong player state, expected state : " + statePlayer + ", actual state : " + playerV.getStatePlayer());
                console.reportError("error.state", null);
                return false;
            }
            if (!getCurrentCard().getStateCard().equals(stateCard)) {
                console.reportError("error.state", null);
                //throw new IllegalStateException("Wrong card state, expected state : " + stateCard + ", actual state : " + getCurrentCard().getStateCard());
                return false;
            }
            if (!getCurrentState().getPhase().equals(stateGame)) {
                console.reportError("error.state", null);
                //throw new IllegalStateException("Wrong game state, expected state : " + stateGame + "  actual state : " + getCurrentState().getPhase());
                return false;
            }
            return true;
        }
        console.reportError("error.playernull", null);
        return false;


    }

    public boolean currentTileControl(PlayerV playerV) {
        if(playerV!=null){
            if (playerV.getCurrentTile() != null) {
                console.reportError("error.viewing", null);
                //throw new AlreadyViewingException("Wrong (CurrentTile != null), Player : " + player.getNickname() + ", actual tile : " + player.getSpaceship().getCurrentTile());
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
                //todo message
                //throw new IllegalPhaseException("the player " + player.getNickname() + " is not allowed to see the minidecks");
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
                //todo manda messaggio

                //throw new TileException("No Tile in position ( " + pos.x() + ", " + pos.y() + " )");
                return false;
            }
            if (!playerV.getSpaceshipBoard()[pos.x()][pos.y()].get().getType().equals(TileType.CABIN)) {
                //todo manda messaggio
                //throw new TileException("Different TileType, expected " + TileType.CABIN + ", actual " + player.getSpaceship().getTile(pos.x(), pos.y()).get().getType());
                return false;
            }
            if (!(playerV.getSpaceshipBoard()[pos.x()][pos.y()].get().getNumHumans()==0 && playerV.getSpaceshipBoard()[pos.x()][pos.y()].get().getNumPAliens()==0 && playerV.getSpaceshipBoard()[pos.x()][pos.y()].get().getNumBAliens()==0)) {
                //todo manda messaggio
                //throw new TileException("Cabin already full");
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
                //todo manda messaggio
                //throw new IllegalStateException("Wrong leader, actual leader : " + getCurrentState().getCurrentPlayer());
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
                //todo manda messaggio
                //throw new TileException("Tile (" + pos.x() + " " + pos.y() + ") from player " + player.getNickname() +" doesn't possess Type: " + type);
                return false;
            } else if (playerV.getSpaceshipBoard()[pos.x()][ pos.y()].isEmpty()) {
                //todo manda messaggio
                //throw new TileException("No Tile in position ( " + pos.x() + ", " + pos.y() + " ) from player " + player.getNickname());
                return false;
            }
            return true;
        }
        console.reportError("error.playernull", null);
        return false;
    }

}


