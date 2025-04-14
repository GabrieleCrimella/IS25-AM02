package it.polimi.ingsw.is25am02.network.rmi.client;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.State;
import it.polimi.ingsw.is25am02.modelDuplicateView.Player;
import it.polimi.ingsw.is25am02.network.ConnectionClient;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.view.ConsoleClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;
import java.util.Scanner;


public class RmiClient extends UnicastRemoteObject implements VirtualView, ConnectionClient {
    VirtualServer server;
    ConsoleClient console;

    public RmiClient() throws RemoteException {
        super();
    }

    public void startConnection() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("192.168.178.20");
        System.out.println("Tentativo di lookup...");
        server = (VirtualServer) registry.lookup("RMIServer");
        System.out.println("Lookup riuscito...");

        System.out.println(">> RMI Client ready.");
    }

    public void closeConnection() throws RemoteException {
        UnicastRemoteObject.unexportObject(this, true);
        System.out.println(">> RMI Client stopped.");
    }

    public void setView(ConsoleClient choice) {
        console = choice;
    }

    public VirtualServer getServer() { return server; }

    public ConsoleClient getConsole() { return console; }


    //todo da rivedere
    public void startProcessing() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Comando: ");
        }
    }

    @Override
    public void reportError(String details) throws RemoteException {
        console.reportError(details);
    }

    @Override
    public void displayMessage(String details) throws Exception {

    }

    @Override
    public void showUpdateEverything(Optional<it.polimi.ingsw.is25am02.model.Player> player1, Optional<it.polimi.ingsw.is25am02.model.Player> player2, Optional<it.polimi.ingsw.is25am02.model.Player> player3, Optional<it.polimi.ingsw.is25am02.model.Player> player4, Gameboard gameboard, Card currentCard, State state) throws RemoteException {
        it.polimi.ingsw.is25am02.modelDuplicateView.enumeration.StateGameType phaseV = it.polimi.ingsw.is25am02.modelDuplicateView.enumeration.StateGameType.valueOf(state.getPhase().name());

        it.polimi.ingsw.is25am02.modelDuplicateView.Card cardV = new it.polimi.ingsw.is25am02.modelDuplicateView.Card(currentCard.getLevel(), currentCard.getStateCard());

        if (player1.isPresent()) {
            Player player1V = new Player(player1.get().getSpaceship().getSpaceshipIterator().getSpaceshipBoard(), player1.get().getNickname(), player1.get().getColor(), player1.get().getStatePlayer(), player1.get().getNumDeck(), player1.get().getLobbyId());
            if (state.getCurrentPlayer().equals(player1)){
                it.polimi.ingsw.is25am02.modelDuplicateView.State stateV = new it.polimi.ingsw.is25am02.modelDuplicateView.State(cardV, player1V, phaseV);
            }
        }
        if (player2.isPresent()){
            Player player2V = new Player(player2.get().getSpaceship().getSpaceshipIterator().getSpaceshipBoard(), player2.get().getNickname(), player2.get().getColor(), player2.get().getStatePlayer(), player2.get().getNumDeck(), player2.get().getLobbyId());
            if (state.getCurrentPlayer().equals(player2)){
                it.polimi.ingsw.is25am02.modelDuplicateView.State stateV = new it.polimi.ingsw.is25am02.modelDuplicateView.State(cardV, player2V, phaseV);
            }
        }
        if (player3.isPresent()){
            Player player3V = new Player(player3.get().getSpaceship().getSpaceshipIterator().getSpaceshipBoard(), player3.get().getNickname(), player3.get().getColor(), player3.get().getStatePlayer(), player3.get().getNumDeck(), player3.get().getLobbyId());
            if (state.getCurrentPlayer().equals(player3)){
                it.polimi.ingsw.is25am02.modelDuplicateView.State stateV = new it.polimi.ingsw.is25am02.modelDuplicateView.State(cardV, player3V, phaseV);
            }
        }
        if (player4.isPresent()){
            Player player4V = new Player(player4.get().getSpaceship().getSpaceshipIterator().getSpaceshipBoard(), player4.get().getNickname(), player4.get().getColor(), player4.get().getStatePlayer(), player4.get().getNumDeck(), player4.get().getLobbyId());
            if (state.getCurrentPlayer().equals(player4)){
                it.polimi.ingsw.is25am02.modelDuplicateView.State stateV = new it.polimi.ingsw.is25am02.modelDuplicateView.State(cardV, player4V, phaseV);
            }
        }

        it.polimi.ingsw.is25am02.modelDuplicateView.Gameboard gameboardV = new it.polimi.ingsw.is25am02.modelDuplicateView.Gameboard(gameboard.getPositions(), gameboard.getDice(), gameboard.getHourGlassFlip());

    }
}
