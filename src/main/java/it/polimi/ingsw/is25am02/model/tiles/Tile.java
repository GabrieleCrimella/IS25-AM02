package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.ConnectorType;
import it.polimi.ingsw.is25am02.model.RotationType;
import it.polimi.ingsw.is25am02.model.TileType;

public abstract class Tile {
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
            thisTile[i] = connectors[positionThis].getNum();
            int positionOther= (i+t.rotationType.getNum())%4;
            otherTile[i] = connectors[positionOther].getNum();
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
}
