package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;

import java.util.ArrayList;

public class Planet extends Card_with_box{
    private int level;
    private BoxStore store;
    private final int daysLost;
    private final ArrayList<ArrayList<Box>> planetOffers;

    public Planet(int level, BoxStore store, int daysLost, ArrayList<ArrayList<Box>> planetOffers) {
        super(level, store);
        this.daysLost = daysLost;
        this.planetOffers = planetOffers;
    }

    public Planet createCard(){
        //Here the code for reading on file the card's values
        return new Planet(level, store, daysLost, planetOffers);
    }

    public void effect(Gameboard gb){

    }
}
