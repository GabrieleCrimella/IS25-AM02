package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PlayerV {
    private Optional<TileV>[][] spaceshipBoard;
    private boolean[][] spaceshipMask;
    private Optional<TileV> currentTile;
    private final HashMap<Integer, TileV> bookedTiles;
    private final String nickname;
    private final PlayerColor color;
    private StatePlayerType statePlayer;
    private boolean deckAllowed;
    private int numDeck;
    private int credits;
    private boolean isShipBroken;
    private int numBAliens;
    private int numPAliens;
    private int numHumans;
    private int numFinalRedBoxes;
    private int numFinalBlueBoxes;
    private int numFinalGreenBoxes;
    private int numFinalYellowBoxes;

    public void setCurrentTile(Optional<TileV> currentTile) {
        this.currentTile = currentTile;
    }

    public void setNumDeck(int numDeck) {
        this.numDeck = numDeck;
    }

    public PlayerV(String nickname, PlayerColor color, boolean[][] mask) {
        isShipBroken = false;
        Optional<TileV>[][] grid = new Optional[12][12];

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                grid[i][j] = Optional.empty();
            }
        }
        ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        String imagepath;
        if (color.equals(PlayerColor.RED)) {
            imagepath = "/image/tiles/GT-new_tiles_16_for web52.jpg";
        } else if (color.equals(PlayerColor.YELLOW)) {
            imagepath = "/image/tiles/GT-new_tiles_16_for web61.jpg";
        } else if (color.equals(PlayerColor.GREEN)) {
            imagepath = "/image/tiles/GT-new_tiles_16_for web34.jpg";
        } else {
            imagepath = "/image/tiles/GT-new_tiles_16_for web33.jpg";
        }

        TileV initialtileV = new TileV(TileType.CABIN, connectors, RotationType.NORTH, true, imagepath, 0, 0);
        this.spaceshipBoard = grid;
        setSpaceshipBoardTile(initialtileV, new Coordinate(7, 7));
        this.nickname = nickname;
        this.color = color;
        this.statePlayer = StatePlayerType.NOT_FINISHED;
        this.spaceshipMask = mask;
        this.currentTile = Optional.empty();
        this.deckAllowed = false;
        this.bookedTiles = new HashMap<>();
        bookedTiles.put(1, null);
        bookedTiles.put(2, null);
    }

    public boolean isShipBroken() {
        return isShipBroken;
    }

    public void setShipBroken(boolean shipBroken) {
        isShipBroken = shipBroken;
    }

    public boolean[][] getSpaceshipMask() {
        return spaceshipMask;
    }

    public void setStatePlayer(StatePlayerType statePlayer) {
        this.statePlayer = statePlayer;
    }

    public void setSpaceshipBoardTile(TileV tileV, Coordinate coordinate) {
        spaceshipBoard[coordinate.x()][coordinate.y()] = Optional.of(tileV);
    }

    public void setDeckAllowed() {
        this.deckAllowed = true;
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

    public int getCredits() {
        return credits;
    }

    public StatePlayerType getStatePlayer() {
        return statePlayer;
    }

    public int getNumDeck() {
        return numDeck;
    }

    public HashMap<Integer, TileV> getBookedTiles() {
        return bookedTiles;
    }

    public void setBookedTiles(int position, TileV tileV) {
        bookedTiles.put(position, tileV);

    }

    public int getNumFinalRedBoxes() {
        calculateBoxes();
        return numFinalRedBoxes;
    }

    public int getNumFinalBlueBoxes() {
        calculateBoxes();
        return numFinalBlueBoxes;
    }

    public int getNumFinalGreenBoxes() {
        calculateBoxes();
        return numFinalGreenBoxes;
    }

    public int getNumFinalYellowBoxes() {
        calculateBoxes();
        return numFinalYellowBoxes;
    }

    public void calculateBoxes(){
        numFinalBlueBoxes = 0;
        numFinalRedBoxes = 0;
        numFinalGreenBoxes = 0;
        numFinalYellowBoxes = 0;
        for(int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (spaceshipBoard[i][j].isPresent()) {
                    TileV tile = spaceshipBoard[i][j].get();
                    numFinalBlueBoxes += tile.getNumBlueBox();
                    numFinalRedBoxes += tile.getNumRedBox();
                    numFinalGreenBoxes += tile.getNumGreenBox();
                    numFinalYellowBoxes += tile.getNumYellowBox();
                }
            }
        }
    }

    public int getNumBatteries(){
        int num =0;
        for(int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (spaceshipBoard[i][j].isPresent()) {
                    TileV tile = spaceshipBoard[i][j].get();
                    num += tile.getNumBattery();
                }
            }
        }
        return num;
    }

    public List<AliveType> getAlive(){
        ArrayList<AliveType> aliveList = new ArrayList<>();
        for(int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (spaceshipBoard[i][j].isPresent()) {
                    TileV tile = spaceshipBoard[i][j].get();
                    if (tile.getNumHumans() == 1) {
                        aliveList.add(AliveType.HUMAN);
                    } else if (tile.getNumHumans() == 2) {
                        aliveList.add(AliveType.HUMAN);
                        aliveList.add(AliveType.HUMAN);
                    } else if (tile.getNumPAliens() == 1) {
                        aliveList.add(AliveType.PURPLE_ALIEN);
                    } else if (tile.getNumBAliens() == 1) {
                        aliveList.add(AliveType.BROWN_ALIEN);
                    }
                }
            }
        }
        return aliveList;
    }

    public int calculateNumBAliens() {
        numBAliens = 0;
        for (AliveType a : getAlive()) {
            if (a.equals(AliveType.BROWN_ALIEN)) {
                numBAliens++;
            }
        }
        return numBAliens;
    }

    public int calculateNumPAliens() {
        numPAliens = 0;
        for (AliveType a : getAlive()) {
            if (a.equals(AliveType.PURPLE_ALIEN)) {
                numPAliens++;
            }
        }
        return numPAliens;
    }

    public int calculateNumHumans() {
        numHumans = 0;
        for (AliveType a : getAlive()) {
            if (a.equals(AliveType.HUMAN)) {
                numHumans++;
            }
        }
        return numHumans;
    }



}

