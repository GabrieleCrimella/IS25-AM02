package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.*;
import it.polimi.ingsw.is25am02.model.exception.AlreadyViewingException;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TraffickerTest {
/*
    @Test
    void test_should_check_if_player_doesnt_have_enough_cannon_power(){
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


        //tile 2 - Storage 7 6 c'è un box verde e uno giallo
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, id2, maxNum);
        GreenBox tilegreenbox = new GreenBox(BoxType.GREEN);
        YellowBox tileyellowbox = new YellowBox(BoxType.YELLOW);
        try { //aggiungo box verde
            storage1.addBox(tilegreenbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        try { //aggiungo box giallo
            storage1.addBox(tileyellowbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
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
        //finished spaceships and initializing them
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        Coordinate pos = new Coordinate(7,7);
        game.addCrew(player1, pos, AliveType.HUMAN);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 0;
        int cannonPowers = 3;
        int daysLost = 1;
        int boxLost = 2 ;
        LinkedList<Box> boxWon = new LinkedList<>();
        boxWon.add(new YellowBox(BoxType.YELLOW));
        boxWon.add(new YellowBox(BoxType.GREEN));
        LinkedList<BoxType> boxWonTypes = new LinkedList<>();
        boxWonTypes.add(BoxType.YELLOW);
        boxWonTypes.add(BoxType.GREEN);
        BoxStore store = new BoxStore();
        Card trafficker = new Trafficker(0,store, cannonPowers, daysLost,boxLost,boxWon, boxWonTypes);

        game.getCurrentState().setCurrentCard(trafficker);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        Coordinate position = new Coordinate(8,6);
        List<Coordinate> coordcannon = new ArrayList<>();
        coordcannon.add(position);
        position = new Coordinate(8,7);
        List<Coordinate> coordbatt = new ArrayList<>();
        coordbatt.add(position);


        game.choiceDoubleCannon(player1, coordcannon, coordbatt);
        assertEquals(StateCardType.REMOVE, game.getCurrentState().getCurrentCard().getStateCard());
        position = new Coordinate(7,6);
        game.removeBox(player1,position,BoxType.YELLOW);
        game.removeBox(player1,position,BoxType.GREEN);
        assertTrue(spaceship1.noBox());

    }

    @Test
    void test_should_check_if_player_has_enough_cannon_power_and_uses_it(){
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


        //tile 2 - Storage 7 6 c'è un box verde e uno giallo
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, id2, maxNum);
        GreenBox tilegreenbox = new GreenBox(BoxType.GREEN);
        YellowBox tileyellowbox = new YellowBox(BoxType.YELLOW);
        try { //aggiungo box verde
            storage1.addBox(tilegreenbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        try { //aggiungo box giallo
            storage1.addBox(tileyellowbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
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
        //finished spaceships and initializing them
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        Coordinate pos = new Coordinate(7,7);
        game.addCrew(player1, pos, AliveType.HUMAN);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 2;
        int cannonPowers = 3;
        int daysLost = 1;
        int boxLost = 2 ;
        LinkedList<Box> boxWon = new LinkedList<>();
        boxWon.add(new YellowBox(BoxType.YELLOW));
        boxWon.add(new GreenBox(BoxType.GREEN));
        LinkedList<BoxType> boxWonTypes = new LinkedList<>();
        boxWonTypes.add(BoxType.YELLOW);
        boxWonTypes.add(BoxType.GREEN);
        BoxStore store = new BoxStore();
        Card trafficker = new Trafficker(2,store, cannonPowers, daysLost,boxLost,boxWon, boxWonTypes);

        game.getCurrentState().setCurrentCard(trafficker);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        Coordinate position = new Coordinate(8,6);
        List<Coordinate> coordcannon = new ArrayList<>();
        coordcannon.add(position);
        position = new Coordinate(6,6);
        coordcannon.add(position);

        position = new Coordinate(8,7);
        List<Coordinate> coordbatt = new ArrayList<>();
        coordbatt.add(position);
        position = new Coordinate(8,7);
        coordbatt.add(position);


        game.choiceDoubleCannon(player1, coordcannon, coordbatt);
        assertEquals(StateCardType.DECISION, game.getCurrentState().getCurrentCard().getStateCard());
        //il giocatore vince contro i contrabbandieri e guadagna 2 box ma perde 1 giorno di volo

        game.choiceBox(player1,true);
        List<Box> checkBoxWon = trafficker.getBoxesWon();
        List<Box> correctWonBox = new ArrayList<>();
        Box yb = new YellowBox(BoxType.YELLOW);
        Box gb = new GreenBox(BoxType.GREEN);
        correctWonBox.add(yb);
        correctWonBox.add(gb);
        assertEquals(checkBoxWon.size(), correctWonBox.size()); //controllo che ha guadagnato le box

        assertEquals(5,game.getGameboard().getPositions().get(player1)); //mi aspetto che si muova indietro di 1

    }

    @Test
    void test_should_check_if_player_has_enough_cannon_power_and_doesnt_want_the_boxes(){
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
        //finished spaceships and initializing them
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        Coordinate pos = new Coordinate(7,7);
        game.addCrew(player1, pos, AliveType.HUMAN);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 0;
        int cannonPowers = 3;
        int daysLost = 1;
        int boxLost = 2 ;
        LinkedList<Box> boxWon = new LinkedList<>();
        boxWon.add(new YellowBox(BoxType.YELLOW));
        boxWon.add(new GreenBox(BoxType.GREEN));
        LinkedList<BoxType> boxWonTypes = new LinkedList<>();
        boxWonTypes.add(BoxType.YELLOW);
        boxWonTypes.add(BoxType.GREEN);
        BoxStore store = new BoxStore();
        Card trafficker = new Trafficker(2,store, cannonPowers, daysLost,boxLost,boxWon, boxWonTypes);

        game.getCurrentState().setCurrentCard(trafficker);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        Coordinate position = new Coordinate(8,6);
        List<Coordinate> coordcannon = new ArrayList<>();
        coordcannon.add(position);
        position = new Coordinate(6,6);
        coordcannon.add(position);

        position = new Coordinate(8,7);
        List<Coordinate> coordbatt = new ArrayList<>();
        coordbatt.add(position);
        position = new Coordinate(8,7);
        coordbatt.add(position);


        game.choiceDoubleCannon(player1, coordcannon, coordbatt);
        assertEquals(StateCardType.DECISION, game.getCurrentState().getCurrentCard().getStateCard());
        //il giocatore vince contro i contrabbandieri ma non vuole guadagnare nulla, quindi non perde giorni di volo

        game.choiceBox(player1,false);
        assertTrue(game.getPlayers().getFirst().getSpaceship().noBox());


        assertEquals(6,game.getGameboard().getPositions().get(player1)); //mi aspetto che si muova indietro di 1

    }

    @Test
    void test_should_check_if_removes_batteries_when_boxes_are_finished(){
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


        //tile 2 - Storage 7 6 c'è un box verde
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, id2, maxNum);
        GreenBox tilegreenbox = new GreenBox(BoxType.GREEN);
        YellowBox tileyellowbox = new YellowBox(BoxType.YELLOW);
        try { //aggiungo box verde
            storage1.addBox(tilegreenbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
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
        //finished spaceships and initializing them
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        Coordinate pos = new Coordinate(7,7);
        game.addCrew(player1, pos, AliveType.HUMAN);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 0;
        int cannonPowers = 3;
        int daysLost = 1;
        int boxLost = 2 ;
        LinkedList<Box> boxWon = new LinkedList<>();
        boxWon.add(new YellowBox(BoxType.YELLOW));
        boxWon.add(new YellowBox(BoxType.GREEN));
        LinkedList<BoxType> boxWonTypes = new LinkedList<>();
        boxWonTypes.add(BoxType.YELLOW);
        boxWonTypes.add(BoxType.GREEN);
        BoxStore store = new BoxStore();
        Card trafficker = new Trafficker(2,store, cannonPowers, daysLost,boxLost,boxWon, boxWonTypes);

        game.getCurrentState().setCurrentCard(trafficker);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        Coordinate position = new Coordinate(8,6);
        List<Coordinate> coordcannon = new ArrayList<>();
        coordcannon.add(position);
        position = new Coordinate(8,7);
        List<Coordinate> coordbatt = new ArrayList<>();
        coordbatt.add(position);

        game.choiceDoubleCannon(player1, coordcannon, coordbatt);
        assertEquals(StateCardType.REMOVE, game.getCurrentState().getCurrentCard().getStateCard());
        position = new Coordinate(7,6);
        game.removeBox(player1,position,BoxType.GREEN);
        assertTrue(spaceship1.noBox());

        position = new Coordinate(8,7);
        game.removeBattery(player1,position);
        assertEquals(0,game.getPlayers().getFirst().getSpaceship().getTile(8,7).get().getNumBattery());

    }

    @Test
    void test_should_check_if_it_goes_to_the_next_player_when_it_is_tie(){
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


        //tile 2 - Storage 7 6 c'è un box verde e uno giallo
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, id2, maxNum);
        GreenBox tilegreenbox = new GreenBox(BoxType.GREEN);
        YellowBox tileyellowbox = new YellowBox(BoxType.YELLOW);
        try { //aggiungo box verde
            storage1.addBox(tilegreenbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        try { //aggiungo box giallo
            storage1.addBox(tileyellowbox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
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
        //finished spaceships and initializing them
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        Coordinate pos = new Coordinate(7,7);
        game.addCrew(player1, pos, AliveType.HUMAN);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 0;
        int cannonPowers = 3;
        int daysLost = 1;
        int boxLost = 2 ;
        LinkedList<Box> boxWon = new LinkedList<>();
        boxWon.add(new YellowBox(BoxType.YELLOW));
        boxWon.add(new GreenBox(BoxType.GREEN));
        LinkedList<BoxType> boxWonTypes = new LinkedList<>();
        boxWonTypes.add(BoxType.YELLOW);
        boxWonTypes.add(BoxType.GREEN);
        BoxStore store = new BoxStore();
        Card trafficker = new Trafficker(0,store, cannonPowers, daysLost,boxLost,boxWon, boxWonTypes);

        game.getCurrentState().setCurrentCard(trafficker);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        Coordinate position = new Coordinate(8,6);
        List<Coordinate> coordcannon = new ArrayList<>();
        coordcannon.add(position);
        position = new Coordinate(6,6);
        coordcannon.add(position);

        position = new Coordinate(8,7);
        List<Coordinate> coordbatt = new ArrayList<>();
        coordbatt.add(position);
        position = new Coordinate(8,7);
        coordbatt.add(position);


        game.choiceDoubleCannon(player1, coordcannon, coordbatt);
        assertEquals(StateCardType.DECISION, game.getCurrentState().getCurrentCard().getStateCard());
        //il giocatore pareggia contro i contrabbandieri e quindi non guadagna box e non perde giorno di volo

        List<Box> correctWonBox = new ArrayList<>();
        Box yb = new YellowBox(BoxType.YELLOW);
        Box gb = new GreenBox(BoxType.GREEN);
        correctWonBox.add(yb);
        correctWonBox.add(gb);
        assertEquals(storage1.getOccupation().size(), correctWonBox.size()); //controllo che i box nella sua nave siano rimasti invariati

        assertEquals(6,game.getGameboard().getPositions().get(player1)); //controllo che non perda giorni di volo
    }

 */

    @Test
    void test_should_check_if_player_does_not_have_power_and_nothing_to_lose(){
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
        assertEquals(StateCardType.CHOICE_ATTRIBUTES, game.getCurrentState().getCurrentCard().getStateCard());
    }

    @Test
    void test_should_check_if_player_does_not_have_power_and_2_batteries(){
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

    @Test
    void test_should_check_if_player_does_not_have_power_and_2_boxes(){
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
        Tile storage1 = new Storage(TileType.STORAGE, connectors2, rotationType2, null, maxNum);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), storage1);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 7,8, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        try {
            storage1.addBox(new YellowBox(BoxType.YELLOW));
        } catch (IllegalAddException e) {
            throw new RuntimeException(e);
        }
        try {
            storage1.addBox(new BlueBox(BoxType.BLUE));
        } catch (IllegalAddException e) {
            throw new RuntimeException(e);
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
            game.getCurrentCard().removeBox(game, player1, storage1, BoxType.YELLOW);
        } catch (IllegalRemoveException e) {
            throw new RuntimeException(e);
        }

        assertEquals(StateCardType.REMOVE, game.getCurrentState().getCurrentCard().getStateCard());

        try {
            game.getCurrentCard().removeBox(game, player1, storage1, BoxType.BLUE);
        } catch (IllegalRemoveException e) {
            throw new RuntimeException(e);
        }

        assertEquals(StateCardType.CHOICE_ATTRIBUTES, game.getCurrentState().getCurrentCard().getStateCard());
    }

    @Test
    void test_should_check_if_player_has_enough_power_to_tie(){
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

        //add 4 cannons
        ConnectorType[] connectors2 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType2 = RotationType.NORTH;
        int maxNum = 3;
        Tile cannon1 = new Cannon(TileType.CANNON, connectors2, rotationType2, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon1);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 6,7, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors3 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType3 = RotationType.NORTH;
        Tile cannon2 = new Cannon(TileType.CANNON, connectors3, rotationType3, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon2);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 6,8, cannon2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType4 = RotationType.NORTH;
        Tile cannon3 = new Cannon(TileType.CANNON, connectors4, rotationType4, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon3);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 6,6, cannon3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors6 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType6 = RotationType.NORTH;
        Tile storage1 = new Storage(TileType.STORAGE, connectors6, rotationType6, null, maxNum);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), storage1);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        Tile cannon4 = new Cannon(TileType.CANNON, connectors5, rotationType5, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon4);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 8,8, cannon4);
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
        assertEquals(StateCardType.CHOICE_ATTRIBUTES, game.getCurrentState().getCurrentCard().getStateCard());
    }


    @Test
    void test_should_check_if_player_has_enough_power_to_win_and_choses_not_to(){
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

        //add 4 cannons
        ConnectorType[] connectors2 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType2 = RotationType.NORTH;
        int maxNum = 3;
        Tile cannon1 = new Cannon(TileType.CANNON, connectors2, rotationType2, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon1);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 6,7, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors3 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType3 = RotationType.NORTH;
        Tile cannon2 = new Cannon(TileType.CANNON, connectors3, rotationType3, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon2);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 6,8, cannon2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType4 = RotationType.NORTH;
        Tile cannon3 = new Cannon(TileType.CANNON, connectors4, rotationType4, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon3);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 6,6, cannon3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors6 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType6 = RotationType.NORTH;
        Tile storage1 = new Storage(TileType.STORAGE, connectors6, rotationType6, null, maxNum);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), storage1);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        Tile cannon4 = new Cannon(TileType.CANNON, connectors5, rotationType5, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon4);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 8,8, cannon4);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors7 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType7 = RotationType.NORTH;
        Tile cannon5 = new Cannon(TileType.CANNON, connectors7, rotationType7, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon5);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 8,6, cannon5);
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

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);

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
        assertEquals(StateCardType.DECISION, game.getCurrentState().getCurrentCard().getStateCard());

        game.getCurrentCard().choiceBox(game, player1, true);


        assertEquals(StateCardType.BOXMANAGEMENT, game.getCurrentState().getCurrentCard().getStateCard());

        List<Box> boxList = new ArrayList<>();
        boxList.add(new BlueBox(BoxType.BLUE));
        List<Box> boxListend = new ArrayList<>();

        game.getCurrentCard().moveBox(game, player1,boxList, boxListend, BoxType.BLUE, true);
        assertEquals(1, boxListend.size());

        game.getCurrentCard().moveBox(game, player1,boxList, boxListend, BoxType.BLUE, false);
        assertEquals(StateCardType.FINISH,game.getCurrentState().getCurrentCard().getStateCard());


    }

    @Test
    void test_should_check_if_player_has_enough_power_to_win_and_uses_boxes(){
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

        //add 4 cannons
        ConnectorType[] connectors2 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType2 = RotationType.NORTH;
        int maxNum = 3;
        Tile cannon1 = new Cannon(TileType.CANNON, connectors2, rotationType2, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon1);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 6,7, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors3 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType3 = RotationType.NORTH;
        Tile cannon2 = new Cannon(TileType.CANNON, connectors3, rotationType3, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon2);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 6,8, cannon2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType4 = RotationType.NORTH;
        Tile cannon3 = new Cannon(TileType.CANNON, connectors4, rotationType4, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon3);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 6,6, cannon3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors6 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType6 = RotationType.NORTH;
        Tile storage1 = new Storage(TileType.STORAGE, connectors6, rotationType6, null, maxNum);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), storage1);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        Tile cannon4 = new Cannon(TileType.CANNON, connectors5, rotationType5, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon4);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 8,8, cannon4);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        ConnectorType[] connectors7 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType7 = RotationType.NORTH;
        Tile cannon5 = new Cannon(TileType.CANNON, connectors7, rotationType7, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cannon5);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 8,6, cannon5);
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

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);

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
        assertEquals(StateCardType.DECISION, game.getCurrentState().getCurrentCard().getStateCard());

        game.getCurrentCard().choiceBox(game, player1, false);

        assertEquals(StateCardType.FINISH,game.getCurrentState().getCurrentCard().getStateCard());


    }
}