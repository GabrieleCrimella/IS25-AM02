package it.polimi.ingsw.is25am02.view.gui.controllers;


import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class LobbyController {
    private PlayerV p;

    @FXML
    private ListView<String> lobbyList;

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
        try {
            p=clientController.getPlayerVFromNickname(controller.getNickname());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        loadLobbies();
    }

    @FXML
    private void onJoinLobby(MouseEvent event) {
        String selectedLobby = lobbyList.getSelectionModel().getSelectedItem();
        if (selectedLobby == null) {
            errorLabel.setText("Seleziona una lobby!");
        } else {
            //todo parametri sotto da cambiare
            boolean success = false;
            try {
                clientController.joinLobby(clientController.getVirtualView(), 1, p.getNickname(), PlayerColor.GREEN);
                errorLabel.setText("");
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
        } catch (RemoteException e) {
            errorLabel.setText("Error " + e.getMessage());
        }
    }


    @FXML
    private void onRefreshLobbies(MouseEvent event) {
        refreshLobbyList();
    }

    private void refreshLobbyList() {
        lobbyList.getItems().clear();
        List<String> lobbies = null;
        try {
            clientController.getLobbies(clientController.getVirtualView());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        lobbyList.getItems().addAll(lobbies);
    }

    private void loadLobbies() {
        //List<String> lobbies = clientController.getAvailableLobbies();
        List<String> lobbies = new ArrayList<>();
        lobbies.add("Lobby 1 - Gabri");
        lobbies.add("Lobby 2 - Marco");
        lobbies.add("Lobby 3 - Luca");
        Platform.runLater(() -> {
            lobbyList.getItems().setAll(lobbies);
            errorLabel.setText("");
        });
    }

    private void displayError(String message) {
        Platform.runLater(() -> errorLabel.setText(message));
    }
}