package it.polimi.ingsw.is25am02.controller.client;

import it.polimi.ingsw.is25am02.model.Coordinate;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.network.ConnectionClient;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;

import java.rmi.RemoteException;
import java.util.List;

public class ClientController implements VirtualServer {
    private final ConnectionClient connection;
    private MenuState menuState;

    public ClientController(ConnectionClient connection) {
        this.connection = connection;
        this.menuState = MenuState.LOGIN;
    }

    public void setMenuState(MenuState state) {
        menuState = state;
    }

    public VirtualView getVirtualView() {
        return (VirtualView) connection;
    }
    
    public void closeConnect() throws Exception {
        connection.closeConnection();
    }

    public void nicknameRegistration(String nickname, VirtualView client) throws RemoteException {
        if(menuControl(MenuState.LOGIN)) {
            connection.getServer().nicknameRegistration(nickname, client);
        }
    }

    public void createLobby(VirtualView client, String nickname, int maxPlayers, PlayerColor color, int level) throws RemoteException {
        if(menuControl(MenuState.MENU)) {
            connection.getServer().createLobby(client, nickname, maxPlayers, color, level);
        }
    }

    public void getLobbies(VirtualView client) throws RemoteException {
        if(menuControl(MenuState.MENU)) {
            connection.getServer().getLobbies(client);
        }
    }

    public void joinLobby(VirtualView client, int lobbyId, String nickname, PlayerColor color) throws RemoteException {
        if(menuControl(MenuState.MENU)) {
            connection.getServer().joinLobby(client, lobbyId, nickname, color);
        }
    }

    public void isGameRunning(VirtualView client, int lobbyId) throws RemoteException {
        if(menuControl(MenuState.MENU)) {
            connection.getServer().isGameRunning(client, lobbyId);
        }
    }

    public void flipHourglass(Player player) throws RemoteException {
        connection.getServer().flipHourglass(player);
    }

    public void takeTile(Player player) throws RemoteException {
        connection.getServer().takeTile(player);
    }

    public void takeTile(Player player, Tile tile) throws RemoteException {
        connection.getServer().takeTile(player, tile);
    }

    public void takeMiniDeck(Player player, int index) throws RemoteException {
        connection.getServer().takeMiniDeck(player, index);
    }

    public void returnMiniDeck(Player player) throws RemoteException {
        connection.getServer().returnMiniDeck(player);
    }

    public void bookTile(Player player) throws RemoteException {
        connection.getServer().bookTile(player);
    }

    public void addBookedTile(Player player, int index, Coordinate pos, RotationType rotation) throws RemoteException {
        connection.getServer().addBookedTile(player, index, pos, rotation);
    }

    public void returnTile(Player player) throws RemoteException {
        connection.getServer().returnTile(player);
    }

    public void addTile(Player player, Coordinate pos, RotationType rotation) throws RemoteException {
        connection.getServer().addTile(player, pos, rotation);
    }

    public void shipFinished(Player player) throws RemoteException {
        connection.getServer().shipFinished(player);
    }

    public void checkSpaceship(Player player) throws RemoteException {
        connection.getServer().checkSpaceship(player);
    }

    public void removeTile(Player player, Coordinate pos) throws RemoteException {
        connection.getServer().removeTile(player, pos);
    }

    public void checkWrongSpaceship(Player player) throws RemoteException {
        connection.getServer().checkWrongSpaceship(player);
    }

    public void addCrew(Player player, Coordinate pos, AliveType type) throws RemoteException {
        connection.getServer().addCrew(player, pos, type);
    }

    public void ready(Player player) throws RemoteException {
        connection.getServer().ready(player);
    }

    public void playNextCard(Player player) throws RemoteException {
        connection.getServer().playNextCard(player);
    }

    public void earlyLanding(Player player) throws RemoteException {
        connection.getServer().earlyLanding(player);
    }

    public void choice(Player player, boolean choice) throws RemoteException {
        connection.getServer().choice(player, choice);
    }

    public void removeCrew(Player player, Coordinate pos) throws RemoteException {
        connection.getServer().removeCrew(player, pos);
    }

    public void choiceBox(Player player, boolean choice) throws RemoteException {
        connection.getServer().choiceBox(player, choice);
    }

    public void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on) throws RemoteException {
        connection.getServer().moveBox(player, start, end, boxType, on);
    }

    public void choicePlanet(Player player, int index) throws RemoteException {
        connection.getServer().choicePlanet(player, index);
    }

    public void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries) throws RemoteException {
        connection.getServer().choiceDoubleMotor(player, motors, batteries);
    }

    public void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries) throws RemoteException {
        connection.getServer().choiceDoubleCannon(player, cannons, batteries);
    }

    public void choiceCrew(Player player) throws RemoteException {
        connection.getServer().choiceCrew(player);
    }

    public void removeBox(Player player, Coordinate pos, BoxType type) throws RemoteException {
        connection.getServer().removeBox(player, pos, type);
    }

    public void removeBattery(Player player, Coordinate pos) throws RemoteException {
        connection.getServer().removeBattery(player, pos);
    }

    public void rollDice(Player player) throws RemoteException {
        connection.getServer().rollDice(player);
    }

    public void calculateDamage(Player player, Coordinate pos) throws RemoteException {
        connection.getServer().calculateDamage(player, pos);
    }

    public void keepBlock(Player player, Coordinate pos) throws RemoteException {
        connection.getServer().keepBlock(player, pos);
    }

    public void Winners(int lobbyId) throws RemoteException {
        connection.getServer().Winners(lobbyId);
    }

    public void endGame(int lobbyId) throws RemoteException {
        connection.getServer().endGame(lobbyId);
    }

    private boolean menuControl(MenuState state){
        if(menuState != state){
            if(menuState == MenuState.LOGIN){
                connection.getConsole().reportError("> you need to log in");
            } else if (menuState == MenuState.MENU) {
                if(state == MenuState.LOGIN) {
                    connection.getConsole().reportError("> you are already logged in");
                } else if (state == MenuState.WAITING) {
                    connection.getConsole().reportError("> you need to wait, for the game to start");
                }
            } else if (menuState == MenuState.WAITING) {
                connection.getConsole().reportError("> you need to wait, for the game to start");
            }
            return false;
        } else { return true; }
    }
}
