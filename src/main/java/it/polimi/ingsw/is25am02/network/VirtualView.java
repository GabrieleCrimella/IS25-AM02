package it.polimi.ingsw.is25am02.network;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.State;

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
    void showTileRemoval(Coordinate coordinate, Player p);
    void showBatteryRemoval(Coordinate coordinate, Player p);
    void showCrewRemoval(Coordinate coordinate, Player p);
    void showBoxRemoval(Coordinate coordinate, Player p);
    void showCreditUpdate(Player p);
    void showUpdatedOthers();
    void showPositionsUpdate(HashMap<Player,Integer> positionOnGameboard);
    void showHourglassUpdate(int timeLeft);
    void showDiceUpdate(int diceResult);
    void showHeapTileUpdate(HeapTiles heapTiles);
    void showMinideckUpdate();
    void showCurrentCardUpdate(Card currentCard);

}
