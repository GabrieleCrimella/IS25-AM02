package it.polimi.ingsw.is25am02.network.rmi.client;

import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
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

    public GameV getGameV() { return gameV; }

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
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()] = Optional.empty();
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
    public void showBoxUpdate(Coordinate coordinate, String nickname, List<BoxType> boxList){
        int numred=0;
        int numyellow=0;
        int numblue=0;
        int numgreen=0;
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                if(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].isPresent()){
                    for(BoxType box: boxList) {
                        if (box.equals(BoxType.RED)) {
                            numred++;
                        } else if (box.equals(BoxType.YELLOW)) {
                            numyellow++;
                        } else if (box.equals(BoxType.GREEN)) {
                            numgreen++;
                        } else {
                            numblue++;
                        }
                    }
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumYellowBox(numyellow);
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumGreenBox(numgreen);
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumBlueBox(numblue);
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumRedBox(numred);
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
    public void showPositionUpdate(String nickname, int position){
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                gameV.getGlobalBoard().setPosition(playerv, position);
            }
        }
    }

    //viene chiamato quando viene girata la clessidra
    @Override
    public void showHourglassUpdate(){
        gameV.getHourglass().flip();

    }

    @Override
    public void showDiceUpdate(int result){
        gameV.getDiceV().setResult(result);

    }

    @Override
    public void showVisibilityUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox){ //todo voglio aggiungere una tile all'heaptile
        TileV tileV = new TileV(tType,connectors,rotationType, true,imagepath,maxBattery, maxBox);
        gameV.getHeapTilesV().addToHeapTile(tileV);
    }

    // tolgo un tile che è stata messa in una nave
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
    public void showDeckAllowUpdate(String player) throws RemoteException{
        for(PlayerV playerV : gameV.getPlayers()){
            if(player.equals(playerV.getNickname())){
                playerV.setDeckAllowed();
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

    //una tile viene aggiunta ad una nave, potrebbe essere che è nel set di tile oppure che devo crearla
    @Override
    public void showTileAdditionUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox,String nickname, Coordinate coordinate){
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                for (TileV tileV : gameV.getHeapTilesV().getSetTileV()) {
                    if (tileV.getImagePath().equals(imagepath)) {
                        tileV.setRotationType(rotationType);
                        playerv.setSpaceshipBoardTile(tileV,coordinate);
                        gameV.getHeapTilesV().removeFromHeapTile(tileV);
                        return;
                    }
                } //se non trovo la tile nell'heap tile la devo creare
                TileV tileV = new TileV(tType,connectors,rotationType,true,imagepath,maxBattery,maxBox);
                playerv.setSpaceshipBoardTile(tileV,coordinate);
            }
        }
    }

    @Override
    public void showUpdateEverything(int level, HashMap<String, PlayerColor> playercolors, String currentCardImage, StateCardType stateCard,StateGameType stateGame, String currentPlayer, boolean[][] mask,int[] startingpositions) throws RemoteException {
        ArrayList<PlayerV> players = new ArrayList<>();
        CardV currentCardV = new CardV(stateCard,currentCardImage);
        PlayerV currentPlayerV = null;

        for(String p: playercolors.keySet()){

            PlayerV playerV = new PlayerV(p,playercolors.get(p), mask);
            if(p.equals(currentPlayer)) currentPlayerV = playerV;
            players.add(playerV);
        }
        StateV stateV = new StateV(currentCardV,currentPlayerV,stateGame);
        GameboardV gameboardV = new GameboardV(startingpositions);
        GameV game = new GameV(players,level, gameboardV, stateV, false, console);

        console.getPrinter().setGame(gameV);
        console.getController().setGameV(game);
    }
}
