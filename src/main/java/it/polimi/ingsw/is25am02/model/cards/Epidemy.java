package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

public class Epidemy extends Card {
    private int level;

    public Epidemy(int level) {
        super(level, StateCardType.DECISION);
    }

    public Epidemy createCard(){
        return new Epidemy(level);
    }

    public void effect(Game game){
    }
}
