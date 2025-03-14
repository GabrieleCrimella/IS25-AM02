package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.Cabin;

import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.EFFECT_ON_PLAYER;
import static it.polimi.ingsw.is25am02.model.enumerations.StatePlayerType.IN_GAME;

public class AbbandonedShip extends Card{
    private final int AliveLost;
    private final int creditWin;
    private final int flyBack;
    private StateCardType stateCardType;
    private int AliveRemoved;    //Crew eliminate from Spaceship

    //Phase: DECISION e REMOVE

    public AbbandonedShip(int level, int AliveLost, int creditWin, int flyBack){
        super(level);
        this.AliveLost = AliveLost;
        this.creditWin = creditWin;
        this.flyBack = flyBack;
        this.AliveRemoved = 0;
        this.stateCardType = StateCardType.DECISION;
    }

    public AbbandonedShip createCard(){
        //Here the code for reading on file the card's values
        return new AbbandonedShip(AliveLost, creditWin, flyBack, getLevel());
    }

    void choice(Game game, Player player, boolean choice){
        if (choice){
            //Cambio stato
            stateCardType = StateCardType.REMOVE;

            //Applico effetti (Volo e Crediti)
            player.getSpaceship().addCosmicCredits(creditWin);
            game.getGameboard().move((-1)*flyBack, player);
        } else {
            game.nextPlayer();
        }
    }

    void removeCrew(Game game, Player player, Cabin cabin){
        try {
            cabin.remove(1);            //todo togliere il parametro dalla remove di cabin
            AliveRemoved++;
        } catch (IllegalStateException e) {  //todo qui sarà IllegalRemoveException()
            //gestisco eccezione non c'è equipaggio sulla cabin passata
        }

        if (AliveRemoved == AliveLost) {
            game.playNextCard();
        }
    }
}
