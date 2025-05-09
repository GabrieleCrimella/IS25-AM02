package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateGameType;
import it.polimi.ingsw.is25am02.utils.enumerations.StatePlayerType;

import java.rmi.RemoteException;
import java.util.List;

public interface UpdateListener {
    void onCreditUpdate(int credit);
    void onRemoveTileUpdate(Coordinate coordinate);
    void onRemoveBatteryUpdate(int battery, Coordinate coordinate);
    void onRemoveCrewUpdate(Coordinate coordinate, int numcrew);
    void onPositionUpdate(int position);
    void onHourglassUpdate();
    void onDiceUpdate(Dice dice);
    void onUpdateEverything(int level, List<Player> players, Gameboard gameboard, Card currentcard, State state, boolean[][] mask, int[] positions);
    void onBoxUpdate(Coordinate coordinate, List<BoxType> box);
    void onMiniDeckUpdate(int deck);
    void onCurrentCardUpdate(String imagepath, StateCardType state);
    void onCurrentTileUpdate(Tile tile);
    void onVsibilityUpdate(Tile tile);
    void onTileRemovalFromHTUpdate(String imagepath);
    void onTileAdditionToSpaceship(Tile tile, Coordinate coordinate);
    void onDeckAllowedUpdate();
    void onCurrentTileNullityUpdate();
    void onGameStateUpdate(StateGameType stateGameType);
    void onCardStateUpdate(StateCardType stateCardType);
    void onPlayerStateUpdate(StatePlayerType statePlayerType);
    void onCurrentPlayerUpdate(String nickname);

}
