package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.Alive;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public sealed abstract class Tile permits BatteryStorage, BrownCabin, Cabin, Cannon, DoubleCannon, DoubleMotor, Motors, PurpleCabin, Shield, SpecialStorage, Storage, Structural{
//todo: ricordiamoci di mettere che all'inizio della preparazione della nave i giocatori trovano già posizionata la "tile iniziale" colorata al centro della loro nave
    private final TileType tType;
    private final ConnectorType[] connectors;
    private final String imagePath;
    private RotationType rotationType;
    private boolean visible;
    private int numMaxBattery;
    private int numMaxBox;
    protected ConcurrentHashMap<String, VirtualView> observers;
    //private final int id;

    //Constructor
    public Tile(TileType t, ConnectorType[] connectors, RotationType rotationType, String imagePath) {
        this.tType = t;
        // Connectors array has to be of length 4
        if (connectors.length != 4) {
            throw new IllegalArgumentException("Connectors array doesn't have exactly 4 elements.");
        }
        this.connectors = new ConnectorType[4];
        for (int i = 0; i < 4; i++) {
            this.connectors[i] = connectors[i];
        }
        this.rotationType = rotationType;
        this.imagePath = imagePath;
        this.visible = false;
    }

    public void setObservers(ConcurrentHashMap<String, VirtualView> observers) {
        this.observers = observers;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(){
        this.visible = true;
    }

    public RotationType getRotationType() {
        return rotationType;
    }

    public void setRotationType(RotationType rotationType) {
        this.rotationType = rotationType;
    }

    public ConnectorType[] getConnectors() {
        return connectors;
    }

    public TileType getType() {
        return tType;
    }

    public ConnectorType connectorOnSide(RotationType sideToCheck) {
        int[] thisTile = new int[4];
        for (int i = 0; i < 4; i++) {
            int positionThis= (i+this.rotationType.getNum())%4;
            thisTile[positionThis] = connectors[i].getNum();
        }
        return switch(sideToCheck){
            case NORTH -> ConnectorType.getConnectorTypeByNum(thisTile[0]);
            case EAST -> ConnectorType.getConnectorTypeByNum(thisTile[1]);
            case SOUTH -> ConnectorType.getConnectorTypeByNum(thisTile[2]);
            case WEST ->  ConnectorType.getConnectorTypeByNum(thisTile[3]);
        };
    }


    //tested
    //it is taken for granted that the tile being passed is actually touching the current tile on the sideToCheck
    public boolean checkConnectors (Tile t, RotationType sideToCheck){
        int[] thisTile = new int[4];
        int[] otherTile = new int[4];
        /*I'm taking for granted that the side I'm checking is in comparison to the this tile.
        So if sideToCheck is North, I'm checking the upper part of this tile and the bottom part of tile t.
         */

        /*Here I created two arrays where in each position I get the type of connector.
        The arrays are already shifted so that rotation is taken into account and
        they are rappresented as [North, East, South, West] as if rotation was 0.
        */
        for (int i = 0; i < 4; i++) {
            int positionThis= (i+this.rotationType.getNum())%4;
            thisTile[positionThis] = connectors[i].getNum();
            int positionOther= (i+t.rotationType.getNum())%4;
            otherTile[positionOther] = t.getConnectors()[i].getNum();
        }

        return switch (sideToCheck) {
            case NORTH -> compatible(thisTile[0], otherTile[2]);
            case EAST -> compatible(thisTile[1], otherTile[3]);
            case SOUTH -> compatible(thisTile[2], otherTile[0]);
            case WEST -> compatible(thisTile[3], otherTile[1]);
        };

    }

    public boolean compatible (int a, int b){
        if (a == 0 || b == 0){
            return true;
        }
        if ( a == b){
            return true;
        }
        if (a == 3 && (b == 1 || b == 2)){
            return true;
        }
        if (b == 3 && (a == 1 || a == 2)){
            return true;
        }
        else
            return false;
    }

    public int getNumMaxBattery() {
        return 0;
    }

    public int getNumMaxBox() {
        return 0;
    }

    //storage e specialStorage
    public List<Box> getOccupation()  throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported method");
    }

    public List<BoxType> getOccupationTypes()  throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported method");
    }

    public void removeBox(Box box) throws UnsupportedOperationException, IllegalRemoveException {
        throw new UnsupportedOperationException("Not supported method");
    }

    public int getNumOccupation() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported method");
    }

    public void addBox(Box box) throws UnsupportedOperationException, IllegalAddException {//usata solo per testing
        throw new UnsupportedOperationException("Not supported method");
    }

    public int getMaxNum() throws UnsupportedOperationException, IllegalAddException {
        throw new UnsupportedOperationException("Not supported method");
    }

    //shield
    public boolean isShielded(RotationType side) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported method");
    }

    //cabin
    public ArrayList<Alive> getCrew()  throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported method");
    }

    public void addCrew(String nicknameP, AliveType type) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported method");
    }

    public void removeCrew() throws UnsupportedOperationException, IllegalRemoveException {
        throw new UnsupportedOperationException("Not supported method");
    }

    //battery storage
    public int getNumBattery() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported method");
    }

    public int getBattery() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public void removeBattery() throws UnsupportedOperationException, IllegalRemoveException {
        throw new UnsupportedOperationException("Not supported method");
    }
}
