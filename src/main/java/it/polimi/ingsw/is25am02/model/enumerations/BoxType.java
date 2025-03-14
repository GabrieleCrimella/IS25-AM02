package it.polimi.ingsw.is25am02.model.enumerations;

public enum BoxType {
    BLUE(1), GREEN(2), YELLOW(3), RED(4), NONE(0);

    private int power;

    private BoxType(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    public boolean isSpecial(){
        return this==BoxType.RED;
    }
}
