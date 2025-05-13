package it.polimi.ingsw.is25am02.network;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.AliveType;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VirtualServer extends Remote {
    void nicknameRegistration(String nickname, VirtualView client) throws RemoteException;
    void createLobby(VirtualView client, String nickname, int maxPlayers, PlayerColor color, int level) throws RemoteException;
    void getLobbies(VirtualView client) throws RemoteException;
    void joinLobby(VirtualView client, int lobbyId, String nickname, PlayerColor color) throws RemoteException;
    void isGameRunning(VirtualView client, int lobbyId) throws RemoteException;
    void ping(String nickname) throws RemoteException;

    // Metodi relativi al flusso di gioco
    void flipHourglass(String nickname) throws RemoteException;
    void hourglass(String nickname) throws RemoteException;
    void takeTile(String nickname) throws RemoteException;
    void takeTile(String nickname, String tile_imagePath) throws RemoteException;
    void takeMiniDeck(String nickname, int index) throws RemoteException;
    void returnMiniDeck(String nickname) throws RemoteException;
    void bookTile(String nickname) throws RemoteException;
    void addBookedTile(String nickname, int index, Coordinate pos, RotationType rotation) throws RemoteException;
    void returnTile(String nickname) throws RemoteException;
    //void rotateTile(String nickname) throws RemoteException;
    //void rotateBookedTile(String nickname, int index) throws RemoteException;
    void addTile(String nickname, Coordinate pos, RotationType rotation) throws RemoteException;
    void shipFinished(String nickname) throws RemoteException;
    void checkSpaceship(String nickname) throws RemoteException;
    void removeTile(String nickname, Coordinate pos) throws RemoteException;
    void checkWrongSpaceship(String nickname) throws RemoteException;
    void addCrew(String nickname, Coordinate pos, AliveType type) throws RemoteException;
    void ready(String nickname) throws RemoteException;
    void playNextCard(String nickname) throws RemoteException;
    void earlyLanding(String nickname) throws RemoteException;
    void choice(String nickname, boolean choice) throws RemoteException;
    void removeCrew(String nickname, Coordinate pos) throws RemoteException;
    void choiceBox(String nickname, boolean choice) throws RemoteException;
    void moveBox(String nickname, Coordinate start, Coordinate end, BoxType boxType, boolean on) throws RemoteException;
    void choicePlanet(String nickname, int index) throws RemoteException;
    void choiceDoubleMotor(String nickname, List<Coordinate> motors, List<Coordinate> batteries) throws RemoteException;
    void choiceDoubleCannon(String nickname, List<Coordinate> cannons, List<Coordinate> batteries) throws RemoteException;
    void choiceCrew(String nickname) throws RemoteException;
    void removeBox(String nickname, Coordinate pos, BoxType type) throws RemoteException;
    void removeBattery(String nickname, Coordinate pos) throws RemoteException;
    void rollDice(String nickname) throws RemoteException;
    void calculateDamage(String nickname, Coordinate pos) throws RemoteException;
    void keepBlock(String nickname, Coordinate pos) throws RemoteException;
    void Winners(int lobbyId) throws RemoteException;
    void endGame(int lobbyId) throws RemoteException;
}
