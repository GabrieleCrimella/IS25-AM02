package it.polimi.ingsw.is25am02.model;

public enum ConnectorType {
    NONE, SINGLE, DOUBLE, UNIVERSAL;

    int numConnectorType;

    public int getNum() {
        return numConnectorType;
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
