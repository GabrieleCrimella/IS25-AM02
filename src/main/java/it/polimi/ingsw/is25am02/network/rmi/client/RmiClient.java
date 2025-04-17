package it.polimi.ingsw.is25am02.network.rmi.client;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
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
import java.util.List;
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
    @Override
    public void showTileRemoval(){

    }
    @Override
    public void showBatteryRemoval(Coordinate coordinate, Player p){


    }
    @Override
    public void showCrewRemoval(Tile t){

    }
    @Override
    public void showBoxRemoval(Tile t){

    }
    @Override
    public void showCreditUpdate(){

    }
    @Override
    public void showUpdatedOthers(){

    }
    @Override
    public void showPositionsUpdate(){

    }
    @Override
    public void showHourglassUpdate(){

    }
    @Override
    public void showDiceUpdate(){

    }
    @Override
    public void showHeapTileUpdate(){

    }
    @Override
    public void showMinideckUpdate(){

    }
    @Override
    public void showCurrentCardUpdate() {

    }
    @Override
    public void showUpdateEverything(List<Player> players, Gameboard gameboard, Card currentCard, State state) throws RemoteException {
        StateGameTypeV phaseV = StateGameTypeV.valueOf(state.getPhase().name());

        CardV cardV = new CardV(currentCard.getLevel(), currentCard.getStateCard());

        for(Player p: players){
            PlayerV playerv = new PlayerV(p.getSpaceship().getSpaceshipIterator().getSpaceshipBoard(),p.getNickname(),p.getColor(),p.getStatePlayer(),p.getNumDeck(),p.getLobbyId());
            if(state.getCurrentPlayer().equals(p)){
                StateV statev = new StateV(cardV,playerv,phaseV);
            }
        }

        GameboardV gameboardV = new GameboardV(gameboard.getPositions(), gameboard.getDice(), gameboard.getHourGlassFlip());

    }
}
