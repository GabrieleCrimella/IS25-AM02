package it.polimi.ingsw.is25am02.network.socket.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.network.rmi.client.RmiClient;
import it.polimi.ingsw.is25am02.network.socket.Command;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.view.ConsoleClient;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerHandler implements Runnable, VirtualServer{
    private final Socket socket;
    private RmiClient rmi;
    private final Gson gson = new Gson();
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = true;

    public ServerHandler(Socket socket, ConsoleClient console) {
        this.socket = socket;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.rmi = new RmiClient();
            rmi.setView(console);
        } catch (IOException e) {
            System.err.println("Errore durante l'esecuzione: " + e.getMessage());
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());

            Object receivedObject;
            // Il ciclo si chiude quando il client si disconnette
            while (running && (receivedObject = in.readObject()) != null) {
                String jsonString = (String) receivedObject;
                try {
                    Command com = gson.fromJson(jsonString, Command.class);
                    processCommand(com);

                } catch (Exception e) {
                    System.err.println("Errore con client1: " + e.getMessage());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            if(running) {
                System.out.println("Il server ha chiuso la connessione");
            }
        } finally {
            closeConnection();
        }
    }

    public void closeConnection() {
        try {
            running = false;
            if (socket != null && !socket.isClosed()) {
                out.close();
                in.close();
                socket.close();
                in = null;
                out = null;
            }
        } catch (IOException e) {
            System.err.println("Errore chiusura socket: " + e.getMessage());
        }
    }

    private void processCommand(Command cmd) {
        try {
            JsonObject params = cmd.getParams();

            switch (cmd.getCommand()) {
                case "heartbeat":
                    rmi.pingFromServer();
                    break;
                case "displayMessage":
                    String keys = gson.fromJson(params.get("keys"), String.class);
                    Type mapType = new TypeToken<Map<String, String>>(){}.getType();
                    Map<String, String> stringParams = gson.fromJson(params.get("params"), mapType);
                    rmi.displayMessage(keys, stringParams);
                    break;
                case "setMenuState":
                    MenuState state = MenuState.valueOf(params.get("state").getAsString());
                    rmi.setMenuState(state);
                    break;
                case "reportError":
                    String errorKeys = gson.fromJson(params.get("keys"), String.class);
                    Type errorMapType = new TypeToken<Map<String, String>>(){}.getType();
                    Map<String, String> errorParams = gson.fromJson(params.get("params"), errorMapType);
                    rmi.reportError(errorKeys, errorParams);
                    break;
                case "setLobbiesView":
                    Type lobbiesType = new TypeToken<Map<Integer, LobbyView>>(){}.getType();
                    Map<Integer, LobbyView> lobbies = gson.fromJson(params.get("lobbies"), lobbiesType);
                    rmi.setLobbiesView(lobbies);
                    break;
                case "setBuildView":
                    int buildLevel = gson.fromJson(params.get("level"), int.class);
                    PlayerColor buildColor = PlayerColor.valueOf(params.get("color").getAsString());
                    rmi.setBuildView(buildLevel, buildColor);
                    break;
                case "setGameView":
                    int gameLevel = gson.fromJson(params.get("level"), int.class);
                    PlayerColor gameColor = PlayerColor.valueOf(params.get("color").getAsString());
                    rmi.setGameView(gameLevel, gameColor);
                    break;
                case "setNickname":
                    String nickname = gson.fromJson(params.get("nickname"), String.class);
                    rmi.setNickname(nickname);
                    break;
                case "showUpdateEverything":
                    int level = gson.fromJson(params.get("level"), int.class);
                    Type playerColorsType = new TypeToken<HashMap<String, PlayerColor>>(){}.getType();
                    HashMap<String, PlayerColor> playercolors = gson.fromJson(params.get("playercolors"), playerColorsType);
                    String currentCardImage = gson.fromJson(params.get("currentCardImage"), String.class);
                    StateCardType stateCard = StateCardType.valueOf(params.get("stateCard").getAsString());
                    CardType type = CardType.valueOf(params.get("type").getAsString());
                    String comment = gson.fromJson(params.get("comment"), String.class);
                    StateGameType stateGame = StateGameType.valueOf(params.get("stateGame").getAsString());
                    String currentPlayer = gson.fromJson(params.get("currentPlayer"), String.class);
                    boolean[][] mask = gson.fromJson(params.get("mask"), boolean[][].class);
                    int[] positions = gson.fromJson(params.get("positions"), int[].class);
                    Type deckType = new TypeToken<HashMap<Integer, List<List<Object>>>>(){}.getType();
                    HashMap<Integer, List<List<Object>>> deck = gson.fromJson(params.get("deck"), deckType);
                    rmi.showUpdateEverything(level, playercolors, currentCardImage, stateCard, type, comment, stateGame, currentPlayer, mask, positions, deck);
                    break;
                case "showTileRemoval":
                    Coordinate removalCoordinate = gson.fromJson(params.get("coordinate"), Coordinate.class);
                    String removalNickname = gson.fromJson(params.get("nickname"), String.class);
                    rmi.showTileRemoval(removalCoordinate, removalNickname);
                    break;
                case "spaceshipBrokenUpdate":
                    String brokenNickname = gson.fromJson(params.get("nickname"), String.class);
                    rmi.spaceshipBrokenUpdate(brokenNickname);
                    break;
                case "showBatteryRemoval":
                    Coordinate batteryCoordinate = gson.fromJson(params.get("coordinate"), Coordinate.class);
                    String batteryNickname = gson.fromJson(params.get("nickname"), String.class);
                    int numBattery = gson.fromJson(params.get("numBattery"), int.class);
                    rmi.showBatteryRemoval(batteryCoordinate, batteryNickname, numBattery);
                    break;
                case "showCrewRemoval":
                    Coordinate crewCoordinate = gson.fromJson(params.get("coordinate"), Coordinate.class);
                    String crewNickname = gson.fromJson(params.get("nickname"), String.class);
                    rmi.showCrewRemoval(crewCoordinate, crewNickname);
                    break;
                case "showBoxUpdate":
                    Coordinate boxCoordinate = gson.fromJson(params.get("coordinate"), Coordinate.class);
                    String boxNickname = gson.fromJson(params.get("nickname"), String.class);
                    Type boxType = new TypeToken<List<BoxType>>(){}.getType();
                    List<BoxType> box = gson.fromJson(params.get("box"), boxType);
                    rmi.showBoxUpdate(boxCoordinate, boxNickname, box);
                    break;
                case "showCreditUpdate":
                    String creditNickname = gson.fromJson(params.get("nickname"), String.class);
                    int cosmicCredits = gson.fromJson(params.get("cosmicCredits"), int.class);
                    rmi.showCreditUpdate(creditNickname, cosmicCredits);
                    break;
                case "showUpdatedOthers":
                    rmi.showUpdatedOthers();
                    break;
                case "showPositionUpdate":
                    String positionNickname = gson.fromJson(params.get("nickname"), String.class);
                    int position = gson.fromJson(params.get("position"), int.class);
                    rmi.showPositionUpdate(positionNickname, position);
                    break;
                case "showHourglassUpdate":
                    long timeLeft = gson.fromJson(params.get("timeLeft"), long.class);
                    rmi.showHourglassUpdate(timeLeft);
                    break;
                case "showDiceUpdate":
                    String diceNickname = gson.fromJson(params.get("nickname"), String.class);
                    int result = gson.fromJson(params.get("result"), int.class);
                    rmi.showDiceUpdate(diceNickname, result);
                    break;
                case "showMinideckUpdate":
                    String minideckNickname = gson.fromJson(params.get("nickname"), String.class);
                    int deckValue = gson.fromJson(params.get("deck"), int.class);
                    rmi.showMinideckUpdate(minideckNickname, deckValue);
                    break;
                case "showCurrentCardUpdate":
                    String imagepath = gson.fromJson(params.get("imagepath"), String.class);
                    StateCardType cardStateCard = StateCardType.valueOf(params.get("stateCard").getAsString());
                    CardType cardType = CardType.valueOf(params.get("type").getAsString());
                    String cardComment = gson.fromJson(params.get("comment"), String.class);
                    rmi.showCurrentCardUpdate(imagepath, cardStateCard, cardType, cardComment);
                    break;
                case "showCurrentTileNullityUpdate":
                    String nullityNickname = gson.fromJson(params.get("nickname"), String.class);
                    rmi.showCurrentTileNullityUpdate(nullityNickname);
                    break;
                case "showCurrentTileUpdate":
                    String tileImagepath = gson.fromJson(params.get("imagepath"), String.class);
                    JsonArray connectorsArray = params.get("connectors").getAsJsonArray();
                    ConnectorType[] connectors = new ConnectorType[connectorsArray.size()];
                    for (int i = 0; i < connectorsArray.size(); i++) {
                        connectors[i] = ConnectorType.valueOf(connectorsArray.get(i).getAsString());
                    }
                    RotationType rotationType = RotationType.valueOf(params.get("rotationType").getAsString());
                    TileType tType = TileType.valueOf(params.get("tType").getAsString());
                    int maxBattery = gson.fromJson(params.get("maxBattery"), int.class);
                    int maxBox = gson.fromJson(params.get("maxBox"), int.class);
                    String tileNickname = gson.fromJson(params.get("nickname"), String.class);
                    rmi.showCurrentTileUpdate(tileImagepath, connectors, rotationType, tType, maxBattery, maxBox, tileNickname);
                    break;
                case "showVisibilityUpdate":
                    String visibilityImagepath = gson.fromJson(params.get("imagepath"), String.class);
                    JsonArray visibilityConnectorsArray = params.get("connectors").getAsJsonArray();
                    ConnectorType[] visibilityConnectors = new ConnectorType[visibilityConnectorsArray.size()];
                    for (int i = 0; i < visibilityConnectorsArray.size(); i++) {
                        visibilityConnectors[i] = ConnectorType.valueOf(visibilityConnectorsArray.get(i).getAsString());
                    }
                    RotationType visibilityRotationType = RotationType.valueOf(params.get("rotationType").getAsString());
                    TileType visibilityTType = TileType.valueOf(params.get("tType").getAsString());
                    int visibilityMaxBattery = gson.fromJson(params.get("maxBattery"), int.class);
                    int visibilityMaxBox = gson.fromJson(params.get("maxBox"), int.class);
                    rmi.showVisibilityUpdate(visibilityImagepath, visibilityConnectors, visibilityRotationType, visibilityTType, visibilityMaxBattery, visibilityMaxBox);
                    break;
                case "showTileRemovalFromHeapTile":
                    String heapImagepath = gson.fromJson(params.get("imagepath"), String.class);
                    rmi.showTileRemovalFromHeapTile(heapImagepath);
                    break;
                case "showDeckAllowUpdate":
                    String player = gson.fromJson(params.get("player"), String.class);
                    rmi.showDeckAllowUpdate(player);
                    break;
                case "showTileAdditionUpdate":
                    String additionImagepath = gson.fromJson(params.get("imagepath"), String.class);
                    JsonArray additionConnectorsArray = params.get("connectors").getAsJsonArray();
                    ConnectorType[] additionConnectors = new ConnectorType[additionConnectorsArray.size()];
                    for (int i = 0; i < additionConnectorsArray.size(); i++) {
                        additionConnectors[i] = ConnectorType.valueOf(additionConnectorsArray.get(i).getAsString());
                    }
                    RotationType additionRotationType = RotationType.valueOf(params.get("rotationType").getAsString());
                    TileType additionTType = TileType.valueOf(params.get("tType").getAsString());
                    int additionMaxBattery = gson.fromJson(params.get("maxBattery"), int.class);
                    int additionMaxBox = gson.fromJson(params.get("maxBox"), int.class);
                    String additionNickname = gson.fromJson(params.get("nickname"), String.class);
                    Coordinate additionCoordinate = gson.fromJson(params.get("coordinate"), Coordinate.class);
                    rmi.showTileAdditionUpdate(additionImagepath, additionConnectors, additionRotationType, additionTType, additionMaxBattery, additionMaxBox, additionNickname, additionCoordinate);
                    break;
                case "showGameStateUpdate":
                    StateGameType newGamestate = StateGameType.valueOf(params.get("newGamestate").getAsString());
                    rmi.showGameStateUpdate(newGamestate);
                    break;
                case "showPlayerStateUpdate":
                    String playerStateNickname = gson.fromJson(params.get("nickname"), String.class);
                    StatePlayerType newPlayerstate = StatePlayerType.valueOf(params.get("newPlayerstate").getAsString());
                    rmi.showPlayerStateUpdate(playerStateNickname, newPlayerstate);
                    break;
                case "showCardStateUpdate":
                    StateCardType newCardstate = StateCardType.valueOf(params.get("newCardstate").getAsString());
                    rmi.showCardStateUpdate(newCardstate);
                    break;
                case "showCurrentPlayerUpdate":
                    String currentPlayerNickname = gson.fromJson(params.get("nickname"), String.class);
                    rmi.showCurrentPlayerUpdate(currentPlayerNickname);
                    break;
                case "showBookTileUpdate":
                    String bookTileNickname = gson.fromJson(params.get("nickname"), String.class);
                    rmi.showBookTileUpdate(bookTileNickname);
                    break;
                case "showAddCrewUpdate":
                    String addCrewNickname = gson.fromJson(params.get("nickname"), String.class);
                    Coordinate pos = gson.fromJson(params.get("pos"), Coordinate.class);
                    AliveType aliveType = AliveType.valueOf(params.get("type").getAsString().toUpperCase());
                    int num = gson.fromJson(params.get("num"), int.class);
                    rmi.showAddCrewUpdate(addCrewNickname, pos, aliveType, num);
                    break;
                case "showBookedTileNullityUpdate":
                    String bookedNullityNickname = gson.fromJson(params.get("nickname"), String.class);
                    int index = gson.fromJson(params.get("index"), int.class);
                    Coordinate bookedPos = gson.fromJson(params.get("pos"), Coordinate.class);
                    RotationType rotation = gson.fromJson(params.get("rotation"), RotationType.class);
                    rmi.showBookedTileNullityUpdate(bookedNullityNickname, index, bookedPos, rotation);
                    break;
                case "showEarlyLandingUpdate":
                    String earlyLandingNickname = gson.fromJson(params.get("nickname"), String.class);
                    rmi.showEarlyLandingUpdate(earlyLandingNickname);
                    break;
                case "showBuildTimeIsOverUpdate":
                    rmi.showBuildTimeIsOverUpdate();
                    break;
                case "showWinnersUpdate":
                    Type winnersType = new TypeToken<Map<String, Integer>>() {}.getType();
                    Map<String, Integer> winners = gson.fromJson(params.get("winners"), winnersType);
                    rmi.showWinnersUpdate(winners);
                    break;
                default:
                    System.err.println("Comando non riconosciuto: " + cmd.getCommand());
                    break;
            }
        } catch (JsonSyntaxException | IllegalStateException e) {
            System.err.println("Errore nel parsing JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Errore interno generico: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void ping(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("ping", params);

            if(out != null) {
                //Sending
                out.writeObject(gson.toJson(cmd));
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo ping: " + e.getMessage());
        }
    }

    @Override
    public void nicknameRegistration(String nickname, VirtualView client) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("registration", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo nicknameRegistration: " + e.getMessage());
        }
    }

    @Override
    public void createLobby(VirtualView client, String nickname, int maxPlayers, PlayerColor color, int level) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("maxPlayers", maxPlayers);
            params.addProperty("color", color.toString());
            params.addProperty("level", level);

            //Creation Command
            Command cmd = new Command("createLobby", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo createLobby: " + e.getMessage());
        }
    }
    @Override
    public void getLobbies(VirtualView client) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();

            //Creation Command
            Command cmd = new Command("getLobbies", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo getLobbies: " + e.getMessage());
        }
    }

    @Override
    public void joinLobby(VirtualView client, int lobbyId, String nickname, PlayerColor color) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("lobbyId", lobbyId);
            params.addProperty("nickname", nickname);
            params.addProperty("color", color.toString());

            //Creation Command
            Command cmd = new Command("joinLobby", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo joinLobby: " + e.getMessage());
        }
    }

    @Override
    public void isGameRunning(VirtualView client, int lobbyId) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("lobbyId", lobbyId);

            //Creation Command
            Command cmd = new Command("isGameRunning", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo isGameRunning: " + e.getMessage());
        }
    }

    @Override
    public void flipHourglass(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("flipHourglass", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo flipHourglass: " + e.getMessage());
        }
    }

    @Override
    public void hourglass(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("hourglass", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo hourglass: " + e.getMessage());
        }
    }

    @Override
    public void takeTile(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("takeTile", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo takeTile: " + e.getMessage());
        }
    }

    @Override
    public void takeTile(String nickname, String tile_imagePath) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("tile_imagePath", tile_imagePath);

            //Creation Command
            Command cmd = new Command("takeTileWithPath", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo takeTile con path: " + e.getMessage());
        }
    }

    @Override
    public void takeMiniDeck(String nickname, int index) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("index", index);

            //Creation Command
            Command cmd = new Command("takeMiniDeck", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo takeMiniDeck: " + e.getMessage());
        }
    }

    @Override
    public void returnMiniDeck(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("returnMiniDeck", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo returnMiniDeck: " + e.getMessage());
        }
    }

    @Override
    public void bookTile(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("bookTile", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo bookTile: " + e.getMessage());
        }
    }

    @Override
    public void addBookedTile(String nickname, int index, Coordinate pos, RotationType rotation) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("index", index);
            params.add("pos", gson.toJsonTree(pos));
            params.addProperty("rotation", rotation.toString());

            //Creation Command
            Command cmd = new Command("addBookedTile", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo addBookedTile: " + e.getMessage());
        }
    }

    @Override
    public void returnTile(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("returnTile", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo returnTile: " + e.getMessage());
        }
    }

    @Override
    public void addTile(String nickname, Coordinate pos, RotationType rotation) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.add("pos", gson.toJsonTree(pos));
            params.addProperty("rotation", rotation.toString());

            //Creation Command
            Command cmd = new Command("addTile", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo addTile: " + e.getMessage());
        }
    }

    @Override
    public void shipFinished(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("shipFinished", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo shipFinished: " + e.getMessage());
        }
    }

    @Override
    public void checkSpaceship(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("checkSpaceship", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo checkSpaceship: " + e.getMessage());
        }
    }

    @Override
    public void removeTile(String nickname, Coordinate pos) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.add("pos", gson.toJsonTree(pos));

            //Creation Command
            Command cmd = new Command("removeTile", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo removeTile: " + e.getMessage());
        }
    }

    @Override
    public void checkWrongSpaceship(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("checkWrongSpaceship", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo checkWrongSpaceship: " + e.getMessage());
        }
    }

    @Override
    public void addCrew(String nickname, Coordinate pos, AliveType type) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.add("pos", gson.toJsonTree(pos));
            params.addProperty("type", type.toString());

            //Creation Command
            Command cmd = new Command("addCrew", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo addCrew: " + e.getMessage());
        }
    }

    @Override
    public void ready(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("ready", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo ready: " + e.getMessage());
        }
    }

    @Override
    public void playNextCard(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("playNextCard", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo playNextCard: " + e.getMessage());
        }
    }

    @Override
    public void earlyLanding(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("earlyLanding", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo earlyLanding: " + e.getMessage());
        }
    }

    @Override
    public void choice(String nickname, boolean choice) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("choice", choice);

            //Creation Command
            Command cmd = new Command("choice", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo choice: " + e.getMessage());
        }
    }

    @Override
    public void removeCrew(String nickname, Coordinate pos) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.add("pos", gson.toJsonTree(pos));

            //Creation Command
            Command cmd = new Command("removeCrew", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo removeCrew: " + e.getMessage());
        }
    }

    @Override
    public void choiceBox(String nickname, boolean choice) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("choice", choice);

            //Creation Command
            Command cmd = new Command("choiceBox", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo choiceBox: " + e.getMessage());
        }
    }

    @Override
    public void moveBox(String nickname, Coordinate start, Coordinate end, BoxType boxType, boolean on) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.add("start", gson.toJsonTree(start));
            params.add("end", gson.toJsonTree(end));
            params.addProperty("boxType", boxType.toString());
            params.addProperty("on", on);

            //Creation Command
            Command cmd = new Command("moveBox", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo moveBox: " + e.getMessage());
        }
    }

    @Override
    public void choicePlanet(String nickname, int index) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("index", index);

            //Creation Command
            Command cmd = new Command("choicePlanet", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo choicePlanet: " + e.getMessage());
        }
    }

    @Override
    public void choiceDoubleMotor(String nickname, List<Coordinate> motors, List<Coordinate> batteries) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.add("motors", gson.toJsonTree(motors));
            params.add("batteries", gson.toJsonTree(batteries));

            //Creation Command
            Command cmd = new Command("choiceDoubleMotor", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo choiceDoubleMotor: " + e.getMessage());
        }
    }

    @Override
    public void choiceDoubleCannon(String nickname, List<Coordinate> cannons, List<Coordinate> batteries) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.add("cannons", gson.toJsonTree(cannons));
            params.add("batteries", gson.toJsonTree(batteries));

            //Creation Command
            Command cmd = new Command("choiceDoubleCannon", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo choiceDoubleCannon: " + e.getMessage());
        }
    }

    @Override
    public void choiceCrew(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("choiceCrew", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo choiceCrew: " + e.getMessage());
        }
    }

    @Override
    public void removeBox(String nickname, Coordinate pos, BoxType type) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.add("pos", gson.toJsonTree(pos));
            params.addProperty("type", type.toString());

            //Creation Command
            Command cmd = new Command("removeBox", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo removeBox: " + e.getMessage());
        }
    }

    @Override
    public void removeBattery(String nickname, Coordinate pos) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.add("pos", gson.toJsonTree(pos));

            //Creation Command
            Command cmd = new Command("removeBattery", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo removeBattery: " + e.getMessage());
        }
    }

    @Override
    public void rollDice(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("rollDice", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo rollDice: " + e.getMessage());
        }
    }

    @Override
    public void calculateDamage(String nickname, Coordinate pos) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.add("pos", gson.toJsonTree(pos));

            //Creation Command
            Command cmd = new Command("calculateDamage", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo calculateDamage: " + e.getMessage());
        }
    }

    @Override
    public void keepBlock(String nickname, Coordinate pos) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.add("pos", gson.toJsonTree(pos));

            //Creation Command
            Command cmd = new Command("keepBlock", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo keepBlock: " + e.getMessage());
        }
    }

    @Override
    public void Winners(int lobbyId) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("lobbyId", lobbyId);

            //Creation Command
            Command cmd = new Command("Winners", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo Winners: " + e.getMessage());
        }
    }

    @Override
    public void endGame(int lobbyId) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("lobbyId", lobbyId);

            //Creation Command
            Command cmd = new Command("endGame", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo endGame: " + e.getMessage());
        }
    }
}
