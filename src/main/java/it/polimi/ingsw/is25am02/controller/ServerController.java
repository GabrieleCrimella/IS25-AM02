package it.polimi.ingsw.is25am02.controller;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//todo domanda importantissima: che succede se il controller non trova il giocatore?
public class ServerController implements Game_Interface {
    private final Map<Integer, Lobby> lobbies = new ConcurrentHashMap<>();
    private final Map<Integer, GameSession> activeGames = new ConcurrentHashMap<>();
    private int nextLobbyId = 1;

    public synchronized int createLobby(int maxPlayers, String nickname, PlayerColor color, int level) {
        Player p = new Player(new Spaceship(level), nickname, color);
        p.setLobbyId(nextLobbyId++);
        Lobby lobby = new Lobby(p.getLobbyId(), maxPlayers, p, level);
        lobbies.put(lobby.getId(), lobby);
        return lobby.getId();
    }

    public synchronized boolean joinLobby(int lobbyId, String nickname, PlayerColor color) {
        Lobby lobby = lobbies.get(lobbyId);
        Player p = new Player(new Spaceship(lobby.getLevel()), nickname, color);
        p.setLobbyId(lobbyId);
        if (lobby != null && lobby.addPlayer(p)) {
            if (lobby.isFull()) {
                startGame(lobby);
                lobbies.remove(lobbyId);
            }
            return true;
        }
        return false;
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
    }

    public synchronized boolean isGameRunning(int lobbyId) {
        return activeGames.containsKey(lobbyId);
    }

//    @Override
//    public Tile takeTile(int lobbyId, String playerId) {
//        GameSession gameSession = activeGames.get(lobbyId);
//        if (gameSession != null) {
//            return gameSession.getGame().takeTile(findPlayerInGame(lobbyId, playerId));
//        }
//        return null;
//    }

    @Override
    public void flipHourglass(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().flipHourglass(player);
    }

    @Override
    public Tile takeTile(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            return g.getGame().takeTile(player);
        return null; //todo?????
    }

    @Override
    public Tile takeTile(Player player, Tile tile) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            return g.getGame().takeTile(player, tile);
        return null;//todo?????
    }

    @Override
    public List<Card> takeMiniDeck(Player player, int index) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            return g.getGame().takeMiniDeck(player, index);
        return null;//todo?????
    }

    @Override
    public void returnMiniDeck(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().returnMiniDeck(player);
    }

    @Override
    public void bookTile(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().bookTile(player);
    }

    @Override
    public void addBookedTile(Player player, int index, Coordinate pos) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().addBookedTile(player, index, pos);
    }

    @Override
    public void returnTile(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().returnTile(player);
    }

    @Override
    public void addTile(Player player, Coordinate pos) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().addTile(player, pos);
    }

    @Override
    public void shipFinished(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().shipFinished(player);
    }

    @Override
    public void checkSpaceship(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().checkSpaceship(player);
    }

    @Override
    public Optional<List<boolean[][]>> removeTile(Player player, Coordinate pos) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            return g.getGame().removeTile(player, pos);
        return null;//todo?????
    }

    @Override
    public void checkWrongSpaceship(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().checkWrongSpaceship(player);
    }

    @Override
    public void addCrew(Player player, Coordinate pos, AliveType type) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().addCrew(player, pos, type);
    }

    @Override
    public void ready(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().ready(player);
    }

    @Override
    public void playNextCard(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().playNextCard(player);
    }

    @Override
    public void earlyLanding(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().earlyLanding(player);
    }

    //todo::su quale game lo chiamo?
    @Override
    public HashMap<Player, Integer> getPosition() {
//        GameSession g = activeGames.get(player.getLobbyId());
//        if (g != null)
//            return g.getGame().getPosition();
        return null;//todo?????
    }

    @Override
    public List<Tile> possibleChoice(Player player, TileType type) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            return g.getGame().possibleChoice(player, type);
        return null;//todo?????
    }

    @Override
    public void choice(Player player, boolean choice) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().choice(player, choice);
    }

    @Override
    public void removeCrew(Player player, Coordinate pos) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().removeCrew(player, pos);
    }

    @Override
    public List<Box> choiceBox(Player player, boolean choice) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            return g.getGame().choiceBox(player, choice);
        return null;//todo?????
    }

    @Override
    public void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().moveBox(player, start, end, boxType, on);
    }

    @Override
    public List<Box> choicePlanet(Player player, int index) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            return g.getGame().choicePlanet(player, index);
        return null;//todo?????
    }

    @Override
    public void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().choiceDoubleMotor(player, motors, batteries);
    }

    @Override
    public void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().choiceDoubleCannon(player, cannons, batteries);
    }

    @Override
    public void choiceCrew(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().choiceCrew(player);
    }

    @Override
    public void removeBox(Player player, Coordinate pos, BoxType type) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().removeBox(player, pos, type);
    }

    @Override
    public void removeBattery(Player player, Coordinate pos) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().removeBattery(player, pos);
    }

    @Override
    public void rollDice(Player player) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().rollDice(player);
    }

    @Override
    public void effect(Game game) {
        GameSession g = activeGames.get(game.getPlayers().getFirst().getLobbyId());
        if (g != null)
            g.getGame().effect(game);
    }

    @Override
    public void calculateDamage(Player player, Coordinate pos) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().calculateDamage(player, pos);
    }

    @Override
    public void keepBlock(Player player, boolean[][] tilesToKeep) {
        GameSession g = activeGames.get(player.getLobbyId());
        if (g != null)
            g.getGame().keepBlock(player, tilesToKeep);
    }

    @Override
    public ArrayList<Player> getWinners() {
        //todo::quale game guardo??? manda oarametro in getWinners
        return null;//todo?????
    }
}
