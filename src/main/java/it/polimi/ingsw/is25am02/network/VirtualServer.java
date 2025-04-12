package it.polimi.ingsw.is25am02.network;

import it.polimi.ingsw.is25am02.controller.Lobby;
import it.polimi.ingsw.is25am02.controller.exception.ColorAlreadyTakenException;
import it.polimi.ingsw.is25am02.controller.exception.LobbyNotFoundException;
import it.polimi.ingsw.is25am02.controller.exception.NicknameAlreadyExistsException;
import it.polimi.ingsw.is25am02.controller.exception.PlayerNotFoundException;
import it.polimi.ingsw.is25am02.model.Coordinate;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.List;
import java.util.Map;

public interface VirtualServer{
    int createLobby(int maxPlayers, String nickname, PlayerColor color, int level);

    void nicknameRegistration(String nickname) throws NicknameAlreadyExistsException;

    boolean joinLobby(int lobbyId, String nickname, PlayerColor color)
            throws LobbyNotFoundException, ColorAlreadyTakenException, PlayerNotFoundException;

    void startGame(Lobby lobby);

    Map<Integer, Lobby> getLobbies();

    void endGame(int lobbyId);

    boolean isGameRunning(int lobbyId);

    void flipHourglass(Player player) throws PlayerNotFoundException;

    void takeTile(Player player) throws PlayerNotFoundException;

    void takeTile(Player player, Tile tile) throws PlayerNotFoundException;

    void takeMiniDeck(Player player, int index) throws PlayerNotFoundException;

    void returnMiniDeck(Player player) throws PlayerNotFoundException;

    void bookTile(Player player) throws PlayerNotFoundException;

    void addBookedTile(Player player, int index, Coordinate pos, RotationType rotation) throws PlayerNotFoundException;

    void returnTile(Player player) throws PlayerNotFoundException;

    void addTile(Player player, Coordinate pos, RotationType rotation) throws PlayerNotFoundException;

    void shipFinished(Player player) throws PlayerNotFoundException;

    void checkSpaceship(Player player) throws PlayerNotFoundException;

    void removeTile(Player player, Coordinate pos) throws PlayerNotFoundException;

    void checkWrongSpaceship(Player player) throws PlayerNotFoundException;

    void addCrew(Player player, Coordinate pos, AliveType type) throws PlayerNotFoundException;

    void ready(Player player) throws PlayerNotFoundException;

    void playNextCard(Player player) throws PlayerNotFoundException;

    void earlyLanding(Player player) throws PlayerNotFoundException;

    void choice(Player player, boolean choice) throws PlayerNotFoundException;

    void removeCrew(Player player, Coordinate pos) throws PlayerNotFoundException;

    void choiceBox(Player player, boolean choice) throws PlayerNotFoundException;

    void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on) throws PlayerNotFoundException;

    void choicePlanet(Player player, int index) throws PlayerNotFoundException;

    void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries) throws PlayerNotFoundException;

    void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries) throws PlayerNotFoundException;

    void choiceCrew(Player player) throws PlayerNotFoundException;

    void removeBox(Player player, Coordinate pos, BoxType type) throws PlayerNotFoundException;

    void removeBattery(Player player, Coordinate pos) throws PlayerNotFoundException;

    void rollDice(Player player) throws PlayerNotFoundException;

    void effect(Game game) throws PlayerNotFoundException;

    void calculateDamage(Player player, Coordinate pos) throws PlayerNotFoundException;

    void keepBlock(Player player, Coordinate pos) throws PlayerNotFoundException;

    void Winners(int lobbyId);
}
