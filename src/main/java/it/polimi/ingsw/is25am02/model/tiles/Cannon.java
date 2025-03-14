package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;

public class Cannon extends Tile {
    public Cannon(TileType t, ConnectorType[] connectors, RotationType rotationType, int id) {
        super(t, connectors, rotationType, id);
    }
}
