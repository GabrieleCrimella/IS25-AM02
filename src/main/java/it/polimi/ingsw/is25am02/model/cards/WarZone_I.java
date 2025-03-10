package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;

import java.util.ArrayList;

public class WarZone_I extends Card {
    private int level;
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
        for(Player p : gb.getRanking()){
            //quello con meno uomini
            int alive = p.getSpaceship().calculateNumAlive();
            if (alive < minAlive) {
                minAlive = alive;
                lessHuman = p;
            }
        }
        gb.move(-flyback,lessHuman);

        for(Player p : gb.getRanking()){//quello con meno motori
            //quello con meno motori
            int motors = p.getSpaceship().calculateMotorPower();
            if (motors < minMotor) {
                minMotor = motors;
                lessMotor = p;
            }
        }
        lessMotor.getSpaceship().removeCrew(humanLost);

        for(Player p : gb.getRanking()){//quello con meno cannoni
            //quello con meno cannoni
            int cannons = p.getSpaceship().calculateCannonPower();
            if (cannons < minCann) {
                minCann = cannons;
                lessCannon = p;
            }
        }
        for(ArrayList<Integer> shot: shots){
            lessCannon.getSpaceship().calculateDamageShots(shot); //todo a che serve line in calculate DamageShots se ho il dado?
        }

    }
}
