package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalStateException;
import it.polimi.ingsw.is25am02.model.tiles.*;

import java.util.*;

import static it.polimi.ingsw.is25am02.model.enumerations.StateCardType.*;
import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.*;
import static it.polimi.ingsw.is25am02.model.enumerations.StatePlayerType.*;

public class Game implements Game_Interface {
    private int diceResult;
    private boolean buildTimeIsOver;
    private String gameName;
    private int maxAllowedPlayers;
    private final List<Player> players;
    private final int level;
    private final CardDeck deck;
    private final Hourglass hourglass;
    private final HeapTiles heapTile;
    private final Gameboard globalBoard;
    private final State currentState;
    private int alreadyFinished = 0; //tiene conto di quanti giocatori hanno già finito
    private int alreadyChecked = 0;  //tiene conto dei giocatori che hanno la nave già controllata
    private int readyPlayer = 0;
    private List<Player> winners = new ArrayList<>();

    public Game(List<Player> p, int level) {
        this.players = p;
        this.level = level;
        this.diceResult = 0;
        this.buildTimeIsOver = false;
        this.globalBoard = new Gameboard(level);
        this.heapTile = new HeapTiles();
        this.currentState = new State(p);
        this.deck = new CardDeck(level);
        this.hourglass = new Hourglass();
    }

    //for testing
    public void tilesSituation(){
        System.out.println("Visible Tiles:");
        Set<Tile> visibleTiles = new HashSet<>();
        visibleTiles = getVisibleTiles();
        for (Tile tile:visibleTiles){
            if (tile==null){
                System.out.print("|    |");
            }
            else if (tile.getType().equals(TileType.BATTERY)){
                System.out.print("| B  |");
            }
            else if (tile.getType().equals(TileType.BROWN_CABIN)){
                System.out.print("| BC |");
            }
            else if (tile.getType().equals(TileType.CABIN)){
                System.out.print("| CB |");
            }
            else if (tile.getType().equals(TileType.CANNON)){
                System.out.print("| CN |");
            }
            else if (tile.getType().equals(TileType.D_CANNON)){
                System.out.print("|DCN |");
            }
            else if (tile.getType().equals(TileType.D_MOTOR)){
                System.out.print("| DM |");
            }
            else if (tile.getType().equals(TileType.MOTOR)){
                System.out.print("| M  |");
            }
            else if (tile.getType().equals(TileType.PURPLE_CABIN)){
                System.out.print("| PC |");
            }
            else if (tile.getType().equals(TileType.SHIELD)){
                System.out.print("| SH |");
            }
            else if (tile.getType().equals(TileType.SPECIAL_STORAGE)){
                System.out.print("| SS |");
            }
            else if (tile.getType().equals(TileType.STORAGE)){
                System.out.print("| S  |");
            }
            else if (tile.getType().equals(TileType.STRUCTURAL)){
                System.out.print("| ST |");
            }
            else{
                System.out.print("| Z |");
            }
        }
        System.out.println();
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
        this.diceResult = globalBoard.getDice().pickRandomNumber();
    }

    public void setDiceResultForTesting(int  diceResult) {
        this.diceResult = diceResult;

    }

    public void setBuildTimeIsOver() {
        buildTimeIsOver = true;
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

    public List<Player> getWinners() { return winners; }

    public void nextPlayer() {
        int index = getGameboard().getRanking().indexOf(getCurrentPlayer());

        if (globalBoard.getRanking().indexOf(getCurrentPlayer()) == globalBoard.getRanking().size() - 1) {//se il giocatore è l'ultimo allora il currentPlayer deve diventare il nuovo primo e lo stato della carta diventa FINISH{
            currentState.setCurrentPlayer(getGameboard().getRanking().getFirst());
            getCurrentCard().setStateCard(FINISH);
        } else if (globalBoard.getRanking().get(index + 1).getStatePlayer() == IN_GAME) {//se il prossimo giocatore è in gioco allora lo metto come prossimo giocatore corrente
            currentState.setCurrentPlayer(getGameboard().getRanking().get(index + 1));//metto il prossimo giocatore come giocatore corrente
        }
    }

    @Override
    public void flipHourglass(Player player) {
        try {
            levelControl();
            buildControl();
            if (!hourglass.getRunning()) {
                if (globalBoard.getHourGlassFlip() > 1) {
                    stateControl(BUILD, NOT_FINISHED, FINISH, player);
                    hourglass.flip(this);
                    globalBoard.decreaseHourGlassFlip();

                } else if (globalBoard.getHourGlassFlip() == 1) {
                    stateControl(BUILD, FINISHED, FINISH, player);
                    hourglass.flip(this);
                    globalBoard.decreaseHourGlassFlip();
                }
            } else {
                throw new IllegalStateException("Hourglass already running");
            }
        } catch (IllegalStateException | IllegalPhaseException | LevelException e) {
            System.out.println(e.getMessage());
        }
    }

    public void takeMiniDeck(Player player, int index) {
        try {
            levelControl();
            buildControl();
            stateControl(BUILD, NOT_FINISHED, FINISH, player);
            currentTileControl(player);
            deckAllowedControl(player);

            player.setNumDeck(index);
            deck.giveDeck(index);

        } catch (LevelException | IllegalStateException | IllegalPhaseException | AlreadyViewingException e) {
            System.out.println(e.getMessage());
        }
    }

    public void returnMiniDeck(Player player) {
        try {
            levelControl();
            buildControl();
            stateControl(BUILD, NOT_FINISHED, FINISH, player);
            currentTileControl(player);
            deckAllowedControl(player);

            deck.returnDeck(player.getNumDeck());
            player.setNumDeck(-1);

        } catch (LevelException | AlreadyViewingException | IllegalStateException | IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void takeTile(Player player) {
        try {
            buildControl();
            stateControl(BUILD, NOT_FINISHED, FINISH, player);
            currentTileControl(player);

            player.getSpaceship().setCurrentTile(heapTile.drawTile());

        } catch (IllegalStateException | AlreadyViewingException | IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void takeTile(Player player, Tile tile) {
        try {
            buildControl();
            stateControl(BUILD, NOT_FINISHED, FINISH, player);
            currentTileControl(player);

            heapTile.removeVisibleTile(tile);
            player.getSpaceship().setCurrentTile(tile);

        } catch (IllegalStateException | AlreadyViewingException | IllegalRemoveException | IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void returnTile(Player player) {
        try {
            buildControl();
            stateControl(BUILD, NOT_FINISHED, FINISH, player);

            heapTile.addTile(player.getSpaceship().getCurrentTile(), true);
            player.getSpaceship().returnTile();

        } catch (IllegalStateException | IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addTile(Player player, Coordinate pos,  RotationType rotation) {
        try {
            buildControl();
            stateControl(BUILD, NOT_FINISHED, FINISH, player);
            Tile currentTile = player.getSpaceship().getCurrentTile();
            currentTile.setRotationType(rotation);

            player.getSpaceship().addTile(pos.x(), pos.y(), currentTile);

            //player can see the minidecks
            if (!player.getDeckAllowed()) {
                player.setDeckAllowed();
            }

        } catch (IllegalStateException | IllegalAddException | IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void bookTile(Player player) {
        try {
            levelControl();
            buildControl();
            stateControl(BUILD, NOT_FINISHED, FINISH, player);

            player.getSpaceship().bookTile(player);
        } catch (IllegalStateException | IllegalAddException | IllegalPhaseException | LevelException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addBookedTile(Player player, int index, Coordinate pos, RotationType  rotation) {
        try {
            levelControl();
            buildControl();
            stateControl(BUILD, NOT_FINISHED, FINISH, player);

            player.getSpaceship().addBookedTile(index, pos.x(), pos.y(), rotation);
        } catch (IllegalStateException | IllegalAddException | IllegalPhaseException | LevelException e) {
            System.out.println(e.getMessage());
        }
    }

    //player goes to the FINISH phase, if he's the last one I change the  game state to CHECK
    //I add the players to the gameboard
    @Override
    public void shipFinished(Player player) {
        try {
            stateControl(BUILD, NOT_FINISHED, FINISH, player);

            player.setStatePlayer(FINISHED);
            alreadyFinished++;
            getGameboard().getPositions().put(player, getGameboard().getStartingPosition()[players.size() - alreadyFinished]);
            if (player.getSpaceship().getBookedTiles().values().stream().anyMatch(Objects::nonNull)) {
                player.getSpaceship().addNumOfWastedTiles((int) player.getSpaceship().getBookedTiles().values().stream().filter(Objects::nonNull).count());
            }

            if (alreadyFinished == players.size()) {
                this.currentState.setPhase(StateGameType.CHECK);
                if(level !=0){
                    deck.createFinalDeck(); //mischio i mazzetti e creo il deck finale
                }
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
    public void removeTile(Player player, Coordinate pos) {
        try {
            stateControl(CORRECTION, WRONG_SHIP, FINISH, player);

            player.getSpaceship().removeTile(pos.x(), pos.y());
        } catch (IllegalStateException | IllegalRemoveException e) {
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
    public void addCrew(Player player, Coordinate pos, AliveType type) {
        try {
            levelControl();
            stateControl(INITIALIZATION_SPACESHIP, CORRECT_SHIP, FINISH, player);
            cabinControl(player, pos);

            if (type.equals(AliveType.HUMAN)) { //se type è human aggiungo due umani
                giveTile(player, pos).addCrew(type);
                giveTile(player, pos).addCrew(type);
            } else if (type.equals(AliveType.BROWN_ALIEN)) { // se type è brown_alien controllo che ci sia il supportovitale vicino e nel caso aggiungo l'alieno
                if (checkTileNear(player, pos, TileType.BROWN_CABIN)) {
                    giveTile(player, pos).addCrew(type);
                }
            } else if (type.equals(AliveType.PURPLE_ALIEN)) { // se type è purple_alien controllo che ci sia il supportovitale vicino e nel caso aggiungo l'alieno
                if (checkTileNear(player, pos, TileType.PURPLE_CABIN)) {
                    giveTile(player, pos).addCrew(type);
                }
            }
        } catch (IllegalStateException | TileException | IllegalAddException | LevelException e) {
            System.out.println(e.getMessage());
        }
    }

    //the player has finished the initialization phase and is ready to play
    @Override
    public void ready(Player player) {
        try {
            stateControl(INITIALIZATION_SPACESHIP, CORRECT_SHIP, FINISH, player);

            player.setStatePlayer(IN_GAME);
            readyPlayer++;

            //1) in case the player doesn't want to initialize all the cabins by himself
            //2) it's the tutorial level
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
        try {
            levelControl();
            stateControl(TAKE_CARD, IN_GAME, FINISH, player);

            getGameboard().getPositions().remove(player);
            player.setStatePlayer(OUT_GAME);

            getCurrentState().setCurrentPlayer(getGameboard().getRanking().getFirst());
        } catch (IllegalStateException | LevelException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void playNextCard(Player player) {
        try {
            stateControl(TAKE_CARD, IN_GAME, FINISH, player);
            outOfGame();
            currentPlayerControl(player);

            if (deck.playnextCard(this) == null || globalBoard.getPositions().isEmpty()) {
                this.getCurrentState().setPhase(RESULT);
            } else {
                this.getCurrentState().setPhase(EFFECT_ON_PLAYER);
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void choice(Player player, boolean choice) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, player);
            currentPlayerControl(player);

            getCurrentCard().choice(this, player, choice);
        } catch (IllegalStateException | UnsupportedOperationException | IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeCrew(Player player, Coordinate pos) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, REMOVE, player);
            currentPlayerControl(player);
            typeControl(player, pos, TileType.CABIN);

            getCurrentCard().removeCrew(this, player, giveTile(player, pos));
        } catch (IllegalStateException | TileException | UnsupportedOperationException | IllegalPhaseException |
                 IllegalRemoveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void choiceBox(Player player, boolean choice) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, player);
            currentPlayerControl(player);

            getCurrentCard().choiceBox(this, player, choice);
        } catch (IllegalStateException | UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, BOXMANAGEMENT, player);
            currentPlayerControl(player);
            moveControl(player, start, end, boxType);

            if(start.x() == -1 && start.y() == -1){ //Start equals Planet
                getCurrentCard().moveBox(this, player, getCurrentCard().getBoxesWon(), giveTile(player, end).getOccupation(), boxType, on);
            } else if(end.x() == -1 && end.y() == -1){ //End equals Planet
                getCurrentCard().moveBox(this, player, giveTile(player, start).getOccupation(), getCurrentCard().getBoxesWon(), boxType, on);
            } else{ //Start and End are types of storage
                getCurrentCard().moveBox(this, player, giveTile(player, start).getOccupation(), giveTile(player, end).getOccupation(), boxType, on);
            }
        } catch (IllegalStateException | IllegalAddException | TileException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void choicePlanet(Player player, int index) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, player);
            currentPlayerControl(player);

            getCurrentCard().choicePlanet(this, player, index);
        } catch (IllegalStateException | UnsupportedOperationException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player);
            currentPlayerControl(player);
            choicesControl(player, motors, batteries, TileType.D_MOTOR);

            getCurrentCard().choiceDoubleMotor(this, player, motors, batteries);
        } catch (IllegalStateException | UnsupportedOperationException | IllegalPhaseException |
                 IllegalRemoveException | TileException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player);
            currentPlayerControl(player);
            choicesControl(player, cannons, batteries, TileType.D_CANNON);

            getCurrentCard().choiceDoubleCannon(this, player, cannons, batteries);
        } catch (IllegalStateException | UnsupportedOperationException | IllegalPhaseException |
                 IllegalRemoveException | TileException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void choiceCrew(Player player) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player);
            currentPlayerControl(player);

            getCurrentCard().choiceCrew(this, player);
        } catch (IllegalStateException | UnsupportedOperationException | IllegalPhaseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeBox(Player player, Coordinate pos, BoxType type) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, REMOVE, player);
            currentPlayerControl(player);

            if(player.getSpaceship().getTile(pos.x(),pos.y()).isPresent() && (
                    player.getSpaceship().getTile(pos.x(),pos.y()).get().getType().equals(TileType.SPECIAL_STORAGE) ||
                    player.getSpaceship().getTile(pos.x(),pos.y()).get().getType().equals(TileType.STORAGE))){
                getCurrentCard().removeBox(this, player, player.getSpaceship().getTile(pos.x(),pos.y()).get(), type);
            } else {
                throw new TileException("Tile is not a storage or doesn't exits");
            }
        } catch (IllegalStateException | TileException | UnsupportedOperationException | IllegalRemoveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeBattery(Player player, Coordinate pos) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, REMOVE, player);
            currentPlayerControl(player);
            typeControl(player, pos, TileType.BATTERY);

            getCurrentCard().removeBattery(this, player, giveTile(player, pos));
        } catch (IllegalStateException | TileException | UnsupportedOperationException | IllegalRemoveException e) {
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
    public void calculateDamage(Player player, Coordinate pos) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, CHOICE_ATTRIBUTES, player);
            currentPlayerControl(player);
            if(player.getSpaceship().getTile(pos.x(), pos.y()).isPresent()){
                typeControl(player, pos, TileType.BATTERY);
            }

            getCurrentCard().calculateDamage(this, player, player.getSpaceship().getTile(pos.x(),pos.y()));
        } catch (IllegalStateException | IllegalPhaseException | UnsupportedOperationException |
                 IllegalRemoveException | TileException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void effect(Game game) {
        try {
            stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, getCurrentPlayer());
            currentPlayerControl(getCurrentPlayer());
            getCurrentCard().effect(game);

        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void keepBlock(Player player, Coordinate pos) {
        try {
            //stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, player);
            stateControl(CORRECTION, WRONG_SHIP, FINISH, player);
            currentPlayerControl(player);

            player.getSpaceship().keepBlock(pos);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void Winners() {
        try {
            if (getCurrentState().getPhase() != RESULT) {
                throw new IllegalStateException("Wrong State");
            }

            int minExposedConnectors = Integer.MAX_VALUE;

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

                if (p.getStatePlayer() == IN_GAME) {
                    int exposedConnectors;
                    //Ranking points
                    p.getSpaceship().addCosmicCredits(getGameboard().getRewardPosition()[getGameboard().getRanking().indexOf(p)]);

                    exposedConnectors = p.getSpaceship().calculateExposedConnectors();
                    if (exposedConnectors < minExposedConnectors) {
                        minExposedConnectors = exposedConnectors;
                    }
                    //Sale of boxes
                    p.getSpaceship().addCosmicCredits(valueBox);
                } else {
                    //Sale of boxes
                    if (valueBox % 2 == 0) {
                        p.getSpaceship().addCosmicCredits(valueBox / 2);
                    } else {
                        p.getSpaceship().addCosmicCredits((valueBox / 2) + 1);
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
            //todo chiama un metodo della view per mostrare la classifica

        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
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

    private void cabinControl(Player player, Coordinate pos) throws TileException {
        if (player.getSpaceship().getTile(pos.x(), pos.y()).isEmpty()) {
            throw new TileException("No Tile in position ( " + pos.x() + ", " + pos.y() + " )");
        }
        if (!player.getSpaceship().getTile(pos.x(), pos.y()).get().getType().equals(TileType.CABIN)) {
            throw new TileException("Different TileType, expected " + TileType.CABIN + ", actual " + player.getSpaceship().getTile(pos.x(), pos.y()).get().getType());
        }
        if (!player.getSpaceship().getTile(pos.x(), pos.y()).get().getCrew().isEmpty()) {
            throw new TileException("Cabin already full");
        }
    }

    private void typeControl(Player player, Coordinate pos, TileType type) throws TileException {
        if (player.getSpaceship().getTile(pos.x(), pos.y()).isPresent() && !player.getSpaceship().getTile(pos.x(), pos.y()).get().getType().equals(type)) {
            throw new TileException("Tile (" + pos.x() + " " + pos.y() + ") from player " + player.getNickname() +" doesn't possess Type: " + type);
        } else if (player.getSpaceship().getTile(pos.x(),pos.y()).isEmpty()) {
            throw new TileException("No Tile in position ( " + pos.x() + ", " + pos.y() + " ) from player " + player.getNickname());
        }
    }

    private void buildControl() throws IllegalPhaseException {
        if (buildTimeIsOver) {
            throw new IllegalPhaseException("the time for building is over, call finishedSpaceship");
        }
    }

    private void levelControl() throws LevelException {
        if (level == 0) {
            throw new LevelException("functionality for higher levels");
        }
    }


    private void deckAllowedControl(Player player) throws IllegalPhaseException {
        if (!player.getDeckAllowed()) {
            throw new IllegalPhaseException("the player " + player.getNickname() + " is not allowed to see the minidecks");
        }
    }

    private boolean checkTileNear(Player player, Coordinate pos, TileType type) throws IllegalAddException, TileException {
        Tile tile = giveTile(player, pos);
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
            throw new IllegalAddException("There is no AlienCabin near (" + pos.x() + "," + pos.y() + ")");
        }
    }


    private void choicesControl(Player player, List<Coordinate> c1, List<Coordinate> c2, TileType t1)  throws TileException {
        if(c1.size() != c2.size()) {
            throw new TileException("Wrong number of tiles");
        }
        for(int i = 0; i < c1.size(); i++) {
            typeControl(player, c1.get(i), t1);
            typeControl(player, c2.get(i), TileType.BATTERY);
        }

        HashMap<Coordinate, Integer> control = new HashMap<>();
        for(Coordinate c : c2) {
            control.put(c, control.getOrDefault(c, 0) + 1);
        }
        for(Coordinate c : c2) {
            if(control.get(c) > giveTile(player,c).getNumBattery()){
                throw new TileException("Battery limit exceeded");
            }
        }
    }

    private void moveControl(Player player, Coordinate start, Coordinate end, BoxType boxType) throws IllegalAddException, TileException {
        //check that start contains the required boxtype
        if(start.x() != -1){ //Tile
            if(giveTile(player, start).getType().equals(TileType.SPECIAL_STORAGE) || giveTile(player, start).getType().equals(TileType.STORAGE)){
                if(giveTile(player, start).getOccupation().stream().noneMatch(p -> p.getType().equals(boxType))){
                    throw new IllegalAddException("Start doesn't contains any box with type " + boxType);
                }
            } else {
                throw new TileException("Start isn't a TileType Storage or Special Storage");
            }
        } else{ //Card list
            if(getCurrentCard().getBoxesWon().stream().noneMatch(p -> p.getType().equals(boxType))){
                throw new IllegalAddException("Start doesn't contains any box with type " + boxType);
            }
        }

        //check that end can contain the new box
        //check storage with red box transition
        if(end.x() != -1){ //Tile
            if(giveTile(player, end).getType().equals(TileType.SPECIAL_STORAGE)){
                if(giveTile(player, end).getOccupation().size() == giveTile(player, end).getMaxNum()){
                    throw new IllegalAddException("End is already full");
                }
            } else if(giveTile(player, end).getType().equals(TileType.STORAGE)){
                if(giveTile(player, end).getOccupation().size() == giveTile(player, end).getMaxNum()){
                    throw new IllegalAddException("End is already full");
                }
                if(boxType == BoxType.RED){
                    throw new IllegalAddException("End is type Storage, it doesn't contain red box");
                }
            } else {
                throw new TileException("Start isn't a TileType Storage or Special Storage");
            }
        }

        //check start != end
        if(start.x() == end.x() && start.y() == end.y()){
            throw new IllegalAddException("start and end are both equal");
        }
    }

    private void outOfGame() {
        HashMap<Player, Integer> positions = getGameboard().getPositions();
        for (Player p : getPlayers()) {
            //0 Human on my spaceship
            if (p.getSpaceship().calculateNumHuman() == 0) {
                getGameboard().getPositions().remove(p);
                p.setStatePlayer(OUT_GAME);
            }

            //0 motorPower in OpenSpace
            if (getCurrentCard().getCardType() == CardType.OPENSPACE) {
                if (getCurrentCard().getFly().get(p) == 0) {
                    getGameboard().getPositions().remove(p);
                    p.setStatePlayer(OUT_GAME);
                }
            }

            //lapped
            if (positions.get(getCurrentPlayer()) - positions.get(p) > getGameboard().getNumStep()) {
                getGameboard().getPositions().remove(p);
                p.setStatePlayer(OUT_GAME);
            }
        }
        getCurrentState().setCurrentPlayer(getGameboard().getRanking().getFirst());
    }

    private Tile giveTile(Player player, Coordinate pos) throws TileException {
        if(player.getSpaceship().getTile(pos.x(), pos.y()).isPresent()){
            return player.getSpaceship().getTile(pos.x(), pos.y()).get();
        } else {
            throw new TileException("Tile doesn't exist");
        }
    }
}
