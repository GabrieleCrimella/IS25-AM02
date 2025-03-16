package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.Spaceship;
import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StardustTest {
    @Test
    void test_Stardust() {
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        Game game = new Game(players,0);
        game.getGameboard().initializeGameBoard(players);



    }



}