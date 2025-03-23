package it.polimi.ingsw.is25am02.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpaceshipIterator implements Iterator<Optional<Tile>>, Iterable<Optional<Tile>> {
    private final Optional<Tile>[][] spaceshipBoard;
    private boolean[][] spaceshipMask;
    private Pair<Integer, Integer> start_pos;
    private int x_current, y_current;
    //the first is x, the second is y

    private static final String JSON_FILE_PATH = "src/main/resources/json/spaceship.json";

    @SuppressWarnings("unchecked")
    public SpaceshipIterator(int level) {
        loadSpaceshipMask(level);

        spaceshipBoard = (Optional<Tile>[][]) new Optional[12][12]; // Creazione della matrice

        // Inizializza la matrice con Optional vuoti
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                spaceshipBoard[i][j] = Optional.empty();
            }
        }
    }

    public SpaceshipIterator reference() {
        x_current = 0;
        y_current = 0;
        return this;
    }

    public int getX_start() {
        return start_pos.getKey();
    }

    public int getY_start() {
        return start_pos.getValue();
    }

    private void loadSpaceshipMask(int level) {
        int spaceshipLevel;
        if (level == 0 || level == 1)
            spaceshipLevel = 1;
        else
            spaceshipLevel = 2;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(JSON_FILE_PATH));

            for (JsonNode levelNode : rootNode) {  //foreach JSON node
                // jump to the right level.
                // remember that level (paramether) 0 means test-flight: the level of the spaceship is 1
                if (levelNode.get("level").asInt() != spaceshipLevel)
                    continue;

                JsonNode maskNode = levelNode.get("mask"); // Estrai la maschera
                spaceshipMask = new boolean[maskNode.size()][maskNode.get(0).size()];  //convert the JSON node in a boolean matrix

                for (int i = 0; i < maskNode.size(); i++) {     //forall rows
                    for (int j = 0; j < maskNode.get(i).size(); j++) {      //forall elements
                        int value = maskNode.get(i).get(j).asInt();
                        if (value == 2) {
                            start_pos = new Pair<>(i, j);
                        }
                        spaceshipMask[i][j] = maskNode.get(i).get(j).asInt() != 0; // true if the element is != 0
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file - spaceship mask");
            e.printStackTrace();
        }
    }

    public void removeOneTile(int x, int y) {
        if (spaceshipMask[x][y])
            spaceshipBoard[x][y] = Optional.empty();
        else throw new IllegalArgumentException("Invalid tile position");
    }

    public List<Tile> returnAllTiles() {
        List<Tile> temp = new LinkedList<>();
        for (Optional<Tile> t : this.reference())
            if (t.isPresent())
                temp.add(t.get());
        return temp;
    }

    public List<Tile> getConnectedNearTiles(Tile t) {
        List<Tile> connectedNear = new LinkedList<>();

        if (getUpTile(t).isPresent() && t.checkConnectors(getUpTile(t).get(), RotationType.NORTH))
            connectedNear.add(getUpTile(t).get());
        if (getDownTile(t).isPresent() && t.checkConnectors(getRightTile(t).get(), RotationType.EAST))
            connectedNear.add(getDownTile(t).get());
        if (getLeftTile(t).isPresent() && t.checkConnectors(getDownTile(t).get(), RotationType.SOUTH))
            connectedNear.add(getLeftTile(t).get());
        if (getRightTile(t).isPresent() && t.checkConnectors(getLeftTile(t).get(), RotationType.WEST))
            connectedNear.add(getRightTile(t).get());

        return connectedNear;
    }

    //todo: il controllo di correttezza con la maschera andrebbe fatto forse SOLO durante il check della spaceship. DISCUTIAMONE E CAPIAMO.
    public void addTile(Tile tile, int x, int y) throws IllegalAddException {
        if (spaceshipMask[x][y] && spaceshipBoard[x][y].isEmpty())
            spaceshipBoard[x][y] = Optional.of(tile);
        else throw new IllegalAddException("Invalid tile position or occupied");
    }

    public Optional<Tile> getFirstTile() {
        for (Optional<Tile> t : this.reference()) {
            if (t.isPresent()) {
                return t;
            }
        }
        return Optional.empty();
    }

    public Optional<Tile> getTile(int x, int y) {
        return spaceshipBoard[x][y];
    }

    @Override
    public boolean hasNext() {
        for (int i = x_current; i < 12; i++) {
            for (int j = (i == x_current) ? y_current + 1 : 0; j < 12; j++) {
                if (spaceshipBoard[i][j].isPresent())
                    return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Tile> next() {
        for (int i = x_current; i < 12; i++) {
            for (int j = (i == x_current) ? y_current + 1 : 0; j < 12; j++) {
                if (spaceshipBoard[i][j].isPresent()) {
                    x_current = i;
                    y_current = j;
                    return spaceshipBoard[i][j];
                }
            }
        }
        throw new NoSuchElementException();
    }


    //todo fare metodo che ritorna coordinate da una tile. e usarlo per i metodi seguenti
    public int getX(Tile t) {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (spaceshipBoard[i][j].isPresent() && spaceshipBoard[i][j].get().equals(t))
                    return i;
            }
        }
        throw new NoSuchElementException();
    }

    public int getY(Tile t) {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (spaceshipBoard[i][j].isPresent() && spaceshipBoard[i][j].get().equals(t))
                    return j;
            }
        }
        throw new NoSuchElementException();
    }

    public Optional<Tile> getUpTile(Tile t) {
        return spaceshipBoard[getX(t)][getY(t) - 1];
    }

    public Optional<Tile> getDownTile(Tile t) {
        return spaceshipBoard[getX(t)][getY(t) + 1];
    }

    public Optional<Tile> getLeftTile(Tile t) {
        return spaceshipBoard[getX(t) - 1][getY(t)];
    }

    public Optional<Tile> getRightTile(Tile t) {
        return spaceshipBoard[getX(t) + 1][getY(t)];
    }

    public Optional<Tile> getFrontTile(Tile t) {
        return switch (t.getRotationType()) {
            case NORTH -> getUpTile(t);
            case EAST -> getRightTile(t);
            case SOUTH -> getDownTile(t);
            case WEST -> getLeftTile(t);
        };
    }

    @Override
    public Iterator<Optional<Tile>> iterator() {
        return this;
    }
}
