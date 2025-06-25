package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.RedBox;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpecialStorageTest {

    @Test
    void test_should_test_getter_method() {
        TileType t = TileType.SPECIAL_STORAGE;
        ConnectorType[] connectors = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.SINGLE};
        RotationType rotationType = RotationType.NORTH;
        int maxNum = 3;
        SpecialStorage storage = new SpecialStorage(t, connectors, rotationType, null, maxNum);

        Box box = new RedBox(BoxType.RED);
        storage.addBox(box);

        assertEquals(3, storage.getNumMaxBox());
        assertEquals(3, storage.getMaxNum());

        List<BoxType> type = new ArrayList<>();
        type.add(BoxType.RED);

        assertEquals(type, storage.getOccupationTypes());

        try {
            storage.removeBox(box);
        } catch (IllegalRemoveException e) {
            throw new RuntimeException(e);
        }
        assertEquals(0, storage.getNumOccupation());
    }
}