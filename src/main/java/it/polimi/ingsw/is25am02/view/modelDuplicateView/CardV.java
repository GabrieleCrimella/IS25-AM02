package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;

public class CardV {
    private StateCardType stateCard;
    private final String imagePath;

    public CardV(StateCardType stateCard, String imagePath) {
        this.stateCard = stateCard;
        this.imagePath = imagePath;
    }

    public void setStateCard(StateCardType stateCard) {
        this.stateCard = stateCard;
    }

    public String getImagePath() {
        return imagePath;
    }

    public StateCardType getStateCard() {
        return stateCard;
    }

}
