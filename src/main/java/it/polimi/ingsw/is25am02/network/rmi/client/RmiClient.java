package it.polimi.ingsw.is25am02.network.rmi.client;

import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.State;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.*;
import it.polimi.ingsw.is25am02.network.ConnectionClient;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.view.ConsoleClient;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class RmiClient extends UnicastRemoteObject implements VirtualView, ConnectionClient {
    VirtualServer server;
    ConsoleClient console;
    GameV gameV;

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

    public void setNickname(String nickname) { console.setNickname(nickname); }


    public VirtualServer getServer() { return server; }

    public ConsoleClient getConsole() {
        return console;
    }


    @Override
    public void reportError(String keys, Map<String, String> params) throws RemoteException {
        console.reportError(keys, params);
    }

    @Override
    public void displayMessage(String keys, Map<String, String> params) throws RemoteException {
        console.displayMessage(keys, params);
    }

    @Override
    public void showTileRemoval(Coordinate coordinate, String nickname){ //tolgo dal player p la tile in posizione coordinate
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                if(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].isPresent()){
                    if(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getType().equals(TileType.CABIN)){
                        showCrewRemoval(coordinate,nickname,playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getNumHumans()-1);
                    }
                    else if(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getType().equals(TileType.BATTERY)){
                        showBatteryRemoval(coordinate,nickname, playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getNumBattery()-1);
                    }
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()] = null;
                }
            }
        }
    }

    @Override
    public void showBatteryRemoval(Coordinate coordinate, String nickname, int numBattery){
    //voglio mettere nella tile in coordinate nella spaceship copiata una batteria in meno
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                if(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].isPresent()){
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumBattery(numBattery);
                }
            }
        }
    }

    @Override
    public void showCrewRemoval(Coordinate coordinate, String nickname, int numCrew){
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                if(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].isPresent()){
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumHumans(numCrew);
                }
            }
        }
    }

    @Override
    public void showBoxRemoval(Coordinate coordinate, String nickname, Box box){
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                if(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].isPresent()){
                    if (box.getType().equals(BoxType.RED)) {
                        playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumRedBox(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getNumRedBox()-1);
                    }
                    else if(box.getType().equals(BoxType.YELLOW)){
                        playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumYellowBox(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getNumYellowBox()-1);
                    }
                    else if(box.getType().equals(BoxType.GREEN)){
                        playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumGreenBox(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getNumGreenBox()-1);
                    }
                    else {
                        playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumBlueBox(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getNumBlueBox()-1);
                    }
                }
            }
        }
    }

    @Override
    public void showCreditUpdate(String nickname, int cosmicCredits){
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                playerv.setCredits(cosmicCredits);

            }
        }
    }

    //todo cosa deve fare?
    @Override
    public void showUpdatedOthers(){

    }

    @Override
    public void showPositionsUpdate(HashMap<Player,Integer> positionOnGameboard){
        HashMap<PlayerV, Integer> newPosition = new HashMap<>();
        for(PlayerV playerV : gameV.getPlayers()){
            for(Player p: positionOnGameboard.keySet()){
                if(p.getNickname().equals(playerV.getNickname())){
                    newPosition.put(playerV, positionOnGameboard.get(p));
                }
            }
        }
        gameV.getGlobalBoard().setPositions(newPosition);
    }

    @Override
    public void showHourglassUpdate(Hourglass hourglass){
        gameV.getHourglass().setTimeLeft(hourglass.getTimeLeft());
    }

    @Override
    public void showDiceUpdate(Dice dice){
        gameV.getDiceV().setResult(dice.getResult());

    }

    @Override
    public void showVisibilityUpdate(String imagepath){
        for (TileV tileV : gameV.getHeapTilesV().getSetTileV()) {
            if (tileV.getImagePath().equals(imagepath)) {
                tileV.setVisible(true);
                return;
            }
        }
    }

    // tolgo un tile che è stato messo in una nave
    @Override
    public void showTileRemovalFromHeapTile(String imagepath){
        for (TileV tileV : gameV.getHeapTilesV().getSetTileV()) {
            if (tileV.getImagePath().equals(imagepath)) {
                gameV.getHeapTilesV().removeTile(tileV);
                return;
            }
        }

    }

    @Override
    public void setMenuState(MenuState state) throws RemoteException {
        console.getController().setMenuState(state);
    }

    @Override
    public void showMinideckUpdate(String nickname, int deck){
        for(PlayerV playerV : gameV.getPlayers()){
            if(nickname.equals(playerV.getNickname())){
                playerV.setNumDeck(deck);
                return;
            }
        }
    }

    @Override
    public void showCurrentCardUpdate(String imagepath, StateCardType stateCard) {
        for(CardV card: gameV.getDeck()){
            if(card.getImagePath().equals(imagepath)){
                gameV.getCurrentState().setCurrentCard(new CardV(stateCard, imagepath));
                return;
            }
        }
    }

    @Override
    public void showCurrentTileUpdate(String imagepath, String nickname) {
        for (TileV tileV : gameV.getHeapTilesV().getSetTileV()) {
            if (tileV.getImagePath().equals(imagepath)) {
                for (PlayerV playerv : gameV.getPlayers()) {
                    if (playerv.getNickname().equals(nickname)) {
                        playerv.setCurrentTile(Optional.of(tileV));
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void showUpdateEverything(List<Player> players, Gameboard gameboard, Card currentCard, State state) throws RemoteException {
        StateGameType phaseV = state.getPhase();

        CardV cardV = new CardV(currentCard.getStateCard(), currentCard.getImagePath());
        StateV statev = null;
        List<PlayerV> playersV = new ArrayList<>();
        HashMap<PlayerV, Integer> position = new HashMap<>();

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
                                tileV = new TileV(tile.get().getType(),tile.get().getRotationType(),true,tile.get().getImagePath(),tile.get().getNumBattery(), 0,1,0,0,0,0,0);
                            }
                            else if(tile.get().getCrew().contains(AliveType.BROWN_ALIEN)){
                                tileV = new TileV(tile.get().getType(),tile.get().getRotationType(),true,tile.get().getImagePath(),tile.get().getNumBattery(), 0,0,1,0,0,0,0);
                            }
                            else{
                                tileV = new TileV(tile.get().getType(),tile.get().getRotationType(),true,tile.get().getImagePath(),tile.get().getNumBattery(), tile.get().getCrew().size(),0,0,0,0,0,0);
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
                            tileV = new TileV(tile.get().getType(),tile.get().getRotationType(),true,tile.get().getImagePath(),tile.get().getNumBattery(), 0,0,0,redCount,yellowCount,greenCount,blueCount);
                        }
                        else{
                            tileV = new TileV(tile.get().getType(),tile.get().getRotationType(),true,tile.get().getImagePath(),tile.get().getNumBattery(), 0,0,0,0,0,0,0);

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
            position.put(playerv,gameboard.getPositions().get(p));
        }



        GameboardV gameboardV = new GameboardV(position, gameboard.getDice(), gameboard.getHourGlassFlip());

        GameV gameV = new GameV(playersV, currentCard.getLevel(), gameboardV, statev, false, new HourglassV());

    }
}
