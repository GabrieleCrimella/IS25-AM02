package it.polimi.ingsw.is25am02.view.tui.commandLine;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.model.Coordinate;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.view.ConsoleClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*
 * Classe che gestisce l'interfaccia testuale (TUI) del gioco.
 * Si occupa di ricevere i comandi dall'utente e inoltrarli al controller client.
 */

//todo tutti i Player vari non ha senso metterli come riferimenti di classi del model!!! usiamo il nickname e stop, oppure una struttura interna alla view
public class TuiConsole implements Runnable, ConsoleClient {
    private ClientController controller;

    //è il posto in cui leggo
    private final BufferedReader reader;
    private boolean running;
    private Player currentPlayer;

    public TuiConsole() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.running = false;
    }

    public void closeConnect() {
        try {
            controller.closeConnect();
        } catch (Exception e) {
            reportError("Error closing connection" + e.getMessage());
        }
    }

    public void setController(ClientController control) {
        controller = control;
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

    /**
     * Ferma l'interfaccia testuale
     */
    public void stop() {
        running = false;
    }

    /**
     * Mostra il messaggio di benvenuto all'avvio del gioco
     */
    private void printWelcomeMessage() {
        System.out.println("========================================");
        System.out.println("=       Galaxy Trucker (CLI)           =");
        System.out.println("========================================");
        printHelp();
    }

    /**
     * Mostra l'elenco dei comandi disponibili
     */
    private void printHelp() {
        System.out.println("\nComandi disponibili:");
        System.out.println("/help - Mostra questa lista di comandi");
        System.out.println("/exit - Esci dal gioco");
        System.out.println("/login <nickname> - Registra il tuo nickname");
        System.out.println("/create <nickname> <maxPlayers> <color> <level> - Crea una nuova lobby");
        System.out.println("/lobbies - Mostra le lobby disponibili");
        System.out.println("/join <lobbyId> <color> - Unisciti a una lobby esistente");
        System.out.println("/ready - Segna il giocatore come pronto");

        System.out.println("\nComandi di gioco:");
        System.out.println("/hourglass - Gira la clessidra");
        System.out.println("/take - Prendi una tessera");
        System.out.println("/takeTile <id> - Prendi una tessera specifica");
        System.out.println("/takeMiniDeck <index> - Prendi un mini-mazzo");
        System.out.println("/returnMiniDeck - Restituisci il mini-mazzo");
        System.out.println("/book - Prenota una tessera");
        System.out.println("/addBooked <index> <x> <y> <rotation> - Aggiungi tessera prenotata");
        System.out.println("/return - Restituisci una tessera");
        System.out.println("/add <x> <y> <rotation> - Aggiungi tessera alla nave");
        System.out.println("/remove <x> <y> - Rimuovi tessera dalla posizione");
        System.out.println("/finish - Segnala che hai terminato la nave");
        System.out.println("/check - Controlla la correttezza della nave");
        System.out.println("/addCrew <x> <y> <type> - Aggiungi membro dell'equipaggio");
        System.out.println("/removeCrew <x> <y> - Rimuovi membro dell'equipaggio");
        System.out.println("/next - Gioca la prossima carta");
        System.out.println("/early - Atterraggio anticipato");
        System.out.println("/choice <true/false> - Fai una scelta");
        System.out.println("/choiceBox <true/false> - Fai una scelta riguardo una scatola");
        System.out.println("/moveBox <startX> <startY> <endX> <endY> <boxType> <on/off> - Muovi una scatola");
        System.out.println("/choicePlanet <index> - Scegli un pianeta");
        System.out.println("/roll - Lancia i dadi");
        System.out.println("/damage <x> <y> - Calcola danno alla posizione");
        System.out.println("/keepBlock <x> <y> - Mantieni blocco alla posizione");

        System.out.println("\nPer altri comandi specifici, consulta la documentazione del gioco.");
    }

    /**
     * Metodo principale del thread che gestisce l'input dell'utente
     */
    @Override
    public void run() {
        printWelcomeMessage();

        while (running) {
            try {
                System.out.print("> ");
                String input = reader.readLine();

                if (input == null || input.trim().isEmpty()) {
                    continue;
                }

                processCommand(input.trim());

            } catch (IOException e) {
                System.err.println("Errore durante la lettura dell'input: " + e.getMessage());
            }
        }
    }

    //Elabora il comando inserito dall'utente
    private void processCommand(String input) {
        if (!input.startsWith("/")) {
            System.out.println("Commands must starts with '/'");
            return;
        }

        // Remove the initial '/'
        input = input.substring(1);

        // Divide the command in tokens
        StringTokenizer tokenizer = new StringTokenizer(input);
        if (!tokenizer.hasMoreTokens()) {
            return;
        }

        String command = tokenizer.nextToken().toLowerCase();
        String nickname = "";

        try {
            switch (command) {
                case "help":
                    printHelp();
                    break;

                case "exit":
                    System.out.println("Uscita dal gioco...");

                    //Stop connection and UI threads
                    stop();
                    closeConnect();
                    System.exit(0);
                    break;

                case "login":
                    if (!tokenizer.hasMoreTokens()) {
                        //potremmo far saltar fuori questa riga solo tipo se viene passato il comando di debug... eureka!!
                        //System.out.println("Formato corretto: /login <nickname>");
                        break;
                    }
                    nickname = tokenizer.nextToken();
                    controller.nicknameRegistration(nickname, controller.getVirtualView());
                    break;

                case "create":
                    if (tokenizer.countTokens() < 4) {
                        System.out.println("Formato corretto: /create <nickname> <maxPlayers> <color> <level>");
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
                        System.out.println("Formato corretto: /join <lobbyId> <color>");
                        break;
                    }
                    int lobbyId = Integer.parseInt(tokenizer.nextToken());
                    PlayerColor joinColor = PlayerColor.valueOf(tokenizer.nextToken().toUpperCase());
                    controller.joinLobby(controller.getVirtualView(), lobbyId, nickname, joinColor);
                    break;

                case "hourglass":
                    controller.flipHourglass(currentPlayer);
                    break;

                case "take":
                    controller.takeTile(currentPlayer);
                    break;

                case "takeTile":
                    if (!tokenizer.hasMoreTokens()) {
                        System.out.println("Formato corretto: /takeTile <id>");
                        break;
                    }
                    int tileId = Integer.parseInt(tokenizer.nextToken());
                    // Qui dovresti ottenere l'oggetto Tile corrispondente a tileId
                    Tile tile = getTileById(tileId); // Questo metodo dovrebbe essere implementato
                    controller.takeTile(currentPlayer, tile);
                    break;

                case "takeMiniDeck":
                    if (!tokenizer.hasMoreTokens()) {
                        System.out.println("Formato corretto: /takeMiniDeck <index>");
                        break;
                    }
                    int miniDeckIndex = Integer.parseInt(tokenizer.nextToken());
                    controller.takeMiniDeck(currentPlayer, miniDeckIndex);
                    break;

                case "returnMiniDeck":
                    controller.returnMiniDeck(currentPlayer);
                    break;

                case "book":
                    controller.bookTile(currentPlayer);
                    break;

                case "addBooked":
                    if (tokenizer.countTokens() < 4) {
                        System.out.println("Formato corretto: /addBooked <index> <x> <y> <rotation>");
                        break;
                    }
                    int index = Integer.parseInt(tokenizer.nextToken());
                    int x = Integer.parseInt(tokenizer.nextToken());
                    int y = Integer.parseInt(tokenizer.nextToken());
                    RotationType rotation = RotationType.valueOf(tokenizer.nextToken().toUpperCase());
                    controller.addBookedTile(currentPlayer, index, new Coordinate(x, y), rotation);
                    break;

                case "return":
                    controller.returnTile(currentPlayer);
                    break;

                case "add":
                    if (tokenizer.countTokens() < 3) {
                        System.out.println("Formato corretto: /add <x> <y> <rotation>");
                        break;
                    }
                    int addX = Integer.parseInt(tokenizer.nextToken());
                    int addY = Integer.parseInt(tokenizer.nextToken());
                    RotationType addRotation = RotationType.valueOf(tokenizer.nextToken().toUpperCase());
                    controller.addTile(currentPlayer, new Coordinate(addX, addY), addRotation);
                    break;

                case "finish":
                    controller.shipFinished(currentPlayer);
                    break;

                case "check":
                    controller.checkSpaceship(currentPlayer);
                    break;

                case "remove":
                    if (tokenizer.countTokens() < 2) {
                        System.out.println("Formato corretto: /remove <x> <y>");
                        break;
                    }
                    int removeX = Integer.parseInt(tokenizer.nextToken());
                    int removeY = Integer.parseInt(tokenizer.nextToken());
                    controller.removeTile(currentPlayer, new Coordinate(removeX, removeY));
                    break;

                case "addCrew":
                    if (tokenizer.countTokens() < 3) {
                        System.out.println("Formato corretto: /addCrew <x> <y> <type>");
                        break;
                    }
                    int crewX = Integer.parseInt(tokenizer.nextToken());
                    int crewY = Integer.parseInt(tokenizer.nextToken());
                    AliveType type = AliveType.valueOf(tokenizer.nextToken().toUpperCase());
                    controller.addCrew(currentPlayer, new Coordinate(crewX, crewY), type);
                    break;

                case "removeCrew":
                    if (tokenizer.countTokens() < 2) {
                        System.out.println("Formato corretto: /removeCrew <x> <y>");
                        break;
                    }
                    int removeCrewX = Integer.parseInt(tokenizer.nextToken());
                    int removeCrewY = Integer.parseInt(tokenizer.nextToken());
                    controller.removeCrew(currentPlayer, new Coordinate(removeCrewX, removeCrewY));
                    break;

                case "ready":
                    controller.ready(currentPlayer);
                    break;

                case "next":
                    controller.playNextCard(currentPlayer);
                    break;

                case "early":
                    controller.earlyLanding(currentPlayer);
                    break;

                case "choice":
                    if (!tokenizer.hasMoreTokens()) {
                        System.out.println("Formato corretto: /choice <true/false>");
                        break;
                    }
                    boolean choice = Boolean.parseBoolean(tokenizer.nextToken());
                    controller.choice(currentPlayer, choice);
                    break;

                case "choiceBox":
                    if (!tokenizer.hasMoreTokens()) {
                        System.out.println("Formato corretto: /choiceBox <true/false>");
                        break;
                    }
                    boolean choiceBox = Boolean.parseBoolean(tokenizer.nextToken());
                    controller.choiceBox(currentPlayer, choiceBox);
                    break;

                case "moveBox":
                    if (tokenizer.countTokens() < 6) {
                        System.out.println("Formato corretto: /moveBox <startX> <startY> <endX> <endY> <boxType> <on/off>");
                        break;
                    }
                    int startX = Integer.parseInt(tokenizer.nextToken());
                    int startY = Integer.parseInt(tokenizer.nextToken());
                    int endX = Integer.parseInt(tokenizer.nextToken());
                    int endY = Integer.parseInt(tokenizer.nextToken());
                    BoxType boxType = BoxType.valueOf(tokenizer.nextToken().toUpperCase());
                    boolean on = tokenizer.nextToken().equalsIgnoreCase("on");
                    controller.moveBox(currentPlayer, new Coordinate(startX, startY), new Coordinate(endX, endY), boxType, on);
                    break;

                case "choicePlanet":
                    if (!tokenizer.hasMoreTokens()) {
                        System.out.println("Formato corretto: /choicePlanet <index>");
                        break;
                    }
                    int planetIndex = Integer.parseInt(tokenizer.nextToken());
                    controller.choicePlanet(currentPlayer, planetIndex);
                    break;

                case "choiceDoublemotor":
                    processChoiceDoubleMotor(tokenizer);
                    break;

                case "choiceDoublecannon":
                    processChoiceDoubleCannon(tokenizer);
                    break;

                case "choiceCrew":
                    controller.choiceCrew(currentPlayer);
                    break;

                case "removeBox":
                    if (tokenizer.countTokens() < 3) {
                        System.out.println("Formato corretto: /removeBox <x> <y> <boxType>");
                        break;
                    }
                    int boxX = Integer.parseInt(tokenizer.nextToken());
                    int boxY = Integer.parseInt(tokenizer.nextToken());
                    BoxType removeBoxType = BoxType.valueOf(tokenizer.nextToken().toUpperCase());
                    controller.removeBox(currentPlayer, new Coordinate(boxX, boxY), removeBoxType);
                    break;

                case "removeBattery":
                    if (tokenizer.countTokens() < 2) {
                        System.out.println("Formato corretto: /removeBattery <x> <y>");
                        break;
                    }
                    int batteryX = Integer.parseInt(tokenizer.nextToken());
                    int batteryY = Integer.parseInt(tokenizer.nextToken());
                    controller.removeBattery(currentPlayer, new Coordinate(batteryX, batteryY));
                    break;

                case "roll":
                    controller.rollDice(currentPlayer);
                    break;

                case "damage":
                    if (tokenizer.countTokens() < 2) {
                        System.out.println("Formato corretto: /damage <x> <y>");
                        break;
                    }
                    int damageX = Integer.parseInt(tokenizer.nextToken());
                    int damageY = Integer.parseInt(tokenizer.nextToken());
                    controller.calculateDamage(currentPlayer, new Coordinate(damageX, damageY));
                    break;

                case "keepBlock":
                    if (tokenizer.countTokens() < 2) {
                        System.out.println("Formato corretto: /keepBlock <x> <y>");
                        break;
                    }
                    int keepX = Integer.parseInt(tokenizer.nextToken());
                    int keepY = Integer.parseInt(tokenizer.nextToken());
                    controller.keepBlock(currentPlayer, new Coordinate(keepX, keepY));
                    break;

                default:
                    System.out.println("Comando non riconosciuto: " + command);
                    break;
            }
        } catch (RemoteException e) {
            System.err.println("Error executing command:" + e.getMessage());
        }
    }

    //Elabora il comando per la scelta di motori doppi
    private void processChoiceDoubleMotor(StringTokenizer tokenizer) {
        try {
            // Formato: /choiceDoubleMotor <numMotors> <motor1X> <motor1Y> ... <numBatteries> <battery1X> <battery1Y> ...
            if (!tokenizer.hasMoreTokens()) {
                System.out.println("Formato corretto: /choiceDoubleMotor <numMotors> <motor1X> <motor1Y> ... <numBatteries> <battery1X> <battery1Y> ...");
                return;
            }

            int numMotors = Integer.parseInt(tokenizer.nextToken());
            List<Coordinate> motors = new ArrayList<>();

            for (int i = 0; i < numMotors; i++) {
                if (tokenizer.countTokens() < 2) {
                    System.err.println("Dati insufficienti per i motori");
                    return;
                }
                int motorX = Integer.parseInt(tokenizer.nextToken());
                int motorY = Integer.parseInt(tokenizer.nextToken());
                motors.add(new Coordinate(motorX, motorY));
            }

            if (!tokenizer.hasMoreTokens()) {
                System.out.println("Formato corretto: /choiceDoubleMotor <numMotors> <motor1X> <motor1Y> ... <numBatteries> <battery1X> <battery1Y> ...");
                return;
            }

            int numBatteries = Integer.parseInt(tokenizer.nextToken());
            List<Coordinate> batteries = new ArrayList<>();

            for (int i = 0; i < numBatteries; i++) {
                if (tokenizer.countTokens() < 2) {
                    System.out.println("Dati insufficienti per le batterie");
                    return;
                }
                int batteryX = Integer.parseInt(tokenizer.nextToken());
                int batteryY = Integer.parseInt(tokenizer.nextToken());
                batteries.add(new Coordinate(batteryX, batteryY));
            }

            controller.choiceDoubleMotor(currentPlayer, motors, batteries);

        } catch (NumberFormatException e) {
            System.err.println("Errore di formato numerico: " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("Error executing command:" + e.getMessage());
        }
    }

    //Elabora il comando per la scelta di cannoni doppi
    private void processChoiceDoubleCannon(StringTokenizer tokenizer) {
        try {
            // Formato: /choiceDoubleCannon <numCannons> <cannon1X> <cannon1Y> ... <numBatteries> <battery1X> <battery1Y> ...
            if (!tokenizer.hasMoreTokens()) {
                System.out.println("Formato corretto: /choiceDoubleCannon <numCannons> <cannon1X> <cannon1Y> ... <numBatteries> <battery1X> <battery1Y> ...");
                return;
            }

            int numCannons = Integer.parseInt(tokenizer.nextToken());
            List<Coordinate> cannons = new ArrayList<>();

            for (int i = 0; i < numCannons; i++) {
                if (tokenizer.countTokens() < 2) {
                    System.out.println("Dati insufficienti per i cannoni");
                    return;
                }
                int cannonX = Integer.parseInt(tokenizer.nextToken());
                int cannonY = Integer.parseInt(tokenizer.nextToken());
                cannons.add(new Coordinate(cannonX, cannonY));
            }

            if (!tokenizer.hasMoreTokens()) {
                System.out.println("Formato corretto: /choiceDoubleCannon <numCannons> <cannon1X> <cannon1Y> ... <numBatteries> <battery1X> <battery1Y> ...");
                return;
            }

            int numBatteries = Integer.parseInt(tokenizer.nextToken());
            List<Coordinate> batteries = new ArrayList<>();

            for (int i = 0; i < numBatteries; i++) {
                if (tokenizer.countTokens() < 2) {
                    System.out.println("Dati insufficienti per le batterie");
                    return;
                }
                int batteryX = Integer.parseInt(tokenizer.nextToken());
                int batteryY = Integer.parseInt(tokenizer.nextToken());
                batteries.add(new Coordinate(batteryX, batteryY));
            }

            controller.choiceDoubleCannon(currentPlayer, cannons, batteries);

        } catch (NumberFormatException e) {
            System.err.println("Errore di formato numerico: " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("Error executing command:" + e.getMessage());
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
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void reportError(String details) {
        System.out.println(details);
    }

    //Mostra lo stato corrente del gioco
    public void displayGameState(String gameState) {
        System.out.println("\n--- Stato del gioco ---");
        System.out.println(gameState);
        System.out.println("----------------------\n");
    }
}