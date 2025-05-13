package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Card {
    private final int level;
    private StateCardType stateCard;
    private final String imagePath;
    private boolean testFlight;
    private String comment;
    protected ConcurrentHashMap<String, VirtualView> observers;

    public Card(int level, StateCardType stateCard, String imagePath, String comment, boolean testFlight) {
        this.level = level;
        this.stateCard = stateCard;
        this.imagePath = imagePath;
        this.comment = comment;
        this.testFlight = testFlight;
    }

    public void setObservers(ConcurrentHashMap<String, VirtualView> observers) {
        this.observers = observers;
    }

    public String getComment() {
        return comment;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean getTestFlight() { return testFlight; }

    public int getLevel() {
        return level;
    }

    public StateCardType getStateCard() {
        return stateCard;
    }

    public void setStateCard(StateCardType stateCardType) {
        this.stateCard = stateCardType;
        for (String nick:observers.keySet()) {
            try {
                observers.get(nick).showCardStateUpdate(stateCardType);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void choice(Game game, Player player, boolean choice) throws UnsupportedOperationException, IllegalPhaseException {
        throw new UnsupportedOperationException("Not supported method for " + this.getCardType());
    }

    public void removeCrew(Game game, Player player, Tile cabin) throws UnsupportedOperationException, IllegalPhaseException, IllegalRemoveException {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void choiceBox(Game game, Player player, boolean choice) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void moveBox(Game game, Player player, List<Box> start, List<Box> end, BoxType boxType, boolean on) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void choicePlanet(Game game, Player player, int index) throws UnsupportedOperationException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void choiceDoubleMotor(Game game, Player player, List<Coordinate> motors, List<Coordinate> batteries) throws UnsupportedOperationException, IllegalPhaseException, IllegalRemoveException {// il primo tile è dmotor, il secondo battery storage
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    //solo per testing
    public void setCurrentPhase(int currentPhase) throws  IllegalPhaseException{
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    //solo per testing
    public void setPhase(int phase) throws IllegalPhaseException{
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void choiceDoubleCannon(Game game, Player player, List<Coordinate> cannons, List<Coordinate> batteries) throws UnsupportedOperationException, IllegalPhaseException, IllegalRemoveException {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void choiceCrew(Game game, Player player) throws UnsupportedOperationException, IllegalPhaseException {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void removeBox(Game game, Player player, Tile storage, BoxType type) throws UnsupportedOperationException, IllegalRemoveException {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void removeBattery(Game game, Player player, Tile storage) throws UnsupportedOperationException, IllegalRemoveException {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void calculateDamage(Game game, Player player, Optional<Tile> batterystorage) throws UnsupportedOperationException, IllegalPhaseException, IllegalRemoveException {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void effect(Game game) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void keepBlocks(Game game, Player player, Coordinate pos) throws UnsupportedOperationException, IllegalPhaseException {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public LinkedList<Box> getBoxesWon() {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public ArrayList<LinkedList<Box>> getPlanetOffers() {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public LinkedList<BoxType> getBoxesWonTypes() {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public ArrayList<LinkedList<BoxType>> getPlanetOffersTypes() {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public CardType getCardType() {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void addBoxWon(Box box) {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void addPlanetOffers(LinkedList<Box> boxes) {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void clearBoxWon() {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public void clearPlanetOffers() {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }

    public HashMap<Player, Integer> getFly() {
        throw new UnsupportedOperationException("Not supported method" + this.getCardType());
    }
}
