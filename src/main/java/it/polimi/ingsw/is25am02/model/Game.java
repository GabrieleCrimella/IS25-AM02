package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.HashMap;
import java.util.List;

public class Game {
    private String gameName;
    private int maxAllowedPlayers;
    private List<Player> players;
    private Gameboard globalBoard;
    private int level;
    private CardDeck deck;
    private Hourglass hourglass;
    private HeapTiles heapTile;

    public Game(List<Player> p, int level){
        this.players = p;
        this.level = level;
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

    public void addTile(Player p, Tile t, int x, int y){
        p.getSpaceship().addTile(x, y, t);
    }
    public void shipFinished(Player p){//todo serve??
    }
    public boolean checkSpaceship(Player p){
        return p.getSpaceship().checkSpaceship();
    }
    public void removeTile(Player p, int x, int y){
        p.getSpaceship().removeTile(x, y);
    }
    public Card playNextCard(){
        return deck.playnextCard();
    }
    public HashMap<Player, Integer> getPositions(){
        return globalBoard.getPositions();
    }
    public List<Tile> possibleChoice(Player p, TileType t){
//todo
        return null;
    }
}
