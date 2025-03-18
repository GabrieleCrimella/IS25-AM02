package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.AlreadyViewingTileException;
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
    private CardDeck deck;
    private Hourglass hourglass;
    private final HeapTiles heapTile;
    private final Gameboard globalBoard;
    private final State currentState;
    private int alreadyFinished = 0; //tiene conto di quanti giocatori hanno già finito
    private int alreadyChecked = 0; //tiene conto dei giocatori che hanno la nave già controllata

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
    public Game GameCreator(List<Player> p, int level) {
        return new Game(p, level);
    }

    @Override
    public void flipHourglass() {
        hourglass.flip();
    }

    @Override
    public Tile takeTile(Player player) {
        Tile temp;

        //State Control
        if (getCurrentState().getPhase() == BUILD) {
            temp = heapTile.drawTile();
            try {
                player.getSpaceship().setCurrentTile(temp);
            } catch (AlreadyViewingTileException e) {
                //se il giocatore sta già guardando una tile, la tile pescata viene rimessa nel mazzo (in modo ancora invisibile) e viene in realtà restituita la tile che il giocatore stava guardando
                heapTile.addTile(temp, false);
                temp = player.getSpaceship().getCurrentTile();
            }
            return temp;
        } else throw new IllegalStateException("Non è il momento di pescare una tile");
    }

    @Override
    public Tile takeTile(Player player, Tile tile) {
        //State Control
        if (getCurrentState().getPhase() == BUILD) {
            heapTile.removeVisibleTile(tile);
            try {
                player.getSpaceship().setCurrentTile(tile);
                return tile;
            } catch (AlreadyViewingTileException e) {
                //se l'utente sta già guardando una tile, rimetto quella che ha passato come parametro nel mucchio e ritorno quella che sta già guardando
                heapTile.addTile(tile, true);
                return player.getSpaceship().getCurrentTile();
            }
        } else throw new IllegalStateException("Non è il momento di pescare una tile");
    }

    //il giocatore "scarta" la tile che stava guardando.
    @Override
    public void returnTile(Player player, Tile tile) {
        //State Control
        if (getCurrentState().getPhase() == BUILD) {
            Tile temp = player.getSpaceship().getCurrentTile();
            heapTile.addTile(temp, true);
            player.getSpaceship().returnTile();
        } else throw new IllegalStateException("Non è il momento di scartare una tile");
    }

    @Override
    public void addTile(Player player, Tile tile, int x, int y) {
        //State Control
        if (getCurrentState().getPhase() == BUILD) {
            player.getSpaceship().addTile(x, y, tile);
        } else throw new IllegalStateException("Non è il momento di aggiungere una tile");
    }

    //player passa alla fase di finish, se è la 4 volta che viene chiamato allora cambio lo stato di tutti gli altri
    //aggiungo i giocatori alla gameboard
    @Override
    public void shipFinished(Player player) {
        int[] position = getGameboard().getStartingPosition();
        player.setStatePlayer(FINISHED);
        alreadyFinished++;
        getGameboard().positions.put(player, position[players.size() - alreadyFinished - 1]);
        if (alreadyFinished == players.size()) { //se tutti i giocatori sono nella fase di finish allora passo alla fase di check
            this.currentState.setPhase(StateGameType.CHECK);
        }
    }

    @Override
    public boolean checkSpaceship(Player player) {
        if (player.getSpaceship().checkSpaceship()) {//se è corretta il player passa nello stato CORRECT
            player.setStatePlayer(CORRECT_SHIP);
            alreadyChecked++;
        } else {
            player.setStatePlayer(WRONG_SHIP);//se la nave è sbagliata passo nello stato di wrong
        }
        if (alreadyChecked == players.size()) { //se tutti i giocatori hanno la nave corretta allora passo alla fase successiva
            this.currentState.setPhase(StateGameType.INITIALIZATION_SPACESHIP);
            return true;
        }
        for (Player p : players) {//se c'è anche un solo giocatore che è in wrong_ship allora si passa alla fase di correction
            if (p.getStatePlayer().equals(WRONG_SHIP)) {
                this.currentState.setPhase(StateGameType.CORRECTION);
            }
        }
        return false;
    }

    @Override
    public void removeTile(Player player, int x, int y) {
        player.getSpaceship().removeTile(x, y);

    }

    //per inizializzazione delle cabine
    @Override
    public void addCrew(Player player, int x, int y, AliveType type) {
        if (type.equals(AliveType.HUMAN) && player.getSpaceship().getTile(x, y).isPresent()) { // faccio l'istruzione due volte perchè aggiungo due umani
            player.getSpaceship().getTile(x, y).get().addCrew(type);
            player.getSpaceship().getTile(x, y).get().addCrew(type);
        } else if (type.equals(AliveType.BROWN_ALIEN)) { //devo controllare se c'è una cabina brown collegata ad almeno una cabina normale
            for (Optional<Tile> tile : player.getSpaceship().getSpaceshipIterator()) {
                if (tile.isPresent() && tile.get().getType().equals(TileType.BROWN_CABIN)) {
                    if (player.getSpaceship().getSpaceshipIterator().getUpTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getUpTile(tile.get()).get().getType().equals(TileType.CABIN)) {// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x, y).get().addCrew(type);
                        return;
                    }
                    if (player.getSpaceship().getSpaceshipIterator().getDownTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getDownTile(tile.get()).get().getType().equals(TileType.CABIN)) {// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x, y).get().addCrew(type);
                        return;
                    }
                    if (player.getSpaceship().getSpaceshipIterator().getRightTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getRightTile(tile.get()).get().getType().equals(TileType.CABIN)) {// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x, y).get().addCrew(type);
                        return;
                    }
                    if (player.getSpaceship().getSpaceshipIterator().getLeftTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getLeftTile(tile.get()).get().getType().equals(TileType.CABIN)) {// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x, y).get().addCrew(type);
                        return;
                    }
                }

            }
        } else { //caso dei purple alien
            for (Optional<Tile> tile : player.getSpaceship().getSpaceshipIterator()) {
                if (tile.isPresent() && tile.get().getType().equals(TileType.PURPLE_CABIN)) {
                    if (player.getSpaceship().getSpaceshipIterator().getUpTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getUpTile(tile.get()).get().getType().equals(TileType.CABIN)) {// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x, y).get().addCrew(type);
                        return;
                    }
                    if (player.getSpaceship().getSpaceshipIterator().getDownTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getDownTile(tile.get()).get().getType().equals(TileType.CABIN)) {// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x, y).get().addCrew(type);
                        return;
                    }
                    if (player.getSpaceship().getSpaceshipIterator().getRightTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getRightTile(tile.get()).get().getType().equals(TileType.CABIN)) {// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x, y).get().addCrew(type);
                        return;
                    }
                    if (player.getSpaceship().getSpaceshipIterator().getLeftTile(tile.get()).isPresent() &&
                            player.getSpaceship().getSpaceshipIterator().getLeftTile(tile.get()).get().getType().equals(TileType.CABIN)) {// se c'è una cabina collegata, allora posso aggiungere un alieno brown
                        player.getSpaceship().getTile(x, y).get().addCrew(type);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void playNextCard(Player player) {
        if (!player.equals(getGameboard().getRanking().getFirst())) { //se il player non è un leader allora lancio eccezione
            throw new IllegalStateException("Il player non è il leader");
        }
        if (!player.getStatePlayer().equals(IN_GAME)) { //se il player non è in game allora lancio eccezione
            throw new IllegalStateException("Il player non è IN GAME");
        }
        if (!getCurrentCard().getStateCard().equals(FINISH)) {
            throw new IllegalStateException("La carta prima non è finita");
        }
        if (!getCurrentState().getPhase().equals(TAKE_CARD)) {
            throw new IllegalStateException("Il gioco non è in stato di take card");
        }

        getDeck().playnextCard();
    }

    @Override
    public HashMap<Player, Integer> getPosition() {
        return getGameboard().getPositions();
    }

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

    @Override
    public ArrayList<Player> getWinners() {
        ArrayList<Player> winners = new ArrayList<>();
        if (getPlayers() == null) {
            throw new IllegalArgumentException("La lista dei giocatori è vuota");
        }
        for (Player p : getPlayers()) {
            if (p.getSpaceship().getCosmicCredits() > 0) {
                winners.add(p);
            }
        }
        return winners;
    }
}
