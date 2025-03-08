package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import it.polimi.ingsw.is25am02.model.tiles.Cabin;
import it.polimi.ingsw.is25am02.model.tiles.SpecialStorage;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.ArrayList;
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

    public void  addTile(int x, int y, Tile t){
        spaceshipBoard[x][y] = t;
    }

    public Tile getTile(int x, int y){
        return spaceshipBoard[x][y];
    }

    public void removeTile(int x, int y){
        spaceshipBoard[x][y] = null;
    }

    public double calculateCannonPower(){
        return 0.0;
    }

    public int calculateMotorPower(){
        return 0;
    }

    public int calculateExposedConnectors(){
        return 0;
    }

    public int getCosmicCredits(){
        return cosmicCredits;
    }

    public void addCosmicCredits(int numCosmicCredits){
        cosmicCredits += numCosmicCredits;
    }

    public void removeCosmicCredits(int numCosmicCredits){
        cosmicCredits -= numCosmicCredits;
    }

    public int getNumOfWastedTiles(){
        return numOfWastedTiles;
    }

    public void addNumOfWastedTiles(int num){
        numOfWastedTiles += num;
    }

    public boolean checkSpaceship(){
        return false;
    }

    public List<Tile> getbatteryStorage(){
        return null;
    }

    public void removeBattery(BatteryStorage t){

    }

    public boolean isExposed(boolean row_column, boolean right_left){
        return false;
    }

    public List<Cabin> getHumanCabins(){
        return null;
    }

    public List<Cabin> getPurpleCabins(){
        return null;
    }

    public List<Cabin> getBrownCabins(){
        return null;
    }

    public Tile getCurrentTile(){
        return currentTile;
    }

    public List<SpecialStorage> getStorageTiles(){
        return null;
    }

    public void calculateDamageMeteorites(ArrayList<Integer> meteorites, int line){

    }

    public void calculateDamageShots(ArrayList<Integer> shots, int line){

    }

    public void removeCrew(int alive){

    }







}
