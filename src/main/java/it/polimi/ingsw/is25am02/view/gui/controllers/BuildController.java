package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.CardV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.HeapTileV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public class BuildController extends GeneralController {
    @FXML
    public Pane postoInizialeTile;
    @FXML
    private StackPane heapTilePopup;
    @FXML
    public StackPane root;
    @FXML
    public Label posizioneAttuale;
    @FXML
    public Label posizioneNuovaTile;
    @FXML
    public Pane tileCentrale;
    @FXML
    private GridPane heapGrid;
    @FXML
    private VBox playerButtonsContainer;
    @FXML
    private ImageView backgroundImageView;
    private RotationType rotation;
    private ImageView currentTileImage;
    @FXML
    private StackPane hourglassPopup;
    @FXML
    private Label hourglassTimeLabel;
    @FXML
    private Pane SpaceshipPane;
    @FXML
    private Group livello2Group;

    @FXML private Button miniDeck1;
    @FXML private Button miniDeck2;
    @FXML private Button miniDeck3;
    @FXML private Label miniDecklabel;


    @FXML
    private StackPane viewSpaceshipPopup;

    @FXML private StackPane miniDeckPopup;

    @FXML
    private HBox miniDeckCardsContainer;

    @FXML
    private ImageView popupBackgroundImage;

    @FXML
    private void closeSpacePopup() {
        viewSpaceshipPopup.setVisible(false);
    }

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

    public void newCard(CardV newCard) {
        // Pulisce eventuali elementi precedenti

        // Crea l'ImageView dinamicamente
        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(500);
        imageView.setLayoutX(10);
        imageView.setLayoutY(10);
        imageView.setPreserveRatio(true);

        imageView.setImage(new Image(getClass().getResourceAsStream(newCard.getImagePath())));

        miniDeckCardsContainer.getChildren().add(imageView);

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
        if(level ==2){
            miniDeck1.setVisible(true);
            miniDeck2.setVisible(true);
            miniDeck3.setVisible(true);
            miniDecklabel.setVisible(true);
        } else {
            livello2Group.setVisible(false);
            miniDeck1.setVisible(false);
            miniDeck2.setVisible(false);
            miniDeck3.setVisible(false);
            miniDecklabel.setVisible(false);
        }

        posizioneAttuale.setVisible(false);
        posizioneNuovaTile.setVisible(false);

        backgroundImageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));

        popupBackgroundImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));

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
        List<String> playersNick = GUIController.getInstance().getController().getPlayers();
        List<String> otherPlayers = playersNick.stream()
                .filter(name -> !name.equals(GUIController.getInstance().getNickname()))
                .toList();

        setOtherPlayers(otherPlayers);
    }

    private StackPane createTile(TileV tile) {
        // Container della tile
        StackPane tilePane = new StackPane();
        tilePane.setPrefSize(149, 149); // stessa misura usata altrove
        tilePane.setMinSize(149, 149);
        tilePane.setMaxSize(149, 149);

        // Crea e configura l'immagine della tile
        ImageView imageView = new ImageView();
        imageView.setFitWidth(149);
        imageView.setFitHeight(149);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(getClass().getResourceAsStream(tile.getImagePath())));

        tilePane.getChildren().add(imageView);
        return tilePane;
    }

    @FXML
    public void closeMiniDeckPopup() {
        miniDeckPopup.setVisible(false);
        miniDeckCardsContainer.getChildren().clear();
    }

    @FXML
    public void onViewHourglass() {
        long remainingTime = GUIController.getInstance().getController().getGameV().getHourglass().getTimeLeft();

        hourglassTimeLabel.setText("Tempo residuo: " + remainingTime);
        // Mostra il popup con sfondo sfocato
        hourglassPopup.setVisible(true);
    }

    @FXML
    public void closeHourglassPopup() {
        hourglassPopup.setVisible(false);
    }


    @FXML
    public void onFlipHourglass() {
        try {
            GUIController.getInstance().getController().flipHourglass(GUIController.getInstance().getNickname());
            showNotification("Hourglass flipped!", NotificationType.SUCCESS, 2000);
        } catch (RemoteException e) {
            showNotification("Impossible to flip the hourglass", NotificationType.ERROR, 3000);
        }
    }

    public void setOtherPlayers(List<String> otherPlayerNicknames) {
        playerButtonsContainer.getChildren().clear();
        for (Node node : SpaceshipPane.getChildren()) {
            if (node instanceof Pane pane && pane.getId() != null && pane.getId().startsWith("cell_")) {
                pane.getChildren().clear();
            }
        }

        for (String nickname : otherPlayerNicknames) {
            Button playerButton = new Button(nickname);
            playerButton.setPrefWidth(150);
            playerButton.setPrefHeight(51);
            playerButton.getStyleClass().add("main-button");

            // Puoi anche settare un'azione personalizzata qui
            playerButton.setOnAction(e -> {
                try {
                    PlayerV playerToShow = GUIController.getInstance().getController().getPlayerVFromNickname(nickname);
                    Optional<TileV>[][] spaceship = playerToShow.getSpaceshipBoard();
                    for (int row = 0; row < spaceship.length; row++) {
                        for (int col = 0; col < spaceship[row].length; col++) {
                            Optional<TileV> tileOpt = spaceship[row][col];
                            if (tileOpt.isPresent()) {
                                TileV tile = tileOpt.get();
                                StackPane tileNode = createTile(tile);

                                for (Node node : SpaceshipPane.getChildren()) {
                                    if (node instanceof Pane && node.getId() != null && node.getId().equals("cell_" + row + "_" + col)) {
                                        ((Pane) node).getChildren().add(tileNode);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    // Mostra il popup
                    viewSpaceshipPopup.setVisible(true);

                } catch (RemoteException ex) {
                    showNotification("Fail to get player from nickname", NotificationType.ERROR, 5000);
                }

            });

            playerButtonsContainer.getChildren().add(playerButton);
        }
    }

    @FXML
    public void onTakeMiniDeck(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String deckId = clickedButton.getText();  // "1", "2", "3"

        try {
            GUIController.getInstance().getController().takeMiniDeck(GUIController.getInstance().getNickname(), Integer.parseInt(deckId)-1);
            List<CardV> minideckTaken = GUIController.getInstance().getController().getGameV().getDeck().getDeck().get(Integer.parseInt(deckId)-1);
            for(CardV card: minideckTaken){
                newCard(card);
            }
            miniDeckPopup.setVisible(true);
        } catch (RemoteException e) {
            showNotification("Failed to take mini deck " + deckId, NotificationType.ERROR, 5000);
        }
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
            showNotification("Tile added at row " + coordinate.x()+" column "+coordinate.y(), NotificationType.SUCCESS, 5000);

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
    public void onReturnTile(){
        try {
            GUIController.getInstance().getController().returnTile(GUIController.getInstance().getNickname());
            showNotification("Tile returned successfully", NotificationType.SUCCESS, 5000);
            postoInizialeTile.getChildren().remove(currentTileImage);
            currentTileImage = null;
        } catch (RemoteException e) {
            showNotification("Failed to return tile", NotificationType.ERROR, 5000);
        }
    }

    @FXML
    public void onHeapTile(){
        heapTilePopup.setVisible(true);
        try {
            HeapTileV heapTileV = GUIController.getInstance().getController().getHeapTiles();
            populateHeapGrid(heapTileV.getListTileV());
            heapTilePopup.setVisible(true);
        } catch (RemoteException e) {
            showNotification("Failed to show Heap Tile", NotificationType.ERROR, 5000);
        }
    }

    private void populateHeapGrid(List<TileV> tiles) {
        heapGrid.getChildren().clear();

        int cols = 4;
        int row = 0;
        int col = 0;

        for (TileV tile : tiles) {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(149);
            imageView.setFitHeight(149);
            imageView.setPreserveRatio(true);
            imageView.setStyle("-fx-cursor: hand;");
            imageView.setImage(new Image(getClass().getResourceAsStream(tile.getImagePath())));
            imageView.setOnMouseClicked(e -> {
                // Quando clicco su una tile nell'heap, la uso come nuova tile centrale
                heapTilePopup.setVisible(false); // Chiudi il popup
                newTile(tile); // Mostra la tile in basso con comportamento interattivo
                try {
                    GUIController.getInstance().getController().takeTile(GUIController.getInstance().getNickname(), tile.getImagePath());
                } catch (RemoteException ex) {
                    showNotification("Failed to remove from Heap Tile", NotificationType.ERROR, 5000);
                }

            }); // no mano, no drag

            Pane tilePane = new Pane();
            tilePane.setPrefSize(149, 149);
            tilePane.getChildren().add(imageView);

            heapGrid.add(tilePane, col, row);

            col++;
            if (col == cols) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    public void closeHeapPopup(ActionEvent event) {
        heapTilePopup.setVisible(false);
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
