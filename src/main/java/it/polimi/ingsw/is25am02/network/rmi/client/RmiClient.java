package it.polimi.ingsw.is25am02.network.rmi.client;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.State;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.*;
import it.polimi.ingsw.is25am02.network.ConnectionClient;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.view.ConsoleClient;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.enumeration.StateGameTypeV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class RmiClient extends UnicastRemoteObject implements VirtualView, ConnectionClient {
    VirtualServer server;
    ConsoleClient console;
    GameV gameV;

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

    public void setNickname(String nickname) { console.setNickname(nickname); }


    public VirtualServer getServer() { return server; }

    public ConsoleClient getConsole() {
        return console;
    }


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
    public void displayMessage(String details) throws RemoteException {
        console.displayMessage(details);
    }

    //todo showtileremoval, se sto rimuovendo una cabina allora tolgo anche gli umani etc
    @Override
    public void showTileRemoval(){

    }

    @Override
    public void showBatteryRemoval(Coordinate coordinate, Player p){
    //voglio mettere nella tile in coordinate nella spaceship copiata una batteria in meno
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(p.getNickname())) {
                if(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].isPresent()){
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumBattery(p.getSpaceship().getTile(coordinate.x(),coordinate.y()).get().getNumBattery());
                }

            }
        }
    }

    @Override
    public void showCrewRemoval(Coordinate coordinate, Player p){
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(p.getNickname())) {
                if(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].isPresent()){
                    if(p.getSpaceship().getTile(coordinate.x(), coordinate.y()).get().getCrew().contains(AliveType.BROWN_ALIEN)){
                        playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumBAliens(0);
                    }
                    else if(p.getSpaceship().getTile(coordinate.x(), coordinate.y()).get().getCrew().contains(AliveType.PURPLE_ALIEN)){
                        playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumPAliens(0);
                    }
                    else{
                        playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumHumans(p.getSpaceship().getTile(coordinate.x(),coordinate.y()).get().getCrew().size());
                    }

                }

            }
        }
    }

    @Override
    public void showBoxRemoval(Tile t){

    }
    @Override
    public void showCreditUpdate(Player p){
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(p.getNickname())) {
                playerv.setCredits(p.getSpaceship().getCosmicCredits());

            }
        }

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
    public void setMenuState(MenuState state) throws RemoteException {
        console.getController().setMenuState(state);
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
        StateV statev = null;
        List<PlayerV> playersV = new ArrayList<>();

        for(Player p: players){
            Optional<TileV>[][] spaceshipV = new Optional[12][12];
            for(int i = 0; i< 12; i++){
                //Optional<Tile>[] tileVect :p.getSpaceship().getSpaceshipIterator().getSpaceshipBoard()
                for(int j = 0; j < 12; j++){
                    TileV tileV = null;
                    Optional<Tile> tile = p.getSpaceship().getTile(i,j);
                    if(tile.isPresent()){
                        if(tile.get().getType().equals(TileType.CABIN)){
                            if(tile.get().getCrew().contains(AliveType.PURPLE_ALIEN)){
                                tileV = new TileV(tile.get().getType(),tile.get().getRotationType(),true,tile.get().getId(),tile.get().getNumBattery(), 0,1,0,0,0,0,0);
                            }
                            else if(tile.get().getCrew().contains(AliveType.BROWN_ALIEN)){
                                tileV = new TileV(tile.get().getType(),tile.get().getRotationType(),true,tile.get().getId(),tile.get().getNumBattery(), 0,0,1,0,0,0,0);
                            }
                            else{
                                tileV = new TileV(tile.get().getType(),tile.get().getRotationType(),true,tile.get().getId(),tile.get().getNumBattery(), tile.get().getCrew().size(),0,0,0,0,0,0);
                            }
                        }
                        else if(tile.get().getType().equals(TileType.STORAGE)||tile.get().getType().equals(TileType.SPECIAL_STORAGE)){
                            int redCount = 0;
                            int yellowCount = 0;
                            int greenCount = 0;
                            int blueCount = 0;
                            for (Box box : tile.get().getOccupation()) {
                                if (box.getType().equals(BoxType.RED)) {
                                    redCount++;
                                }
                                else if(box.getType().equals(BoxType.YELLOW)){
                                    yellowCount++;
                                }
                                else if(box.getType().equals(BoxType.GREEN)){
                                    greenCount++;
                                }
                                else {
                                    blueCount++;
                                }
                            }
                            tileV = new TileV(tile.get().getType(),tile.get().getRotationType(),true,tile.get().getId(),tile.get().getNumBattery(), 0,0,0,redCount,yellowCount,greenCount,blueCount);
                        }
                        else{
                            tileV = new TileV(tile.get().getType(),tile.get().getRotationType(),true,tile.get().getId(),tile.get().getNumBattery(), 0,0,0,0,0,0,0);

                        }
                        spaceshipV[i][j] = Optional.of(tileV);

                    }
                }
            }
            PlayerV playerv = new PlayerV(spaceshipV,p.getNickname(),p.getColor(),p.getStatePlayer(),p.getNumDeck(),p.getLobbyId());
            if(state.getCurrentPlayer().equals(p)){
                statev = new StateV(cardV,playerv,phaseV);
            }
            playersV.add(playerv);
        }

        GameboardV gameboardV = new GameboardV(gameboard.getPositions(), gameboard.getDice(), gameboard.getHourGlassFlip());

        GameV gameV = new GameV(playersV, currentCard.getLevel(), gameboardV, statev);

    }
}
