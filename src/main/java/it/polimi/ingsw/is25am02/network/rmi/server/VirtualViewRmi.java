package it.polimi.ingsw.is25am02.network.rmi.server;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Spaceship;
import it.polimi.ingsw.is25am02.model.State;
import it.polimi.ingsw.is25am02.network.VirtualView;

import java.rmi.Remote;
import java.util.Optional;

public interface VirtualViewRmi extends Remote, VirtualView {

    @Override
    void showUpdateEverything(Optional<it.polimi.ingsw.is25am02.model.Player> player1, Optional<it.polimi.ingsw.is25am02.model.Player> player2, Optional<it.polimi.ingsw.is25am02.model.Player> player3, Optional<it.polimi.ingsw.is25am02.model.Player> player4, Gameboard gameboard, Card currentCard, State state) throws Exception;

    //todo metodi della view
}
