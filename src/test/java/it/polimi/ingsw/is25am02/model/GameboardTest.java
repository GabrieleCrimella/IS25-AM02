package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameboardTest {

    @Test
    void test_should_move_forward_correctly() {
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        Game game = new Game(players,0);
        game.getGameboard().initializeGameBoard(players);
        game.getGameboard().move(2, player1);
        game.getGameboard().move(2, player2);
        game.getGameboard().move(3, player3);
        game.getGameboard().move(4, player4);
        //correct
        HashMap<Player, Integer> correct = new HashMap<Player, Integer>();
        correct.put(player1, 6);
        correct.put(player2, 4);
        correct.put(player3, 5);
        correct.put(player4, 7);
        assertEquals(true, game.getGameboard().getPositions().equals(correct));
    }

    @Test
    void test_should_move_backward_and_forward_correctly() {
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        Game game = new Game(players,0);
        game.getGameboard().initializeGameBoard(players);
        game.getGameboard().move(-2, player1);
        game.getGameboard().move(1, player2);
        game.getGameboard().move(-1, player3);
        game.getGameboard().move(-1, player4);
        //correct
        HashMap<Player, Integer> correct = new HashMap<Player, Integer>();
        correct.put(player1, -1);
        correct.put(player2, 3);
        correct.put(player3, -2);
        correct.put(player4, -3);
        assertEquals(true, game.getGameboard().getPositions().equals(correct));

    }

    @Test
    void test_should_check_correct_ranking() {
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        Game game = new Game(players,0);
        game.getGameboard().initializeGameBoard(players);
        game.getGameboard().move(-2, player1);
        game.getGameboard().move(1, player2);
        game.getGameboard().move(-1, player3);
        game.getGameboard().move(-1, player4);
        //correct
        HashMap<Player, Integer> correct = new HashMap<Player, Integer>();
        correct.put(player1, -1);
        correct.put(player2, 3);
        correct.put(player3, -2);
        correct.put(player4, -3);

        LinkedList<Player> correctRanking = new LinkedList<Player>();
        correctRanking.add(player2);
        correctRanking.add(player1);
        correctRanking.add(player3);
        correctRanking.add(player4);

        assertEquals(true, game.getGameboard().getPositions().equals(correct));
        assertEquals(true, game.getGameboard().getRanking().equals(correctRanking));
    }

    @Test
    void test_should_initialize_game_board_level_0() {
        Gameboard gameboard = new Gameboard(0);
        HashMap<Player, Integer> correct = new HashMap<Player, Integer>();
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        //lista da passare a initalizeGameboard
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //risposta corretta
        correct.put(player1, 4);
        correct.put(player2, 2);
        correct.put(player3, 1);
        correct.put(player4, 0);
        gameboard.initializeGameBoard(players);
        assertEquals(true, gameboard.getPositions().equals(correct));
    }

    @Test
    void test_should_initialize_game_board_level_1() {
        Gameboard gameboard = new Gameboard(1);
        HashMap<Player, Integer> correct = new HashMap<Player, Integer>();
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        //lista da passare a initalizeGameboard
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //risposta corretta
        correct.put(player1, 4);
        correct.put(player2, 2);
        correct.put(player3, 1);
        correct.put(player4, 0);
        gameboard.initializeGameBoard(players);
        assertEquals(true, gameboard.getPositions().equals(correct));
    }

    @Test
    void test_should_initialize_game_board_level_2() {
        Gameboard gameboard = new Gameboard(2);
        HashMap<Player, Integer> correct = new HashMap<Player, Integer>();
        Spaceship spaceship1 = new Spaceship(0);
        Spaceship spaceship2 = new Spaceship(0);
        Spaceship spaceship3 = new Spaceship(0);
        Spaceship spaceship4 = new Spaceship(0);
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        //lista da passare a initalizeGameboard
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //risposta corretta
        correct.put(player1, 6);
        correct.put(player2, 3);
        correct.put(player3, 1);
        correct.put(player4, 0);
        gameboard.initializeGameBoard(players);
        assertEquals(true, gameboard.getPositions().equals(correct));
    }
}