package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.enumerations.StateGameType;
import it.polimi.ingsw.is25am02.model.enumerations.StatePlayerType;

import java.util.ArrayList;
import java.util.HashMap;

public class State {
    private Card currentCard;
    private Player currentPlayer;
    private HashMap<Player, StatePlayerType> state;
    private StateGameType phase;

    public void state(ArrayList<Player> players) {
        this.phase = StateGameType.START;
        this.currentCard = null;
        this.currentPlayer = players.getFirst();
        this.state = new HashMap<>();

        for(Player player : players){
            state.put(player, StatePlayerType.NOT_FINISHED);
        }
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public HashMap<Player, StatePlayerType> getState() {
        return state;
    }

    public StatePlayerType getPlayerState(Player player) {
        return state.get(player);
    }

    public StateGameType getPhase() {
        return phase;
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(state.keySet());
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setPlayerState(Player player, StatePlayerType state) {
        this.state.put(player, state);
    }

    public void setPhase(StateGameType phase) {
        this.phase = phase;
    }
}
