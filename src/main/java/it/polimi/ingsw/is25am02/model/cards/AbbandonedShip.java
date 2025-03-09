package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;

public class AbbandonedShip extends Card{
    private final int humanLost;
    private final int creditWin;
    private final int flyBack;
    private int level;

    public AbbandonedShip(int level, int humanLost, int creditWin, int flyBack){
        super(level);
        this.humanLost = humanLost;
        this.creditWin = creditWin;
        this.flyBack = flyBack;
    }

    public AbbandonedShip createCard(){
        //Here the code for reading on file the card's values
        return new AbbandonedShip(humanLost, creditWin, flyBack, level);
    }

    public void effect(Gameboard gb){
        for(Player i: gb.getRanking()){
            if(i.choose()){
                i.getSpaceship().addCosmicCredits(creditWin);
                gb.move(flyBack, i);
                i.getSpaceship().removeCrew(humanLost);
                break;
            }
        }
    }
}
