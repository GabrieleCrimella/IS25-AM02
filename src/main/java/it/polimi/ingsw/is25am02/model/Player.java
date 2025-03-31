package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.model.enumerations.StatePlayerType;

import static it.polimi.ingsw.is25am02.model.enumerations.StatePlayerType.NOT_FINISHED;

public class Player {
    private final Spaceship spaceship;
    private final String nickname;
    private final PlayerColor color;
    private StatePlayerType statePlayer;
    private boolean deckAllowed;
    private int numDeck;
    private int lobbyId;

    public Player(Spaceship spaceship, String nickname, PlayerColor color) {
        this.spaceship = spaceship;
        this.nickname = nickname;
        this.color = color;
        this.statePlayer = NOT_FINISHED;
        this.deckAllowed = false;
        this.numDeck = -1;
    }

    public int getLobbyId() {
        return lobbyId;
    }

    //todo andrebbe messo nel costruttore ma vorrebbe dire sistemare tutti i test
    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerColor getColor() {
        return color;
    }

    public StatePlayerType getStatePlayer() {
        return statePlayer;
    }

    public void setStatePlayer(StatePlayerType statePlayer) {
        this.statePlayer = statePlayer;
    }

    public boolean getDeckAllowed() { return deckAllowed; }

    public void setDeckAllowed() { this.deckAllowed = true; }

    public int getNumDeck() { return numDeck; }

    public void setNumDeck(int numDeck) { this.numDeck = numDeck; }
}
