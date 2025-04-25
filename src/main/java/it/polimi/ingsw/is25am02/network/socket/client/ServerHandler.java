package it.polimi.ingsw.is25am02.network.socket.client;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.AliveType;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;

import java.rmi.RemoteException;
import java.util.List;

public class ServerHandler implements VirtualServer {

    //todo per ogni metodo creo un oggetto command che riempio con i parametri passati e poi uso out di socket
    // (dati da prendere dal socketClient)


    @Override
    public void ping(String nickname) throws RemoteException {

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
    public void flipHourglass(String nickname) throws RemoteException {

    }

    @Override
    public void takeTile(String nickname) throws RemoteException {

    }

    @Override
    public void takeTile(String nickname, Tile tile) throws RemoteException {

    }

    @Override
    public void takeMiniDeck(String nickname, int index) throws RemoteException {

    }

    @Override
    public void returnMiniDeck(String nickname) throws RemoteException {

    }

    @Override
    public void bookTile(String nickname) throws RemoteException {

    }

    @Override
    public void addBookedTile(String nickname, int index, Coordinate pos, RotationType rotation) throws RemoteException {

    }

    @Override
    public void returnTile(String nickname) throws RemoteException {

    }

    @Override
    public void addTile(String nickname, Coordinate pos, RotationType rotation) throws RemoteException {

    }

    @Override
    public void shipFinished(String nickname) throws RemoteException {

    }

    @Override
    public void checkSpaceship(String nickname) throws RemoteException {

    }

    @Override
    public void removeTile(String nickname, Coordinate pos) throws RemoteException {

    }

    @Override
    public void checkWrongSpaceship(String nickname) throws RemoteException {

    }

    @Override
    public void addCrew(String nickname, Coordinate pos, AliveType type) throws RemoteException {

    }

    @Override
    public void ready(String nickname) throws RemoteException {

    }

    @Override
    public void playNextCard(String nickname) throws RemoteException {

    }

    @Override
    public void earlyLanding(String nickname) throws RemoteException {

    }

    @Override
    public void choice(String nickname, boolean choice) throws RemoteException {

    }

    @Override
    public void removeCrew(String nickname, Coordinate pos) throws RemoteException {

    }

    @Override
    public void choiceBox(String nickname, boolean choice) throws RemoteException {

    }

    @Override
    public void moveBox(String nickname, Coordinate start, Coordinate end, BoxType boxType, boolean on) throws RemoteException {

    }

    @Override
    public void choicePlanet(String nickname, int index) throws RemoteException {

    }

    @Override
    public void choiceDoubleMotor(String nickname, List<Coordinate> motors, List<Coordinate> batteries) throws RemoteException {

    }

    @Override
    public void choiceDoubleCannon(String nickname, List<Coordinate> cannons, List<Coordinate> batteries) throws RemoteException {

    }

    @Override
    public void choiceCrew(String nickname) throws RemoteException {

    }

    @Override
    public void removeBox(String nickname, Coordinate pos, BoxType type) throws RemoteException {

    }

    @Override
    public void removeBattery(String nickname, Coordinate pos) throws RemoteException {

    }

    @Override
    public void rollDice(String nickname) throws RemoteException {

    }

    @Override
    public void calculateDamage(String nickname, Coordinate pos) throws RemoteException {

    }

    @Override
    public void keepBlock(String nickname, Coordinate pos) throws RemoteException {

    }

    @Override
    public void Winners(int lobbyId) throws RemoteException {

    }

    @Override
    public void endGame(int lobbyId) throws RemoteException {

    }
}
