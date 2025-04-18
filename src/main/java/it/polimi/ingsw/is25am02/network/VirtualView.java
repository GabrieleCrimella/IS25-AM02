package it.polimi.ingsw.is25am02.network;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.State;

import java.rmi.Remote;
import java.util.List;
import java.rmi.RemoteException;
import java.util.Optional;

/*
Interface that defines the methods used by the server to notify clients of state changes.
*/
public interface VirtualView extends Remote {
    void reportError(String details) throws Exception;
    void displayMessage(String details) throws Exception;
    void setMenuState(MenuState state) throws RemoteException;
    void setNickname(String nickname) throws RemoteException;

    void showUpdateEverything(List<Player> players, Gameboard gameboard, Card currentCard, State state) throws Exception;
    void showTileRemoval();
    void showBatteryRemoval(Coordinate coordinate, Player p);
    void showCrewRemoval(Coordinate coordinate, Player p);
    void showBoxRemoval(Tile t);
    void showCreditUpdate(Player p);
    void showUpdatedOthers();
    void showPositionsUpdate();
    void showHourglassUpdate();
    void showDiceUpdate();
    void showHeapTileUpdate();
    void showMinideckUpdate();
    void showCurrentCardUpdate();

}
