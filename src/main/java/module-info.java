module it.polimi.ingsw.is25am02 {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens it.polimi.ingsw.is25am02 to javafx.fxml;
    exports it.polimi.ingsw.is25am02;
}