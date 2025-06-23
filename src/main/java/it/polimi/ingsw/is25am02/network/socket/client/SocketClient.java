package it.polimi.ingsw.is25am02.network.socket.client;

import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.network.ConnectionClient;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.view.ConsoleClient;

import java.net.Socket;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketClient implements ConnectionClient, VirtualView {
    private ServerHandler handler;
    private ConsoleClient console;

    @Override
    public void startConnection(String ip) throws Exception {
        Socket socket = new Socket(ip, 8080);
        handler = new ServerHandler(socket, console);
        System.out.println("Client connesso");
        new Thread(handler).start();
    }

    @Override
    public void closeConnection() {
        handler.closeConnection();
    }

    @Override
    public void setView(ConsoleClient view) {
        console = view;
    }

    @Override
    public VirtualServer getServer() {
        return handler;
    }

    @Override
    public ConsoleClient getConsole() {
        return console;
    }

    @Override
    public void reportError(String keys, Map<String, String> params) throws Exception {

    }

    @Override
    public void displayMessage(String keys, Map<String, String> params) throws Exception {

    }

    @Override
    public void pingFromServer() throws RemoteException {

    }

    @Override
    public void setLobbiesView(Map<Integer, LobbyView> lobbies) throws RemoteException {

    }

    @Override
    public void setGameView(int level, PlayerColor color) throws RemoteException {

    }

    @Override
    public void setBuildView(int level, PlayerColor color) throws RemoteException {

    }

    @Override
    public void setMenuState(MenuState state) throws RemoteException {

    }

    @Override
    public void setNickname(String nickname) throws RemoteException {

    }

    @Override
    public void showUpdateEverything(int level, HashMap<String, PlayerColor> playercolors, String currentCardImage, StateCardType stateCard, CardType type, String comment, StateGameType stateGame, String currentPlayer, boolean[][] mask, int[] positions, HashMap<Integer, List<List<Object>>> deck) throws Exception {

    }

    @Override
    public void showTileRemoval(Coordinate coordinate, String nickname) throws RemoteException {

    }

    @Override
    public void spaceshipBrokenUpdate(String nickname) throws RemoteException {

    }

    @Override
    public void showBatteryRemoval(Coordinate coordinate, String nickname, int numBattery) throws RemoteException {

    }

    @Override
    public void showCrewRemoval(Coordinate coordinate, String nickname) throws RemoteException {

    }

    @Override
    public void showBoxUpdate(Coordinate coordinate, String nickname, List<BoxType> box) throws RemoteException {

    }

    @Override
    public void showCreditUpdate(String nickname, int cosmicCredits) throws RemoteException {

    }

    @Override
    public void showUpdatedOthers() throws RemoteException {

    }

    @Override
    public void showPositionUpdate(String nickname, int position) throws RemoteException {

    }

    @Override
    public void showHourglassUpdate(long timeLeft) throws RemoteException {

    }

    @Override
    public void showDiceUpdate(String nickname, int result) throws RemoteException {

    }

    @Override
    public void showMinideckUpdate(String nickname, int deck) throws RemoteException {

    }

    @Override
    public void showCurrentCardUpdate(String imagepath, StateCardType stateCard, CardType type, String comment) throws RemoteException {

    }

    @Override
    public void showCurrentTileNullityUpdate(String nickname) throws RemoteException {

    }

    @Override
    public void showCurrentTileUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox, String nickname) throws RemoteException {

    }

    @Override
    public void showVisibilityUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox) throws RemoteException {

    }

    @Override
    public void showTileRemovalFromHeapTile(String imagepath) throws RemoteException {

    }

    @Override
    public void showDeckAllowUpdate(String player) throws RemoteException {

    }

    @Override
    public void showTileAdditionUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox, String nickname, Coordinate coordinate) throws RemoteException {

    }

    @Override
    public void showGameStateUpdate(StateGameType newGamestate) throws RemoteException {

    }

    @Override
    public void showPlayerStateUpdate(String nickname, StatePlayerType newPlayerstate) throws RemoteException {

    }

    @Override
    public void showCardStateUpdate(StateCardType newCardstate) throws RemoteException {

    }

    @Override
    public void showCurrentPlayerUpdate(String nickname) throws RemoteException {

    }

    @Override
    public void showBookTileUpdate(String nickname) throws RemoteException {

    }

    @Override
    public void showAddCrewUpdate(String nickname, Coordinate pos, AliveType type, int num) throws RemoteException {

    }

    @Override
    public void showBookedTileNullityUpdate(String nickname, int index, Coordinate pos, RotationType rotation) throws RemoteException {

    }

    @Override
    public void showEarlyLandingUpdate(String nickname) throws RemoteException {

    }

    @Override
    public void showBuildTimeIsOverUpdate() throws RemoteException {

    }

    @Override
    public void showWinnersUpdate(Map<String, Integer> winners) throws RemoteException {

    }
}
