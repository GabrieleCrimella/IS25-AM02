package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.BlueBox;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;

import java.util.*;

import static it.polimi.ingsw.is25am02.model.enumerations.StateCardType.*;
import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.*;
import static it.polimi.ingsw.is25am02.model.enumerations.StatePlayerType.*;

public class Game implements Game_Interface {
    private int diceResult;
    private String gameName;
    private int maxAllowedPlayers;
    private final List<Player> players;
    private final int level;
    private final CardDeck deck;
    private Hourglass hourglass;
    private final HeapTiles heapTile;
    private final Gameboard globalBoard;
    private final State currentState;
    private int alreadyFinished = 0; //tiene conto di quanti giocatori hanno già finito
    private int alreadyChecked = 0; //tiene conto dei giocatori che hanno la nave già controllata
    private int readyPlayer = 0;

    //todo: dove sono tutte le "instanziazioni" degli attributi?
    public Game(List<Player> p, int level) {
        this.players = p;
        this.level = level;
        this.diceResult = 0;
        this.globalBoard = new Gameboard(level);
        this.heapTile = new HeapTiles();
        this.currentState = new State(p);
        this.deck = new CardDeck();
        this.currentState.setPhase(BUILD);
    }

    //getter
    public CardDeck getDeck() {
        return deck;
    }

    public Gameboard getGameboard() {
        return globalBoard;
    }

    public List<Player> getPlayers() {
        return players;
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

        if (getGameboard().getRanking().indexOf(getCurrentPlayer()) == getGameboard().getRanking().size() - 1) {//se il giocatore è l'ultimo allora il currentPlayer deve diventare il nuovo primo e lo stato della carta diventa FINISH{
            currentState.setCurrentPlayer(getGameboard().getRanking().getFirst());
            getCurrentCard().setStateCard(FINISH);
        } else if (getGameboard().getRanking().get(index + 1).getStatePlayer() == IN_GAME) {//se il prossimo giocatore è in gioco allora lo metto come prossimo giocatore corrente
            currentState.setCurrentPlayer(getGameboard().getRanking().get(index + 1));//metto il prossimo giocatore come giocatore corrente
        }
    }

    @Override
    public void flipHourglass() {
        hourglass.flip();
    }

    @Override
    public Tile takeTile(Player player) {
        if(stateControl(BUILD, NOT_FINISHED, FINISH, player)){
            Tile temp = heapTile.drawTile();

            if(player.getSpaceship().getCurrentTile() == null){
                player.getSpaceship().setCurrentTile(temp);
            } else{
                //if the player is already looking at a tile, the drawn tile is put back into
                //the deck (still invisibly) and the tile the player was looking at is returned
                heapTile.addTile(temp, false);
            }
        }
        return player.getSpaceship().getCurrentTile();
    }

    @Override
    public Tile takeTile(Player player, Tile tile) {
        if(stateControl(BUILD, NOT_FINISHED, FINISH, player)){
            heapTile.removeVisibleTile(tile);

            if(player.getSpaceship().getCurrentTile() == null){
                player.getSpaceship().setCurrentTile(tile);
            } else{
                //if the player is already looking at a tile, the drawn tile is put back into
                //the deck (still visibly) and the tile the player was looking at is returned
                heapTile.addTile(tile, true);
            }
        }
        return player.getSpaceship().getCurrentTile();
    }

    //the player "discard" the tile he was looking at
    @Override
    public void returnTile(Player player) {
        if(stateControl(BUILD, NOT_FINISHED, FINISH, player)){
            heapTile.addTile(player.getSpaceship().getCurrentTile(), true);
            player.getSpaceship().returnTile();
        }
    }

    @Override
    public void addTile(Player player, int x, int y) {
        if(stateControl(BUILD, NOT_FINISHED, FINISH, player)){
            player.getSpaceship().addTile(x, y, player.getSpaceship().getCurrentTile());
        }
    }


    //player goes to the FINISH phase, if he's the last one I change the  game state to CHECK
    //I add the players to the gameboard
    @Override
    public void shipFinished(Player player) {
        if(stateControl(BUILD, NOT_FINISHED, FINISH, player)){
            int[] position = getGameboard().getStartingPosition();
            player.setStatePlayer(FINISHED);
            alreadyFinished++;
            getGameboard().positions.put(player, position[players.size() - alreadyFinished - 1]);

            if (alreadyFinished == players.size()) {
                this.currentState.setPhase(StateGameType.CHECK);
            }
        }
    }

    @Override
    public void checkSpaceship(Player player) {
        if(stateControl(CHECK, FINISHED, FINISH, player)) {
            if (player.getSpaceship().checkSpaceship()) {
                player.setStatePlayer(CORRECT_SHIP);
            } else {
                player.setStatePlayer(WRONG_SHIP);
            }
            alreadyChecked++;

            if (alreadyChecked == players.size()) {
                for (Player p : players) {
                    if (p.getStatePlayer().equals(WRONG_SHIP)) {
                        this.currentState.setPhase(StateGameType.CORRECTION);
                        alreadyChecked--;
                    }
                }
                this.currentState.setPhase(StateGameType.INITIALIZATION_SPACESHIP);
            }
        }
    }

    @Override
    public void removeTile(Player player, int x, int y) {
        if(stateControl(CORRECTION, WRONG_SHIP, FINISH, player)){
            player.getSpaceship().removeTile(x, y);
        }
    }

    @Override
    public void checkWrongSpaceship(Player player) {
        if(stateControl(CORRECTION, WRONG_SHIP, FINISH, player)) {
            if (player.getSpaceship().checkSpaceship()) {
                player.setStatePlayer(CORRECT_SHIP);
                alreadyChecked++;
            }

            if (alreadyChecked == players.size()) {
                this.currentState.setPhase(StateGameType.INITIALIZATION_SPACESHIP);
            }
        }
    }

    //cabin initialization
    @Override
    public void addCrew(Player player, int x, int y, AliveType type) {
        if(stateControl(INITIALIZATION_SPACESHIP, CORRECT_SHIP, FINISH, player) && initializationCabinControl(player, x, y)){
            if (type.equals(AliveType.HUMAN)) { //se type è human aggiungo due umani
                player.getSpaceship().getTile(x, y).get().addCrew(type);
                player.getSpaceship().getTile(x, y).get().addCrew(type);
            }
            else if(type.equals(AliveType.BROWN_ALIEN)) { // se type è brown_alien controllo che ci sia il supportovitale vicino e nel caso aggiungo l'alieno
                if (checkTileNear(player, x, y, TileType.BROWN_CABIN)) {
                    player.getSpaceship().getTile(x, y).get().addCrew(type);
                }
            }
            else if(type.equals(AliveType.PURPLE_ALIEN)){ // se type è pruple_alien controllo che ci sia il supportovitale vicino e nel caso aggiungo l'alieno
                if (checkTileNear(player, x, y,TileType.PURPLE_CABIN)) {
                    player.getSpaceship().getTile(x, y).get().addCrew(type);
                }
            }
        }
    }

    //the player has finished the initialization phase and is ready to play
    @Override
    public void ready(Player player) {
        if(stateControl(INITIALIZATION_GAME, CORRECT_SHIP, FINISH, player)){
            player.setStatePlayer(IN_GAME);
            readyPlayer++;

            //in case the player doesn't want to initialize all the cabins by himself
            List<Tile> cabins = player.getSpaceship().getTilesByType(TileType.CABIN);
            for (Tile c : cabins) {
                if(c.getCrew().isEmpty()){
                    c.addCrew(AliveType.HUMAN);
                    c.addCrew(AliveType.HUMAN);
                }
            }
            //when all players are ready the game starts
            if(readyPlayer == players.size()){
                getCurrentState().setPhase(TAKE_CARD);
            }
        }
    }

    @Override
    public void playNextCard(Player player) {
        if(stateControl(TAKE_CARD, IN_GAME, FINISH, player) && currentPlayerControl(player)){
            getDeck().playnextCard();
        }
    }

    @Override
    public HashMap<Player, Integer> getPosition() {
        return getGameboard().getPositions();
    }

    @Override
    public List<Tile> possibleChoice(Player player, TileType type) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player)){
            return player.getSpaceship().getTilesByType(type);
        }
        return null;
    }

    @Override
    public void choice(Player player, boolean choice) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, player) && currentPlayerControl(player)){
            getCurrentCard().choice(this, player, choice);
        }
    }

    @Override
    public void removeCrew(Player player, Cabin cabin) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, REMOVE, player) && currentPlayerControl(player) && player.getSpaceship().own(cabin)){
            getCurrentCard().removeCrew(this, player, cabin);
        }
    }

    @Override
    public List<Box> choiceBox(Player player, boolean choice) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, player) && currentPlayerControl(player)){
            return getCurrentCard().choiceBox(this, player, choice);
        }
        else return null;
    }

    @Override
    public void moveBox(Player player, List<Box> start, List<Box> end, Box box, boolean on) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, BOXMANAGEMENT, player) && currentPlayerControl(player) && start.contains(box)){
            getCurrentCard().moveBox(this, player, start, end, box, on);
        }
    }

    @Override
    public List<Box> choicePlanet(Player player, int index) {
        if(stateControl(EFFECT_ON_PLAYER,IN_GAME, DECISION, player) && currentPlayerControl(player)){
            return getCurrentCard().choicePlanet(this, player, index);
        }
        else return null;
    }

    @Override
    public void choiceDoubleMotor(Player player, Optional<List<Pair<Tile, Tile>>> choices) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player) && currentPlayerControl(player)){
            getCurrentCard().choiceDoubleMotor(this, player, choices);
        }
    }

    @Override
    public void choiceDoubleCannon(Player player, Optional<List<Pair<DoubleCannon, BatteryStorage>>> choices) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player) && currentPlayerControl(player)){
            getCurrentCard().choiceDoubleCannon(this, player, choices);
        }
    }

    @Override
    public void choiceCrew(Player player) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player) && currentPlayerControl(player)){
            getCurrentCard().choiceCrew(this, player);
        }
    }

    @Override
    public void removeBox(Player player, SpecialStorage storage, BoxType type) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, REMOVE, player) && currentPlayerControl(player) && player.getSpaceship().own(storage)){
            getCurrentCard().removeBox(this, player, storage, type);
        }
    }

    @Override
    public void removeBattery(Player player, BatteryStorage storage) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, REMOVE, player) && currentPlayerControl(player) && player.getSpaceship().own(storage)){
            getCurrentCard().removeBattery(this, player, storage);
        }
    }

    @Override
    public void rollDice(Player player) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, ROLL, player) && currentPlayerControl(player)){
            setDiceResult();
            getCurrentCard().setStateCard(CHOICE_ATTRIBUTES);
        }
    }

    @Override
    public void calculateDamage(Player player, Optional<BatteryStorage> batteryStorage) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player) && currentPlayerControl(player)){
            getCurrentCard().calculateDamage(this, player, batteryStorage);
        }
    }

    @Override
    public void holdSpaceship(Player player, int x, int y) {
        if(stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, player) && currentPlayerControl(player)){
            getCurrentCard().holdSpaceship(this, player, x, y);
        }
    }

    @Override
    public ArrayList<Player> getWinners() {
        int minExposedConnectors = Integer.MAX_VALUE;
        if(getCurrentState().getPhase() == RESULT && getPlayers() != null){
            ArrayList<Player> winners = new ArrayList<>();

            for (Player p : getPlayers()) {
                int exposedConnectors;


                int valueBox = 0;
                //aggiungo crediti in base alla posizione che ho raggiunto
                p.getSpaceship().addCosmicCredits(getGameboard().getRewardPosition()[getGameboard().getRanking().indexOf(p)]);
                //va trovato giocatore con meno connettori esposti
                exposedConnectors = p.getSpaceship().calculateExposedConnectors();
                if(exposedConnectors < minExposedConnectors){
                    minExposedConnectors = exposedConnectors;
                }
                for(Tile s_storage : p.getSpaceship().getTilesByType(TileType.SPECIAL_STORAGE)){
                    for(Box box : s_storage.getOccupation()){
                        valueBox += box.getValue();
                    }
                }
                for(Tile storage : p.getSpaceship().getTilesByType(TileType.STORAGE)){
                    for(Box box : storage.getOccupation()){
                        valueBox += box.getValue();
                    }
                }
                p.getSpaceship().addCosmicCredits(valueBox); //aggiungo crediti dovuti alla vendita delle merci
                p.getSpaceship().removeCosmicCredits(p.getSpaceship().getNumOfWastedTiles()); //tolgo crediti quanti sono gli scarti
            }
            for (Player p : getPlayers()) {
                if (p.getSpaceship().calculateExposedConnectors() == minExposedConnectors) {
                    p.getSpaceship().addCosmicCredits(getGameboard().getBestShip());
                }
            }

            for(Player p : getPlayers()){
                if (p.getSpaceship().getCosmicCredits() > 0) {
                    winners.add(p);
                }
            }

            return winners;
        }
        return null;
    }

    private boolean stateControl(StateGameType stateGame, StatePlayerType statePlayer, StateCardType stateCard, Player player) {
        if (!player.getStatePlayer().equals(statePlayer)){
            System.out.println("Wrong player state, expected state : " + statePlayer + ", actual state : " + player.getStatePlayer());
            return false;
        }
        if (!getCurrentCard().getStateCard().equals(stateCard)) {
            System.out.println("Wrong card state, expected state : " + stateCard + ", actual state : " + getCurrentCard().getStateCard());
            return false;
        }
        if (!getCurrentState().getPhase().equals(stateGame)) {
            System.out.println("Wrong game state, expected state : " + stateGame + "  actual state : " + getCurrentState().getPhase());
            return false;
        }
        return true;
    }

    private boolean currentPlayerControl(Player player){
        if (!player.equals(getCurrentPlayer())) {
            System.out.println("Wrong leader, actual leader : " + getCurrentState().getCurrentPlayer());
            return false;
        }
        return true;
    }

    private boolean initializationCabinControl(Player player, int x, int y){
        if(player.getSpaceship().getTile(x,y).isEmpty()){
            System.out.println("No Tile in position ( " + x + ", " + y + " )");
            return false;
        }
        if(!player.getSpaceship().getTile(x,y).get().getType().equals(TileType.CABIN)){
            System.out.println("Different TileType, expected " +TileType.CABIN +", actual " + player.getSpaceship().getTile(x,y).get().getType());
            return false;
        }
        if(!player.getSpaceship().getTile(x,y).get().getCrew().isEmpty()){
            System.out.println("Cabin already full");
            return false;
        }
        return true;
    }

    private boolean checkTileNear(Player player, int x, int y, TileType type) {
        Tile tile = player.getSpaceship().getTile(x, y).get();
        if (player.getSpaceship().getSpaceshipIterator().getUpTile(tile).isPresent() &&
                player.getSpaceship().getSpaceshipIterator().getUpTile(tile).get().getType().equals(type)) {
            return true;
        } else if (player.getSpaceship().getSpaceshipIterator().getRightTile(tile).isPresent() &&
                player.getSpaceship().getSpaceshipIterator().getRightTile(tile).get().getType().equals(type)) {
            return true;
        } else if (player.getSpaceship().getSpaceshipIterator().getLeftTile(tile).isPresent() &&
                player.getSpaceship().getSpaceshipIterator().getLeftTile(tile).get().getType().equals(type)) {
            return true;
        } else if (player.getSpaceship().getSpaceshipIterator().getDownTile(tile).isPresent() &&
                player.getSpaceship().getSpaceshipIterator().getDownTile(tile).get().getType().equals(type)) {
            return true;
        } else {
            return false;
        }
    }
}
