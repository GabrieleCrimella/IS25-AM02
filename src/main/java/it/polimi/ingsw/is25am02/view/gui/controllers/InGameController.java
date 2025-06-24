package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.CardV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;
import java.util.*;

public class InGameController extends GeneralController {
    @FXML
    private StackPane root;
    @FXML
    private Group level1Gameboard;
    @FXML
    private Group level2Gameboard;
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private ImageView popupBackgroundImage;
    @FXML
    private VBox playerButtonsContainer;
    @FXML
    private Pane SpaceshipPane;
    @FXML
    private StackPane viewSpaceshipPopup;
    @FXML
    private Label creditsValue;
    @FXML
    private Label lostTilesValue;
    @FXML
    private Label batteriesValue;
    @FXML
    private Label RedValue;
    @FXML
    private Label GreenValue;
    @FXML
    private Label BlueValue;
    @FXML
    private Label YellowValue;
    @FXML
    private Label humansValue;
    @FXML
    private Label brownAliensValue;
    @FXML
    private Label purpleAliensValue;
    @FXML
    private ImageView MySpaceshipImage;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private Pane MySpaceship;
    @FXML
    private Pane cardPane;
    @FXML
    private ImageView playerBluePiece;
    @FXML
    private ImageView playerGreenPiece;
    @FXML
    private ImageView playerRedPiece;
    @FXML
    private ImageView playerYellowPiece;
    @FXML
    private Label currentPlayerNameLabel;
    @FXML
    private Button choiceboxtrue;
    @FXML
    private Button finishcannon;
    @FXML
    private Button choiceboxfalse;
    @FXML
    private Button finishmoveboxes;
    @FXML
    private Button finishmotor;
    @FXML
    private Button rollDice;
    @FXML
    private ImageView diceAnimation;
    @FXML
    private Label diceResult;
    @FXML
    private Label myname;
    @FXML
    private Button calculatedamage;
    @FXML
    private Button noChoicePlanet;
    @FXML
    private Button choiceYes;
    @FXML
    private Button choiceNo;
    @FXML
    private VBox commentBox;


    private Map<Integer, Pane> GameboardCells = new HashMap<>();
    private Map<String, PlayerColor> playerColors = new HashMap<>();
    private Map<Coordinate, BoxType> myBoxes = new HashMap<>();
    private List<Coordinate> batteries = new ArrayList<>();
    private List<Coordinate> doubles = new ArrayList<>();
    private Coordinate boxCoordinate;
    private int doubleCount = 0;
    private int batteryCount = 0;
    @FXML
    private Label doubleLabel;
    @FXML
    private Label batteryLabel;

    @FXML
    public void initialize(int level, PlayerColor color) {
        String imagePath;
        String imagePathMySpaceship;
        root.getStylesheets().add(
                getClass().getResource("/style/style.css").toExternalForm()
        );
        playerYellowPiece.setImage(new Image(getClass().getResourceAsStream("/image/pieces/yellowPiece.png")));
        playerRedPiece.setImage(new Image(getClass().getResourceAsStream("/image/pieces/redPiece.png")));
        playerGreenPiece.setImage(new Image(getClass().getResourceAsStream("/image/pieces/greenPiece.png")));
        playerBluePiece.setImage(new Image(getClass().getResourceAsStream("/image/pieces/bluePiece.png")));
        switch (level) {
            case 0:
                imagePath = "/image/cardboard/cardboard-3.png";
                imagePathMySpaceship = "/image/cardboard/cardboard-1.jpg";
                for (Node node : level1Gameboard.getChildren()) {
                    if (node instanceof Pane && node.getId() != null) {
                        int id = Integer.parseInt(node.getId());
                        GameboardCells.put(id, (Pane) node);
                    }
                }
                break;
            case 2:
                imagePath = "/image/cardboard/cardboard-5.png";
                imagePathMySpaceship = "/image/cardboard/cardboard-1b.jpg";
                for (Node node : level2Gameboard.getChildren()) {
                    if (node instanceof Pane && node.getId() != null) {
                        int id = Integer.parseInt(node.getId());
                        GameboardCells.put(id, (Pane) node);
                    }
                }
                break;
            default:
                imagePath = "/image/background.jpg";
                imagePathMySpaceship = "/image/cardboard/cardboard-1b.jpg";
        }
        if (level == 2) {
            level1Gameboard.setVisible(false);
            level1Gameboard.setManaged(false);
            level1Gameboard.setDisable(true);
            level2Gameboard.setVisible(true);
            level2Gameboard.setManaged(true);
            level2Gameboard.setDisable(false);

        } else {
            level1Gameboard.setVisible(true);
            level1Gameboard.setManaged(true);
            level1Gameboard.setDisable(false);
            level2Gameboard.setVisible(false);
            level2Gameboard.setManaged(false);
            level2Gameboard.setDisable(true);
        }

        backgroundImageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        popupBackgroundImage.setImage(new Image(getClass().getResourceAsStream(imagePathMySpaceship)));
        MySpaceshipImage.setImage(new Image(getClass().getResourceAsStream(imagePathMySpaceship)));

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


        List<String> playersNick = GUIController.getInstance().getController().getPlayers();
        List<String> otherPlayers = playersNick.stream()
                .filter(name -> !name.equals(GUIController.getInstance().getNickname()))
                .toList();

        setOtherPlayers(otherPlayers);
        backgroundImage.setEffect(new GaussianBlur(30));
        setMySpaceship();
        updateStats();
        myname.setText(GUIController.getInstance().getNickname());
        try {
            movePlayerToPosition(GUIController.getInstance().getController().getGameV().getGlobalBoard().getPositions().get(GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname())));
        } catch (RemoteException e) {
            showNotification("Fail to update initial positions", NotificationType.ERROR, 5000);
        }
    }

    public void updateStats() {
        try {
            creditsValue.setText(String.valueOf(GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname()).getCredits()));
            lostTilesValue.setText(String.valueOf(0));
            batteriesValue.setText(String.valueOf(GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname()).getNumBatteries()));
            RedValue.setText(String.valueOf(GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname()).getNumFinalRedBoxes()));
            GreenValue.setText(String.valueOf(GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname()).getNumFinalGreenBoxes()));
            BlueValue.setText(String.valueOf(GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname()).getNumFinalBlueBoxes()));
            YellowValue.setText(String.valueOf(GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname()).getNumFinalYellowBoxes()));
            humansValue.setText(String.valueOf(GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname()).calculateNumHumans()));
            brownAliensValue.setText(String.valueOf(GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname()).calculateNumBAliens()));
            purpleAliensValue.setText(String.valueOf(GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname()).calculateNumPAliens()));
        } catch (RemoteException e) {
            showNotification("Fail to update stats", NotificationType.ERROR, 5000);
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
                                StackPane tileNode = createTile(tile, true);

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
        for (PlayerV p : GUIController.getInstance().getController().getGameV().getPlayers()) {
            playerColors.put(p.getNickname(), p.getColor());
        }
    }

    @FXML
    private void closeSpacePopup() {
        viewSpaceshipPopup.setVisible(false);
    }

    private StackPane createTile(TileV tile, boolean isOtherPlayer) {
        // Container della tile
        StackPane tilePane = new StackPane();
        ImageView imageView = new ImageView();
        if (isOtherPlayer) {

            tilePane.setPrefSize(149, 149); // stessa misura usata altrove
            tilePane.setMinSize(149, 149);
            tilePane.setMaxSize(149, 149);
            imageView.setFitWidth(149);
            imageView.setFitHeight(149);
        } else {
            tilePane.setPrefSize(72, 72); // stessa misura usata altrove
            tilePane.setMinSize(72, 72);
            tilePane.setMaxSize(72, 72);
            imageView.setFitWidth(72);
            imageView.setFitHeight(72);
        }

        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(getClass().getResourceAsStream(tile.getImagePath())));
        switch (tile.getRotationType()) {
            case NORTH:
                imageView.setRotate(0);
                break;
            case SOUTH:
                imageView.setRotate(180);
                break;
            case EAST:
                imageView.setRotate(90);
                break;
            case WEST:
                imageView.setRotate(270);
                break;
        }

        tilePane.getChildren().add(imageView);
        return tilePane;
    }

    public void clickedOnTile(Coordinate coordinate) {
        Optional<TileV>[][] spaceshipBoard = null;
        try {
            spaceshipBoard = GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname()).getSpaceshipBoard();
        } catch (RemoteException e) {
            showNotification("Error loading your spaceship", NotificationType.ERROR, 5000);
        }
        if (GUIController.getInstance().getController().getGameV().getCurrentCard().getStateCard().equals(StateCardType.DECISION) && (
                GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.PIRATE) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.METEORITES_STORM) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE1) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE2))) {
            try {
                GUIController.getInstance().getController().keepBlock(GUIController.getInstance().getNickname(), coordinate);
            } catch (RemoteException e) {
                showNotification("Error with keep blocks", NotificationType.ERROR, 5000);
            }
        } else if (spaceshipBoard[coordinate.x()][coordinate.y()].isPresent() && (spaceshipBoard[coordinate.x()][coordinate.y()].get().getType().equals(TileType.STORAGE) ||
                spaceshipBoard[coordinate.x()][coordinate.y()].get().getType().equals(TileType.SPECIAL_STORAGE)) && (
                GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE2) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.PLANET) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.ABANDONED_STATION) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.TRAFFICKER))) {
            boxCoordinate = coordinate;
            showBoxManagementPopup(coordinate);
        } else if (spaceshipBoard[coordinate.x()][coordinate.y()].isPresent() && (spaceshipBoard[coordinate.x()][coordinate.y()].get().getType().equals(TileType.D_CANNON)) &&
                (GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.PIRATE) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.METEORITES_STORM) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE1) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE2) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.TRAFFICKER) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.SLAVE_OWNER))) {
            doubleCount++;
            doubleLabel.setText("Number of double cannon activated: " + doubleCount);
            doubleLabel.setVisible(true);
            doubles.add(coordinate);
        } else if (spaceshipBoard[coordinate.x()][coordinate.y()].isPresent() && (spaceshipBoard[coordinate.x()][coordinate.y()].get().getType().equals(TileType.BATTERY))) {
            if (GUIController.getInstance().getController().getGameV().getCurrentCard().getStateCard().equals(StateCardType.CHOICE_ATTRIBUTES) && (
                    GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.TRAFFICKER) ||
                            GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.OPENSPACE) ||
                            GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE1) ||
                            GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE2) ||
                            GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.SLAVE_OWNER) ||
                            GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.PIRATE))) {
                batteryCount++;
                batteryLabel.setText("Number of batteries used: " + batteryCount);
                batteryLabel.setVisible(true);
                batteries.add(coordinate);
            } else if (GUIController.getInstance().getController().getGameV().getCurrentCard().getStateCard().equals(StateCardType.REMOVE) &&
                    (GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE2) ||
                            GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.TRAFFICKER))) {
                try {
                    GUIController.getInstance().getController().removeBattery(GUIController.getInstance().getNickname(), coordinate);
                } catch (RemoteException e) {
                    showNotification("Error removing battery", NotificationType.ERROR, 5000);
                }
            } else if (GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.METEORITES_STORM)) {
                try {
                    GUIController.getInstance().getController().calculateDamage(GUIController.getInstance().getNickname(), coordinate);
                } catch (RemoteException e) {
                    showNotification("Error during calculate damage", NotificationType.ERROR, 5000);
                }
            }
        } else if (spaceshipBoard[coordinate.x()][coordinate.y()].isPresent() && (spaceshipBoard[coordinate.x()][coordinate.y()].get().getType().equals(TileType.D_MOTOR)) &&
                (GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.OPENSPACE) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE1) ||
                        GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE2))) {
            doubleCount++;
            doubleLabel.setText("Number of double motor activated: " + doubleCount);
            doubleLabel.setVisible(true);
            doubles.add(coordinate);
        } else if (spaceshipBoard[coordinate.x()][coordinate.y()].isPresent() && (spaceshipBoard[coordinate.x()][coordinate.y()].get().getType().equals(TileType.CABIN))
                && (GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.SLAVE_OWNER) ||
                GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE1) ||
                GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.ABANDONED_SHIP))) {
            try {
                GUIController.getInstance().getController().removeCrew(GUIController.getInstance().getNickname(), coordinate);
            } catch (RemoteException e) {
                showNotification("Error during remove crew", NotificationType.ERROR, 5000);
            }
        }
    }

    private void showBoxManagementPopup(Coordinate coordinate) {
        Optional<TileV>[][] spaceshipBoard;
        try {
            spaceshipBoard = GUIController.getInstance().getController()
                    .getPlayerVFromNickname(GUIController.getInstance().getNickname())
                    .getSpaceshipBoard();
        } catch (RemoteException e) {
            showNotification("Errore nel caricamento della nave", NotificationType.ERROR, 5000);
            return;
        }

        Optional<TileV> tileOpt = spaceshipBoard[coordinate.x()][coordinate.y()];

        TileV tile = tileOpt.get();

        // Sfondo scuro
        StackPane dimBackground = new StackPane();
        dimBackground.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        dimBackground.setPrefSize(root.getWidth(), root.getHeight());

        VBox overlay = new VBox(10);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(20));
        overlay.setStyle("-fx-background-color: yellow; -fx-background-radius: 15;");
        overlay.setMaxWidth(600);
        overlay.setMaxHeight(800);

        Label title = new Label("Box Management");
        title.getStyleClass().add("white-context-label");


        HBox redRow = createBoxRow(BoxType.RED, coordinate);
        HBox yellowRow = createBoxRow(BoxType.YELLOW, coordinate);
        HBox greenRow = createBoxRow(BoxType.GREEN, coordinate);
        HBox blueRow = createBoxRow(BoxType.BLUE, coordinate);

        Button finishButton = new Button("Finish");
        finishButton.getStyleClass().add("main-button"); // Assicurati che il tuo CSS definisca .main-button
        finishButton.setOnAction(ev -> {
            root.getChildren().remove(dimBackground);

            for (Map.Entry<Coordinate, BoxType> entry : myBoxes.entrySet()) {
                Coordinate coord = entry.getKey();
                BoxType type = entry.getValue();
                try {
                    GUIController.getInstance().getController().removeBox(GUIController.getInstance().getNickname(), coord, type);  // Assicurati che il metodo esista e sia accessibile
                } catch (RemoteException e) {
                    showNotification("Error removing boxes", NotificationType.ERROR, 5000);
                }
            }
            myBoxes.clear();
        });


        overlay.getChildren().addAll(title, redRow, yellowRow, greenRow, blueRow, finishButton);

        dimBackground.getChildren().add(overlay);
        StackPane.setAlignment(overlay, Pos.CENTER);

        root.getChildren().add(dimBackground);
    }

    private HBox createBoxRow(BoxType box, Coordinate coordinate) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);

        Label label = new Label(String.valueOf(box.name() + " boxes: "));
        label.getStyleClass().add("info-value");

        Button plus = new Button("+");
        Button minus = new Button("-");

        plus.getStyleClass().add("main-button");
        minus.getStyleClass().add("main-button");

        plus.setOnAction(ev -> {
            if (myBoxes.isEmpty()) {//se aggiungo un box e non ho tolto nessun box da nessuna parte, vuol dire che sto prendendo quelli della carta
                try {
                    GUIController.getInstance().getController().moveBox(
                            GUIController.getInstance().getNickname(), new Coordinate(-1, -1),
                            coordinate, box, true);
                } catch (RemoteException e) {
                    showNotification("Error with move box", NotificationType.ERROR, 3000);
                }
            } else {//altrimenti sto prendendo quelli dalla mappa che ho creato prima
                try {
                    GUIController.getInstance().getController().moveBox(
                            GUIController.getInstance().getNickname(), getFirstCoordinateOfType(box),
                            coordinate, box, true);
                    myBoxes.remove(coordinate);
                } catch (RemoteException e) {
                    showNotification("Error with move box", NotificationType.ERROR, 3000);
                }
            }
        });

        minus.setOnAction(ev -> {//se rimuovo box, allora li aggiungo alla mappa myboxes nel caso l'utente volesse poi riaggiungerli
            if (GUIController.getInstance().getController().getGameV().getCurrentCard().getStateCard().equals(StateCardType.BOXMANAGEMENT)) {
                try {
                    GUIController.getInstance().getController().moveBox(
                            GUIController.getInstance().getNickname(), coordinate, new Coordinate(-1, -1), box, true);
                } catch (RemoteException e) {
                    showNotification("Error with move box", NotificationType.ERROR, 3000);
                }
                myBoxes.put(coordinate, box);
            } else if (GUIController.getInstance().getController().getGameV().getCurrentCard().getStateCard().equals(StateCardType.REMOVE)) {
                try {
                    GUIController.getInstance().getController().removeBox(GUIController.getInstance().getNickname(), coordinate, box);
                } catch (RemoteException e) {
                    showNotification("Error with remove box", NotificationType.ERROR, 3000);
                }
            }
        });

        row.getChildren().addAll(label, plus, minus);
        return row;
    }

    public Coordinate getFirstCoordinateOfType(BoxType type) {
        for (Map.Entry<Coordinate, BoxType> entry : myBoxes.entrySet()) {
            if (entry.getValue() == type) {
                return entry.getKey();
            }
        }
        return null; // Nessuna coordinata trovata per quel tipo
    }


    public void setMySpaceship() {
        for (Node node : MySpaceship.getChildren()) {
            if (node instanceof Pane pane && pane.getId() != null && pane.getId().startsWith("cell_")) {
                pane.getChildren().clear();

                pane.setOnMouseClicked(event -> {
                    String paneId = pane.getId();
                    String[] parts = paneId.split("_");
                    int row = Integer.parseInt(parts[1]);
                    int col = Integer.parseInt(parts[2]);

                    clickedOnTile(new Coordinate(row, col)); // chiamata al tuo metodo
                });
            }
        }
        try {
            PlayerV myPlayer = GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname());
            Optional<TileV>[][] spaceship = myPlayer.getSpaceshipBoard();

            for (int row = 0; row < spaceship.length; row++) {
                for (int col = 0; col < spaceship[row].length; col++) {
                    Optional<TileV> tileOpt = spaceship[row][col];
                    if (tileOpt.isPresent()) {
                        TileV tile = tileOpt.get();
                        StackPane tileNode = createTile(tile, false);

                        for (Node node : MySpaceship.getChildren()) {
                            if (node instanceof Pane && node.getId() != null && node.getId().equals("cell_" + row + "_" + col)) {
                                ((Pane) node).getChildren().add(tileNode);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (RemoteException e) {
            showNotification("Error loading your spaceship", NotificationType.ERROR, 5000);
        }
    }

    public void newCard(CardV newCard) {
        calculatedamage.setVisible(false);
        calculatedamage.setDisable(true);
        choiceboxtrue.setVisible(false);
        choiceboxtrue.setDisable(true);
        choiceboxfalse.setVisible(false);
        choiceboxfalse.setDisable(true);
        finishcannon.setVisible(false);
        finishcannon.setDisable(true);
        finishmotor.setVisible(false);
        finishmotor.setDisable(true);
        finishmoveboxes.setVisible(false);
        finishmoveboxes.setDisable(true);
        rollDice.setVisible(false);
        rollDice.setDisable(true);
        diceResult.setVisible(false);
        choiceNo.setVisible(false);
        choiceNo.setDisable(true);
        choiceYes.setVisible(false);
        choiceYes.setDisable(true);
        noChoicePlanet.setVisible(false);
        noChoicePlanet.setDisable(true);
        batteryLabel.setVisible(false);
        doubleLabel.setVisible(false);
        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(500);
        imageView.setLayoutX(10);
        imageView.setLayoutY(10);
        imageView.setPreserveRatio(true);

        imageView.setImage(new Image(getClass().getResourceAsStream(newCard.getImagePath())));
        cardPane.getChildren().clear();
        cardPane.getChildren().add(imageView);
        if (newCard.getCardType().equals(CardType.ABANDONED_STATION)) {
            choiceboxtrue.setVisible(true);
            choiceboxtrue.setDisable(false);
            choiceboxfalse.setVisible(true);
            choiceboxfalse.setDisable(false);
            loadComments("Abandoned station: if you have enough alive on board, you can choose whether to take the boxes or not. If you choose to do so, click on the storage and add the boxes you want");
        } else if (newCard.getCardType().equals(CardType.ABANDONED_SHIP)) {
            choiceYes.setVisible(true);
            choiceYes.setDisable(false);
            choiceNo.setVisible(true);
            choiceNo.setDisable(false);
            loadComments("Abandoned ship: you can choose whether or not to sacrifice alive to gain credits");
        } else if (newCard.getCardType().equals(CardType.PIRATE)) {
            finishcannon.setVisible(true);
            finishcannon.setDisable(false);
            calculatedamage.setVisible(true);
            calculatedamage.setDisable(false);
            if(isLeader()){
                rollDice.setVisible(true);
                rollDice.setDisable(false);
            }
            loadComments("Pirate: you can choose to activate cannons using batteries by clicking on them. If you win, you receive credits. If it's a draw, the effect passes to the next player. If you lose, you must roll the dice to find out where you'll be hit and activate Calculate Damage by clicking on a battery or directly on Calculate Damage");
        } else if (newCard.getCardType().equals(CardType.SLAVE_OWNER)) {
            finishcannon.setVisible(true);
            finishcannon.setDisable(false);
            loadComments("Slave Owner: you can choose to activate cannons using batteries by clicking on them. If you win you receive credits. If it's a draw, the effect passes to the next player. If you lose, you must click on the cabins to eliminate the alive crew members..");
        } else if (newCard.getCardType().equals(CardType.WARZONE2)) {
            finishcannon.setVisible(true);
            finishcannon.setDisable(false);
            loadComments("War Zone 2: Phase 1, you can choose to activate cannons using batteries; Phase 2: you can choose to activate motors using batteries; the player with fewer motors must choose where to remove boxes by clicking on storages. Phase 3: the player with fewer alive must roll the dice to find out where they’ll be hit. By clicking on a battery, you activate either a shield or a cannon, otherwise you can click on Calculate Damage. If the ship breaks apart, you’ll need to choose which part to keep by clicking on a tile from that section.");
        } else if (newCard.getCardType().equals(CardType.TRAFFICKER)) {
            finishcannon.setVisible(true);
            finishcannon.setDisable(false);
            loadComments("Trafficker: you can choose to activate cannons using batteries by clicking on them. If you win, you can add boxes by clicking on the storage. If it's a draw, it's the next player's turn. If you lose, you must leave behind 2 boxes by reducing their quantity. If you've run out of boxes, you lose batteries instead by clicking on the battery storage.");
        } else if (newCard.getCardType().equals(CardType.OPENSPACE)) {
            finishmotor.setVisible(true);
            finishmotor.setDisable(false);
            loadComments("Open space: you can choose to activate motors using batteries by clicking on them.");
        } else if (newCard.getCardType().equals(CardType.WARZONE1)) {
            try {
                GUIController.getInstance().getController().choiceCrew(GUIController.getInstance().getNickname());
            } catch (RemoteException e) {
                showNotification("Error in choice crew", NotificationType.ERROR, 5000);
            }
            loadComments("War Zone 1: Phase 1, the player with fewer humans automatically loses flight days. Phase 2, you can choose to activate motors using batteries; the player with fewer motors must choose where to remove alive crew members by clicking on the cabins. Phase 3, you can choose to activate cannons using batteries; the player with fewer cannons must roll the dice to find out where they’ll be hit. By clicking on a battery, you activate either a shield or a cannon, otherwise you can click on Calculate Damage. If the ship breaks apart, you’ll need to choose which part to keep by clicking on a tile from that section.");
        } else if (newCard.getCardType().equals(CardType.METEORITES_STORM)) {
            if(isLeader()){
                rollDice.setVisible(false);
                rollDice.setDisable(true);
            }
            calculatedamage.setVisible(true);
            calculatedamage.setDisable(false);
            loadComments("Meterites Storm: the leader will roll the dice, and you can calculate damage by clicking on the battery if you want to use it, or by clicking on Calculate Damage, for all the meteorites shown on the card. If your ship breaks apart, you'll be able to choose which part to keep by clicking on a tile from that section.");
        } else if (newCard.getCardType().equals(CardType.PLANET)) {
            finishmoveboxes.setVisible(true);
            finishmoveboxes.setDisable(false);
            noChoicePlanet.setVisible(true);
            noChoicePlanet.setDisable(false);
            for (int i = 0; i < 4; i++) {
                Pane planetPane = new Pane();
                planetPane.setPrefSize(200, 50);
                planetPane.setLayoutX(34);
                planetPane.setLayoutY(69 + (i * 90)); // Spaziatura tra i pane
                planetPane.getStyleClass().add("planet-pane");

                final int index = i;
                planetPane.setOnMouseClicked(event -> {
                    try {
                        GUIController.getInstance().getController().choicePlanet(GUIController.getInstance().getNickname(), index);
                    } catch (RemoteException e) {
                        showNotification("Error in choice planet", NotificationType.ERROR, 5000);
                    }
                });

                cardPane.getChildren().add(planetPane);
            }
            loadComments("Planets: you can choose which planet to board by clicking on the card, then click on the storage to load the boxes.");
        }
    }

    @FXML
    public void onNextCard() {
        try {
            GUIController.getInstance().getController().playNextCard(GUIController.getInstance().getNickname());
        } catch (RemoteException e) {
            showNotification("Error taking next card", NotificationType.ERROR, 5000);
        }
    }

    public void movePlayerToPosition(int cellId) {
        int numStep = GUIController.getInstance().getController().getGameV().getGlobalBoard().getNumstep();

        Map<PlayerColor, ImageView> pieceMap = Map.of(
                PlayerColor.BLUE, playerBluePiece,
                PlayerColor.GREEN, playerGreenPiece,
                PlayerColor.RED, playerRedPiece,
                PlayerColor.YELLOW, playerYellowPiece
        );
        PlayerColor mycolor = null;
        try {
            mycolor = GUIController.getInstance().getController().getPlayerVFromNickname(GUIController.getInstance().getNickname()).getColor();
        } catch (RemoteException e) {
            showNotification("Error taking player color", NotificationType.ERROR, 5000);
        }
        ImageView myPiece = pieceMap.get(mycolor);
        int targetIndex = ((cellId + 1) % numStep + numStep) % numStep;
        Pane targetPane = GameboardCells.get(targetIndex);

        if (targetPane != null && myPiece != null) {
            Bounds bounds = targetPane.getBoundsInParent();
            double centerX = bounds.getMinX() + (targetPane.getPrefWidth() - myPiece.getFitWidth()) / 2;
            double centerY = bounds.getMinY() + (targetPane.getPrefHeight() - myPiece.getFitHeight()) / 2;

            myPiece.setVisible(true);
            myPiece.setLayoutX(centerX);
            myPiece.setLayoutY(centerY);
        }

        for (PlayerV player : GUIController.getInstance().getController().getGameV().getPlayers()) {
            if (!player.getNickname().equals(GUIController.getInstance().getNickname())) {
                PlayerColor color = player.getColor();
                ImageView piece = pieceMap.get(color);
                pieceMap.get(color).setVisible(true);

                Integer cellIdOther = GUIController.getInstance()
                        .getController()
                        .getGameV()
                        .getGlobalBoard()
                        .getPositions()
                        .get(player);

                int targetIndexOther = ((cellIdOther + 1) % numStep + numStep) % numStep;
                Pane targetPaneOther = GameboardCells.get(targetIndexOther);

                if (targetPaneOther != null && piece != null) {
                    Bounds bounds = targetPaneOther.getBoundsInParent();
                    double centerX = bounds.getMinX() + (targetPaneOther.getPrefWidth() - piece.getFitWidth()) / 2;
                    double centerY = bounds.getMinY() + (targetPaneOther.getPrefHeight() - piece.getFitHeight()) / 2;

                    piece.setLayoutX(centerX);
                    piece.setLayoutY(centerY);
                }
            }
        }
    }

    @FXML
    public void onChoiceBoxFalse() {
        try {
            GUIController.getInstance().getController().choiceBox(GUIController.getInstance().getNickname(), false);
        } catch (RemoteException e) {
            showNotification("Error during choice box", NotificationType.ERROR, 5000);
        }
    }

    @FXML
    public void onChoiceBoxTrue() {
        try {
            GUIController.getInstance().getController().choiceBox(GUIController.getInstance().getNickname(), true);
        } catch (RemoteException e) {
            showNotification("Error during choice box", NotificationType.ERROR, 5000);
        }

    }

    @FXML
    public void onfinishMoveBoxes() {
        try {
            GUIController.getInstance().getController().moveBox(GUIController.getInstance().getNickname(), new Coordinate(-1, -1), boxCoordinate, BoxType.GREEN, false);
        } catch (RemoteException e) {
            showNotification("Error during choice box", NotificationType.ERROR, 5000);
        }
    }

    @FXML
    public void onFinishChoiceCannon() {//prendo la lista di batterie e la lista di cannoni e le mando al server
        try {
            GUIController.getInstance().getController().choiceDoubleCannon(GUIController.getInstance().getNickname(), doubles, batteries);
        } catch (RemoteException e) {
            showNotification("Error during choice cannon", NotificationType.ERROR, 5000);
        }
    }

    @FXML
    public void onFinishChoiceMotor() {
        try {
            GUIController.getInstance().getController().choiceDoubleMotor(GUIController.getInstance().getNickname(), doubles, batteries);
        } catch (RemoteException e) {
            showNotification("Error during choice motor", NotificationType.ERROR, 5000);
        }

    }

    @FXML
    public void onRollDice() {
        diceResult.setVisible(false);
        diceAnimation.setVisible(true);
        Image diceGif = new Image(getClass().getResource("/image/dices.gif").toExternalForm());
        diceAnimation.setImage(diceGif);
        try {
            GUIController.getInstance().getController().rollDice(GUIController.getInstance().getNickname());
        } catch (RemoteException e) {
            showNotification("Error rolling dice", NotificationType.ERROR, 5000);
        }
        new Thread(() -> {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                diceAnimation.setVisible(false);


                diceResult.setVisible(true);
                int result = GUIController.getInstance().getController().getGameV().getDiceV().getResult();
                diceResult.setText("Result: " + result);

            });
        }).start();
    }

    public void updateDice(int result) {
        diceResult.setText("Result: " + result);
    }

    @FXML
    public void onCalculateDamage() {
        try {
            GUIController.getInstance().getController().calculateDamage(GUIController.getInstance().getNickname(), new Coordinate(-1, -1));
        } catch (RemoteException e) {
            showNotification("Error with calculate damage", NotificationType.ERROR, 5000);
        }
    }

    public void onRemoveTile(Coordinate coordinate) {
        Platform.runLater(() -> {
            for (Node node : MySpaceship.getChildren()) {
                if (node instanceof Pane pane && pane.getId() != null && pane.getId().startsWith("cell_")) {
                    Coordinate paneCoord = getCoordinatesFromId(pane);
                    if (paneCoord != null && paneCoord.equals(coordinate)) {
                        pane.getChildren().clear();
                        break;
                    }
                }
            }
        });
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

    @FXML
    public void onNoChoicePlanet() {
        try {
            GUIController.getInstance().getController().choicePlanet(GUIController.getInstance().getNickname(), -1);
        } catch (RemoteException e) {
            showNotification("Error with choice planet", NotificationType.ERROR, 5000);
        }
    }

    @FXML
    public void onChoiceYes() {
        try {
            GUIController.getInstance().getController().choice(GUIController.getInstance().getNickname(), true);
        } catch (RemoteException e) {
            showNotification("Error with choice", NotificationType.ERROR, 5000);
        }
    }

    @FXML
    public void onChoiceNo() {
        try {
            GUIController.getInstance().getController().choice(GUIController.getInstance().getNickname(), false);
        } catch (RemoteException e) {
            showNotification("Error with choice", NotificationType.ERROR, 5000);
        }
    }


    public void updateCurrentPlayerName() {
        currentPlayerNameLabel.setText(GUIController.getInstance().getController().getGameV().getCurrentState().getCurrentPlayer().getNickname());
    }

    public void hideChoiceBox(boolean visible) {
        choiceboxtrue.setVisible(false);
        choiceboxtrue.setDisable(true);
        choiceboxfalse.setVisible(false);
        choiceboxfalse.setDisable(true);
        if (visible) { //if chioce box true
            finishmoveboxes.setVisible(true);
            finishmoveboxes.setDisable(false);
        }
    }

    public void hideChoice(boolean choice) {
        choiceNo.setVisible(false);
        choiceNo.setDisable(true);
        choiceYes.setVisible(false);
        choiceYes.setDisable(true);
    }

    public void hideFinishCannon(int number) {
        finishcannon.setVisible(false);
        finishcannon.setDisable(true);
        batteries = new ArrayList<>();
        doubles = new ArrayList<>();
        doubleCount = 0;
        batteryCount = 0;
        doubleLabel.setVisible(false);
        batteryLabel.setVisible(false);
        if (GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE1)) {
            if (isLeader()) {
                rollDice.setVisible(true);
                rollDice.setDisable(false);
            }
            calculatedamage.setVisible(true);
            calculatedamage.setDisable(false);
        } else if (GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE2)) {
            finishmotor.setVisible(true);
            finishmotor.setDisable(false);
        }
    }

    public void hideFinishMotor(int number) {
        finishmotor.setVisible(false);
        finishmotor.setDisable(true);
        batteries = new ArrayList<>();
        doubles = new ArrayList<>();
        doubleCount = 0;
        batteryCount = 0;
        doubleLabel.setVisible(false);
        batteryLabel.setVisible(false);
        if (GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE2)) {
            try {
                GUIController.getInstance().getController().choiceCrew(GUIController.getInstance().getNickname());
            } catch (RemoteException e) {
                showNotification("Error with choice crew", NotificationType.ERROR, 5000);
            }
            if( isLeader()) {
                rollDice.setVisible(true);
                rollDice.setDisable(false);
            }
            calculatedamage.setVisible(true);
            calculatedamage.setDisable(false);
        } else if (GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE1)) {
            finishcannon.setVisible(true);
            finishcannon.setDisable(false);
        }

    }

    public void hideMoveBox() {
        finishmoveboxes.setVisible(false);
        finishmoveboxes.setDisable(true);
    }

    public void hideChoicePlanet() {
        noChoicePlanet.setVisible(false);
        noChoicePlanet.setDisable(true);
    }

    public void hideCalculateDamage() {
        //calculatedamage.setVisible(false);
        //calculatedamage.setDisable(true);
    }

    public void afterChoiceCrew() {
        if (GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.WARZONE1)) {
            finishmotor.setVisible(true);
            finishmotor.setDisable(false);
            finishcannon.setVisible(true);
            finishcannon.setDisable(false);
            if(isLeader()){
                rollDice.setVisible(true);
                rollDice.setDisable(false);
            }
            diceResult.setVisible(true);
            calculatedamage.setVisible(true);
            calculatedamage.setDisable(false);
        }
    }

    public void showdiceresult() {
        diceResult.setVisible(true);
        int result = GUIController.getInstance().getController().getGameV().getDiceV().getResult();
        diceResult.setText("Result: " + result);

    }


    public void loadComments(String comments) {
        commentBox.getChildren().clear();

        if (comments == null || comments.isBlank()) {
            return;
        }

        String[] commentList = comments.split("\\n");

        for (String comment : commentList) {
            Label commentLabel = new Label(comment);
            commentLabel.setWrapText(true);
            commentLabel.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-background-radius: 5;");

            HBox container = new HBox(commentLabel);
            container.setStyle("-fx-padding: 5;");
            commentBox.getChildren().add(container);
        }
    }

    public boolean isLeader() {
        Map<PlayerV, Integer> positions = GUIController.getInstance()
                .getController()
                .getGameV()
                .getGlobalBoard()
                .getPositions();

        Optional<Map.Entry<PlayerV, Integer>> maxEntry = positions.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        boolean isMe = false;
        if (maxEntry.isPresent()) {
            String topPlayerNickname = maxEntry.get().getKey().getNickname();
            isMe = topPlayerNickname.equals(GUIController.getInstance().getNickname());
        }
        return isMe;
    }
}

