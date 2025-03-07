package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.ConnectorType;
import it.polimi.ingsw.is25am02.model.RotationType;
import it.polimi.ingsw.is25am02.model.TileType;

public class PurpleCabin extends Cabin{
    public PurpleCabin(TileType t, ConnectorType[] connectors, RotationType rotationType, int id, int numHuman, int numPurpleAlien, int numBrownAlien) {
        super(t, connectors, rotationType, id, numHuman, numPurpleAlien, numBrownAlien);
    }
}
