package it.polimi.ingsw.is25am02.view.modelDuplicateView;


import it.polimi.ingsw.is25am02.view.modelDuplicateView.enumeration.StateGameType;

public class State {
    private Card currentCard;
    private Player currentPlayer;
    private StateGameType phase;

    public State(Card currentCard, Player currentPlayer, StateGameType phase) {
        this.currentCard = currentCard;
        this.currentPlayer = currentPlayer;
        this.phase = phase;
    }
}
