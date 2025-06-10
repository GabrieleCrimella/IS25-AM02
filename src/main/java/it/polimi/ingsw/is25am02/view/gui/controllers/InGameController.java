package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;
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

public class InGameController extends GeneralController{
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
    @FXML private Label creditsValue;
    @FXML private Label lostTilesValue;
    @FXML private Label batteriesValue;
    @FXML private Label RedValue;
    @FXML private Label GreenValue;
    @FXML private Label BlueValue;
    @FXML private Label YellowValue;
    @FXML private Label humansValue;
    @FXML private Label brownAliensValue;
    @FXML private Label purpleAliensValue;
    @FXML private ImageView MySpaceshipImage;
    @FXML private ImageView backgroundImage;
    @FXML private Pane MySpaceship;
    @FXML private Pane cardPane;
    @FXML private ImageView playerBluePiece;
    @FXML private ImageView playerGreenPiece;
    @FXML private ImageView playerRedPiece;
    @FXML private ImageView playerYellowPiece;


    private Map<Integer, Pane> GameboardCells = new HashMap<>();
    private Map<String, PlayerColor> playerColors = new HashMap<>();
    private Map<Coordinate,BoxType> myBoxes = new HashMap<>();
    private Coordinate boxCoordinate;

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
                                StackPane tileNode = createTile(tile,true);

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
        for(PlayerV p: GUIController.getInstance().getController().getGameV().getPlayers()){
            playerColors.put(p.getNickname(), p.getColor());
        }
    }

    @FXML
    private void closeSpacePopup() {
        viewSpaceshipPopup.setVisible(false);
    }

    private StackPane createTile(TileV tile,boolean isOtherPlayer) {
        // Container della tile
        StackPane tilePane = new StackPane();
        ImageView imageView = new ImageView();
        if(isOtherPlayer){

        tilePane.setPrefSize(149, 149); // stessa misura usata altrove
        tilePane.setMinSize(149, 149);
        tilePane.setMaxSize(149, 149);
            imageView.setFitWidth(149);
            imageView.setFitHeight(149);
        }
        else{
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
        if(spaceshipBoard[coordinate.x()][coordinate.y()].isPresent() && (spaceshipBoard[coordinate.x()][coordinate.y()].get().getType().equals(TileType.STORAGE)||
                spaceshipBoard[coordinate.x()][coordinate.y()].get().getType().equals(TileType.SPECIAL_STORAGE))) {
            boxCoordinate = coordinate;
            showBoxManagementPopup(coordinate);


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


        HBox redRow = createBoxRow(BoxType.RED, tile.getNumRedBox(), coordinate);
        HBox yellowRow = createBoxRow(BoxType.YELLOW, tile.getNumYellowBox(), coordinate);
        HBox greenRow = createBoxRow(BoxType.GREEN, tile.getNumGreenBox(), coordinate);
        HBox blueRow = createBoxRow(BoxType.BLUE, tile.getNumBlueBox(), coordinate);

        Button finishButton = new Button("Finish");
        finishButton.getStyleClass().add("main-button"); // Assicurati che il tuo CSS definisca .main-button
        finishButton.setOnAction(ev -> {
            root.getChildren().remove(dimBackground);

            for (Map.Entry<Coordinate, BoxType> entry : myBoxes.entrySet()) {
                Coordinate coord = entry.getKey();
                BoxType type = entry.getValue();
                try {
                    GUIController.getInstance().getController().removeBox(GUIController.getInstance().getNickname(),coord, type);  // Assicurati che il metodo esista e sia accessibile
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

    private HBox createBoxRow(BoxType box, int initialValue, Coordinate coordinate) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);

        Label label = new Label(String.valueOf(box.name() + " boxes: " + initialValue));
        label.getStyleClass().add("info-value");

        Button plus = new Button("+");
        Button minus = new Button("-");

        plus.getStyleClass().add("main-button");
        minus.getStyleClass().add("main-button");

        plus.setOnAction(ev -> {
            if(myBoxes.isEmpty()){//se aggiungo un box e non ho tolto nessun box da nessuna parte, vuol dire che sto prendendo quelli della carta
                try {
                    GUIController.getInstance().getController().moveBox(
                            GUIController.getInstance().getNickname(), new Coordinate(-1,-1),
                            coordinate, box, true);
                } catch (RemoteException e) {
                    showNotification("Error with move box", NotificationType.ERROR, 3000);
                }
            } else{//altrimenti sto prendendo quelli dalla mappa che ho creato prima
                try {
                    GUIController.getInstance().getController().moveBox(
                            GUIController.getInstance().getNickname(), getFirstCoordinateOfType(box),
                            coordinate, box, true);
                } catch (RemoteException e) {
                    showNotification("Error with move box", NotificationType.ERROR, 3000);
                }

            }
        });

        minus.setOnAction(ev -> {//se rimuovo box, allora li aggiungo alla mappa myboxes nel caso l'utente volesse poi riaggiungerli
            myBoxes.put(coordinate, box);
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



    public void setMySpaceship(){
        for (Node node : MySpaceship.getChildren()) {
            if (node instanceof Pane pane && pane.getId() != null && pane.getId().startsWith("cell_")) {
                pane.getChildren().clear();

                pane.setOnMouseClicked(event -> {
                    String paneId = pane.getId();
                    String[] parts = paneId.split("_");
                    int row = Integer.parseInt(parts[1]);
                    int col = Integer.parseInt(parts[2]);

                    clickedOnTile(new Coordinate(row,col)); // chiamata al tuo metodo
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
                        StackPane tileNode = createTile(tile,false);

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
        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(500);
        imageView.setLayoutX(10);
        imageView.setLayoutY(10);
        imageView.setPreserveRatio(true);

        imageView.setImage(new Image(getClass().getResourceAsStream(newCard.getImagePath())));
        cardPane.getChildren().clear();
        cardPane.getChildren().add(imageView);
    }

    @FXML
    public void onNextCard(){
        try {
            GUIController.getInstance().getController().playNextCard(GUIController.getInstance().getNickname());
        } catch (RemoteException e) {
            showNotification("Error taking next card", NotificationType.ERROR, 5000);
        }
    }

    public void movePlayerToPosition(int cellId){

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
        Pane targetPane = GameboardCells.get(cellId+1);

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

                Pane targetPaneOther = GameboardCells.get(cellIdOther+1);

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
    public void onChoiceBox(){
        try {
            GUIController.getInstance().getController().choiceBox(GUIController.getInstance().getNickname(),true);
        } catch (RemoteException e) {
            showNotification("Error during choice box", NotificationType.ERROR, 5000);
        }

    }

    @FXML
    public void onNextPlayer(){
        if(GUIController.getInstance().getController().getGameV().getCurrentCard().getCardType().equals(CardType.ABANDONED_STATION)){
            try {
                GUIController.getInstance().getController().moveBox(GUIController.getInstance().getNickname(), new Coordinate(-1,-1), boxCoordinate, BoxType.GREEN,false);
            } catch (RemoteException e) {
                showNotification("Error during choice box", NotificationType.ERROR, 5000);
            }
        }
    }
}
