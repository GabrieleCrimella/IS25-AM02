package it.polimi.ingsw.is25am02.network;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.rmi.Remote;
import java.util.List;
import java.util.Optional;

/*
Interface that defines the methods used by the server to notify clients of state changes.
*/
public interface VirtualView extends Remote {
    void reportError(String details) throws Exception;

    void displayMessage(String details) throws Exception;

    void showUpdateEverything(List<Player> players, Gameboard gameboard, Card currentCard, State state) throws Exception;
    void showTileRemoval();
    void showBatteryRemoval(Coordinate coordinate, Player p);
    void showCrewRemoval(Tile t);
    void showBoxRemoval(Tile t);
    void showCreditUpdate();
    void showUpdatedOthers();
    void showPositionsUpdate();
    void showHourglassUpdate();
    void showDiceUpdate();
    void showHeapTileUpdate();
    void showMinideckUpdate();
    void showCurrentCardUpdate();

}
