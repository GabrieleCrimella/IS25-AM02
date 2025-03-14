package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;

import java.util.*;

import static it.polimi.ingsw.is25am02.model.enumerations.StateCardType.*;
import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.EFFECT_ON_PLAYER;
import static it.polimi.ingsw.is25am02.model.enumerations.StatePlayerType.IN_GAME;

public class Game implements Game_Interface {
    private int diceResult;

    private String gameName;
    private int maxAllowedPlayers;
    private List<Player> players;
    private int level;
    private CardDeck deck;
    private Hourglass hourglass;
    private HeapTiles heapTile;
    private Gameboard globalBoard;
    private State currentState;

    public Game(List<Player> p, int level){
        this.players = p;
        this.level = level;
        this.diceResult = 0;
    }

    //getter
    public CardDeck getDeck(){ return deck; }
    public Gameboard getGameboard(){
        return globalBoard;
    }
    private List<Player> getPlayers(){ return null; }
    public String getName(){ return gameName; }
    public int getlevel(){
        return level;
    }
    public int getMaxAllowedPlayers(){ return maxAllowedPlayers; }
    public HeapTiles getHeapTile() {
        return heapTile;
    }
    public Hourglass getHourglass(){
        return hourglass;
    }
    public State getCurrentState(){ return currentState; }
    public int getDiceResult() {
        return diceResult;
    }

    public void setDiceResult(int diceResult) {
        this.diceResult = getGameboard().getDice().pickRandomNumber();
    }
    public Set<Tile> getVisibleTiles(){
        return heapTile.getVisibleTiles();
    }

    public Player getCurrentPlayer() {
        return currentState.getCurrentPlayer();
    }
    public Card getCurrentCard(){ return currentState.getCurrentCard(); }

    //todo
    public void nextPlayer(){
        //Player diventa il prossimo player
        //Se il player è l'ultimo il currentPlayer deve diventare il nuovo primo e cambiare carta. (forse exception forse viene implementato qui dentro)
        //Gioca la carta corrente con il player nuovo
    }
    //todo
    public void previousPlayer(){
        //come next player ma torna indietro
    }

    @Override
    public Game GameCreator(List<Player> p, int level) {
        return new Game(p, level);
    }
    //todo
    @Override
    public void flipHourglass() {

    }
    //todo
    @Override
    public Tile takeTile(Player player) {
        return null;
    }
    //todo
    @Override
    public Tile takeTile(Player player, Tile tile) {
        return null;
    }
    //todo
    @Override
    public void returnTile(Player player, Tile tile) {

    }
    //todo
    @Override
    public void addTile(Player player, Tile tile, int x, int y) {

    }
    //todo
    @Override
    public void shipFinished(Player player) {

    }
    //todo
    @Override
    public boolean checkSpaceship(Player player) {
        return false;
    }
    //todo
    @Override
    public void removeTile(Player player, int x, int y) {

    }
    //todo
    @Override
    public void addCrew(Player player, int x, int y, AliveType type) {

    }
    //todo
    @Override
    public void playNextCard() {

    }
    //todo
    @Override
    public HashMap<Integer, Player> getPosition() {
        return null;
    }
    //todo
    @Override
    public HashMap<Player, StatePlayerType> getState() {
        return null;
    }
    //todo
    @Override
    public List<Tile> possibleChoice(Player player, TileType type) {
        return List.of();
    }

    @Override
    public void choice(Player player, boolean choice) {
        //State Control
        if (getCurrentCard().getStateCard() == DECISION && player.getStatePlayer() == IN_GAME &&
            getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            getCurrentCard().choice(this,player, choice);
        }
        else throw new IllegalStateException();
    }

    @Override
    public void removeCrew(Player player, Cabin cabin) {
        //StateControl
        if (getCurrentCard().getStateCard() == REMOVE && player.getStatePlayer() == IN_GAME &&
            getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player) &&
            player.getSpaceship().own(cabin)) {
            getCurrentCard().removeCrew(this, player, cabin);
        }
        else throw new IllegalStateException();
    }
    //todo
    @Override
    public List<Box> choiceBox(Player player, boolean choice) {
        return List.of();
    }

    @Override
    public void moveBox(Player player, List<Box> start, List<Box> end, Box box, boolean on) throws Exception {
        //State Control
        if(getCurrentCard().getStateCard() == BOXMANAGEMENT && player.getStatePlayer() == IN_GAME &&
           getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player) &&
           start.contains(box)){
            if(on) {
                start.remove(box);
                end.add(box);
            }
            else {
                getCurrentCard().setStateCard(DECISION);
                nextPlayer();
            }
        }
        throw new Exception();
    }

    @Override
    public List<Box> choicePlanet(Player player, int index) {
        //State Control
        if (getCurrentCard().getStateCard() == DECISION && player.getStatePlayer() == IN_GAME &&
            getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            return getCurrentCard().choicePlanet(this,player, index);
        }
        else throw new IllegalStateException();
    }

    @Override
    public void choiceDoubleMotor(Player player, List<Pair<DoubleMotor, BatteryStorage>> choices) {
        //State Control
        if (getCurrentCard().getStateCard() == CHOICE_ATTRIBUTES && player.getStatePlayer() == IN_GAME &&
            getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            getCurrentCard().choiceDoubleMotor(player, choices);
        }
        else throw new IllegalStateException();
    }

    @Override
    public void choiceDoubleCannon(Player player, List<Pair<DoubleCannon, BatteryStorage>> choices) {
        //State Control
        if (getCurrentCard().getStateCard() == CHOICE_ATTRIBUTES && player.getStatePlayer() == IN_GAME &&
            getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            getCurrentCard().choiceDoubleCannon(player, choices);
        }
        else throw new IllegalStateException();
    }

    @Override
    public void removeBox(Player player, SpecialStorage storage, BoxType type) {
        //State Control
        if (getCurrentCard().getStateCard() == REMOVE && player.getStatePlayer() == IN_GAME &&
            getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            getCurrentCard().removeBox(player, storage, type);
        }
        else throw new IllegalStateException();
    }

    @Override
    public void removeBattery(Player player, BatteryStorage storage) {
        //State Control
        if (getCurrentCard().getStateCard() == REMOVE && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            getCurrentCard().removeBattery(player, storage);
        }
        else throw new IllegalStateException();
    }
    //todo
    @Override
    public ArrayList<Player> getWinners() {
        return null;
    }
}
