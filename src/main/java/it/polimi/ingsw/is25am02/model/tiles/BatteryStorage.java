package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;

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
    public int getBattery() {
        return battery;
    }

    @Override
    public void removeBattery() throws IllegalRemoveException {
        if (battery > 0) {
            battery--;
        }
        else throw new IllegalRemoveException("BatteryStorage empty");
    }
}
