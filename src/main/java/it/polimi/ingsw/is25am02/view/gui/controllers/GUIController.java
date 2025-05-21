package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.model.Lobby;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GUIController implements Runnable {
    private String nickname;
    private ClientController controller;
    private static GUIController instance;
    private Stage primaryStage;
    private Map<Integer, Lobby> lobbies;
    private Map<String, Object> controllers = new HashMap<>();
    private static Scene scene;
    private static Stage stage;
    private LobbyController lobbyController;

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

    public void setLobbyController(LobbyController controller) {
        this.lobbyController = controller;
    }

    public LobbyController getLobbyController() {
        return lobbyController;
    }

    public void showError(String keys) {
        System.out.println(keys);
    }


    public <T> T switchScene(String fxmlName, String title, Consumer<T> initializer) {
        try {
            // Se il controller è già presente, non ricaricare la scena ma aggiorna solo i dati
            if (controllers.containsKey(fxmlName)) {
                System.out.println("La scena '" + fxmlName + "' è già caricata. Riutilizzo il controller.");
                T controller = (T) controllers.get(fxmlName);
                if (initializer != null) {
                    initializer.accept(controller);
                }
                return controller;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlName + ".fxml"));

            Parent root = loader.load();
            T controller = loader.getController();

            if (initializer != null) {
                initializer.accept(controller);
            }

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
            primaryStage.setFullScreen(true);
            primaryStage.setResizable(false);
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
