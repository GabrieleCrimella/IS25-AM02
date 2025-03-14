package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.Cabin;

import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.EFFECT_ON_PLAYER;
import static it.polimi.ingsw.is25am02.model.enumerations.StatePlayerType.IN_GAME;

public class AbandonedShip extends Card{
    private final int AliveLost;
    private final int creditWin;
    private final int flyBack;
    private StateCardType stateCardType;
    private int AliveRemoved;    //Crew eliminate from Spaceship

    //Phase: DECISION e REMOVE

    public AbandonedShip(int level, int AliveLost, int creditWin, int flyBack){
        super(level);
        this.AliveLost = AliveLost;
        this.creditWin = creditWin;
        this.flyBack = flyBack;
        this.AliveRemoved = 0;
        this.stateCardType = StateCardType.DECISION;
    }

    public AbandonedShip createCard(){
        //Here the code for reading on file the card's values
        return new AbandonedShip(AliveLost, creditWin, flyBack, getLevel());
    }

    void choice(Game game, Player player, boolean choice){

        //Controlli di Stato
        if (stateCardType != StateCardType.DECISION || player.getStatePlayer() != IN_GAME || game.getCurrentState().getPhase() != EFFECT_ON_PLAYER) {
            throw new IllegalStateException();
        }
        else if (game.getCurrentPlayer() != player) {
            throw new IllegalStateException();   //todo qui sarà IllegalPlayerException()
        }

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

        //Controlli di Stato
        if (stateCardType != StateCardType.REMOVE || player.getStatePlayer() != IN_GAME || game.getCurrentState().getPhase() != EFFECT_ON_PLAYER) {
            throw new IllegalStateException();
        }
        else if (game.getCurrentPlayer() != player) {
            throw new IllegalStateException();   //todo qui sarà IllegalPlayerException()
        }
        else if( !player.getSpaceship().own(cabin) ){
            throw new IllegalStateException();   //todo qui sarà IllegalTileException()
        }

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
