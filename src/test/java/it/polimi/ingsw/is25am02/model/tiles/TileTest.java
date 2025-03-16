package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class TileTest {

    @Test
    void test_should_check_single_connects_with_single() {
        //single with single no rotation
        //first tile
        TileType t1 = TileType.BATTERY;
        ConnectorType[] connectors1 = {ConnectorType.NONE,ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.NONE };
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        int maxBattery1 = 3;
        BatteryStorage batteryStorage = new BatteryStorage(t1, connectors1, rotationType1, id1, maxBattery1);
        //second tile
        TileType t2 = TileType.MOTOR;
        ConnectorType[] connectors2 = {ConnectorType.NONE,ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE };
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Motors motor = new Motors(t2, connectors2, rotationType2, id2);
        //
        assertEquals(true, batteryStorage.checkConnectors(motor, RotationType.EAST));
    }

    @Test
    void test_should_check_single_connects_with_universal() {
        //single with single no rotation
        //first tile
        TileType t1 = TileType.BATTERY;
        ConnectorType[] connectors1 = {ConnectorType.NONE,ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.NONE };
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        int maxBattery1 = 3;
        BatteryStorage batteryStorage = new BatteryStorage(t1, connectors1, rotationType1, id1, maxBattery1);
        //second tile
        TileType t2 = TileType.MOTOR;
        ConnectorType[] connectors2 = {ConnectorType.NONE,ConnectorType.NONE, ConnectorType.NONE, ConnectorType.UNIVERSAL };
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Motors motor = new Motors(t2, connectors2, rotationType2, id2);
        //
        assertEquals(true, batteryStorage.checkConnectors(motor, RotationType.EAST));
    }

    @Test
    void test_should_check_double_connects_with_universal() {
        //single with single no rotation
        //first tile
        TileType t1 = TileType.BATTERY;
        ConnectorType[] connectors1 = {ConnectorType.NONE,ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.NONE };
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        int maxBattery1 = 3;
        BatteryStorage batteryStorage = new BatteryStorage(t1, connectors1, rotationType1, id1, maxBattery1);
        //second tile
        TileType t2 = TileType.MOTOR;
        ConnectorType[] connectors2 = {ConnectorType.NONE,ConnectorType.NONE, ConnectorType.NONE, ConnectorType.UNIVERSAL };
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Motors motor = new Motors(t2, connectors2, rotationType2, id2);
        //
        assertEquals(true, batteryStorage.checkConnectors(motor, RotationType.EAST));
    }

    @Test
    void test_should_check_double_connects_with_double() {
        //single with single no rotation
        //first tile
        TileType t1 = TileType.BATTERY;
        ConnectorType[] connectors1 = {ConnectorType.NONE,ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.NONE };
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        int maxBattery1 = 3;
        BatteryStorage batteryStorage = new BatteryStorage(t1, connectors1, rotationType1, id1, maxBattery1);
        //second tile
        TileType t2 = TileType.MOTOR;
        ConnectorType[] connectors2 = {ConnectorType.NONE,ConnectorType.NONE, ConnectorType.NONE, ConnectorType.DOUBLE };
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Motors motor = new Motors(t2, connectors2, rotationType2, id2);
        //
        assertEquals(true, batteryStorage.checkConnectors(motor, RotationType.EAST));
    }

    @Test
    void test_should_check_single_does_not_connect_with_double() {
        //single with single no rotation
        //first tile
        TileType t1 = TileType.BATTERY;
        ConnectorType[] connectors1 = {ConnectorType.NONE,ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.NONE };
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        int maxBattery1 = 3;
        BatteryStorage batteryStorage = new BatteryStorage(t1, connectors1, rotationType1, id1, maxBattery1);
        //second tile
        TileType t2 = TileType.MOTOR;
        ConnectorType[] connectors2 = {ConnectorType.NONE,ConnectorType.NONE, ConnectorType.NONE, ConnectorType.DOUBLE };
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Motors motor = new Motors(t2, connectors2, rotationType2, id2);
        //
        assertEquals(false, batteryStorage.checkConnectors(motor, RotationType.EAST));
    }

    @Test
    void test_should_check_single_does_not_connect_with_none() {
        //single with single no rotation
        //first tile
        TileType t1 = TileType.BATTERY;
        ConnectorType[] connectors1 = {ConnectorType.NONE,ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.NONE };
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        int maxBattery1 = 3;
        BatteryStorage batteryStorage = new BatteryStorage(t1, connectors1, rotationType1, id1, maxBattery1);
        //second tile
        TileType t2 = TileType.MOTOR;
        ConnectorType[] connectors2 = {ConnectorType.NONE,ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE };
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Motors motor = new Motors(t2, connectors2, rotationType2, id2);
        //
        assertEquals(false, batteryStorage.checkConnectors(motor, RotationType.EAST));
    }

    @Test
    void test_should_check_single_connects_with_single_with_rotation() {
        //single with single no rotation
        //first tile
        TileType t1 = TileType.BATTERY;
        ConnectorType[] connectors1 = {ConnectorType.SINGLE,ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE };
        RotationType rotationType1 = RotationType.EAST;
        int id1 = 1;
        int maxBattery1 = 3;
        BatteryStorage batteryStorage = new BatteryStorage(t1, connectors1, rotationType1, id1, maxBattery1);
        //second tile
        TileType t2 = TileType.MOTOR;
        ConnectorType[] connectors2 = {ConnectorType.NONE,ConnectorType.NONE, ConnectorType.NONE, ConnectorType.SINGLE };
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Motors motor = new Motors(t2, connectors2, rotationType2, id2);
        //
        assertEquals(true, batteryStorage.checkConnectors(motor, RotationType.EAST));
    }

    @Test
    void test_should_check_single_connects_with_universal_with_rotation() {
        //single with single no rotation
        //first tile
        TileType t1 = TileType.BATTERY;
        ConnectorType[] connectors1 = {ConnectorType.SINGLE,ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE };
        RotationType rotationType1 = RotationType.EAST;
        int id1 = 1;
        int maxBattery1 = 3;
        BatteryStorage batteryStorage = new BatteryStorage(t1, connectors1, rotationType1, id1, maxBattery1);
        //second tile
        TileType t2 = TileType.MOTOR;
        ConnectorType[] connectors2 = {ConnectorType.NONE,ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.NONE };
        RotationType rotationType2 = RotationType.SOUTH;
        int id2 = 1;
        Motors motor = new Motors(t2, connectors2, rotationType2, id2);
        //
        assertEquals(true, batteryStorage.checkConnectors(motor, RotationType.EAST));
    }

    @Test
    void test_should_check_single_does_not_connect_with_universal_with_rotation() {
        //single with single no rotation
        //first tile
        TileType t1 = TileType.BATTERY;
        ConnectorType[] connectors1 = {ConnectorType.SINGLE,ConnectorType.NONE, ConnectorType.NONE, ConnectorType.NONE };
        RotationType rotationType1 = RotationType.EAST;
        int id1 = 1;
        int maxBattery1 = 3;
        BatteryStorage batteryStorage = new BatteryStorage(t1, connectors1, rotationType1, id1, maxBattery1);
        //second tile
        TileType t2 = TileType.MOTOR;
        ConnectorType[] connectors2 = {ConnectorType.NONE,ConnectorType.UNIVERSAL, ConnectorType.NONE, ConnectorType.NONE };
        RotationType rotationType2 = RotationType.WEST;
        int id2 = 1;
        Motors motor = new Motors(t2, connectors2, rotationType2, id2);
        //
        assertEquals(false, batteryStorage.checkConnectors(motor, RotationType.EAST));
    }

    @Test
    void test_should_check_connector_on_given_side(){
        TileType t = TileType.BATTERY;
        ConnectorType[] connectors = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.UNIVERSAL};
        RotationType rotationType = RotationType.NORTH;
        int id = 1;
        int maxBattery = 3;
        BatteryStorage batteryStorage = new BatteryStorage(t, connectors, rotationType, id, maxBattery);
        assertEquals(ConnectorType.DOUBLE, batteryStorage.connectorOnSide(RotationType.NORTH));
        assertEquals(ConnectorType.NONE, batteryStorage.connectorOnSide(RotationType.EAST));
        assertEquals(ConnectorType.SINGLE, batteryStorage.connectorOnSide(RotationType.SOUTH));
        assertEquals(ConnectorType.UNIVERSAL, batteryStorage.connectorOnSide(RotationType.WEST));

    }



}
