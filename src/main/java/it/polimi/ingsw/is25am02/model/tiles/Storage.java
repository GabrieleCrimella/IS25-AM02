package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;

import java.util.List;

public final class Storage extends Tile {

    int maxNum;
    List<Box> occupation;

    public Storage(TileType t, ConnectorType[] connectors, RotationType rotationType, int id, int maxNum, List<Box> occupation) {
        super(t, connectors, rotationType, id);
        this.maxNum = maxNum;
        this.occupation = occupation;
    }

    @Override
    List<Box> getOccupation(){
        return occupation;
    }

    @Override
    void removeBox(Box box){
        occupation.remove(box);
    }

    @Override
    int getNumOccupation(){
        return occupation.size();
    }

    @Override
    public void addBox(Box box) {
        if (!box.getType().isSpecial())
            occupation.add(box);
    }

}
