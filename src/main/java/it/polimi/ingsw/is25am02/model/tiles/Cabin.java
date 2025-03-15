package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;

public class Cabin extends Tile{
    int numHuman;
    int numPurpleAlien;
    int numBrownAlien;

    public Cabin(TileType t, ConnectorType[] connectors, RotationType rotationType, int id) {
        super(t, connectors, rotationType, id);
    }

    public void setAlive(int human, int brown_alien, int purple_alien){
        numHuman = human;
        numBrownAlien = brown_alien;
        numPurpleAlien = purple_alien;
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
