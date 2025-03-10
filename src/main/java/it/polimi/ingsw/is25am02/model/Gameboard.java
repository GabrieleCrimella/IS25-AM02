package it.polimi.ingsw.is25am02.model;

import java.util.HashMap;
import java.util.LinkedList;

public class Gameboard {
    private final int numStep;
    private final int[] startingPosition;
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

    public void initializeGameBoard(int[] startingPosition) {
        for (int i = 0; i < getRanking().size(); i++) {
            positions.put(getRanking().get(i), startingPosition[i]);
        }
    }

    public void move(int step, Player player) {
        int initial_position = positions.get(player);
        int target_position = initial_position+step;
        for(int i = initial_position; i <= initial_position+step; i++) {
            while(positions.containsValue(i)){
                target_position++;
            }
        }

        positions.put(player,target_position);
    }

    public LinkedList<Player> getRanking() {
        LinkedList<Player> ranking = new LinkedList<>();
        ranking.addAll(positions.keySet()); //metto tutti i giocatori nella lista
        //ordino i giocatori in base alla posizione sulla board
        ranking.sort((p1, p2) -> Integer.compare(positions.get(p2), positions.get(p1)));
        return ranking;
    }
}
