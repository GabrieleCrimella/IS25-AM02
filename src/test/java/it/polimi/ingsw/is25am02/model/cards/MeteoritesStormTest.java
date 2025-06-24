package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeteoritesStormTest {

    @Test
    void test_should_check_remove_single_tile_and_return_rest_of_ship() {
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED, null, 1);
        player1.setStatePlayer(StatePlayerType.FINISHED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE, null, 1);
        player2.setStatePlayer(StatePlayerType.FINISHED);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN, null, 1);
        player3.setStatePlayer(StatePlayerType.FINISHED);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW, null, 1);
        player4.setStatePlayer(StatePlayerType.FINISHED);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 0 con i 4 players
        Game game = new Game(players, 2);
        //inizializzo
        game.getGameboard().initializeGameBoard(players);

        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            spaceship1.addTile(player1.getNickname(),7, 7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 2 - Storage 6 7
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.SINGLE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname(),6, 7, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 3 - battery 8 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.DOUBLE, ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(),8, 7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - dcannon 8 6
        TileType t5 = TileType.D_CANNON;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile dcannon1 = new DoubleCannon(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(),8, 6, dcannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cannon 6 6
        TileType t6 = TileType.CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cannon = new Cannon(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(),6, 6, cannon);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 7 - cabin 7 8
        TileType t7 = TileType.CABIN;
        ConnectorType[] connectors7 = {ConnectorType.UNIVERSAL, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.NONE};
        RotationType rotationType7 = RotationType.NORTH;
        int id7 = 1;
        Tile cabin7 = new Cabin(t7, connectors7, rotationType7, null);
        try {
            spaceship1.addTile(player1.getNickname(),7, 8, cabin7);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
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

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        int level = 2;
        ArrayList<Pair<Integer, RotationType>> meteorites = new ArrayList<>();
        Pair<Integer,RotationType> meteor1 = new Pair<>(0,RotationType.NORTH);
        meteorites.add(meteor1);
        Card meteoritesStorm = new MeteoritesStorm(level, meteorites, null, null, true);

        game.getCurrentState().setCurrentCard(meteoritesStorm);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        game.calculateDamage(player1,new Coordinate(8,7));

        try {
            meteoritesStorm.keepBlocks(game,player1,new Coordinate(6,6)); //voglio tenere la parte di nave composta dalla casella 7,8
        } catch (IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(5, player1.getSpaceship().getNumOfWastedTiles());

    }

    @Test
    void test_should_check_one_big_from_north_with_double_cannon() {
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED, null, 1);
        player1.setStatePlayer(StatePlayerType.FINISHED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE, null, 1);
        player2.setStatePlayer(StatePlayerType.FINISHED);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN, null, 1);
        player3.setStatePlayer(StatePlayerType.FINISHED);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW, null, 1);
        player4.setStatePlayer(StatePlayerType.FINISHED);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 0 con i 4 players
        Game game = new Game(players, 2);
        //inizializzo
        game.getGameboard().initializeGameBoard(players);

        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            spaceship1.addTile(player1.getNickname(),7, 7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 2 - Storage 6 7
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.SINGLE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname(),6, 7, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 3 - battery 8 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.DOUBLE, ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(),8, 7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - dcannon 8 6
        TileType t5 = TileType.D_CANNON;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile dcannon1 = new DoubleCannon(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(),8, 6, dcannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cannon 6 6
        TileType t6 = TileType.CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cannon = new Cannon(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(),6, 6, cannon);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 7 - cabin 7 8
        TileType t7 = TileType.CABIN;
        ConnectorType[] connectors7 = {ConnectorType.UNIVERSAL, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.NONE};
        RotationType rotationType7 = RotationType.NORTH;
        int id7 = 1;
        Tile cabin7 = new Cabin(t7, connectors7, rotationType7, null);
        try {
            spaceship1.addTile(player1.getNickname(),7, 8, cabin7);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
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

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        int level = 2;
        ArrayList<Pair<Integer, RotationType>> meteorites = new ArrayList<>();
        Pair<Integer, RotationType> meteor1 = new Pair<>(1, RotationType.NORTH);
        meteorites.add(meteor1);
        Card meteoritesStorm = new MeteoritesStorm(level, meteorites, null, null, true);

        game.getCurrentState().setCurrentCard(meteoritesStorm);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        game.calculateDamage(player1, new Coordinate(8, 7));

        /*try {
            meteoritesStorm.keepBlocks(game, player1, new Coordinate(6, 6)); //voglio tenere la parte di nave composta dalla casella 7,8
        } catch (IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }*/
        assertEquals(0, player1.getSpaceship().getNumOfWastedTiles());
        assertEquals(1, player1.getSpaceship().getTile(8,7).get().getNumBattery());
    }
}