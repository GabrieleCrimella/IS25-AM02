package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateGameType;
import it.polimi.ingsw.is25am02.utils.enumerations.StatePlayerType;
import it.polimi.ingsw.is25am02.network.VirtualView;

import java.rmi.RemoteException;
import java.util.List;
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
    }

    public int getLobbyId() {
        return lobbyId;
    }

    @Override
    public void onCreditUpdate(int credit){
        try {
            observer.showCreditUpdate(getNickname(),credit);
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
    public void onRemoveCrewUpdate(Coordinate coordinate, int numcrew){
        try {
            observer.showCrewRemoval(coordinate, getNickname(), numcrew);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show crew removal", e);
        }
    }

    @Override
    public void onPositionUpdate(int position){
        try {
            observer.showPositionUpdate(getNickname(),position);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show position update", e);
        }
    }

    @Override
    public void onHourglassUpdate(Hourglass hourglass){
        try {
            observer.showHourglassUpdate(hourglass);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show hourglass update", e);

        }
    }

    @Override
    public void onDiceUpdate(Dice dice){
        try {
            observer.showDiceUpdate(dice);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show dice update", e);

        }
    }

    @Override
    public void onUpdateEverything(List<Player> players, Gameboard gameboard, Card currentcard, State state){
        try {
            observer.showUpdateEverything(players, gameboard, currentcard, state);
        } catch (Exception e) {
            ServerController.logger.log(Level.SEVERE, "error in method show update everything", e);

        }
    }

    @Override
    public void onBoxRemovalUpdate(Coordinate coordinate, Box box){
        try {
            observer.showBoxRemoval(coordinate, getNickname(), box);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show box removal update", e);
        }

    }

    @Override
    public void onBoxAdditionUpdate(Coordinate coordinate, Box box){
        observer.showBoxAddition(coordinate, getNickname(), box);

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
    public void onCurrentCardUpdate(String imagepath, StateCardType statecard){
        try {
            observer.showCurrentCardUpdate(imagepath, statecard);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show current card update", e);

        }

    }

    @Override
    public void onCurrentTileUpdate(String imagepath){
        try {
            observer.showCurrentTileUpdate(imagepath, getNickname());
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show current tile update", e);

        }

    }

    @Override
    public void onVsibilityUpdate(String imagepath){
        try {
            observer.showVisibilityUpdate(imagepath);
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

    public Spaceship getSpaceship() {
        return spaceship;
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerColor getColor() {
        return color;
    }

    public VirtualView getObserver() {return observer; }

    public StatePlayerType getStatePlayer() {
        return statePlayer;
    }

    public void setStatePlayer(StatePlayerType statePlayer) { this.statePlayer = statePlayer; }

    public boolean getDeckAllowed() { return deckAllowed; }

    public void setDeckAllowed() { this.deckAllowed = true; }

    public int getNumDeck() { return numDeck; }

    public void setNumDeck(int numDeck) { this.numDeck = numDeck; }
}
