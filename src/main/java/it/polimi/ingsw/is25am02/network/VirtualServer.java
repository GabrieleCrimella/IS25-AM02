package it.polimi.ingsw.is25am02.network;

import it.polimi.ingsw.is25am02.model.Coordinate;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VirtualServer extends Remote {
    void nicknameRegistration(String nickname, VirtualView client) throws RemoteException;
    void createLobby(VirtualView client, String nickname, int maxPlayers, PlayerColor color, int level) throws RemoteException;
    void getLobbies(VirtualView client) throws RemoteException;
    void joinLobby(VirtualView client, int lobbyId, String nickname, PlayerColor color) throws RemoteException;
    void isGameRunning(VirtualView client, int lobbyId) throws RemoteException;

    // Metodi relativi al flusso di gioco
    void flipHourglass(Player player) throws RemoteException;
    void takeTile(Player player) throws RemoteException;
    void takeTile(Player player, Tile tile) throws RemoteException;
    void takeMiniDeck(Player player, int index) throws RemoteException;
    void returnMiniDeck(Player player) throws RemoteException;
    void bookTile(Player player) throws RemoteException;
    void addBookedTile(Player player, int index, Coordinate pos, RotationType rotation) throws RemoteException;
    void returnTile(Player player) throws RemoteException;
    void addTile(Player player, Coordinate pos, RotationType rotation) throws RemoteException;
    void shipFinished(Player player) throws RemoteException;
    void checkSpaceship(Player player) throws RemoteException;
    void removeTile(Player player, Coordinate pos) throws RemoteException;
    void checkWrongSpaceship(Player player) throws RemoteException;
    void addCrew(Player player, Coordinate pos, AliveType type) throws RemoteException;
    void ready(Player player) throws RemoteException;
    void playNextCard(Player player) throws RemoteException;
    void earlyLanding(Player player) throws RemoteException;
    void choice(Player player, boolean choice) throws RemoteException;
    void removeCrew(Player player, Coordinate pos) throws RemoteException;
    void choiceBox(Player player, boolean choice) throws RemoteException;
    void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on) throws RemoteException;
    void choicePlanet(Player player, int index) throws RemoteException;
    void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries) throws RemoteException;
    void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries) throws RemoteException;
    void choiceCrew(Player player) throws RemoteException;
    void removeBox(Player player, Coordinate pos, BoxType type) throws RemoteException;
    void removeBattery(Player player, Coordinate pos) throws RemoteException;
    void rollDice(Player player) throws RemoteException;
    void calculateDamage(Player player, Coordinate pos) throws RemoteException;
    void keepBlock(Player player, Coordinate pos) throws RemoteException;
    void Winners(int lobbyId) throws RemoteException;
    void endGame(int lobbyId) throws RemoteException;
}
