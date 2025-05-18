package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class HomeSceneController {

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

            // Richiamo il metodo di registrazione
            clientController.nicknameRegistration(nickname, clientController.getVirtualView());
            System.out.println("passo 1");
            // Salvataggio del nickname nel clientController
            GUIController c = GUIController.getInstance();
            System.out.println("passo 2");
            c.setNickname(nickname);
            System.out.println("passo 3");

            // Cambio scena verso la lobby
            //todo va fatto solo se il server cambia stato
            GUIController.getInstance().switchScene("lobby", "Seleziona una Lobby");
        } catch (Exception e) {
            showError("Errore durante il login: " + e.getMessage());
        }
    }

    /**
     * Mostra un errore nella Label.
     * @param message Messaggio di errore da mostrare.
     */
    private void showError(String message) {
        Platform.runLater(() -> {
            if (errorLabel != null) {
                errorLabel.setText(message);
            }
        });
    }
}
