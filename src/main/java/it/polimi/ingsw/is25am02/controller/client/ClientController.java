package it.polimi.ingsw.is25am02.controller.client;

import it.polimi.ingsw.is25am02.model.Coordinate;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;

import java.rmi.RemoteException;
import java.util.List;

public class ClientController implements VirtualServer{
    private final VirtualServer server;

    public ClientController(VirtualServer server) {
        this.server = server;
    }

    @Override
    public void nicknameRegistration(String nickname, VirtualView client) throws RemoteException {
        server.nicknameRegistration(nickname, client);
    }

    @Override
    public void createLobby(VirtualView client, String nickname, int maxPlayers, PlayerColor color, int level) throws RemoteException {
        server.createLobby(client, nickname, maxPlayers, color, level);
    }

    @Override
    public void getLobbies(VirtualView client) throws RemoteException {
        server.getLobbies(client);

    }

    @Override
    public void joinLobby(VirtualView client, int lobbyId, String nickname, PlayerColor color) throws RemoteException {
        server.joinLobby(client, lobbyId, nickname, color);

    }

    @Override
    public void isGameRunning(VirtualView client, int lobbyId) throws RemoteException {
        server.isGameRunning(client, lobbyId);

    }

    @Override
    public void flipHourglass(Player player) throws RemoteException {
        server.flipHourglass(player);

    }

    @Override
    public void takeTile(Player player) throws RemoteException {
        server.takeTile(player);

    }

    @Override
    public void takeTile(Player player, Tile tile) throws RemoteException {
        server.takeTile(player, tile);

    }

    @Override
    public void takeMiniDeck(Player player, int index) throws RemoteException {
        server.takeMiniDeck(player, index);

    }

    @Override
    public void returnMiniDeck(Player player) throws RemoteException {
        server.returnMiniDeck(player);

    }

    @Override
    public void bookTile(Player player) throws RemoteException {
        server.bookTile(player);

    }

    @Override
    public void addBookedTile(Player player, int index, Coordinate pos, RotationType rotation) throws RemoteException {
        server.addBookedTile(player, index, pos, rotation);

    }

    @Override
    public void returnTile(Player player) throws RemoteException {
        server.returnTile(player);

    }

    @Override
    public void addTile(Player player, Coordinate pos, RotationType rotation) throws RemoteException {
        server.addTile(player, pos, rotation);

    }

    @Override
    public void shipFinished(Player player) throws RemoteException {
        server.shipFinished(player);

    }

    @Override
    public void checkSpaceship(Player player) throws RemoteException {
        server.checkSpaceship(player);

    }

    @Override
    public void removeTile(Player player, Coordinate pos) throws RemoteException {
        server.removeTile(player, pos);

    }

    @Override
    public void checkWrongSpaceship(Player player) throws RemoteException {
        server.checkWrongSpaceship(player);

    }

    @Override
    public void addCrew(Player player, Coordinate pos, AliveType type) throws RemoteException {
        server.addCrew(player, pos, type);

    }

    @Override
    public void ready(Player player) throws RemoteException {
        server.ready(player);

    }

    @Override
    public void playNextCard(Player player) throws RemoteException {
        server.playNextCard(player);

    }

    @Override
    public void earlyLanding(Player player) throws RemoteException {
        server.earlyLanding(player);

    }

    @Override
    public void choice(Player player, boolean choice) throws RemoteException {
        server.choice(player, choice);

    }

    @Override
    public void removeCrew(Player player, Coordinate pos) throws RemoteException {
        server.removeCrew(player, pos);

    }

    @Override
    public void choiceBox(Player player, boolean choice) throws RemoteException {
        server.choiceBox(player, choice);

    }

    @Override
    public void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on) throws RemoteException {
        server.moveBox(player, start, end, boxType, on);

    }

    @Override
    public void choicePlanet(Player player, int index) throws RemoteException {
        server.choicePlanet(player, index);

    }

    @Override
    public void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries) throws RemoteException {
        server.choiceDoubleMotor(player, motors, batteries);

    }

    @Override
    public void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries) throws RemoteException {
        server.choiceDoubleCannon(player, cannons, batteries);

    }

    @Override
    public void choiceCrew(Player player) throws RemoteException {
        server.choiceCrew(player);

    }

    @Override
    public void removeBox(Player player, Coordinate pos, BoxType type) throws RemoteException {
        server.removeBox(player, pos, type);

    }

    @Override
    public void removeBattery(Player player, Coordinate pos) throws RemoteException {
        server.removeBattery(player, pos);

    }

    @Override
    public void rollDice(Player player) throws RemoteException {
        server.rollDice(player);

    }

    @Override
    public void calculateDamage(Player player, Coordinate pos) throws RemoteException {
        server.calculateDamage(player, pos);

    }

    @Override
    public void keepBlock(Player player, Coordinate pos) throws RemoteException {
        server.keepBlock(player, pos);

    }

    @Override
    public void Winners(int lobbyId) throws RemoteException {
        server.Winners(lobbyId);

    }

    @Override
    public void endGame(int lobbyId) throws RemoteException {
        server.endGame(lobbyId);

    }
}
