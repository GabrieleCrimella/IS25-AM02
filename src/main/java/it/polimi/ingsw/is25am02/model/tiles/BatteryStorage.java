package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;

public final class BatteryStorage extends Tile {
    int battery;
    int maxBattery;

    public BatteryStorage(TileType t, ConnectorType[] connectors, RotationType rotationType, int id, int maxBattery) {
        super(t, connectors, rotationType, id);
        this.battery = maxBattery;
        this.maxBattery = maxBattery;
    }

    @Override
    public int getNumBattery() {
        return battery;
    }

    @Override
    public void removeBattery() {
        if (battery > 0) {
            battery--;
        }
        else System.out.println("BatteryStorage empty");
    }
}
