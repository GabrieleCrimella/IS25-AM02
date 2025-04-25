package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.utils.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;

import java.util.ArrayList;
import java.util.List;

public final class Storage extends Tile {
    int maxNum;
    List<Box> occupation;

    public Storage(TileType t, ConnectorType[] connectors, RotationType rotationType, String imagePath, int maxNum) {
        super(t, connectors, rotationType, imagePath);
        this.maxNum = maxNum;
        this.occupation = new ArrayList<Box>();
    }

    @Override
    public int getMaxNum() { return maxNum; }

    @Override
    public List<Box> getOccupation(){
        return occupation;
    }

    @Override
    public void removeBox(Box box) throws IllegalRemoveException {
        if(occupation.isEmpty()){
            throw new IllegalRemoveException("Storage empty");
        }
        else if(!occupation.contains(box)){
            throw new IllegalRemoveException("Storage doesn't contain box : " + box.getType());
        }
        occupation.remove(box);
    }

    @Override
    public int getNumOccupation(){
        return occupation.size();
    }

    @Override
    public void addBox(Box box) throws IllegalAddException {
        if (!box.getType().isSpecial()){
            occupation.add(box);
            occupation.sort((b1, b2) -> Integer.compare(b2.getType().getPower(), b1.getType().getPower()));
            //i box dentro occupation sono ordinati da quello più importante al meno importante
        }
        else throw new IllegalAddException("Box cannot be special box in normal storage");
    }
}
