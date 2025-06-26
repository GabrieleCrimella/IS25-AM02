package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.cards.boxes.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {
    private Storage storage;
    private final ConnectorType[] connectors = {
            ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL,
            ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL
    };

    @Test
    void test_should_check_addBox() {
        TileType t = TileType.STORAGE;
        ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType = RotationType.NORTH;
        int id = 1;
        int maxNum = 3;
        Tile storage = new Storage(t, connectors, rotationType, null, maxNum);

        GreenBox greenBox = new GreenBox(BoxType.GREEN);
        List<Box> boxCorretti1 =  new ArrayList<>();
        boxCorretti1.add(greenBox);
        try {
            storage.addBox(greenBox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(boxCorretti1, storage.getOccupation());

        YellowBox yellowBox = new YellowBox(BoxType.YELLOW);
        List<Box> boxCorretti2 =  new ArrayList<>();
        boxCorretti2.add(yellowBox);
        boxCorretti2.add(greenBox);
        try {
            storage.addBox(yellowBox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(boxCorretti2, storage.getOccupation());
    }

    @Test
    void testAddBoxNormal() throws IllegalAddException {
        storage = new Storage(TileType.STORAGE, connectors, RotationType.NORTH, null, 3);
        GreenBox greenBox = new GreenBox(BoxType.GREEN);
        YellowBox yellowBox = new YellowBox(BoxType.YELLOW);

        storage.addBox(greenBox);
        storage.addBox(yellowBox);

        List<Box> expected = List.of(yellowBox, greenBox); // ordine per potenza
        assertEquals(expected, storage.getOccupation());
    }

    @Test
    void testAddBoxSpecialThrows() {
        storage = new Storage(TileType.STORAGE, connectors, RotationType.NORTH, null, 3);
        RedBox specialBox = new RedBox(BoxType.RED); // special == true

        IllegalAddException ex = assertThrows(IllegalAddException.class, () -> storage.addBox(specialBox));
        assertEquals("Box cannot be special box in normal storage", ex.getMessage());
    }

    @Test
    void testGetMaxNum() {
        storage = new Storage(TileType.STORAGE, connectors, RotationType.NORTH, null, 3);
        assertEquals(3, storage.getMaxNum());
        assertEquals(3, storage.getNumMaxBox()); // entrambi devono tornare lo stesso valore
    }

    @Test
    void testGetOccupationTypes(){
        storage = new Storage(TileType.STORAGE, connectors, RotationType.NORTH, null, 3);
        RedBox redBox = new RedBox(BoxType.RED);
        BlueBox blueBox = new BlueBox(BoxType.BLUE);

        try {
            storage.addBox(redBox);
        } catch (IllegalAddException e) {
            //go on
        }

        try {
            storage.addBox(blueBox);
        } catch (IllegalAddException e) {
            //go on
        }

        List<BoxType> types = storage.getOccupationTypes();
        assertEquals(List.of(BoxType.BLUE), types);
    }

    @Test
    void testRemoveBoxEmptyStorageThrows() {
        storage = new Storage(TileType.STORAGE, connectors, RotationType.NORTH, null, 3);
        GreenBox greenBox = new GreenBox(BoxType.GREEN);

        IllegalRemoveException ex = assertThrows(IllegalRemoveException.class, () -> storage.removeBox(greenBox));
        assertEquals("Storage empty", ex.getMessage());
    }

    @Test
    void testRemoveBoxNotContainedThrows() throws IllegalAddException {
        storage = new Storage(TileType.STORAGE, connectors, RotationType.NORTH, null, 3);
        GreenBox greenBox = new GreenBox(BoxType.GREEN);
        YellowBox yellowBox = new YellowBox(BoxType.YELLOW);

        storage.addBox(greenBox);

        IllegalRemoveException ex = assertThrows(IllegalRemoveException.class, () -> storage.removeBox(yellowBox));
        assertEquals("Storage doesn't contain box : YELLOW", ex.getMessage());
    }

    @Test
    void testRemoveBoxSuccessfully() throws IllegalAddException, IllegalRemoveException {
        storage = new Storage(TileType.STORAGE, connectors, RotationType.NORTH, null, 3);
        BlueBox box= new BlueBox(BoxType.BLUE);
        storage.addBox(box);

        assertEquals(1, storage.getNumOccupation());

        storage.removeBox(box);

        assertTrue(storage.getOccupation().isEmpty());
        assertEquals(0, storage.getNumOccupation());
    }
}