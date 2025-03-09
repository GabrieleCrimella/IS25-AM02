package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;

import java.util.ArrayList;

public class WarZone_I extends Card {
    private int level;
    private final int flyback;
    private final int humanLost;
    private ArrayList<ArrayList<Integer>> shots;

    public WarZone_I(int level, int flyback, int humanLost, ArrayList<ArrayList<Integer>> shots) {
        super(level);
        this.flyback = flyback;
        this.humanLost = humanLost;
        this.shots = shots;
    }

    public WarZone_I createCard(){
        //Here the code for reading on file the card's values
        return new WarZone_I(level, flyback, humanLost, shots);
    }

    public void effect(Gameboard gb){

    }
}
