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

    }

    @Override
    public void createLobby(VirtualView client, String nickname, int maxPlayers, PlayerColor color, int level) throws RemoteException {

    }

    @Override
    public void getLobbies(VirtualView client) throws RemoteException {

    }

    @Override
    public void joinLobby(VirtualView client, int lobbyId, String nickname, PlayerColor color) throws RemoteException {

    }

    @Override
    public void isGameRunning(VirtualView client, int lobbyId) throws RemoteException {

    }

    @Override
    public void flipHourglass(Player player) throws RemoteException {

    }

    @Override
    public void takeTile(Player player) throws RemoteException {

    }

    @Override
    public void takeTile(Player player, Tile tile) throws RemoteException {

    }

    @Override
    public void takeMiniDeck(Player player, int index) throws RemoteException {

    }

    @Override
    public void returnMiniDeck(Player player) throws RemoteException {

    }

    @Override
    public void bookTile(Player player) throws RemoteException {

    }

    @Override
    public void addBookedTile(Player player, int index, Coordinate pos, RotationType rotation) throws RemoteException {

    }

    @Override
    public void returnTile(Player player) throws RemoteException {

    }

    @Override
    public void addTile(Player player, Coordinate pos, RotationType rotation) throws RemoteException {

    }

    @Override
    public void shipFinished(Player player) throws RemoteException {

    }

    @Override
    public void checkSpaceship(Player player) throws RemoteException {

    }

    @Override
    public void removeTile(Player player, Coordinate pos) throws RemoteException {

    }

    @Override
    public void checkWrongSpaceship(Player player) throws RemoteException {

    }

    @Override
    public void addCrew(Player player, Coordinate pos, AliveType type) throws RemoteException {

    }

    @Override
    public void ready(Player player) throws RemoteException {

    }

    @Override
    public void playNextCard(Player player) throws RemoteException {

    }

    @Override
    public void earlyLanding(Player player) throws RemoteException {

    }

    @Override
    public void choice(Player player, boolean choice) throws RemoteException {

    }

    @Override
    public void removeCrew(Player player, Coordinate pos) throws RemoteException {

    }

    @Override
    public void choiceBox(Player player, boolean choice) throws RemoteException {

    }

    @Override
    public void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on) throws RemoteException {

    }

    @Override
    public void choicePlanet(Player player, int index) throws RemoteException {

    }

    @Override
    public void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries) throws RemoteException {

    }

    @Override
    public void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries) throws RemoteException {

    }

    @Override
    public void choiceCrew(Player player) throws RemoteException {

    }

    @Override
    public void removeBox(Player player, Coordinate pos, BoxType type) throws RemoteException {

    }

    @Override
    public void removeBattery(Player player, Coordinate pos) throws RemoteException {

    }

    @Override
    public void rollDice(Player player) throws RemoteException {

    }

    @Override
    public void calculateDamage(Player player, Coordinate pos) throws RemoteException {

    }

    @Override
    public void keepBlock(Player player, Coordinate pos) throws RemoteException {

    }

    @Override
    public void Winners(int lobbyId) throws RemoteException {

    }

    @Override
    public void endGame(int lobbyId) throws RemoteException {

    }
}
