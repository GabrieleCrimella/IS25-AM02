package it.polimi.ingsw.is25am02.model.cards.Boxes;

import it.polimi.ingsw.is25am02.model.BoxType;

public abstract class Box {
    private int value;
    private BoxType type;

    public Box(int value, BoxType type) {
        this.value = value;
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public BoxType getType() {
        return type;
    }
}
