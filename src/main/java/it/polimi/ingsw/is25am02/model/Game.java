package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.AlreadyViewingTileException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import it.polimi.ingsw.is25am02.model.HeapTiles;
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
    private final int level;
    private CardDeck deck;
    private Hourglass hourglass;
    private HeapTiles heapTile;
    private Gameboard globalBoard;
    private State currentState;
    private int ciao;

    //todo: dove sono tutte le "instanziazioni" degli attributi?
    public Game(List<Player> p, int level) {
        this.players = p;
        this.level = level;
        this.diceResult = 0;
        this.globalBoard = new Gameboard(level);
        this.heapTile = new HeapTiles();
        this.currentState = new State(p);
    }

    //getter
    public CardDeck getDeck() {
        return deck;
    }

    public Gameboard getGameboard() {
        return globalBoard;
    }

    private List<Player> getPlayers() {
        return null;
    }

    public String getName() {
        return gameName;
    }

    public int getlevel() {
        return level;
    }

    public int getMaxAllowedPlayers() {
        return maxAllowedPlayers;
    }

    public HeapTiles getHeapTile() {
        return heapTile;
    }

    public Hourglass getHourglass() {
        return hourglass;
    }

    public State getCurrentState() {
        return currentState;
    }

    public int getDiceResult() {
        return diceResult;
    }

    public void setDiceResult() {
        this.diceResult = getGameboard().getDice().pickRandomNumber();
    }

    public Set<Tile> getVisibleTiles() {
        return heapTile.getVisibleTiles();
    }

    public Player getCurrentPlayer() {
        return currentState.getCurrentPlayer();
    }

    public Card getCurrentCard() {
        return currentState.getCurrentCard();
    }

    public void nextPlayer() {
        int index = getGameboard().getRanking().indexOf(getCurrentPlayer());

        if (getGameboard().getRanking().indexOf(getCurrentPlayer()) == getGameboard().getRanking().size()-1) {//se il giocatore è l'ultimo allora il currentPlayer deve diventare il nuovo primo e lo stato della carta diventa FINISH{
            currentState.setCurrentPlayer(getGameboard().getRanking().getFirst());
            getCurrentCard().setStateCard(FINISH);
        } else if (getGameboard().getRanking().get(index + 1).getStatePlayer() == IN_GAME) {//se il prossimo giocatore è in gioco allora lo metto come prossimo giocatore corrente
            currentState.setCurrentPlayer(getGameboard().getRanking().get(index + 1));//metto il prossimo giocatore come giocatore corrente
        }
    }

    public void previousPlayer() {//todo potrebbe non servirci (era stata pensata per andare in ordine inverso di rotta)
        //come next player ma torna indietro
        int index = getGameboard().getRanking().indexOf(getCurrentPlayer());

        if (index == 0) {//se il giocatore è il primo allora il currentPlayer rimane il primo e lo stato della carta diventa FINISH{
            getCurrentCard().setStateCard(FINISH);
        } else if (getGameboard().getRanking().get(index - 1).getStatePlayer() == IN_GAME) {//se il precedente giocatore è in gioco allora lo metto come prossimo giocatore corrente
            currentState.setCurrentPlayer(getGameboard().getRanking().get(index - 1));//metto il prossimo giocatore come giocatore corrente
        }

    }

    @Override
    public Game GameCreator(List<Player> p, int level) {
        return new Game(p, level);
    }

    //todo
    @Override
    public void flipHourglass() {

    }

    @Override
    public Tile takeTile(Player player) {
        Tile temp = heapTile.drawTile();
        try {
            player.getSpaceship().setCurrentTile(temp);
        } catch (AlreadyViewingTileException e) {
            //se il giocatore sta già guardando una tile, la tile pescata viene rimessa nel mazzo (in modo ancora invisibile) e viene in realtà restituita la tile che il giocatore stava guardando
            heapTile.addTile(temp, false);
            temp = player.getSpaceship().getCurrentTile();
        }
        return temp;
    }

    @Override
    public Tile takeTile(Player player, Tile tile) {
        heapTile.removeVisibleTile(tile);
        try {
            player.getSpaceship().setCurrentTile(tile);
            return tile;
        } catch (AlreadyViewingTileException e) {
            //se l'utente sta già guardando una tile, rimetto quella che ha passato come parametro nel mucchio e ritorno quella che sta già guardando
            heapTile.addTile(tile, true);
            return player.getSpaceship().getCurrentTile();
        }
    }

    //il giocatore "scarta" la tile che stava guardando.
    @Override
    public void returnTile(Player player, Tile tile) {
        Tile temp = player.getSpaceship().getCurrentTile();
        heapTile.addTile(temp, true);
        player.getSpaceship().returnTile();
    }

    //todo
    @Override
    public void addTile(Player player, Tile tile, int x, int y) {
        player.getSpaceship().addTile(x, y, tile);
    }

    //todo
    @Override
    public void shipFinished(Player player) {
        //ci sono delle cose
        //una votla che tutti e 4 hanno finito
        this.currentState.setPhase(StateGameType.CHECK);
    }

    //todo
    @Override
    public boolean checkSpaceship(Player player) {

        //quando vanno tutte bene

        return false;
    }

    //todo
    @Override
    public void removeTile(Player player, int x, int y) {

    }

    //per inizializzazione delle cabine
    @Override
    public void addCrew(Player player, int x, int y, AliveType type) {
        if (type.equals(AliveType.HUMAN) && player.getSpaceship().getTile(x,y).isPresent()){ // faccio l'istruzione due volte perchè aggiungo due umani
            player.getSpaceship().getTile(x,y).get().addCrew(type);
            player.getSpaceship().getTile(x,y).get().addCrew(type);
        }
        else if (type.equals(AliveType.BROWN_ALIEN)){ //devo controllare se c'è una cabina brown collegata ad almeno una cabina normale
            for(Optional<Tile> tile : player.getSpaceship().getSpaceshipIterator()){
                if(tile.isPresent() && tile.get().getType().equals(TileType.BROWN_CABIN)){
                    if(player.getSpaceship().getSpaceshipIterator().getUpTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getUpTile(tile.get()).get().getType().equals(TileType.CABIN)){// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x,y).get().addCrew(type);
                        return;
                    }
                    if(player.getSpaceship().getSpaceshipIterator().getDownTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getDownTile(tile.get()).get().getType().equals(TileType.CABIN)){// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x,y).get().addCrew(type);
                        return;
                    }
                    if(player.getSpaceship().getSpaceshipIterator().getRightTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getRightTile(tile.get()).get().getType().equals(TileType.CABIN)){// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x,y).get().addCrew(type);
                        return;
                    }
                    if(player.getSpaceship().getSpaceshipIterator().getLeftTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getLeftTile(tile.get()).get().getType().equals(TileType.CABIN)){// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x,y).get().addCrew(type);
                        return;
                    }
                }

            }
        }
        else { //caso dei purple alien
            for(Optional<Tile> tile : player.getSpaceship().getSpaceshipIterator()){
                if(tile.isPresent() && tile.get().getType().equals(TileType.PURPLE_CABIN)){
                    if(player.getSpaceship().getSpaceshipIterator().getUpTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getUpTile(tile.get()).get().getType().equals(TileType.CABIN)){// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x,y).get().addCrew(type);
                        return;
                    }
                    if(player.getSpaceship().getSpaceshipIterator().getDownTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getDownTile(tile.get()).get().getType().equals(TileType.CABIN)){// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x,y).get().addCrew(type);
                        return;
                    }
                    if(player.getSpaceship().getSpaceshipIterator().getRightTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getRightTile(tile.get()).get().getType().equals(TileType.CABIN)){// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x,y).get().addCrew(type);
                        return;
                    }
                    if(player.getSpaceship().getSpaceshipIterator().getLeftTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getLeftTile(tile.get()).get().getType().equals(TileType.CABIN)){// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x,y).get().addCrew(type);
                        return;
                    }
                }
            }

        }

    }

    //todo
    @Override
    public void playNextCard(Player player) {

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
        return player.getSpaceship().getTilesByType(type);
    }

    @Override
    public void choice(Player player, boolean choice) {
        //State Control
        if (getCurrentCard().getStateCard() == DECISION && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            getCurrentCard().choice(this, player, choice);
        } else throw new IllegalStateException();
    }

    @Override
    public void removeCrew(Player player, Cabin cabin) {
        //StateControl
        if (getCurrentCard().getStateCard() == REMOVE && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player) &&
                player.getSpaceship().own(cabin)) {
            getCurrentCard().removeCrew(this, player, cabin);
        } else throw new IllegalStateException();
    }

    @Override
    public List<Box> choiceBox(Player player, boolean choice) {
        //State Control
        if (getCurrentCard().getStateCard() == DECISION && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            return getCurrentCard().choiceBox(this, player, choice);
        }
        throw new IllegalStateException();
    }

    @Override
    public void moveBox(Player player, List<Box> start, List<Box> end, Box box, boolean on) {
        //State Control
        if (getCurrentCard().getStateCard() == BOXMANAGEMENT && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player) &&
                start.contains(box)) {
            getCurrentCard().moveBox(this, player, start, end, box, on);
        }
        throw new IllegalStateException();
    }

    @Override
    public List<Box> choicePlanet(Player player, int index) {
        //State Control
        if (getCurrentCard().getStateCard() == DECISION && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            return getCurrentCard().choicePlanet(this, player, index);
        } else throw new IllegalStateException();
    }

    @Override
    public void choiceDoubleMotor(Player player, Optional<List<Pair<DoubleMotor, BatteryStorage>>> choices) {
        //State Control
        if (getCurrentCard().getStateCard() == CHOICE_ATTRIBUTES && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            getCurrentCard().choiceDoubleMotor(this, player, choices);
        } else throw new IllegalStateException();
    }

    @Override
    public void choiceDoubleCannon(Player player, Optional<List<Pair<DoubleCannon, BatteryStorage>>> choices) {
        //State Control
        if (getCurrentCard().getStateCard() == CHOICE_ATTRIBUTES && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            getCurrentCard().choiceDoubleCannon(this, player, choices);
        } else throw new IllegalStateException();
    }

    @Override
    public void choiceCrew(Player player) {
        //State Control
        if (getCurrentCard().getStateCard() == CHOICE_ATTRIBUTES && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            getCurrentCard().choiceCrew(this, player);
        } else throw new IllegalStateException();
    }

    @Override
    public void removeBox(Player player, SpecialStorage storage, BoxType type) {
        //State Control
        if (getCurrentCard().getStateCard() == REMOVE && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player) &&
                player.getSpaceship().own(storage)) {
            getCurrentCard().removeBox(this, player, storage, type);
        } else throw new IllegalStateException();
    }

    @Override
    public void removeBattery(Player player, BatteryStorage storage) {
        //State Control
        if (getCurrentCard().getStateCard() == REMOVE && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            getCurrentCard().removeBattery(this, player, storage);
        } else throw new IllegalStateException();
    }

    @Override
    public void rollDice(Player player) {
        //State Control
        if (getCurrentCard().getStateCard() == ROLL && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            setDiceResult();
            getCurrentCard().setStateCard(CHOICE_ATTRIBUTES);
        } else throw new IllegalStateException();
    }

    @Override
    public void calculateDamage(Player player, Optional<BatteryStorage> batteryStorage) {
        //State Control
        if (getCurrentCard().getStateCard() == CHOICE_ATTRIBUTES && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            getCurrentCard().calculateDamage(this, player, batteryStorage);
        } else throw new IllegalStateException();
    }

    @Override
    public void holdSpaceship(Player player, int x, int y) {
        //State Control
        if (getCurrentCard().getStateCard() == DECISION && player.getStatePlayer() == IN_GAME &&
                getCurrentState().getPhase() == EFFECT_ON_PLAYER && getCurrentPlayer().equals(player)) {
            getCurrentCard().holdSpaceship(this, player, x, y);
        } else throw new IllegalStateException();
    }

    //todo
    @Override
    public ArrayList<Player> getWinners() {
        return null;
    }
}
