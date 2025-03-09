package it.polimi.ingsw.is25am02.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import it.polimi.ingsw.is25am02.model.tiles.Cabin;
import it.polimi.ingsw.is25am02.model.tiles.SpecialStorage;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Spaceship {
    private static final String JSON_FILE_PATH = "src/main/resources/json/spaceship.json";
    private Tile[][] spaceshipBoard;
    private boolean[][] maskSpaceship;
    private int numOfWastedTiles;
    private int cosmicCredits;
    private Tile currentTile;
    private int x_start, y_start;

    public Spaceship(int level) {
        this.spaceshipBoard = new Tile[12][12];
        this.numOfWastedTiles = 0;
        this.cosmicCredits = 0;
        currentTile = null;

        loadSpaceshipMask(level);
    }

    //Todo: da togliere, serve solo per il testing, per vedere che la legge correttamente da json
    public boolean[][] getMaskSpaceship() {
        return maskSpaceship;
    }

    //todo: implemnetare il getter della posizione x e y di partenza delle tile, quella che in json è rappresentata con 2

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
                maskSpaceship = new boolean[maskNode.size()][maskNode.get(0).size()];  //convert the JSON node in a boolean matrix

                for (int i = 0; i < maskNode.size(); i++) {     //forall row
                    for (int j = 0; j < maskNode.get(i).size(); j++) {      //forall element
                        int value = maskNode.get(i).get(j).asInt();
                        if (value == 2) {
                            x_start = i;
                            y_start = j;
                        }
                        maskSpaceship[i][j] = maskNode.get(i).get(j).asInt() != 0; // true if the element is != 0
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file - spaceship mask");
            e.printStackTrace();
        }
    }

    public void addTile(int x, int y, Tile t) {
        spaceshipBoard[x][y] = t;
    }

    public Tile getTile(int x, int y) {
        return spaceshipBoard[x][y];
    }

    public void removeTile(int x, int y) {
        spaceshipBoard[x][y] = null;
    }

    public double calculateCannonPower() {
        return 0;
    }

    public int calculateMotorPower() {
        return 0;
    }

    public int calculateExposedConnectors() {
        return 0;
    }

    public int getCosmicCredits() {
        return cosmicCredits;
    }

    public void addCosmicCredits(int numCosmicCredits) {
        cosmicCredits += numCosmicCredits;
    }

    public void removeCosmicCredits(int numCosmicCredits) {
        cosmicCredits -= numCosmicCredits;
    }

    public int getNumOfWastedTiles() {
        return numOfWastedTiles;
    }

    public void addNumOfWastedTiles(int num) {
        numOfWastedTiles += num;
    }

    public boolean checkSpaceship() {
        return false;
    }

    public List<Tile> getbatteryStorage() {
        return null;
    }

    public void removeBattery(BatteryStorage t) {

    }

    public boolean isExposed(boolean row_column, boolean right_left) {
        return false;
    }

    public List<Cabin> getHumanCabins() {
        return null;
    }

    public List<Cabin> getPurpleCabins() {
        return null;
    }

    public List<Cabin> getBrownCabins() {
        return null;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public List<SpecialStorage> getStorageTiles() {
        return null;
    }

    public void calculateDamageMeteorites(ArrayList<Integer> meteorites, int line) {

    }

    public void calculateDamageShots(ArrayList<Integer> shots, int line) {
    }

    public void removeCrew(int alive) {
    }

    public int crewMember() {
        return 0;
    }

    public void boxManage() {
    }


}
