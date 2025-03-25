package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalStateException;
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
        try {
            //Check
            stateControl(BUILD, NOT_FINISHED, FINISH, player);
            currentTileControl(player);

            player.getSpaceship().setCurrentTile(heapTile.drawTile());

        } catch (IllegalStateException | AlreadyViewingException e) {
            System.out.println(e.getMessage());
        }
        return player.getSpaceship().getCurrentTile();
    }

    @Override
    public Tile takeTile(Player player, Tile tile) {
        try {
            //Check
            stateControl(BUILD, NOT_FINISHED, FINISH, player);
            currentTileControl(player);

            heapTile.removeVisibleTile(tile);
            player.getSpaceship().setCurrentTile(tile);

        } catch (IllegalStateException | AlreadyViewingException | IllegalRemoveException e) {
            System.out.println(e.getMessage());
        }
        return player.getSpaceship().getCurrentTile();
    }

    @Override
    public void returnTile(Player player) {
        try {
            //Check
            stateControl(BUILD, NOT_FINISHED, FINISH, player);

            heapTile.addTile(player.getSpaceship().getCurrentTile(), true);
            player.getSpaceship().returnTile();

        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addTile(Player player, int x, int y) {
        try {
            //Check
            stateControl(BUILD, NOT_FINISHED, FINISH, player);

            player.getSpaceship().addTile(x, y, player.getSpaceship().getCurrentTile());

        } catch (IllegalStateException | IllegalAddException e) {
            System.out.println(e.getMessage());
        }
    }


    //player goes to the FINISH phase, if he's the last one I change the  game state to CHECK
    //I add the players to the gameboard
    @Override
    public void shipFinished(Player player) {
        try {
            //Check
            stateControl(BUILD, NOT_FINISHED, FINISH, player);

            player.setStatePlayer(FINISHED);
            alreadyFinished++;
            getGameboard().positions.put(player, getGameboard().getStartingPosition()[players.size() - alreadyFinished - 1]);

            if (alreadyFinished == players.size()) {
                this.currentState.setPhase(StateGameType.CHECK);
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void checkSpaceship(Player player) {
        try {
            stateControl(CHECK, FINISHED, FINISH, player);

            if (player.getSpaceship().checkSpaceship()) {
                player.setStatePlayer(CORRECT_SHIP);
            } else {
                player.setStatePlayer(WRONG_SHIP);
            }
            alreadyChecked++;

            if (alreadyChecked == players.size()) {
                this.currentState.setPhase(StateGameType.INITIALIZATION_SPACESHIP);
                for (Player p : players) {
                    if (p.getStatePlayer().equals(WRONG_SHIP)) {
                        this.currentState.setPhase(StateGameType.CORRECTION);
                        alreadyChecked--;
                    }
                }
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<List<boolean[][]>> removeTile(Player player, int x, int y) {
        try {
            //stateControl(CORRECTION, WRONG_SHIP, FINISH, player);
            stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player);


            return player.getSpaceship().removeTile(x, y);
        } catch (IllegalStateException | IllegalRemoveException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void keepBlock(Player player, boolean[][] tilesToKeep) {
        try {
            //stateControl(CORRECTION, WRONG_SHIP, FINISH, player);
            stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player);

            player.getSpaceship().keepBlock(tilesToKeep);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void checkWrongSpaceship(Player player) {
        try {
            stateControl(CORRECTION, WRONG_SHIP, FINISH, player);

            if (player.getSpaceship().checkSpaceship()) {
                player.setStatePlayer(CORRECT_SHIP);
                alreadyChecked++;
            }

            if (alreadyChecked == players.size()) {
                this.currentState.setPhase(StateGameType.INITIALIZATION_SPACESHIP);
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addCrew(Player player, int x, int y, AliveType type) {
        try {
            stateControl(INITIALIZATION_SPACESHIP, CORRECT_SHIP, FINISH, player);
            initializationCabinControl(player, x, y);

            if (type.equals(AliveType.HUMAN)) { //se type è human aggiungo due umani
                player.getSpaceship().getTile(x, y).get().addCrew(type);
                player.getSpaceship().getTile(x, y).get().addCrew(type);
            } else if (type.equals(AliveType.BROWN_ALIEN)) { // se type è brown_alien controllo che ci sia il supportovitale vicino e nel caso aggiungo l'alieno
                if (checkTileNear(player, x, y, TileType.BROWN_CABIN)) {
                    player.getSpaceship().getTile(x, y).get().addCrew(type);
                }
            } else if (type.equals(AliveType.PURPLE_ALIEN)) { // se type è purple_alien controllo che ci sia il supportovitale vicino e nel caso aggiungo l'alieno
                if (checkTileNear(player, x, y, TileType.PURPLE_CABIN)) {
                    player.getSpaceship().getTile(x, y).get().addCrew(type);
                }
            }
        } catch (IllegalStateException | TileException | IllegalAddException e) {
            System.out.println(e.getMessage());
        }
    }

    //the player has finished the initialization phase and is ready to play
    @Override
    public void ready(Player player) {
        try {
            stateControl(INITIALIZATION_GAME, CORRECT_SHIP, FINISH, player);

            player.setStatePlayer(IN_GAME);
            readyPlayer++;

            //in case the player doesn't want to initialize all the cabins by himself
            List<Tile> cabins = player.getSpaceship().getTilesByType(TileType.CABIN);
            for (Tile c : cabins) {
                if (c.getCrew().isEmpty()) {
                    c.addCrew(AliveType.HUMAN);
                    c.addCrew(AliveType.HUMAN);
                }
            }
            //when all players are ready the game starts
            if (readyPlayer == players.size()) {
                getCurrentState().setPhase(TAKE_CARD);
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void earlyLanding(Player player) {
        try{
            stateControl(TAKE_CARD, IN_GAME, FINISH, player);

            getGameboard().getPositions().remove(player);
            player.setStatePlayer(OUT_GAME);

            getCurrentState().setCurrentPlayer(getGameboard().getRanking().getFirst());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void playNextCard(Player player) {
        try {
            stateControl(TAKE_CARD, IN_GAME, FINISH, player);
            outOfGame();
            currentPlayerControl(player);

            if(getDeck().playnextCard(getCurrentState()) == null) {
                this.getCurrentState().setPhase(RESULT);
            } else {
                this.getCurrentState().setPhase(EFFECT_ON_PLAYER);
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public HashMap<Player, Integer> getPosition() {
        return getGameboard().getPositions();
    }

    @Override
    public List<Tile> possibleChoice(Player player, TileType type) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player);
            return player.getSpaceship().getTilesByType(type);

        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void choice(Player player, boolean choice) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, player);
            currentPlayerControl(player);

            getCurrentCard().choice(this, player, choice);
        } catch (IllegalStateException | UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeCrew(Player player, Cabin cabin) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, REMOVE, player);
            currentPlayerControl(player);
            possessionControl(player, cabin);

            getCurrentCard().removeCrew(this, player, cabin);
        } catch (IllegalStateException | TileException | UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Box> choiceBox(Player player, boolean choice) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, player);
            currentPlayerControl(player);

            return getCurrentCard().choiceBox(this, player, choice);
        } catch (IllegalStateException | UnsupportedOperationException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //todo Scaturisce un GRAVE ERRORE, RISOLVERE ASAP (chiedere a Davide)
    @Override
    public void moveBox(Player player, List<Box> start, List<Box> end, Box box, boolean on) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, BOXMANAGEMENT, player);
            currentPlayerControl(player);

            // start.contains(box); va messo o lo controllo nelle add, remove di movebox?

            getCurrentCard().moveBox(this, player, start, end, box, on);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Box> choicePlanet(Player player, int index) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, player);
            currentPlayerControl(player);

            return getCurrentCard().choicePlanet(this, player, index);
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    //todo sono arrivato fino a qui per il controllo delle eccezioni
    @Override
    public void choiceDoubleMotor(Player player, Optional<List<Pair<Tile, Tile>>> choices) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player);
            currentPlayerControl(player);

            getCurrentCard().choiceDoubleMotor(this, player, choices);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void choiceDoubleCannon(Player player, Optional<List<Pair<DoubleCannon, BatteryStorage>>> choices) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player);
            currentPlayerControl(player);

            getCurrentCard().choiceDoubleCannon(this, player, choices);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void choiceCrew(Player player) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player);
            currentPlayerControl(player);

            getCurrentCard().choiceCrew(this, player);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeBox(Player player, SpecialStorage storage, BoxType type) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, REMOVE, player);
            currentPlayerControl(player);
            possessionControl(player, storage);

            getCurrentCard().removeBox(this, player, storage, type);
        } catch (IllegalStateException | TileException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeBattery(Player player, BatteryStorage storage) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, REMOVE, player);
            currentPlayerControl(player);
            possessionControl(player, storage);

            getCurrentCard().removeBattery(this, player, storage);
        } catch (IllegalStateException | TileException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void rollDice(Player player) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, ROLL, player);
            currentPlayerControl(player);

            setDiceResult();
            getCurrentCard().setStateCard(CHOICE_ATTRIBUTES);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void calculateDamage(Player player, Optional<BatteryStorage> batteryStorage) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player);
            currentPlayerControl(player);

            getCurrentCard().calculateDamage(this, player, batteryStorage);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void holdSpaceship(Player player, int x, int y) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, player);
            currentPlayerControl(player);

            getCurrentCard().holdSpaceship(this, player, x, y);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Player> getWinners() {
        try{
            if(getCurrentState().getPhase() != RESULT){
                throw new IllegalStateException("Wrong State");
            }

            int minExposedConnectors = Integer.MAX_VALUE;
            ArrayList<Player> winners = new ArrayList<>();

            for (Player p : getPlayers()) {
                //Boxes Values
                int valueBox = 0;
                for (Tile s_storage : p.getSpaceship().getTilesByType(TileType.SPECIAL_STORAGE)) {
                    for (Box box : s_storage.getOccupation()) {
                        valueBox += box.getValue();
                    }
                }
                for (Tile storage : p.getSpaceship().getTilesByType(TileType.STORAGE)) {
                    for (Box box : storage.getOccupation()) {
                        valueBox += box.getValue();
                    }
                }

                if(p.getStatePlayer() == IN_GAME){
                    int exposedConnectors;
                    //Ranking points
                    p.getSpaceship().addCosmicCredits(getGameboard().getRewardPosition()[getGameboard().getRanking().indexOf(p)]);

                    exposedConnectors = p.getSpaceship().calculateExposedConnectors();
                    if (exposedConnectors < minExposedConnectors) {
                        minExposedConnectors = exposedConnectors;
                    }
                    //Sale of boxes
                    p.getSpaceship().addCosmicCredits(valueBox);
                }
                else{
                    //Sale of boxes
                    if(valueBox%2 == 0){
                        p.getSpaceship().addCosmicCredits(valueBox/2);
                    } else {
                        p.getSpaceship().addCosmicCredits((valueBox/2) + 1);
                    }
                }
                //Payment of damages
                p.getSpaceship().removeCosmicCredits(p.getSpaceship().getNumOfWastedTiles());
            }

            //Best Spaceship
            for (Player p : getPlayers()) {
                if (p.getStatePlayer() == IN_GAME && p.getSpaceship().calculateExposedConnectors() == minExposedConnectors) {
                    p.getSpaceship().addCosmicCredits(getGameboard().getBestShip());
                }
            }

            for (Player p : getPlayers()) {
                if (p.getSpaceship().getCosmicCredits() > 0) {
                    winners.add(p);
                }
            }

            winners.sort((p1, p2) -> Integer.compare(p1.getSpaceship().getCosmicCredits(), p2.getSpaceship().getCosmicCredits()));
            return winners;

        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void stateControl(StateGameType stateGame, StatePlayerType statePlayer, StateCardType stateCard, Player player) throws IllegalStateException {
        if (!player.getStatePlayer().equals(statePlayer)) {
            throw new IllegalStateException("Wrong player state, expected state : " + statePlayer + ", actual state : " + player.getStatePlayer());
        }
        if (!getCurrentCard().getStateCard().equals(stateCard)) {
            throw new IllegalStateException("Wrong card state, expected state : " + stateCard + ", actual state : " + getCurrentCard().getStateCard());
        }
        if (!getCurrentState().getPhase().equals(stateGame)) {
            throw new IllegalStateException("Wrong game state, expected state : " + stateGame + "  actual state : " + getCurrentState().getPhase());
        }
    }

    private void currentPlayerControl(Player player) throws IllegalStateException {
        if (!player.equals(getCurrentPlayer())) {
            throw new IllegalStateException("Wrong leader, actual leader : " + getCurrentState().getCurrentPlayer());
        }
    }

    private void currentTileControl(Player player) throws AlreadyViewingException {
        if (player.getSpaceship().getCurrentTile() != null) {
            throw new AlreadyViewingException("Wrong (CurrentTile != null), Player : " + player.getNickname() + ", actual tile : " + player.getSpaceship().getCurrentTile());
        }
    }

    private void initializationCabinControl(Player player, int x, int y) throws TileException {
        if (player.getSpaceship().getTile(x, y).isEmpty()) {
            throw new TileException("No Tile in position ( " + x + ", " + y + " )");
        }
        if (!player.getSpaceship().getTile(x, y).get().getType().equals(TileType.CABIN)) {
            throw new TileException("Different TileType, expected " + TileType.CABIN + ", actual " + player.getSpaceship().getTile(x, y).get().getType());
        }
        if (!player.getSpaceship().getTile(x, y).get().getCrew().isEmpty()) {
            throw new TileException("Cabin already full");
        }
    }

    private void possessionControl(Player player, Tile tile) throws TileException {
        if (!player.getSpaceship().own(tile)) {
            throw new TileException("Player: " + player.getNickname() + ", doesn't possess Tile: " + tile);
        }
    }

    private boolean checkTileNear(Player player, int x, int y, TileType type) throws IllegalAddException {
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
            throw new IllegalAddException("There is no AlienCabin near (" + x + "," + y + ")");
        }
    }

    private void outOfGame(){
        HashMap<Player,Integer> positions = getGameboard().getPositions();
        for (Player p : getPlayers()) {
            //0 Human on my spaceship
            if(p.getSpaceship().calculateNumHuman() == 0){
                getGameboard().getPositions().remove(p);
                p.setStatePlayer(OUT_GAME);
            }

            //0 motorPower in OpenSpace
            if(getCurrentCard().getCardType() == CardType.OPENSPACE){
                if(getCurrentCard().getFly().get(p) == 0){
                    getGameboard().getPositions().remove(p);
                    p.setStatePlayer(OUT_GAME);
                }
            }

            //lapped
            if(positions.get(getCurrentPlayer()) - positions.get(p) > getGameboard().getNumStep()){
                getGameboard().getPositions().remove(p);
                p.setStatePlayer(OUT_GAME);
            }
        }
        getCurrentState().setCurrentPlayer(getGameboard().getRanking().getFirst());
    }
}
