package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.exception.AlreadyViewingException;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MeteoritesStormTest {

    @Test
    void test_should_check_remove_single_tile_and_return_rest_of_ship() {
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
/*
        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cabin1);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
*/
        //tile 3 - battery 6 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), battery3);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 6,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - cabin 8 7
        TileType t5 = TileType.CABIN;
        ConnectorType[] connectors5 = {ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.SINGLE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile cabin2 = new Cabin(t5, connectors5, rotationType5, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cabin2);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
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
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cabin3);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 5,7, cabin3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 2 - Storage 7 8
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
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

        //tile 4 - cannon 9 7
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname(), 7,6, cannon1);
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

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        int level = 2;
        ArrayList<Pair<Integer, RotationType>> meteorites = new ArrayList<>();
        Pair<Integer,RotationType> meteor1 = new Pair<>(0,RotationType.NORTH);
        meteorites.add(meteor1);
        Card meteoritesStorm = new MeteoritesStorm(level, meteorites, null, null, true);

        game.getCurrentState().setCurrentCard(meteoritesStorm);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
        LinkedList<String> nicknames = new LinkedList<>(
                game.getGameboard().getRanking().stream()
                        .map(Player::getNickname)
                        .toList()
        );
        game.getCurrentState().getCurrentCard().setCurrentOrder(nicknames);

        try {
            game.setDiceResultManually(8);
            game.getCurrentCard().calculateDamage(game, player1, Optional.empty());
        } catch (IllegalPhaseException e) {
            throw new RuntimeException(e);
        } catch (IllegalRemoveException e) {
            throw new RuntimeException(e);
        }

        assertEquals(1, player1.getSpaceship().getNumOfWastedTiles());

    }

    @Test
    void test_should_check_one_big_from_north_with_double_cannon() {
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
/*
        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cabin1);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
*/
        //tile 3 - battery 6 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), battery3);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 6,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - cabin 8 7
        TileType t5 = TileType.CABIN;
        ConnectorType[] connectors5 = {ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.SINGLE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile cabin2 = new Cabin(t5, connectors5, rotationType5, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cabin2);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 8,7, cabin2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - cabin 5 7
        TileType t6 = TileType.D_CANNON;
        ConnectorType[] connectors6 = {ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType6 = RotationType.NORTH;
        int id6 = 1;
        Tile dcannon = new DoubleCannon(t6, connectors6, rotationType6, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), dcannon);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 5,7, dcannon);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 2 - Storage 7 8
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
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

        //tile 4 - cannon 9 7
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname(), 7,6, cannon1);
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

        pos = new Coordinate(8, 7);
        game.addCrew(player1, pos, AliveType.HUMAN);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        int level = 2;
        ArrayList<Pair<Integer, RotationType>> meteorites = new ArrayList<>();
        Pair<Integer,RotationType> meteor1 = new Pair<>(1,RotationType.NORTH);
        meteorites.add(meteor1);
        Card meteoritesStorm = new MeteoritesStorm(level, meteorites, null, null, true);

        game.getCurrentState().setCurrentCard(meteoritesStorm);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
        LinkedList<String> nicknames = new LinkedList<>(
                game.getGameboard().getRanking().stream()
                        .map(Player::getNickname)
                        .toList()
        );
        game.getCurrentState().getCurrentCard().setCurrentOrder(nicknames);

        try {
            game.setDiceResultManually(7);
            game.getCurrentCard().calculateDamage(game, player1, Optional.of(battery3));
        } catch (IllegalPhaseException e) {
            throw new RuntimeException(e);
        } catch (IllegalRemoveException e) {
            throw new RuntimeException(e);
        }

        assertEquals(0, player1.getSpaceship().getNumOfWastedTiles());

    }

    @Test
    void test_should_check_remove_single_tile_and_use_keep_block() {
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
/*
        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cabin1);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
*/
        //tile 3 - battery 6 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), battery3);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 6,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - cabin 8 7
        TileType t5 = TileType.CABIN;
        ConnectorType[] connectors5 = {ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.SINGLE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile cabin2 = new Cabin(t5, connectors5, rotationType5, null);
        try {
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cabin2);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
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
            game.getPlayers().get(0).getSpaceship().setCurrentTile(game.getPlayers().get(0).getNickname(), cabin3);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        try {
            spaceship1.addTile(player1.getNickname(), 5,7, cabin3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        //tile 2 - Storage 7 8
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
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

        //tile 4 - cannon 9 7
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType4 = RotationType.NORTH;
        int id4 = 1;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname(), 7,6, cannon1);
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

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        //Create current card in state choice attributes

        int level = 2;
        ArrayList<Pair<Integer, RotationType>> meteorites = new ArrayList<>();
        Pair<Integer,RotationType> meteor1 = new Pair<>(1,RotationType.EAST);
        meteorites.add(meteor1);
        Card meteoritesStorm = new MeteoritesStorm(level, meteorites, null, null, true);

        game.getCurrentState().setCurrentCard(meteoritesStorm);
        game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
        LinkedList<String> nicknames = new LinkedList<>(
                game.getGameboard().getRanking().stream()
                        .map(Player::getNickname)
                        .toList()
        );
        game.getCurrentState().getCurrentCard().setCurrentOrder(nicknames);

        try {
            game.setDiceResultManually(6);
            game.getCurrentCard().calculateDamage(game, player1, Optional.empty());
        } catch (IllegalPhaseException e) {
            throw new RuntimeException(e);
        } catch (IllegalRemoveException e) {
            throw new RuntimeException(e);
        }

        try {
            game.getCurrentCard().keepBlocks(game, player1, new Coordinate (7,7));
        } catch (IllegalPhaseException e) {
            throw new RuntimeException(e);
        }

        assertEquals(2, player1.getSpaceship().getNumOfWastedTiles());

    }


}