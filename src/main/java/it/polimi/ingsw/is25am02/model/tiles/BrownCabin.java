package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.utils.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;

public final class BrownCabin extends Tile{
    public BrownCabin(TileType t, ConnectorType[] connectors, RotationType rotationType, String imagePath) {
        super(t, connectors, rotationType, imagePath);
    }
}
