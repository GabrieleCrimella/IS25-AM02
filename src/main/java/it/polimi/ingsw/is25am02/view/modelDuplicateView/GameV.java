package it.polimi.ingsw.is25am02.view.modelDuplicateView;



import it.polimi.ingsw.is25am02.model.HeapTiles;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.exception.IllegalStateException;
import it.polimi.ingsw.is25am02.model.exception.LevelException;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateGameType;
import it.polimi.ingsw.is25am02.utils.enumerations.StatePlayerType;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

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

    public GameV(List<PlayerV> players, int level, GameboardV globalBoard, StateV currentState, boolean buildTimeIsOver, HourglassV hourglass) {
        this.players = players;
        this.level = level;
        this.globalBoard = globalBoard;
        this.currentState = currentState;
        this.buildTimeIsOver = buildTimeIsOver;
        this.hourglass = hourglass;
    }

    public HeapTileV getHeapTilesV() {
        return heapTilesV;
    }

    public void setDiceV(DiceV diceV) {
        this.diceV = diceV;
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

    public void flipHourglass(PlayerV playerV) {
        try {
            levelControl();
            buildControl();
            if (!hourglass.getRunning()) {
                stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, playerV);
                connection.getServer().flipHourglass(player);
            } else {
                throw new it.polimi.ingsw.is25am02.model.exception.IllegalStateException("Hourglass already running");
            }
        } catch (IllegalStateException | IllegalPhaseException | LevelException e) {
            try {
                player.getObserver().displayMessage(e.getMessage());
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method getLobbies");
            }
        }
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
    private void levelControl() throws LevelException {
        if (level == 0) {
            throw new LevelException("functionality for higher levels");
        }
    }

    private void buildControl() throws IllegalPhaseException {
        if (buildTimeIsOver) {
            throw new IllegalPhaseException("the time for building is over, call finishedSpaceship");
        }
    }

    public CardV getCurrentCard() {
        return currentState.getCurrentCard();
    }

    private void stateControl(StateGameType stateGame, StatePlayerType statePlayer, StateCardType stateCard, PlayerV playerV) throws IllegalStateException {
        if (!playerV.getStatePlayer().equals(statePlayer)) {
            throw new IllegalStateException("Wrong player state, expected state : " + statePlayer + ", actual state : " + playerV.getStatePlayer());
        }
        if (!getCurrentCard().getStateCard().equals(stateCard)) {
            throw new IllegalStateException("Wrong card state, expected state : " + stateCard + ", actual state : " + getCurrentCard().getStateCard());
        }
        if (!getCurrentState().getPhase().equals(stateGame)) {
            throw new IllegalStateException("Wrong game state, expected state : " + stateGame + "  actual state : " + getCurrentState().getPhase());
        }
    }

}


