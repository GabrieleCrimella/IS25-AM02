package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.TAKE_CARD;

public class AbbandonedStation extends Card {
    private final int AliveNeeded;
    private final int daysLost;
    private final LinkedList<Box> boxesWon;
    private final LinkedList<BoxType> boxesWonTypes;
    private final CardType cardType;

    public AbbandonedStation(int level, BoxStore store, int AliveNeeded, int daysLost, LinkedList<Box> boxesWon, LinkedList<BoxType> boxesWonTypes) {
        super(level, StateCardType.DECISION);
        this.AliveNeeded = AliveNeeded;
        this.daysLost = daysLost;
        this.boxesWon = boxesWon;
        this.boxesWonTypes = boxesWonTypes;
        this.cardType = CardType.ABANDONED_STATION;
    }
    @Override
    public void addBoxWon(Box box){boxesWon.add(box);}

    @Override
    public void clearBoxWon(){
        boxesWon.clear();
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public LinkedList<Box> getBoxesWon() {
        return boxesWon;
    }

    @Override
    public LinkedList<BoxType> getBoxesWonTypes() {
        return boxesWonTypes;
    }

    @Override
    public void choiceBox(Game game, Player player, boolean choice){
        if(player.getSpaceship().calculateNumAlive() >= AliveNeeded && choice){ //se ho abbastanza giocatori per salire sulla nave
            setStateCard(StateCardType.BOXMANAGEMENT);
            game.getGameboard().move((-1)*daysLost, player);
        }
        else {
            game.nextPlayer();
        }
    }

    @Override
    public void moveBox(Game game, Player player, List<Box> start, List<Box> end, BoxType boxType, boolean on){
        if(on) {
            Iterator<Box> it = start.iterator();
            while (it.hasNext()) {
                Box box = it.next();
                if (box.getType().equals(boxType)) {
                    it.remove();
                    end.add(box);
                    break;
                }
            }
        }
        else {
            game.getCurrentCard().setStateCard(StateCardType.FINISH);
            game.getCurrentState().setPhase(TAKE_CARD);
        }
    }

}
