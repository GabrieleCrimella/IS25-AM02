package it.polimi.ingsw.is25am02.model.cards.boxes;

import it.polimi.ingsw.is25am02.model.BoxType;

import java.util.ArrayList;
import java.util.HashMap;

import static it.polimi.ingsw.is25am02.model.BoxType.*;

public class BoxStore {
    HashMap<BoxType, ArrayList<Box>> store;

    public BoxStore(){
        store = new HashMap<>();
        //Here code to initialize Store with all boxes
        store.put(RED,new ArrayList<>());
        store.put(BLUE, new ArrayList<>());
        store.put(YELLOW, new ArrayList<>());
        store.put(GREEN, new ArrayList<>());
    }

    public HashMap<BoxType, ArrayList<Box>> getStore(){
        return store;
    }

    public void addBox(Box box){
        store.get(box.getType()).add(box);
    }

    public Box removeBox(BoxType type){
        return store.get(type).removeFirst();
    }
}
