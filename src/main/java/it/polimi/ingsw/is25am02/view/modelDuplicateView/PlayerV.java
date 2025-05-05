package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.util.Optional;

public class PlayerV {
    private Optional<TileV>[][] spaceshipBoard;
    private boolean[][] spaceshipMask;
    private Optional<TileV> currentTile;
    private final String nickname;
    private final PlayerColor color;
    private StatePlayerType statePlayer;
    private boolean deckAllowed; //todo bisogna aggiornare anche questo
    private int numDeck;
    private int credits;

    public void setCurrentTile(Optional<TileV> currentTile) {
        this.currentTile = currentTile;
    }

    public void setNumDeck(int numDeck) {
        this.numDeck = numDeck;
    }

    public PlayerV(String nickname, PlayerColor color, boolean[][] mask) {
        Optional<TileV>[][] grid = new Optional[12][12];

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                grid[i][j] = Optional.empty();
            }
        }
        this.spaceshipBoard = grid;
        ConnectorType[] connectors ={ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL};
        String imagepath;
        if(color.equals(PlayerColor.RED)) {
            imagepath = "/images/tiles/GT-new_tiles_16_for web52.jpg";
        } else if(color.equals(PlayerColor.YELLOW)) {
            imagepath = "/images/tiles/GT-new_tiles_16_for web61.jpg";
        } else if(color.equals(PlayerColor.GREEN)) {
            imagepath = "/images/tiles/GT-new_tiles_16_for web34.jpg";
        } else {
            imagepath = "/images/tiles/GT-new_tiles_16_for web33.jpg";
        }

        TileV initialtileV = new TileV(TileType.CABIN, connectors, RotationType.NORTH, true, imagepath,0,0,0,0,0,0,0,0,0,0);
        setSpaceshipBoardTile(initialtileV,new Coordinate(7,7));
        this.nickname = nickname;
        this.color = color;
        this.statePlayer = StatePlayerType.NOT_FINISHED;
        this.spaceshipMask = mask;
        this.currentTile = Optional.empty();
    }

    public boolean[][] getSpaceshipMask() {
        return spaceshipMask;
    }

    public void setSpaceshipBoardTile(TileV tileV, Coordinate coordinate) {
        spaceshipBoard[coordinate.x()][coordinate.y()] = Optional.of(tileV);
    }

    public Optional<TileV>[][] getSpaceshipBoard() {
        return spaceshipBoard;
    }

    public Optional<TileV> getCurrentTile() {
        return currentTile;
    }

    public boolean getDeckAllowed() {
        return deckAllowed;
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public StatePlayerType getStatePlayer() {
        return statePlayer;
    }

    public int getNumDeck() {
        return numDeck;
    }

}

