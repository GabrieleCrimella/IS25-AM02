package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;

public class CardV {
    private final int level;
    private StateCardType stateCard;
    private final String imagePath;

    public CardV(int level, StateCardType stateCard, String imagePath) {
        this.level = level;
        this.stateCard = stateCard;
        this.imagePath = imagePath;
    }

    public StateCardType getStateCard() {
        return stateCard;
    }

}
