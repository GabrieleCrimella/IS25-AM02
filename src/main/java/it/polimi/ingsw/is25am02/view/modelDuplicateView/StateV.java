package it.polimi.ingsw.is25am02.view.modelDuplicateView;


import it.polimi.ingsw.is25am02.utils.enumerations.StateGameType;
public class StateV {
    private CardV currentCard;
    private PlayerV currentPlayer;
    private StateGameType phase;

    public StateV(CardV currentCard, PlayerV currentPlayer, StateGameType phase) {
        this.currentCard = currentCard;
        this.currentPlayer = currentPlayer;
        this.phase = phase;
    }

    public void setCurrentPlayer(PlayerV currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setPhase(StateGameType phase) {
        this.phase = phase;
    }

    public void setCurrentCard(CardV currentCard) {
        this.currentCard = currentCard;
    }

    public CardV getCurrentCard() {
        return currentCard;
    }

    public StateGameType getPhase() {
        return phase;
    }

    public PlayerV getCurrentPlayer() {
        return currentPlayer;
    }
}
