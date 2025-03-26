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
import java.util.Optional;

public interface Game_Interface {

    //Phase BUILD
    void flipHourglass();
    Tile takeTile(Player player);
    Tile takeTile(Player player, Tile tile);
    void bookTile(Player player);
    void addBookedTile(Player player, int index, int x, int y);
    void returnTile(Player player);
    void addTile(Player player, int x, int y);
    void shipFinished(Player player);

    //Phase CHECK
    void checkSpaceship(Player player);

    //Phase CORRECTION
    Optional<List<boolean[][]>> removeTile(Player player, int x, int y);
    void keepBlock(Player player, boolean[][] tilesToKeep);
    void checkWrongSpaceship(Player player);

    //Phase INITIALIZATION_SPACESHIP (Automatica se nessun player ha supporti vitali per alieni sulla nave)
    void addCrew(Player player, int x, int y, AliveType type);
    void ready(Player player);

    //Phase TAKE_CARD
    void playNextCard(Player player);
    void earlyLanding(Player player);

    //Phase EFFECT_ON_PLAYERS
    HashMap<Player, Integer> getPosition();
    List<Tile> possibleChoice(Player player, TileType type);
    void choice(Player player, boolean choice);
    void removeCrew(Player player, Tile cabin);
    List<Box> choiceBox(Player player, boolean choice) throws Exception;
    void moveBox(Player player, List<Box> start, List<Box> end, Box box, boolean on) throws Exception;
    List<Box> choicePlanet(Player player, int index);
    void choiceDoubleMotor(Player player, Optional<List<Pair<Tile, Tile>>> choices);
    void choiceDoubleCannon(Player player, Optional<List<Pair<Tile, Tile>>> choices);
    void choiceCrew(Player player);
    void removeBox(Player player, Tile storage, BoxType type);
    void removeBattery(Player player, Tile storage);
    void rollDice(Player player);
    void effect(Game game);
    void calculateDamage(Player player, Optional<Tile> batteryStorage);
    void holdSpaceship(Player player, int x, int y);


    //Phase RESULT
    ArrayList<Player> getWinners();

}
