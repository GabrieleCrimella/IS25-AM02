package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.ConnectorType;
import it.polimi.ingsw.is25am02.model.RotationType;
import it.polimi.ingsw.is25am02.model.TileType;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;

public class Storage extends SpecialStorage {

    public Storage(TileType t, ConnectorType[] connectors, RotationType rotationType, int id, int maxNum) {
        super(t, connectors, rotationType, id, maxNum);
    }

    @Override
    public void addBox(Box box) {
        if (!box.getType().isSpecial())
            super.addBox(box);
    }
}
