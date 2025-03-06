package it.polimi.ingsw.is25am02.model;

public enum BoxType {
    RED(4), BLUE(1), GREEN(2), YELLOW(3), NONE(0);

    private int power;

    private BoxType(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }
}
