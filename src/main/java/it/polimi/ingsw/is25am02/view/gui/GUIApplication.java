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


public class GUIApplication extends Application implements ConsoleClient, Runnable {
    public static final Logger logger = Logger.getLogger(GUIApplication.class.getName());
    public static int MIN_WIDTH = 1560;
    public static int MIN_HEIGHT = 900;
    private static Instant lastFullScreenAction = null;
    private final Object controllerLock = new Object();
    private volatile boolean guiInitialized = false;
    private volatile boolean controllerSet = false;

    public GUIApplication() {



    }


    @Override
    public void init() {
        logger.info("GUIApp started");
        loadFonts();
    }

    @Override
    public void start(Stage stage) throws IOException {
        GUIController.getInstance(stage);
        synchronized (controllerLock) {
            guiInitialized = true;
            controllerLock.notifyAll();
        }

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double width = screenBounds.getWidth();
        double height = screenBounds.getHeight();

        logger.info("Screen size is " + width + " x " + height);

        if (width < MIN_WIDTH || height < MIN_HEIGHT) {
            System.setProperty("prism.allowhidpi", "false");
        }

        // Set the stage to close the application when the window is closed.
        stage.setOnCloseRequest(event -> {
            logger.info("GUIApp quit");
            Platform.exit();
            System.exit(0);
        });

        // Set the stage title and dimensions.
        stage.setTitle("Galaxy Trucker");
        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/image/icon.png")));

        // Set the current stage on all controllers.
        GUIController.setStage(stage);

        URL fxml1URL = (getClass().getResource("/fxml/HomeScene.fxml"));
        if (fxml1URL == null) {
            throw new IOException("Cannot load resource: HomeScene.fxml");
        }
        FXMLLoader loader = new FXMLLoader(fxml1URL);

        // Load the scene
        Parent root = loader.load();

        Scene scene = new Scene(root, MIN_WIDTH, MIN_HEIGHT);

        // Handle fullscreen events
        handleFullscreenEvents(stage, scene);

        // Set the stage to the current scene.
        stage.setScene(scene);

        // Set the current scene on all controllers.
        GUIController.setScene(scene);

        // Get the controller from the loader.
        HomeSceneController controller = loader.getController();

        // Call the beforeShow method of the controller.
        // This method is used to perform actions right before the window is shown.
        //controller.beforeMount(null);

        stage.show();
    }


    private void handleFullscreenEvents(Stage stage, Scene scene) {
        String fullScreenButton;
        // Set the full screen button based on the OS.
        if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
            fullScreenButton = "F11";
        } else {
            fullScreenButton = "Cmd+F";
        }
//        // Show a toast message when entering full screen.
//        stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue) {
//                Controller.showToast(ToastLevels.INFO, "Full Screen Enabled", "Press " + fullScreenButton + " to exit full screen.");
//            }
//        });
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        // Set full screen shortcut
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (System.getProperty("os.name").toLowerCase().contains("mac") && event.isMetaDown() && event.getCode() == KeyCode.F && !shouldDiscardEvent()) {
                lastFullScreenAction = Instant.now();
                stage.setFullScreen(!stage.isFullScreen());
            } else if (event.getCode() == KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });
        //Controller.showToast(ToastLevels.INFO, "Welcome!", "Press " + fullScreenButton + " to enter full screen.");
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
        synchronized (controllerLock) {
            while (!guiInitialized) {
                try {
                    controllerLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for GUIController initialization", e);
                }
            }
            GUIController.getInstance().setController(controller);
            controllerSet = true;
            controllerLock.notifyAll(); // notify anyone waiting on getController()
        }

    }

    @Override
    public ClientController getController() {
        synchronized (controllerLock) {
            while (!controllerSet) {
                try {
                    controllerLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for ClientController to be set", e);
                }
            }
            return GUIController.getInstance().getController();
        }
    }

    @Override
    public void setNickname(String nickname) {

    }

    @Override
    public String getNickname() {
        return "";
    }

    @Override
    public void start() {
        Thread guiThread = new Thread(this);
        guiThread.setDaemon(true); // o true, se vuoi che si chiuda con il processo principale
        guiThread.start();

    }

    @Override
    public void reportError(String keys, Map<String, String> params) {

    }

    @Override
    public void displayMessage(String keys, Map<String, String> params) {

    }

    @Override
    public void setLobbiesView(Map<Integer, Lobby> lobbies) {

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
    public void run() {
        launch();
    }

    //todo si potrebbe aggiugnere anche il caricamento dei fonts
}