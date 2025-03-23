package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

public class InitialCard extends Card {

    private CardType cardType;

    @Override
    public CardType getCardType() {
        return cardType;
    }

    public InitialCard(int level, StateCardType stateCard) {
        super(level, stateCard);
        this.cardType = CardType.INITIAL_CARD;
    }

}
