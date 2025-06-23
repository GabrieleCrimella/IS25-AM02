package it.polimi.ingsw.is25am02.network.socket.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.network.socket.Command;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.utils.enumerations.*;

import java.awt.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable, VirtualView {
    private final Socket socket;
    private final ServerController controller;
    private final Gson gson = new Gson();
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = true;

    public ClientHandler(Socket socket, ServerController controller) throws IOException {
        this.socket = socket;
        this.controller = controller;
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            Object receivedObject;
            // Il ciclo si chiude quando il client si disconnette
            while (running && (receivedObject = in.readObject()) != null) {
                    String jsonString = (String) receivedObject;

                    try {
                        Command cmd = gson.fromJson(jsonString, Command.class);
                        processCommand(cmd);

                    } catch (Exception e) {
                        System.err.println("Errore con client1: " + e.getMessage());
                    }
            }
        } catch (IOException | ClassNotFoundException e) {
            if(running) {
                System.out.println("Il client ha chiuso la connessione");
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
            boolean goodRequest = true;
            JsonObject params = cmd.getParams();

            switch (cmd.getCommand()) {
                case "ping":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        controller.ping(gson.fromJson(params.get("nickname"), String.class));
                    }
                    goodRequest = true;
                    break;
                case "registration":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        controller.nicknameRegistration(gson.fromJson(params.get("nickname"), String.class), this);
                    }
                    goodRequest = true;
                    break;
                case "createLobby":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(!params.has("maxPlayers") || !params.get("maxPlayers").isJsonPrimitive() ||
                            !params.get("maxPlayers").getAsJsonPrimitive().isNumber()) {
                        goodRequest = false;
                    }

                    if(!params.has("color") || !params.get("color").isJsonPrimitive() ||
                            !params.get("color").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(!params.has("level") || !params.get("level").isJsonPrimitive() ||
                            !params.get("level").getAsJsonPrimitive().isNumber()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        int maxPlayers = gson.fromJson(params.get("maxPlayers"), Integer.class);
                        PlayerColor color = gson.fromJson(params.get("color"), PlayerColor.class);
                        int level = gson.fromJson(params.get("level"), Integer.class);
                        controller.createLobby(this, nickname, maxPlayers, color, level);
                    }
                    goodRequest = true;
                    break;
                case "getLobbies":
                    controller.getLobbies(this);
                    break;

                case "joinLobby":
                    if (!params.has("lobbyId") || !params.get("lobbyId").isJsonPrimitive() ||
                            !params.get("lobbyId").getAsJsonPrimitive().isNumber()) {
                        goodRequest = false;
                    }

                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(!params.has("color") || !params.get("color").isJsonPrimitive() ||
                            !params.get("color").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        int lobbyId = gson.fromJson(params.get("lobbyId"), Integer.class);
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        PlayerColor color = gson.fromJson(params.get("color"), PlayerColor.class);
                        controller.joinLobby(this, lobbyId, nickname, color);
                    }
                    goodRequest = true;
                    break;

                case "isGameRunning":
                    if (!params.has("lobbyId") || !params.get("lobbyId").isJsonPrimitive() ||
                            !params.get("lobbyId").getAsJsonPrimitive().isNumber()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        int lobbyId = gson.fromJson(params.get("lobbyId"), Integer.class);
                        controller.isGameRunning(this, lobbyId);
                    }
                    goodRequest = true;
                    break;

                case "flipHourglass":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.flipHourglass(nickname);
                    }
                    goodRequest = true;
                    break;

                case "hourglass":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.hourglass(nickname);
                    }
                    goodRequest = true;
                    break;

                case "takeTile":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.takeTile(nickname);
                    }
                    goodRequest = true;
                    break;

                case "takeTileWithPath":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("tile_imagePath") || !params.get("tile_imagePath").isJsonPrimitive() ||
                            !params.get("tile_imagePath").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        String tilePath = gson.fromJson(params.get("tile_imagePath"), String.class);
                        controller.takeTile(nickname, tilePath);
                    }
                    goodRequest = true;
                    break;

                case "takeMiniDeck":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("index") || !params.get("index").isJsonPrimitive() ||
                            !params.get("index").getAsJsonPrimitive().isNumber()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        int index = gson.fromJson(params.get("index"), Integer.class);
                        controller.takeMiniDeck(nickname, index);
                    }
                    goodRequest = true;
                    break;

                case "returnMiniDeck":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.returnMiniDeck(nickname);
                    }
                    goodRequest = true;
                    break;

                case "bookTile":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.bookTile(nickname);
                    }
                    goodRequest = true;
                    break;

                case "addBookedTile":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("index") || !params.get("index").isJsonPrimitive() ||
                            !params.get("index").getAsJsonPrimitive().isNumber()) {
                        goodRequest = false;
                    }

                    if (!params.has("pos") || !params.get("pos").isJsonObject()) {
                        goodRequest = false;
                    }

                    if (!params.has("rotation") || !params.get("rotation").isJsonPrimitive() ||
                            !params.get("rotation").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        int index = gson.fromJson(params.get("index"), Integer.class);
                        Coordinate pos = gson.fromJson(params.get("pos"), Coordinate.class);
                        RotationType rotation = gson.fromJson(params.get("rotation"), RotationType.class);
                        controller.addBookedTile(nickname, index, pos, rotation);
                    }
                    goodRequest = true;
                    break;

                case "returnTile":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.returnTile(nickname);
                    }
                    goodRequest = true;
                    break;

                case "addTile":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("pos") || !params.get("pos").isJsonObject()) {
                        goodRequest = false;
                    }

                    if (!params.has("rotation") || !params.get("rotation").isJsonPrimitive() ||
                            !params.get("rotation").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        Coordinate pos = gson.fromJson(params.get("pos"), Coordinate.class);
                        RotationType rotation = gson.fromJson(params.get("rotation"), RotationType.class);
                        controller.addTile(nickname, pos, rotation);
                    }
                    goodRequest = true;
                    break;

                case "shipFinished":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.shipFinished(nickname);
                    }
                    goodRequest = true;
                    break;

                case "checkSpaceship":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.checkSpaceship(nickname);
                    }
                    goodRequest = true;
                    break;

                case "removeTile":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("pos") || !params.get("pos").isJsonObject()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        Coordinate pos = gson.fromJson(params.get("pos"), Coordinate.class);
                        controller.removeTile(nickname, pos);
                    }
                    goodRequest = true;
                    break;

                case "checkWrongSpaceship":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.checkWrongSpaceship(nickname);
                    }
                    goodRequest = true;
                    break;

                case "addCrew":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("pos") || !params.get("pos").isJsonObject()) {
                        goodRequest = false;
                    }

                    if (!params.has("type") || !params.get("type").isJsonPrimitive() ||
                            !params.get("type").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        Coordinate pos = gson.fromJson(params.get("pos"), Coordinate.class);
                        AliveType type = gson.fromJson(params.get("type"), AliveType.class);
                        controller.addCrew(nickname, pos, type);
                    }
                    goodRequest = true;
                    break;

                case "ready":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.ready(nickname);
                    }
                    goodRequest = true;
                    break;

                case "playNextCard":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.playNextCard(nickname);
                    }
                    goodRequest = true;
                    break;

                case "earlyLanding":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.earlyLanding(nickname);
                    }
                    goodRequest = true;
                    break;

                case "choice":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("choice") || !params.get("choice").isJsonPrimitive() ||
                            !params.get("choice").getAsJsonPrimitive().isBoolean()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        boolean choice = gson.fromJson(params.get("choice"), Boolean.class);
                        controller.choice(nickname, choice);
                    }
                    goodRequest = true;
                    break;

                case "removeCrew":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("pos") || !params.get("pos").isJsonObject()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        Coordinate pos = gson.fromJson(params.get("pos"), Coordinate.class);
                        controller.removeCrew(nickname, pos);
                    }
                    goodRequest = true;
                    break;

                case "choiceBox":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("choice") || !params.get("choice").isJsonPrimitive() ||
                            !params.get("choice").getAsJsonPrimitive().isBoolean()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        boolean choice = gson.fromJson(params.get("choice"), Boolean.class);
                        controller.choiceBox(nickname, choice);
                    }
                    goodRequest = true;
                    break;

                case "moveBox":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("start") || !params.get("start").isJsonObject()) {
                        goodRequest = false;
                    }

                    if (!params.has("end") || !params.get("end").isJsonObject()) {
                        goodRequest = false;
                    }

                    if (!params.has("boxType") || !params.get("boxType").isJsonPrimitive() ||
                            !params.get("boxType").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("on") || !params.get("on").isJsonPrimitive() ||
                            !params.get("on").getAsJsonPrimitive().isBoolean()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        Coordinate start = gson.fromJson(params.get("start"), Coordinate.class);
                        Coordinate end = gson.fromJson(params.get("end"), Coordinate.class);
                        BoxType boxType = gson.fromJson(params.get("boxType"), BoxType.class);
                        boolean on = gson.fromJson(params.get("on"), Boolean.class);
                        controller.moveBox(nickname, start, end, boxType, on);
                    }
                    goodRequest = true;
                    break;

                case "choicePlanet":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("index") || !params.get("index").isJsonPrimitive() ||
                            !params.get("index").getAsJsonPrimitive().isNumber()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        int index = gson.fromJson(params.get("index"), Integer.class);
                        controller.choicePlanet(nickname, index);
                    }
                    goodRequest = true;
                    break;

                case "choiceDoubleMotor":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("motors") || !params.get("motors").isJsonArray()) {
                        goodRequest = false;
                    }

                    if (!params.has("batteries") || !params.get("batteries").isJsonArray()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        List<Coordinate> motors = gson.fromJson(params.get("motors"),
                                new com.google.gson.reflect.TypeToken<List<Coordinate>>(){}.getType());
                        List<Coordinate> batteries = gson.fromJson(params.get("batteries"),
                                new com.google.gson.reflect.TypeToken<List<Coordinate>>(){}.getType());
                        controller.choiceDoubleMotor(nickname, motors, batteries);
                    }
                    goodRequest = true;
                    break;

                case "choiceDoubleCannon":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("cannons") || !params.get("cannons").isJsonArray()) {
                        goodRequest = false;
                    }

                    if (!params.has("batteries") || !params.get("batteries").isJsonArray()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        List<Coordinate> cannons = gson.fromJson(params.get("cannons"),
                                new com.google.gson.reflect.TypeToken<List<Coordinate>>(){}.getType());
                        List<Coordinate> batteries = gson.fromJson(params.get("batteries"),
                                new com.google.gson.reflect.TypeToken<List<Coordinate>>(){}.getType());
                        controller.choiceDoubleCannon(nickname, cannons, batteries);
                    }
                    goodRequest = true;
                    break;

                case "choiceCrew":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.choiceCrew(nickname);
                    }
                    goodRequest = true;
                    break;

                case "removeBox":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("pos") || !params.get("pos").isJsonObject()) {
                        goodRequest = false;
                    }

                    if (!params.has("type") || !params.get("type").isJsonPrimitive() ||
                            !params.get("type").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        Coordinate pos = gson.fromJson(params.get("pos"), Coordinate.class);
                        BoxType type = gson.fromJson(params.get("type"), BoxType.class);
                        controller.removeBox(nickname, pos, type);
                    }
                    goodRequest = true;
                    break;

                case "removeBattery":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("pos") || !params.get("pos").isJsonObject()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        Coordinate pos = gson.fromJson(params.get("pos"), Coordinate.class);
                        controller.removeBattery(nickname, pos);
                    }
                    goodRequest = true;
                    break;

                case "rollDice":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        controller.rollDice(nickname);
                    }
                    goodRequest = true;
                    break;

                case "calculateDamage":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("pos") || !params.get("pos").isJsonObject()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        Coordinate pos = gson.fromJson(params.get("pos"), Coordinate.class);
                        controller.calculateDamage(nickname, pos);
                    }
                    goodRequest = true;
                    break;

                case "keepBlock":
                    if (!params.has("nickname") || !params.get("nickname").isJsonPrimitive() ||
                            !params.get("nickname").getAsJsonPrimitive().isString()) {
                        goodRequest = false;
                    }

                    if (!params.has("pos") || !params.get("pos").isJsonObject()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        String nickname = gson.fromJson(params.get("nickname"), String.class);
                        Coordinate pos = gson.fromJson(params.get("pos"), Coordinate.class);
                        controller.keepBlock(nickname, pos);
                    }
                    goodRequest = true;
                    break;

                case "winners":
                    if (!params.has("lobbyId") || !params.get("lobbyId").isJsonPrimitive() ||
                            !params.get("lobbyId").getAsJsonPrimitive().isNumber()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        int lobbyId = gson.fromJson(params.get("lobbyId"), Integer.class);
                        controller.Winners(lobbyId);
                    }
                    goodRequest = true;
                    break;

                case "endGame":
                    if (!params.has("lobbyId") || !params.get("lobbyId").isJsonPrimitive() ||
                            !params.get("lobbyId").getAsJsonPrimitive().isNumber()) {
                        goodRequest = false;
                    }

                    if(goodRequest) {
                        int lobbyId = gson.fromJson(params.get("lobbyId"), Integer.class);
                        controller.endGame(lobbyId);
                    }
                    goodRequest = true;
                    break;


            }
        } catch (JsonSyntaxException | IllegalStateException e) {
            System.err.println("Errore nel parsing JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Errore interno generico: " + e.getMessage());
        }
    }

    @Override
    public void pingFromServer() throws RemoteException {
        try {
            //Creation Json for params
            JsonObject jsonParams = new JsonObject();

            //Creation Command
            Command cmd = new Command("heartbeat", jsonParams);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo pingFromServer: " + e.getMessage());
        }
    }

    @Override
    public void reportError(String keys, Map<String, String> params) throws Exception {
        try {
            //Creation Json for params
            JsonObject jsonParams = new JsonObject();
            jsonParams.addProperty("keys", keys);
            jsonParams.add("params", gson.toJsonTree(params));

            //Creation Command
            Command cmd = new Command("reportError", jsonParams);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo reportError: " + e.getMessage());
        }
    }

    @Override
    public void displayMessage(String keys, Map<String, String> param) throws Exception {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("keys", keys);
            params.add("params", gson.toJsonTree(param));

            //Creation Command
            Command cmd = new Command("displayMessage", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo displayMessage: " + e.getMessage());
        }
    }

    @Override
    public void setLobbiesView(Map<Integer, LobbyView> lobbies) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.add("lobbies", gson.toJsonTree(lobbies));

            //Creation Command
            Command cmd = new Command("setLobbiesView", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo setLobbiesView: " + e.getMessage());
        }
    }

    @Override
    public void setBuildView(int level, PlayerColor color) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("level", level);
            params.addProperty("color", color.toString());

            //Creation Command
            Command cmd = new Command("setBuildView", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo setBuildView: " + e.getMessage());
        }
    }

    @Override
    public void setGameView(int level, PlayerColor color) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("level", level);
            params.addProperty("color", color.toString());

            //Creation Command
            Command cmd = new Command("setGameView", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo setGameView: " + e.getMessage());
        }
    }

    @Override
    public void setMenuState(MenuState state) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("state", state.toString());

            //Creation Command
            Command cmd = new Command("setMenuState", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo setMenuState: " + e.getMessage());
        }
    }

    @Override
    public void setNickname(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("setNickname", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo setNickname: " + e.getMessage());
        }
    }

    @Override
    public void showUpdateEverything(int level, HashMap<String, PlayerColor> playercolors, String currentCardImage, StateCardType stateCard, CardType type, String comment, StateGameType stateGame, String currentPlayer, boolean[][] mask, int[] positions, HashMap<Integer, List<List<Object>>> deck) throws Exception {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("level", level);
            params.add("playercolors", gson.toJsonTree(playercolors));
            params.addProperty("currentCardImage", currentCardImage);
            params.addProperty("stateCard", stateCard.toString());
            params.addProperty("type", type.toString());
            params.addProperty("comment", comment);
            params.addProperty("stateGame", stateGame.toString());
            params.addProperty("currentPlayer", currentPlayer);
            params.add("mask", gson.toJsonTree(mask));
            params.add("positions", gson.toJsonTree(positions));
            params.add("deck", gson.toJsonTree(deck));

            //Creation Command
            Command cmd = new Command("showUpdateEverything", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showUpdateEverything: " + e.getMessage());
        }
    }

    @Override
    public void showTileRemoval(Coordinate coordinate, String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.add("coordinate", gson.toJsonTree(coordinate));
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("showTileRemoval", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showTileRemoval: " + e.getMessage());
        }
    }

    @Override
    public void spaceshipBrokenUpdate(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("spaceshipBrokenUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo spaceshipBrokenUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showBatteryRemoval(Coordinate coordinate, String nickname, int numBattery) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.add("coordinate", gson.toJsonTree(coordinate));
            params.addProperty("nickname", nickname);
            params.addProperty("numBattery", numBattery);

            //Creation Command
            Command cmd = new Command("showBatteryRemoval", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showBatteryRemoval: " + e.getMessage());
        }
    }

    @Override
    public void showCrewRemoval(Coordinate coordinate, String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.add("coordinate", gson.toJsonTree(coordinate));
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("showCrewRemoval", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showCrewRemoval: " + e.getMessage());
        }
    }

    @Override
    public void showBoxUpdate(Coordinate coordinate, String nickname, List<BoxType> box) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.add("coordinate", gson.toJsonTree(coordinate));
            params.addProperty("nickname", nickname);
            params.add("box", gson.toJsonTree(box));

            //Creation Command
            Command cmd = new Command("showBoxUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showBoxUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showCreditUpdate(String nickname, int cosmicCredits) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("cosmicCredits", cosmicCredits);

            //Creation Command
            Command cmd = new Command("showCreditUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showCreditUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showUpdatedOthers() throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();

            //Creation Command
            Command cmd = new Command("showUpdatedOthers", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showUpdatedOthers: " + e.getMessage());
        }
    }

    @Override
    public void showPositionUpdate(String nickname, int position) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("position", position);

            //Creation Command
            Command cmd = new Command("showPositionUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showPositionUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showHourglassUpdate(long timeLeft) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("timeLeft", timeLeft);

            //Creation Command
            Command cmd = new Command("showHourglassUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showHourglassUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showDiceUpdate(String nickname, int result) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("result", result);

            //Creation Command
            Command cmd = new Command("showDiceUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showDiceUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showMinideckUpdate(String nickname, int deck) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("deck", deck);

            //Creation Command
            Command cmd = new Command("showMinideckUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showMinideckUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showCurrentCardUpdate(String imagepath, StateCardType stateCard, CardType type, String comment) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("imagepath", imagepath);
            params.addProperty("stateCard", stateCard.toString());
            params.addProperty("type", type.toString());
            params.addProperty("comment", comment);

            //Creation Command
            Command cmd = new Command("showCurrentCardUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showCurrentCardUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showCurrentTileNullityUpdate(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("showCurrentTileNullityUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showCurrentTileNullityUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showCurrentTileUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox, String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("imagepath", imagepath);
            params.add("connectors", gson.toJsonTree(connectors));
            params.addProperty("rotationType", rotationType.toString());
            params.addProperty("tType", tType.toString());
            params.addProperty("maxBattery", maxBattery);
            params.addProperty("maxBox", maxBox);
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("showCurrentTileUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showCurrentTileUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showVisibilityUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("imagepath", imagepath);
            params.add("connectors", gson.toJsonTree(connectors));
            params.addProperty("rotationType", rotationType.toString());
            params.addProperty("tType", tType.toString());
            params.addProperty("maxBattery", maxBattery);
            params.addProperty("maxBox", maxBox);

            //Creation Command
            Command cmd = new Command("showVisibilityUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showVisibilityUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showTileRemovalFromHeapTile(String imagepath) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("imagepath", imagepath);

            //Creation Command
            Command cmd = new Command("showTileRemovalFromHeapTile", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showTileRemovalFromHeapTile: " + e.getMessage());
        }
    }

    @Override
    public void showDeckAllowUpdate(String player) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("player", player);

            //Creation Command
            Command cmd = new Command("showDeckAllowUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showDeckAllowUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showTileAdditionUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox, String nickname, Coordinate coordinate) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("imagepath", imagepath);
            params.add("connectors", gson.toJsonTree(connectors));
            params.addProperty("rotationType", rotationType.toString());
            params.addProperty("tType", tType.toString());
            params.addProperty("maxBattery", maxBattery);
            params.addProperty("maxBox", maxBox);
            params.addProperty("nickname", nickname);
            params.add("coordinate", gson.toJsonTree(coordinate));

            //Creation Command
            Command cmd = new Command("showTileAdditionUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showTileAdditionUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showGameStateUpdate(StateGameType newGamestate) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("newGamestate", newGamestate.toString());

            //Creation Command
            Command cmd = new Command("showGameStateUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showGameStateUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showPlayerStateUpdate(String nickname, StatePlayerType newPlayerstate) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("newPlayerstate", newPlayerstate.toString());

            //Creation Command
            Command cmd = new Command("showPlayerStateUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showPlayerStateUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showCardStateUpdate(StateCardType newCardstate) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("newCardstate", newCardstate.toString());

            //Creation Command
            Command cmd = new Command("showCardStateUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showCardStateUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showCurrentPlayerUpdate(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("showCurrentPlayerUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showCurrentPlayerUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showBookTileUpdate(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("showBookTileUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showBookTileUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showAddCrewUpdate(String nickname, Coordinate pos, AliveType type, int num) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.add("pos", gson.toJsonTree(pos));
            params.addProperty("type", type.toString());
            params.addProperty("num", num);

            //Creation Command
            Command cmd = new Command("showAddCrewUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showAddCrewUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showBookedTileNullityUpdate(String nickname, int index, Coordinate pos, RotationType rotation) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);
            params.addProperty("index", index);
            params.add("pos", gson.toJsonTree(pos));
            params.addProperty("rotation", rotation.toString());

            //Creation Command
            Command cmd = new Command("showBookedTileNullityUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showBookedTileNullityUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showEarlyLandingUpdate(String nickname) throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();
            params.addProperty("nickname", nickname);

            //Creation Command
            Command cmd = new Command("showEarlyLandingUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showEarlyLandingUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showBuildTimeIsOverUpdate() throws RemoteException {
        try {
            //Creation Json for params
            JsonObject params = new JsonObject();

            //Creation Command
            Command cmd = new Command("showBuildTimeIsOverUpdate", params);

            //Sending
            out.writeObject(gson.toJson(cmd));
            out.flush();
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del metodo showBuildTimeIsOverUpdate: " + e.getMessage());
        }
    }

    @Override
    public void showWinnersUpdate(Map<String, Integer> winners) throws RemoteException{
        //todo davide bisgona aggiungere questo metodo
    }
}
