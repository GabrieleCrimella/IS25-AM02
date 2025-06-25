package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.StatePlayerType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HourglassTest {
    @Test
    void test_should_get_and_flip() {
        Hourglass hourglass = new Hourglass();

        assertEquals(60, hourglass.getTimeLeft());
        assertFalse(hourglass.getRunning());

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

        hourglass.flip(game);
        assertTrue(hourglass.getRunning());

    }
}