package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import it.polimi.ingsw.is25am02.model.tiles.Cabin;
import it.polimi.ingsw.is25am02.model.tiles.SpecialStorage;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.List;

public class Spaceship {
    Tile[][] spaceshipBoard;
    boolean[][] maskSpaceship;
    int numOfWastedTiles;
    int cosmicCredits;
    Tile currentTile;

    public Spaceship(int level) {
        this.spaceshipBoard = new Tile [12][12];
        this.numOfWastedTiles = 0;
        this.cosmicCredits = 0;
        currentTile = null;
        if  (level == 0){

        }
        else if (level == 1){

        }
        else {

        }
    }

    void startMask(){

    }

    void addTile(int x, int y, Tile t){
        spaceshipBoard[x][y] = t;
    }

    Tile getTile(int x, int y){
        return spaceshipBoard[x][y];
    }

    void removeTile(int x, int y){
        spaceshipBoard[x][y] = null;
    }

    double calculateCannonPower(){
        return 0.0;
    }

    double calculateMotorPower(){
        return 0.0;
    }

    int calculateExposedConnectors(){
        return 0;
    }

    int getCosmicCredits(){
        return cosmicCredits;
    }

    void addCosmicCredits(int numCosmicCredits){
        cosmicCredits += numCosmicCredits;
    }

    void removeCosmicCredits(int numCosmicCredits){
        cosmicCredits -= numCosmicCredits;
    }

    int getNumOfWastedTiles(){
        return numOfWastedTiles;
    }

    void addNumOfWastedTiles(int num){
        numOfWastedTiles += num;
    }

    boolean checkSpaceship(){
        return false;
    }

    void fixSpaceship(){

    }

    List<Tile> getbatteryStorage(){
        return null;
    }

    void removeBattery(BatteryStorage t){

    }

    boolean isExposed(boolean row_column, boolean right_left){
        return false;
    }

    List<Cabin> getHumanCabins(){
        return null;
    }

    List<Cabin> getPurpleCabins(){
        return null;
    }

    List<Cabin> getBrownCabins(){
        return null;
    }

    Tile getCurrentTile(){
        return currentTile;
    }

    List<SpecialStorage> getStorageTiles(){
        return null;
    }









}
