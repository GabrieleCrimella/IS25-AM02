package it.polimi.ingsw.is25am02.model.cards.boxes;

import it.polimi.ingsw.is25am02.model.enumerations.BoxType;

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

        //inizializzo array di gialli
        for (int i=0; i < 17; i++){
            YellowBox yellowbox = new YellowBox(BoxType.YELLOW);
            store.get(BoxType.YELLOW).add(yellowbox);
        }

        //inizliazzo array di verdi
        for (int i=0; i < 13; i++){
            GreenBox greenBox = new GreenBox(BoxType.GREEN);
            store.get(BoxType.GREEN).add(greenBox);
        }

        //inizializzo array di blu
        for (int i=0; i < 14; i++){
            BlueBox blueBox = new BlueBox(BoxType.BLUE);
            store.get(BoxType.BLUE).add(blueBox);
        }

        for (int i=0; i < 12; i++){
            RedBox redBox = new RedBox(BoxType.RED);
            store.get(BoxType.RED).add(redBox);
        }

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
