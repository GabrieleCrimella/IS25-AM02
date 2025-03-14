package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;

import java.util.ArrayList;

public class WarZone_II extends Card_with_box {
    private int level;
    private final int flyback;
    private final int boxesLost;
    private int minAlive = Integer.MAX_VALUE;
    private int minCann = Integer.MAX_VALUE;
    private int minMotor = Integer.MAX_VALUE;
    private Player lessHuman;
    private Player lessCannon;
    private Player lessMotor;
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
