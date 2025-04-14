package it.polimi.ingsw.is25am02.network;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.State;

import java.rmi.Remote;
import java.util.Optional;

/*
Interface that defines the methods used by the server to notify clients of state changes.
*/
public interface VirtualView extends Remote {
    void reportError(String details) throws Exception;

    void displayMessage(String details) throws Exception;

    void showUpdateEverything(Optional<it.polimi.ingsw.is25am02.model.Player> player1, Optional<it.polimi.ingsw.is25am02.model.Player> player2, Optional<it.polimi.ingsw.is25am02.model.Player> player3, Optional<it.polimi.ingsw.is25am02.model.Player> player4, Gameboard gameboard, Card currentCard, State state) throws Exception;
    //todo all the showUpdate!!!
}
