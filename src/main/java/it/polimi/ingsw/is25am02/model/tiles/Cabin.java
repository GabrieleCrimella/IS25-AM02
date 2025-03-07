package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.ConnectorType;
import it.polimi.ingsw.is25am02.model.RotationType;
import it.polimi.ingsw.is25am02.model.TileType;

public class Cabin extends Tile{
    int numHuman;
    int numPurpleAlien;
    int numBrownAlien;

    public Cabin(TileType t, ConnectorType[] connectors, RotationType rotationType, int id, int numHuman, int numPurpleAlien, int numBrownAlien) {
        super(t, connectors, rotationType, id);
        this.numHuman = numHuman;
        this.numPurpleAlien = numPurpleAlien;
        this.numBrownAlien = numBrownAlien;
    }

    public int getNumHuman() {
        return numHuman;
    }

    public int getNumPurpleAlien() {
        return numPurpleAlien;
    }

    public int getNumBrownAlien() {
        return numBrownAlien;
    }

    public void remove (int num){
        if (numHuman != 0){
            numHuman =  numHuman - num;
        }
        if (numPurpleAlien != 0){
            numPurpleAlien = numPurpleAlien - num;
        }
        if (numBrownAlien != 0){
            numBrownAlien = numBrownAlien - num;
        }
    }


}
