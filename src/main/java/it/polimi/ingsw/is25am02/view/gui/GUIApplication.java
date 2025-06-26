package it.polimi.ingsw.is25am02.view.gui;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.view.ConsoleClient;
import it.polimi.ingsw.is25am02.view.gui.controllers.*;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.CardV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;
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
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GUIApplication extends Application implements ConsoleClient {
    public static final Logger logger = Logger.getLogger(GUIApplication.class.getName());
    public static int MIN_WIDTH = 1560;
    public static int MIN_HEIGHT = 900;
    private static Instant lastFullScreenAction = null;
    private Map<Integer, LobbyView> lobbies;


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
        stage.setFullScreen(true);  // a tutto schermo
        stage.setResizable(false);  // non ridimensionabile
        stage.setFullScreenExitHint(""); // rimuove il messaggio per uscire dal fullscreen
        stage.setWidth(1920);
        stage.setHeight(1080);
        stage.initStyle(StageStyle.DECORATED);
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
        //controller.initialize();

        GUIController.getInstance().addControllerToList((it.polimi.ingsw.is25am02.view.gui.controllers.GeneralController) controller, "HomeScene");

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
            GUIController.getInstance().showError(keys, params);
        });

    }

    @Override
    public void displayMessage(String keys, Map<String, String> params) {
        if (keys.equals("disconnect.game")||keys.equals("disconnect.lobby_owner")) {
            Platform.runLater(() -> {
                try {
                    GUIController.getInstance().switchScene("lobby", "Seleziona una Lobby", (LobbyController g) -> {
                        //g.initialize();
                    });
                } catch (Exception e) {
                    System.err.println("Errore durante switchScene: ");
                    e.printStackTrace();
                }
            });
        }

        Platform.runLater(() -> {
            GUIController.getInstance().showMessage(keys, params);
        });
    }

    @Override
    public void setLobbiesView(Map<Integer, LobbyView> lobbies) {
        this.lobbies = lobbies;
        Platform.runLater(() -> {
            GUIController.getInstance().setLobbiesView(lobbies);
        });
    }

    @Override
    public void newTile(TileV newTile) {
        Platform.runLater(() -> {
            GUIController.getInstance().newTile(newTile);
        });
    }

    @Override
    public void newCard(CardV card) {
        Platform.runLater(() -> {
            GUIController.getInstance().newCard(card);
            /*
            GUIController.getInstance().<InGameController>switchScene("InGame", "in game", controller -> {
                controller.newCard(card);
            });
             */
        });

    }


    @Override
    public void setBuildView(int level, PlayerColor color) {
        //System.out.println("setBuildView - ho ricevuto una richiesta di build");

        Platform.runLater(() -> {
            GUIController.getInstance().<BuildController>switchScene("Build", "Build", controller -> {
                controller.initialize(level, color);
            });
        });
    }

    @Override
    public void setGameView(int level, PlayerColor color) {
        Platform.runLater(() -> {
            GUIController.getInstance().<InGameController>switchScene("InGame", "InGame", controller -> {
                controller.initialize(level, color);
            });
        });

    }

    @Override
    public void updateStats() {
        Platform.runLater(() -> {
            GUIController.getInstance().onUpdateStats();
        });
    }

    @Override
    public void updateCurrentPlayer() {
        Platform.runLater(() -> {
            GUIController.getInstance().updateCurrentPlayer();
        });
    }

    @Override
    public void updateDice(int result) {
        Platform.runLater(() -> {
            GUIController.getInstance().updateDice(result);
        });
    }

    @Override
    public void onResultState() {
        Platform.runLater(() -> {
            GUIController.getInstance().<ResultController>switchScene("Result", "Result", controller -> {
                //controller.initialize();
            });
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
    public void spaceshipBrokenUpdate(String details) {

    }

    @Override
    public void removedTile(Coordinate coordinate) {
        Platform.runLater(() -> {
            GUIController.getInstance().onRemoveTile(coordinate);
        });
    }
}