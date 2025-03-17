package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;

import java.util.ArrayList;
import java.util.List;

public sealed abstract class Tile permits BatteryStorage, BrownCabin, Cabin, Cannon, DoubleCannon, DoubleMotor, Motors, PurpleCabin, Shield, SpecialStorage, Storage, Structural{
//todo: ricordiamoci di mettere che all'inizio della preparazione della nave i giocatori trovano già posizionata la "tile iniziale" folorata al centro della loro nave
    private TileType tType;
    private ConnectorType[] connectors;
    private RotationType rotationType;
    private boolean visible;
    private int id;

    //Constructor
    public Tile(TileType t, ConnectorType[] connectors, RotationType rotationType, int id) {
        this.tType = t;
        // Connectors array has to be of length 4
        if (connectors.length != 4) {
            throw new IllegalArgumentException("Connectors array doesn't have exactly 4 elements.");
        }
        this.connectors = connectors;
        this.rotationType = rotationType;
        this.id = id;
        this.visible = false;
    }

    public int getId() {
        return id;
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
            return false;
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
    //metodi degli altri tipi

    //storage e specialStorage
    public List<Box> getOccupation(){

        List<Box> vuoto = new ArrayList<Box>();
        return vuoto;
    }

    public void removeBox(Box box){
    }

    public int getNumOccupation(){
        return 0;
    }

    public void addBox(Box box) {
    }

    //shield

    public boolean isShielded(RotationType side){
        return false;
    }

    //cabin
    public void setAlive(int human, int brown_alien, int purple_alien){}

    public int getNumHuman(){
        return 0;
    }

    public int getNumPurpleAlien(){
        return 0;
    }

    public int getNumBrownAlien(){
        return 0;}

    public void remove (int num){}

    //battery storage

    public int getNumBattery() {
        return 0;
    }

    public void removeBattery(){}
}
