package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.view.ConsoleClient;
import it.polimi.ingsw.is25am02.view.tui.utils.GraphicPrinter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import it.polimi.ingsw.is25am02.utils.Coordinate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GUIController implements Runnable, ConsoleClient {
    private String nickname;
    private ClientController controller;
    private static GUIController instance;
    private Stage primaryStage;

    // Mappa per tenere traccia dei controller
    private Map<String, Object> controllers = new HashMap<>();

    /**
     * The current scene.
     */
    private static Scene scene;
    /**
     * The current stage.
     */
    private static Stage stage;

    private GUIController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static GUIController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("GUIController non è stato ancora inizializzato!");
        }
        return instance;
    }

    public static GUIController getInstance(Stage primaryStage) {
        if (instance == null) {
            instance = new GUIController(primaryStage);
        }
        return instance;
    }

    public static void setScene(Scene scene) {
        GUIController.scene = scene;
    }

    public static void setStage(Stage stage) {
        GUIController.stage = stage;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void start() {

    }

    @Override
    public void reportError(String keys, Map<String, String> params) {

    }

    @Override
    public void displayMessage(String keys, Map<String, String> params) {

    }

    @Override
    public GraphicPrinter getPrinter() {
        return null;
    }

    @Override
    public void startCountdown() {

    }

    @Override
    public void spaceshipBrokenUpdate(String details, Coordinate[][] ships) {

    }

    @Override
    public void setController(ClientController controller) {
        this.controller = controller;
    }

    @Override
    public ClientController getController() {
        return controller;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public <T> T switchScene(String fxmlName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/" + fxmlName + ".fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
            primaryStage.show();

            // Recupero il controller e lo salvo nella mappa
            T controller = loader.getController();
            controllers.put(fxmlName, controller);

            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getController(String fxmlName) {
        return (T) controllers.get(fxmlName);
    }

    @Override
    public void run() {

    }
}
