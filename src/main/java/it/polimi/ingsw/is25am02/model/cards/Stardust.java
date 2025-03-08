package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;

import java.util.ListIterator;

public class Stardust extends Card {
    private int level;

    public Stardust(int level) {
        super(level);
    }

    public Stardust createCard(){
        return new Stardust(level);
    }

    public void effect(Gameboard gb){
        ListIterator<Player> iterator = gb.getRanking().listIterator(gb.getRanking().size());
        Player i;
        while(iterator.hasPrevious()){
            i = iterator.previous();
            gb.move((-1) * i.getSpaceship().calculateExposedConnectors(), i);
        }
    }
}
