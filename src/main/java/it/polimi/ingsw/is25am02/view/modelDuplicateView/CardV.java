package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;

public class CardV {
    private final int level;
    private StateCardType stateCard;
    //o serve l'id/imagine correlata

    public CardV(int level, StateCardType stateCard) {
        this.level = level;
        this.stateCard = stateCard;
    }

    public StateCardType getStateCard() {
        return stateCard;
    }

}
