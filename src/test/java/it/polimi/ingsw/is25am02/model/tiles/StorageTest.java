package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.cards.boxes.*;
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
}