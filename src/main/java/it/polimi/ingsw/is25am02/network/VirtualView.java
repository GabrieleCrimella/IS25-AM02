package it.polimi.ingsw.is25am02.network;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.State;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.*;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.rmi.Remote;
import java.util.*;
import java.rmi.RemoteException;

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

    void showBoxAddition(Coordinate coordinate, String nickname, Box box);

    void showCreditUpdate(String nickname, int cosmicCredits) throws RemoteException;

    void showUpdatedOthers() throws RemoteException;

    void showPositionUpdate(String nickname, int position) throws RemoteException;

    void showHourglassUpdate(Hourglass hourglass) throws RemoteException;

    void showDiceUpdate(Dice dice) throws RemoteException;

    void showMinideckUpdate(String nickname, int deck) throws RemoteException;

    void showCurrentCardUpdate(String imagepath, StateCardType stateCard) throws RemoteException;

    void showCurrentTileUpdate(String imagepath, String nickname) throws RemoteException;

    void showVisibilityUpdate(String imagepath) throws RemoteException;

    void showTileRemovalFromHeapTile(String imagepath) throws RemoteException;

}