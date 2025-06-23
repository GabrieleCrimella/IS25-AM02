package it.polimi.ingsw.is25am02.network;

import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.model.Lobby;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.utils.enumerations.*;

import java.rmi.Remote;
import java.util.*;
import java.rmi.RemoteException;

/*
Interface that defines the methods used by the server to notify clients of state changes.
*/
public interface VirtualView extends Remote {
    void pingFromServer() throws RemoteException;

    void reportError(String keys, Map<String, String> params) throws Exception;

    void displayMessage(String keys, Map<String, String> params) throws Exception;

    void setLobbiesView(Map<Integer, LobbyView> lobbies) throws RemoteException;

    void setBuildView(int level, PlayerColor color) throws RemoteException;

    void setGameView(int level, PlayerColor color) throws RemoteException;

    void setMenuState(MenuState state) throws RemoteException;

    void setNickname(String nickname) throws RemoteException;

    void showUpdateEverything(int level, HashMap<String, PlayerColor> playercolors, String currentCardImage, StateCardType stateCard, CardType type, String comment, StateGameType stateGame, String currentPlayer, boolean[][] mask, int[] positions, HashMap<Integer, List<List<Object>>> deck) throws Exception;

    void showTileRemoval(Coordinate coordinate, String nickname) throws RemoteException;

    void spaceshipBrokenUpdate(String nickname) throws RemoteException;

    void showBatteryRemoval(Coordinate coordinate, String nickname, int numBattery) throws RemoteException;

    void showCrewRemoval(Coordinate coordinate, String nickname) throws RemoteException;

    void showBoxUpdate(Coordinate coordinate, String nickname, List<BoxType> box) throws RemoteException;

    void showCreditUpdate(String nickname, int cosmicCredits) throws RemoteException;

    void showUpdatedOthers() throws RemoteException;

    void showPositionUpdate(String nickname, int position) throws RemoteException;

    void showHourglassUpdate(long timeLeft) throws RemoteException;

    void showDiceUpdate(String nickname, int result) throws RemoteException;

    void showMinideckUpdate(String nickname, int deck) throws RemoteException;

    void showCurrentCardUpdate(String imagepath, StateCardType stateCard, CardType type, String comment) throws RemoteException;

    void showCurrentTileNullityUpdate(String nickname) throws RemoteException;

    void showCurrentTileUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox, String nickname) throws RemoteException;

    void showVisibilityUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox) throws RemoteException;

    void showTileRemovalFromHeapTile(String imagepath) throws RemoteException;

    void showDeckAllowUpdate(String player) throws RemoteException;

    void showTileAdditionUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox, String nickname, Coordinate coordinate) throws RemoteException;

    void showGameStateUpdate(StateGameType newGamestate) throws RemoteException;

    void showPlayerStateUpdate(String nickname, StatePlayerType newPlayerstate) throws RemoteException;

    void showCardStateUpdate(StateCardType newCardstate) throws RemoteException;

    void showCurrentPlayerUpdate(String nickname) throws RemoteException;

    void showBookTileUpdate(String nickname) throws RemoteException;

    void showAddCrewUpdate(String nickname, Coordinate pos, AliveType type, int num) throws RemoteException;

    void showBookedTileNullityUpdate(String nickname, int index, Coordinate pos, RotationType rotation) throws RemoteException;

    void showEarlyLandingUpdate(String nickname) throws RemoteException;

    void showBuildTimeIsOverUpdate() throws RemoteException;

    void showWinnersUpdate(Map<String, Integer> winners) throws RemoteException;
}