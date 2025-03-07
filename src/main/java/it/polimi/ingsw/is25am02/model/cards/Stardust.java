package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;

public class Stardust extends Card {
    private int level;

    public Stardust(int level) {
        super(level);
    }

    public Stardust createCard(){
        return new Stardust(level);
    }

    public void effect(Gameboard gb){

    }
}
