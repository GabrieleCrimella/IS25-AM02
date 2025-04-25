package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;

public final class DoubleCannon extends Tile{
    public DoubleCannon(TileType t, ConnectorType[] connectors, RotationType rotationType, String imagePath) {
        super(t, connectors, rotationType, imagePath);
    }
}
