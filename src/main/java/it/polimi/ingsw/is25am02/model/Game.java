package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.*;
import it.polimi.ingsw.is25am02.model.exception.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalStateException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("all")
public class Game implements Game_Interface {
    private int diceResult;
    private boolean buildTimeIsOver;
    private int maxAllowedPlayers;
    private final List<Player> players;
    private final ConcurrentHashMap<String, VirtualView> observers;
    private final int level;
    private final CardDeck deck;
    private final Hourglass hourglass;
    private final HeapTiles heapTile;
    private final Gameboard globalBoard;
    private final State currentState;
    private int alreadyFinished = 0; //tiene conto di quanti giocatori hanno già finito
    private int alreadyChecked = 0;  //tiene conto dei giocatori che hanno la nave già controllata
    private int readyPlayer = 0;
    private final List<Player> winners = new ArrayList<>();

    public Game(List<Player> players, int level) {
        this.players = players;
        this.observers= new ConcurrentHashMap<>();
        for(Player p : players){
            observers.put(p.getNickname(), p.getObserver());
        }
        for(Player p : players){
            p.getSpaceship().setObservers(observers);
            p.getSpaceship().getSpaceshipIterator().setObservers(observers);
        }
        this.level = level;
        this.diceResult = 0;
        this.buildTimeIsOver = false;
        this.globalBoard = new Gameboard(level);
        this.heapTile = new HeapTiles();
        for (Player player : players) {
            try {
                player.getSpaceship().addInitialTile(player.getSpaceship().getSpaceshipIterator().getX_start(), player.getSpaceship().getSpaceshipIterator().getY_start(), heapTile.getCabinStartPlayer().get(player.getColor()));
            } catch (IllegalAddException e) {
                System.out.println(e.getMessage());
            }
        }
        this.currentState = new State(players.getFirst());
        this.deck = new CardDeck(level);
        this.hourglass = new Hourglass();
    }

    //for testing
    public void tilesSituation(){
        System.out.println("Visible Tiles:");
        Set<Tile> visibleTiles;
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

    public CardDeck getDeck() {
        return deck;
    }

    public Gameboard getGameboard() {
        return globalBoard;
    }

    public List<Player> getPlayers() {
        return players;
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
            getCurrentCard().setStateCard(StateCardType.FINISH);
        } else if (globalBoard.getRanking().get(index + 1).getStatePlayer() == StatePlayerType.IN_GAME) {//se il prossimo giocatore è in gioco allora lo metto come prossimo giocatore corrente
            currentState.setCurrentPlayer(getGameboard().getRanking().get(index + 1));//metto il prossimo giocatore come giocatore corrente
        }
    }

    private Optional<Tile> getTileFromImagePath(String tileImagePath) {
        for (Tile tile : heapTile.getVisibleTiles()) {
            if (tile.getImagePath().equals(tileImagePath)) {
                return Optional.of(tile);
            }
        }
        return Optional.empty();
    }

    @Override
    public void flipHourglass(Player player) {
        try {
            levelControl();
            buildControl();
            if (!hourglass.getRunning()) {
                if (globalBoard.getHourGlassFlip() > 1) {
                    stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, player);
                    hourglass.flip(this);
                    globalBoard.decreaseHourGlassFlip();

                } else if (globalBoard.getHourGlassFlip() == 1) {
                    stateControl(StateGameType.BUILD, StatePlayerType.FINISHED, StateCardType.FINISH, player);
                    hourglass.flip(this);
                    globalBoard.decreaseHourGlassFlip();
                }
                player.onHourglassUpdate();
            } else {
                throw new IllegalStateException("");
            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.hourglass", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method getLobbies");
            }
        } catch (LevelException e) {
            try {
                player.getObserver().reportError("error.level", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method getLobbies");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method getLobbies");
            }
        }
    }

    @Override
    public void takeMiniDeck(Player player, int index) {
        try {
            levelControl();
            buildControl();
            stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, player);
            currentTileControl(player);
            deckAllowedControl(player);

            player.setNumDeck(index);
            deck.giveDeck(index);
            player.onMiniDeckUpdate(index);

        } catch (LevelException e) {
            try {
                player.getObserver().reportError("error.level", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method takeminideck");
            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method takeminideck");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method takeminideck");
            }
        } catch (AlreadyViewingException e) {
            try {
                player.getObserver().reportError("error.viewing", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method takeminideck");
            }
        }
    }

    @Override
    public void returnMiniDeck(Player player) {
        try {
            levelControl();
            buildControl();
            stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, player);
            currentTileControl(player);
            deckAllowedControl(player);

            deck.returnDeck(player.getNumDeck());
            player.setNumDeck(-1);

        } catch (LevelException e) {
            try {
                player.getObserver().reportError("error.level", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method returnminideck");
            }
        } catch (AlreadyViewingException e) {
            try {
                player.getObserver().reportError("error.viewing", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method returnminideck");
            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method returnminideck");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method returnminideck");
            }
        }
    }

    //fatta
    @Override
    public void takeTile(Player player) {
        try {
            buildControl();
            stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, player);
            currentTileControl(player);

            Tile drawnTile = heapTile.drawTile();
            player.getSpaceship().setCurrentTile(drawnTile);

            for (Player p : players){
                p.onCurrentTileUpdate(player.getNickname(), drawnTile);
            }

        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method takeTile (player)");
            }
        } catch (AlreadyViewingException e) {
            try {
                player.getObserver().reportError("error.viewing", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method takeTile (player)");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method takeTile (player)");
            }
        }
    }

    //fatta
    @Override
    public void takeTile(Player player, String tile_imagePath) {
        try {
            buildControl();
            stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, player);
            currentTileControl(player);

            Optional<Tile> tile = getTileFromImagePath(tile_imagePath);
            if (tile.isEmpty()) {
                throw new TileException("Tile not found");
            }

            player.getSpaceship().setCurrentTile(tile.get());
            heapTile.removeVisibleTile(tile.get());

            for (Player p : players){
                p.onCurrentTileUpdate(player.getNickname(), tile.get());
                p.onTileRemovalFromHTUpdate(tile.get().getImagePath());

            }

        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method taketile (player,tile)");
            }
        } catch (AlreadyViewingException e) {
            try {
                player.getObserver().reportError("error.viewing", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method taketile (player,tile)");
            }
        } catch (IllegalRemoveException e) {
            try {
                player.getObserver().reportError("error.remove", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method taketile (player,tile)");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method taketile (player,tile)");
            }
        } catch (TileException e) {
            try {
                player.getObserver().reportError("error.tile", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method taketile (player,tile)");
            }
        }
    }

    //fatta
    @Override
    public void returnTile(Player player) {
        try {
            buildControl();
            stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, player);

            heapTile.addTile(player.getSpaceship().getCurrentTile(), true);
            player.getSpaceship().returnTile();

            for (Player p : players){
                player.onVsibilityUpdate(player.getNickname(), player.getSpaceship().getCurrentTile());
                player.onCurrentTileNullityUpdate(player.getNickname());
            }


        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method returnTile");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method returnTile");
            }
        }
    }

    //fatta
    @Override
    public void addTile(Player player, Coordinate pos, RotationType rotation) {
        try {
            buildControl();
            stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, player);
            Tile currentTile = player.getSpaceship().getCurrentTile();
            currentTile.setRotationType(rotation);

            player.getSpaceship().addTile(pos.x(), pos.y(), currentTile);


            //player can see the minidecks
            if (!player.getDeckAllowed()) {
                player.setDeckAllowed();
                player.onDeckAllowedUpdate();
            }

            for (Player p : players){
                p.onTileAdditionToSpaceship(player.getNickname(),player.getSpaceship().getTile(pos.x(), pos.y()).get() , pos);
                p.onCurrentTileNullityUpdate(player.getNickname());
            }

        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method addtile");
            }
        } catch (IllegalAddException e) {
            try {
                player.getObserver().reportError("error.add", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method addtile");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method addtile");
            }
        }
    }

    //fatta
    public void bookTile(Player player) {
        try {
            levelControl();
            buildControl();
            stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, player);

            player.getSpaceship().bookTile(player);

            for (Player p : players){
                p.onBookTileUpdate(player.getNickname());
                p.onCurrentTileNullityUpdate(player.getNickname());

            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method booktile");
            }
        } catch (IllegalAddException e) {
            try {
                player.getObserver().reportError("error.add", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method booktile");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method booktile");
            }
        } catch (LevelException e) {
            try {
                player.getObserver().reportError("error.level", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method booktile");
            }
        }
    }

    //todo mancano gli update necessari per questo metodo
    @Override
    public void addBookedTile(Player player, int index, Coordinate pos, RotationType  rotation) {
        try {
            levelControl();
            buildControl();
            stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, player);

            player.getSpaceship().addBookedTile(index, pos.x(), pos.y(), rotation);
            for (Player p: players){
                p.onTileAdditionToSpaceship(player.getNickname(), player.getSpaceship().getTile(pos.x(), pos.y()).get(),pos);
            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method addbookedTile");
            }
        } catch (IllegalAddException e) {
            try {
                player.getObserver().reportError("error.add", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method addbookedTile");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method addbookedTile");
            }
        } catch (LevelException e) {
            try {
                player.getObserver().reportError("error.level", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method addbookedTile");
            }
        }
    }

    //fatta
    //player goes to the FINISH phase, if he's the last one I change the  game state to CHECK
    //I add the players to the gameboard
    @Override
    public void shipFinished(Player player) {
        try {
            stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, player);

            player.setStatePlayer(StatePlayerType.FINISHED);
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

            for (Player p : players){
                p.onPlayerStateUpdate(player.getNickname(), player.getStatePlayer());
                p.onPositionUpdate(player.getNickname(), getGameboard().getStartingPosition()[players.size() - alreadyFinished]);
            }


        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method shipfinished");
            }
        }
    }

    //fatta
    @Override
    public void checkSpaceship(Player player) {
        try {
            stateControl(StateGameType.CHECK, StatePlayerType.FINISHED, StateCardType.FINISH, player);

            if (player.getSpaceship().checkSpaceship()) {
                player.setStatePlayer(StatePlayerType.CORRECT_SHIP);
            } else {
                player.setStatePlayer(StatePlayerType.WRONG_SHIP);
            }
            alreadyChecked++;

            if (alreadyChecked == players.size()) {
                this.currentState.setPhase(StateGameType.INITIALIZATION_SPACESHIP);
                for (Player p : players) {
                    if (p.getStatePlayer().equals(StatePlayerType.WRONG_SHIP)) {
                        this.currentState.setPhase(StateGameType.CORRECTION);
                        alreadyChecked--;
                    }
                }
            }

            for (Player p : players) {
                p.onPlayerStateUpdate(player.getNickname(), player.getStatePlayer());
            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method checkspaceship");
            }
        }
    }

    //todo serve modo alternativo per fare update perchè la remove tile rimuove più tile in una volta
    @Override
    public void removeTile(Player player, Coordinate pos) {
        try {
            stateControl(StateGameType.CORRECTION, StatePlayerType.WRONG_SHIP, StateCardType.FINISH, player);

            player.getSpaceship().removeTile(pos.x(), pos.y());
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removeTile");
            }
        } catch (IllegalRemoveException e) {
            try {
                player.getObserver().reportError("error.remove", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removeTile");
            }
        }
    }

    //fatta
    @Override
    public void checkWrongSpaceship(Player player) {
        try {
            stateControl(StateGameType.CORRECTION, StatePlayerType.WRONG_SHIP, StateCardType.FINISH, player);

            if (player.getSpaceship().checkSpaceship()) {
                player.setStatePlayer(StatePlayerType.CORRECT_SHIP);
                alreadyChecked++;
            }

            if (alreadyChecked == players.size()) {
                this.currentState.setPhase(StateGameType.INITIALIZATION_SPACESHIP);
            }

            for (Player p : players) {
                p.onPlayerStateUpdate(player.getNickname(), player.getStatePlayer());
            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method checkWrongSpaceship");
            }
        }
    }

    //todo manca update per fare questo metodo
    @Override
    public void addCrew(Player player, Coordinate pos, AliveType type) {
        try {
            levelControl();
            stateControl(StateGameType.INITIALIZATION_SPACESHIP, StatePlayerType.CORRECT_SHIP, StateCardType.FINISH, player);
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
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method addCrew");
            }
        } catch (TileException e) {
            try {
                player.getObserver().reportError("error.tile", Map.of("tType", "Cabin"));
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method addCrew");
            }
        } catch (IllegalAddException e) {
            try {
                player.getObserver().reportError("error.add", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method addCrew");
            }
        } catch (LevelException e) {
            try {
                player.getObserver().reportError("error.level", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method addCrew");
            }
        }
    }

    //todo manca l'update per addcrew
    //the player has finished the initialization phase and is ready to play
    @Override
    public void ready(Player player) {
        try {
            stateControl(StateGameType.INITIALIZATION_SPACESHIP, StatePlayerType.CORRECT_SHIP, StateCardType.FINISH, player);

            player.setStatePlayer(StatePlayerType.IN_GAME);
            readyPlayer++;

            //1) in case the player doesn't want to initialize all the cabins by himself
            //2) it's the tutorial level
            List<Tile> cabins = player.getSpaceship().getTilesByType(TileType.CABIN);
            for (Tile c : cabins) {
                if (c.getCrew().isEmpty()) {
                    //todo bisogna chiamare l'update per addCrew
                    c.addCrew(AliveType.HUMAN);
                    c.addCrew(AliveType.HUMAN);
                }
            }
            //when all players are ready the game starts
            if (readyPlayer == players.size()) {
                getCurrentState().setPhase(StateGameType.TAKE_CARD);
            }

            for (Player p : players) {
                p.onPlayerStateUpdate(player.getNickname(), player.getStatePlayer());
            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method ready");
            }
        }
    }

    //todo update per gameboard
    @Override
    public void earlyLanding(Player player) {
        try {
            levelControl();
            stateControl(StateGameType.TAKE_CARD, StatePlayerType.IN_GAME, StateCardType.FINISH, player);

            getGameboard().getPositions().remove(player);
            player.setStatePlayer(StatePlayerType.OUT_GAME);

            getCurrentState().setCurrentPlayer(getGameboard().getRanking().getFirst());

            for  (Player p : players) {
                p.onPlayerStateUpdate(player.getNickname(), player.getStatePlayer());
            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method earlylanding");
            }
        } catch (LevelException e) {
            try {
                player.getObserver().reportError("error.level", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method earlylanding");
            }
        }
    }

    //todo questa funzione non fa niente
    @Override
    public void playNextCard(Player player) {
        try {
            stateControl(StateGameType.TAKE_CARD, StatePlayerType.IN_GAME, StateCardType.FINISH, player);
            outOfGame();
            currentPlayerControl(player);

            if (deck.playnextCard(this) == null || globalBoard.getPositions().isEmpty()) {
                this.getCurrentState().setPhase(StateGameType.RESULT);
            } else {
                this.getCurrentState().setPhase(StateGameType.EFFECT_ON_PLAYER);
            }


            for (Player p : players) {
                p.onCurrentCardUpdate(getCurrentCard().getImagePath(), getCurrentCard().getStateCard(), getCurrentCard().getCardType());
            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method playNextCard");
            }
        }
    }

    @Override
    public void choice(Player player, boolean choice) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.DECISION, player);
            currentPlayerControl(player);

            getCurrentCard().choice(this, player, choice);
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choice");
            }
        } catch (UnsupportedOperationException e) {
            try {
                player.getObserver().reportError("error.command", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choice");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choice");
            }
        }
    }

    @Override
    public void removeCrew(Player player, Coordinate pos) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.REMOVE, player);
            currentPlayerControl(player);
            typeControl(player, pos, TileType.CABIN);

            getCurrentCard().removeCrew(this, player, giveTile(player, pos));
            for (Player p: getPlayers()) {
                if(giveTile(player,pos)!=null){
                    //todo bisogna riaggiunere la linea sotto, solo che removecrewupdate viene chiamato anche nelle carte e quindi devo capire come aggiustare quelle
                    //    p.onRemoveCrewUpdate(player.getNickname(), pos, giveTile(player,pos).getCrew().size());
                }

            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removecrew");
            }
        } catch (TileException e) {
            try {
                player.getObserver().reportError("error.tile", Map.of("tType", "cabin"));
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removecrew");
            }
        } catch (UnsupportedOperationException e) {
            try {
                player.getObserver().reportError("error.command", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removecrew");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removecrew");
            }
        } catch (IllegalRemoveException e) {
            try {
                player.getObserver().reportError("error.remove", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removecrew");
            }
        }
    }

    @Override
    public void choiceBox(Player player, boolean choice) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.DECISION, player);
            currentPlayerControl(player);

            getCurrentCard().choiceBox(this, player, choice);
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceBox");
            }
        } catch (UnsupportedOperationException e) {
            try {
                player.getObserver().reportError("error.command", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceBox");
            }
        }
    }

    @Override
    public void moveBox(Player player, Coordinate start, Coordinate end, BoxType boxType, boolean on) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.BOXMANAGEMENT, player);
            currentPlayerControl(player);
            moveControl(player, start, end, boxType);

            if(start.x() == -1 && start.y() == -1){ //Start equals Planet
                getCurrentCard().moveBox(this, player, getCurrentCard().getBoxesWon(), giveTile(player, end).getOccupation(), boxType, on);
            } else if(end.x() == -1 && end.y() == -1){ //End equals Planet
                getCurrentCard().moveBox(this, player, giveTile(player, start).getOccupation(), getCurrentCard().getBoxesWon(), boxType, on);
            } else{ //Start and End are types of storage
                getCurrentCard().moveBox(this, player, giveTile(player, start).getOccupation(), giveTile(player, end).getOccupation(), boxType, on);
            }
            //player.onBoxUpdate(start,player.getSpaceship().getTile(start.x(), start.y()).get().getOccupationTypes());
            //player.onBoxUpdate(end,player.getSpaceship().getTile(end.x(), end.y()).get().getOccupationTypes());
            //todo metti questi update nelle carte
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method moveBox");
            }
        } catch (IllegalAddException e) {
            try {
                player.getObserver().reportError("error.add", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method moveBox");
            }
        } catch (TileException e) {
            try {
                player.getObserver().reportError("error.tile", Map.of("tType", "storage / card"));
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method moveBox");
            }
        }
    }

    @Override
    public void choicePlanet(Player player, int index) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.DECISION, player);
            currentPlayerControl(player);

            getCurrentCard().choicePlanet(this, player, index);
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choicePlanet");
            }
        } catch (UnsupportedOperationException e) {
            try {
                player.getObserver().reportError("error.command", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choicePlanet");
            }
        } catch (IllegalArgumentException e) {
            try {
                player.getObserver().reportError("error.argument", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choicePlanet");
            }
        }
    }

    @Override
    public void choiceDoubleMotor(Player player, List<Coordinate> motors, List<Coordinate> batteries) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.CHOICE_ATTRIBUTES, player);
            currentPlayerControl(player);
            choicesControl(player, motors, batteries, TileType.D_MOTOR);

            getCurrentCard().choiceDoubleMotor(this, player, motors, batteries);
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceDoubleMotor");
            }
        } catch (UnsupportedOperationException e) {
            try {
                player.getObserver().reportError("error.command", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceDoubleMotor");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceDoubleMotor");
            }
        } catch (IllegalRemoveException e) {
            try {
                player.getObserver().reportError("error.remove", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceDoubleMotor");
            }
        } catch (TileException e) {
            try {
                player.getObserver().reportError("error.tile", Map.of("tType", "motors / battery storages"));
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceDoubleMotor");
            }
        }
    }

    @Override
    public void choiceDoubleCannon(Player player, List<Coordinate> cannons, List<Coordinate> batteries) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.CHOICE_ATTRIBUTES, player);
            currentPlayerControl(player);
            choicesControl(player, cannons, batteries, TileType.D_CANNON);

            getCurrentCard().choiceDoubleCannon(this, player, cannons, batteries);
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceDoubleCannon");
            }
        } catch (UnsupportedOperationException e) {
            try {
                player.getObserver().reportError("error.command", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceDoubleCannon");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceDoubleCannon");
            }
        } catch (IllegalRemoveException e) {
            try {
                player.getObserver().reportError("error.remove", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceDoubleCannon");
            }
        } catch (TileException e) {
            try {
                player.getObserver().reportError("error.tile", Map.of("tType", "cannons / battery storages"));
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceDoubleCannon");
            }
        }
    }

    @Override
    public void choiceCrew(Player player) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.CHOICE_ATTRIBUTES, player);
            currentPlayerControl(player);

            getCurrentCard().choiceCrew(this, player);
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceCrew");
            }
        } catch (UnsupportedOperationException e) {
            try {
                player.getObserver().reportError("error.command", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceCrew");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method choiceCrew");
            }
        }
    }

    @Override
    public void removeBox(Player player, Coordinate pos, BoxType type) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.REMOVE, player);
            currentPlayerControl(player);

            if(player.getSpaceship().getTile(pos.x(),pos.y()).isPresent() && (
                    player.getSpaceship().getTile(pos.x(),pos.y()).get().getType().equals(TileType.SPECIAL_STORAGE) ||
                    player.getSpaceship().getTile(pos.x(),pos.y()).get().getType().equals(TileType.STORAGE))){
                getCurrentCard().removeBox(this, player, player.getSpaceship().getTile(pos.x(),pos.y()).get(), type);
                //player.onBoxUpdate(pos,player.getSpaceship().getTile(pos.x(),pos.y()).get().getOccupationTypes());
                //todo metti questo update nella carta
            } else {
                throw new TileException("Tile is not a storage or doesn't exits");
            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removeBox");
            }
        } catch (TileException e) {
            try {
                player.getObserver().reportError("error.tile", Map.of("tType", "storage"));
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removeBox");
            }
        } catch (UnsupportedOperationException e) {
            try {
                player.getObserver().reportError("error.command", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removeBox");
            }
        } catch (IllegalRemoveException e) {
            try {
                player.getObserver().reportError("error.remove", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removeBox");
            }
        }
    }

    @Override
    public void removeBattery(Player player, Coordinate pos) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.REMOVE, player);
            currentPlayerControl(player);
            typeControl(player, pos, TileType.BATTERY);

            getCurrentCard().removeBattery(this, player, giveTile(player, pos));
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removeBattery");
            }
        } catch (TileException e) {
            try {
                player.getObserver().reportError("error.tile", Map.of("tType", "battery storage"));
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removeBattery");
            }
        } catch (UnsupportedOperationException e) {
            try {
                player.getObserver().reportError("error.command", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removeBattery");
            }
        } catch (IllegalRemoveException e) {
            try {
                player.getObserver().reportError("error.remove", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method removeBattery");
            }
        }
    }

    @Override
    public void rollDice(Player player) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.ROLL, player);
            currentPlayerControl(player);

            setDiceResult();
            getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);

            for (Player p: players){
                p.onDiceUpdate(player.getNickname(), getGameboard().getDice());
            }
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method rollDice");
            }
        }
    }

    @Override
    public void calculateDamage(Player player, Coordinate pos) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.CHOICE_ATTRIBUTES, player);
            currentPlayerControl(player);
            if(player.getSpaceship().getTile(pos.x(), pos.y()).isPresent()){
                typeControl(player, pos, TileType.BATTERY);
            }

            getCurrentCard().calculateDamage(this, player, player.getSpaceship().getTile(pos.x(),pos.y()));
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method calculateDamage");
            }
        } catch (IllegalPhaseException e) {
            try {
                player.getObserver().reportError("error.phase", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method calculateDamage");
            }
        } catch (UnsupportedOperationException e) {
            try {
                player.getObserver().reportError("error.command", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method calculateDamage");
            }
        } catch (IllegalRemoveException e) {
            try {
                player.getObserver().reportError("error.remove", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method calculateDamage");
            }
        } catch (TileException e) {
            try {
                player.getObserver().reportError("error.tile", Map.of("tType", "battery storage"));
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method calculateDamage");
            }
        }
    }

    @Override
    public void effect(Game game) {
        try {
            stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.DECISION, getCurrentPlayer());
            currentPlayerControl(getCurrentPlayer());

            getCurrentCard().effect(game);

        } catch (IllegalStateException e) {
            for(Player p: players){
                try {
                    p.getObserver().reportError("error.state", null);
                } catch (Exception ex) {
                    reportErrorOnServer("connection problem in method effect");
                }
            }
        }
    }

    //todo non c'è l'update necessario per questo metodo
    @Override
    public void keepBlock(Player player, Coordinate pos) {
        try {
            //stateControl(EFFECT_ON_PLAYER, IN_GAME, DECISION, player);
            stateControl(StateGameType.CORRECTION, StatePlayerType.WRONG_SHIP, StateCardType.FINISH, player);
            currentPlayerControl(player);

            player.getSpaceship().keepBlock(pos);
        } catch (IllegalStateException e) {
            try {
                player.getObserver().reportError("error.state", null);
            } catch (Exception ex) {
                reportErrorOnServer("connection problem in method keepBlock");
            }
        }
    }

    @Override
    public void Winners() {
        try {
            if (getCurrentState().getPhase() != StateGameType.RESULT) {
                throw new IllegalStateException("");
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

                if (p.getStatePlayer() == StatePlayerType.IN_GAME) {
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
                if (p.getStatePlayer() == StatePlayerType.IN_GAME && p.getSpaceship().calculateExposedConnectors() == minExposedConnectors) {
                    p.getSpaceship().addCosmicCredits(getGameboard().getBestShip());
                }
            }

            for (Player p : getPlayers()) {
                if (p.getSpaceship().getCosmicCredits() > 0) {
                    winners.add(p);
                }
            }

            winners.sort(Comparator.comparingInt(p -> p.getSpaceship().getCosmicCredits()));
            //todo chiama un metodo della view per mostrare la classifica

        } catch (IllegalStateException e) {
            for(Player p: players){
                try {
                    p.getObserver().reportError("error.state", null);
                } catch (Exception ex) {
                    reportErrorOnServer("connection problem in method Winners");
                }
            }
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

    @SuppressWarnings("SameReturnValue")
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
                p.setStatePlayer(StatePlayerType.OUT_GAME);
                p.onPlayerStateUpdate(p.getNickname(), StatePlayerType.OUT_GAME);
            }

            //0 motorPower in OpenSpace
            if (getCurrentCard().getCardType() == CardType.OPENSPACE) {
                if (getCurrentCard().getFly().get(p) == 0) {
                    getGameboard().getPositions().remove(p);
                    p.setStatePlayer(StatePlayerType.OUT_GAME);
                    p.onPlayerStateUpdate(p.getNickname(), StatePlayerType.OUT_GAME);
                }
            }

            //lapped
            if (positions.get(getCurrentPlayer()) - positions.get(p) > getGameboard().getNumStep()) {
                getGameboard().getPositions().remove(p);
                p.setStatePlayer(StatePlayerType.OUT_GAME);
                p.onPlayerStateUpdate(p.getNickname(), StatePlayerType.OUT_GAME);
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

    private void reportErrorOnServer(String message) {
        System.err.println(">> " + message);
    }
}
