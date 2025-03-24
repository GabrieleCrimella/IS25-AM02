package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.BlueBox;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.RedBox;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @Test
    void test_should_play_the_game() {
        List<Player> players = new ArrayList<>();
        Game game = make_a_spaceship(players);
        System.out.println(game.getCurrentState().getPhase());

        //pesco una nuova tile per il giocatore 0
        try {
            System.out.println(game.takeTile(players.get(0)).getType());
        } catch (IllegalStateException e) {
            System.out.println("Non puoi pescare una tile");
        }
        assert (game.getCurrentState().getPhase().equals(StateGameType.BUILD));
    }


    public Game make_a_spaceship(List<Player> players) {
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        //4 player
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        player1.setStatePlayer(StatePlayerType.FINISHED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        player2.setStatePlayer(StatePlayerType.FINISHED);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        player3.setStatePlayer(StatePlayerType.FINISHED);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
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
        game.addCrew(player2, 7,7, AliveType.HUMAN);

        //tile 2 - Storage 7 6
        TileType t2 = TileType.SPECIAL_STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum = 2;
        Tile storage1 = new SpecialStorage(t2, connectors2, rotationType2, id2, maxNum);
        try {
            spaceship1.addTile(7,6, storage1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }
        Box blueBox = new BlueBox(BoxType.BLUE);
        Box redBox = new RedBox(BoxType.RED);
        storage1.addBox(blueBox);
        storage1.addBox(redBox);

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

        game.addCrew(player2, 7,7, AliveType.HUMAN);

        //inizializzo spaceship3
        //tile 1 - cabin centrale
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

        game.addCrew(player3, 7,7, AliveType.HUMAN);

        //inizializzo spaceship4
        //tile 1 - cabin centrale
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

        game.addCrew(player4, 7,7, AliveType.HUMAN);
        game.getCurrentState().setPhase(StateGameType.RESULT);

        game.getPlayers().getFirst().getSpaceship().addNumOfWastedTiles(2);

        //Classifica = Red, Blue, Green, Yellow
        //Nave più bella = RED
        //Vendita merci = RED (4+1)
        //Perdite = Red(2)
        List<Player>  winners = game.getWinners();
        assertEquals(9,winners.get(0).getSpaceship().getCosmicCredits());
        assertEquals(5,winners.get(1).getSpaceship().getCosmicCredits());
        assertEquals(4,winners.get(2).getSpaceship().getCosmicCredits());
        assertEquals(3,winners.get(3).getSpaceship().getCosmicCredits());


    }


    @Test
    void test_should_check_addCrew_works_for_Humans_and_Purple_Aliens(){
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
        Game game = new Game(players, 0);
        game.getGameboard().initializeGameBoard(players);

        //inizializzo spaceship1 - 2 cabin normali con persone
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

        //tile 2 - cabin
        TileType t2 = TileType.CABIN;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Tile cabin2 = new Cabin(t2, connectors2, rotationType2, id2);
        try {
            spaceship1.addTile(8,7, cabin2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.addCrew(player1, 7,7, AliveType.HUMAN);
        game.addCrew(player1, 8,7, AliveType.HUMAN);
        assertEquals(2, game.getPlayers().getFirst().getSpaceship().getTile(7,7).get().getCrew().size());
        assertEquals(2, game.getPlayers().getFirst().getSpaceship().getTile(8,7).get().getCrew().size());

        //inizializzo spaceship2 - 2 cabine normali, una con alieni una con persone
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

        //tile 2 -cabin viola 7 8
        TileType t22 = TileType.PURPLE_CABIN;
        ConnectorType[] connectors22 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType22 = RotationType.NORTH;
        int id22 = 1;
        Tile purplecabin22 = new PurpleCabin(t22, connectors22, rotationType22, id22);
        try {
            spaceship2.addTile(7,8, purplecabin22);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - cabin 8 8
        TileType t23 = TileType.CABIN;
        ConnectorType[] connectors23 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType23 = RotationType.NORTH;
        int id23 = 1;
        Tile cabin23 = new Cabin(t23, connectors23, rotationType23, id23);
        try {
            spaceship2.addTile(8,8, cabin23);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.addCrew(player2, 7,7, AliveType.HUMAN);
        assertEquals(2, game.getPlayers().get(1).getSpaceship().getTile(7,7).get().getCrew().size());
        game.addCrew(player2, 8,8, AliveType.PURPLE_ALIEN);
        assertEquals(1, game.getPlayers().get(1).getSpaceship().getTile(8,8).get().getCrew().size());
    }

}