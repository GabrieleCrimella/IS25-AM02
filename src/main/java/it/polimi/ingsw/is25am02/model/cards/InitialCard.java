package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

public class InitialCard extends Card {

    public InitialCard(int level, StateCardType stateCard) {
        super(level, stateCard);
    }

    public InitialCard createCard(){
        //Here the code for reading on file the card's values
        return new InitialCard(getLevel(), StateCardType.FINISH);
    }
}
