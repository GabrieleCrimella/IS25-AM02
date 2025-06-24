package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.utils.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatteryStorageTest {

    @Test
    void test_should_getNumBattery() {
        TileType t = TileType.BATTERY;
        ConnectorType[] connectors = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.SINGLE};
        RotationType rotationType = RotationType.NORTH;
        int id = 1;
        int maxBattery = 3;
        BatteryStorage batteryStorage = new BatteryStorage(t, connectors, rotationType, null, maxBattery);
        assertEquals(3, batteryStorage.getNumBattery());
    }

    @Test
    void test_shoul_removeBattery() {
    }
}