package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.BoxType;
import it.polimi.ingsw.is25am02.model.ConnectorType;
import it.polimi.ingsw.is25am02.model.RotationType;
import it.polimi.ingsw.is25am02.model.TileType;

public class Storage extends SpecialStorage{

    public Storage(TileType t, ConnectorType[] connectors, RotationType rotationType, int id, int maxNum) {
        super(t, connectors, rotationType, id, maxNum);
    }

    public boolean isvalid(BoxType t){
        return false;
    }


}
