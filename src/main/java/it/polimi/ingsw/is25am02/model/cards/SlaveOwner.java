package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Gameboard;

import java.util.ArrayList;

public class SlaveOwner extends Enemies{
    private int level;
    private int cannonPowers;
    private int daysLost;
    private int credit;
    private int humanLost;

    public SlaveOwner(int level, int cannonPowers, int daysLost, int credit, int humanLost) {
        super(level, cannonPowers, daysLost, credit);
        this.humanLost = humanLost;
    }

    public SlaveOwner createCard(){
        //Here the code for reading on file the card's values
        return new SlaveOwner(level, cannonPowers, daysLost, credit, humanLost);
    }

    public void effect(Gameboard gb){

    }
}
