package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.InitialCard;
import it.polimi.ingsw.is25am02.model.cards.boxes.BlueBox;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.RedBox;
import it.polimi.ingsw.is25am02.model.exception.AlreadyViewingException;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.network.rmi.client.RmiClient;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.GameV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.HourglassV;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @Test
    void test_should_play_the_game() {
        List<Player> players = new ArrayList<>();
        Game game = make_a_spaceship(players);
        System.out.println(game.getCurrentState().getPhase());
        assert (game.getCurrentState().getPhase().equals(StateGameType.BUILD));
    }

    public Game make_a_spaceship(List<Player> players) {
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED, null, 1);
        player1.setStatePlayer(StatePlayerType.FINISHED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE, null, 1);
        player2.setStatePlayer(StatePlayerType.FINISHED);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN, null, 1);
        player3.setStatePlayer(StatePlayerType.FINISHED);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW, null, 1);
        player4.setStatePlayer(StatePlayerType.FINISHED);
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 0 con i 4 players
        return new Game(players, 0);
    }

    public Game make_a_spaceship_no_players() {
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
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
        Game game = new Game(players, 0);
        game.getGameboard().initializeGameBoard(players);
        return game;

    }

    @Test
    void test_should_check_if_winners_are_caluclated_correctly(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        //4 player

        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED, null, 1);
        player1.setObservers(null);
        player1.setStatePlayer(StatePlayerType.IN_GAME);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE, null, 1);
        player2.setObservers(null);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN, null, 1);
        player3.setObservers(null);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW, null, 1);
        player4.setObservers(null);
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

        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            spaceship1.addTile(player1.getNickname(), 7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        Coordinate pos = new Coordinate(7,7);
        game.addCrew(player2, pos, AliveType.HUMAN);

        //tile 2 - Storage 7 6
        TileType t2 = TileType.SPECIAL_STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int maxNum = 2;
        Tile storage1 = new SpecialStorage(t2, connectors2, rotationType2, "", maxNum);
        try {
            spaceship1.addTile(player1.getNickname(),7,6, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        Box blueBox = new BlueBox(BoxType.BLUE);
        Box redBox = new RedBox(BoxType.RED);
        try {
            storage1.addBox(blueBox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        try {
            storage1.addBox(redBox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 8 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType3 = RotationType.NORTH;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(),8,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - cannon 7 5
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType4 = RotationType.NORTH;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname(),7,5, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - dcannon 8 6
        TileType t5 = TileType.D_CANNON;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        Tile dcannon1= new DoubleCannon(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(),8,6, dcannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 6 - dcannon 6 6
        TileType t6 = TileType.D_CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        Tile dcannon2= new DoubleCannon(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(),6,6, dcannon2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //inizializzo spaceship2
        //tile 1 - cabin centrale
        TileType t21 = TileType.CABIN;
        ConnectorType[] connectors21 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType21 = RotationType.NORTH;
        Tile cabin21 = new Cabin(t21, connectors21, rotationType21, null);
        try {
            spaceship2.addTile(player2.getNickname(),7,7, cabin21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        Coordinate pos2 = new Coordinate(7,7);
        game.addCrew(player2, pos2, AliveType.HUMAN);

        //inizializzo spaceship3
        //tile 1 - cabin centrale
        TileType t31 = TileType.CABIN;
        ConnectorType[] connectors31 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType31 = RotationType.NORTH;
        Tile cabin31 = new Cabin(t31, connectors31, rotationType31, null);
        try {
            spaceship3.addTile(player3.getNickname(),7,7, cabin31);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        Coordinate pos3 = new Coordinate(7,7);
        game.addCrew(player3, pos3, AliveType.HUMAN);

        //inizializzo spaceship4
        //tile 1 - cabin centrale
        TileType t41 = TileType.CABIN;
        ConnectorType[] connectors41 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType41 = RotationType.NORTH;
        Tile cabin41 = new Cabin(t41, connectors41, rotationType41, null);
        try {
            spaceship4.addTile(player4.getNickname(),7,7, cabin41);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        Coordinate pos4 = new Coordinate(7,7);
        game.addCrew(player4, pos4, AliveType.HUMAN);
        game.getCurrentState().setPhase(StateGameType.RESULT);

        game.getPlayers().getFirst().getSpaceship().addNumOfWastedTiles(2);

        //Classifica = Red, Blue, Green, Yellow
        //Nave più bella = Tutti tranne red
        //Vendita merci = RED (4+1)
        //Perdite = Red(2)
        game.Winners();
        assertEquals(7,game.getWinners().get("Rosso"));
        assertEquals(5,game.getWinners().get("Blu"));
        assertEquals(4,game.getWinners().get("Verde"));
        assertEquals(3,game.getWinners().get("Giallo"));
    }


    @Test
    void test_should_check_addCrew_works_for_Humans_and_Purple_Aliens(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
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
        Game game = new Game(players, 2);
        game.getGameboard().initializeGameBoard(players);

        //inizializzo spaceship1 - 2 cabin normali con persone
        //tile 1 - cabin centrale
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            spaceship1.addTile(player1.getNickname(), 7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 2 - cabin
        TileType t2 = TileType.CABIN;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType2 = RotationType.NORTH;
        Tile cabin2 = new Cabin(t2, connectors2, rotationType2, null);
        try {
            spaceship1.addTile(player1.getNickname(),8,7, cabin2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        game.getCurrentState().setCurrentCard(new InitialCard(0,null,null,false));
        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        Coordinate pos = new Coordinate(7,7);
        Coordinate pos2 = new Coordinate(8,7);
        game.addCrew(player1, pos, AliveType.HUMAN);
        game.addCrew(player1, pos2, AliveType.HUMAN);
        assertEquals(2, game.getPlayers().getFirst().getSpaceship().getTile(7,7).get().getCrew().size());
        assertEquals(2, game.getPlayers().getFirst().getSpaceship().getTile(8,7).get().getCrew().size());

        //inizializzo spaceship2 - 2 cabine normali, una con alieni una con persone
        //tile 1 - cabin centrale 7 7
        TileType t21 = TileType.CABIN;
        ConnectorType[] connectors21 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType21 = RotationType.NORTH;
        Tile cabin21 = new Cabin(t21, connectors21, rotationType21, null);
        try {
            spaceship2.addTile(player2.getNickname(),7,7, cabin21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 2 -cabin viola 7 8
        TileType t22 = TileType.PURPLE_CABIN;
        ConnectorType[] connectors22 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType22 = RotationType.NORTH;
        Tile purplecabin22 = new PurpleCabin(t22, connectors22, rotationType22, null);
        try {
            spaceship2.addTile(player2.getNickname(),7,8, purplecabin22);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - cabin 8 8
        TileType t23 = TileType.CABIN;
        ConnectorType[] connectors23 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType23 = RotationType.NORTH;
        Tile cabin23 = new Cabin(t23, connectors23, rotationType23, null);
        try {
            spaceship2.addTile(player2.getNickname(),8,8, cabin23);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        Coordinate pos3 = new Coordinate(7,7);
        game.addCrew(player2, pos3, AliveType.HUMAN);
        assertEquals(2, game.getPlayers().get(1).getSpaceship().getTile(7,7).get().getCrew().size());
        Coordinate pos4 = new Coordinate(8,8);
        game.addCrew(player2, pos4, AliveType.PURPLE_ALIEN);
        assertEquals(1, game.getPlayers().get(1).getSpaceship().getTile(8,8).get().getCrew().size());
    }

    @Test
    void test_should_check_remove_single_tile_and_return_rest_of_ship(){

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

        //fase di build
        //inizializzo spaceship1
        //tile 1 - cabin centrale 7 7
        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, null);
        try {
            spaceship1.addTile(player1.getNickname(),7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage() + " 1");
        }


        //tile 2 - Storage 7 6
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int maxNum = 2;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname(),7,6, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage() + " 2");
        }

        //tile 3 - battery 8 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType3 = RotationType.NORTH;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(),8,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage() + " 3");
        }

        //tile 4 - cannon 7 5
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType4 = RotationType.NORTH;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname(),7,5, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage() + " 4");
        }

        //tile 5 - dcannon 8 6
        TileType t5 = TileType.D_CANNON;
        ConnectorType[] connectors5 = {ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile dcannon1= new DoubleCannon(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(),8,6, dcannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage() + " 5");
        }

        //tile 6 - dcannon 6 6
        TileType t6 = TileType.D_CANNON;
        ConnectorType[] connectors6 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        RotationType rotationType6 = RotationType.NORTH;
        Tile dcannon2= new DoubleCannon(t6, connectors6, rotationType6, null);
        try {
            spaceship1.addTile(player1.getNickname(),6,6, dcannon2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage() + " 6");
        }

        player1.setStatePlayer(StatePlayerType.WRONG_SHIP);
        player2.setStatePlayer(StatePlayerType.WRONG_SHIP);
        player3.setStatePlayer(StatePlayerType.WRONG_SHIP);
        player4.setStatePlayer(StatePlayerType.WRONG_SHIP);
        game.getCurrentState().setPhase(StateGameType.CORRECTION);


        game.removeTile(player1, new Coordinate( 7, 5));


        assertEquals(true, spaceship1.getTile(7,5).isEmpty());
        assertEquals(TileType.STORAGE, spaceship1.getTile(7,6).get().getType());

    }

    @Test
    void test_should_check_remove_single_tile_and_return_two_options_when_hit_in_the_middle(){
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


        //tile 2 - Storage 5 8
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.NONE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 3;
        Tile storage1 = new Storage(t2, connectors2, rotationType2, null, maxNum);
        try {
            spaceship1.addTile(player1.getNickname(),5,8, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - battery 6 7
        TileType t3 = TileType.BATTERY;
        ConnectorType[] connectors3 = {ConnectorType.NONE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        int maxNum3 = 2;
        Tile battery3 = new BatteryStorage(t3, connectors3, rotationType3, null, maxNum3);
        try {
            spaceship1.addTile(player1.getNickname(),6,7, battery3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 4 - cannon 9 7
        TileType t4 = TileType.CANNON;
        ConnectorType[] connectors4 = {ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.DOUBLE};
        RotationType rotationType4 = RotationType.NORTH;
        Tile cannon1= new Cannon(t4, connectors4, rotationType4, null);
        try {
            spaceship1.addTile(player1.getNickname(),9,7, cannon1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 5 - cabin 8 7
        TileType t5 = TileType.CABIN;
        ConnectorType[] connectors5 = {ConnectorType.SINGLE, ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.SINGLE};
        RotationType rotationType5 = RotationType.NORTH;
        int id5 = 1;
        Tile cabin2 = new Cabin(t5, connectors5, rotationType5, null);
        try {
            spaceship1.addTile(player1.getNickname(),8,7, cabin2);
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
            spaceship1.addTile(player1.getNickname(),5,7, cabin3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        player1.setStatePlayer(StatePlayerType.WRONG_SHIP);
        player2.setStatePlayer(StatePlayerType.WRONG_SHIP);
        player3.setStatePlayer(StatePlayerType.WRONG_SHIP);
        player4.setStatePlayer(StatePlayerType.WRONG_SHIP);
        game.getCurrentState().setPhase(StateGameType.CORRECTION);

        assertEquals(false, spaceship1.getTile(7,7).isEmpty());
        assertEquals(false, spaceship1.getTile(8,7).isEmpty());

    }

    @Test
    void test_should_check_remove_single_tile_and_return_rest_of_ship_with_3_tiles(){

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

        //inizializzo spaceship2
        //tile 21 - cabin centrale 7 7
        TileType t21 = TileType.CABIN;
        ConnectorType[] connectors21 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType21 = RotationType.NORTH;
        int id21 = 1;
        Tile cabin21 = new Cabin(t21, connectors21, rotationType21, null);
        try {
            spaceship2.addTile(player2.getNickname(),7,7, cabin21);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        TileType t22 = TileType.BATTERY;
        ConnectorType[] connectors22 = {ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType22 = RotationType.NORTH;
        int maxNum22 = 2;
        Tile battery22 = new BatteryStorage(t22, connectors22, rotationType22, null, maxNum22);
        try {
            spaceship2.addTile(player2.getNickname(),8,6, battery22);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        TileType t23 = TileType.SHIELD;
        ConnectorType[] connectors23 = {ConnectorType.DOUBLE, ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType23 = RotationType.NORTH;
        int id23 = 0;
        boolean[] shielded = new boolean[]{true, true, false, false};
        Tile shield23 = new Shield(t23, connectors23, rotationType23, null, shielded);
        try {
            spaceship2.addTile(player2.getNickname(),8,7, shield23);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }


        player1.setStatePlayer(StatePlayerType.WRONG_SHIP);
        player2.setStatePlayer(StatePlayerType.WRONG_SHIP);
        player3.setStatePlayer(StatePlayerType.WRONG_SHIP);
        player4.setStatePlayer(StatePlayerType.WRONG_SHIP);
        game.getCurrentState().setPhase(StateGameType.CORRECTION);

        Coordinate pos = new Coordinate(8,6);
        game.getCurrentState().setCurrentPlayer(player2);
        //Optional<List<boolean [][]>> resultGame = game.removeTile(player2,pos);
        //game.keepBlock(player2,resultGame.get().get(0));


        assertEquals(true, spaceship2.getTile(8,6).isEmpty());
        assertEquals(false, spaceship2.getTile(8,7).isEmpty());
        assertEquals(false, spaceship2.getTile(7,7).isEmpty());

    }

    @Test
    void test_should_check_if_i_can_add_tiles_to_the_spaceship(){
        //inizializzo spaceship
        Spaceship spaceshipRosso = new Spaceship(2);
        Spaceship spaceshipBlu = new Spaceship(2);
        Spaceship spaceshipVerde = new Spaceship(2);
        Spaceship spaceshipGiallo = new Spaceship(2);

        //inizializzo player
        Player playerRosso = new Player(spaceshipRosso, "Mario Rossi", PlayerColor.RED, null, 1);
        Player playerBlu = new Player(spaceshipBlu, "Lisa Dagli Occhi Blu", PlayerColor.BLUE, null, 1);
        Player playerVerde = new Player (spaceshipVerde, "Giuseppe Verdi", PlayerColor.GREEN, null, 1);
        Player playerGiallo = new Player (spaceshipGiallo, "DJ Giallo", PlayerColor.YELLOW, null, 1);

        //aggiungo player alla lista
        List<Player> giocatori = new ArrayList<Player>();
        giocatori.add(playerRosso);
        giocatori.add(playerBlu);
        giocatori.add(playerVerde);
        giocatori.add(playerGiallo);

        //inizializzo game
        Game game = new Game(giocatori, 2);

        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());

        //aggiungo in posizione 7,7
        game.takeTile(playerRosso);
        Coordinate pos77 = new Coordinate(7,7);
        game.addTile(playerRosso,pos77, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());

        //aggiungo in posizione 7,6
        game.takeTile(playerRosso);
        Coordinate pos76 = new Coordinate(7,6);
        game.addTile(playerRosso,pos76, RotationType.NORTH);
        //spaceshipRosso.viewSpaceship();
        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());



    }

    @Test
    void test_should_check_if_i_can_book_and_return_tiles(){
        //inizializzo spaceship
        Spaceship spaceshipRosso = new Spaceship(2);
        Spaceship spaceshipBlu = new Spaceship(2);
        Spaceship spaceshipVerde = new Spaceship(2);
        Spaceship spaceshipGiallo = new Spaceship(2);

        //inizializzo player
        Player playerRosso = new Player(spaceshipRosso, "Mario Rossi", PlayerColor.RED, null, 1);
        Player playerBlu = new Player(spaceshipBlu, "Lisa Dagli Occhi Blu", PlayerColor.BLUE,  null, 1);
        Player playerVerde = new Player (spaceshipVerde, "Giuseppe Verdi", PlayerColor.GREEN, null, 1);
        Player playerGiallo = new Player (spaceshipGiallo, "DJ Giallo", PlayerColor.YELLOW, null, 1);

        //aggiungo player alla lista
        List<Player> giocatori = new ArrayList<Player>();
        giocatori.add(playerRosso);
        giocatori.add(playerBlu);
        giocatori.add(playerVerde);
        giocatori.add(playerGiallo);

        //inizializzo game
        Game game = new Game(giocatori, 2);

        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());

        //aggiungo in posizione 7,7
        game.takeTile(playerRosso);
        Coordinate pos77 = new Coordinate(7,7);
        game.addTile(playerRosso,pos77, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());

        //prendi tile e ritornala
        game.takeTile(playerRosso);
        game.returnTile(playerRosso);

        game.takeTile(playerRosso);
        game.bookTile(playerRosso);

        game.takeTile(playerRosso);
        game.bookTile(playerRosso);


        //spaceshipRosso.viewSpaceship();
        //game.tilesSituation();
        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());

    }

    @Test
    void test_should_check_if_i_can_add_a_booked_tile(){
        //inizializzo spaceship
        Spaceship spaceshipRosso = new Spaceship(2);
        Spaceship spaceshipBlu = new Spaceship(2);
        Spaceship spaceshipVerde = new Spaceship(2);
        Spaceship spaceshipGiallo = new Spaceship(2);

        //inizializzo player
        Player playerRosso = new Player(spaceshipRosso, "Mario Rossi", PlayerColor.RED, null, 1);
        Player playerBlu = new Player(spaceshipBlu, "Lisa Dagli Occhi Blu", PlayerColor.BLUE, null, 1);
        Player playerVerde = new Player (spaceshipVerde, "Giuseppe Verdi", PlayerColor.GREEN, null, 1);
        Player playerGiallo = new Player (spaceshipGiallo, "DJ Giallo", PlayerColor.YELLOW, null, 1);

        //aggiungo player alla lista
        List<Player> giocatori = new ArrayList<Player>();
        giocatori.add(playerRosso);
        giocatori.add(playerBlu);
        giocatori.add(playerVerde);
        giocatori.add(playerGiallo);

        //inizializzo game
        Game game = new Game(giocatori, 2);

        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());

        //aggiungo in posizione 7,7
        game.takeTile(playerRosso);
        Coordinate pos77 = new Coordinate(7,7);
        game.addTile(playerRosso,pos77, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());

        //aggiungo in posizione 7,6
        game.takeTile(playerRosso);
        Coordinate pos76 = new Coordinate(7,6);
        game.addTile(playerRosso,pos76, RotationType.NORTH);

        //book a tile
        game.takeTile(playerRosso);
        game.bookTile(playerRosso);

        //book a tile
        game.takeTile(playerRosso);
        game.bookTile(playerRosso);

        //restituisco un tile
        game.takeTile(playerRosso);
        game.returnTile(playerRosso);

        //game.tilesSituation();
        //spaceshipRosso.viewSpaceship();

        Coordinate pos87 = new Coordinate(8,7);
        game.addBookedTile(playerRosso, 1, pos87, RotationType.NORTH);
        //game.tilesSituation();
        //spaceshipRosso.viewSpaceship();

        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());



    }

    @Test
    void test_should_check_if_i_can_add_visibile_tile(){
        //inizializzo spaceship
        Spaceship spaceshipRosso = new Spaceship(2);
        Spaceship spaceshipBlu = new Spaceship(2);
        Spaceship spaceshipVerde = new Spaceship(2);
        Spaceship spaceshipGiallo = new Spaceship(2);

        //inizializzo player
        Player playerRosso = new Player(spaceshipRosso, "Mario Rossi", PlayerColor.RED, null, 1);
        Player playerBlu = new Player(spaceshipBlu, "Lisa Dagli Occhi Blu", PlayerColor.BLUE, null, 1);
        Player playerVerde = new Player (spaceshipVerde, "Giuseppe Verdi", PlayerColor.GREEN, null, 1);
        Player playerGiallo = new Player (spaceshipGiallo, "DJ Giallo", PlayerColor.YELLOW, null, 1);

        //aggiungo player alla lista
        List<Player> giocatori = new ArrayList<Player>();
        giocatori.add(playerRosso);
        giocatori.add(playerBlu);
        giocatori.add(playerVerde);
        giocatori.add(playerGiallo);

        //inizializzo game
        Game game = new Game(giocatori, 2);

        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());

        //aggiungo in posizione 7,7
        game.takeTile(playerRosso);
        Coordinate pos77 = new Coordinate(7,7);
        game.addTile(playerRosso,pos77, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());

        //prendo e ritorno un tile
        game.takeTile(playerRosso);
        game.returnTile(playerRosso);

        //creo un tile come se fosse visibile
        ConnectorType connectors[] = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        Tile tileDiProva = new Cabin(TileType.CABIN, connectors, RotationType.NORTH, "prova" );
        tileDiProva.setVisible();
        game.getHeapTile().getSetTiles().add(tileDiProva);
        game.takeTile(playerRosso, tileDiProva.getImagePath());

        //controllo che il currentTile sia quello visibile e lo aggiungo in posizione 75
        assertEquals(tileDiProva, spaceshipRosso.getCurrentTile());
        Coordinate pos75 = new Coordinate(7,6);
        game.addTile(playerRosso, pos75, RotationType.NORTH);

        //spaceshipRosso.viewSpaceship();
        //game.tilesSituation();
        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());

    }

    @Test
    void test_should_check_if_player_can_take_and_return_a_mini_deck(){
        //inizializzo spaceship
        Spaceship spaceshipRosso = new Spaceship(2);
        Spaceship spaceshipBlu = new Spaceship(2);
        Spaceship spaceshipVerde = new Spaceship(2);
        Spaceship spaceshipGiallo = new Spaceship(2);

        //inizializzo player
        Player playerRosso = new Player(spaceshipRosso, "Mario Rossi", PlayerColor.RED, null, 1);
        Player playerBlu = new Player(spaceshipBlu, "Lisa Dagli Occhi Blu", PlayerColor.BLUE, null, 1);
        Player playerVerde = new Player (spaceshipVerde, "Giuseppe Verdi", PlayerColor.GREEN, null, 1);
        Player playerGiallo = new Player (spaceshipGiallo, "DJ Giallo", PlayerColor.YELLOW, null, 1);

        //aggiungo player alla lista
        List<Player> giocatori = new ArrayList<Player>();
        giocatori.add(playerRosso);
        giocatori.add(playerBlu);
        giocatori.add(playerVerde);
        giocatori.add(playerGiallo);

        //inizializzo game
        Game game = new Game(giocatori, 2);
        game.getGameboard().initializeGameBoard(giocatori);

        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());

        //prendo e ritorno un tile
        game.takeTile(playerRosso);
        game.returnTile(playerRosso);
        game.takeTile(playerRosso);
        game.addTile(playerRosso, new Coordinate(7, 8),  RotationType.NORTH);

        game.takeMiniDeck(playerRosso,0);
        assertEquals(true, game.getDeck().getDeck().get(0).getValue());
        for (int i=0;i<game.getDeck().getDeck().get(0).getKey().size();i++){
            System.out.println(game.getDeck().getDeck().get(0).getKey().get(i).toString());
        }
        game.returnMiniDeck(playerRosso);
        assertEquals(false, game.getDeck().getDeck().get(0).getValue());
        game.takeMiniDeck(playerBlu,0);
        for (int i=0;i<game.getDeck().getDeck().get(0).getKey().size();i++){
            System.out.println(game.getDeck().getDeck().get(0).getKey().get(i).toString());
        }

        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());
    }

    @Test
    void test_should_check_if_players_are_finished(){
        //inizializzo spaceship
        Spaceship spaceshipRosso = new Spaceship(2);
        Spaceship spaceshipBlu = new Spaceship(2);
        Spaceship spaceshipVerde = new Spaceship(2);
        Spaceship spaceshipGiallo = new Spaceship(2);

        //inizializzo player
        Player playerRosso = new Player(spaceshipRosso, "Mario Rossi", PlayerColor.RED, null, 1);
        Player playerBlu = new Player(spaceshipBlu, "Lisa Dagli Occhi Blu", PlayerColor.BLUE, null, 1);
        Player playerVerde = new Player (spaceshipVerde, "Giuseppe Verdi", PlayerColor.GREEN, null, 1);
        Player playerGiallo = new Player (spaceshipGiallo, "DJ Giallo", PlayerColor.YELLOW, null, 1);

        //aggiungo player alla lista
        List<Player> giocatori = new ArrayList<Player>();
        giocatori.add(playerRosso);
        giocatori.add(playerBlu);
        giocatori.add(playerVerde);
        giocatori.add(playerGiallo);

        //inizializzo game
        Game game = new Game(giocatori, 2);

        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());

        //aggiungo posizione 7,7 al playerrosso
        game.takeTile(playerRosso);
        Coordinate pos77 = new Coordinate(7,7);
        game.addTile(playerRosso,pos77, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());

        //aggiungo in posizione 7,7 al playerBlu
        game.takeTile(playerBlu);
        game.addTile(playerBlu,pos77, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerBlu.getStatePlayer());

        //aggiungo in posizione 7,7 al playerVerde
        game.takeTile(playerVerde);
        game.addTile(playerVerde,pos77, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerVerde.getStatePlayer());

        //aggiungo in posizione 7,7 al playerGiallo
        game.takeTile(playerGiallo);
        game.addTile(playerGiallo,pos77, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerGiallo.getStatePlayer());

        //aggiungo in posizione 8,8 al playerGiallo sbagliato
        game.takeTile(playerGiallo);
        Coordinate pos88 = new Coordinate(8,8);
        game.addTile(playerGiallo,pos88, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerGiallo.getStatePlayer());


        //finisce il rosso
        game.shipFinished(playerRosso);
        assertEquals(StatePlayerType.FINISHED, playerRosso.getStatePlayer());
        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        //finisce il blu
        game.shipFinished(playerBlu);
        assertEquals(StatePlayerType.FINISHED, playerBlu.getStatePlayer());
        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        //finisce il verde
        game.shipFinished(playerVerde);
        assertEquals(StatePlayerType.FINISHED, playerVerde.getStatePlayer());
        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        //finisce il giallo
        game.shipFinished(playerGiallo);
        assertEquals(StatePlayerType.FINISHED, playerGiallo.getStatePlayer());
        assertEquals(StateGameType.CHECK, game.getCurrentState().getPhase());

    }

    @Test
    void test_should_check_if_4_players_have_wrong_spaceship_and_fix_them(){
        //inizializzo spaceship
        Spaceship spaceshipRosso = new Spaceship(2);
        Spaceship spaceshipBlu = new Spaceship(2);
        Spaceship spaceshipVerde = new Spaceship(2);
        Spaceship spaceshipGiallo = new Spaceship(2);

        //inizializzo player
        Player playerRosso = new Player(spaceshipRosso, "Mario Rossi", PlayerColor.RED, null, 1);
        Player playerBlu = new Player(spaceshipBlu, "Lisa Dagli Occhi Blu", PlayerColor.BLUE, null, 1);
        Player playerVerde = new Player (spaceshipVerde, "Giuseppe Verdi", PlayerColor.GREEN, null, 1);
        Player playerGiallo = new Player (spaceshipGiallo, "DJ Giallo", PlayerColor.YELLOW, null, 1);

        //aggiungo player alla lista
        List<Player> giocatori = new ArrayList<Player>();
        giocatori.add(playerRosso);
        giocatori.add(playerBlu);
        giocatori.add(playerVerde);
        giocatori.add(playerGiallo);

        //inizializzo game
        Game game = new Game(giocatori, 2);

        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());

        TileType t22 = TileType.BATTERY;
        ConnectorType[] connectors22 = {ConnectorType.NONE, ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.DOUBLE};
        RotationType rotationType22 = RotationType.NORTH;
        int maxNum22 = 2;
        Tile battery22 = new BatteryStorage(t22, connectors22, rotationType22, null, maxNum22);
        try {
            spaceshipRosso.addTile(playerRosso.getNickname(),8,7, battery22);
            spaceshipGiallo.addTile(playerGiallo.getNickname(),8,7, battery22);
            spaceshipBlu.addTile(playerBlu.getNickname(),8,7, battery22);
            spaceshipVerde.addTile(playerVerde.getNickname(),8,7, battery22);

        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());
        assertEquals(StatePlayerType.NOT_FINISHED,playerBlu.getStatePlayer());
        assertEquals(StatePlayerType.NOT_FINISHED,playerVerde.getStatePlayer());
        assertEquals(StatePlayerType.NOT_FINISHED,playerGiallo.getStatePlayer());

        //finisce il rosso
        game.shipFinished(playerRosso);
        assertEquals(StatePlayerType.FINISHED, playerRosso.getStatePlayer());
        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        //finisce il blu
        game.shipFinished(playerBlu);
        assertEquals(StatePlayerType.FINISHED, playerBlu.getStatePlayer());
        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        //finisce il verde
        game.shipFinished(playerVerde);
        assertEquals(StatePlayerType.FINISHED, playerVerde.getStatePlayer());
        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        //finisce il giallo
        game.shipFinished(playerGiallo);
        assertEquals(StatePlayerType.FINISHED, playerGiallo.getStatePlayer());
        assertEquals(StateGameType.CHECK, game.getCurrentState().getPhase());


        game.checkSpaceship(playerRosso);
        assertEquals(StatePlayerType.WRONG_SHIP, playerRosso.getStatePlayer());
        assertEquals(StateGameType.CORRECTION, game.getCurrentState().getPhase());
        game.removeTile(playerRosso,new Coordinate(8,7));
        game.checkWrongSpaceship(playerRosso);
        assertEquals(StatePlayerType.CORRECT_SHIP, playerRosso.getStatePlayer());
        assertEquals(StateGameType.CHECK, game.getCurrentState().getPhase());

        game.checkSpaceship(playerBlu);
        assertEquals(StatePlayerType.WRONG_SHIP, playerBlu.getStatePlayer());
        assertEquals(StateGameType.CORRECTION, game.getCurrentState().getPhase());
        game.removeTile(playerBlu,new Coordinate(8,7));
        game.checkWrongSpaceship(playerBlu);
        assertEquals(StatePlayerType.CORRECT_SHIP, playerBlu.getStatePlayer());
        assertEquals(StateGameType.CHECK, game.getCurrentState().getPhase());

        game.checkSpaceship(playerGiallo);
        assertEquals(StatePlayerType.WRONG_SHIP, playerGiallo.getStatePlayer());
        assertEquals(StateGameType.CORRECTION, game.getCurrentState().getPhase());
        game.removeTile(playerGiallo,new Coordinate(8,7));
        game.checkWrongSpaceship(playerGiallo);
        assertEquals(StatePlayerType.CORRECT_SHIP, playerGiallo.getStatePlayer());
        assertEquals(StateGameType.CHECK, game.getCurrentState().getPhase());

        game.checkSpaceship(playerVerde);
        assertEquals(StatePlayerType.WRONG_SHIP, playerVerde.getStatePlayer());
        assertEquals(StateGameType.CORRECTION, game.getCurrentState().getPhase());
        game.removeTile(playerVerde,new Coordinate(8,7));
        game.checkWrongSpaceship(playerVerde);
        assertEquals(StatePlayerType.CORRECT_SHIP, playerVerde.getStatePlayer());
        assertEquals(StateGameType.INITIALIZATION_SPACESHIP, game.getCurrentState().getPhase());
    }

    @Test
    void test_should_check_spaceship_initialization(){
        //inizializzo spaceship
        Spaceship spaceshipRosso = new Spaceship(2);
        Spaceship spaceshipBlu = new Spaceship(2);
        Spaceship spaceshipVerde = new Spaceship(2);
        Spaceship spaceshipGiallo = new Spaceship(2);

        //inizializzo player
        Player playerRosso = new Player(spaceshipRosso, "Mario Rossi", PlayerColor.RED, null, 1);
        Player playerBlu = new Player(spaceshipBlu, "Lisa Dagli Occhi Blu", PlayerColor.BLUE, null, 1);
        Player playerVerde = new Player (spaceshipVerde, "Giuseppe Verdi", PlayerColor.GREEN, null, 1);
        Player playerGiallo = new Player (spaceshipGiallo, "DJ Giallo", PlayerColor.YELLOW, null, 1);

        //aggiungo player alla lista
        List<Player> giocatori = new ArrayList<Player>();
        giocatori.add(playerRosso);
        giocatori.add(playerBlu);
        giocatori.add(playerVerde);
        giocatori.add(playerGiallo);

        //inizializzo game
        Game game = new Game(giocatori, 2);

        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
/*
        //aggiungo tile preso a caso in posizione 10,9 al playerrosso
        game.takeTile(playerRosso);
        Coordinate pos109 = new Coordinate(10,9);
        game.addTile(playerRosso,pos109, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());*/

        ConnectorType connectorsUUUU[] = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.NONE};
        Tile Cabina77Blu = new Cabin(TileType.CABIN,connectorsUUUU, RotationType.NORTH, "cabina77blu" );
        Cabina77Blu.setVisible();
        game.getHeapTile().getSetTiles().add(Cabina77Blu);
        Coordinate pos78 =  new Coordinate(7,8);
        try {
            game.getPlayers().get(1).getSpaceship().setCurrentTile(game.getPlayers().get(1).getNickname(), Cabina77Blu);
        } catch (AlreadyViewingException e) {
            throw new RuntimeException(e);
        }
        game.addTile(playerBlu, pos78,RotationType.NORTH);
/*
        //aggiungo tile preso a caso in posizione 10,9 al playerBlu
        game.takeTile(playerBlu);
        game.addTile(playerBlu,pos109, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerBlu.getStatePlayer());

        //aggiungo tile preso a caso in posizione 10,9 al playerVerde
        game.takeTile(playerVerde);
        game.addTile(playerVerde,pos109, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerVerde.getStatePlayer());

        //aggiungo tile preso a caso in posizione 10,9 al playerGiallo
        game.takeTile(playerGiallo);
        game.addTile(playerGiallo,pos109, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerGiallo.getStatePlayer());*/
/*
        //aggiungo Cabina centrale al Rosso
        ConnectorType connectorsUUUU[] = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        Tile Cabina77Rosso = new Cabin(TileType.CABIN,connectorsUUUU, RotationType.NORTH, null );
        Cabina77Rosso.setVisible();
        game.getHeapTile().getSetTiles().add(Cabina77Rosso);
        game.takeTile(playerRosso, Cabina77Rosso.getImagePath());
        Coordinate pos77 = new Coordinate(7,7);
        game.addTile(playerRosso, pos77,RotationType.NORTH);

        //aggiungo Cabina centrale al Blu
        Tile Cabina77Blu = new Cabin(TileType.CABIN,connectorsUUUU, RotationType.NORTH, null );
        Cabina77Blu.setVisible();
        game.getHeapTile().getSetTiles().add(Cabina77Blu);
        game.takeTile(playerBlu, Cabina77Blu.getImagePath());
        game.addTile(playerBlu, pos77,RotationType.NORTH);

        //aggiungo Cabina centrale al Verde
        Tile Cabina77Verde = new Cabin(TileType.CABIN,connectorsUUUU, RotationType.NORTH, null );
        Cabina77Verde.setVisible();
        game.getHeapTile().getSetTiles().add(Cabina77Verde);
        game.takeTile(playerVerde, Cabina77Verde.getImagePath());
        game.addTile(playerVerde, pos77,RotationType.NORTH);

        //aggiungo Cabina centrale al Giallo
        Tile Cabina77Giallo = new Cabin(TileType.CABIN,connectorsUUUU, RotationType.NORTH, null );
        Cabina77Giallo.setVisible();
        game.getHeapTile().getSetTiles().add(Cabina77Giallo);
        game.takeTile(playerGiallo, Cabina77Giallo.getImagePath());
        game.addTile(playerGiallo, pos77,RotationType.NORTH);
*/

        //finisce il rosso
        game.shipFinished(playerRosso);
        assertEquals(StatePlayerType.FINISHED, playerRosso.getStatePlayer());
        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        //finisce il blu
        game.shipFinished(playerBlu);
        assertEquals(StatePlayerType.FINISHED, playerBlu.getStatePlayer());
        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        //finisce il verde
        game.shipFinished(playerVerde);
        assertEquals(StatePlayerType.FINISHED, playerVerde.getStatePlayer());
        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        //finisce il giallo
        game.shipFinished(playerGiallo);
        assertEquals(StatePlayerType.FINISHED, playerGiallo.getStatePlayer());
        assertEquals(StateGameType.CHECK, game.getCurrentState().getPhase());
/*
        per visualizzare le navi
        System.out.println("SpaceshipRosso");
        spaceshipRosso.viewSpaceship();
        System.out.println("SpaceshipBlu");
        spaceshipBlu.viewSpaceship();
        System.out.println("SpaceshipVerde");
        spaceshipVerde.viewSpaceship();
        System.out.println("SpaceshipGiallo");
        spaceshipGiallo.viewSpaceship();


 */


        game.checkSpaceship(playerRosso);
        assertEquals(StatePlayerType.CORRECT_SHIP, playerRosso.getStatePlayer());
        assertEquals(StateGameType.CHECK, game.getCurrentState().getPhase());
        game.checkSpaceship(playerBlu);
        assertEquals(StatePlayerType.WRONG_SHIP, playerBlu.getStatePlayer());
        assertEquals(StateGameType.CORRECTION, game.getCurrentState().getPhase());
        game.removeTile(playerBlu,pos78);
        game.checkWrongSpaceship(playerBlu);
        assertEquals(StatePlayerType.CORRECT_SHIP, playerBlu.getStatePlayer());
        game.checkSpaceship(playerGiallo);
        assertEquals(StatePlayerType.CORRECT_SHIP, playerGiallo.getStatePlayer());
        assertEquals(StateGameType.CHECK, game.getCurrentState().getPhase());
        game.checkSpaceship(playerVerde);
        assertEquals(StatePlayerType.CORRECT_SHIP, playerVerde.getStatePlayer());

        assertEquals(StateGameType.INITIALIZATION_SPACESHIP, game.getCurrentState().getPhase());
        game.ready(playerGiallo);
        assertEquals(true, spaceshipGiallo.calculateNumAlive()>0);
        game.ready(playerVerde);
        assertEquals(true, spaceshipVerde.calculateNumAlive()>0);
        game.ready(playerBlu);
        assertEquals(true, spaceshipBlu.calculateNumAlive()>0);
        game.ready(playerRosso);
        assertEquals(true, spaceshipRosso.calculateNumAlive()>0);

        assertEquals(StateGameType.TAKE_CARD, game.getCurrentState().getPhase());

        assertEquals(6, game.getGameboard().getPositions().get(playerRosso));
        assertEquals(3, game.getGameboard().getPositions().get(playerBlu));
        assertEquals(1, game.getGameboard().getPositions().get(playerVerde));
        assertEquals(0, game.getGameboard().getPositions().get(playerGiallo));


        //game.tilesSituation();

    }

    @Test
    void test_should_check_take_card_phase(){
        //inizializzo spaceship
        Spaceship spaceshipRosso = new Spaceship(2);
        Spaceship spaceshipBlu = new Spaceship(2);
        Spaceship spaceshipVerde = new Spaceship(2);
        Spaceship spaceshipGiallo = new Spaceship(2);

        //inizializzo player
        Player playerRosso = new Player(spaceshipRosso, "Mario Rossi", PlayerColor.RED, null, 1);
        Player playerBlu = new Player(spaceshipBlu, "Lisa Dagli Occhi Blu", PlayerColor.BLUE, null, 1);
        Player playerVerde = new Player (spaceshipVerde, "Giuseppe Verdi", PlayerColor.GREEN, null, 1);
        Player playerGiallo = new Player (spaceshipGiallo, "DJ Giallo", PlayerColor.YELLOW, null, 1);

        //aggiungo player alla lista
        List<Player> giocatori = new ArrayList<Player>();
        giocatori.add(playerRosso);
        giocatori.add(playerBlu);
        giocatori.add(playerVerde);
        giocatori.add(playerGiallo);

        //inizializzo game
        Game game = new Game(giocatori, 2);
        game.getGameboard().initializeGameBoard(giocatori);

        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());

        //aggiungo tile preso a caso in posizione 10,9 al playerrosso
        game.takeTile(playerRosso);
        Coordinate pos109 = new Coordinate(10,9);
        game.addTile(playerRosso,pos109, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerRosso.getStatePlayer());

        //aggiungo tile preso a caso in posizione 10,9 al playerBlu
        game.takeTile(playerBlu);
        game.addTile(playerBlu,pos109, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerBlu.getStatePlayer());

        //aggiungo tile preso a caso in posizione 10,9 al playerVerde
        game.takeTile(playerVerde);
        game.addTile(playerVerde,pos109, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerVerde.getStatePlayer());

        //aggiungo tile preso a caso in posizione 10,9 al playerGiallo
        game.takeTile(playerGiallo);
        game.addTile(playerGiallo,pos109, RotationType.NORTH);
        assertEquals(StatePlayerType.NOT_FINISHED,playerGiallo.getStatePlayer());

        /*//aggiungo Cabina centrale al Rosso
        ConnectorType connectorsUUUU[] = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        Tile Cabina77Rosso = new Cabin(TileType.CABIN,connectorsUUUU, RotationType.NORTH, null );
        Cabina77Rosso.setVisible();
        game.getHeapTile().getSetTiles().add(Cabina77Rosso);
        game.takeTile(playerRosso, Cabina77Rosso.getImagePath());
        Coordinate pos77 = new Coordinate(7,7);
        game.addTile(playerRosso, pos77,RotationType.NORTH);

        //aggiungo Cabina centrale al Blu
        Tile Cabina77Blu = new Cabin(TileType.CABIN,connectorsUUUU, RotationType.NORTH, null );
        Cabina77Blu.setVisible();
        game.getHeapTile().getSetTiles().add(Cabina77Blu);
        game.takeTile(playerBlu, Cabina77Blu.getImagePath());
        game.addTile(playerBlu, pos77,RotationType.NORTH);

        //aggiungo Cabina centrale al Verde
        Tile Cabina77Verde = new Cabin(TileType.CABIN,connectorsUUUU, RotationType.NORTH, null);
        Cabina77Verde.setVisible();
        game.getHeapTile().getSetTiles().add(Cabina77Verde);
        game.takeTile(playerVerde, Cabina77Verde.getImagePath());
        game.addTile(playerVerde, pos77,RotationType.NORTH);

        //aggiungo Cabina centrale al Giallo
        Tile Cabina77Giallo = new Cabin(TileType.CABIN,connectorsUUUU, RotationType.NORTH, null);
        Cabina77Giallo.setVisible();
        game.getHeapTile().getSetTiles().add(Cabina77Giallo);
        game.takeTile(playerGiallo, Cabina77Giallo.getImagePath());
        game.addTile(playerGiallo, pos77,RotationType.NORTH);*/


        //finisce il rosso
        game.shipFinished(playerRosso);
        assertEquals(StatePlayerType.FINISHED, playerRosso.getStatePlayer());
        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        //finisce il blu
        game.shipFinished(playerBlu);
        assertEquals(StatePlayerType.FINISHED, playerBlu.getStatePlayer());
        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        //finisce il verde
        game.shipFinished(playerVerde);
        assertEquals(StatePlayerType.FINISHED, playerVerde.getStatePlayer());
        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        //finisce il giallo
        game.shipFinished(playerGiallo);
        assertEquals(StatePlayerType.FINISHED, playerGiallo.getStatePlayer());
        assertEquals(StateGameType.CHECK, game.getCurrentState().getPhase());

        game.checkSpaceship(playerRosso);
        assertEquals(StatePlayerType.CORRECT_SHIP, playerRosso.getStatePlayer());
        assertEquals(StateGameType.CHECK, game.getCurrentState().getPhase());
        game.checkSpaceship(playerBlu);
        assertEquals(StatePlayerType.CORRECT_SHIP, playerBlu.getStatePlayer());
        assertEquals(StateGameType.CHECK, game.getCurrentState().getPhase());
        game.checkSpaceship(playerGiallo);
        assertEquals(StatePlayerType.CORRECT_SHIP, playerGiallo.getStatePlayer());
        assertEquals(StateGameType.CHECK, game.getCurrentState().getPhase());
        game.checkSpaceship(playerVerde);
        assertEquals(StatePlayerType.CORRECT_SHIP, playerVerde.getStatePlayer());
        assertEquals(StateGameType.INITIALIZATION_SPACESHIP, game.getCurrentState().getPhase());

        game.ready(playerGiallo);
        assertEquals(true, spaceshipGiallo.calculateNumAlive()>0);
        game.ready(playerVerde);
        assertEquals(true, spaceshipVerde.calculateNumAlive()>0);
        game.ready(playerBlu);
        assertEquals(true, spaceshipBlu.calculateNumAlive()>0);
        game.ready(playerRosso);
        assertEquals(true, spaceshipRosso.calculateNumAlive()>0);

        assertEquals(StateGameType.TAKE_CARD, game.getCurrentState().getPhase());

        assertEquals(6, game.getGameboard().getPositions().get(playerRosso));
        assertEquals(3, game.getGameboard().getPositions().get(playerBlu));
        assertEquals(1, game.getGameboard().getPositions().get(playerVerde));
        assertEquals(0, game.getGameboard().getPositions().get(playerGiallo));

        game.playNextCard(playerRosso);
    }

    @Test
    void test_should_check_getter_and_setter() {
        //inizializzo spaceship
        Spaceship spaceshipRosso = new Spaceship(2);
        Spaceship spaceshipBlu = new Spaceship(2);
        Spaceship spaceshipVerde = new Spaceship(2);
        Spaceship spaceshipGiallo = new Spaceship(2);

        //inizializzo player
        Player playerRosso = new Player(spaceshipRosso, "Mario Rossi", PlayerColor.RED, null, 1);
        Player playerBlu = new Player(spaceshipBlu, "Lisa Dagli Occhi Blu", PlayerColor.BLUE, null, 1);
        Player playerVerde = new Player (spaceshipVerde, "Giuseppe Verdi", PlayerColor.GREEN, null, 1);
        Player playerGiallo = new Player (spaceshipGiallo, "DJ Giallo", PlayerColor.YELLOW, null, 1);

        //aggiungo player alla lista
        List<Player> giocatori = new ArrayList<Player>();
        giocatori.add(playerRosso);
        giocatori.add(playerBlu);
        giocatori.add(playerVerde);
        giocatori.add(playerGiallo);

        //inizializzo game
        Game game = new Game(giocatori, 2);
        game.getGameboard().initializeGameBoard(giocatori);
        LinkedList<String> nicknames = new LinkedList<>(
                game.getGameboard().getRanking().stream()
                        .map(Player::getNickname)
                        .toList()
        );
        game.getCurrentState().getCurrentCard().setCurrentOrder(nicknames);

        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());

        assertEquals(2, game.getlevel());
        assertEquals(0, game.getMaxAllowedPlayers());
        Hourglass hour = game.getHourglass();
        assertEquals(false, hour.getRunning());

        game.setDiceResult();
        game.setBuildTimeIsOver();

        Set<Tile> tiles = game.getVisibleTiles();
        assertEquals(0, tiles.size());

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
        game.getCurrentState().setCurrentPlayer(playerGiallo);
        game.nextPlayer();
        assertEquals(playerRosso, game.getCurrentState().getCurrentPlayer());

        Player io = game.getPlayerFromNickname("never");
        assertEquals(null, io);
    }

    @Test
    void test_should_check_hourglass() {
        //inizializzo spaceship
        Spaceship spaceshipRosso = new Spaceship(2);
        Spaceship spaceshipBlu = new Spaceship(2);
        Spaceship spaceshipVerde = new Spaceship(2);
        Spaceship spaceshipGiallo = new Spaceship(2);

        //inizializzo player
        Player playerRosso = new Player(spaceshipRosso, "Mario Rossi", PlayerColor.RED, null, 1);
        Player playerBlu = new Player(spaceshipBlu, "Lisa Dagli Occhi Blu", PlayerColor.BLUE, null, 1);
        Player playerVerde = new Player (spaceshipVerde, "Giuseppe Verdi", PlayerColor.GREEN, null, 1);
        Player playerGiallo = new Player (spaceshipGiallo, "DJ Giallo", PlayerColor.YELLOW, null, 1);

        //aggiungo player alla lista
        List<Player> giocatori = new ArrayList<Player>();
        giocatori.add(playerRosso);
        giocatori.add(playerBlu);
        giocatori.add(playerVerde);
        giocatori.add(playerGiallo);

        //inizializzo game
        Game game = new Game(giocatori, 2);
        game.getGameboard().initializeGameBoard(giocatori);

        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
    }

    @Test
    void test_should_check_early_landing() {
        //inizializzo spaceship
        Spaceship spaceshipRosso = new Spaceship(2);
        Spaceship spaceshipBlu = new Spaceship(2);
        Spaceship spaceshipVerde = new Spaceship(2);
        Spaceship spaceshipGiallo = new Spaceship(2);

        //inizializzo player
        Player playerRosso = new Player(spaceshipRosso, "Mario Rossi", PlayerColor.RED, null, 1);
        Player playerBlu = new Player(spaceshipBlu, "Lisa Dagli Occhi Blu", PlayerColor.BLUE, null, 1);
        Player playerVerde = new Player (spaceshipVerde, "Giuseppe Verdi", PlayerColor.GREEN, null, 1);
        Player playerGiallo = new Player (spaceshipGiallo, "DJ Giallo", PlayerColor.YELLOW, null, 1);

        //aggiungo player alla lista
        List<Player> giocatori = new ArrayList<Player>();
        giocatori.add(playerRosso);
        giocatori.add(playerBlu);
        giocatori.add(playerVerde);
        giocatori.add(playerGiallo);

        //inizializzo game
        Game game = new Game(giocatori, 2);
        game.getGameboard().initializeGameBoard(giocatori);

        assertEquals(StateGameType.BUILD, game.getCurrentState().getPhase());
        for(Player p : giocatori){
            p.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        }
        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        for(Player p : giocatori){
            game.ready(p);
        }

        game.earlyLanding(playerRosso);

        assertEquals(StatePlayerType.OUT_GAME, playerRosso.getStatePlayer());
    }
}