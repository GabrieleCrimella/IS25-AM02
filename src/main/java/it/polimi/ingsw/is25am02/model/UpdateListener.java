package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;

import java.rmi.RemoteException;
import java.util.List;

public interface UpdateListener {
    void onCreditUpdate(int credit);
    void onRemoveTileUpdate(Coordinate coordinate);
    void onRemoveBatteryUpdate(int battery, Coordinate coordinate);
    void onRemoveCrewUpdate(Coordinate coordinate, int numcrew);
    void onPositionUpdate(int position);
    void onHourglassUpdate(Hourglass hourglass);
    void onDiceUpdate(Dice dice);
    void onUpdateEverything(List<Player> players, Gameboard gameboard, Card currentcard, State state);
    void onBoxUpdate(Coordinate coordinate, List<Box> box);
    void onMiniDeckUpdate(int deck);
    void onCurrentCardUpdate(String imagepath, StateCardType state);
    void onCurrentTileUpdate(String imagepath);
    void onVsibilityUpdate(String imagepath);
    void onTileRemovalFromHTUpdate(String imagepath);

}
