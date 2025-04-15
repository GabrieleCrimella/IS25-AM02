package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

public class Card {
    private final int level;
    private StateCardType stateCard;
    //o serve l'id/imagine correlata

    public Card(int level, StateCardType stateCard) {
        this.level = level;
        this.stateCard = stateCard;
    }

}
