package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.tiles.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class GameTest {

    @Test
    void test_should_play_the_game() {
        List<Player> players = new ArrayList<>();
        Game game = make_a_spaceship(players);
        System.out.println(game.getCurrentState().getPhase());

        //pesco una nuova tile per il giocatore 0
        System.out.println(game.takeTile(players.get(0)).getType());
        System.out.println(game.getCurrentState().getPhase());
    }


    public Game make_a_spaceship(List<Player> players) {
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
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 0 con i 4 players
        return new Game(players, 0);
    }
}