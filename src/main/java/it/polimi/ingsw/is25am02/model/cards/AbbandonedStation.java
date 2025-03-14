package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.is25am02.model.enumerations.StateCardType.DECISION;

public class AbbandonedStation extends Card_with_box {
    private final int AliveNeeded;
    private final int daysLost;
    private LinkedList<Box> boxesWon;

    public AbbandonedStation(int level, BoxStore store, int AliveNeeded, int daysLost, LinkedList<Box> boxesWon) {
        super(level, store, StateCardType.DECISION);
        this.AliveNeeded = AliveNeeded;
        this.daysLost = daysLost;
        this.boxesWon = boxesWon;
    }

    public AbbandonedStation createCard(){
        //Here the code for reading on file the card's values
        return new AbbandonedStation(getLevel(), getBoxStore(), AliveNeeded, daysLost, boxesWon);
    }

    List<Box> choiceBox(Game game, Player player, boolean choice){
        if(player.getSpaceship().calculateNumAlive()>AliveNeeded&&choice){ //se ho abbastanza giocatori per salire sulla nave
            setStateCard(StateCardType.BOXMANAGEMENT);
            game.getGameboard().move((-1)*daysLost, player);
        }
        else
           game.nextPlayer();
        return boxesWon;
    }

    void movebox(Game game, Player player, List<Box> start, List<Box> end, Box box, boolean on){
        if(on) {
            start.remove(box);
            end.add(box);
        }
        else {
            game.playNextCard();
        }
    }

}
