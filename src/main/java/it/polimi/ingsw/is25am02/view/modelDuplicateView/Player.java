package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.model.enumerations.StatePlayerType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.Optional;

public class Player {
    private final Optional<Tile>[][] spaceshipBoard;
    private final String nickname;
    private final PlayerColor color;
    private StatePlayerType statePlayer;
    private int numDeck;
    private int lobbyId;

    public Player(Optional<Tile>[][] spaceshipBoard, String nickname, PlayerColor color, StatePlayerType statePlayer, int numDeck, int lobbyId) {
        this.spaceshipBoard = spaceshipBoard;
        this.nickname = nickname;
        this.color = color;
        this.statePlayer = statePlayer;
        this.numDeck = numDeck;
        this.lobbyId = lobbyId;
    }
}

