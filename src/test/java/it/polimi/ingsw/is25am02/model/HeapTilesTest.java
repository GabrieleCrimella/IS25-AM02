package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.tiles.Tile;
import org.junit.jupiter.api.Test;

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

}