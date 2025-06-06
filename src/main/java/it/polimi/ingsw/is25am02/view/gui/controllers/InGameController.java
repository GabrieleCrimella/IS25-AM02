package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
    @FXML private Label aliveValue;
    @FXML private Label batteriesValue;
    @FXML private Label boxesValue;
    @FXML private ImageView MySpaceshipImage;
    @FXML private ImageView backgroundImage;
    @FXML private Pane MySpaceship;


    @FXML
    public void initialize(int level, PlayerColor color) {
        String imagePath;
        String imagePathMySpaceship;
        root.getStylesheets().add(
                getClass().getResource("/style/style.css").toExternalForm()
        );
        switch (level) {
            case 0:
                imagePath = "/image/cardboard/cardboard-3.png";
                imagePathMySpaceship = "/image/cardboard/cardboard-1.jpg";
                break;
            case 2:
                imagePath = "/image/cardboard/cardboard-5.png";
                imagePathMySpaceship = "/image/cardboard/cardboard-1b.jpg";
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
    }

    public void updateStats(int credits, int lostTiles, int alive, int batteries, int boxes) {
        creditsValue.setText(String.valueOf(credits));
        lostTilesValue.setText(String.valueOf(lostTiles));
        aliveValue.setText(String.valueOf(alive));
        batteriesValue.setText(String.valueOf(batteries));
        boxesValue.setText(String.valueOf(boxes));
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
            tilePane.setPrefSize(88, 88); // stessa misura usata altrove
            tilePane.setMinSize(88, 88);
            tilePane.setMaxSize(88, 88);
            imageView.setFitWidth(88);
            imageView.setFitHeight(88);
        }

        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(getClass().getResourceAsStream(tile.getImagePath())));

        tilePane.getChildren().add(imageView);
        return tilePane;
    }

    public void setMySpaceship(){
        for (Node node : MySpaceship.getChildren()) {
            if (node instanceof Pane pane && pane.getId() != null && pane.getId().startsWith("cell_")) {
                pane.getChildren().clear();
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
            showNotification("Errore durante il caricamento della tua navicella", NotificationType.ERROR, 5000);
        }


    }



}
