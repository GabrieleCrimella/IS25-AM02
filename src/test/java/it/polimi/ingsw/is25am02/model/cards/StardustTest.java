package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.exception.AlreadyViewingException;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StardustTest {

    public Game make_a_spaceship(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED, null, 1);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE, null, 1);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN, null, 1);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW, null, 1);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 0 con i 4 players
        Game game = new Game(players,2);
        //inizializzo
        game.getGameboard().initializeGameBoard(players);

        //inizializzo spaceship1
        //tile 1 - cabin centrale
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            spaceship1.addTile(player1.getNickname(), 7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 2 - cabin
        TileType t2 = TileType.CABIN;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Tile cabin2 = new Cabin(t2, connectors2, rotationType2, null);
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, cabin2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - motor
        TileType t3 = TileType.MOTOR;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        Tile motor3 = new Motors(t3, connectors3, rotationType3, null);
        try {
            spaceship1.addTile(player1.getNickname(), 7,8, motor3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - battery
        TileType t4 = TileType.BATTERY;
        ConnectorType[] connectors4 = {ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.SINGLE, ConnectorType.NONE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        int maxBattery = 3;
        Tile battery4 = new BatteryStorage(t4, connectors4, rotationType4, null, maxBattery);
        try {
            spaceship1.addTile(player1.getNickname(), 6,8, battery4);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - shield
        TileType t5 = TileType.SHIELD;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        boolean[] shielded = {true, false, false, true};
        Tile shield5 = new Shield(t5, connectors5, rotationType5, null, shielded);
        try {
            spaceship1.addTile(player1.getNickname(), 6,7, shield5);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cannon
        TileType t6 = TileType.CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cannon6 = new Cannon(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(), 7,6, cannon6);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 7 - specialstorage
        TileType t7 = TileType.SPECIAL_STORAGE;
        ConnectorType[] connectors7 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType7 = RotationType.NORTH;
        int id7 = 1;
        int maxNum = 2;
        Tile specialStorage7 = new SpecialStorage(t7, connectors7, rotationType7, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname() ,9,7, specialStorage7);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //inizializzo spaceship2
        //tile 1 - cabin centrale
        TileType t21 = TileType.CABIN;
        ConnectorType[] connectors21 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType21 = RotationType.NORTH;
        int id21 = 1;
        Tile cabin21 = new Cabin(t21, connectors21, rotationType21, null);
        try {
            spaceship2.addTile(player1.getNickname(), 7,7, cabin21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship3
        //tile 1 - cabin centrale
        TileType t31 = TileType.CABIN;
        ConnectorType[] connectors31 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType31 = RotationType.NORTH;
        int id31 = 1;
        Tile cabin31 = new Cabin(t31, connectors31, rotationType31, null);
        try {
            spaceship3.addTile(player1.getNickname(), 7,7, cabin31);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship4
        //tile 1 - cabin centrale
        TileType t41 = TileType.CABIN;
        ConnectorType[] connectors41 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType41 = RotationType.NORTH;
        int id41 = 1;
        Tile cabin41 = new Cabin(t41, connectors41, rotationType41, null);
        try {
            spaceship4.addTile(player1.getNickname(), 7,7, cabin41);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);

        Coordinate pos1 = new Coordinate(7,7);
        game.addCrew(player1, pos1, AliveType.HUMAN);
        Coordinate pos2 = new Coordinate(8,7);
        game.addCrew(player1, pos2,AliveType.HUMAN);
        Coordinate pos5  = new Coordinate(7,7);
        game.addCrew(player3, pos5, AliveType.HUMAN);

        Coordinate  pos3 = new Coordinate(7,7);
        game.addCrew(player2, pos3, AliveType.HUMAN);

        Coordinate pos4 = new Coordinate(7,7);
        game.addCrew(player4, pos4, AliveType.HUMAN);

        player1.setStatePlayer(StatePlayerType.IN_GAME);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        player4.setStatePlayer(StatePlayerType.IN_GAME);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);

        return game;
    }

    @Test
    void test_should_check_if_player_goes_back(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED, null, 1);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE, null, 1);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN, null, 1);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW, null, 1);

        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 0 con i 4 players
        Game game = new Game(players,0);
        //inizializzo
        //game.getCurrentState().setPhase(StateGameType.BUILD);
        game.getGameboard().initializeGameBoard(players);
        game.shipFinished(player1);
        game.shipFinished(player2);
        game.shipFinished(player3);
        game.shipFinished(player4);

        game.checkSpaceship(player1);
        game.checkSpaceship(player2);
        game.checkSpaceship(player3);
        game.checkSpaceship(player4);


        Coordinate pos = new Coordinate(7, 7);
        game.addCrew(player1, pos, AliveType.HUMAN);

        pos = new Coordinate(7, 8);
        game.addCrew(player1, pos, AliveType.HUMAN);
        game.ready(player1);
        game.ready(player2);
        game.ready(player3);
        game.ready(player4);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        game.getCurrentState().setCurrentPlayer(player1);


        Card stardust = new Stardust(0, "prova", "commento", true);
        LinkedList<String> currentOrder = new LinkedList<>();
        currentOrder.add("Rosso");
        currentOrder.add("Blu");
        currentOrder.add("Verde");
        currentOrder.add("Giallo");
        game.getCurrentState().setCurrentCard(stardust);
        game.getCurrentCard().setCurrentOrder(currentOrder);


        game.effect(game);

        assertEquals(0, game.getGameboard().getPositions().get(player1));
        assertEquals(-2, game.getGameboard().getPositions().get(player2));
        assertEquals(-3, game.getGameboard().getPositions().get(player3));
        assertEquals(-4, game.getGameboard().getPositions().get(player4));



    }

    @Test
    void test_should_check_if_player_goes_back_2(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED, null, 1);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE, null, 1);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN, null, 1);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW, null, 1);

        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 0 con i 4 players
        Game game = new Game(players,0);
        //inizializzo
        game.getCurrentState().setPhase(StateGameType.BUILD);
        game.getGameboard().initializeGameBoard(players);

        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int maxNum = 3;
        Tile battery1 = new BatteryStorage(TileType.BATTERY, connectors2, rotationType2, null, maxNum);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), battery1);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 7,8, battery1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.shipFinished(player1);
        game.shipFinished(player2);
        game.shipFinished(player3);
        game.shipFinished(player4);

        //finished spaceships and initializing them
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        Coordinate pos = new Coordinate(7, 7);
        game.addCrew(player1, pos, AliveType.HUMAN);

        pos = new Coordinate(7, 8);
        game.addCrew(player1, pos, AliveType.HUMAN);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        player4.setStatePlayer(StatePlayerType.IN_GAME);

        BoxStore store = new BoxStore();
        LinkedList<Box> currentBoxesWonT = new LinkedList<>();
        LinkedList<BoxType> currentBoxesWonTypeT = new LinkedList<BoxType>();
        currentBoxesWonTypeT.add(BoxType.YELLOW);
        currentBoxesWonTypeT.add(BoxType.GREEN);
        currentBoxesWonTypeT.add(BoxType.BLUE);
        Card trafficker = new Trafficker(0, store, 4, 1, 2, currentBoxesWonT, currentBoxesWonTypeT, "prova", "commento", true);
        LinkedList<String> currentOrder = new LinkedList<>();
        currentOrder.add("Rosso");
        currentOrder.add("Blu");
        currentOrder.add("Verde");
        currentOrder.add("Giallo");
        game.getCurrentState().setCurrentCard(trafficker);
        game.getCurrentCard().setCurrentOrder(currentOrder);


        try {
            game.getCurrentCard().choiceDoubleCannon(game, player1, new ArrayList<>(),new ArrayList<>() );
        } catch (IllegalPhaseException e) {
            throw new RuntimeException(e);
        } catch (IllegalRemoveException e) {
            throw new RuntimeException(e);
        }
        assertEquals(StateCardType.REMOVE, game.getCurrentState().getCurrentCard().getStateCard());

        try {
            game.getCurrentCard().removeBattery(game, player1, battery1);
        } catch (IllegalRemoveException e) {
            throw new RuntimeException(e);
        }

        assertEquals(StateCardType.REMOVE, game.getCurrentState().getCurrentCard().getStateCard());

        try {
            game.getCurrentCard().removeBattery(game, player1, battery1);
        } catch (IllegalRemoveException e) {
            throw new RuntimeException(e);
        }

        assertEquals(StateCardType.CHOICE_ATTRIBUTES, game.getCurrentState().getCurrentCard().getStateCard());
    }
}