package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;

import java.util.ArrayList;

public class SlaveOwner extends Enemies{
    private int level;
    private int cannonPowers;
    private int daysLost;
    private int credit;
    private final int humanLost;

    public SlaveOwner(int level, int cannonPowers, int daysLost, int credit, int humanLost) {
        super(level, cannonPowers, daysLost, credit);
        this.humanLost = humanLost;
    }

    public SlaveOwner createCard(){
        //Here the code for reading on file the card's values
        return new SlaveOwner(level, cannonPowers, daysLost, credit, humanLost);
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

        for(Player i : loser){
            i.getSpaceship().removeCrew(humanLost);
        }
    }
}
