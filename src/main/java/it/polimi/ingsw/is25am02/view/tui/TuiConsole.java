package it.polimi.ingsw.is25am02.view.tui;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.utils.enumerations.AliveType;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.view.ConsoleClient;
import it.polimi.ingsw.is25am02.view.tui.utils.JsonMessageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/*
 * Classe che gestisce l'interfaccia testuale (TUI) del gioco.
 * Si occupa di ricevere i comandi dall'utente e inoltrarli al controller client.
 */
public class TuiConsole implements Runnable, ConsoleClient {
    private ClientController controller;
    private final JsonMessageManager messManager;


    //è il posto in cui leggo
    private final BufferedReader reader;
    private boolean running;
    private Player currentPlayer;
    private String nickname;

    public TuiConsole() throws Exception {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.running = false;
        this.messManager = new JsonMessageManager("src/main/resources/json/messages.json");
    }

    public void closeConnect() {
        try {
            controller.closeConnect();
        } catch (Exception e) {
            reportError("error.connection.close", null);
        }
    }

    public void setController(ClientController control) {
        controller = control;
    }

    public ClientController getController() {
        return controller;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    /**
     * Avvia l'interfaccia testuale in un thread separato
     */
    public void start() {
        if (!running) {
            running = true;
            new Thread(this).start();
        }
    }

    //Ferma l'interfaccia testuale
    public void stop() {
        running = false;
    }

    //Mostra il messaggio di benvenuto all'avvio del gioco
    private void printWelcomeMessage() {
        displayMessage("welcome", null);
        printHelp();
    }


    //Mostra l'elenco dei comandi disponibili
    private void printHelp() {
        displayMessage("help", null);
    }

    //Metodo principale del thread che gestisce l'input dell'utente
    @Override
    public void run() {
        printWelcomeMessage();

        while (running) {
            try {
                //System.out.print("> ");
                String input = reader.readLine();

                if (input == null || input.trim().isEmpty()) {
                    continue;
                }

                processCommand(input.trim());

            } catch (IOException e) {
                reportError("error.reading.loop", null);
            }
        }
    }

    //Elabora il comando inserito dall'utente
    private void processCommand(String input) {
        if (!input.startsWith("/")) {
            reportError("error.reading.slash", null);
            return;
        }

        // Remove the initial '/'
        input = input.substring(1);

        // Divide the command in tokens
        StringTokenizer tokenizer = new StringTokenizer(input);
        if (!tokenizer.hasMoreTokens()) {
            return;
        }

        String command = tokenizer.nextToken();

        try {
            switch (command) {
                case "help":
                    printHelp();
                    break;

                case "exit":
                    displayMessage("exit", null);

                    //Stop connection and UI threads
                    stop();
                    closeConnect();
                    System.exit(0);
                    break;

                case "login":
                    if (tokenizer.countTokens()==0) {
                        reportError("error.reading.input.login", null);
                        break;
                    }else if(tokenizer.countTokens()>1){
                        reportError("error.reading.input.syntax", Map.of("syntax","/login <nickname>"));
                        break;
                    }
                    controller.nicknameRegistration(tokenizer.nextToken(), controller.getVirtualView());
                    break;

                case "create":
                    if (tokenizer.countTokens() < 3) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/create <maxPlayers> <color> <level>"));
                        break;
                    }
                    int maxPlayers = Integer.parseInt(tokenizer.nextToken());
                    PlayerColor color = PlayerColor.valueOf(tokenizer.nextToken().toUpperCase());
                    int level = Integer.parseInt(tokenizer.nextToken());
                    controller.createLobby(controller.getVirtualView(), nickname, maxPlayers, color, level);
                    break;

                case "lobbies":
                    controller.getLobbies(controller.getVirtualView());
                    break;

                case "join":
                    if (tokenizer.countTokens() < 2) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/join <lobbyId> <color>"));
                        break;
                    }
                    int lobbyId = Integer.parseInt(tokenizer.nextToken());
                    PlayerColor joinColor = PlayerColor.valueOf(tokenizer.nextToken().toUpperCase());
                    controller.joinLobby(controller.getVirtualView(), lobbyId, nickname, joinColor);
                    break;

                case "hourglass":
                    controller.flipHourglass(currentPlayer.getNickname());
                    break;

                case "take":
                    controller.takeTile(currentPlayer.getNickname());
                    break;

                case "takeTile":
                    if (!tokenizer.hasMoreTokens()) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/takeTile <id>"));
                        break;
                    }
                    int tileId = Integer.parseInt(tokenizer.nextToken());
                    // Qui dovresti ottenere l'oggetto Tile corrispondente a tileId
                    Tile tile = getTileById(tileId); // Questo metodo dovrebbe essere implementato
                    controller.takeTile(currentPlayer.getNickname(), tile);
                    break;

                case "takeMiniDeck":
                    if (!tokenizer.hasMoreTokens()) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/takeMiniDeck <index>"));
                        break;
                    }
                    int miniDeckIndex = Integer.parseInt(tokenizer.nextToken());
                    controller.takeMiniDeck(currentPlayer.getNickname(), miniDeckIndex);
                    break;

                case "returnMiniDeck":
                    controller.returnMiniDeck(currentPlayer.getNickname());
                    break;

                case "book":
                    controller.bookTile(currentPlayer.getNickname());
                    break;

                case "addBooked":
                    if (tokenizer.countTokens() < 4) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/addBooked <index> <x> <y> <rotation>"));
                        break;
                    }
                    int index = Integer.parseInt(tokenizer.nextToken());
                    int x = Integer.parseInt(tokenizer.nextToken());
                    int y = Integer.parseInt(tokenizer.nextToken());
                    RotationType rotation = RotationType.valueOf(tokenizer.nextToken().toUpperCase());
                    controller.addBookedTile(currentPlayer.getNickname(), index, new Coordinate(x, y), rotation);
                    break;

                case "return":
                    controller.returnTile(currentPlayer.getNickname());
                    break;

                case "add":
                    if (tokenizer.countTokens() < 3) {
                        reportError("error.reading.input.syntax", Map.of("syntax","/add <x> <y> <rotation>"));
                        break;
                    }
                    int addX = Integer.parseInt(tokenizer.nextToken());
                    int addY = Integer.parseInt(tokenizer.nextToken());
                    RotationType addRotation = RotationType.valueOf(tokenizer.nextToken().toUpperCase());
                    controller.addTile(currentPlayer.getNickname(), new Coordinate(addX, addY), addRotation);
                    break;

                case "finish":
                    controller.shipFinished(currentPlayer.getNickname());
                    break;

                case "check":
                    controller.checkSpaceship(currentPlayer.getNickname());
                    break;

                case "remove":
                    if (tokenizer.countTokens() < 2) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/remove <x> <y>"));
                        break;
                    }
                    int removeX = Integer.parseInt(tokenizer.nextToken());
                    int removeY = Integer.parseInt(tokenizer.nextToken());
                    controller.removeTile(currentPlayer.getNickname(), new Coordinate(removeX, removeY));
                    break;

                case "addCrew":
                    if (tokenizer.countTokens() < 3) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/addCrew <x> <y> <type>"));
                        break;
                    }
                    int crewX = Integer.parseInt(tokenizer.nextToken());
                    int crewY = Integer.parseInt(tokenizer.nextToken());
                    AliveType type = AliveType.valueOf(tokenizer.nextToken().toUpperCase());
                    controller.addCrew(currentPlayer.getNickname(), new Coordinate(crewX, crewY), type);
                    break;

                case "removeCrew":
                    if (tokenizer.countTokens() < 2) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/removeCrew <x> <y>"));
                        break;
                    }
                    int removeCrewX = Integer.parseInt(tokenizer.nextToken());
                    int removeCrewY = Integer.parseInt(tokenizer.nextToken());
                    controller.removeCrew(currentPlayer.getNickname(), new Coordinate(removeCrewX, removeCrewY));
                    break;

                case "ready":
                    controller.ready(currentPlayer.getNickname());
                    break;

                case "next":
                    controller.playNextCard(currentPlayer.getNickname());
                    break;

                case "early":
                    controller.earlyLanding(currentPlayer.getNickname());
                    break;

                case "choice":
                    if (!tokenizer.hasMoreTokens()) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/choice <true/false>"));
                        break;
                    }
                    boolean choice = Boolean.parseBoolean(tokenizer.nextToken());
                    controller.choice(currentPlayer.getNickname(), choice);
                    break;

                case "choiceBox":
                    if (!tokenizer.hasMoreTokens()) {
                        reportError("error.reading.input.syntax", Map.of("syntax","/choiceBox <true/false>"));
                        break;
                    }
                    boolean choiceBox = Boolean.parseBoolean(tokenizer.nextToken());
                    controller.choiceBox(currentPlayer.getNickname(), choiceBox);
                    break;

                case "moveBox":
                    if (tokenizer.countTokens() < 6) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/moveBox <startX> <startY> <endX> <endY> <boxType> <on/off>"));
                        break;
                    }
                    int startX = Integer.parseInt(tokenizer.nextToken());
                    int startY = Integer.parseInt(tokenizer.nextToken());
                    int endX = Integer.parseInt(tokenizer.nextToken());
                    int endY = Integer.parseInt(tokenizer.nextToken());
                    BoxType boxType = BoxType.valueOf(tokenizer.nextToken().toUpperCase());
                    boolean on = tokenizer.nextToken().equalsIgnoreCase("on");
                    controller.moveBox(currentPlayer.getNickname(), new Coordinate(startX, startY), new Coordinate(endX, endY), boxType, on);
                    break;

                case "choicePlanet":
                    if (!tokenizer.hasMoreTokens()) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/choicePlanet <index>"));
                        break;
                    }
                    int planetIndex = Integer.parseInt(tokenizer.nextToken());
                    controller.choicePlanet(currentPlayer.getNickname(), planetIndex);
                    break;

                case "choiceDoublemotor":
                    processChoiceDoubleMotor(tokenizer);
                    break;

                case "choiceDoublecannon":
                    processChoiceDoubleCannon(tokenizer);
                    break;

                case "choiceCrew":
                    controller.choiceCrew(currentPlayer.getNickname());
                    break;

                case "removeBox":
                    if (tokenizer.countTokens() < 3) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/removeBox <x> <y> <boxType>"));
                        break;
                    }
                    int boxX = Integer.parseInt(tokenizer.nextToken());
                    int boxY = Integer.parseInt(tokenizer.nextToken());
                    BoxType removeBoxType = BoxType.valueOf(tokenizer.nextToken().toUpperCase());
                    controller.removeBox(currentPlayer.getNickname(), new Coordinate(boxX, boxY), removeBoxType);
                    break;

                case "removeBattery":
                    if (tokenizer.countTokens() < 2) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/removeBattery <x> <y>"));
                        break;
                    }
                    int batteryX = Integer.parseInt(tokenizer.nextToken());
                    int batteryY = Integer.parseInt(tokenizer.nextToken());
                    controller.removeBattery(currentPlayer.getNickname(), new Coordinate(batteryX, batteryY));
                    break;

                case "roll":
                    controller.rollDice(currentPlayer.getNickname());
                    break;

                case "damage":
                    if (tokenizer.countTokens() < 2) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/damage <x> <y>"));
                        break;
                    }
                    int damageX = Integer.parseInt(tokenizer.nextToken());
                    int damageY = Integer.parseInt(tokenizer.nextToken());
                    controller.calculateDamage(currentPlayer.getNickname(), new Coordinate(damageX, damageY));
                    break;

                case "keepBlock":
                    if (tokenizer.countTokens() < 2) {
                        reportError("error.reading.input.syntax", Map.of("syntax", "/keepBlock <x> <y>"));
                        break;
                    }
                    int keepX = Integer.parseInt(tokenizer.nextToken());
                    int keepY = Integer.parseInt(tokenizer.nextToken());
                    controller.keepBlock(currentPlayer.getNickname(), new Coordinate(keepX, keepY));
                    break;

                default:
                    reportError("error.reading.notFound", Map.of("command", command));
                    break;
            }
        } catch (RemoteException e) {
            reportError("error.connection.command", null);
        }
    }

    //Elabora il comando per la scelta di motori doppi
    private void processChoiceDoubleMotor(StringTokenizer tokenizer) {
        try {
            // Formato: /choiceDoubleMotor <numMotors> <motor1X> <motor1Y> ... <numBatteries> <battery1X> <battery1Y> ...
            if (!tokenizer.hasMoreTokens()) {
                reportError("error.reading.input.syntax", Map.of("syntax", "/choiceDoubleMotor <numMotors> <motor1X> <motor1Y> ... <numBatteries> <battery1X> <battery1Y> ..."));
                return;
            }

            int numMotors = Integer.parseInt(tokenizer.nextToken());
            List<Coordinate> motors = new ArrayList<>();

            for (int i = 0; i < numMotors; i++) {
                if (tokenizer.countTokens() < 2) {
                    reportError("error.menu.data", Map.of("object", "motors"));
                    return;
                }
                int motorX = Integer.parseInt(tokenizer.nextToken());
                int motorY = Integer.parseInt(tokenizer.nextToken());
                motors.add(new Coordinate(motorX, motorY));
            }

            if (!tokenizer.hasMoreTokens()) {
                reportError("error.reading.input.syntax", Map.of("syntax", "/choiceDoubleMotor <numMotors> <motor1X> <motor1Y> ... <numBatteries> <battery1X> <battery1Y> ..."));
                return;
            }

            int numBatteries = Integer.parseInt(tokenizer.nextToken());
            List<Coordinate> batteries = new ArrayList<>();

            for (int i = 0; i < numBatteries; i++) {
                if (tokenizer.countTokens() < 2) {
                    reportError("error.menu.data", Map.of("object", "batteries"));
                    return;
                }
                int batteryX = Integer.parseInt(tokenizer.nextToken());
                int batteryY = Integer.parseInt(tokenizer.nextToken());
                batteries.add(new Coordinate(batteryX, batteryY));
            }

            controller.choiceDoubleMotor(currentPlayer.getNickname(), motors, batteries);

        } catch (NumberFormatException e) {
            reportError("error.reading.format", null);
        } catch (RemoteException e) {
            reportError("error.connection.command", null);
        }
    }

    //Elabora il comando per la scelta di cannoni doppi
    private void processChoiceDoubleCannon(StringTokenizer tokenizer) {
        try {
            // Formato: /choiceDoubleCannon <numCannons> <cannon1X> <cannon1Y> ... <numBatteries> <battery1X> <battery1Y> ...
            if (!tokenizer.hasMoreTokens()) {
                reportError("error.reading.input.syntax", Map.of("syntax", "/choiceDoubleCannon <numCannons> <cannon1X> <cannon1Y> ... <numBatteries> <battery1X> <battery1Y> ..."));
                return;
            }

            int numCannons = Integer.parseInt(tokenizer.nextToken());
            List<Coordinate> cannons = new ArrayList<>();

            for (int i = 0; i < numCannons; i++) {
                if (tokenizer.countTokens() < 2) {
                    reportError("error.menu.data", Map.of("object", "cannons"));
                    return;
                }
                int cannonX = Integer.parseInt(tokenizer.nextToken());
                int cannonY = Integer.parseInt(tokenizer.nextToken());
                cannons.add(new Coordinate(cannonX, cannonY));
            }

            if (!tokenizer.hasMoreTokens()) {
                reportError("error.reading.input.syntax", Map.of("syntax", "/choiceDoubleCannon <numCannons> <cannon1X> <cannon1Y> ... <numBatteries> <battery1X> <battery1Y> ..."));
                return;
            }

            int numBatteries = Integer.parseInt(tokenizer.nextToken());
            List<Coordinate> batteries = new ArrayList<>();

            for (int i = 0; i < numBatteries; i++) {
                if (tokenizer.countTokens() < 2) {
                    reportError("error.menu.data", Map.of("object", "batteries"));
                    return;
                }
                int batteryX = Integer.parseInt(tokenizer.nextToken());
                int batteryY = Integer.parseInt(tokenizer.nextToken());
                batteries.add(new Coordinate(batteryX, batteryY));
            }

            controller.choiceDoubleCannon(currentPlayer.getNickname(), cannons, batteries);

        } catch (NumberFormatException e) {
            reportError("error.reading.format", null);
        } catch (RemoteException e) {
            reportError("error.connection.command", null);
        }
    }

    //Metodo per ottenere una Tile in base all'ID
    //Questo metodo dovrebbe essere implementato in base alla tua struttura dati
    private Tile getTileById(int tileId) {
        // Implementazione da completare
        // Questo è solo un metodo placeholder
        return null;
    }

    //Mostra un messaggio all'utente
    @Override
    public void displayMessage(String keys, Map<String, String> params) {
        System.out.println(messManager.getMessageWithParams(keys, params));
    }

    @Override
    public void reportError(String keys, Map<String, String> params) {
        System.err.println(messManager.getMessageWithParams(keys, params));
    }

    //Mostra lo stato corrente del gioco
    public void displayGameState(String gameState) {
        displayMessage("info.gameState", Map.of("gameState", gameState));
    }
}