package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.StatePlayerType;
import it.polimi.ingsw.is25am02.network.VirtualView;

import static it.polimi.ingsw.is25am02.utils.enumerations.StatePlayerType.NOT_FINISHED;

public class Player {
    private final Spaceship spaceship;
    private final String nickname;
    private final PlayerColor color;
    private final VirtualView observer;
    private StatePlayerType statePlayer;
    private boolean deckAllowed;
    private int numDeck;
    private final int lobbyId;

    public Player(Spaceship spaceship, String nickname, PlayerColor color, VirtualView observer, int lobbyId) {
        this.spaceship = spaceship;
        this.nickname = nickname;
        this.color = color;
        this.observer = observer;
        this.lobbyId = lobbyId;
        this.statePlayer = NOT_FINISHED;
        this.deckAllowed = false;
        this.numDeck = -1;
    }

    public int getLobbyId() {
        return lobbyId;
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

    public VirtualView getObserver() {return observer; }

    public StatePlayerType getStatePlayer() {
        return statePlayer;
    }

    public void setStatePlayer(StatePlayerType statePlayer) { this.statePlayer = statePlayer; }

    public boolean getDeckAllowed() { return deckAllowed; }

    public void setDeckAllowed() { this.deckAllowed = true; }

    public int getNumDeck() { return numDeck; }

    public void setNumDeck(int numDeck) { this.numDeck = numDeck; }
}
