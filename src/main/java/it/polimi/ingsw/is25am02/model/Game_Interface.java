package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;

public interface Game_Interface {
    //Phase START
    Game Game(ArrayList<Player> p, int level);

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
    void effect(); //Bisogna ancora capire bene come implementare
    HashMap<Integer, Player> getPosition();
    HashMap<Player, StatePlayerType> getState();
    ArrayList<Tile> possibleChoice(Player player, TileType type);

    //Phase CHANGE_CONDITION
    //Nothing
    
    //Phase RESULT
    ArrayList<Player> getWinners();

}
