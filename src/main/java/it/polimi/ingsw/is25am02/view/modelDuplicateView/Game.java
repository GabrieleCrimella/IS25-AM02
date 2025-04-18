package it.polimi.ingsw.is25am02.view.modelDuplicateView;



import java.util.ArrayList;
import java.util.List;

public class Game {
    private String gameName;
    private final List<Player> players;
    private final int level;
    private final Gameboard globalBoard;
    private final State currentState;

    public Game(List<Player> players, int level, Gameboard globalBoard, State currentState) {
        this.players = players;
        this.level = level;
        this.globalBoard = globalBoard;
        this.currentState = currentState;
    }
}


