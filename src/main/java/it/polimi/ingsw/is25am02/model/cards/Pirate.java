package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Gameboard;

import java.util.ArrayList;

public class Pirate extends Enemies{
    private int level;
    private int cannonPowers;
    private int daysLost;
    private int credit;
    private ArrayList<ArrayList<Integer>> shots;

    public Pirate(int level, int cannonPowers, int daysLost, int credit, ArrayList<ArrayList<Integer>> shots) {
        super(level, cannonPowers, daysLost, credit);
        this.shots = shots;
    }

    public Pirate createCard(){
        //Here the code for reading on file the card's values
        return new Pirate(level, cannonPowers, daysLost, credit, shots);
    }

    public void effect(Gameboard gb){

    }
}
