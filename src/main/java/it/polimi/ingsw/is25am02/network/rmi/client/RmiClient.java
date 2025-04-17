package it.polimi.ingsw.is25am02.network.rmi.client;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.State;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.CardV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.GameboardV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
import it.polimi.ingsw.is25am02.network.ConnectionClient;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.view.ConsoleClient;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.StateV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.enumeration.StateGameTypeV;

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
        Registry registry = LocateRegistry.getRegistry("172.20.10.3");
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
        console.displayMessage(details);
    }

    //todo showtileremoval, se sto rimuovendo una cabina allora tolgo anche gli umani etc
    //todo showbatteryremoval
    //todo showcrewremoval
    //todo showboxremoval
    //todo showcreditupdate

    //todo showuptadeothers ????

    //todo showpositionsupdate
    //todo showhourglassupdate
    //todo showdiceupdate

    //todo showheapTilesupdate
    //todo showminideckupdate
    //todo showcurrentcardupdate

    //todo passargli la lista di players
    //todo cambia nome cose

    @Override
    public void showUpdateEverything(Optional<it.polimi.ingsw.is25am02.model.Player> player1, Optional<it.polimi.ingsw.is25am02.model.Player> player2, Optional<it.polimi.ingsw.is25am02.model.Player> player3, Optional<it.polimi.ingsw.is25am02.model.Player> player4, Gameboard gameboard, Card currentCard, State state) throws RemoteException {
        StateGameTypeV phaseV = StateGameTypeV.valueOf(state.getPhase().name());

        CardV cardV = new CardV(currentCard.getLevel(), currentCard.getStateCard());

        if (player1.isPresent()) {
            PlayerV player1V = new PlayerV(player1.get().getSpaceship().getSpaceshipIterator().getSpaceshipBoard(), player1.get().getNickname(), player1.get().getColor(), player1.get().getStatePlayer(), player1.get().getNumDeck(), player1.get().getLobbyId());
            if (state.getCurrentPlayer().equals(player1)){
                StateV stateV = new StateV(cardV, player1V, phaseV);
            }
        }
        if (player2.isPresent()){
            PlayerV player2V = new PlayerV(player2.get().getSpaceship().getSpaceshipIterator().getSpaceshipBoard(), player2.get().getNickname(), player2.get().getColor(), player2.get().getStatePlayer(), player2.get().getNumDeck(), player2.get().getLobbyId());
            if (state.getCurrentPlayer().equals(player2)){
                StateV stateV = new StateV(cardV, player2V, phaseV);
            }
        }
        if (player3.isPresent()){
            PlayerV player3V = new PlayerV(player3.get().getSpaceship().getSpaceshipIterator().getSpaceshipBoard(), player3.get().getNickname(), player3.get().getColor(), player3.get().getStatePlayer(), player3.get().getNumDeck(), player3.get().getLobbyId());
            if (state.getCurrentPlayer().equals(player3)){
                StateV stateV = new StateV(cardV, player3V, phaseV);
            }
        }
        if (player4.isPresent()){
            PlayerV player4V = new PlayerV(player4.get().getSpaceship().getSpaceshipIterator().getSpaceshipBoard(), player4.get().getNickname(), player4.get().getColor(), player4.get().getStatePlayer(), player4.get().getNumDeck(), player4.get().getLobbyId());
            if (state.getCurrentPlayer().equals(player4)){
                StateV stateV = new StateV(cardV, player4V, phaseV);
            }
        }

        GameboardV gameboardV = new GameboardV(gameboard.getPositions(), gameboard.getDice(), gameboard.getHourGlassFlip());

    }
}
