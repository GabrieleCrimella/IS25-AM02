package it.polimi.ingsw.is25am02.view.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultController extends GeneralController{

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Label winnersLabel;

    public void initialize() {
        backgroundImage.setEffect(new GaussianBlur(30));


        Button closeButton = new Button("x");
        closeButton.setOnAction(e -> {
            Platform.exit();
            try {
                GUIController.getInstance().getController().closeConnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        });
    }

    public void showWinners(HashMap<String, Integer> winners) {
        StringBuilder winnersText = new StringBuilder("Winners: ");
        for (Map.Entry<String, Integer> entry : winners.entrySet()) {
            winnersText.append(entry.getKey())
                    .append(" (")
                    .append(entry.getValue())
                    .append(") ")
                    .append("  ");
        }
        winnersLabel.setText(winnersText.toString().trim());
    }


}
