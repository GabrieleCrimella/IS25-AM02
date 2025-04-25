package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;

import java.util.ArrayList;

//special storage può contenere tutte le box
public final class SpecialStorage extends Tile{
    int maxNum;
    ArrayList<Box> occupation;

    public SpecialStorage(TileType t, ConnectorType[] connectors, RotationType rotationType, String imagePath, int maxNum) {
        super(t, connectors, rotationType, imagePath);
        this.occupation = new ArrayList<>();
        this.maxNum = maxNum;
    }

    @Override
    public int getMaxNum() { return maxNum; }

    @Override
    public void addBox(Box box){
        occupation.add(box);
        occupation.sort((b1, b2) -> Integer.compare(b2.getType().getPower(), b1.getType().getPower()));
        //i box dentro occupation sono ordinati da quello più importante al meno importante
    }

    @Override
    public ArrayList<Box> getOccupation(){
        return occupation;
    }

    @Override
    public void removeBox(Box box) throws IllegalRemoveException {
        if(occupation.isEmpty()){
            throw new IllegalRemoveException("SpecialStorage empty");
        }
        else if(!occupation.contains(box)){
            throw new IllegalRemoveException("SpecialStorage doesn't contain box : " + box.getType());
        }
        occupation.remove(box);
    }

    @Override
    public int getNumOccupation(){
        return occupation.size();
    }


}
