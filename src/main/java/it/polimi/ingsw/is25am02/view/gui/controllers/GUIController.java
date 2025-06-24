package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.model.Lobby;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.CardV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;
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

    public ClientController getController() {
        return controller;
    }

    private GUIController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            this.messManager = new JsonMessageManager("src/main/resources/json/messages.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void addControllerToList(GeneralController c, String fxmlName) {
        if (!controllers.containsKey(fxmlName)) {
            controllers.put(fxmlName, c);
            inUse = fxmlName;
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
    }


    public <GeneralController> GeneralController switchScene(String fxmlName, String title, Consumer<GeneralController> initializer) {
        try {
            // Se il controller è già presente, non ricaricare la scena ma aggiorna solo i dati
            if (controllers.containsKey(fxmlName)) {
                //System.out.println("La scena '" + fxmlName + "' è già caricata. Riutilizzo il controller.");
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
        switch (keys) {
            case "build.bookedTile"->{
                if (inUse.equals("Build")) {
                    BuildController bldCtrl = (BuildController) controllers.get(inUse);
                    bldCtrl.onBookedTileSuccess();
                    return;
                }
            }
            case "lobby.join" -> {
                if (inUse.equals("lobby")) {
                    LobbyController lobbyCtrl = (LobbyController) controllers.get(inUse);
                    lobbyCtrl.onJoinLobbySuccess();
                    return;
                }
            }
            case "info.loginDone" -> {
                if (inUse.equals("HomeScene")) {
                    HomeSceneController homeCtrl = (HomeSceneController) controllers.get(inUse);
                    homeCtrl.loginDone();
                    return;
                }
            }
            case "build.addTile" -> {
                if (inUse.equals("Build")) {
                    BuildController bldCtrl = (BuildController) controllers.get(inUse);
                    bldCtrl.onAddTileSuccess();
                    return;
                }
            }
            case "build.returnTile" -> {
                if (inUse.equals("Build")) {
                    BuildController bldCtrl = (BuildController) controllers.get(inUse);
                    bldCtrl.onReturnTileSuccess();
                    return;
                }
            }
            case "info.gameState" -> {
                if (params.containsKey("state") && params.get("state").equals("INITIALIZATION_SPACESHIP")) {
                    if (inUse.equals("Build")) {
                        BuildController bldCtrl = (BuildController) controllers.get(inUse);
                        bldCtrl.setInitializationSpaceship();
                    }
                }
            }
            case "info.finished" -> {
                if (inUse.equals("Build")) {
                    BuildController bldCtrl = (BuildController) controllers.get(inUse);
                    bldCtrl.onShipFinished();
                    return;
                }
            }
            case "info.spaceship.right" -> {
                if (inUse.equals("Build")) {
                    BuildController bldCtrl = (BuildController) controllers.get(inUse);
                    bldCtrl.onSpaceshipRight();
                }
                //controllers.get(inUse).showNotification(messManager.getMessageWithParams(keys, params), GeneralController.NotificationType.SUCCESS, 5000);
            }
            case "hourglass.flipped"->{
                if (inUse.equals("Build")) {
                    BuildController bldCtrl = (BuildController) controllers.get(inUse);
                    bldCtrl.onHourglassFlipped();
                }
            }
            case "hourglass.time" -> {
                int timeleft = Integer.parseInt(params.get("tim"));
                getController().getGameV().getHourglass().setTimeLeft(timeleft);
                BuildController bldCtrl = (BuildController) controllers.get(inUse);
                bldCtrl.seeHourglass(timeleft);
            }
            case "minideck.view"->{
                if (inUse.equals("Build")) {
                    BuildController bldCtrl = (BuildController) controllers.get(inUse);
                    bldCtrl.onViewMiniDeck(params.get("index"));
                }
            }
            case "minideck.return"->{
                if (inUse.equals("Build")) {
                    BuildController bldCtrl = (BuildController) controllers.get(inUse);
                    bldCtrl.onReturnMiniDeck();
                }
            }
            case "ingame.hidechoicebox"->{
                boolean visible = Boolean.parseBoolean(params.get("visible"));
                if (inUse.equals("InGame")) {
                    InGameController inGameCtrl = (InGameController) controllers.get(inUse);
                    inGameCtrl.hideChoiceBox(visible);
                }
            }
            case "ingame.hidechoice"->{
                boolean choice = Boolean.parseBoolean(params.get("choice"));
                if (inUse.equals("InGame")) {
                    InGameController inGameCtrl = (InGameController) controllers.get(inUse);
                    inGameCtrl.hideChoice(choice);
                }
            }
            case "ingame.hidefinishcannon"->{
                int number = Integer.parseInt(params.get("number"));
                if (inUse.equals("InGame")) {
                    InGameController inGameCtrl = (InGameController) controllers.get(inUse);
                    inGameCtrl.hideFinishCannon(number);
                }
            }
            case "ingame.hidefinishmotor"->{
                int number = Integer.parseInt(params.get("number"));
                if (inUse.equals("InGame")) {
                    InGameController inGameCtrl = (InGameController) controllers.get(inUse);
                    inGameCtrl.hideFinishMotor(number);
                }
            }
            case "ingame.hidemovebox"->{
                if (inUse.equals("InGame")) {
                    InGameController inGameCtrl = (InGameController) controllers.get(inUse);
                    inGameCtrl.hideMoveBox();
                }
            }
            case "ingame.hidechoiceplanet"->{
                if (inUse.equals("InGame")) {
                    InGameController inGameCtrl = (InGameController) controllers.get(inUse);
                    inGameCtrl.hideChoicePlanet();
                }
            }
            case "ingame.hidecalculatedamage"->{
                if (inUse.equals("InGame")) {
                    InGameController inGameCtrl = (InGameController) controllers.get(inUse);
                    inGameCtrl.hideCalculateDamage();
                }
            }
            case "ingame.afterchoicecrew"->{
                if (inUse.equals("InGame")) {
                    InGameController inGameCtrl = (InGameController) controllers.get(inUse);
                    inGameCtrl.afterChoiceCrew();
                }
            }
            case "ingame.diceupdate"->{
                if (inUse.equals("InGame")) {
                    InGameController inGameCtrl = (InGameController) controllers.get(inUse);
                    inGameCtrl.showdiceresult();
                }
            }
            case "build.addCrew"->{
                if (inUse.equals("Build")) {
                    BuildController bldCtrl = (BuildController) controllers.get(inUse);
                    bldCtrl.onAddCrewSuccess();
                    return;
                }
            }
            case "ingame.moveongameboard"->{
                if (inUse.equals("InGame")) {
                    InGameController inGameCtrl = (InGameController) controllers.get(inUse);
                    inGameCtrl.movePlayerToPosition(Integer.parseInt(params.get("pos")));
                }
            }
            case "ingame.winners"->{
                String winners = (params.get("winners"));
                HashMap<String, Integer> winnersMap = parseWinnersMap(winners);
                if (inUse.equals("Result")) {
                    ResultController resultController = (ResultController) controllers.get(inUse);
                    resultController.showWinners(winnersMap);
                }
            }
            case "info.outOfGame"->{
                if(inUse.equals("InGame")){
                    InGameController inGameCtrl = (InGameController) controllers.get(inUse);
                    inGameCtrl.onOutOfGame();
                    return;
                }
            }
            default ->
                    controllers.get(inUse).showNotification(messManager.getMessageWithParams(keys, params), GeneralController.NotificationType.SUCCESS, 5000);
        }
    }

    public HashMap<String, Integer> parseWinnersMap(String mapStr) {
        HashMap<String, Integer> result = new HashMap<>();
        mapStr = mapStr.replaceAll("[{}]", "").trim();

        if (mapStr.isEmpty()) return result;

        // Split delle entry: es "Alice=3, Bob=2"
        String[] entries = mapStr.split(",\\s*");
        for (String entry : entries) {
            String[] kv = entry.split("=");
            if (kv.length == 2) {
                String key = kv[0].trim();
                try {
                    Integer value = Integer.parseInt(kv[1].trim());
                    result.put(key, value);
                } catch (NumberFormatException e) {
                    // log, ignora o gestisci errori di parsing
                }
            }
        }
        return result;
    }

    public void onRemoveTile(Coordinate coordinate) {
        if (inUse.equals("Build")) {
            BuildController bldCtrl = (BuildController) controllers.get(inUse);
            bldCtrl.onRemoveTile(coordinate);
        } else if(inUse.equals("InGame")){
            InGameController inGameCtrl = (InGameController) controllers.get(inUse);
            inGameCtrl.onRemoveTile(coordinate);
        }
    }

    public void newCard(CardV card) {
        if(inUse.equals("InGame")){
            InGameController inGameCtrl = (InGameController) controllers.get(inUse);
            inGameCtrl.newCard(card);
        }
    }

    public void onUpdateStats() {
        if (inUse.equals("InGame")) {
            InGameController inGameCtrl = (InGameController) controllers.get(inUse);
            inGameCtrl.updateStats();
        }
    }

    public void updateDice(int result) {
        if( inUse.equals("InGame")) {
            InGameController inGameCtrl = (InGameController) controllers.get(inUse);
            inGameCtrl.updateDice(result);
        }
    }

    public void updateCurrentPlayer() {
    if (inUse.equals("InGame")) {
            InGameController inGameCtrl = (InGameController) controllers.get(inUse);
            inGameCtrl.updateCurrentPlayerName();
        }
    }


    public void moveOnGameboard(int pos) {
    if (inUse.equals("InGame")) {
            InGameController inGameCtrl = (InGameController) controllers.get(inUse);
            inGameCtrl.movePlayerToPosition(pos);
        }
    }


    public void newTile(TileV newTile) {
    if (inUse.equals("Build")) {
            BuildController bldCtrl = (BuildController) controllers.get(inUse);
            bldCtrl.newTile(newTile);
        }
    }

    public void setLobbiesView(Map<Integer, LobbyView> lobbies) {
    if (inUse.equals("lobby")) {
            LobbyController lobbyCtrl = (LobbyController) controllers.get(inUse);
            lobbyCtrl.setLobbyListFromMap(lobbies);
        }
    }
}
