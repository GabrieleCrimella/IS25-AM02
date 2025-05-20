package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class BuildController {
    @FXML
    private ImageView backgroundImageView;

    @FXML
    private ImageView draggableTile;

    @FXML
    private Pane boardPane;



    @FXML
    public void initialize(int level, PlayerColor color) {

        String imagePath;
        switch (level) {
            case 0:
                imagePath = "/image/cardboard/cardboard-1.jpg";
                break;
            case 2:
                imagePath = "/image/cardboard/cardboard-1b.jpg";
                break;
            default:
                imagePath = "/image/background.jpg"; // fallback
        }

        backgroundImageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        setupDragAndDrop();
    }

    private void setupDragAndDrop() {
        draggableTile.setOnDragDetected(event -> {
            draggableTile.startFullDrag(); // per eventi mouse drag
            event.consume();
        });

        // Rende ciascun "slot" sulla plancia reattivo al rilascio
        for (javafx.scene.Node node : boardPane.getChildren()) {
            if (node instanceof Pane slot) {
                slot.setOnMouseDragReleased(event -> {
                    // Rimuovi da dove era prima
                    ((Pane) draggableTile.getParent()).getChildren().remove(draggableTile);
                    // Aggiungi dentro lo slot
                    draggableTile.setLayoutX(0); // reset posizione relativa
                    draggableTile.setLayoutY(0);
                    slot.getChildren().add(draggableTile);
                    event.consume();
                });
            }
        }
    }


}
