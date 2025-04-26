package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.StatePlayerType;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.util.Optional;

public class PlayerV {
    private final Optional<TileV>[][] spaceshipBoard;
    private Optional<TileV> currentTile;
    private final String nickname;
    private final PlayerColor color;
    private StatePlayerType statePlayer;
    private boolean deckAllowed; //todo bisogna aggiornare anche questo
    private int numDeck;
    private int lobbyId;
    private int credits;

    public void setCurrentTile(Optional<TileV> currentTile) {
        this.currentTile = currentTile;
    }

    public void setNumDeck(int numDeck) {
        this.numDeck = numDeck;
    }

    public PlayerV(Optional<TileV>[][] spaceshipBoard, String nickname, PlayerColor color, StatePlayerType statePlayer, int numDeck, int lobbyId) {
        this.spaceshipBoard = spaceshipBoard;
        this.nickname = nickname;
        this.color = color;
        this.statePlayer = statePlayer;
        this.numDeck = numDeck;
        this.lobbyId = lobbyId;
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

    public int getLobbyId() {
        return lobbyId;
    }
}

