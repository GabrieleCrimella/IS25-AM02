package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.*;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PirateTest {

    @Test
    void test_should_check_if_player_doesnt_have_enough_cannon_power() {
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
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, id1);
        try {
            spaceship1.addTile(7, 7, cabin1);
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
            spaceship1.addTile(6, 7, storage1);
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
            spaceship1.addTile(8, 7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - dcannon 8 6
        TileType t5 = TileType.D_CANNON;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile dcannon1 = new DoubleCannon(t5, connectors5, rotationType5, id5);
        try {
            spaceship1.addTile(8, 6, dcannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - dcannon 6 6
        TileType t6 = TileType.D_CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile dcannon2 = new DoubleCannon(t6, connectors6, rotationType6, id6);
        try {
            spaceship1.addTile(6, 6, dcannon2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 7 - cabin 7 8
        TileType t7 = TileType.CABIN;
        ConnectorType[] connectors7 = {ConnectorType.UNIVERSAL, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.NONE};
        RotationType rotationType7 = RotationType.NORTH;
        int id7 = 1;
        Tile cabin7 = new Cabin(t7, connectors7, rotationType7, id7);
        try {
            spaceship1.addTile(7, 8, cabin7);
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

        //create card
        int level = 0;
        int cannonPowers = 10;
        int daysLost = 1;
        int credit = 2;
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        Pair<Integer, RotationType> singleShot = new Pair<>(0,RotationType.NORTH);
        shots.add(singleShot);
        Card pirate = new Pirate(level, cannonPowers, daysLost, credit, shots);

        game.getCurrentState().setCurrentCard(pirate);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        Coordinate position = new Coordinate(8, 6);
        List<Coordinate> coordcannon = new ArrayList<>();
        coordcannon.add(position);
        position = new Coordinate(8, 7);
        List<Coordinate> coordbatt = new ArrayList<>();
        coordbatt.add(position);


        game.choiceDoubleCannon(player1, coordcannon, coordbatt);
        assertEquals(StateCardType.CHOICE_ATTRIBUTES, game.getCurrentState().getCurrentCard().getStateCard());
        //controllare che la nave viene colpita dallo shots 0 north

        try {
            pirate.setPhase(2);
        } catch (IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }

        //la batteria è in 87
        //todo cambia dice
        game.calculateDamage(player1,new Coordinate(8,7));
        try {
            pirate.keepBlocks(game,player1,new Coordinate(7,8)); //voglio tenere la parte di nave composta dalla casella 7,8
        } catch (IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(5, player1.getSpaceship().getNumOfWastedTiles());

    }

    @Test
    void test_should_check_if_player_has_enough_cannon_power_and_uses_it() {
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
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, id1);
        try {
            spaceship1.addTile(7, 7, cabin1);
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
        GreenBox tilegreenbox = new GreenBox(BoxType.GREEN);
        YellowBox tileyellowbox = new YellowBox(BoxType.YELLOW);
        try {
            spaceship1.addTile(7, 6, storage1);
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
            spaceship1.addTile(8, 7, battery3);
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
            spaceship1.addTile(8, 6, dcannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - dcannon 6 6
        TileType t6 = TileType.D_CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile dcannon2 = new DoubleCannon(t6, connectors6, rotationType6, id6);
        try {
            spaceship1.addTile(6, 6, dcannon2);
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

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 0;
        int cannonPowers = 2;
        int daysLost = 2;
        int credit = 2;
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        Card pirate = new Pirate(level, cannonPowers, daysLost, credit, shots);

        game.getCurrentState().setCurrentCard(pirate);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        Coordinate position = new Coordinate(8, 6);
        List<Coordinate> coordcannon = new ArrayList<>();
        coordcannon.add(position);
        position = new Coordinate(6, 6);
        coordcannon.add(position);

        position = new Coordinate(8, 7);
        List<Coordinate> coordbatt = new ArrayList<>();
        coordbatt.add(position);
        position = new Coordinate(8, 7);
        coordbatt.add(position);


        game.choiceDoubleCannon(player1, coordcannon, coordbatt);
        assertEquals(StateCardType.DECISION, game.getCurrentState().getCurrentCard().getStateCard());
        game.choice(player1, true);
        //il giocatore vince contro i pirati e guadagna 2 crediti ma perde 2 giorno di volo


        assertEquals(2, game.getPlayers().getFirst().getSpaceship().getCosmicCredits()); //devo avere due crediti sulla spaceship
        assertEquals(4, game.getGameboard().getPositions().get(player1)); //mi aspetto che si muova indietro di 2

    }

    @Test
    void test_should_check_if_player_has_enough_cannon_power_and_doesnt_use_it() {
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
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, id1);
        try {
            spaceship1.addTile(7, 7, cabin1);
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
        GreenBox tilegreenbox = new GreenBox(BoxType.GREEN);
        YellowBox tileyellowbox = new YellowBox(BoxType.YELLOW);
        try {
            spaceship1.addTile(7, 6, storage1);
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
            spaceship1.addTile(8, 7, battery3);
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
            spaceship1.addTile(8, 6, dcannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - dcannon 6 6
        TileType t6 = TileType.D_CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile dcannon2 = new DoubleCannon(t6, connectors6, rotationType6, id6);
        try {
            spaceship1.addTile(6, 6, dcannon2);
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

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        //create card
        int level = 0;
        int cannonPowers = 2;
        int daysLost = 2;
        int credit = 2;
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        Card pirate = new Pirate(level, cannonPowers, daysLost, credit, shots);

        game.getCurrentState().setCurrentCard(pirate);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

        Coordinate position = new Coordinate(8, 6);
        List<Coordinate> coordcannon = new ArrayList<>();
        coordcannon.add(position);
        position = new Coordinate(6, 6);
        coordcannon.add(position);

        position = new Coordinate(8, 7);
        List<Coordinate> coordbatt = new ArrayList<>();
        coordbatt.add(position);
        position = new Coordinate(8, 7);
        coordbatt.add(position);


        game.choiceDoubleCannon(player1, coordcannon, coordbatt);
        assertEquals(StateCardType.DECISION, game.getCurrentState().getCurrentCard().getStateCard());
        game.choice(player1, false);
        //il giocatore vince contro i pirati e ma non guadagna 2 crediti e non perde 2 giorno di volo


        assertEquals(0, game.getPlayers().getFirst().getSpaceship().getCosmicCredits()); //non ho crediti
        assertEquals(6, game.getGameboard().getPositions().get(player1)); //rimango fermo

    }
}
