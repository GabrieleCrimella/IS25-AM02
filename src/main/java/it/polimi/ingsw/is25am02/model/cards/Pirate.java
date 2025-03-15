package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.is25am02.model.enumerations.StateCardType.DECISION;

public class Pirate extends Enemies{
    private ArrayList<ArrayList<Integer>> shots;

    public Pirate(int level, int cannonPowers, int daysLost, int credit, ArrayList<ArrayList<Integer>> shots) {
        super(level, cannonPowers, daysLost, credit, DECISION);
        this.shots = shots;
    }

    public Pirate createCard(){
        //Here the code for reading on file the card's values
        return new Pirate(getLevel(), getCannonPowers(), getDaysLost(), getCredit(), shots);
    }
}
