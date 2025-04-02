package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpidemyTest {

    public Game make_a_spaceship(){
        //4 spaceship
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
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
        //game di livello 2 con i 4 players
        Game game = new Game(players,2);
        //inizializzo
        game.getGameboard().initializeGameBoard(players);

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

        //tile 3 - Purple cabin
        TileType t3 = TileType.PURPLE_CABIN;
        ConnectorType[] connectors3 = {ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        Tile cabin3 = new Cabin(t3, connectors3, rotationType3, id3);
        try {
            spaceship1.addTile(8,8, cabin3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        player1.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player2.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player3.setStatePlayer(StatePlayerType.CORRECT_SHIP);
        player4.setStatePlayer(StatePlayerType.CORRECT_SHIP);

        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);

        Coordinate pos1 = new Coordinate(7,7);
        game.addCrew(player1, pos1, AliveType.HUMAN);
        //game.addCrew(player1, 7,7, AliveType.HUMAN);
        Coordinate pos2 = new Coordinate(8,7);
        game.addCrew(player1, pos2,AliveType.PURPLE_ALIEN);



        player1.setStatePlayer(StatePlayerType.IN_GAME);
        player2.setStatePlayer(StatePlayerType.IN_GAME);
        player3.setStatePlayer(StatePlayerType.IN_GAME);
        player4.setStatePlayer(StatePlayerType.IN_GAME);

        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);


        return game;
    }

    @Test
    void test_should_remove_affected_crew(){
        Game game = make_a_spaceship();
        Card epidemy = new Epidemy(2);

        game.getCurrentState().setCurrentCard(epidemy);
        game.getCurrentState().setCurrentPlayer(game.getPlayers().getFirst());
        game.getPlayers().getFirst().setStatePlayer(StatePlayerType.IN_GAME);
        game.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);

        game.effect(game);
        //epidemy.effect(game);
        List<AliveType> cabinAffected = new ArrayList<>();
        cabinAffected.add(AliveType.HUMAN);


        assertEquals(cabinAffected.size(), game.getPlayers().getFirst().getSpaceship().getTile(7,7).get().getCrew().size());
        assertEquals(0, game.getPlayers().getFirst().getSpaceship().getTile(8,7).get().getCrew().size());

    }

}