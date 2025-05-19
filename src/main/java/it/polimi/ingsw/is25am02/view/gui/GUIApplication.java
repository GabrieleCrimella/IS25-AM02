package it.polimi.ingsw.is25am02.view.gui;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.utils.Lobby;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.view.ConsoleClient;
import it.polimi.ingsw.is25am02.view.gui.controllers.GUIController;
import it.polimi.ingsw.is25am02.view.gui.controllers.HomeSceneController;
import it.polimi.ingsw.is25am02.view.tui.utils.GraphicPrinter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Map;
import java.util.logging.Logger;


public class GUIApplication extends Application implements ConsoleClient {
    public static final Logger logger = Logger.getLogger(GUIApplication.class.getName());
    public static int MIN_WIDTH = 1560;
    public static int MIN_HEIGHT = 900;
    private static Instant lastFullScreenAction = null;
    private Map<Integer, Lobby> lobbies;


    public GUIApplication() {
        GUIController.initEmpty();

    }


    @Override
    public void init() {
        logger.info("GUIApp started");
        loadFonts();


    }

    @Override
    public void start(Stage stage) throws IOException {
        GUIController.getInstance(stage);

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double width = screenBounds.getWidth();
        double height = screenBounds.getHeight();

        logger.info("Screen size is " + width + " x " + height);

        if (width < MIN_WIDTH || height < MIN_HEIGHT) {
            System.setProperty("prism.allowhidpi", "false");
        }

        stage.setOnCloseRequest(event -> {
            logger.info("GUIApp quit");
            Platform.exit();
            System.exit(0);
        });

        stage.setTitle("Galaxy Trucker");
        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/image/icon.png")));

        GUIController.setStage(stage);

        URL fxml1URL = (getClass().getResource("/fxml/HomeScene.fxml"));
        if (fxml1URL == null) {
            throw new IOException("Cannot load resource: HomeScene.fxml");
        }
        FXMLLoader loader = new FXMLLoader(fxml1URL);

        Parent root = loader.load();
        Scene scene = new Scene(root, MIN_WIDTH, MIN_HEIGHT);
        handleFullscreenEvents(stage, scene);

        stage.setScene(scene);
        GUIController.setScene(scene);
        HomeSceneController controller = loader.getController();


        stage.show();
    }


    private void handleFullscreenEvents(Stage stage, Scene scene) {
        String fullScreenButton;
        if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
            fullScreenButton = "F11";
        } else {
            fullScreenButton = "Cmd+F";
        }

        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (System.getProperty("os.name").toLowerCase().contains("mac") && event.isMetaDown() && event.getCode() == KeyCode.F && !shouldDiscardEvent()) {
                lastFullScreenAction = Instant.now();
                stage.setFullScreen(!stage.isFullScreen());
            } else if (event.getCode() == KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });
    }

    private boolean shouldDiscardEvent() {
        return lastFullScreenAction != null
                && lastFullScreenAction.plusMillis(900).isAfter(Instant.now());
    }

    private void loadFonts() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/MedievalSharp-Book.ttf"), 10);
        Font.loadFont(getClass().getResourceAsStream("/fonts/CalSans-SemiBold.ttf"), 10);
    }

    @Override
    public void setController(ClientController controller) {
        GUIController.getInstance().setController(controller);

    }

    @Override
    public ClientController getController() {
        return GUIController.getInstance().getController();
    }

    @Override
    public void setNickname(String nickname) {
        GUIController.getInstance().setNickname(nickname);

    }

    @Override
    public String getNickname() {
        return GUIController.getInstance().getNickname();
    }

    @Override
    public void start() {
        launch();

    }

    @Override
    public void reportError(String keys, Map<String, String> params) {
        Platform.runLater(() -> {
            GUIController.getInstance().showError(keys);
        });

    }

    @Override
    public void displayMessage(String keys, Map<String, String> params) {


    }

    @Override
    public void setLobbiesView(Map<Integer, Lobby> lobbies) {
        this.lobbies = lobbies;

        // Richiama il controller della GUI
        Platform.runLater(() -> {
            GUIController.getInstance()
                    .getLobbyController()
                    .setLobbyListFromMap(lobbies);
        });

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


    //todo si potrebbe aggiugnere anche il caricamento dei fonts
}