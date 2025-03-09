package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;

import java.util.ArrayList;

public class WarZone_II extends Card {
    private int level;
    private final int flyback;
    private final int boxesLost;
    private ArrayList<ArrayList<Integer>> shots;

    public WarZone_II(int level, int flyback, int boxesLost, ArrayList<ArrayList<Integer>> shots) {
        super(level);
        this.flyback = flyback;
        this.boxesLost = boxesLost;
        this.shots = shots;
    }

    public WarZone_II createCard(){
        //Here the code for reading on file the card's values
        return new WarZone_II(level, flyback, boxesLost, shots);
    }

    public void effect(Gameboard gb){

    }
}
