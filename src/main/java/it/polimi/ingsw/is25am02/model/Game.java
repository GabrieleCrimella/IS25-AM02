package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Game {
    private Card currentCard;
    private Player currentPlayer;
    private Gameboard globalBoard;
    private int diceResult;

    private String gameName;
    private int maxAllowedPlayers;
    private List<Player> players;
    private int level;
    private CardDeck deck;
    private Hourglass hourglass;
    private HeapTiles heapTile;

    public Game(List<Player> p, int level){
        this.players = p;
        this.level = level;
        this.diceResult = 0;
    }

    public CardDeck getDeck(){
        return deck;
    }

    public Gameboard getGameboard(){
        return globalBoard;
    }
    public List<Player> getWinners(){//ritorna la lista dei giocatori che hanno una quantità di crediti positivi
        return null;
    }
    private List<Player> getPlayers(){
        return null;
    }
    public String getName(){
        return gameName;
    }
    public int getlevel(){
        return level;
    }
    public int getMaxAllowedPlayers(){
        return maxAllowedPlayers;
    }

    public Card getCurrentCard(){
        return currentCard;
    }


    public HeapTiles getHeapTile() {
        return heapTile;
    }
    public Hourglass getHourglass(){
        return hourglass;
    }

    public void flipHourglass(){
        hourglass.flip();
    }
    public Tile getTile(Player p, int x, int y){//restituisce il tile in una certa posizione
        return p.getSpaceship().getTile(x, y);
    }
    public void returnTile(Tile t){// vedo la tile e la ributto nel mucchio
        //todo devo aggiungere la tile all'heap tiles
    }

    public void addTile(Player p, Tile t, int x, int y, RotationType rotation){
        t.setRotationType(rotation);
        p.getSpaceship().addTile(x, y, t);
    }
    public void shipFinished(Player p){//todo prende il player e lo mette in una delle posizioni di partenza
    }
    public boolean checkSpaceship(Player p){
        return p.getSpaceship().checkSpaceship();
    }
    public void removeTile(Player p, int x, int y){
        p.getSpaceship().removeTile(x, y);
    }
    public HashMap<Player, Integer> getPositions(){
        return globalBoard.getPositions();
    }
    public List<Tile> possibleChoice(Player p, TileType t){
        //todo mi ritorna una lista con tutti con tutte le tile di quel tipo per quel giocatore
        return null;
    }
    public void removeTile(int x, int y) {
        //todo
    }
    public Set<Tile> getVisibleTiles(){
        return heapTile.getVisibleTiles();
    }

    public void playNextCard(){

        this.currentPlayer = getGameboard().getRanking().getFirst();
        this.currentCard = deck.playnextCard();
    }

    public int getDiceResult() {
        return diceResult;
    }

    public void setDiceResult(int diceResult) {
        this.diceResult = getGameboard().getDice().pickRandomNumber();
    }

    public void nextPlayer(){
        //Player diventa il prossimo player
        //Se il player è l'ultimo il currentPlayer deve diventare il nuovo primo e cambiare carta. (forse exception forse viene implementato qui dentro)
        //Gioca la carta corrente con il player nuovo
    }

    public void previousPlayer(){
        //come next player ma torna indietro
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
