package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

import java.util.LinkedList;
import java.util.List;

public class AbandonedStation extends Card_with_box {
    private BoxStore store;
    private final int humanNeeded;
    private final int daysLost;
    private LinkedList<Box> boxesWon;
    private StateCardType stateCardType;

    public AbandonedStation(int level, BoxStore store, int humanNeeded, int daysLost, LinkedList<Box> boxesWon) {
        super(level, store);
        this.humanNeeded = humanNeeded;
        this.daysLost = daysLost;
        this.boxesWon = boxesWon;
        this.stateCardType=StateCardType.DECISION;
    }

    public AbandonedStation createCard(){
        //Here the code for reading on file the card's values
        return new AbandonedStation(getLevel(), store, humanNeeded, daysLost, boxesWon);
    }

    List<Box> choiceBox(Game game, Player p, boolean choice){
        if (stateCardType != StateCardType.DECISION) {
            throw new IllegalStateException();
        }

        if(choice){
            stateCardType = StateCardType.BOXMANAGEMENT;
            if(p.getSpaceship().crewMember()<humanNeeded){//se gli uomini richiesti sono maggiori di quelli che ha il giocatore
                throw new IllegalArgumentException("Non hai abbastanza crew");
            }
            moveBox(p.getSpaceship().get,boxesWon);//todo
            p.getSpaceship().boxManage();
            game.getGameboard().move(daysLost, p);
            game.playNextCard();

        }
        else{
            game.nextPlayer();
        }
        return null;
    }
    void moveBox(List<Box> start, List<Box> end, BoxType type){
//todo
    }

}
