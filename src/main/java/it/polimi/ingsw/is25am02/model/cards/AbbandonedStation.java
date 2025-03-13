package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;

import java.util.LinkedList;
import java.util.List;

public class AbbandonedStation extends Card_with_box {
    private BoxStore store;
    private final int humanNeeded;
    private final int daysLost;
    private LinkedList<Box> boxesWon;
    private StateCardType stateCardType;

    public AbbandonedStation(int level, BoxStore store, int humanNeeded, int daysLost, LinkedList<Box> boxesWon) {
        super(level, store);
        this.humanNeeded = humanNeeded;
        this.daysLost = daysLost;
        this.boxesWon = boxesWon;
    }

    public AbbandonedStation createCard(){
        //Here the code for reading on file the card's values
        return new AbbandonedStation(getLevel(), store, humanNeeded, daysLost, boxesWon);
    }

    List<Box> choiceBox(Player p, boolean choice){
        return null;
    }
    void moveBox(List<Box> start, List<Box> end, BoxType type){

    }

    public void effect(Game game, boolean choice){
        if(game.getCurrentPlayer().getSpaceship().crewMember() >= humanNeeded && choice){
            game.getCurrentPlayer().getSpaceship().boxManage();
            game.getGameboard().move(daysLost, game.getCurrentPlayer());
            game.playNextCard();
        }
        else {
            game.nextPlayer();
        }
        /*
            for(Player i : gb.getRanking()){
            if(i.getSpaceship().crewMember() >= humanNeeded && i.choose()){
                i.getSpaceship().boxManage();
                gb.move(daysLost, i);
                break;
            }
         */
    }
}
