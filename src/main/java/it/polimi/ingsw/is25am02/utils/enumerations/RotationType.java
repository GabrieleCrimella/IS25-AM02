package it.polimi.ingsw.is25am02.utils.enumerations;

public enum RotationType {
    NORTH(0), EAST(1), SOUTH(2), WEST(3);

    int numRotationType;

    RotationType(int num) {
        this.numRotationType = num;
    }

    public int getNum() {
        return numRotationType;
    }
}
