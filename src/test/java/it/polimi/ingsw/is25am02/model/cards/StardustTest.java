package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.Spaceship;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StardustTest {

    public Game make_a_spaceship(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
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
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, id1);
        try {
            spaceship1.addTile(7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 2 - cabin
        TileType t2 = TileType.CABIN;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Tile cabin2 = new Cabin(t2, connectors2, rotationType2, id2);
        try {
            spaceship1.addTile(8,7, cabin2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - motor
        TileType t3 = TileType.MOTOR;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        Tile motor3 = new Motors(t3, connectors3, rotationType3, id3);
        try {
            spaceship1.addTile(7,8, motor3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - battery
        TileType t4 = TileType.BATTERY;
        ConnectorType[] connectors4 = {ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.SINGLE, ConnectorType.NONE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        int maxBattery = 3;
        Tile battery4 = new BatteryStorage(t4, connectors4, rotationType4, id4, maxBattery);
        try {
            spaceship1.addTile(6,8, battery4);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - shield
        TileType t5 = TileType.SHIELD;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        boolean[] shielded = {true, false, false, true};
        Tile shield5 = new Shield(t5, connectors5, rotationType5, id5, shielded);
        try {
            spaceship1.addTile(6,7, shield5);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cannon
        TileType t6 = TileType.CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cannon6 = new Cannon(t6, connectors6, rotationType6, id6);
        try {
            spaceship1.addTile(7,6, cannon6);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 7 - specialstorage
        TileType t7 = TileType.SPECIAL_STORAGE;
        ConnectorType[] connectors7 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType7 = RotationType.NORTH;
        int id7 = 1;
        int maxNum = 2;
        Tile specialStorage7 = new SpecialStorage(t7, connectors7, rotationType7, id7, maxNum);
        try {
            spaceship1.addTile(9,7, specialStorage7);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.addCrew(player1, 7,7, AliveType.HUMAN);
        game.addCrew(player1, 8,7,AliveType.HUMAN);

        //inizializzo spaceship2
        //tile 1 - cabin centrale
        TileType t21 = TileType.CABIN;
        ConnectorType[] connectors21 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType21 = RotationType.NORTH;
        int id21 = 1;
        Tile cabin21 = new Cabin(t21, connectors21, rotationType21, id21);
        try {
            spaceship2.addTile(7,7, cabin21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.addCrew(player2, 7,7, AliveType.HUMAN);

        //inizializzo spaceship3
        //tile 1 - cabin centrale
        TileType t31 = TileType.CABIN;
        ConnectorType[] connectors31 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType31 = RotationType.NORTH;
        int id31 = 1;
        Tile cabin31 = new Cabin(t31, connectors31, rotationType31, id31);
        try {
            spaceship3.addTile(7,7, cabin31);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.addCrew(player3, 7,7, AliveType.HUMAN);

        //inizializzo spaceship4
        //tile 1 - cabin centrale
        TileType t41 = TileType.CABIN;
        ConnectorType[] connectors41 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType41 = RotationType.NORTH;
        int id41 = 1;
        Tile cabin41 = new Cabin(t41, connectors41, rotationType41, id41);
        try {
            spaceship4.addTile(7,7, cabin41);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.addCrew(player4, 7,7, AliveType.HUMAN);
        return game;
    }

    @Test
    void test_Stardust() {
        Game game = make_a_spaceship();
        int calcul1 = game.getPlayers().getFirst().getSpaceship().calculateExposedConnectors();
        int calcul2 = game.getPlayers().get(1).getSpaceship().calculateExposedConnectors();
        int calcul3 = game.getPlayers().get(2).getSpaceship().calculateExposedConnectors();
        int calcul4 = game.getPlayers().get(3).getSpaceship().calculateExposedConnectors();


        assertEquals(calcul1, 2);
        assertEquals(calcul2, 4);
        assertEquals(calcul3, 4);
        assertEquals(calcul4, 4);

        HashMap<Player,Integer> correct = new HashMap<>();
        correct.put(game.getPlayers().getFirst(), 2);
        correct.put(game.getPlayers().get(1), -2);
        correct.put(game.getPlayers().get(2), -3);
        correct.put(game.getPlayers().get(3), -4);

        //create card
        int level = 0;
        Card stardust = new Stardust(level);
        game.getCurrentState().setCurrentCard(stardust);
        game.getCurrentState().setCurrentPlayer(game.getPlayers().getFirst());

        stardust.effect(game);

        assertEquals(true, game.getGameboard().getPositions().get(game.getPlayers().getFirst()).equals(correct.get(game.getPlayers().getFirst())));
        assertEquals(true, game.getGameboard().getPositions().get(game.getPlayers().get(1)).equals(correct.get(game.getPlayers().get(1))));
        assertEquals(true, game.getGameboard().getPositions().get(game.getPlayers().get(2)).equals(correct.get(game.getPlayers().get(2))));
        assertEquals(true, game.getGameboard().getPositions().get(game.getPlayers().get(3)).equals(correct.get(game.getPlayers().get(3))));
        assertEquals(true, game.getGameboard().getPositions().equals(correct));
    }



}