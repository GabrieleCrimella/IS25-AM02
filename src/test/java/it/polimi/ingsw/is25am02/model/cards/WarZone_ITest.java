package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.cards.boxes.GreenBox;
import it.polimi.ingsw.is25am02.model.cards.boxes.YellowBox;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WarZone_ITest {

    @Test
    void test_should_check_if_the_one_with_less_humans_lose_days_of_flight(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        player1.setStatePlayer(StatePlayerType.FINISHED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        player2.setStatePlayer(StatePlayerType.FINISHED);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        player3.setStatePlayer(StatePlayerType.FINISHED);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        player4.setStatePlayer(StatePlayerType.FINISHED);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 0 con i 4 players
        Game game = new Game(players,2);
        //inizializzo
        game.getGameboard().initializeGameBoard(players);

        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
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


        //tile 2 - Storage 7 6
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, id2, maxNum);
        try {
            spaceship1.addTile(7,6, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 3 - battery 8 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, id3, maxNum3);
        try {
            spaceship1.addTile(8,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - dcannon 8 6
        TileType t5 = TileType.D_CANNON;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile dcannon1 = new DoubleCannon(t5, connectors5, rotationType5, id5);
        try {
            spaceship1.addTile(8,6, dcannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - dcannon 6 6
        TileType t6 = TileType.D_CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile dcannon2= new DoubleCannon(t6, connectors6, rotationType6, id6);
        try {
            spaceship1.addTile(6,6, dcannon2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship2
        //tile 1 - cabin centrale 7 7
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


        //tile 2 - Storage 7 6
        TileType t22 = TileType.STORAGE;
        ConnectorType[] connectors22 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType22 = RotationType.NORTH;
        int id22 = 1;
        int maxNum2 = 3;
        Tile storage21 = new Storage(t22, connectors22, rotationType22, id22, maxNum2);
        try {
            spaceship2.addTile(7,6, storage21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship3
        //tile 1 - cabin centrale 7 7
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

        //tile 2 - cabin umani 7 8
        TileType t32 = TileType.CABIN;
        ConnectorType[] connectors32 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType32 = RotationType.NORTH;
        int id32 = 1;
        Tile cabin32 = new Cabin(t32, connectors32, rotationType32, id32);
        try {
            spaceship3.addTile(7,8, cabin32);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //finished spaceships and initializing them
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        Coordinate pos = new Coordinate(7,7);
        game.addCrew(player1, pos, AliveType.HUMAN);
        game.addCrew(player2, pos, AliveType.HUMAN);

        game.addCrew(player3, pos, AliveType.HUMAN);
        pos = new Coordinate(7,8);
        game.addCrew(player3, pos, AliveType.HUMAN);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 0;
        int flyback = 3;
        int aliveLost = 3;
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        Card warzone1 = new WarZone_I(level,flyback, aliveLost, shots);

        game.getCurrentState().setCurrentCard(warzone1);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);


        assertEquals(StateCardType.CHOICE_ATTRIBUTES, game.getCurrentState().getCurrentCard().getStateCard());
        //devo vedere che il giocatore con meno umani perde 3 giorni di volo
        //player 1 ha 1 umano
        //player 2 ha 2 umani
        //player 3 ha 4 umani
        //player 4 ha zero umani
        for(Player p: players){
            try {
                warzone1.choiceCrew(game,p);
            } catch (IllegalPhaseException e) {
                System.out.println(e.getMessage());
            }
        }
        assertEquals(-3, game.getGameboard().getPositions().get(player4));


    }

    @Test
    void test_should_check_if_the_one_with_less_motor_lose_humans(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        player1.setStatePlayer(StatePlayerType.FINISHED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        player2.setStatePlayer(StatePlayerType.FINISHED);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        player3.setStatePlayer(StatePlayerType.FINISHED);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        player4.setStatePlayer(StatePlayerType.FINISHED);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 0 con i 4 players
        Game game = new Game(players,2);
        //inizializzo
        game.getGameboard().initializeGameBoard(players);

        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
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


        //tile 2 - Storage 7 6
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, id2, maxNum);
        try {
            spaceship1.addTile(7,6, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 8 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, id3, maxNum3);
        try {
            spaceship1.addTile(8,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - dmotor 8 6
        TileType t5 = TileType.D_MOTOR;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile dmotor1 = new DoubleMotor(t5, connectors5, rotationType5, id5);
        try {
            spaceship1.addTile(8,6, dmotor1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - dmotor 6 6
        TileType t6 = TileType.D_MOTOR;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile dmotor2= new DoubleMotor(t6, connectors6, rotationType6, id6);
        try {
            spaceship1.addTile(6,6, dmotor2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship2
        //tile 1 - cabin centrale 7 7
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


        //tile 2 - Storage 7 6
        TileType t22 = TileType.STORAGE;
        ConnectorType[] connectors22 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType22 = RotationType.NORTH;
        int id22 = 1;
        int maxNum2 = 3;
        Tile storage21 = new Storage(t22, connectors22, rotationType22, id22, maxNum2);
        try {
            spaceship2.addTile(7,6, storage21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 8 7
        TileType t23 = TileType.BATTERY;
        ConnectorType[] connectors23 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType23 = RotationType.NORTH;
        int id23 = 1;
        int maxNum23 = 2;
        Tile battery23 = new BatteryStorage(t23, connectors23, rotationType23, id23, maxNum23);
        try {
            spaceship2.addTile(8,7, battery23);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - dmotor 6 6
        TileType t24 = TileType.D_MOTOR;
        ConnectorType[] connectors24 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType24 = RotationType.NORTH;
        int id24 = 1;
        Tile dmotor24= new DoubleMotor(t24, connectors24, rotationType24, id24);
        try {
            spaceship2.addTile(6,6, dmotor24);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship3
        //tile 1 - cabin centrale 7 7
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

        //tile 2 - cabin umani 7 8
        TileType t32 = TileType.CABIN;
        ConnectorType[] connectors32 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType32 = RotationType.NORTH;
        int id32 = 1;
        Tile cabin32 = new Cabin(t32, connectors32, rotationType32, id32);
        try {
            spaceship3.addTile(7,8, cabin32);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - dmotor 6 6
        TileType t33 = TileType.D_MOTOR;
        ConnectorType[] connectors33 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType33 = RotationType.NORTH;
        int id33 = 1;
        Tile dmotor33= new DoubleMotor(t33, connectors33, rotationType33, id33);
        try {
            spaceship3.addTile(6,6, dmotor33);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - battery 8 7
        TileType t34 = TileType.BATTERY;
        ConnectorType[] connectors34 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType34 = RotationType.NORTH;
        int id34 = 1;
        int maxNum34 = 2;
        Tile battery34 = new BatteryStorage(t34, connectors34, rotationType34, id34, maxNum34);
        try {
            spaceship3.addTile(8,7, battery34);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship4
        //tile 1 - cabin centrale 7 7
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

        //tile 2 - motor 7 6
        TileType t42 = TileType.MOTOR;
        ConnectorType[] connectors42 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType42 = RotationType.NORTH;
        int id42 = 1;
        Tile dmotor42 = new DoubleMotor(t42, connectors42, rotationType42, id42);
        try {
            spaceship4.addTile(7,6, dmotor42);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //finished spaceships and initializing them
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        Coordinate pos = new Coordinate(7,7);
        game.addCrew(player1, pos, AliveType.HUMAN);
        game.addCrew(player2, pos, AliveType.HUMAN);
        game.addCrew(player3, pos, AliveType.HUMAN);
        game.addCrew(player4, pos, AliveType.HUMAN);

        pos = new Coordinate(7,8);
        game.addCrew(player3, pos, AliveType.HUMAN);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        player4.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 0;
        int flyback = 3;
        int aliveLost = 1;
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        Card warzone1 = new WarZone_I(level,flyback, aliveLost, shots);

        game.getCurrentState().setCurrentCard(warzone1);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
        game.getCurrentState().setCurrentPlayer(player1);
        try {
            warzone1.setCurrentPhase(2);
        } catch (IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }

        //player 1 ha 2 doppi motori in 8,6 e 6,6, ha potenza 4, batteria in 8,7
        Coordinate position = new Coordinate(8,6);
        List<Coordinate> coordmot1 = new ArrayList<>();
        coordmot1.add(position);
        position = new Coordinate(6,6);
        coordmot1.add(position);
        position = new Coordinate(8,7);
        List<Coordinate> coordbatt1 = new ArrayList<>();
        coordbatt1.add(position);
        coordbatt1.add(position);

        game.choiceDoubleMotor(player1, coordmot1, coordbatt1);

        game.getCurrentState().setCurrentPlayer(player2);

        //player 2 ha 1 motore doppio in 6,6, ha potenza 2, batteria in 8,7
        position = new Coordinate(6,6);
        List<Coordinate> coordmot2 = new ArrayList<>();
        coordmot2.add(position);
        position = new Coordinate(8,7);
        List<Coordinate> coordbatt2 = new ArrayList<>();
        coordbatt2.add(position);

        game.choiceDoubleMotor(player2, coordmot2, coordbatt2);

        game.getCurrentState().setCurrentPlayer(player3);

        //player 3 ha 1 motore doppio in 6,6, ha potenza 2, batteria in 8,7
        position = new Coordinate(6,6);
        List<Coordinate> coordmot3 = new ArrayList<>();
        coordmot3.add(position);
        position = new Coordinate(8,7);
        List<Coordinate> coordbatt3 = new ArrayList<>();
        coordbatt3.add(position);

        game.choiceDoubleMotor(player3, coordmot3, coordbatt3);

        game.getCurrentState().setCurrentPlayer(player4);

        //player 4 ha 1 motore singolo in 7 6, ha potenza 1
        List<Coordinate> coordmot4 = new ArrayList<>();
        List<Coordinate> coordbatt4 = new ArrayList<>();

        game.choiceDoubleMotor(player4, coordmot4, coordbatt4);


        assertEquals(StateCardType.REMOVE, game.getCurrentState().getCurrentCard().getStateCard());
        //devo vedere che il giocatore con meno motori perde 1 umani (nel mio caso il player 4 perde 1 umano)

        game.removeCrew(player4,new Coordinate(7,7));

        assertEquals(1, game.getPlayers().get(3).getSpaceship().calculateNumAlive());
    }

    @Test
    void test_should_check_if_the_one_with_less_cannon_take_shots_without_keep_blocks(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        player1.setStatePlayer(StatePlayerType.FINISHED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        player2.setStatePlayer(StatePlayerType.FINISHED);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        player3.setStatePlayer(StatePlayerType.FINISHED);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        player4.setStatePlayer(StatePlayerType.FINISHED);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 2 con i 4 players
        Game game = new Game(players,2);
        //inizializzo
        game.getGameboard().initializeGameBoard(players);

        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
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


        //tile 2 - Storage 6 7
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.SINGLE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, id2, maxNum);
        try {
            spaceship1.addTile(6,7, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 3 - battery 8 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.DOUBLE, ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, id3, maxNum3);
        try {
            spaceship1.addTile(8,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - dcannon 8 6
        TileType t5 = TileType.D_CANNON;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile dcannon1 = new DoubleCannon(t5, connectors5, rotationType5, id5);
        try {
            spaceship1.addTile(8,6, dcannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cannon 6 6
        TileType t6 = TileType.CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cannon= new Cannon(t6, connectors6, rotationType6, id6);
        try {
            spaceship1.addTile(6,6, cannon);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship2
        //tile 1 - cabin centrale 7 7
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


        //tile 3 - battery 8 7
        TileType t23 = TileType.BATTERY;
        ConnectorType[] connectors23 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType23 = RotationType.NORTH;
        int id23 = 1;
        int maxNum23 = 2;
        Tile battery23 = new BatteryStorage(t23, connectors23, rotationType23, id23, maxNum23);
        try {
            spaceship2.addTile(8,7, battery23);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship3
        //tile 1 - cabin centrale 7 7
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

        //tile 2 - cabin umani 7 8
        TileType t32 = TileType.CABIN;
        ConnectorType[] connectors32 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType32 = RotationType.NORTH;
        int id32 = 1;
        Tile cabin32 = new Cabin(t32, connectors32, rotationType32, id32);
        try {
            spaceship3.addTile(7,8, cabin32);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship4
        //tile 1 - cabin centrale 7 7
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


        //finished spaceships and initializing them
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        Coordinate pos = new Coordinate(7,7);
        game.addCrew(player1, pos, AliveType.HUMAN);
        game.addCrew(player2, pos, AliveType.HUMAN);

        game.addCrew(player3, pos, AliveType.HUMAN);
        game.addCrew(player4, pos, AliveType.HUMAN);
        pos = new Coordinate(7,8);
        game.addCrew(player3, pos, AliveType.HUMAN);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        player4.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 1;
        int flyback = 3;
        int aliveLost = 2;
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        Pair<Integer,RotationType> shot1 = new Pair<>(1,RotationType.NORTH);
        shots.add(shot1);
        Card warzone1 = new WarZone_I(level,flyback, aliveLost, shots);

        game.getCurrentState().setCurrentCard(warzone1);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        Coordinate position = new Coordinate(8,6);
        List<Coordinate> coordcannon = new ArrayList<>();
        coordcannon.add(position);
        position = new Coordinate(8,7);
        List<Coordinate> coordbatt = new ArrayList<>();
        coordbatt.add(position);


        try {
            warzone1.setCurrentPhase(3);
        } catch (IllegalPhaseException e) {
            throw new RuntimeException(e);
        }

        game.choiceDoubleCannon(player1, coordcannon, coordbatt);
        List<Coordinate> coordcannon1 = new ArrayList<>();
        List<Coordinate> coordbatt1 = new ArrayList<>();
        game.choiceDoubleCannon(player2, coordcannon1, coordbatt1);
        game.choiceDoubleCannon(player3, coordcannon1, coordbatt1);
        game.choiceDoubleCannon(player4, coordcannon1, coordbatt1);
        assertEquals(StateCardType.ROLL, game.getCurrentState().getCurrentCard().getStateCard());
        //devo vedere che il giocatore con meno cannoni subisce i colpi
        //player 1 ha 1 doppi cannoni in 8,6 e singolo in 6,6, ha potenza 3
        //player 2 ha potenza 0
        //player 3 ha potenza 0
        //player 4 ha potenza 0

        try {
            warzone1.setCurrentPhase(3);
        } catch (IllegalPhaseException e) {
            throw new RuntimeException(e);
        }
        game.getCurrentState().getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
        Coordinate pos1 = new Coordinate(0,0);
        try {
            warzone1.setCurrentPhase(4);
        } catch (IllegalPhaseException e) {
            throw new RuntimeException(e);
        }
        game.calculateDamage(player2,pos1);

        /*try {
            warzone1.keepBlocks(game, player3, new Coordinate(7, 8)); //voglio tenere la parte di nave composta dalla casella 7 8
        } catch (IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }*/
        //non c'è bisogno di chiamare keep blocks perchè la nave non si spezza in più pezzi


        //dovrebbe subire gli shots solo il giocatore che è piu avanti di quelli a parimerito, quindi il player 2
        assertEquals(1, player2.getSpaceship().getNumOfWastedTiles());
    }

    @Test
    void test_should_check_if_the_one_with_less_cannon_take_shots_with_keep_blocks(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        player1.setStatePlayer(StatePlayerType.FINISHED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        player2.setStatePlayer(StatePlayerType.FINISHED);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        player3.setStatePlayer(StatePlayerType.FINISHED);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        player4.setStatePlayer(StatePlayerType.FINISHED);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 2 con i 4 players
        Game game = new Game(players,2);
        //inizializzo
        game.getGameboard().initializeGameBoard(players);

        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
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


        //tile 2 - Storage 6 7
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.SINGLE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, id2, maxNum);
        try {
            spaceship1.addTile(6,7, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 3 - battery 8 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.DOUBLE, ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, id3, maxNum3);
        try {
            spaceship1.addTile(8,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - dcannon 8 6
        TileType t5 = TileType.D_CANNON;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile dcannon1 = new DoubleCannon(t5, connectors5, rotationType5, id5);
        try {
            spaceship1.addTile(8,6, dcannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cannon 6 6
        TileType t6 = TileType.CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cannon= new Cannon(t6, connectors6, rotationType6, id6);
        try {
            spaceship1.addTile(6,6, cannon);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship2
        //tile 1 - cabin centrale 7 7
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


        //tile 2 - Storage 6 7
        TileType t22 = TileType.STORAGE;
        ConnectorType[] connectors22 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType22 = RotationType.NORTH;
        int id22 = 1;
        int maxNum2 = 3;
        Tile storage21 = new Storage(t22, connectors22, rotationType22, id22, maxNum2);
        try {
            spaceship2.addTile(6,7, storage21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 8 7
        TileType t23 = TileType.BATTERY;
        ConnectorType[] connectors23 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType23 = RotationType.NORTH;
        int id23 = 1;
        int maxNum23 = 2;
        Tile battery23 = new BatteryStorage(t23, connectors23, rotationType23, id23, maxNum23);
        try {
            spaceship2.addTile(8,7, battery23);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship3
        //tile 1 - cabin centrale 7 7
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

        //tile 2 - cabin umani 7 8
        TileType t32 = TileType.CABIN;
        ConnectorType[] connectors32 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType32 = RotationType.NORTH;
        int id32 = 1;
        Tile cabin32 = new Cabin(t32, connectors32, rotationType32, id32);
        try {
            spaceship3.addTile(7,8, cabin32);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship4
        //tile 1 - cabin centrale 7 7
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


        //finished spaceships and initializing them
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        Coordinate pos = new Coordinate(7,7);
        game.addCrew(player1, pos, AliveType.HUMAN);
        game.addCrew(player2, pos, AliveType.HUMAN);

        game.addCrew(player3, pos, AliveType.HUMAN);
        game.addCrew(player4, pos, AliveType.HUMAN);
        pos = new Coordinate(7,8);
        game.addCrew(player3, pos, AliveType.HUMAN);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        player4.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 1;
        int flyback = 3;
        int aliveLost = 2;
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        Pair<Integer,RotationType> shot1 = new Pair<>(1,RotationType.NORTH);
        shots.add(shot1);
        Card warzone1 = new WarZone_I(level,flyback, aliveLost, shots);

        game.getCurrentState().setCurrentCard(warzone1);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        Coordinate position = new Coordinate(8,6);
        List<Coordinate> coordcannon = new ArrayList<>();
        coordcannon.add(position);
        position = new Coordinate(8,7);
        List<Coordinate> coordbatt = new ArrayList<>();
        coordbatt.add(position);


        try {
            warzone1.setCurrentPhase(3);
        } catch (IllegalPhaseException e) {
            throw new RuntimeException(e);
        }

        game.choiceDoubleCannon(player1, coordcannon, coordbatt);
        List<Coordinate> coordcannon1 = new ArrayList<>();
        List<Coordinate> coordbatt1 = new ArrayList<>();
        game.choiceDoubleCannon(player2, coordcannon1, coordbatt1);
        game.choiceDoubleCannon(player3, coordcannon1, coordbatt1);
        game.choiceDoubleCannon(player4, coordcannon1, coordbatt1);
        assertEquals(StateCardType.ROLL, game.getCurrentState().getCurrentCard().getStateCard());
        //devo vedere che il giocatore con meno cannoni subisce i colpi
        //player 1 ha 1 doppi cannoni in 8,6 e singolo in 6,6, ha potenza 3
        //player 2 ha potenza 0
        //player 3 ha potenza 0
        //player 4 ha potenza 0

        try {
            warzone1.setCurrentPhase(3);
        } catch (IllegalPhaseException e) {
            throw new RuntimeException(e);
        }
        game.getCurrentState().getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
        Coordinate pos1 = new Coordinate(0,0);
        try {
            warzone1.setCurrentPhase(4);
        } catch (IllegalPhaseException e) {
            throw new RuntimeException(e);
        }
        game.calculateDamage(player2,pos1);

        try {
            warzone1.keepBlocks(game, player2, new Coordinate(6, 7)); //voglio tenere la parte di nave composta dalla casella 7 8
        } catch (IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }


        //dovrebbe subire gli shots solo il giocatore che è piu avanti di quelli a parimerito, quindi il player 2
        assertEquals(2, player2.getSpaceship().getNumOfWastedTiles());
    }
}