package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceTest {

    @Test
    void test_should_check_choiceDoubleMotor_when_first_player_activates_motor(){
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
            spaceship1.addTile(player1.getNickname(),7,7, cabin1);
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
            spaceship1.addTile(player1.getNickname(),7,8, motor1);
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
            spaceship1.addTile(player1.getNickname(),8,8, dmotor1);
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
            spaceship1.addTile(player1.getNickname(),6,8, dmotor2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 5 - battery 9 8
        TileType t5 = TileType.BATTERY;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        int maxNum = 3;
        Tile battery1 = new BatteryStorage(t5, connectors5, rotationType5, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname(),8,9, battery1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);

        Coordinate pos4 = new Coordinate(7,7);
        game.addCrew(player1, pos4, AliveType.HUMAN);

        player1.setStatePlayer(StatePlayerType.IN_GAME);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        player4.setStatePlayer(StatePlayerType.IN_GAME);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);

        //creo la carta
        Card openSpace = new OpenSpace(2, null, null, true);

        game.getCurrentState().setCurrentCard(openSpace);
        game.getCurrentState().setCurrentPlayer(game.getPlayers().getFirst());

        //doppio motore in 88 batteria in 98
        List<Coordinate> dMotAttivi= new ArrayList<>();
        Coordinate pos88 = new Coordinate(8,8);
        dMotAttivi.add(pos88);
        List<Coordinate> batterieUsate= new ArrayList<>();
        Coordinate pos98 = new Coordinate(8,9);
        batterieUsate.add(pos98);

        game.choiceDoubleMotor(player1, dMotAttivi, batterieUsate);

        assertEquals(10, game.getGameboard().getPositions().get(player1),7);
        assertEquals(2, game.getPlayers().getFirst().getSpaceship().getTile(8,9).get().getBattery());

    }

    @Test
    void test_should_check_choiceDoubleMotor_when_first_player_activates_motor_and_second_player_doesnt(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED, null, 1);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE, null, 1);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN, null, 1);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW, null, 1);
        player4.setStatePlayer(StatePlayerType.IN_GAME);
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
            spaceship1.addTile(player1.getNickname(),7,7, cabin1);
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
            spaceship1.addTile(player1.getNickname(),7,8, motor1);
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
            spaceship1.addTile(player1.getNickname(),8,8, dmotor1);
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
            spaceship1.addTile(player1.getNickname(),6,8, dmotor2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 5 - battery 9 8
        TileType t5 = TileType.BATTERY;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        int maxNum = 3;
        Tile battery1 = new BatteryStorage(t5, connectors5, rotationType5, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname(),8,9, battery1);
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
            spaceship2.addTile(player2.getNickname(), 7,7, cabin21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 2 - motor 7 8
        TileType t22 = TileType.MOTOR;
        ConnectorType[] connectors22 = {ConnectorType.UNIVERSAL, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType22 = RotationType.NORTH;
        int id22 = 1;
        Tile motor21 = new Motors(t22, connectors22, rotationType22, null);
        try {
            spaceship2.addTile(player2.getNickname(),7,8, motor21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 3 - dmotor 8 8
        TileType t23 = TileType.D_MOTOR;
        ConnectorType[] connectors23 = {ConnectorType.SINGLE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType23 = RotationType.NORTH;
        int id23 = 1;
        Tile dmotor21 = new DoubleMotor(t23, connectors23, rotationType23, null);
        try {
            spaceship2.addTile(player2.getNickname(),8,8, dmotor21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 4 - dmotor 6 8
        TileType t24 = TileType.D_MOTOR;
        ConnectorType[] connectors24 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType24 = RotationType.NORTH;
        int id24 = 1;
        Tile dmotor22 = new DoubleMotor(t24, connectors24, rotationType24, null);
        try {
            spaceship2.addTile(player2.getNickname(),6,8, dmotor22);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 5 - battery 9 8
        TileType t25 = TileType.BATTERY;
        ConnectorType[] connectors25 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType25 = RotationType.NORTH;
        int id25 = 1;
        int maxNum2 = 3;
        Tile battery21 = new BatteryStorage(t25, connectors25, rotationType25, null, maxNum2);
        try {
            spaceship2.addTile(player2.getNickname(),8,9, battery21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);

        Coordinate pos4 = new Coordinate(7,7);
        game.addCrew(player1, pos4, AliveType.HUMAN);
        game.addCrew(player2,pos4,AliveType.HUMAN);

        player1.setStatePlayer(StatePlayerType.IN_GAME);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        player4.setStatePlayer(StatePlayerType.IN_GAME);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);

        //creo la carta
        Card openSpace = new OpenSpace(2, null, null, true);

        game.getCurrentState().setCurrentCard(openSpace);
        game.getCurrentState().setCurrentPlayer(game.getPlayers().getFirst());

        //doppio motore in 88 batteria in 98
        List<Coordinate> dMotAttivi= new ArrayList<>();
        Coordinate pos88 = new Coordinate(8,8);
        dMotAttivi.add(pos88);
        List<Coordinate> batterieUsate= new ArrayList<>();
        Coordinate pos98 = new Coordinate(8,9);
        batterieUsate.add(pos98);

        game.choiceDoubleMotor(player1, dMotAttivi, batterieUsate);

        //il secondo non attiva i doppi motori
        List<Coordinate> dMotAttivi2= new ArrayList<>();
        List<Coordinate> batterieUsate2= new ArrayList<>();

        game.choiceDoubleMotor(player2, dMotAttivi2, batterieUsate2);

        assertEquals(10, game.getGameboard().getPositions().get(player1),7);
        assertEquals(2, game.getPlayers().getFirst().getSpaceship().getTile(8,9).get().getBattery());

        assertEquals(4, game.getGameboard().getPositions().get(player2));
        assertEquals(3, game.getPlayers().get(1).getSpaceship().getTile(8,9).get().getBattery());

    }

}