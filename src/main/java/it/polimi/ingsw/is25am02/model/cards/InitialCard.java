package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;

public class InitialCard extends Card {

    private final CardType cardType;

    @Override
    public CardType getCardType() {
        return cardType;
    }

    public InitialCard(int level, String imagepath) {
        super(level, StateCardType.FINISH, imagepath);
        this.cardType = CardType.INITIAL_CARD;
    }

}
