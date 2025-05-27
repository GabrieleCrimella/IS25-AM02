package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.model.Lobby;
import it.polimi.ingsw.is25am02.view.tui.utils.JsonMessageManager;
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
    private Map<String, GeneralController> controllers = new HashMap<>();
    private String inUse;
    private static Scene scene;
    private static Stage stage;
    private LobbyController lobbyController;
    private JsonMessageManager messManager;
    public boolean errorshown;

    public boolean isErrorshown() {
        return errorshown;
    }

    public void setErrorshown(boolean errorshown) {
        this.errorshown = errorshown;
    }

    public ClientController getController() {
        return controller;
    }

    private GUIController(Stage primaryStage) {
        errorshown = false;
        this.primaryStage = primaryStage;
        try {
            this.messManager = new JsonMessageManager("src/main/resources/json/messages.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void showError(String keys, Map<String, String> params) {
        if (keys.equals("info.spaceship.wrong")) {
            if (inUse.equals("Build")) {
                BuildController bldCtrl = (BuildController) controllers.get(inUse);
                bldCtrl.onSpaceshipWrong();
            }
        }
        controllers.get(inUse).showNotification(messManager.getMessageWithParams(keys, params), GeneralController.NotificationType.ERROR, 5000);
        errorshown = true;
        System.out.println("errorshown " + errorshown);
    }


    public <GeneralController> GeneralController switchScene(String fxmlName, String title, Consumer<GeneralController> initializer) {
        try {
            // Se il controller è già presente, non ricaricare la scena ma aggiorna solo i dati
            if (controllers.containsKey(fxmlName)) {
                System.out.println("La scena '" + fxmlName + "' è già caricata. Riutilizzo il controller.");
                GeneralController controller = (GeneralController) controllers.get(fxmlName);
                if (initializer != null) {
                    initializer.accept(controller);
                }
                return controller;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlName + ".fxml"));

            Parent root = loader.load();
            GeneralController controller = loader.getController();

            if (initializer != null) {
                initializer.accept(controller);
            }

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
            primaryStage.setFullScreen(true);
            primaryStage.setResizable(false);
            primaryStage.show();

            controllers.put(fxmlName, (it.polimi.ingsw.is25am02.view.gui.controllers.GeneralController) controller);
            inUse = fxmlName;

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

    public void showMessage(String keys, Map<String, String> params) {
        if (keys.equals("build.addTiles")) {
            if (inUse.equals("Build")) {
                BuildController bldCtrl = (BuildController) controllers.get(inUse);
                bldCtrl.onAddTileSuccess();
                return;
            }
        } else if (keys.equals("info.finished")) {
            if (inUse.equals("Build")) {
                BuildController bldCtrl = (BuildController) controllers.get(inUse);
                bldCtrl.onShipFinished();
                return;
            }
        } else if (keys.equals("info.spaceship.right")) {
            if (inUse.equals("Build")) {
                BuildController bldCtrl = (BuildController) controllers.get(inUse);
                bldCtrl.onSpaceshipRight();
            }
            //controllers.get(inUse).showNotification(messManager.getMessageWithParams(keys, params), GeneralController.NotificationType.SUCCESS, 5000);
        } else if (keys.equals("hourglass.time")) {
            int timeleft = Integer.parseInt(params.get("tim"));
            getController().getGameV().getHourglass().setTimeLeft(timeleft);
            BuildController bldCtrl = (BuildController) controllers.get(inUse);
            bldCtrl.seeHourglass(timeleft);

        }
    }
}
