package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.utils.Lobby;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class GUIController implements Runnable {
    private String nickname;
    private ClientController controller;
    private static GUIController instance;
    private Stage primaryStage;
    private Map<Integer, Lobby> lobbies;
    private Map<String, Object> controllers = new HashMap<>();
    private static Scene scene;
    private static Stage stage;

    public ClientController getController() {
        return controller;
    }


    private GUIController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static synchronized GUIController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("GUIController non è stato ancora inizializzato!");
        }
        return instance;
    }

    public static synchronized GUIController getInstance(Stage primaryStage) {
        if (instance == null) {
            instance = new GUIController(primaryStage);
        } else if (instance.primaryStage == null) {
            instance.primaryStage = primaryStage;
        }
        return instance;
    }


    public void setController(ClientController controller) {

        this.controller = controller;
    }

    public static synchronized void initEmpty() {
        if (instance == null) {
            instance = new GUIController(null); // stage verrà impostato dopo
        }
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


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private LobbyController lobbyController;

    public void setLobbyController(LobbyController controller) {
        this.lobbyController = controller;
    }

    public LobbyController getLobbyController() {
        return lobbyController;
    }

    public void showError(String keys){
        System.out.println(keys);
    }



    public <T> T switchScene(String fxmlName, String title, java.util.function.Consumer<T> initializer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlName + ".fxml"));


            Parent root = loader.load();
            T controller = loader.getController();

            if (initializer != null) {
                initializer.accept(controller);
            }

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
            primaryStage.show();

            controllers.put(fxmlName, controller);

            return controller;
        } catch (IOException e) {
            //todo
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
