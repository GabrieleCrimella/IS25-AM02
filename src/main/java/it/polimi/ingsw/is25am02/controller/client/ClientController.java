package it.polimi.ingsw.is25am02.controller.client;

import it.polimi.ingsw.is25am02.model.Coordinate;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.network.ConnectionClient;
import it.polimi.ingsw.is25am02.network.VirtualView;

import java.rmi.RemoteException;
import java.util.List;

public class ClientController {
    private final ConnectionClient connection;

    public ClientController(ConnectionClient connection) {
        this.connection = connection;
    }

    public VirtualView getVirtualView() { return (VirtualView) connection; }

    public void nicknameRegistration(String nickname, VirtualView client) {
        try {
            connection.getServer().nicknameRegistration(nickname, client);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void createLobby(VirtualView client, String nickname, int maxPlayers, PlayerColor color, int level) {
        try {
            connection.getServer().createLobby(client, nickname, maxPlayers, color, level);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void getLobbies(VirtualView client) {
        try {
            connection.getServer().getLobbies(client);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void joinLobby(VirtualView client, int lobbyId, String nickname, PlayerColor color) {
        try {
            connection.getServer().joinLobby(client, lobbyId, nickname, color);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void isGameRunning(VirtualView client, int lobbyId)  {
        try {
            connection.getServer().isGameRunning(client, lobbyId);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void flipHourglass(Player player)  {
        try {
            connection.getServer().flipHourglass(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void takeTile(Player player)  {
        try {
            connection.getServer().takeTile(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void takeTile(Player player, Tile tile)  {
        try {
            connection.getServer().takeTile(player, tile);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void takeMiniDeck(Player player, int index)  {
        try {
            connection.getServer().takeMiniDeck(player, index);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void returnMiniDeck(Player player)  {
        try {
            connection.getServer().returnMiniDeck(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void bookTile(Player player)  {
        try {
            connection.getServer().bookTile(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void addBookedTile(Player player, int index, Coordinate pos, RotationType rotation)  {
        try {
            connection.getServer().addBookedTile(player, index, pos, rotation);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void returnTile(Player player)  {
        try {
            connection.getServer().returnTile(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void addTile(Player player, Coordinate pos, RotationType rotation)  {
        try {
            connection.getServer().addTile(player, pos, rotation);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void shipFinished(Player player)  {
        try {
            connection.getServer().shipFinished(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void checkSpaceship(Player player)  {
        try {
            connection.getServer().checkSpaceship(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void removeTile(Player player, Coordinate pos)  {
        try {
            connection.getServer().removeTile(player, pos);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void checkWrongSpaceship(Player player)  {
        try {
            connection.getServer().checkWrongSpaceship(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void addCrew(Player player, Coordinate pos, AliveType type)  {
        try {
            connection.getServer().addCrew(player, pos, type);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void ready(Player player)  {
        try {
            connection.getServer().ready(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void playNextCard(Player player)  {
        try {
            connection.getServer().playNextCard(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void earlyLanding(Player player)  {
        try {
            connection.getServer().earlyLanding(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void choice(Player player, boolean choice)  {
        try {
            connection.getServer().choice(player, choice);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void removeCrew(Player player, Coordinate pos)  {
        try {
            connection.getServer().removeCrew(player, pos);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void choiceBox(Player player, boolean choice)  {
        try {
            connection.getServer().choiceBox(player, choice);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on)  {
        try {
            connection.getServer().moveBox(player, start, end, boxType, on);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void choicePlanet(Player player, int index)  {
        try {
            connection.getServer().choicePlanet(player, index);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries)  {
        try {
            connection.getServer().choiceDoubleMotor(player, motors, batteries);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries)  {
        try {
            connection.getServer().choiceDoubleCannon(player, cannons, batteries);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void choiceCrew(Player player)  {
        try {
            connection.getServer().choiceCrew(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void removeBox(Player player, Coordinate pos, BoxType type)  {
        try {
            connection.getServer().removeBox(player, pos, type);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void removeBattery(Player player, Coordinate pos)  {
        try {
            connection.getServer().removeBattery(player, pos);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void rollDice(Player player)  {
        try {
            connection.getServer().rollDice(player);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void calculateDamage(Player player, Coordinate pos)  {
        try {
            connection.getServer().calculateDamage(player, pos);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void keepBlock(Player player, Coordinate pos)  {
        try {
            connection.getServer().keepBlock(player, pos);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void Winners(int lobbyId)  {
        try {
            connection.getServer().Winners(lobbyId);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }

    public void endGame(int lobbyId)  {
        try {
            connection.getServer().endGame(lobbyId);
        } catch (RemoteException e) {
            //todo notifica al client che c'è stato un problema (connection.getConsole().methodname(); )
        }
    }
}
