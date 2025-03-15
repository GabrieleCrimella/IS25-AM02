package it.polimi.ingsw.is25am02.model.enumerations;

public enum ConnectorType {
    NONE(0), SINGLE(1), DOUBLE(2), UNIVERSAL(3);

    private final int numConnectorType;

    ConnectorType(int numConnectorType) {
        this.numConnectorType = numConnectorType;
    }

    public int getNum() {
        return numConnectorType;
    }

    public static ConnectorType getConnectorTypeByNum(int num) {
        return switch (num) {
            case 1 -> SINGLE;
            case 2 -> DOUBLE;
            case 3 -> UNIVERSAL;
            default -> NONE;
        };
    }

    public boolean compatible(ConnectorType other) {
        if (this == NONE || other == NONE) {
            return false;
        }
        if (this == SINGLE && other == UNIVERSAL) {
            return true;
        }
        if (this == DOUBLE && other == UNIVERSAL) {
            return true;
        }
        if (this == UNIVERSAL && (other == SINGLE || other == DOUBLE)) {
            return true;
        }
        return false;
    }
}
