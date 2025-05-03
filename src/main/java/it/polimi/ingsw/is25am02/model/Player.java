package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.network.VirtualView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
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
        this.spaceship.getSpaceshipIterator().setListener(this);
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
    public void onHourglassUpdate(){
        try {
            observer.showHourglassUpdate();
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show hourglass update", e);

        }
    }

    @Override
    public void onDiceUpdate(Dice dice){
        try {
            observer.showDiceUpdate(dice.getResult());
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show dice update", e);

        }
    }

    @Override
    public void onTileAdditionToSpaceship(String imagepath, Coordinate coordinate){
        try {
            observer.showTileAdditionUpdate(imagepath,getNickname(),coordinate);
        } catch (RemoteException e) {
            ServerController.logger.log(Level.SEVERE, "error in method show tile addition update", e);
        }
    }


    @Override
    public void onUpdateEverything(int level, List<Player> players, Gameboard gameboard, Card currentcard, State state, boolean[][] mask){
        try {
            ArrayList<String> nicknamePlayers = new ArrayList<>();
            HashMap<String, PlayerColor> playersColor = new HashMap<>();
            HashMap<String, Integer> positions = new HashMap<>();
            for(Player p:players){
                nicknamePlayers.add(p.getNickname());
                playersColor.put(p.getNickname(),p.getColor());
                positions.put(p.getNickname(),gameboard.getPositions().get(p));
            }

            observer.showUpdateEverything(level,nicknamePlayers, playersColor,positions,currentcard.getImagePath(),currentcard.getStateCard(),state.getPhase(),state.getCurrentPlayer().getNickname(), mask);
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
