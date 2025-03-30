package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.tiles.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface Game_Interface {

    //Phase BUILD
    void flipHourglass(Player player);
    Tile takeTile(Player player);
    Tile takeTile(Player player, Tile tile);
    List<Card> takeMiniDeck(Player player, int index);
    void returnMiniDeck(Player player);
    void bookTile(Player player);
    void addBookedTile(Player player, int index, Coordinate pos);
    void returnTile(Player player);
    void addTile(Player player, Coordinate pos);
    void shipFinished(Player player);

    //Phase CHECK
    void checkSpaceship(Player player);

    //Phase CORRECTION
    Optional<List<boolean[][]>> removeTile(Player player, Coordinate pos);
    void checkWrongSpaceship(Player player);

    //Phase INITIALIZATION_SPACESHIP (Automatica se nessun player ha supporti vitali per alieni sulla nave)
    void addCrew(Player player, Coordinate pos, AliveType type);
    void ready(Player player);

    //Phase TAKE_CARD
    void playNextCard(Player player);
    void earlyLanding(Player player);

    //Phase EFFECT_ON_PLAYERS
    HashMap<Player, Integer> getPosition();
    List<Tile> possibleChoice(Player player, TileType type);
    void choice(Player player, boolean choice);
    void removeCrew(Player player, Coordinate pos);
    List<Box> choiceBox(Player player, boolean choice);
    void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on);
    List<Box> choicePlanet(Player player, int index);
    void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries);
    void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries);
    void choiceCrew(Player player);
    void removeBox(Player player, Coordinate pos, BoxType type);
    void removeBattery(Player player, Coordinate pos);
    void rollDice(Player player);
    void effect(Game game);
    void calculateDamage(Player player, Coordinate pos);
    void keepBlock(Player player, boolean[][] tilesToKeep);

    //Phase RESULT
    ArrayList<Player> getWinners();

}
