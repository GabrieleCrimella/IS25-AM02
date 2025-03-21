package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;

import java.util.ArrayList;
import java.util.List;

//special storage può contenere tutte le box
public final class SpecialStorage extends Tile{
    int maxNum;
    ArrayList<Box> occupation;

    public SpecialStorage(TileType t, ConnectorType[] connectors, RotationType rotationType, int id, int maxNum) {
        super(t, connectors, rotationType, id);
        this.occupation = new ArrayList<>();
        this.maxNum = maxNum;
    }

    @Override
    public void addBox(Box box){
        occupation.add(box);
    }

    @Override
    public ArrayList<Box> getOccupation(){
        return occupation;
    }

    @Override
    public void removeBox(Box box){
        occupation.remove(box);
    }

    @Override
    public int getNumOccupation(){
        return occupation.size();
    }


}
