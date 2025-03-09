package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.ConnectorType;
import it.polimi.ingsw.is25am02.model.RotationType;
import it.polimi.ingsw.is25am02.model.TileType;

public class BatteryStorage extends Tile {
    int battery;
    int maxBattery;

    public BatteryStorage(TileType t, ConnectorType[] connectors, RotationType rotationType, int id, int maxBattery) {
        super(t, connectors, rotationType, id);
        this.battery = maxBattery;
        this.maxBattery = maxBattery;
    }

    public int getNumBattery() {
        return battery;
    }

    public void removeBattery(){
        battery = battery -1;
    }
}
