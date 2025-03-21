package it.polimi.ingsw.is25am02.model;

import com.sun.jdi.connect.Connector;
import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HeapTilesTest {

    @Test
    void testShouldGetTile() {
        //creo le tile prendendo i dati da JSON
        HeapTiles heapTiles = new HeapTiles();

        //pesco una tile nuova
        Tile tile = heapTiles.drawTile();
        assertNotNull(tile);

        //ri-aggiungo la tile che ho scelto al mucchio
        heapTiles.addTile(tile,true);

        //verifico che sia stata reinserita nel mucchio in modo "visibile"
        Set<Tile> sT = heapTiles.getVisibleTiles();
        assertTrue(sT.size() == 1 && sT.contains(tile));

        //tolgo un elemento visibile dal mucchio
        heapTiles.removeVisibleTile(sT.iterator().next());

        //verifico che non ci siano più carte visibili
        Set<Tile> sT2 = heapTiles.getVisibleTiles();
        assertEquals(0, sT2.size());
    }


    @Test
    void loadTilesTest() {
        HeapTiles heapTiles = new HeapTiles();


        boolean foundStructural = false;
        boolean foundShield = false;
        boolean foundBattery = false;
        boolean foundPCabin = false;
        boolean foundBCabin = false;
        boolean foundDCannon = false;
        boolean foundStorage = false;
        boolean foundSStorage = false;
        boolean foundDMotor = false;
        boolean foundCannon = false;
        boolean foundCabin = false;
        boolean foundMotor = false;
        for (Tile tile : heapTiles.getSetTiles()) {
            ConnectorType[] connectors = new ConnectorType[4];

            if (tile.getType().equals(TileType.STRUCTURAL)) {
                foundStructural = true;
                assertEquals(TileType.STRUCTURAL, tile.getType());
                connectors[0] = ConnectorType.UNIVERSAL;
                connectors[1] = ConnectorType.DOUBLE;
                connectors[2] = ConnectorType.NONE;
                connectors[3] = ConnectorType.UNIVERSAL;
                if(Arrays.equals(connectors, tile.getConnectors())) {
                    assertEquals(connectors, tile.getConnectors());
                }
            }
            else if (tile.getType().equals(TileType.SHIELD)) {
                foundShield = true;
                assertEquals(TileType.SHIELD, tile.getType());
                connectors[0] = ConnectorType.NONE;
                connectors[1] = ConnectorType.DOUBLE;
                connectors[2] = ConnectorType.DOUBLE;
                connectors[3] = ConnectorType.UNIVERSAL;
                if(tile.isShielded(RotationType.NORTH) &&
                        tile.isShielded(RotationType.EAST) &&
                        !tile.isShielded(RotationType.SOUTH) &&
                        !tile.isShielded(RotationType.WEST) &&
                        Arrays.equals(connectors, tile.getConnectors())) {
                    assertEquals(connectors, tile.getConnectors());
                }
            }

            else if (tile.getType().equals(TileType.BATTERY)) {
                foundBattery = true;
                assertEquals(TileType.BATTERY, tile.getType());
                connectors[0] = ConnectorType.UNIVERSAL;
                connectors[1] = ConnectorType.SINGLE;
                connectors[2] = ConnectorType.DOUBLE;
                connectors[3] = ConnectorType.NONE;
                if(Arrays.equals(connectors, tile.getConnectors())) {
                    assertEquals(connectors, tile.getConnectors());
                }
            }

            else if (tile.getType().equals(TileType.BROWN_CABIN)) {
                foundBCabin = true;
                assertEquals(TileType.BROWN_CABIN, tile.getType());
                connectors[0] = ConnectorType.UNIVERSAL;
                connectors[1] = ConnectorType.SINGLE;
                connectors[2] = ConnectorType.DOUBLE;
                connectors[3] = ConnectorType.NONE;
                if(Arrays.equals(connectors, tile.getConnectors())) {
                    assertEquals(connectors, tile.getConnectors());
                }
            }
            else if (tile.getType().equals(TileType.PURPLE_CABIN)) {
                foundPCabin = true;
                assertEquals(TileType.PURPLE_CABIN, tile.getType());
                connectors[0] = ConnectorType.DOUBLE;
                connectors[1] = ConnectorType.SINGLE;
                connectors[2] = ConnectorType.NONE;
                connectors[3] = ConnectorType.SINGLE;
                if(Arrays.equals(connectors, tile.getConnectors())) {
                    assertEquals(connectors, tile.getConnectors());
                }
            }

            else if (tile.getType().equals(TileType.CABIN)) {
                foundCabin = true;
                assertEquals(TileType.CABIN, tile.getType());
                connectors[0] = ConnectorType.DOUBLE;
                connectors[1] = ConnectorType.NONE;
                connectors[2] = ConnectorType.DOUBLE;
                connectors[3] = ConnectorType.UNIVERSAL;
                if(Arrays.equals(connectors, tile.getConnectors())) {
                    assertEquals(connectors, tile.getConnectors());
                }
            }

            else if (tile.getType().equals(TileType.CANNON)) {
                foundCannon = true;
                assertEquals(TileType.CANNON, tile.getType());
                connectors[0] = ConnectorType.NONE;
                connectors[1] = ConnectorType.NONE;
                connectors[2] = ConnectorType.DOUBLE;
                connectors[3] = ConnectorType.NONE;
                if(Arrays.equals(connectors, tile.getConnectors())) {
                    assertEquals(connectors, tile.getConnectors());
                }
            }

            else if (tile.getType().equals(TileType.D_CANNON)) {
                foundDCannon = true;
                assertEquals(TileType.D_CANNON, tile.getType());
                connectors[0] = ConnectorType.NONE;
                connectors[1] = ConnectorType.NONE;
                connectors[2] = ConnectorType.DOUBLE;
                connectors[3] = ConnectorType.NONE;
                if(Arrays.equals(connectors, tile.getConnectors())) {
                    assertEquals(connectors, tile.getConnectors());
                }
            }

            else if (tile.getType().equals(TileType.MOTOR)) {
                foundMotor = true;
                assertEquals(TileType.MOTOR, tile.getType());
                connectors[0] = ConnectorType.DOUBLE;
                connectors[1] = ConnectorType.NONE;
                connectors[2] = ConnectorType.NONE;
                connectors[3] = ConnectorType.NONE;
                if(Arrays.equals(connectors, tile.getConnectors())) {
                    assertEquals(connectors, tile.getConnectors());
                }
            }

            else if (tile.getType().equals(TileType.D_MOTOR)) {
                foundDMotor = true;
                assertEquals(TileType.D_MOTOR, tile.getType());
                connectors[0] = ConnectorType.DOUBLE;
                connectors[1] = ConnectorType.NONE;
                connectors[2] = ConnectorType.NONE;
                connectors[3] = ConnectorType.UNIVERSAL;
                if(Arrays.equals(connectors, tile.getConnectors())) {
                    assertEquals(connectors, tile.getConnectors());
                }
            }

            else if (tile.getType().equals(TileType.SPECIAL_STORAGE)) {
                foundSStorage = true;
                assertEquals(TileType.SPECIAL_STORAGE, tile.getType());
                connectors[0] = ConnectorType.NONE;
                connectors[1] = ConnectorType.NONE;
                connectors[2] = ConnectorType.NONE;
                connectors[3] = ConnectorType.DOUBLE;
                if(Arrays.equals(connectors, tile.getConnectors()) && tile.getOccupation().size() == 2) {
                    assertEquals(connectors, tile.getConnectors());
                }
            }

            else if (tile.getType().equals(TileType.STORAGE)) {
                foundStorage = true;
                assertEquals(TileType.STORAGE, tile.getType());
                connectors[0] = ConnectorType.DOUBLE;
                connectors[1] = ConnectorType.UNIVERSAL;
                connectors[2] = ConnectorType.NONE;
                connectors[3] = ConnectorType.SINGLE;
                if(Arrays.equals(connectors, tile.getConnectors()) && tile.getOccupation().size() == 2) {
                    assertEquals(connectors, tile.getConnectors());
                }
            }
        }
        assertTrue(foundStructural);
        assertTrue(foundShield);
        assertTrue(foundBattery);
        assertTrue(foundPCabin);
        assertTrue(foundBCabin);
        assertTrue(foundMotor);
        assertTrue(foundCabin);
        assertTrue(foundSStorage);
        assertTrue(foundStorage);
        assertTrue(foundDCannon);
        assertTrue(foundCannon);
        assertTrue(foundDMotor);
    }
}

