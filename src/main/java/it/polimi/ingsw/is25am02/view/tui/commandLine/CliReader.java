package it.polimi.ingsw.is25am02.view.tui.commandLine;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.model.Coordinate;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*
 * Classe che gestisce l'interfaccia testuale (TUI) del gioco.
 * Si occupa di ricevere i comandi dall'utente e inoltrarli al controller client.
 */
public class CliReader implements Runnable {
    private final ClientController clientController;

    //è il posto in cui leggo
    private final BufferedReader reader;
    private boolean running;
    private Player currentPlayer;

    public CliReader(ClientController clientController) {
        //
        this.clientController = clientController;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.running = false;
    }

    /**
     * Imposta il giocatore corrente
     * @param player il giocatore corrente
     */
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
        System.out.println("/create <maxPlayers> <color> <level> - Crea una nuova lobby");
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

        try {
            switch (command) {
                case "help":
                    printHelp();
                    break;

                case "exit":
                    System.out.println("Uscita dal gioco...");
                    running = false;

                    //todo chiamata del metodo che contiene la disconnessione del server e la fine della partita da parte del giocatore di questo client
                    System.exit(0); //chiudo tutto, baracca e burattini
                    break;

                case "login":
                    if (!tokenizer.hasMoreTokens()) {
                        //potremmo far saltar fuori questa riga solo tipo se viene passato il comando di debug
                        //System.out.println("Formato corretto: /login <nickname>");
                        break;
                    }
                    String nickname = tokenizer.nextToken();
                    if(clientController.registerNickname(nickname)==0) {
                        System.out.println("Nickname registrato con successo: " + nickname);
                    }
                    else {
                        System.out.println("Nickname già in uso, prova con un altro.");
                    }

                    /* todo come gestiamo il fatto che un username è già stato preso? mi spiego:
                     *> andrebbe visualizzato all'utente un messaggio tipo
                     *> "l'username che hai inserito è già stato preso".
                     *> Lo facciamo visualizzare da questa classe, facendo tornare
                     *> da clientController un codice di errore?
                     *> oppure ClientController stampa direttamente a video
                     *> ciò che va/non va? opterei (ho optato per) la prima soluzione,
                     *> in modo che la gestione dell'interazione con l'utente
                     *> sia un'esclusiva di questa classe.
                     *
                     *> NOOOO, forse sto sbagliando tutto. gli aggiornamenti dovrebbero
                     *> arrivare non come ritorno del metodo, ma come metodi a parte... HOW
                     *> dovrebbe esserci un metodo che scrive a schermo i messaggi di errore
                     *> provenienti dal server
                     *>>>>> questa potrebbe essere una soluzione intelligente
                     */
                    break;

                case "create":
                    if (tokenizer.countTokens() < 3) {
                        System.out.println("Formato corretto: /create <maxPlayers> <color> <level>");
                        break;
                    }
                    int maxPlayers = Integer.parseInt(tokenizer.nextToken());
                    PlayerColor color = PlayerColor.valueOf(tokenizer.nextToken().toUpperCase());
                    int level = Integer.parseInt(tokenizer.nextToken());
                    clientController.createLobby(maxPlayers, color, level);
                    break;

                case "lobbies":
                    clientController.getLobbies();
                    break;

                case "join":
                    if (tokenizer.countTokens() < 2) {
                        System.out.println("Formato corretto: /join <lobbyId> <color>");
                        break;
                    }
                    int lobbyId = Integer.parseInt(tokenizer.nextToken());
                    PlayerColor joinColor = PlayerColor.valueOf(tokenizer.nextToken().toUpperCase());
                    clientController.joinLobby(lobbyId, joinColor);
                    break;

                case "hourglass":
                    clientController.flipHourglass();
                    break;

                case "take":
                    clientController.takeTile();
                    break;

                case "takeTile":
                    if (!tokenizer.hasMoreTokens()) {
                        System.out.println("Formato corretto: /takeTile <id>");
                        break;
                    }
                    int tileId = Integer.parseInt(tokenizer.nextToken());
                    // Qui dovresti ottenere l'oggetto Tile corrispondente a tileId
                    Tile tile = getTileById(tileId); // Questo metodo dovrebbe essere implementato
                    clientController.takeTile(tile);
                    break;

                case "takeMiniDeck":
                    if (!tokenizer.hasMoreTokens()) {
                        System.out.println("Formato corretto: /takeMiniDeck <index>");
                        break;
                    }
                    int miniDeckIndex = Integer.parseInt(tokenizer.nextToken());
                    clientController.takeMiniDeck(miniDeckIndex);
                    break;

                case "returnMiniDeck":
                    clientController.returnMiniDeck();
                    break;

                case "book":
                    clientController.bookTile();
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
                    clientController.addBookedTile(index, new Coordinate(x, y), rotation);
                    break;

                case "return":
                    clientController.returnTile();
                    break;

                case "add":
                    if (tokenizer.countTokens() < 3) {
                        System.out.println("Formato corretto: /add <x> <y> <rotation>");
                        break;
                    }
                    int addX = Integer.parseInt(tokenizer.nextToken());
                    int addY = Integer.parseInt(tokenizer.nextToken());
                    RotationType addRotation = RotationType.valueOf(tokenizer.nextToken().toUpperCase());
                    clientController.addTile(new Coordinate(addX, addY), addRotation);
                    break;

                case "finish":
                    clientController.shipFinished();
                    break;

                case "check":
                    clientController.checkSpaceship();
                    break;

                case "remove":
                    if (tokenizer.countTokens() < 2) {
                        System.out.println("Formato corretto: /remove <x> <y>");
                        break;
                    }
                    int removeX = Integer.parseInt(tokenizer.nextToken());
                    int removeY = Integer.parseInt(tokenizer.nextToken());
                    clientController.removeTile(new Coordinate(removeX, removeY));
                    break;

                case "addCrew":
                    if (tokenizer.countTokens() < 3) {
                        System.out.println("Formato corretto: /addCrew <x> <y> <type>");
                        break;
                    }
                    int crewX = Integer.parseInt(tokenizer.nextToken());
                    int crewY = Integer.parseInt(tokenizer.nextToken());
                    AliveType type = AliveType.valueOf(tokenizer.nextToken().toUpperCase());
                    clientController.addCrew(new Coordinate(crewX, crewY), type);
                    break;

                case "removeCrew":
                    if (tokenizer.countTokens() < 2) {
                        System.out.println("Formato corretto: /removeCrew <x> <y>");
                        break;
                    }
                    int removeCrewX = Integer.parseInt(tokenizer.nextToken());
                    int removeCrewY = Integer.parseInt(tokenizer.nextToken());
                    clientController.removeCrew(new Coordinate(removeCrewX, removeCrewY));
                    break;

                case "ready":
                    clientController.ready();
                    break;

                case "next":
                    clientController.playNextCard();
                    break;

                case "early":
                    clientController.earlyLanding();
                    break;

                case "choice":
                    if (!tokenizer.hasMoreTokens()) {
                        System.out.println("Formato corretto: /choice <true/false>");
                        break;
                    }
                    boolean choice = Boolean.parseBoolean(tokenizer.nextToken());
                    clientController.choice(choice);
                    break;

                case "choiceBox":
                    if (!tokenizer.hasMoreTokens()) {
                        System.out.println("Formato corretto: /choiceBox <true/false>");
                        break;
                    }
                    boolean choiceBox = Boolean.parseBoolean(tokenizer.nextToken());
                    clientController.choiceBox(choiceBox);
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
                    clientController.moveBox(new Coordinate(startX, startY), new Coordinate(endX, endY), boxType, on);
                    break;

                case "choicePlanet":
                    if (!tokenizer.hasMoreTokens()) {
                        System.out.println("Formato corretto: /choicePlanet <index>");
                        break;
                    }
                    int planetIndex = Integer.parseInt(tokenizer.nextToken());
                    clientController.choicePlanet(planetIndex);
                    break;

                case "choiceDoublemotor":
                    processChoiceDoubleMotor(tokenizer);
                    break;

                case "choiceDoublecannon":
                    processChoiceDoubleCannon(tokenizer);
                    break;

                case "choiceCrew":
                    clientController.choiceCrew();
                    break;

                case "removeBox":
                    if (tokenizer.countTokens() < 3) {
                        System.out.println("Formato corretto: /removeBox <x> <y> <boxType>");
                        break;
                    }
                    int boxX = Integer.parseInt(tokenizer.nextToken());
                    int boxY = Integer.parseInt(tokenizer.nextToken());
                    BoxType removeBoxType = BoxType.valueOf(tokenizer.nextToken().toUpperCase());
                    clientController.removeBox(new Coordinate(boxX, boxY), removeBoxType);
                    break;

                case "removeBattery":
                    if (tokenizer.countTokens() < 2) {
                        System.out.println("Formato corretto: /removeBattery <x> <y>");
                        break;
                    }
                    int batteryX = Integer.parseInt(tokenizer.nextToken());
                    int batteryY = Integer.parseInt(tokenizer.nextToken());
                    clientController.removeBattery(new Coordinate(batteryX, batteryY));
                    break;

                case "roll":
                    clientController.rollDice();
                    break;

                case "damage":
                    if (tokenizer.countTokens() < 2) {
                        System.out.println("Formato corretto: /damage <x> <y>");
                        break;
                    }
                    int damageX = Integer.parseInt(tokenizer.nextToken());
                    int damageY = Integer.parseInt(tokenizer.nextToken());
                    clientController.calculateDamage(new Coordinate(damageX, damageY));
                    break;

                case "keepBlock":
                    if (tokenizer.countTokens() < 2) {
                        System.out.println("Formato corretto: /keepBlock <x> <y>");
                        break;
                    }
                    int keepX = Integer.parseInt(tokenizer.nextToken());
                    int keepY = Integer.parseInt(tokenizer.nextToken());
                    clientController.keepBlock(new Coordinate(keepX, keepY));
                    break;

                default:
                    System.out.println("Comando non riconosciuto: " + command);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Errore durante l'esecuzione del comando: " + e.getMessage());
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
                    System.out.println("Dati insufficienti per i motori");
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

            clientController.choiceDoubleMotor(motors, batteries);

        } catch (NumberFormatException e) {
            System.out.println("Errore di formato numerico: " + e.getMessage());
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

            clientController.choiceDoubleCannon(cannons, batteries);

        } catch (NumberFormatException e) {
            System.out.println("Errore di formato numerico: " + e.getMessage());
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
    public void displayMessage(String message) {
        System.out.println(message);
    }

    //Mostra lo stato corrente del gioco
    public void displayGameState(String gameState) {
        System.out.println("\n--- Stato del gioco ---");
        System.out.println(gameState);
        System.out.println("----------------------\n");
    }
}