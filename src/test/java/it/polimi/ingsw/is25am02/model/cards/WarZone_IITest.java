package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.GreenBox;
import it.polimi.ingsw.is25am02.model.cards.boxes.YellowBox;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WarZone_IITest {

    @Test
    void test_should_check_if_the_one_with_less_cannons_lose_days_of_flight(){
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
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            spaceship1.addTile(player1.getNickname(),7,7, cabin1);
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
            spaceship1.addTile(player1.getNickname(),6,7, storage1);
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
            spaceship1.addTile(player1.getNickname(),8,7, battery3);
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
            spaceship1.addTile(player1.getNickname(),8,6, dcannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cannon 6 6
        TileType t6 = TileType.CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cannon= new Cannon(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(),6,6, cannon);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship2
        //tile 1 - cabin centrale 7 7
        TileType t21 = TileType.CABIN;
        ConnectorType[] connectors21 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType21 = RotationType.NORTH;
        int id21 = 1;
        Tile cabin21 = new Cabin(t21, connectors21, rotationType21, null);
        try {
            spaceship2.addTile(player1.getNickname(),7,7, cabin21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 3 - battery 8 7
        TileType t23 = TileType.BATTERY;
        ConnectorType[] connectors23 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType23 = RotationType.NORTH;
        int id23 = 1;
        int maxNum23 = 2;
        Tile battery23 = new BatteryStorage(t23, connectors23, rotationType23, null, maxNum23);
        try {
            spaceship2.addTile(player1.getNickname(),8,7, battery23);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship3
        //tile 1 - cabin centrale 7 7
        TileType t31 = TileType.CABIN;
        ConnectorType[] connectors31 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType31 = RotationType.NORTH;
        int id31 = 1;
        Tile cabin31 = new Cabin(t31, connectors31, rotationType31, null);
        try {
            spaceship3.addTile(player1.getNickname(),7,7, cabin31);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 2 - cabin umani 7 8
        TileType t32 = TileType.CABIN;
        ConnectorType[] connectors32 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType32 = RotationType.NORTH;
        int id32 = 1;
        Tile cabin32 = new Cabin(t32, connectors32, rotationType32, null);
        try {
            spaceship3.addTile(player1.getNickname(),7,8, cabin32);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship4
        //tile 1 - cabin centrale 7 7
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
        int boxeslost = 2;
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        Pair<Integer,RotationType> shot1 = new Pair<>(1,RotationType.NORTH);
        shots.add(shot1);
        Card warzone2 = new WarZone_II(level,flyback, boxeslost, shots, null, null, true);

        game.getCurrentState().setCurrentCard(warzone2);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
        LinkedList<String> nicknames = new LinkedList<>(
                game.getGameboard().getRanking().stream()
                        .map(Player::getNickname)
                        .toList()
        );
        game.getCurrentState().getCurrentCard().setCurrentOrder(nicknames);

        Coordinate position = new Coordinate(8,6);
        List<Coordinate> coordcannon = new ArrayList<>();
        coordcannon.add(position);
        position = new Coordinate(8,7);
        List<Coordinate> coordbatt = new ArrayList<>();
        coordbatt.add(position);

        game.choiceDoubleCannon(player1, coordcannon, coordbatt);
        List<Coordinate> coordcannon1 = new ArrayList<>();
        List<Coordinate> coordbatt1 = new ArrayList<>();
        game.choiceDoubleCannon(player2, coordcannon1, coordbatt1);
        game.choiceDoubleCannon(player3, coordcannon1, coordbatt1);
        game.choiceDoubleCannon(player4, coordcannon1, coordbatt1);
        //devo vedere che il giocatore con meno cannoni subisce i colpi
        //player 1 ha 1 doppi cannoni in 8,6 e singolo in 6,6, ha potenza 3
        //player 2 ha potenza 0
        //player 3 ha potenza 0
        //player 4 ha potenza 0

        //il giocatore 2 dovrebbe perdere 3 giorni di volo
        assertEquals(-2, game.getGameboard().getPositions().get(player2));
    }

    @Test
    void test_should_check_if_the_one_with_less_motor_lose_boxes(){
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
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            spaceship1.addTile(player1.getNickname(), 7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 2 - battery 7 6
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType3 = RotationType.NORTH;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(),7,6, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - dmotor 8 7
        TileType t5 = TileType.D_MOTOR;
        ConnectorType[] connectors5 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        Tile dmotor1 = new DoubleMotor(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, dmotor1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - dmotor 8 8
        TileType t6 = TileType.D_MOTOR;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType6 = RotationType.NORTH;
        Tile dmotor2= new DoubleMotor(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(),8,8, dmotor2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - motor 8 9
        TileType t7 = TileType.D_MOTOR;
        ConnectorType[] connectors7 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType7 = RotationType.NORTH;
        Tile motor= new DoubleMotor(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(),8,9, motor);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship2
        //tile 1 - cabin centrale 7 7
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


        //tile 2 - Storage 7 6
        TileType t22 = TileType.STORAGE;
        ConnectorType[] connectors22 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType22 = RotationType.NORTH;
        int maxNum2 = 3;
        Tile storage21 = new Storage(t22, connectors22, rotationType22, null, maxNum2);
        GreenBox greenbox = new GreenBox(BoxType.GREEN);
        YellowBox yellowbox = new YellowBox(BoxType.YELLOW);
        try { //aggiungo box verde
            storage21.addBox(greenbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        try { //aggiungo box giallo
            storage21.addBox(yellowbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        try {
            spaceship2.addTile(player1.getNickname(), 7,6, storage21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 8 7
        TileType t23 = TileType.BATTERY;
        ConnectorType[] connectors23 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType23 = RotationType.NORTH;
        int id23 = 1;
        int maxNum23 = 2;
        Tile battery23 = new BatteryStorage(t23, connectors23, rotationType23, null, maxNum23);
        try {
            spaceship2.addTile(player1.getNickname(), 8,7, battery23);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship3
        //tile 1 - cabin centrale 7 7
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

        //tile 2 - cabin umani 7 8
        TileType t32 = TileType.CABIN;
        ConnectorType[] connectors32 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType32 = RotationType.NORTH;
        int id32 = 1;
        Tile cabin32 = new Cabin(t32, connectors32, rotationType32, null);
        try {
            spaceship3.addTile(player1.getNickname(), 7,8, cabin32);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - dmotor 6 6
        TileType t33 = TileType.D_MOTOR;
        ConnectorType[] connectors33 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType33 = RotationType.NORTH;
        int id33 = 1;
        Tile dmotor33= new DoubleMotor(t33, connectors33, rotationType33, null);
        try {
            spaceship3.addTile(player1.getNickname(),6,6, dmotor33);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - battery 8 7
        TileType t34 = TileType.BATTERY;
        ConnectorType[] connectors34 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType34 = RotationType.NORTH;
        int id34 = 1;
        int maxNum34 = 2;
        Tile battery34 = new BatteryStorage(t34, connectors34, rotationType34, null, maxNum34);
        try {
            spaceship3.addTile(player1.getNickname(),8,7, battery34);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship4
        //tile 1 - cabin centrale 7 7
        TileType t41 = TileType.CABIN;
        ConnectorType[] connectors41 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType41 = RotationType.NORTH;
        int id41 = 1;
        Tile cabin41 = new Cabin(t41, connectors41, rotationType41, null);
        try {
            spaceship4.addTile(player1.getNickname(),7,7, cabin41);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 2 - motor 7 6
        TileType t42 = TileType.MOTOR;
        ConnectorType[] connectors42 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType42 = RotationType.NORTH;
        int id42 = 1;
        Tile dmotor42 = new DoubleMotor(t42, connectors42, rotationType42, null);
        try {
            spaceship4.addTile(player1.getNickname(),7,6, dmotor42);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - Storage 6 7 c'è un box verde e uno giallo
        TileType t43 = TileType.STORAGE;
        ConnectorType[] connectors43 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType43 = RotationType.NORTH;
        int id43 = 1;
        int maxNum4 = 3;
        Tile storage43 = new Storage(t43, connectors43, rotationType43, null, maxNum4);
        GreenBox tilegreenbox = new GreenBox(BoxType.GREEN);
        YellowBox tileyellowbox = new YellowBox(BoxType.YELLOW);
        try { //aggiungo box verde
            storage43.addBox(tilegreenbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        try { //aggiungo box giallo
            storage43.addBox(tileyellowbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        try {
            spaceship4.addTile(player1.getNickname(),6,7, storage43);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //finished spaceships and initializing them
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        for(Player p : players){
            p.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        }
        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        for(Player p : players){
            game.ready(p);
        }

        //create card
        int level = 2;
        int flyback = 4;
        int boxesLost = 3;
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        Card warzone2 = new WarZone_II(level,flyback, boxesLost, shots, null, null, true);

        game.getCurrentState().setCurrentCard(warzone2);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        LinkedList<String> nicknames = new LinkedList<>(
                game.getGameboard().getRanking().stream()
                        .map(Player::getNickname)
                        .toList()
        );
        game.getCurrentState().getCurrentCard().setCurrentOrder(nicknames);
        game.getCurrentState().setCurrentPlayer(player1);
        try {
            warzone2.setCurrentPhase(2);
        } catch (IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }

        //player 1 ha 2 doppi motori in 8,6 e 6,6, ha potenza 4, batteria in 8,7
        Coordinate position = new Coordinate(8,7);
        Coordinate pos1 = new Coordinate(8,8);
        Coordinate pos2 = new Coordinate(7,6);
        Coordinate pos7 = new Coordinate(7,6);

        List<Coordinate> coordmot1 = new ArrayList<>();
        coordmot1.add(position);
        coordmot1.add(pos1);

        List<Coordinate> coordbatt1 = new ArrayList<>();
        coordbatt1.add(pos2);
        coordbatt1.add(pos7);

        game.choiceDoubleMotor(player1, coordmot1, coordbatt1);

        game.choiceDoubleMotor(player2, new ArrayList<>(), new ArrayList<>());

        game.choiceDoubleMotor(player3, new ArrayList<>(), new ArrayList<>());

        //player 4 ha 1 motore singolo in 7 6, ha potenza 1
        List<Coordinate> coordmot4 = new ArrayList<>();
        List<Coordinate> coordbatt4 = new ArrayList<>();

        game.choiceDoubleMotor(player4, coordmot4, coordbatt4);


        assertEquals(StateCardType.REMOVE, game.getCurrentState().getCurrentCard().getStateCard());
        //devo vedere che il giocatore con meno motori perde 2 box (nel mio caso il player 2 perde 3 box)
        Coordinate pos3 = new Coordinate(7,6);
        game.removeBox(player2,pos3,BoxType.YELLOW);
        game.removeBox(player2,pos3,BoxType.GREEN);

        Coordinate pos4 = new Coordinate(8,7);

        game.removeBattery(player2, pos4);

        assertTrue(player2.getSpaceship().noBox());
        assertEquals(player2.getSpaceship().getTile(8,7).get().getNumBattery(), 1);
        assertEquals(StateCardType.CHOICE_ATTRIBUTES, game.getCurrentState().getCurrentCard().getStateCard());
    }

    @Test
    void test_should_check_if_the_one_with_less_humans_take_shots(){
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
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            spaceship1.addTile(player1.getNickname(), 7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 2 - battery 7 6
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType3 = RotationType.NORTH;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(),7,6, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - dmotor 8 7
        TileType t5 = TileType.D_MOTOR;
        ConnectorType[] connectors5 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        Tile dmotor1 = new DoubleMotor(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, dmotor1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - dmotor 8 8
        TileType t6 = TileType.D_MOTOR;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType6 = RotationType.NORTH;
        Tile dmotor2= new DoubleMotor(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(),8,8, dmotor2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - motor 8 9
        TileType t7 = TileType.D_MOTOR;
        ConnectorType[] connectors7 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType7 = RotationType.NORTH;
        Tile motor= new DoubleMotor(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(),8,9, motor);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship2
        //tile 1 - cabin centrale 7 7
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


        //tile 2 - Storage 7 6
        TileType t22 = TileType.STORAGE;
        ConnectorType[] connectors22 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType22 = RotationType.NORTH;
        int maxNum2 = 3;
        Tile storage21 = new Storage(t22, connectors22, rotationType22, null, maxNum2);
        GreenBox greenbox = new GreenBox(BoxType.GREEN);
        YellowBox yellowbox = new YellowBox(BoxType.YELLOW);
        try { //aggiungo box verde
            storage21.addBox(greenbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        try { //aggiungo box giallo
            storage21.addBox(yellowbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        try {
            spaceship2.addTile(player1.getNickname(), 7,6, storage21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 8 7
        TileType t23 = TileType.BATTERY;
        ConnectorType[] connectors23 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType23 = RotationType.NORTH;
        int id23 = 1;
        int maxNum23 = 2;
        Tile battery23 = new BatteryStorage(t23, connectors23, rotationType23, null, maxNum23);
        try {
            spaceship2.addTile(player1.getNickname(), 8,7, battery23);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship3
        //tile 1 - cabin centrale 7 7
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

        //tile 2 - cabin umani 7 8
        TileType t32 = TileType.CABIN;
        ConnectorType[] connectors32 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType32 = RotationType.NORTH;
        int id32 = 1;
        Tile cabin32 = new Cabin(t32, connectors32, rotationType32, null);
        try {
            spaceship3.addTile(player1.getNickname(), 7,8, cabin32);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - dmotor 6 6
        TileType t33 = TileType.D_MOTOR;
        ConnectorType[] connectors33 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType33 = RotationType.NORTH;
        int id33 = 1;
        Tile dmotor33= new DoubleMotor(t33, connectors33, rotationType33, null);
        try {
            spaceship3.addTile(player1.getNickname(),6,6, dmotor33);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - battery 8 7
        TileType t34 = TileType.BATTERY;
        ConnectorType[] connectors34 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType34 = RotationType.NORTH;
        int id34 = 1;
        int maxNum34 = 2;
        Tile battery34 = new BatteryStorage(t34, connectors34, rotationType34, null, maxNum34);
        try {
            spaceship3.addTile(player1.getNickname(),8,7, battery34);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship4
        //tile 1 - cabin centrale 7 7
        TileType t41 = TileType.CABIN;
        ConnectorType[] connectors41 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType41 = RotationType.NORTH;
        int id41 = 1;
        Tile cabin41 = new Cabin(t41, connectors41, rotationType41, null);
        try {
            spaceship4.addTile(player1.getNickname(),7,7, cabin41);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 2 - motor 7 6
        TileType t42 = TileType.MOTOR;
        ConnectorType[] connectors42 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType42 = RotationType.NORTH;
        int id42 = 1;
        Tile dmotor42 = new DoubleMotor(t42, connectors42, rotationType42, null);
        try {
            spaceship4.addTile(player1.getNickname(),7,6, dmotor42);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - Storage 6 7 c'è un box verde e uno giallo
        TileType t43 = TileType.STORAGE;
        ConnectorType[] connectors43 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType43 = RotationType.NORTH;
        int id43 = 1;
        int maxNum4 = 3;
        Tile storage43 = new Storage(t43, connectors43, rotationType43, null, maxNum4);
        GreenBox tilegreenbox = new GreenBox(BoxType.GREEN);
        YellowBox tileyellowbox = new YellowBox(BoxType.YELLOW);
        try { //aggiungo box verde
            storage43.addBox(tilegreenbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        try { //aggiungo box giallo
            storage43.addBox(tileyellowbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        try {
            spaceship4.addTile(player1.getNickname(),6,7, storage43);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //finished spaceships and initializing them
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        for(Player p : players){
            p.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        }
        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        for(Player p : players){
            game.ready(p);
        }

        //create card
        int level = 2;
        int flyback = 4;
        int boxesLost = 3;
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        shots.add(new Pair<>(1,RotationType.NORTH));
        Card warzone2 = new WarZone_II(level,flyback, boxesLost, shots, null, null, true);

        game.getCurrentState().setCurrentCard(warzone2);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        LinkedList<String> nicknames = new LinkedList<>(
                game.getGameboard().getRanking().stream()
                        .map(Player::getNickname)
                        .toList()
        );
        game.getCurrentState().getCurrentCard().setCurrentOrder(nicknames);
        game.getCurrentState().setCurrentPlayer(player1);
        try {
            warzone2.setCurrentPhase(3);
        } catch (IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(StateCardType.CHOICE_ATTRIBUTES, game.getCurrentState().getCurrentCard().getStateCard());

        for(Player p: players){
            game.choiceCrew(p);
        }

        assertEquals(StateCardType.ROLL, game.getCurrentState().getCurrentCard().getStateCard());

        game.rollDice(player1);
        game.setDiceResultManually(8);

        game.calculateDamage(player1,new Coordinate(0,0));

        game.keepBlock(player1, new Coordinate(7,7));

        assertEquals(2,player1.getSpaceship().getNumOfWastedTiles());

    }

}