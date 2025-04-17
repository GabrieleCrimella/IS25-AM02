package it.polimi.ingsw.is25am02.view.modelDuplicateView;


import it.polimi.ingsw.is25am02.view.modelDuplicateView.enumeration.StateGameTypeV;

public class StateV {
    private CardV currentCard;
    private PlayerV currentPlayer;
    private StateGameTypeV phase;

    public StateV(CardV currentCard, PlayerV currentPlayer, StateGameTypeV phase) {
        this.currentCard = currentCard;
        this.currentPlayer = currentPlayer;
        this.phase = phase;
    }
}
