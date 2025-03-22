package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.Spaceship;
import it.polimi.ingsw.is25am02.model.cards.boxes.BlueBox;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.cards.boxes.RedBox;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.tiles.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbbandonedStationTest {

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
        spaceship1.addTile(7,7, cabin1);

        //tile 2 - cabin
        TileType t2 = TileType.CABIN;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Tile cabin2 = new Cabin(t2, connectors2, rotationType2, id2);
        spaceship1.addTile(8,7, cabin2);

        //tile 3 - motor
        TileType t3 = TileType.MOTOR;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        Tile motor3 = new Motors(t3, connectors3, rotationType3, id3);
        spaceship1.addTile(7,8, motor3);

        //tile 4 - battery
        TileType t4 = TileType.BATTERY;
        ConnectorType[] connectors4 = {ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.SINGLE, ConnectorType.NONE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        int maxBattery = 3;
        Tile battery4 = new BatteryStorage(t4, connectors4, rotationType4, id4, maxBattery);
        spaceship1.addTile(6,8, battery4);

        //tile 5 - shield
        TileType t5 = TileType.SHIELD;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        boolean[] shielded = {true, false, false, true};
        Tile shield5 = new Shield(t5, connectors5, rotationType5, id5, shielded);
        spaceship1.addTile(6,7, shield5);

        //tile 6 - cannon
        TileType t6 = TileType.CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile cannon6 = new Cannon(t6, connectors6, rotationType6, id6);
        spaceship1.addTile(7,6, cannon6);

        //tile 7 - specialstorage
        TileType t7 = TileType.SPECIAL_STORAGE;
        ConnectorType[] connectors7 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType7 = RotationType.NORTH;
        int id7 = 1;
        int maxNum = 2;
        Tile specialStorage7 = new SpecialStorage(t7, connectors7, rotationType7, id7, maxNum);
        spaceship1.addTile(9,7, specialStorage7);

        game.addCrew(player1, 7,7,AliveType.HUMAN);
        game.addCrew(player1, 8,7,AliveType.HUMAN);
        game.addCrew(player1, 7,7,AliveType.HUMAN);
        game.addCrew(player1, 8,7,AliveType.HUMAN);
        return game;
    }

    @Test
    void test_should_check_first_person_wants_to_use_card(){
        Game game = make_a_spaceship();
        //CreateCard
        int level = 0;
        BoxStore store = new BoxStore();
        int daysLost = 1;
        int aliveNeeded = 1;
        RedBox redbox1 = new RedBox(BoxType.RED);
        RedBox redbox2 = new RedBox(BoxType.RED);
        BlueBox bluebox = new BlueBox(BoxType.BLUE);
        //lista di boxesWon con 2 rossi e un blu
        LinkedList<Box> boxesWon = new LinkedList<Box>();
        LinkedList<BoxType> boxesWonTypes = new LinkedList<>();
        boxesWon.add(redbox1);
        boxesWon.add(redbox2);
        boxesWon.add(bluebox);
        AbbandonedStation abbandonedStation = new AbbandonedStation(level, store, aliveNeeded, daysLost, boxesWon, boxesWonTypes);

        List<Box> availableBoxes = abbandonedStation.choiceBox(game, game.getPlayers().getFirst(),true );

        //correct list
        LinkedList<Box> correctAvailableBoxes = new LinkedList<Box>();
        correctAvailableBoxes.add(redbox1);
        correctAvailableBoxes.add(redbox2);
        correctAvailableBoxes.add(bluebox);

        //correctPositions
        HashMap<Player,Integer> correctPositions = new HashMap<Player,Integer>();
        correctPositions.put(game.getPlayers().get(0),3);
        correctPositions.put(game.getPlayers().get(1),2);
        correctPositions.put(game.getPlayers().get(2),1);
        correctPositions.put(game.getPlayers().get(3),0);

        assertEquals(true, game.getGameboard().getPositions().equals(correctPositions));
        assertEquals(true, availableBoxes.equals(correctAvailableBoxes));
    }

    @Test
    void test_should_check_that_moveBox_works(){
        Game game = make_a_spaceship();
        //CreateCard
        int level = 0;
        BoxStore store = new BoxStore();
        int daysLost = 1;
        int aliveNeeded = 1;
        RedBox redbox1 = new RedBox(BoxType.RED);
        RedBox redbox2 = new RedBox(BoxType.RED);
        BlueBox bluebox = new BlueBox(BoxType.BLUE);
        //lista di boxesWon con 2 rossi e un blu
        LinkedList<Box> boxesWon = new LinkedList<Box>();
        LinkedList<BoxType> boxesWonTypes = new LinkedList<>();
        boxesWon.add(redbox1);
        boxesWon.add(redbox2);
        boxesWon.add(bluebox);
        Card abbandonedStation = new AbbandonedStation(level, store, aliveNeeded, daysLost, boxesWon, boxesWonTypes);

        List<Box> availableBoxes = abbandonedStation.choiceBox(game, game.getPlayers().getFirst(),true );

        //correct avaiableboxesinitial
        LinkedList<Box> correctAvailableBoxesinitial = new LinkedList<Box>();
        correctAvailableBoxesinitial.add(redbox1);
        correctAvailableBoxesinitial.add(redbox2);
        correctAvailableBoxesinitial.add(bluebox);
        assertEquals(availableBoxes.equals(correctAvailableBoxesinitial),true);
        abbandonedStation.moveBox(game, game.getPlayers().getFirst(), abbandonedStation.getBoxesWon(), game.getPlayers().getFirst().getSpaceship().getTile(9,7).get().getOccupation(), redbox1, true);


        //correct list
        LinkedList<Box> correctAvailableBoxes = new LinkedList<Box>();
        correctAvailableBoxes.add(redbox2);
        correctAvailableBoxes.add(bluebox);

        //correct Occupation
        ArrayList<Box> correctOccupationBoxes = new ArrayList<Box>();
        correctOccupationBoxes.add(redbox1);

        //correctPositions
        HashMap<Player,Integer> correctPositions = new HashMap<Player,Integer>();
        correctPositions.put(game.getPlayers().get(0),3);
        correctPositions.put(game.getPlayers().get(1),2);
        correctPositions.put(game.getPlayers().get(2),1);
        correctPositions.put(game.getPlayers().get(3),0);

        assertEquals(true, game.getGameboard().getPositions().equals(correctPositions));
        assertEquals(true, availableBoxes.equals(correctAvailableBoxes));
        assertEquals(true, game.getPlayers().getFirst().getSpaceship().getTile(9,7).get().getOccupation().equals(correctOccupationBoxes));
    }
}