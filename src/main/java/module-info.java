module it.polimi.ingsw.is25am02 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.rmi;
    requires java.desktop;
    requires com.google.gson;
    requires java.sql;


    opens it.polimi.ingsw.is25am02.view.gui to javafx.fxml;
    exports it.polimi.ingsw.is25am02.view.gui to javafx.graphics;
    //exports it.polimi.ingsw.is25am02;
    exports it.polimi.ingsw.is25am02.network;
    opens it.polimi.ingsw.is25am02.view.gui.controllers to javafx.fxml;
    opens it.polimi.ingsw.is25am02.utils.enumerations to javafx.fxml;

}