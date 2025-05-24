package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.rmi.RemoteException;

public class BuildController {
    @FXML
    public Pane postoInizialeTile;
    @FXML
    private ImageView backgroundImageView;

    @FXML
    private ImageView draggableTile;

    @FXML
    private VBox notificationContainer;

    @FXML
    private Pane boardPane;

    public void newTile(TileV newTile) {
        // Pulisce eventuali elementi precedenti
        postoInizialeTile.getChildren().clear();

        // Crea l'ImageView dinamicamente
        ImageView imageView = new ImageView();
        imageView.setFitWidth(149);
        imageView.setFitHeight(149);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-cursor: hand;");

        // Imposta l'immagine (da una proprietà della TileV)
        //System.out.println(newTile.getImagePath());
        imageView.setImage(new Image(getClass().getResourceAsStream(newTile.getImagePath())));
        //imageView.setImage(new Image(getClass().getResourceAsStream("/image/tiles/GT-new_tiles_16_for_web93.jpg")));

        // Aggiungi al Pane
        postoInizialeTile.getChildren().add(imageView);
        setupDragAndDrop(imageView);
    }

    public enum NotificationType {
        INFO, SUCCESS, ERROR
    }

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
    }

    public Coordinate getCoordinatesFromId(Node node) {
        if (node == null || node.getId() == null) {
            return null;
        }

        String id = node.getId(); // es. "cell_3_5"
        if (!id.startsWith("cell_")) {
            return null;
        }

        String[] parts = id.split("_");
        if (parts.length != 3) {
            return null;
        }

        try {
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            return new Coordinate(row, col);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void showNotification(String message, NotificationType type, int durationMillis) {
        Label notification = new Label(message);
        notification.getStyleClass().add("toast-notification");

        switch (type) {
            case SUCCESS -> notification.getStyleClass().add("toast-success");
            case ERROR -> notification.getStyleClass().add("toast-error");
            case INFO -> notification.getStyleClass().add("toast-info");
        }

        notificationContainer.getChildren().add(notification);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), notification);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        PauseTransition delay = new PauseTransition(Duration.millis(durationMillis));
        delay.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), notification);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> notificationContainer.getChildren().remove(notification));
            fadeOut.play();
        });
        delay.play();
    }


    private void setupDragAndDrop(ImageView imageView) {
        imageView.setOnDragDetected(event -> {
            imageView.startFullDrag(); // per eventi mouse drag
            event.consume();
        });

        // Rende ciascun "slot" sulla plancia reattivo al rilascio
        for (javafx.scene.Node node : boardPane.getChildren()) {
            if (node instanceof Pane slot) {
                slot.setOnMouseDragReleased(event -> {
                    // Rimuovi da dove era prima
                    ((Pane) imageView.getParent()).getChildren().remove(imageView);
                    // Aggiungi dentro lo slot
                    imageView.setLayoutX(0); // reset posizione relativa
                    imageView.setLayoutY(0);
                    slot.getChildren().add(imageView);
                    event.consume();
                });
            }
        }
    }

    @FXML
    public void onTakeTile() {
        // Logica per prendere il tile, se necessario
        // Potrebbe essere un metodo da implementare in base alla logica del gioco
        try {
            GUIController.getInstance().getController().takeTile(GUIController.getInstance().getNickname());
        } catch (RemoteException e) {
            showNotification("Failed to take tile", NotificationType.ERROR, 500);
        }

        //showNotification("Take Tile", NotificationType.INFO, 500);
        System.out.println("button take tile pressed");
    }


}
