package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

import java.util.ArrayList;

public class WarZone_I extends Card {
    private final int flyback;
    private final int humanLost;
    private int minAlive = Integer.MAX_VALUE;
    private int minCann = Integer.MAX_VALUE;
    private int minMotor = Integer.MAX_VALUE;
    private Player lessHuman;
    private Player lessCannon;
    private Player lessMotor;
    private ArrayList<ArrayList<Integer>> shots;

    public WarZone_I(int level, int flyback, int humanLost, ArrayList<ArrayList<Integer>> shots) {
        super(level, StateCardType.DECISION);
        this.flyback = flyback;
        this.humanLost = humanLost;
        this.shots = shots;
    }

    public WarZone_I createCard(){
        //Here the code for reading on file the card's values
        return new WarZone_I(getLevel(), flyback, humanLost, shots);
    }

    public void effect(Gameboard gb){
    }
}
