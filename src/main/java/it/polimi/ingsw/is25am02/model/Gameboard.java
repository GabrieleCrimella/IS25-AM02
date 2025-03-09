package it.polimi.ingsw.is25am02.model;

import java.util.HashMap;
import java.util.LinkedList;

public class Gameboard {
    private int numStep;
    private int[] startingPosition;
    HashMap<Player,Integer> positions;
    Dice dice;

    public Gameboard( int numStep, int[] startingPosition ) {
        this.numStep = numStep;
        this.startingPosition = startingPosition;
        this.positions = new HashMap<>();
        this.dice = new Dice();
    }

    public int getNumStep() {
        return numStep;
    }

    public HashMap<Player,Integer> getPositions() {
        return positions;
    }

    public Dice getDice() {
        return dice;
    }

    public void move(int step, Player player) {

    }

    public LinkedList<Player> getRanking() {
        return null;
    }
}
