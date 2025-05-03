package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.StatePlayerType;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.util.Optional;

public class PlayerV {
    private Optional<TileV>[][] spaceshipBoard;
    private boolean[][] spaceshipMask;
    private Optional<TileV> currentTile;
    private final String nickname;
    private final PlayerColor color;
    private StatePlayerType statePlayer;
    private boolean deckAllowed; //todo bisogna aggiornare anche questo
    private int numDeck;
    private int credits;

    public void setCurrentTile(Optional<TileV> currentTile) {
        this.currentTile = currentTile;
    }

    public void setNumDeck(int numDeck) {
        this.numDeck = numDeck;
    }

    public PlayerV(Optional<TileV>[][] spaceshipBoard, String nickname, PlayerColor color, boolean[][] mask) {
        this.spaceshipBoard = spaceshipBoard;
        this.nickname = nickname;
        this.color = color;
        this.statePlayer = StatePlayerType.NOT_FINISHED;
    }

    public boolean[][] getSpaceshipMask() {
        return spaceshipMask;
    }

    public void setSpaceshipMask(boolean[][] spaceshipMask) {
        this.spaceshipMask = spaceshipMask;
    }

    public void setSpaceshipBoardTile(TileV tileV, Coordinate coordinate) {
        spaceshipBoard[coordinate.x()][coordinate.y()] = Optional.of(tileV);
    }

    public Optional<TileV>[][] getSpaceshipBoard() {
        return spaceshipBoard;
    }

    public Optional<TileV> getCurrentTile() {
        return currentTile;
    }

    public boolean getDeckAllowed() {
        return deckAllowed;
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public StatePlayerType getStatePlayer() {
        return statePlayer;
    }

    public int getNumDeck() {
        return numDeck;
    }

}

