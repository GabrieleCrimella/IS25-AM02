package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;

import java.util.ArrayList;
import java.util.List;

public class SpecialStorage extends Tile{
    int maxNum;
    ArrayList<Box> occupation;

    public SpecialStorage(TileType t, ConnectorType[] connectors, RotationType rotationType, int id, int maxNum) {
        super(t, connectors, rotationType, id);
        this.maxNum = maxNum;
    }

    public void addBox(Box box){
        occupation.add(box);
    }

    public ArrayList<Box> getOccupation(){
        return occupation;
    }

    public void removeBox(Box box){
        occupation.remove(box);
    }

    public int getNumOccupation(){
        return occupation.size();
    }


}
