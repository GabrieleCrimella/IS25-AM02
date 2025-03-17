package it.polimi.ingsw.is25am02.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Gameboard {
    private final int numStep;
    private final int[] startingPosition;
    HashMap<Player,Integer> positions;
    Dice dice;

    //chidi se va bene mettere
    public Gameboard(int level) {
        if ( level == 0 ){
            this.numStep = 18;
            startingPosition = new int[] {0, 1, 2, 4};
        }
        else if (level == 1){
            this.numStep = 18;
            startingPosition = new int[] {0,1,2,4};
        }
        else{
            this.numStep = 24;
            startingPosition = new int[] {0,1, 3, 6};

        }

        this.positions = new HashMap<>();
        this.dice = new Dice();

    }

    public int getNumStep() {
        return numStep;
    }

    public int[] getStartingPosition() {
        return startingPosition;
    }

    public HashMap<Player,Integer> getPositions() {
        return positions;
    }

    public Dice getDice() {
        return dice;
    }

    public void initializeGameBoard(List<Player> orderedPlayers) {
        //la lista passata è una lista ordinata in base a quando ogni player ha finito la nave.
        //Il player che ha finito per primo è primo nella lista.
        for (int i = 0; i < orderedPlayers.size(); i++) {
            positions.put(orderedPlayers.get(i), startingPosition[orderedPlayers.size()-i-1]);
        }
    }

    public void move(int step, Player player) {
        int initial_position = positions.get(player);
        int target_position = initial_position + step;
        if (step > 0) {
            for (int i = initial_position+1; i <= target_position; i++) {
                if (positions.containsValue(i)) {
                    target_position++;
                }
            }
        }
        if (step < 0) {
            for (int i = initial_position-1; i >= target_position; i--) {
                if (positions.containsValue(i)) {
                    target_position--;
                }
            }
        }

        positions.put(player, target_position);
    }

    public LinkedList<Player> getRanking() {
        LinkedList<Player> ranking = new LinkedList<>();
        ranking.addAll(positions.keySet()); //metto tutti i giocatori nella lista
        //ordino i giocatori in base alla posizione sulla board
        ranking.sort((p1, p2) -> Integer.compare(positions.get(p2), positions.get(p1)));
        return ranking;
    }
}
