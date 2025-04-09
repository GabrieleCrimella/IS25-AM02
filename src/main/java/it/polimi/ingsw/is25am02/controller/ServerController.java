package it.polimi.ingsw.is25am02.controller;

import it.polimi.ingsw.is25am02.controller.exception.LobbyNotFoundException;
import it.polimi.ingsw.is25am02.controller.exception.ColorAlreadyTakenException;
import it.polimi.ingsw.is25am02.controller.exception.NicknameAlreadyExistsException;
import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.controller.exception.PlayerNotFoundException;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServerController {
    private final Map<Integer, Lobby> lobbies = new ConcurrentHashMap<>();
    private final Map<Integer, GameSession> activeGames = new ConcurrentHashMap<>();
    private int nextLobbyId = 1;
    private final List<String> nicknames = new ArrayList<>();

    //todo: controller client blocca la chiamata di questo metodo nei casi non consentiti
    public synchronized int createLobby(int maxPlayers, String nickname, PlayerColor color, int level) {
        Player p = new Player(new Spaceship(level), nickname, color);
        p.setLobbyId(nextLobbyId++);
        Lobby lobby = new Lobby(p.getLobbyId(), maxPlayers, p, level);
        lobbies.put(lobby.getId(), lobby);
        return lobby.getId();
    }

    public void nicknameRegistration(String nickname) throws NicknameAlreadyExistsException {
        if (!nicknames.contains(nickname)) {
            nicknames.add(nickname);
        } else {
            throw new NicknameAlreadyExistsException("Nickname already exists");
        }
    }

    public synchronized boolean joinLobby(int lobbyId, String nickname, PlayerColor color) throws LobbyNotFoundException, ColorAlreadyTakenException, PlayerNotFoundException {
        //controllo dell'username che sia univoco. se non presente nel server, il metodo viene eseguito
        //altrimenti viene ritornara un'exception???

        if (!lobbies.containsKey(lobbyId)) {
            throw new LobbyNotFoundException("Lobby not found");
        }
        if (lobbies.get(lobbyId).getPlayers().stream().anyMatch(player -> player.getColor().equals(color))) {
            throw new ColorAlreadyTakenException("Color already taken");
        }
        if (!nicknames.contains(nickname)) {
            throw new PlayerNotFoundException("Nickname already exists");
        }
        Lobby lobby = lobbies.get(lobbyId);
        Player p = new Player(new Spaceship(lobby.getLevel()), nickname, color);
        p.setLobbyId(lobbyId);
        if (lobby.addPlayer(p)) {
            if (lobby.isFull()) {
                startGame(lobby);
                lobbies.remove(lobbyId);
            }
            return true;
        }
        return false;
    }

    public Map<Integer, Lobby> getLobbies() {
        return lobbies;
    }

    private void startGame(Lobby lobby) {
        //Game game = new Game(lobby.getPlayers(), lobby.getLevel());
        GameSession gameSession = new GameSession(lobby.getId(), lobby.getPlayers(), lobby.getLevel());
        activeGames.put(lobby.getId(), gameSession);
        lobby.startGame();
        gameSession.start();
    }

    public synchronized void endGame(int lobbyId) {
        GameSession gameSession = activeGames.remove(lobbyId);
        if (gameSession != null) {
            gameSession.endGame();
        }
        lobbies.get(lobbyId).getPlayers().stream().forEach(player -> nicknames.remove(player.getNickname()));
        lobbies.remove(lobbyId);
    }

    public synchronized boolean isGameRunning(int lobbyId) {
        return activeGames.containsKey(lobbyId);
    }

    private GameSession getGameFromPlayer(Player player) throws PlayerNotFoundException {
        GameSession g = activeGames.get(player.getLobbyId());
        if (!lobbies.get(player.getLobbyId()).isGameStarted() || g == null)
            throw new PlayerNotFoundException("Player not found in any game");
        else
            return g;
    }

//    @Override
//    public Tile takeTile(int lobbyId, String playerId) {
//        GameSession gameSession = activeGames.get(lobbyId);
//        if (gameSession != null) {
//            return gameSession.getGame().takeTile(findPlayerInGame(lobbyId, playerId));
//        }
//        return null;
//    }

    public void flipHourglass(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().flipHourglass(player);
    }

    public void takeTile(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().takeTile(player);
    }

    public void takeTile(Player player, Tile tile) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().takeTile(player, tile);
    }

    public void takeMiniDeck(Player player, int index) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().takeMiniDeck(player, index);
    }

    public void returnMiniDeck(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().returnMiniDeck(player);
    }

    public void bookTile(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().bookTile(player);
    }

    public void addBookedTile(Player player, int index, Coordinate pos, RotationType rotation) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().addBookedTile(player, index, pos, rotation);
    }

    public void returnTile(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().returnTile(player);
    }

    public void addTile(Player player, Coordinate pos, RotationType rotation) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().addTile(player, pos, rotation);
    }

    public void shipFinished(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().shipFinished(player);
    }

    public void checkSpaceship(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().checkSpaceship(player);
    }

    public void removeTile(Player player, Coordinate pos) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().removeTile(player, pos);
    }

    public void checkWrongSpaceship(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().checkWrongSpaceship(player);
    }

    public void addCrew(Player player, Coordinate pos, AliveType type) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().addCrew(player, pos, type);
    }

    public void ready(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().ready(player);
    }

    public void playNextCard(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().playNextCard(player);
    }

    public void earlyLanding(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().earlyLanding(player);
    }

    public void choice(Player player, boolean choice) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().choice(player, choice);
    }

    public void removeCrew(Player player, Coordinate pos) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().removeCrew(player, pos);
    }

    public void choiceBox(Player player, boolean choice) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().choiceBox(player, choice);
    }

    public void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().moveBox(player, start, end, boxType, on);
    }

    public void choicePlanet(Player player, int index) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().choicePlanet(player, index);
    }

    public void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().choiceDoubleMotor(player, motors, batteries);
    }

    public void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().choiceDoubleCannon(player, cannons, batteries);
    }

    public void choiceCrew(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().choiceCrew(player);
    }

    public void removeBox(Player player, Coordinate pos, BoxType type) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().removeBox(player, pos, type);
    }

    public void removeBattery(Player player, Coordinate pos) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().removeBattery(player, pos);
    }

    public void rollDice(Player player) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().rollDice(player);
    }

    public void effect(Game game) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(game.getPlayers().getFirst());
        g.getGame().effect(game);
    }

    public void calculateDamage(Player player, Coordinate pos) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().calculateDamage(player, pos);
    }

    public void keepBlock(Player player, Coordinate pos) throws PlayerNotFoundException {
        GameSession g = getGameFromPlayer(player);
        g.getGame().keepBlock(player, pos);
    }

    public void Winners(int lobbyId) {
        GameSession g = activeGames.get(lobbyId);
        g.getGame().Winners();
    }
}
