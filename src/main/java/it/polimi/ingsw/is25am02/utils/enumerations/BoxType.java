package it.polimi.ingsw.is25am02.utils.enumerations;

public enum BoxType {
    BLUE(1), GREEN(2), YELLOW(3), RED(4), NONE(0);

    private int power;

    BoxType(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    public static BoxType getBoxTypeByNum(int num) {
        return switch (num) {
            case 0 -> NONE;
            case 1 -> BLUE;
            case 2 -> GREEN;
            case 3 -> YELLOW;
            case 4 -> RED;
            default -> throw new IllegalArgumentException("Invalid BoxType number: " + num);
        };
    }

    public static int getNumByTypeBox(BoxType boxType) {
        return switch (boxType) {
            case NONE -> 0;
            case BLUE -> 1;
            case GREEN -> 2;
            case YELLOW -> 3;
            case RED -> 4;
            default -> throw new IllegalArgumentException("Invalid BoxType ");
        };
    }

    public boolean isSpecial(){
        return this==BoxType.RED;
    }
}
