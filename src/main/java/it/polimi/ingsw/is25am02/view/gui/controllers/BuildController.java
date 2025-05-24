package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.rmi.RemoteException;

public class BuildController extends GeneralController {
    @FXML
    public Pane postoInizialeTile;

    @FXML
    public StackPane root;
    @FXML
    public Label posizioneAttuale;
    @FXML
    public Label posizioneNuovaTile;
    @FXML
    public Pane tileCentrale;
    @FXML
    private ImageView backgroundImageView;
    private RotationType rotation;
    private ImageView currentTileImage;

    @FXML
    private Pane boardPane;

    private Coordinate coordinate;

    public void newTile(TileV newTile) {
        // Pulisce eventuali elementi precedenti
        rotation = RotationType.NORTH;
        postoInizialeTile.getChildren().clear();

        // Crea l'ImageView dinamicamente
        ImageView imageView = new ImageView();
        currentTileImage = imageView;
        imageView.setOnMouseClicked(e-> rotate());
        imageView.setFitWidth(149);
        imageView.setFitHeight(149);
        imageView.setLayoutX(10);
        imageView.setLayoutY(10);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.getStyleClass().add("draggableTile");

        // Imposta l'immagine (da una proprietà della TileV)
        //System.out.println(newTile.getImagePath());
        imageView.setImage(new Image(getClass().getResourceAsStream(newTile.getImagePath())));
        //imageView.setImage(new Image(getClass().getResourceAsStream("/image/tiles/GT-new_tiles_16_for_web93.jpg")));
        postoInizialeTile.getChildren().add(imageView);
        setupDragAndDrop(imageView);
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

        posizioneAttuale.setVisible(false);
        posizioneNuovaTile.setVisible(false);

        backgroundImageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));

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

        VBox temp = newNotificazionContainer();

        root.getChildren().add(temp);
        StackPane.setAlignment(temp, Pos.TOP_RIGHT);

        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.R) {
                        rotate();
                    }
                });

                // Facoltativo: forza il focus al root per ricevere eventi tastiera
                Platform.runLater(() -> root.requestFocus());
            }
        });

        aggiungiTileCentrale(color);
    }

    private void aggiungiTileCentrale(PlayerColor color) {
// Mappa colore → path immagine
        String imagePath = switch (color.toString().toLowerCase()) {
            case "red" -> "/image/tiles/GT-new_tiles_16_for web52.jpg";
            case "blue" -> "/image/tiles/GT-new_tiles_16_for web33.jpg";
            case "green" -> "/image/tiles/GT-new_tiles_16_for web34.jpg";
            case "yellow" -> "/image/tiles/GT-new_tiles_16_for web61.jpg";
            default -> throw new IllegalArgumentException("Colore non supportato: " + color);
        };

        // Crea ImageView
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        imageView.setFitWidth(tileCentrale.getPrefWidth());
        imageView.setFitHeight(tileCentrale.getPrefHeight());
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-cursor: hand;");

        // Rimuove contenuti precedenti e aggiunge l'immagine
        tileCentrale.getChildren().clear();
        tileCentrale.getChildren().add(imageView);
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
                    coordinate = getCoordinatesFromId(node);
                    posizioneAttuale.setVisible(true);
                    posizioneNuovaTile.setVisible(true);
                    posizioneNuovaTile.setText("(" + coordinate.x() + ", " + coordinate.y() + ")");

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

    @FXML
    public void onAddTile() {
        if (coordinate == null) {
            showNotification("Please select a tile position", NotificationType.ERROR, 5000);
            return;
        }

        try {
            GUIController.getInstance().getController().addTile(GUIController.getInstance().getNickname(), coordinate, rotation);
            showNotification("Tile added at " + coordinate, NotificationType.SUCCESS, 5000);

            // Reset the draggable tile
            coordinate = null;
            posizioneAttuale.setVisible(false);
            posizioneNuovaTile.setVisible(false);
            rotation = RotationType.NORTH;
        } catch (RemoteException e) {
            showNotification("Failed to add tile", NotificationType.ERROR, 5000);
        }
    }

    @FXML
    public void rotate() {
        if(currentTileImage == null) {
            showNotification("No tile to rotate", NotificationType.ERROR, 5000);
            return;
        }
        if (rotation == RotationType.NORTH) {
            rotation = RotationType.EAST;
        } else if (rotation == RotationType.EAST) {
            rotation = RotationType.SOUTH;
        } else if (rotation == RotationType.SOUTH) {
            rotation = RotationType.WEST;
        } else {
            rotation = RotationType.NORTH;
        }
        updateRotationDisplay();
    }

    private void updateRotationDisplay() {
        double angle = switch (rotation) {
            case NORTH -> 0;
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
        };
        currentTileImage.setRotate(angle);
    }
}
