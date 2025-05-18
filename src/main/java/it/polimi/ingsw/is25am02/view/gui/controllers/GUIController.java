package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.utils.Lobby;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
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
        System.out.println("GUIController instance check1");
        if (instance == null) {
            throw new IllegalStateException("GUIController non è stato ancora inizializzato!");
        }
        return instance;
    }

    public static synchronized GUIController getInstance(Stage primaryStage) {
        System.out.println("GUIController instance check2");
        if (instance == null) {
            instance = new GUIController(primaryStage);
        }
        return instance;
    }


    public void setController(ClientController controller) {
        if(controller != null)
            System.out.println("CONTROLLER OK");
        else
            System.out.println("CONTROLLER NO");
        this.controller = controller;
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
