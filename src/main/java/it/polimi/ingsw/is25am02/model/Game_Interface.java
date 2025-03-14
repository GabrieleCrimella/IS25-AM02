package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.StatePlayerType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Game_Interface {
    //Phase START
    Game GameCreator(List<Player> p, int level);

    //Phase INITIALIZATION_GAME
    //Nothing

    //Phase BUILD
    void flipHourglass();
    Tile takeTile(Player player);
    Tile takeTile(Player player, Tile tile);
    void returnTile(Player player, Tile tile);
    void addTile(Player player, Tile tile, int x, int y);
    void shipFinished(Player player);

    //Phase CHECK
    boolean checkSpaceship(Player player);

    //Phase CORRECTION
    void removeTile(Player player, int x, int y);

    //Phase INITIALIZATION_SPACESHIP (Automatica se nessun player ha supporti vitali per alieni sulla nave)
    void addCrew(Player player, int x, int y, AliveType type);

    //Phase TAKE_CARD
    void playNextCard();

    //Phase EFFECT_ON_PLAYERS
    HashMap<Integer, Player> getPosition();
    HashMap<Player, StatePlayerType> getState();
    List<Tile> possibleChoice(Player player, TileType type);
    void choice(Player player, boolean choice);
    void removeCrew(Player player, Cabin cabin);
    List<Box> choiceBox(Player player, boolean choice);
    void moveBox(List<Box> start, List<Box> end, BoxType type);
    List<Box> choicePlanet(Player player, int index);
    void choiceDoubleMotor(Player player, List<Pair<DoubleMotor, BatteryStorage>> choices);
    void choiceDoubleCannon(Player player, List<Pair<DoubleCannon, BatteryStorage>> choices);
    void removeBox(Player player, SpecialStorage storage, BoxType type);
    void removeBattery(Player player, BatteryStorage storage);


    //Phase CHANGE_CONDITION
    //Nothing
    
    //Phase RESULT
    ArrayList<Player> getWinners();

}
