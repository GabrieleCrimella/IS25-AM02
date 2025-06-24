package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.BlueBox;
import it.polimi.ingsw.is25am02.model.cards.boxes.RedBox;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpaceshipTest {

    public Game make_a_spaceship(){
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
            spaceship1.addTile( player1.getNickname(), 8,7, cabin2);
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

        Coordinate pos1 = new Coordinate(7,7);
        game.addCrew(player1, pos1, AliveType.HUMAN);
        Coordinate pos2 = new Coordinate(8,7);
        game.addCrew(player1, pos2,AliveType.HUMAN);
        return game;
    }
    @Test
    void check_calculate_num_alive() {
        Game game = make_a_spaceship();
        assertEquals(4, game.getPlayers().getFirst().getSpaceship().calculateNumAlive());
    }

    @Test
    void test_should_check_get_Tiles_By_Type_Cabin(){
        Game game = make_a_spaceship();
        assertEquals(2, game.getPlayers().getFirst().getSpaceship().getTilesByType(TileType.CABIN).size());
    }

    @Test
    void test_should_check_get_Tiles_By_Type_Shield(){
        Game game = make_a_spaceship();
        assertEquals(1, game.getPlayers().getFirst().getSpaceship().getTilesByType(TileType.SHIELD).size());
    }

    @Test
    void test_should_check_get_Tiles_By_Type_Cannon(){
        Game game = make_a_spaceship();
        assertEquals(1, game.getPlayers().getFirst().getSpaceship().getTilesByType(TileType.CANNON).size());
    }

    @Test
    void test_should_check_that_mask_level_0_is_correct(){
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
        game.getGameboard().initializeGameBoard(players);

        //tile 1 - cabin centrale
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        //controllo riga vuota 0
        for (int i=0;i<12;i++){
           int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 0, cabin1);});
        }
        //controllo riga vuota 1
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 1, cabin1);});
        }
        //controllo riga vuota 2
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 2, cabin1);});
        }
        //controllo riga vuota 3
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 3, cabin1);});
        }
        //controllo riga vuota 4
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 4, cabin1);});
        }
        //controllo riga 5
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 0, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 1, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 2, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 3, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 4, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 5, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 6, 5, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 7, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 8, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 9, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 10, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 11, 5, cabin1);});

        //controllo riga 6
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 0, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 1, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 2, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 3, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 4, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 5, 6, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 6, 6, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 7, 6, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 8, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 9, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 10, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 11, 6, cabin1);});

        //controllo riga 7
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 0, 7, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 1, 7, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 2, 7, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 3, 7, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 4, 7, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 5, 7, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 6, 7, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 7, 7, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 8, 7, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 9, 7, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 10, 7, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 11, 7, cabin1);});

        //controllo riga 8
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 0, 8, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 1, 8, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 2, 8, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 3, 8, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 4, 8, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 5, 8, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 6, 8, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 7, 8, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 8, 8, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 9, 8, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 10, 8, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 11, 8, cabin1);});

        //controllo riga 9
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 0, 9, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 1, 9, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 2, 9, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 3, 9, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 4, 9, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 5, 9, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 6, 9, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 7, 9, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(),8, 9, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 9, 9, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 10, 9, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 11, 9, cabin1);});

        //controllo riga vuota 10
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 10, cabin1);});
        }
        //controllo riga vuota 11
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 11, cabin1);});
        }

    }

    //todo anna
    @Test
    void test_should_check_that_mask_level_1_is_correct(){
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

        //tile 1 - cabin centrale
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        //controllo riga vuota 0
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 0, cabin1);});
        }
        //controllo riga vuota 1
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 1, cabin1);});
        }
        //controllo riga vuota 2
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 2, cabin1);});
        }
        //controllo riga vuota 3
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 3, cabin1);});
        }
        //controllo riga vuota 4
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 4, cabin1);});
        }
        //controllo riga 5
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 0, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 1, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 2, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 3, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 4, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 5, 5, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 6, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 7, 5, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 8, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 9, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 10, 5, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 11, 5, cabin1);});

        //controllo riga 6
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 0, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 1, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 2, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 3, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 4, 6, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 5, 6, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 6, 6, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 7, 6, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 8, 6, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 9, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 10, 6, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 11, 6, cabin1);});

        //controllo riga 7
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 0, 7, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname() , 1, 7, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 2, 7, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 3, 7, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 4, 7, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 5, 7, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 6, 7, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 7, 7, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 8, 7, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 9, 7, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 10, 7, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 11, 7, cabin1);});

        //controllo riga 8
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 0, 8, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 1, 8, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 2, 8, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 3, 8, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 4, 8, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 5, 8, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname() , 6, 8, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 7, 8, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 8, 8, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 9, 8, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 10, 8, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 11, 8, cabin1);});

        //controllo riga 9
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 0, 9, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 1, 9, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 2, 9, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 3, 9, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 4, 9, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 5, 9, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 6, 9, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 7, 9, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 8, 9, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 9, 9, cabin1);});
        assertDoesNotThrow( () -> {spaceship1.addTile(player1.getNickname(), 10, 9, cabin1);});
        assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), 11, 9, cabin1);});

        //controllo riga vuota 10
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 10, cabin1);});
        }
        //controllo riga vuota 11
        for (int i=0;i<12;i++){
            int x=i;
            assertThrows(IllegalArgumentException.class, () -> {spaceship1.addTile(player1.getNickname(), x, 11, cabin1);});
        }

    }

    @Test
    void test_should_check_get_Tiles_By_Type_Battery(){
        Game game = make_a_spaceship();
        assertEquals(1, game.getPlayers().getFirst().getSpaceship().getTilesByType(TileType.BATTERY).size());
    }

    @Test
    void test_should_check_get_Tiles_By_Type_Motor(){
        Game game = make_a_spaceship();
        assertEquals(1, game.getPlayers().getFirst().getSpaceship().getTilesByType(TileType.MOTOR).size());
    }

    @Test
    void test_should_check_if_tiles_are_placed_correctly(){
        Game game = make_a_spaceship();
        assertEquals(TileType.SHIELD, game.getPlayers().getFirst().getSpaceship().getTile(6,7).get().getType());
        assertEquals(TileType.BATTERY, game.getPlayers().getFirst().getSpaceship().getTile(6,8).get().getType());
    }

    @Test
    void test_should_check_is_Shielded(){
        Game game = make_a_spaceship();
        assertEquals(true, game.getPlayers().getFirst().getSpaceship().isShielded(RotationType.NORTH));
        assertEquals(true, game.getPlayers().getFirst().getSpaceship().isShielded(RotationType.WEST));
        assertEquals(false, game.getPlayers().getFirst().getSpaceship().isShielded(RotationType.SOUTH));
        assertEquals(false, game.getPlayers().getFirst().getSpaceship().isShielded(RotationType.EAST));
    }

    @Test
    void test_get_Up_tile(){
        Game game = make_a_spaceship();
        Spaceship spaceship = game.getPlayers().getFirst().getSpaceship();
        assertEquals(TileType.CANNON, spaceship.getSpaceshipIterator().getUpTile(spaceship.getTile(7,7).get()).get().getType());
    }

    @Test
    void test_should_check_calculate_exposed_connectors(){
        Game game = make_a_spaceship();
        assertEquals(3, game.getPlayers().getFirst().getSpaceship().calculateExposedConnectors());
    }


    /*
    @Test
    void test_should_load_spaceship_mask() {
        boolean[][] matrix_test_01 = {
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, true, false, false, false, false},
                {false, false, false, false, false, false, true, true, true, false, false, false},
                {false, false, false, false, false, true, true, true, true, true, false, false},
                {false, false, false, false, false, true, true, true, true, true, false, false},
                {false, false, false, false, false, true, true, false, true, true, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false}
        };

        boolean[][] matrix_test_2 = {
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, true, false, true, false, false, false},
                {false, false, false, false, false, true, true, true, true, true, false, false},
                {false, false, false, false, true, true, true, true, true, true, true, false},
                {false, false, false, false, true, true, true, true, true, true, true, false},
                {false, false, false, false, true, true, true, false, true, true, true, false},
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false}
        };
        
        Spaceship spaceship = new Spaceship(0);
        assert(Arrays.deepEquals(spaceship.getMaskSpaceship(), matrix_test_01));
    }

     */



    @Test
    void test_should_check_calculateMotorPower(){
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
        //tile 2 - motor 7 8
        TileType t2 = TileType.MOTOR;
        ConnectorType[] connectors2 = {ConnectorType.UNIVERSAL, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Tile motor1 = new Motors(t2, connectors2, rotationType2, null);
        try {
            spaceship1.addTile(player1.getNickname(), 7,8, motor1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 3 - dmotor 8 8
        TileType t3 = TileType.D_MOTOR;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        Tile dmotor1 = new DoubleMotor(t3, connectors3, rotationType3, null);
        try {
            spaceship1.addTile(player1.getNickname(), 8,8, dmotor1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 4 - motor 7 8
        TileType t4 = TileType.D_MOTOR;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile dmotor2 = new DoubleMotor(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname() , 6,8, dmotor2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        List<Tile> listaDoppiMotoriAttivi = new ArrayList<>();
        listaDoppiMotoriAttivi.add(dmotor2);

        assertEquals(3,spaceship1.calculateMotorPower(listaDoppiMotoriAttivi));

        //accendo anche l'altro cannone
        listaDoppiMotoriAttivi.add(dmotor1);
        assertEquals(5,spaceship1.calculateMotorPower(listaDoppiMotoriAttivi));

    }

    @Test
    void test_should_check_calculateCannonPower(){
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
        game.getGameboard().initializeGameBoard(players);

        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
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

        //tile 2 - Storage 7 6
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 2;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname(), 7,6, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 8 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 3;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - cannon 7 5
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname(), 7,5, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - dcannon 8 6
        TileType t5 = TileType.D_CANNON;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile dcannon1= new DoubleCannon(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(), 8,6, dcannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - dcannon 6 6
        TileType t6 = TileType.D_CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile dcannon2= new DoubleCannon(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(), 6,6, dcannon2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        List<Tile> listaDoppiCannoniAttivi = new ArrayList<>();
        listaDoppiCannoniAttivi.add(dcannon1);

        assertEquals(3, spaceship1.calculateCannonPower(listaDoppiCannoniAttivi));
        listaDoppiCannoniAttivi.add(dcannon2);
        assertEquals(5, spaceship1.calculateCannonPower(listaDoppiCannoniAttivi));

    }

    @Test
    void test_should_check_if_check_spaceship_works_for_a_correct_spaceship(){
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

        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
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


        //tile 2 - Storage 5 8
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname(), 5,8, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 6 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(), 6,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - cannon 9 7
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname(), 9,7, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - cabin 8 7
        TileType t5 = TileType.CABIN;
        ConnectorType[] connectors5 = {ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile cabin2 = new Cabin(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, cabin2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cabin 5 7
        TileType t6 = TileType.CABIN;
        ConnectorType[] connectors6 = {ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cabin3 = new Cabin(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(), 5,7, cabin3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.shipFinished(player1);
        game.shipFinished(player2);
        game.shipFinished(player3);
        game.shipFinished(player4);

        assertEquals(true, spaceship1.checkSpaceship());


    }

    @Test
    void test_should_check_if_check_spaceship_works_for_an_incorrect_spaceship_with_wrong_connectors(){
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

        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            spaceship1.addTile(player1.getNickname() , 7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 2 - Storage 5 8
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname(), 5,8, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 6 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(), 6,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - cannon 9 7
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname(), 9,7, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - cabin 8 7
        TileType t5 = TileType.CABIN;
        ConnectorType[] connectors5 = {ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile cabin2 = new Cabin(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, cabin2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cabin 5 7
        TileType t6 = TileType.CABIN;
        ConnectorType[] connectors6 = {ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cabin3 = new Cabin(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(), 5,7, cabin3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.shipFinished(player1);
        game.shipFinished(player2);
        game.shipFinished(player3);
        game.shipFinished(player4);

        assertEquals(false, spaceship1.checkSpaceship());


    }

    @Test
    void test_should_check_if_check_spaceship_works_for_an_incorrect_spaceship_with_wrong_connectors2(){
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

        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
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


        //tile 2 - Storage 5 8
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname(), 5,8, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 6 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(), 6,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - cannon 9 7
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname(), 9,7, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - cabin 8 7
        TileType t5 = TileType.CABIN;
        ConnectorType[] connectors5 = {ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile cabin2 = new Cabin(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, cabin2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cabin 5 7
        TileType t6 = TileType.CABIN;
        ConnectorType[] connectors6 = {ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cabin3 = new Cabin(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(), 5,7, cabin3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.shipFinished(player1);
        game.shipFinished(player2);
        game.shipFinished(player3);
        game.shipFinished(player4);

        assertEquals(false, spaceship1.checkSpaceship());


    }

    @Test
    void test_should_check_if_check_spaceship_works_for_an_incorrect_spaceship(){
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

        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
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


        //tile 2 - Storage 5 8
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname(), 5,8, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 6 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(), 6,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - cannon 9 7
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname(), 9,7, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - cabin 8 7
        TileType t5 = TileType.CABIN;
        ConnectorType[] connectors5 = {ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile cabin2 = new Cabin(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, cabin2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cabin 5 7
        TileType t6 = TileType.CABIN;
        ConnectorType[] connectors6 = {ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cabin3 = new Cabin(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(), 5,7, cabin3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cabin 5 7
        TileType t7 = TileType.CABIN;
        ConnectorType[] connectors7 = {ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType7 = RotationType.NORTH;
        int id7 = 1;
        Tile cabin4 = new Cabin(t7, connectors7, rotationType7, null);
        try {
            spaceship1.addTile(player1.getNickname(), 6,9, cabin4);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.shipFinished(player1);
        game.shipFinished(player2);
        game.shipFinished(player3);
        game.shipFinished(player4);

        assertEquals(false, spaceship1.checkSpaceship());


    }


    @Test
    void test_should_check_isExposed_works(){

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

        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            spaceship1.addTile(player1.getNickname() , 7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 2 - Storage 5 8
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname(), 5,8, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 6 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(), 6,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - cannon 9 7
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname(), 9,7, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - cabin 8 7
        TileType t5 = TileType.CABIN;
        ConnectorType[] connectors5 = {ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile cabin2 = new Cabin(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, cabin2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cabin 5 7
        TileType t6 = TileType.CABIN;
        ConnectorType[] connectors6 = {ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cabin3 = new Cabin(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(), 5,7, cabin3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        player1.setStatePlayer(StatePlayerType.IN_GAME);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        player4.setStatePlayer(StatePlayerType.IN_GAME);
        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);

        assertEquals(true,spaceship1.isExposed(RotationType.NORTH, 7));
        assertEquals(true,spaceship1.isExposed(RotationType.NORTH, 8));
        assertEquals(false,spaceship1.isExposed(RotationType.NORTH, 9));
        assertEquals(true,spaceship1.isExposed(RotationType.EAST, 7));
        assertEquals(true,spaceship1.isExposed(RotationType.WEST, 7));
        assertEquals(true,spaceship1.isExposed(RotationType.SOUTH, 5));



    }

    @Test
    void test_should_check_if_isMostExpensive_works(){
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
            spaceship1.addTile(player1.getNickname(), 9,7, specialStorage7);
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
        Coordinate  pos3 = new Coordinate(7,7);


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

        Coordinate pos5  = new Coordinate(7,7);

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


        Coordinate pos4 = new Coordinate(7,7);

        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        Coordinate pos1 = new Coordinate(7,7);
        Coordinate pos2 = new Coordinate(8,7);

        BlueBox blueBox = new BlueBox(BoxType.BLUE);
        try {
            specialStorage7.addBox(blueBox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(false, spaceship1.isMostExpensive(BoxType.RED));
        assertEquals(true, spaceship1.isMostExpensive(BoxType.BLUE));
        RedBox redBox = new RedBox(BoxType.RED);
        try {
            specialStorage7.addBox(redBox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(true, spaceship1.isMostExpensive(BoxType.RED));




    }

}