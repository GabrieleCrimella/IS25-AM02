package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface UpdateListener {
    void onCreditUpdate(int credit);
    void onRemoveTileUpdate( Coordinate coordinate);
    void onRemoveBatteryUpdate(int battery, Coordinate coordinate);
    void onRemoveCrewUpdate(String nickname, Coordinate coordinate);
    void onPositionUpdate(String nickname, int position);
    void onHourglassUpdate();
    void onDiceUpdate(String nickname, Dice dice);
    void onUpdateEverything(int level, List<Player> players, Gameboard gameboard, Card currentcard, State state, boolean[][] mask, int[] positions, HashMap<Integer , Pair<List<Card>,Boolean>> deck);
    void onBoxUpdate(Coordinate coordinate, List<BoxType> box);
    void onMiniDeckUpdate(int deck);
    void onCurrentCardUpdate(String imagepath, StateCardType state, CardType type, String comment);
    void onCurrentTileUpdate(String nickname, Tile tile);
    void onVsibilityUpdate(String nickname, Tile tile);
    void onTileRemovalFromHTUpdate(String imagepath);
    void onTileAdditionToSpaceship(String nickname, Tile tile, Coordinate coordinate);
    void onDeckAllowedUpdate();
    void onCurrentTileNullityUpdate(String nickname);
    void onGameStateUpdate(StateGameType stateGameType);
    void onCardStateUpdate(StateCardType stateCardType);
    void onPlayerStateUpdate(String nickname, StatePlayerType statePlayerType);
    void onCurrentPlayerUpdate(String nickname);
    void onBookTileUpdate(String nickname);

}
