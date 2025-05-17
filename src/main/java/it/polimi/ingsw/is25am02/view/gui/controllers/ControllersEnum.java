package it.polimi.ingsw.is25am02.view.gui.controllers;

public enum ControllersEnum {
    HOME("HomeScene"),
    LOBBY("lobby");

    private final String fxmlFileName;

    ControllersEnum(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }

    public String getFxmlFileName() {
        return fxmlFileName;
    }
}
