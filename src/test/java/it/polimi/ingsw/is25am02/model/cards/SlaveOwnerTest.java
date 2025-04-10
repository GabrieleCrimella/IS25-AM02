package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SlaveOwnerTest {

    @Test
    void test_should_check_if_player_doesnt_have_enough_cannon_power(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(1);
        Spaceship spaceship2 = new Spaceship(1);
        Spaceship spaceship3 = new Spaceship(1);
        Spaceship spaceship4 = new Spaceship(1);
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
        Game game = new Game(players,1);
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
        int maxNum = 2;
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

        //tile 4 - cannon 7 5
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, id4);
        try {
            spaceship1.addTile(7,5, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - dcannon 8 6
        TileType t5 = TileType.D_CANNON;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile dcannon1= new DoubleCannon(t5, connectors5, rotationType5, id5);
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
        game.addCrew(player1, pos,AliveType.HUMAN);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        player4.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 1;
        int cannonPowers = 4;
        int daysLost = 1;
        int credit = 8 ;
        int humanLost = 1;
        Card slaveOwner = new SlaveOwner(level,cannonPowers, daysLost,credit,humanLost);

        game.getCurrentState().setCurrentCard(slaveOwner);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
/*
        Queato era per quando c'erano i pair
        Pair<Tile, Tile> primaCoppia = new  Pair<>(dcannon1, battery3);
        List<Pair<Tile, Tile>> listaDoppiCannoniAttivi = new ArrayList<>();
        listaDoppiCannoniAttivi.add(primaCoppia);
        Optional<List<Pair<Tile, Tile>>> optionalListaDoppiCannoniAttivi = Optional.of(listaDoppiCannoniAttivi);

 */
        List<Coordinate> dcannonAttivi= new ArrayList<>();
        Coordinate pos86 = new Coordinate(8,6);
        dcannonAttivi.add(pos86);
        List<Coordinate> batterieUsate= new ArrayList<>();
        Coordinate pos87 = new Coordinate(8,7);
        batterieUsate.add(pos87);

        game.choiceDoubleCannon(player1, dcannonAttivi, batterieUsate);
        assertEquals(StateCardType.REMOVE, game.getCurrentState().getCurrentCard().getStateCard());
        Coordinate pos77 = new Coordinate(7,7);
        game.removeCrew(player1,pos77);
        assertEquals(1, spaceship1.calculateNumAlive());

    }

    @Test
    void test_should_check_if_player_has_enough_cannon_power_and_uses_it(){
        //test fatto per stampare un errore
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(1);
        Spaceship spaceship2 = new Spaceship(1);
        Spaceship spaceship3 = new Spaceship(1);
        Spaceship spaceship4 = new Spaceship(1);
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
        Game game = new Game(players,1);
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
        int maxNum = 2;
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

        //tile 4 - cannon 7 5
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, id4);
        try {
            spaceship1.addTile(7,5, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - dcannon 8 6
        TileType t5 = TileType.D_CANNON;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile dcannon1= new DoubleCannon(t5, connectors5, rotationType5, id5);
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
        game.addCrew(player1, pos,AliveType.HUMAN);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 1;
        int cannonPowers = 4;
        int daysLost = 2;
        int credit = 8 ;
        int humanLost = 1;
        Card slaveOwner = new SlaveOwner(level,cannonPowers, daysLost,credit,humanLost);

        game.getCurrentState().setCurrentCard(slaveOwner);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        List<Coordinate> dcannonAttivi= new ArrayList<>();
        Coordinate pos86 = new Coordinate(8,6);
        dcannonAttivi.add(pos86);
        Coordinate pos66 = new Coordinate(6,6);
        dcannonAttivi.add(pos66);
        List<Coordinate> batterieUsate= new ArrayList<>();
        Coordinate pos87 = new Coordinate(8,7);
        batterieUsate.add(pos87);
        batterieUsate.add(pos87);

        game.choiceDoubleCannon(player1, dcannonAttivi, batterieUsate);
        assertEquals(StateCardType.DECISION, game.getCurrentState().getCurrentCard().getStateCard());
        game.choice( player1, true);
        assertEquals(StateCardType.FINISH, game.getCurrentState().getCurrentCard().getStateCard());
        assertEquals(8,player1.getSpaceship().getCosmicCredits());
        assertEquals(StateGameType.TAKE_CARD, game.getCurrentState().getPhase());
        Coordinate pos77 = new Coordinate(7,7);
        game.removeCrew(player1,pos77); //non dovrebbe funzionare
        assertEquals(2, spaceship1.calculateNumAlive());
        assertEquals(-1,game.getGameboard().getPositions().get(player1));

    }

    @Test
    void test_should_check_if_player_has_enough_cannon_power_and_doesnt_use_it(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(1);
        Spaceship spaceship2 = new Spaceship(1);
        Spaceship spaceship3 = new Spaceship(1);
        Spaceship spaceship4 = new Spaceship(1);
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
        Game game = new Game(players,1);
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
        int maxNum = 2;
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

        //tile 4 - cannon 7 5
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, id4);
        try {
            spaceship1.addTile(7,5, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - dcannon 8 6
        TileType t5 = TileType.D_CANNON;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile dcannon1= new DoubleCannon(t5, connectors5, rotationType5, id5);
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
        game.addCrew(player1, pos,AliveType.HUMAN);

        player1.setStatePlayer(StatePlayerType.IN_GAME);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        player4.setStatePlayer(StatePlayerType.IN_GAME);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);

        //Create current card in state choice attributes

        //create card
        int level = 1;
        int cannonPowers = 4;
        int daysLost = 2;
        int credit = 8 ;
        int humanLost = 1;
        Card slaveOwner = new SlaveOwner(level,cannonPowers, daysLost,credit,humanLost);

        game.getCurrentState().setCurrentCard(slaveOwner);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        List<Coordinate> dcannonAttivi= new ArrayList<>();
        Coordinate pos86 = new Coordinate(8,6);
        dcannonAttivi.add(pos86);
        Coordinate pos66 = new Coordinate(6,6);
        dcannonAttivi.add(pos66);
        List<Coordinate> batterieUsate= new ArrayList<>();
        Coordinate pos87 = new Coordinate(8,7);
        batterieUsate.add(pos87);
        batterieUsate.add(pos87);


        game.choiceDoubleCannon(player1, dcannonAttivi, batterieUsate);
        assertEquals(StateCardType.DECISION, game.getCurrentState().getCurrentCard().getStateCard());
        game.choice( player1, false);
        assertEquals(StateCardType.FINISH, game.getCurrentState().getCurrentCard().getStateCard());
        assertEquals(0,player1.getSpaceship().getCosmicCredits());
        assertEquals(StateGameType.TAKE_CARD, game.getCurrentState().getPhase());
        assertEquals(2, spaceship1.calculateNumAlive());
        assertEquals(4,game.getGameboard().getPositions().get(player1));

    }

}