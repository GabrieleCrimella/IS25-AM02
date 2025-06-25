package it.polimi.ingsw.is25am02.view.gui.controllers;


import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LobbyController extends GeneralController {
    @FXML
    public Button createButton;

    @FXML
    public StackPane root;

    private PlayerV p;

    @FXML
    private ListView<LobbyView> lobbyList;

    @FXML
    private ImageView backgroundImage;


    @FXML
    private Button joinButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Label loadingLabel;

    @FXML
    private VBox createForm;

    @FXML
    private Spinner<Integer> maxPlayersSpinner;

    @FXML
    private ChoiceBox<Integer> levelChoiceBox;

    @FXML
    private ChoiceBox<String> colorChoiceBox;

    private StackPane dimBackground;


    @FXML
    private Label errorLabel;

    public void initialize() {

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
        backgroundImage.setEffect(new GaussianBlur(30));


        try {
            GUIController.getInstance().getController().getLobbies(GUIController.getInstance().getController().getVirtualView());
        } catch (RemoteException e) {
            errorLabel.setText("get lobbies non funziona");
        }

        /*
        lobbyList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(LobbyView lobby, boolean empty) {
                super.updateItem(lobby, empty);
                if (empty || lobby == null) {
                    setText(null);
                } else {
                    String lobbyDescription = String.format(
                            "Lobby ID: %d | Giocatori: %d/%d | Livello: %d",
                            lobby.getId(),
                            lobby.getNicknames().size(),
                            lobby.getMaxPlayers(),
                            lobby.getLevel()
                    );
                    setText(lobbyDescription);
                }
            }
        });

         */



    }

    private void hideButtonShowLoading() {
        joinButton.setVisible(false);
        refreshButton.setVisible(false);
        createForm.setVisible(false);
        createButton.setVisible(false);
        lobbyList.setVisible(false);
        loadingLabel.setVisible(true);
        loadingLabel.setText("Loading...");
    }

    public void setLobbies(List<LobbyView> lobbies) {
        ObservableList<LobbyView> observableList = FXCollections.observableArrayList(lobbies);
        lobbyList.setItems(observableList);
        lobbyList.setVisible(true);

        lobbyList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(LobbyView lobby, boolean empty) {
                super.updateItem(lobby, empty);
                if (empty || lobby == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // Descrizione lobby
                    String lobbyDescription = String.format(
                            "Lobby ID: %d\nOwner: %s\nPlayers: %d/%d\nLevel: %d",
                            lobby.getId(),
                            lobby.getNicknames().getFirst(),
                            lobby.getNicknames().size(),
                            lobby.getMaxPlayers(),
                            lobby.getLevel()
                    );

                    Label lobbyLabel = new Label(lobbyDescription);
                    lobbyLabel.setStyle("""
                                    -fx-font-size: 16px;
                                    -fx-font-weight: normal;
                                    -fx-line-spacing: 4px;
                                    -fx-text-fill: #333;
                            """);
                    lobbyLabel.setWrapText(true);

                    Button joinButton = new Button("Join");
                    joinButton.setStyle("""
                                    -fx-background-color: linear-gradient(to bottom, #9be79b, #5ccc5c);
                                    -fx-text-fill: black;
                                    -fx-font-size: 14px;
                                    -fx-font-weight: bold;
                                    -fx-background-radius: 6;
                                    -fx-padding: 6 12;
                                    -fx-cursor: hand;
                            """);
                    joinButton.setOnAction(e -> showJoinDialog(lobby));

                    VBox descriptionBox = new VBox(lobbyLabel);
                    descriptionBox.setAlignment(Pos.CENTER_LEFT);
                    descriptionBox.setSpacing(5);

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    HBox cellContent = new HBox(20, descriptionBox, spacer, joinButton);
                    cellContent.setAlignment(Pos.CENTER_LEFT);

                    cellContent.setPadding(new Insets(10));
                    cellContent.setStyle("-fx-border-color: #ccc; -fx-border-width: 0 0 1px 0; -fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 8;");

                    setGraphic(cellContent);
                }
            }
        });
    }

    private void showJoinDialog(LobbyView lobby) {
        dimBackground = new StackPane();
        dimBackground.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        dimBackground.setPrefSize(root.getWidth(), root.getHeight());

        VBox overlay = new VBox(10);
        overlay.setPadding(new Insets(20));
        overlay.setAlignment(Pos.CENTER);
        overlay.getStyleClass().add("dialog-container");
        overlay.setMaxWidth(350);
        overlay.setMaxHeight(250);

        Label instruction = new Label("Scegli un colore per la lobby " + lobby.getId() + ":");
        instruction.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");

        ChoiceBox<PlayerColor> colorChoiceBox = new ChoiceBox<>();
        colorChoiceBox.setItems(FXCollections.observableArrayList(PlayerColor.values()));

        Button confirmButton = new Button("Conferma Join");
        confirmButton.setStyle("-fx-background-color: #1A237E; -fx-text-fill: white; -fx-font-weight: bold;");
        confirmButton.setOnAction(ev -> {
            PlayerColor selectedColor = colorChoiceBox.getValue();
            if (selectedColor == null) {
                errorLabel.setText("Seleziona un colore.");
                return;
            }

            try {
                GUIController.getInstance().getController().joinLobby(GUIController.getInstance().getController().getVirtualView(), lobby.getId(), GUIController.getInstance().getNickname(), selectedColor);
            } catch (RemoteException ex) {
                errorLabel.setText("Errore durante il join: " + ex.getMessage());
            }
        });

        Button cancelButton = new Button("Annulla");
        cancelButton.setOnAction(ev -> root.getChildren().remove(dimBackground));

        overlay.getChildren().addAll(instruction, colorChoiceBox, confirmButton, cancelButton);

        dimBackground.getChildren().add(overlay);
        StackPane.setAlignment(overlay, Pos.CENTER);

        root.getChildren().add(dimBackground);
    }


    public void setLobbyListFromMap(Map<Integer, LobbyView> lobbyMap) {
        Platform.runLater(() -> {
            setLobbies(new ArrayList<>(lobbyMap.values()));
            errorLabel.setText("");
        });
    }

    @FXML
    private void onJoinLobby(MouseEvent event) {
        LobbyView selectedLobby = lobbyList.getSelectionModel().getSelectedItem();
        if (!lobbyList.isVisible()) {
            lobbyList.setVisible(true);
            refreshLobbyList();          // Popola la lista appena la mostri
            errorLabel.setText("Seleziona una lobby dalla lista.");
            return;
        }
        if (selectedLobby == null) {
            refreshLobbyList();
            errorLabel.setText("Seleziona una lobby!");
        } else {
            PlayerColor color = PlayerColor.valueOf(colorChoiceBox.getValue());
            if (color == null) {
                errorLabel.setText("Scegli un colore.");
                return;
            }

            try {
                int lobbyId = selectedLobby.getId();
                GUIController.getInstance().getController().joinLobby(GUIController.getInstance().getController().getVirtualView(), lobbyId, GUIController.getInstance().getNickname(), color);
                errorLabel.setText("");  // Pulisce eventuali errori precedenti
                hideButtonShowLoading();
            } catch (RemoteException e) {
                errorLabel.setText("Impossibile accedere alla lobby.");
            }

        }
    }

    @FXML
    private void onCreateLobby(MouseEvent event) {
        createForm.setVisible(true);

    }

    @FXML
    private void onConfirmCreateLobby(MouseEvent event) {
        int maxplayers = maxPlayersSpinner.getValue();
        int level = levelChoiceBox.getValue();
        PlayerColor color = PlayerColor.valueOf(colorChoiceBox.getValue());

        if (color == null) {
            errorLabel.setText("Please select both level and color.");
            return;
        }

        try {
            GUIController.getInstance().getController().createLobby(GUIController.getInstance().getController().getVirtualView(), GUIController.getInstance().getNickname(), maxplayers, color, level);
            onLobbyCreatedCorrectly();
        } catch (RemoteException e) {
            errorLabel.setText("Error " + e.getMessage());
        }
    }

    public void onLobbyCreatedCorrectly() {
        System.out.println("lobby creata");
        createForm.setVisible(false);
        hideButtonShowLoading();
    }


    @FXML
    private void onRefreshLobbies(MouseEvent event) {
        System.out.println("Refresh button clicked");
        refreshLobbyList();
    }

    private void refreshLobbyList() {
        try {
            GUIController.getInstance().getController().getLobbies(GUIController.getInstance().getController().getVirtualView());

        } catch (RemoteException e) {
            displayError("Errore nel recupero delle lobby.");
        }
    }

    private void loadLobbies() {
//        List<String> lobbies = clientController.getAvailableLobbies();
//        List<String> lobbies = new ArrayList<>();
//        lobbies.add("Lobby 1 - Gabri");
//        lobbies.add("Lobby 2 - Marco");
//        lobbies.add("Lobby 3 - Luca");
//        Platform.runLater(() -> {
//            lobbyList.getItems().setAll(lobbies);
//            errorLabel.setText("");
//        });
    }

    private void displayError(String message) {
        Platform.runLater(() -> errorLabel.setText(message));
    }

    public void onJoinLobbySuccess() {
        hideButtonShowLoading();
        root.getChildren().remove(dimBackground); // Rimuove overlay
    }
}