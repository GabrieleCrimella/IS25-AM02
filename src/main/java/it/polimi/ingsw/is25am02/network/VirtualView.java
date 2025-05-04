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

    void showUpdateEverything(int level, List<String> nickPlayers, HashMap<String, PlayerColor> playercolors,HashMap<String, Integer> positions, String currentCardImage, StateCardType stateCard,StateGameType stateGame, String currentPlayer, boolean[][] mask) throws Exception;

    void showTileRemoval(Coordinate coordinate, String nickname) throws RemoteException;

    void showBatteryRemoval(Coordinate coordinate, String nickname, int numBattery) throws RemoteException;

    void showCrewRemoval(Coordinate coordinate, String nickname, int numCrew) throws RemoteException;

    void showBoxUpdate(Coordinate coordinate, String nickname, List<BoxType> box) throws RemoteException;

    void showCreditUpdate(String nickname, int cosmicCredits) throws RemoteException;

    void showUpdatedOthers() throws RemoteException;

    void showPositionUpdate(String nickname, int position) throws RemoteException;

    void showHourglassUpdate() throws RemoteException;

    void showDiceUpdate(int result) throws RemoteException;

    void showMinideckUpdate(String nickname, int deck) throws RemoteException;

    void showCurrentCardUpdate(String imagepath, StateCardType stateCard) throws RemoteException;

    void showCurrentTileUpdate(String imagepath, String nickname) throws RemoteException;

    void showVisibilityUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox) throws RemoteException;

    void showTileRemovalFromHeapTile(String imagepath) throws RemoteException;

    void showTileAdditionUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox,String nickname, Coordinate coordinate) throws RemoteException;

}