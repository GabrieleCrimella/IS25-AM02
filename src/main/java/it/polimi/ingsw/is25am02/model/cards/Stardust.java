package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateGameType;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;

public class Stardust extends Card {

    private final CardType cardType;

    public Stardust(int level, String imagepath,String comment, boolean testFlight) {
        super(level, StateCardType.DECISION, imagepath,comment,testFlight);
        this.cardType = CardType.STARDUST;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void effect(Game game){
        //Iterator<Player> it = game.getGameboard().getRanking().descendingIterator();
        LinkedList<Player> playersOrdered = new LinkedList<>();
        for (String orderedNick : getCurrentOrder()){
            playersOrdered.add(game.getPlayerFromNickname(orderedNick));
        }
        Iterator<Player> it = playersOrdered.descendingIterator();
        while (it.hasNext()) {
            Player player = it.next();
            game.getGameboard().move((-1) * player.getSpaceship().calculateExposedConnectors(), player );
            if(observers != null) {
                for (String nick : observers.keySet()) {
                    try {
                        observers.get(nick).showPositionUpdate(player.getNickname(), game.getGameboard().getPositions().get(player));
                    } catch (RemoteException e) {
                        ServerController.logger.log(Level.SEVERE, "error in method removeCrew", e);
                    }
                }
            }
        }
        setStateCard(StateCardType.FINISH);
        game.getCurrentState().setPhase(StateGameType.TAKE_CARD);
    }
}
