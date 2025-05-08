package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.InitialCard;
import it.polimi.ingsw.is25am02.utils.enumerations.StateGameType;

public class State {
    private Card currentCard;
    private Player currentPlayer;
    private StateGameType phase;

    public State(Player player) {
        this.phase = StateGameType.BUILD;
        this.currentCard = new InitialCard(1, "");
        this.currentPlayer = player;
        currentPlayer.onGameStateUpdate(StateGameType.BUILD);
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public StateGameType getPhase() {
        return phase;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setPhase(StateGameType phase) {
        this.phase = phase;
        currentPlayer.onGameStateUpdate(phase);
    }
}
