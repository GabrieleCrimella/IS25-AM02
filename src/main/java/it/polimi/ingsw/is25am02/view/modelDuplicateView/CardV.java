package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import javafx.util.Pair;

import java.util.ArrayList;

public class CardV {
    private StateCardType stateCard;
    private final String imagePath;
    private final CardType cardType;
    private String comment;


    public CardV(StateCardType stateCard, String imagePath, CardType cardType, String comment) {
        this.stateCard = stateCard;
        this.imagePath = imagePath;
        this.cardType = cardType;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }
}
