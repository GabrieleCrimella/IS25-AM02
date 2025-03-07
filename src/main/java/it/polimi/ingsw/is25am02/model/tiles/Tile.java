package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.ConnectorType;
import it.polimi.ingsw.is25am02.model.RotationType;
import it.polimi.ingsw.is25am02.model.TileType;

public abstract class Tile {
    private TileType tType;
    private ConnectorType[] connectors; // Correct declaration
    private RotationType rotationType;
    private boolean visibile;
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
        this.visibile = false;
    }

    public int getId() {
        return id;
    }

    public boolean isVisibile() {
        return visibile;
    }

    public RotationType getRotationType() {
        return rotationType;
    }

    public ConnectorType[] getConnectors() {
        return connectors;
    }

    public TileType gettType() {
        return tType;
    }

    public boolean checkConnectors (Tile t, RotationType sideToCheck){
        return false;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }
}
