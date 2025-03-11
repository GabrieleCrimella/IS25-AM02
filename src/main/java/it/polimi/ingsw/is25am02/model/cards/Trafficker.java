package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;

import java.util.ArrayList;

public class Trafficker extends Card_with_box{
    private int level;
    private BoxStore store;
    private final int cannonPowers;
    private final int daysLost;
    private final int boxesLost;
    private ArrayList<Box> boxesWon;

    public Trafficker(int level, BoxStore store, int cannonPowers, int daysLost, int boxesLost, ArrayList<Box> boxesWon) {
        super(level, store);
        this.cannonPowers = cannonPowers;
        this.daysLost = daysLost;
        this.boxesLost = boxesLost;
        this.boxesWon = boxesWon;
    }

    public Trafficker createCard(){
        //Here the code for reading on file the card's values
        return new Trafficker(level, store, cannonPowers, daysLost, boxesLost, boxesWon);
    }

    public void effect(Gameboard gb){

    }
}
