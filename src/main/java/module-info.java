module it.polimi.ingsw.is25am02 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.rmi;
    requires java.desktop;
    requires com.google.gson;
    requires java.sql;

    opens it.polimi.ingsw.is25am02.view.gui to javafx.fxml;
    opens it.polimi.ingsw.is25am02.view.gui.controllers to javafx.fxml;

    // Per Gson - solo opens è sufficiente
    opens it.polimi.ingsw.is25am02.utils.enumerations to javafx.fxml, com.google.gson;
    opens it.polimi.ingsw.is25am02.network.socket to com.google.gson;
    opens it.polimi.ingsw.is25am02.utils to com.google.gson;
    opens it.polimi.ingsw.is25am02.controller.client to com.google.gson;

    // Per JavaFX - export necessario per l'accesso pubblico
    exports it.polimi.ingsw.is25am02.view.gui to javafx.graphics;
    exports it.polimi.ingsw.is25am02.network;
}