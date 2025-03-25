package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.TAKE_CARD;

public class AbbandonedShip extends Card{
    private final int AliveLost;
    private final int creditWin;
    private final int flyBack;
    private int AliveRemoved;    //Crew eliminate from Spaceship
    private final CardType cardType;

    //Phase: DECISION e REMOVE

    public AbbandonedShip(int level, int AliveLost, int creditWin, int flyBack){
        super(level,StateCardType.DECISION);
        this.AliveLost = AliveLost;
        this.creditWin = creditWin;
        this.flyBack = flyBack;
        this.AliveRemoved = 0;
        this.cardType = CardType.ABANDONED_SHIP;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void choice(Game game, Player player, boolean choice){
        if (choice){
            //Cambio stato
            setStateCard(StateCardType.REMOVE);

            //Applico effetti (Volo e Crediti)
            player.getSpaceship().addCosmicCredits(creditWin);
            game.getGameboard().move((-1)*flyBack, player);
        } else {
            game.nextPlayer();
        }
    }

    @Override
    public void removeCrew(Game game, Player player, Tile cabin) throws IllegalRemoveException {
        cabin.removeCrew();
        AliveRemoved++;

        if (AliveRemoved == AliveLost) {
            game.getCurrentCard().setStateCard(StateCardType.FINISH);
            game.getCurrentState().setPhase(TAKE_CARD);
        }
    }
}
