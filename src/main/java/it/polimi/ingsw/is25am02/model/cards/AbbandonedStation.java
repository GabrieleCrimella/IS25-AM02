package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;

import java.util.LinkedList;

public class AbbandonedStation extends Card_with_box {
    private int level;
    private BoxStore store;
    private final int humanNeeded;
    private final int daysLost;
    private LinkedList<Box> boxesWon;

    public AbbandonedStation(int level, BoxStore store, int humanNeeded, int daysLost, LinkedList<Box> boxesWon) {
        super(level, store);
        this.humanNeeded = humanNeeded;
        this.daysLost = daysLost;
        this.boxesWon = boxesWon;
    }

    public AbbandonedStation createCard(){
        //Here the code for reading on file the card's values
        return new AbbandonedStation(level, store, humanNeeded, daysLost, boxesWon);
    }

    public void effect(Gameboard gb){
        for(Player i : gb.getRanking()){
            if(i.getSpaceship().crewMember() >= humanNeeded && i.choose()){
                i.getSpaceship().boxManage();
                gb.move(daysLost, i);
                break;
            }
        }
    }
}
