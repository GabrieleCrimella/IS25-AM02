package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

import java.util.LinkedList;
import java.util.List;

public class AbbandonedStation extends Card_with_box {
    private BoxStore store;
    private final int AliveNeeded;
    private final int daysLost;
    private LinkedList<Box> boxesWon;
    private StateCardType stateCardType;

    public AbbandonedStation(int level, BoxStore store, int AliveNeeded, int daysLost, LinkedList<Box> boxesWon, StateCardType stateCardType) {
        super(level, store, stateCardType);
        this.AliveNeeded = AliveNeeded;
        this.daysLost = daysLost;
        this.boxesWon = boxesWon;
        this.stateCardType=StateCardType.DECISION;
    }

    public AbbandonedStation createCard(){
        //Here the code for reading on file the card's values
        return new AbbandonedStation(getLevel(), store, AliveNeeded, daysLost, boxesWon, stateCardType);
    }

    List<Box> choiceBox(Game game, Player player, boolean choice){
        if(player.getSpaceship().calculateNumAlive()>AliveNeeded&&choice){ //se ho abbastanza giocatori per salire sulla nave
            stateCardType=StateCardType.BOXMANAGEMENT;
            game.getGameboard().move((-1)*daysLost, player);
        }
        else
           game.nextPlayer();
        return boxesWon;
    }


}
