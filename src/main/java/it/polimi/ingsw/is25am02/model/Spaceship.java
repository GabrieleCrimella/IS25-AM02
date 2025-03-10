package it.polimi.ingsw.is25am02.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am02.model.tiles.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.min;

public class Spaceship {
    private static final String JSON_FILE_PATH = "src/main/resources/json/spaceship.json";
    private final Tile[][] spaceshipBoard;
    private boolean[][] maskSpaceship;
    private int numOfWastedTiles;
    private int cosmicCredits;
    private final Tile currentTile;
    private int x_start, y_start;

    public Spaceship(int level) {
        this.spaceshipBoard = new Tile[12][12];
        this.numOfWastedTiles = 0;
        this.cosmicCredits = 0;
        currentTile = null;

        loadSpaceshipMask(level);
    }

    //serve solo per il testing, per vedere che la legge correttamente da json
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

                for (int i = 0; i < maskNode.size(); i++) {     //forall rows
                    for (int j = 0; j < maskNode.get(i).size(); j++) {      //forall elements
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

    //ritorna i cannoni doppi che può usare il giocatore
    public List<DoubleCannon> getNumOfDoubleCannon() {
        List<DoubleCannon> doubleCannons = new ArrayList<>();
        for (Tile[] tiles : spaceshipBoard) {
            for (Tile tile : tiles) {
                if (tile.getType().equals(TileType.D_CANNON)) {
                    doubleCannons.add((DoubleCannon) tile);
                }
            }
        }
        return doubleCannons;
    }

    //todo: fare il metodo, aggiungere a UML che VA AGGIUNTA UNA firma (overload) del metodo e che ho aggiunto il metodo getNumOfDoubleCannon
    public double calculateCannonPower(List<DoubleCannon> doubleCannons) {
        //calcola la potenza sinoglola dei cannoni singoli contando l'orientazione e quella dei cannoni doppi contando l'orientazione
        return 0.0;
    }

    public int calculateMotorPower(List<DoubleMotor> doubleMotors) {
        int power = 0;
        for (Tile[] tiles : spaceshipBoard) {
            for (Tile tile : tiles) {
                if (tile.getType().equals(TileType.MOTOR)) {
                    power++;
                }
            }
        }
        return power + doubleMotors.size() * 2;
    }

    //ritorna i motori doppi che può usare il giocatore
    public List<DoubleMotor> getNumOfDoubleMotor() {
        List<DoubleMotor> doubleMotor = new ArrayList<>();
        for (Tile[] tiles : spaceshipBoard) {
            for (Tile tile : tiles) {
                if (tile.getType().equals(TileType.D_MOTOR)) {
                    doubleMotor.add((DoubleMotor) tile);
                }
            }
        }
        return doubleMotor;
    }

    //todo: fare il metodo
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
        for (int i = 1; i < spaceshipBoard.length - 1; i++) {
            for (int j = 1; j < spaceshipBoard[i].length - 1; j++) {
                if (spaceshipBoard[i + 1][j] != null) {
                    if (!spaceshipBoard[i][j].checkConnectors(spaceshipBoard[i + 1][j], RotationType.EAST))
                        return false;
                }
                if (spaceshipBoard[i - 1][j] != null) {
                    if (!spaceshipBoard[i][j].checkConnectors(spaceshipBoard[i - 1][j], RotationType.WEST))
                        return false;
                }
                if (spaceshipBoard[i][j + 1] != null) {
                    if (!spaceshipBoard[i][j].checkConnectors(spaceshipBoard[i][j + 1], RotationType.NORTH))
                        return false;
                }
                if (spaceshipBoard[i][j - 1] != null) {
                    if (!spaceshipBoard[i][j].checkConnectors(spaceshipBoard[i][j - 1], RotationType.SOUTH))
                        return false;
                }
            }
        }
        //todo: controllare che i motori siano verso il dietro
        //todo: controllare che non ci sia niente nella tile dopo dove punta un cannone
        //todo: controllare che non ci sia niente dietro i motori

        //todo: guardare anche le cose double
        return true;
    }

    public List<BatteryStorage> getbatteryStorage() {
        List<BatteryStorage> batteryStorages = new ArrayList<>();
        for (Tile[] tiles : spaceshipBoard) {
            for (Tile tile : tiles) {
                if (tile.getType().equals(TileType.BATTERY)) {
                    batteryStorages.add((BatteryStorage) tile);
                }
            }
        }
        return batteryStorages;
    }

    public void removeBattery(BatteryStorage t) {
        t.removeBattery();
    }

    //todo: fare il metodo
    public boolean isExposed(boolean row_column, boolean right_left__or__up_down) {
        return true;
    }

    public List<Cabin> getHumanCabins() {
        List<Cabin> humanCabins = new ArrayList<>();
        for (Tile[] tiles : spaceshipBoard) {
            for (Tile tile : tiles) {
                if (tile.getType().equals(TileType.CABIN)) {
                    humanCabins.add((Cabin) tile);
                }
            }
        }
        return humanCabins;
    }

    public List<Cabin> getPurpleCabins() {
        List<Cabin> purpleCabins = new ArrayList<>();
        for (Tile[] tiles : spaceshipBoard) {
            for (Tile tile : tiles) {
                if (tile.getType().equals(TileType.PURPLE_CABIN)) {
                    purpleCabins.add((Cabin) tile);
                }
            }
        }
        return purpleCabins;
    }

    public List<Cabin> getBrownCabins() {
        List<Cabin> brownCabins = new ArrayList<>();
        for (Tile[] tiles : spaceshipBoard) {
            for (Tile tile : tiles) {
                if (tile.getType().equals(TileType.BROWN_CABIN)) {
                    brownCabins.add((Cabin) tile);
                }
            }
        }
        return brownCabins;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public List<SpecialStorage> getStorageTiles() {
        List<SpecialStorage> storageTiles = new ArrayList<>();
        for (Tile[] tiles : spaceshipBoard) {
            for (Tile tile : tiles) {
                if (tile.getType().equals(TileType.SPECIAL_STORAGE) || tile.getType().equals(TileType.STORAGE)) {
                    storageTiles.add((SpecialStorage) tile);
                }
            }
        }
        return storageTiles;
    }

    //todo: fare il metodo
    public void calculateDamageMeteorites(ArrayList<Integer> meteorites, int line) {

    }

    //todo: fare il metodo
    public void calculateDamageShots(ArrayList<Integer> shots, int line) {
    }

    //todo: rifare il metodo, creandone prima un altro che ritorna le tile che contengono delle persone vive.
    //todo: in questo metodo non passare il numero di vivi da rimuovere, ma passare le tile da cui rimuovere i vivi
    public void removeCrew(int alive) {
        for (Tile[] tiles : spaceshipBoard) {
            for (Tile tile : tiles) {
                if (tile.getType().equals(TileType.CABIN)) {
                    Cabin temp = (Cabin) tile;
                    int num = temp.getNumBrownAlien() + temp.getNumPurpleAlien() + temp.getNumHuman();
                    if (num > 0 && alive > 0) {
                        temp.remove(min(alive, num));
                    }
                }
            }
        }
    }

    //todo: fare il metodo. ritorna il numero di vivi sulla nave (aliens e umani). gli alieni contano doppio
    public int crewMember() {
        return 0;
    }

    //todo: fare il metodo
    public void boxManage() {
    }

    public int calculateNumAlive(){
        int alive = 0;
        for (Cabin cabin : getHumanCabins()) {
            alive +=cabin.getNumHuman();
            alive += 2*cabin.getNumPurpleAlien();
            alive += 2*cabin.getNumBrownAlien();
        }
        return alive;
    }



}
