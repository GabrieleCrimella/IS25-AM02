package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;

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
        ArrayList<Player> loser = new ArrayList<>();
        for(Player i : gb.getRanking()){
            if(i.getSpaceship().calculateCannonPower() > cannonPowers){
                if(i.choose()){
                    i.getSpaceship().addCosmicCredits(credit);
                    gb.move(daysLost,i);
                }
                break;
            }
            else if(i.getSpaceship().calculateCannonPower() < cannonPowers){
                loser.add(i);
            }
        }
        for(ArrayList<Integer> j : shots){
            int num = gb.getDice().pickRandomNumber();
            for(Player i : loser) {
                i.getSpaceship().calculateDamageShots(j, num);
            }
        }
    }
}
