package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.tiles.Cabin;

public class AbbandonedShip extends Card{
    private final int humanLost;
    private final int creditWin;
    private final int flyBack;
    private StateCardType stateCardType;
    private int leftHuman;//va decrementato per tenere conto degli umani che sono da togliere

    public AbbandonedShip(int level, int humanLost, int creditWin, int flyBack){
        super(level);
        this.humanLost = humanLost;
        this.creditWin = creditWin;
        this.flyBack = flyBack;
        this.leftHuman = humanLost;
    }

    public AbbandonedShip createCard(){
        //Here the code for reading on file the card's values
        return new AbbandonedShip(humanLost, creditWin, flyBack, getLevel());
    }

    void choice(Player p, boolean choice){

    }
    void removeCrew(Player p, Cabin cabin, int numCrew){

    }
    public void effect(Game game, boolean choice){
            if (choice){
                game.getCurrentPlayer().getSpaceship().addCosmicCredits(creditWin);
                game.getGameboard().move(flyBack, game.getCurrentPlayer());
                game.getCurrentPlayer().getSpaceship().removeCrew(humanLost);
                game.playNextCard();
            }
            else {
                game.nextPlayer();
            }
            /*
            Anti-Pattern
            for(Player i: gb.getRanking()){
            if(i.choose()){
                i.getSpaceship().addCosmicCredits(creditWin);
                gb.move(flyBack, i);
                i.getSpaceship().removeCrew(humanLost);
                break;
            }
             */

    }
}
