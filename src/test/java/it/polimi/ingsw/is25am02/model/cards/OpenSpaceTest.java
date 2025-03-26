package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.Spaceship;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.scene.chart.BarChart;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceTest {

    @Test
    void test_should_check_choiceDoubleMotor_when_first_player_activates_motor(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        player4.setStatePlayer(StatePlayerType.IN_GAME);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 0 con i 4 players
        Game game = new Game(players,0);
        //inizializzo
        game.getGameboard().initializeGameBoard(players);

        //creo la carta
        Card openSpace = new OpenSpace(0);
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
        //tile 2 - motor 7 8
        TileType t2 = TileType.MOTOR;
        ConnectorType[] connectors2 = {ConnectorType.UNIVERSAL, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Tile motor1 = new Motors(t2, connectors2, rotationType2, id2);
        try {
            spaceship1.addTile(7,8, motor1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 3 - dmotor 8 8
        TileType t3 = TileType.D_MOTOR;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        Tile dmotor1 = new DoubleMotor(t3, connectors3, rotationType3, id3);
        try {
            spaceship1.addTile(8,8, dmotor1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 4 - motor 7 8
        TileType t4 = TileType.D_MOTOR;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile dmotor2 = new DoubleMotor(t4, connectors4, rotationType4, id4);
        try {
            spaceship1.addTile(6,8, dmotor2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 5 - battery 9 8
        TileType t5 = TileType.BATTERY;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        int maxNum = 3;
        Tile battery1 = new BatteryStorage(t5, connectors5, rotationType5, id5, maxNum);
        try {
            spaceship1.addTile(8,9, battery1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        Pair<Tile, Tile> CoppiaDiMotorEBattery = new Pair<>(dmotor1, battery1);
        List<Pair<Tile, Tile>> listaDiCoppieDiDMotorEBattery = new ArrayList<Pair<Tile,Tile>>();
        listaDiCoppieDiDMotorEBattery.add(CoppiaDiMotorEBattery);
        Optional<List<Pair<Tile,Tile>>> optionalListaDiCoppieDiMotorEBattery = Optional.of(listaDiCoppieDiDMotorEBattery);

        try {
            openSpace.choiceDoubleMotor(game,player1,optionalListaDiCoppieDiMotorEBattery);
        } catch (IllegalPhaseException e) {
            System.out.println(e.getMessage());
        } catch (IllegalRemoveException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(7, game.getGameboard().getPositions().get(player1),7);
        assertEquals(2, game.getPlayers().getFirst().getSpaceship().getTile(8,9).get().getBattery());

    }

    @Test
    void test_should_check_choiceDoubleMotor_when_first_player_activates_motor_and_second_player_doesnt(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        player4.setStatePlayer(StatePlayerType.IN_GAME);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 0 con i 4 players
        Game game = new Game(players,0);
        //inizializzo
        game.getGameboard().initializeGameBoard(players);

        //creo la carta
        Card openSpace = new OpenSpace(0);
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
        //tile 2 - motor 7 8
        TileType t2 = TileType.MOTOR;
        ConnectorType[] connectors2 = {ConnectorType.UNIVERSAL, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Tile motor1 = new Motors(t2, connectors2, rotationType2, id2);
        try {
            spaceship1.addTile(7,8, motor1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 3 - dmotor 8 8
        TileType t3 = TileType.D_MOTOR;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        Tile dmotor1 = new DoubleMotor(t3, connectors3, rotationType3, id3);
        try {
            spaceship1.addTile(8,8, dmotor1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 4 - motor 7 8
        TileType t4 = TileType.D_MOTOR;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile dmotor2 = new DoubleMotor(t4, connectors4, rotationType4, id4);
        try {
            spaceship1.addTile(6,8, dmotor2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 5 - battery 9 8
        TileType t5 = TileType.BATTERY;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        int maxNum = 3;
        Tile battery1 = new BatteryStorage(t5, connectors5, rotationType5, id5, maxNum);
        try {
            spaceship1.addTile(8,9, battery1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        Pair<Tile, Tile> CoppiaDiMotorEBattery = new Pair<>(dmotor1, battery1);
        List<Pair<Tile, Tile>> listaDiCoppieDiDMotorEBattery = new ArrayList<Pair<Tile,Tile>>();
        listaDiCoppieDiDMotorEBattery.add(CoppiaDiMotorEBattery);
        Optional<List<Pair<Tile,Tile>>> optionalListaDiCoppieDiMotorEBattery = Optional.of(listaDiCoppieDiDMotorEBattery);

        try {
            openSpace.choiceDoubleMotor(game,player1,optionalListaDiCoppieDiMotorEBattery);
        } catch (IllegalPhaseException e) {
            System.out.println(e.getMessage());
        } catch (IllegalRemoveException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(7, game.getGameboard().getPositions().get(player1),7);
        assertEquals(2, game.getPlayers().getFirst().getSpaceship().getTile(8,9).get().getBattery());


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
        //tile 2 - motor 7 8
        TileType t22 = TileType.MOTOR;
        ConnectorType[] connectors22 = {ConnectorType.UNIVERSAL, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType22 = RotationType.NORTH;
        int id22 = 1;
        Tile motor21 = new Motors(t22, connectors22, rotationType22, id22);
        try {
            spaceship2.addTile(7,8, motor21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 3 - dmotor 8 8
        TileType t23 = TileType.D_MOTOR;
        ConnectorType[] connectors23 = {ConnectorType.SINGLE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType23 = RotationType.NORTH;
        int id23 = 1;
        Tile dmotor21 = new DoubleMotor(t23, connectors23, rotationType23, id23);
        try {
            spaceship2.addTile(8,8, dmotor21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 4 - motor 7 8
        TileType t24 = TileType.D_MOTOR;
        ConnectorType[] connectors24 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType24 = RotationType.NORTH;
        int id24 = 1;
        Tile dmotor22 = new DoubleMotor(t24, connectors24, rotationType24, id24);
        try {
            spaceship2.addTile(6,8, dmotor22);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        //tile 5 - battery 9 8
        TileType t25 = TileType.BATTERY;
        ConnectorType[] connectors25 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType25 = RotationType.NORTH;
        int id25 = 1;
        int maxNum2 = 3;
        Tile battery21 = new BatteryStorage(t25, connectors25, rotationType25, id25, maxNum2);
        try {
            spaceship2.addTile(8,9, battery21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        try {
            openSpace.choiceDoubleMotor(game,player2,Optional.empty());
        } catch (IllegalPhaseException e) {
            System.out.println(e.getMessage());
        } catch (IllegalRemoveException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(3, game.getGameboard().getPositions().get(player2));
        assertEquals(3, game.getPlayers().get(1).getSpaceship().getTile(8,9).get().getBattery());

    }

}