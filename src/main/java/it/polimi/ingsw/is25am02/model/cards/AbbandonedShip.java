package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.rmi.RemoteException;
import java.util.logging.Level;

import static it.polimi.ingsw.is25am02.utils.enumerations.StateGameType.TAKE_CARD;

public class AbbandonedShip extends Card{
    private final int AliveLost;
    private final int creditWin;
    private final int flyBack;
    private int AliveRemoved;    //Crew eliminate from Spaceship
    private final CardType cardType;

    //Phase: DECISION e REMOVE

    public AbbandonedShip(int level, int AliveLost, int creditWin, int flyBack, String imagepath, String comment,boolean testFlight){
        super(level,StateCardType.DECISION, imagepath, comment, testFlight);
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
            for (String nick:observers.keySet()) {
                try {
                    observers.get(nick).showCreditUpdate(player.getNickname(),player.getSpaceship().getCosmicCredits());
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method choice", e);
                }
                try {
                    observers.get(nick).showPositionUpdate(player.getNickname(),game.getGameboard().getPositions().get(player));
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method choice", e);
                }
            }
        } else {
            game.nextPlayer();
        }
    }

    @Override
    public void removeCrew(Game game, Player player, Tile cabin) throws IllegalRemoveException {
        cabin.removeCrew();
        for (String nick:observers.keySet()) {
            Coordinate pos = new Coordinate (player.getSpaceship().getSpaceshipIterator().getX(cabin), player.getSpaceship().getSpaceshipIterator().getY(cabin));
            try {
                observers.get(nick).showCrewRemoval(pos, player.getNickname());
            } catch (RemoteException e) {
                ServerController.logger.log(Level.SEVERE, "error in method removeCrew", e);
            }
        }

        AliveRemoved++;

        if (AliveRemoved == AliveLost) {
            game.getCurrentCard().setStateCard(StateCardType.FINISH);
            game.getCurrentState().setPhase(TAKE_CARD);
        }
    }
}
