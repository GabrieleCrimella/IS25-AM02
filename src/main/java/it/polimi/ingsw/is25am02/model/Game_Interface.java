package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.AliveType;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.*;

import java.util.List;

public interface Game_Interface {

    //Phase BUILD
    void flipHourglass(Player player) throws Exception; //todo testare
    void hourglass(Player player) throws Exception;
    void takeTile(Player player);
    void takeTile(Player player, String tile_imagePath);
    void takeMiniDeck(Player player, int index);
    void returnMiniDeck(Player player);
    void bookTile(Player player);
    void addBookedTile(Player player, int index, Coordinate pos, RotationType rotation); //testato
    void returnTile(Player player);
    void addTile(Player player, Coordinate pos, RotationType rotation);
    void shipFinished(Player player);

    //Phase CHECK
    void checkSpaceship(Player player);

    //Phase CORRECTION
    void removeTile(Player player, Coordinate pos);
    void checkWrongSpaceship(Player player);

    //Phase INITIALIZATION_SPACESHIP (Automatica se nessun player ha supporti vitali per alieni sulla nave)
    void addCrew(Player player, Coordinate pos, AliveType type); //todo da testare
    void ready(Player player);

    //Phase TAKE_CARD
    void playNextCard(Player player);
    void earlyLanding(Player player); //todo da testare

    //todo mi viene mal di testa solo a pensare come testarli
    //Phase EFFECT_ON_PLAYERS
    void choice(Player player, boolean choice);
    void removeCrew(Player player, Coordinate pos);
    void choiceBox(Player player, boolean choice);
    void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on);
    void choicePlanet(Player player, int index);
    void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries);
    void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries);
    void choiceCrew(Player player);
    void removeBox(Player player, Coordinate pos, BoxType type);
    void removeBattery(Player player, Coordinate pos);
    void rollDice(Player player);
    void effect(Game game);
    void calculateDamage(Player player, Coordinate pos);
    void keepBlock(Player player, Coordinate pos);

    //Phase RESULT
    void Winners();

}
