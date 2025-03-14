package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;

import java.util.List;

public class SpecialStorage extends Tile{
    int maxNum;
    List<Box> occupation;

    public SpecialStorage(TileType t, ConnectorType[] connectors, RotationType rotationType, int id, int maxNum) {
        super(t, connectors, rotationType, id);
        this.maxNum = maxNum;
    }

    public void addBox(Box box){
        occupation.add(box);
    }

    List<Box> getOccupation(){
        return occupation;
    }

    void removeBox(Box box){
        occupation.remove(box);
    }

    int getNumOccupation(){
        return occupation.size();
    }


}
