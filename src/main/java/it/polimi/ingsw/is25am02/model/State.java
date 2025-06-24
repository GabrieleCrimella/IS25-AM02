package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.model.cards.InitialCard;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.utils.enumerations.StateGameType;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class State {
    private Card currentCard;
    private Player currentPlayer;
    private StateGameType phase;
    private Game game;
    private ConcurrentHashMap<String, VirtualView> observers;

    public State(Player player, Game game) {
        this.phase = StateGameType.BUILD;
        this.currentCard = new InitialCard(1, "", "", false);
        this.currentPlayer = player;
        this.game = game;
    }

    public void setObservers(ConcurrentHashMap<String, VirtualView> observers) {
        this.observers = observers;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public StateGameType getPhase() {
        return phase;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
        if (observers != null) {
            for (String nick : observers.keySet()) {
                try {
                    observers.get(nick).showCurrentCardUpdate(currentCard.getImagePath(), currentCard.getStateCard(), currentCard.getCardType(), currentCard.getComment());
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method SetCurrentCard", e);
                }
            }
        }

    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        if (observers != null) {
            for (String nick : observers.keySet()) {
                try {
                    observers.get(nick).showCurrentPlayerUpdate(currentPlayer.getNickname());
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method setCurrentPlayer", e);
                }
            }
        }
    }

    public void setPhase(StateGameType phase) {
        this.phase = phase;
        if (observers != null) {
            for (String nick : observers.keySet()) {
                try {
                    if (phase == StateGameType.TAKE_CARD) {
                        observers.get(nick).showCurrentPlayerUpdate(game.getGameboard().getRanking().getFirst().getNickname());
                    }
                    observers.get(nick).showGameStateUpdate(phase);
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method returnTile", e);
                }
            }
        }
    }
}
