package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.utils.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;

public final class BatteryStorage extends Tile {
    int battery;
    int maxBattery;

    public BatteryStorage(TileType t, ConnectorType[] connectors, RotationType rotationType, String imagePath, int maxBattery) {
        super(t, connectors, rotationType, imagePath);
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
