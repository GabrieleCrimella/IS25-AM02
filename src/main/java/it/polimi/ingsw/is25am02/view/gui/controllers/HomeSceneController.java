package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class HomeSceneController {
    @FXML
    public Button closeButton;
    @FXML
    private TextField nicknameField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    // Richiamo il ClientController dal singleton di GUIController
    private ClientController clientController;


    @FXML
    private void onLogin() {
        String nickname = nicknameField.getText().trim();
        clientController = GUIController.getInstance().getController();

        if (nickname.isEmpty()) {
            showError("Il nickname non può essere vuoto.");
            return;
        }

        try {

            clientController.nicknameRegistration(nickname, clientController.getVirtualView());
            GUIController c = GUIController.getInstance();
            c.setNickname(nickname);

            // Cambio scena verso la lobby
            //todo va fatto solo se il server cambia stato

            GUIController.getInstance().switchScene("lobby", "Seleziona una Lobby", (LobbyController g) -> {
                g.initialize(clientController);
            });
        } catch (Exception e) {
            showError("Errore durante il login: " + e.getMessage());
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Platform.exit();
    }


    private void showError(String message) {
        Platform.runLater(() -> {
            if (errorLabel != null) {
                errorLabel.setText(message);
            }
        });
    }
}
