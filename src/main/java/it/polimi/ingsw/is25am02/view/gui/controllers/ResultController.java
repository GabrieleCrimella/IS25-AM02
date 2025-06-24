package it.polimi.ingsw.is25am02.view.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultController extends GeneralController{
    @FXML
    public StackPane root;
    @FXML
    private ImageView backgroundImage;

    @FXML
    private Label winnersLabel;

    public void initialize() {
        backgroundImage.setEffect(new GaussianBlur(30));

        System.out.println("ma io qui ci sono passato!");

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

        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(10, 10, 0, 0));
        closeButton.getStyleClass().add("closeButton");

        root.getChildren().add(closeButton); // dove rootPane è il contenitore della vista

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
