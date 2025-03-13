package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.tiles.Cabin;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import static it.polimi.ingsw.is25am02.model.TileType.CABIN;

public class AbandonedShip extends Card{
    private final int humanLost;
    private final int creditWin;
    private final int flyBack;
    private StateCardType stateCardType;
    private int leftHuman;//va decrementato per tenere conto degli umani che sono da togliere
    //ho le due fasi di DECISION e CHOICE ATTRIBUTES
    public AbandonedShip(int level, int humanLost, int creditWin, int flyBack){
        super(level);
        this.humanLost = humanLost;
        this.creditWin = creditWin;
        this.flyBack = flyBack;
        this.leftHuman = humanLost;
        this.stateCardType = StateCardType.DECISION;
    }

    public AbandonedShip createCard(){
        //Here the code for reading on file the card's values
        return new AbandonedShip(humanLost, creditWin, flyBack, getLevel());
    }

    void choice(Game game, Player p, boolean choice){
        if (stateCardType != StateCardType.DECISION) {
            throw new IllegalStateException();
        }
        if (choice){
            stateCardType = StateCardType.CHOICE_ATTRIBUTES;
            p.getSpaceship().addCosmicCredits(creditWin);
            game.getGameboard().move(flyBack, p);
            for(Tile cabin : game.possibleChoice(p,CABIN)){
                if (leftHuman <=0) break;
                removeCrew((Cabin) cabin, humanLost); //todo problema di casting
                leftHuman-= humanLost; //todo non ci conviene togliere una persona alla volta?

            }
            if(leftHuman>0){//se sono uscita perchè erano finite le tiles da controllare, vuol dire che ho finito gli umani sulla nave
                throw new IllegalArgumentException("No Crew Left");
            }

            game.playNextCard();
        }
        else {
            game.nextPlayer();
        }
    }
    void removeCrew(Cabin cabin, int numCrew){
        cabin.remove(numCrew);
    }
}
