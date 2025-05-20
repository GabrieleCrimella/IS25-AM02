package it.polimi.ingsw.is25am02.view.gui.controllers;


import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.model.Lobby;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LobbyController {
    private PlayerV p;

    @FXML
    private ListView<LobbyView> lobbyList;

    @FXML
    private Button joinButton;

    @FXML
    private Button refreshButton;

    @FXML
    private VBox createForm;

    @FXML
    private Spinner<Integer> maxPlayersSpinner;

    @FXML
    private ChoiceBox<Integer> levelChoiceBox;

    @FXML
    private ChoiceBox<PlayerColor> colorChoiceBox;


    @FXML
    private Label errorLabel;

    private ClientController clientController;

    private GUIController controller;

    public void initialize(ClientController clientController) {
        controller=GUIController.getInstance();
        this.clientController = clientController;
        controller.setLobbyController(this);
        try {
            clientController.getLobbies(clientController.getVirtualView());
        } catch (RemoteException e) {
            errorLabel.setText("get lobbies non funziona");
        }

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
    }

    public void setLobbies(List<LobbyView> lobbies) {
        ObservableList<LobbyView> observableList = FXCollections.observableArrayList(lobbies);
        lobbyList.setItems(observableList);
        lobbyList.setVisible(true);

        // CellFactory personalizzato
        lobbyList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(LobbyView lobby, boolean empty) {
                super.updateItem(lobby, empty);
                if (empty || lobby == null) {
                    setText(null);
                } else {
                    System.out.println("OKOK");
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
    }


    public void setLobbyListFromMap(Map<Integer, LobbyView> lobbyMap) {
        Platform.runLater(() -> {
            System.out.println("Aggiornamento lista lobby in GUI...");
            setLobbies(new ArrayList<>(lobbyMap.values()));
            //lobbyList.getItems().setAll(lobbyMap.values());
            errorLabel.setText("");
        });
    }

    @FXML
    private void onJoinLobby(MouseEvent event) {
        LobbyView selectedLobby = lobbyList.getSelectionModel().getSelectedItem();
        if (!lobbyList.isVisible()) {
            lobbyList.setVisible(true);  // Necessario se hai messo managed="false"
            refreshLobbyList();          // Popola la lista appena la mostri
            errorLabel.setText("Seleziona una lobby dalla lista.");
            return;
        }
        if (selectedLobby == null) {
            refreshLobbyList();
            errorLabel.setText("Seleziona una lobby!");
        } else {

            PlayerColor color = colorChoiceBox.getValue();
            if (color == null) {
                errorLabel.setText("Scegli un colore.");
                return;
            }

            try {
                int lobbyId = selectedLobby.getId();
                clientController.joinLobby(clientController.getVirtualView(), lobbyId, controller.getNickname(), color);
                errorLabel.setText("");  // Pulisce eventuali errori precedenti
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
        PlayerColor color = colorChoiceBox.getValue();

        if ( color == null) {
            errorLabel.setText("Please select both level and color.");
            return;
        }

        try {
            clientController.createLobby(clientController.getVirtualView(), controller.getNickname(), maxplayers, color, level);
            System.out.println("lobby creata");
            createForm.setVisible(false);
        } catch (RemoteException e) {
            errorLabel.setText("Error " + e.getMessage());
        }
    }


    @FXML
    private void onRefreshLobbies(MouseEvent event) {
        System.out.println("Refresh button clicked");
        refreshLobbyList();
    }

    private void refreshLobbyList() {
        try {
            clientController.getLobbies(clientController.getVirtualView());

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
}