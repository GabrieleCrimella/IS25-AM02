package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;

public class CardV {
    private StateCardType stateCard;
    private final String imagePath;
    private final CardType cardType;

    public CardV(StateCardType stateCard, String imagePath, CardType cardType) {
        this.stateCard = stateCard;
        this.imagePath = imagePath;
        this.cardType = cardType;
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

    public CardType getCardType() {
        return cardType;
    }
}
