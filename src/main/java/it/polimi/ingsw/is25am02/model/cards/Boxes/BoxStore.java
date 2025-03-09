package it.polimi.ingsw.is25am02.model.cards.boxes;

import it.polimi.ingsw.is25am02.model.BoxType;

import java.util.ArrayList;
import java.util.HashMap;

public class BoxStore {
    HashMap<BoxType, ArrayList<Box>> store;

    public BoxStore() {
        store = new HashMap<>();

        store.put(BoxType.RED, new ArrayList<>());
        store.put(BoxType.YELLOW, new ArrayList<>());
        store.put(BoxType.GREEN, new ArrayList<>());
        store.put(BoxType.BLUE, new ArrayList<>());
    }

    public HashMap<BoxType, ArrayList<Box>> getStore() {
        return store;
    }

    public void addBox(Box box){
        store.get(box.getType()).add(box);
    }

    public Box removeBox(BoxType type) {
        return store.get(type).removeFirst();
    }
}
