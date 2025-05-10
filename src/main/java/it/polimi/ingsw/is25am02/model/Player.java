package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.network.VirtualView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import static it.polimi.ingsw.is25am02.utils.enumerations.StatePlayerType.NOT_FINISHED;

public class Player implements UpdateListener {
    private final Spaceship spaceship;
    private final String nickname;
    private final PlayerColor color;
    private final VirtualView observer;
    private StatePlayerType statePlayer;
    private boolean deckAllowed;
    private int numDeck;
    private final int lobbyId;
    private ConcurrentHashMap<String, VirtualView> observers;

    public Player(Spaceship spaceship, String nickname, PlayerColor color, VirtualView observer, int lobbyId) {
        this.spaceship = spaceship;
        this.nickname = nickname;
        this.color = color;
        this.observer = observer;
        this.lobbyId = lobbyId;
        this.statePlayer = NOT_FINISHED;
        this.deckAllowed = false;
        this.numDeck = -1;
        this.spaceship.setListener(this);
        this.spaceship.getSpaceshipIterator().setListener(this);
    }

    public void setObservers(ConcurrentHashMap<String, VirtualView> observers) {
        this.observers = observers;
    }

    public int getLobbyId() {
        return lobbyId;
    }

    @Override
    public void onCreditUpdate(int credit){
        try {
            observer.showCreditUpdate(nickname,credit);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show credit update", e);
        }
    }

    @Override
    public void onRemoveTileUpdate(Coordinate coordinate){
        try {
            observer.showTileRemoval(coordinate,getNickname());
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show tile removal", e);
        }
    }

    @Override
    public void onRemoveBatteryUpdate(int battery, Coordinate coordinate){
        try {
            observer.showBatteryRemoval(coordinate,getNickname(),battery);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show battery removal", e);

        }
    }

    @Override
    public void onRemoveCrewUpdate(String nickname, Coordinate coordinate){
        try {
            observer.showCrewRemoval(coordinate, nickname);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show crew removal", e);
        }
    }

    @Override
    public void onPositionUpdate(String nickname, int position){
        try {
            observer.showPositionUpdate(nickname,position);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show position update", e);
        }
    }

    @Override
    public void onHourglassUpdate(){
        try {
            observer.showHourglassUpdate();
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show hourglass update", e);

        }
    }

    @Override
    public void onDiceUpdate(String nickname,Dice dice){
        try {
            observer.showDiceUpdate(nickname, dice.getResult());
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show dice update", e);

        }
    }

    @Override
    public void onTileAdditionToSpaceship(String nickname, Tile tile, Coordinate coordinate){
        try {
            observer.showTileAdditionUpdate(tile.getImagePath(),tile.getConnectors(),tile.getRotationType(), tile.getType(),tile.getNumMaxBattery(),tile.getNumMaxBox(),nickname,coordinate);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show tile addition update", e);
        }
    }


    @Override
    public void onUpdateEverything(int level, List<Player> players, Gameboard gameboard, Card currentcard, State state, boolean[][] mask, int[] positions){
        try {
            ArrayList<String> nicknamePlayers = new ArrayList<>();
            HashMap<String, PlayerColor> playersColor = new HashMap<>();
            for(Player p : players){
                playersColor.put(p.getNickname(),p.getColor());
            }

            observer.showUpdateEverything(level, playersColor,currentcard.getImagePath(),currentcard.getStateCard(),currentcard.getCardType(),state.getPhase(),state.getCurrentPlayer().getNickname(), mask, positions);
        } catch (Exception e) {
            ServerController.logger.log(Level.SEVERE, "error in method show update everything", e);

        }
    }


    @Override
    public void onBoxUpdate(Coordinate coordinate, List<BoxType> box){
        try {
            observer.showBoxUpdate(coordinate, getNickname(), box);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show box addition update", e);
        }

    }

    @Override
    public void onMiniDeckUpdate(int deck){
        try {
            observer.showMinideckUpdate(getNickname(), deck);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method mini deck update", e);

        }

    }

    @Override
    public void onCurrentCardUpdate(String imagepath, StateCardType statecard, CardType type){
        try {
            observer.showCurrentCardUpdate(imagepath, statecard, type);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show current card update", e);

        }

    }

    @Override
    public void onCurrentTileUpdate(String nickname, Tile tile){
        try {
            observer.showCurrentTileUpdate(tile.getImagePath(),tile.getConnectors(),tile.getRotationType(),tile.getType(),tile.getNumMaxBattery(),tile.getNumMaxBox(), nickname);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show current tile update", e);

        }

    }

    @Override
    public void onCurrentTileNullityUpdate(String nickname){
        try {
            observer.showCurrentTileNullityUpdate(nickname);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show current tile nullity update", e);
        }
    }

    @Override
    public void onVsibilityUpdate(String nickname, Tile tile){
        try {
            observer.showVisibilityUpdate(tile.getImagePath(),tile.getConnectors(),tile.getRotationType(), tile.getType(),tile.getNumMaxBattery(),tile.getNumMaxBox());
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show visibility update", e);

        }

    }

    @Override
    public void onTileRemovalFromHTUpdate(String imagepath){
        try {
            observer.showTileRemovalFromHeapTile(imagepath);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show tile removal from Heap Tile", e);

        }

    }

    @Override
    public void onDeckAllowedUpdate(){
        try {
            observer.showDeckAllowUpdate(getNickname());
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show deck allow update", e);
        }
    }

    @Override
    public void onGameStateUpdate(StateGameType stateGameType){
        try {
            observer.showGameStateUpdate(stateGameType);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show game state update", e);
        }
    }

    @Override
    public void onCardStateUpdate(StateCardType stateCardType){
        try {
            observer.showCardStateUpdate(stateCardType);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show card state update", e);
        }

    }

    @Override
    public void onPlayerStateUpdate(String nickname, StatePlayerType statePlayerType){
        try {
            observer.showPlayerStateUpdate(nickname, statePlayerType);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show state player update", e);
        }

    }

    @Override
    public void onBookTileUpdate(String nickname){
        try {
            observer.showBookTileUpdate(nickname);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show book tile update", e);
        }
    }
    @Override
    public void onCurrentPlayerUpdate(String nickname){
        try {
            observer.showCurrentPlayerUpdate(nickname);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show current player update", e);
        }
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerColor getColor() {
        return color;
    }

    public VirtualView getObserver() { return observer; }

    public StatePlayerType getStatePlayer() {
        return statePlayer;
    }

    public void setStatePlayer(StatePlayerType statePlayer) {
        this.statePlayer = statePlayer;
        for (String nick: observers.keySet()){
            try {
                observers.get(nick).showPlayerStateUpdate(nickname, statePlayer);
            } catch (RemoteException e) {
                ServerController.logger.log(Level.SEVERE, "error in setstateplayer", e);
            }
        }
    }

    public boolean getDeckAllowed() { return deckAllowed; }

    public void setDeckAllowed() { this.deckAllowed = true; }

    public int getNumDeck() { return numDeck; }

    public void setNumDeck(int numDeck) { this.numDeck = numDeck; }
}
