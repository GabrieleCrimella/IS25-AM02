package it.polimi.ingsw.is25am02.network;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.State;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.utils.Coordinate;

import java.rmi.Remote;
import java.util.HashMap;
import java.util.List;
import java.rmi.RemoteException;
import java.util.Map;

/*
Interface that defines the methods used by the server to notify clients of state changes.
*/
public interface VirtualView extends Remote {
    void reportError(String keys, Map<String, String> params) throws Exception;
    void displayMessage(String keys, Map<String, String> params) throws Exception;
    void setMenuState(MenuState state) throws RemoteException;
    void setNickname(String nickname) throws RemoteException;

    void showUpdateEverything(List<Player> players, Gameboard gameboard, Card currentCard, State state) throws Exception;
    void showTileRemoval(Coordinate coordinate, String nickname) throws RemoteException;
    void showBatteryRemoval(Coordinate coordinate, String nickname, int numBattery) throws RemoteException;
    void showCrewRemoval(Coordinate coordinate, String nickname, int numCrew) throws RemoteException;
    void showBoxRemoval(Coordinate coordinate, String nickname, Box box) throws RemoteException;
    void showCreditUpdate(String nickname, int cosmicCredits) throws RemoteException;
    void showUpdatedOthers() throws RemoteException;
    void showPositionsUpdate(HashMap<Player,Integer> positionOnGameboard) throws RemoteException;
    void showHourglassUpdate(Hourglass hourglass) throws RemoteException;
    void showDiceUpdate(Dice dice) throws RemoteException;
    void showMinideckUpdate() throws RemoteException;
    void showCurrentCardUpdate(String imagepath) throws RemoteException;
    void showCurrentTileUpdate(String imagepath) throws RemoteException;
    void showVisibilityUpdate(String imagepath) throws RemoteException;
     void showTileRemovalFromHeapTile(String imagepath) throws RemoteException;

}
