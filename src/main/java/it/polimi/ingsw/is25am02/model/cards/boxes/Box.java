package it.polimi.ingsw.is25am02.model.cards.boxes;

import it.polimi.ingsw.is25am02.model.enumerations.BoxType;

public abstract class Box {
    private final int value;
    private final BoxType type;

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
