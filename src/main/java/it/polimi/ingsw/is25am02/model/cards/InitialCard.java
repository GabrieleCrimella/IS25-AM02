package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

public class InitialCard extends Card {

    private final CardType cardType;

    @Override
    public CardType getCardType() {
        return cardType;
    }

    public InitialCard(int level) {
        super(level, StateCardType.FINISH);
        this.cardType = CardType.INITIAL_CARD;
    }

}
