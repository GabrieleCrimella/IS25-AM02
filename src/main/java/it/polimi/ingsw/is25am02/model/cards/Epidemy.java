package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;

public class Epidemy extends Card {
    private int level;

    public Epidemy(int level) {
        super(level);
    }

    public Epidemy createCard(){
        return new Epidemy(level);
    }

    public void effect(Gameboard gb){

    }
}
