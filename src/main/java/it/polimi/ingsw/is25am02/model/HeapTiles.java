package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class HeapTiles {
    private Set<Tile> setTiles;
    private Random random;

    public HeapTiles(Set<Tile> setTiles, Random random) {
        this.setTiles = setTiles;
        this.random = random;
    }

    public HeapTiles(Set<Tile> setTiles) {
        this.setTiles = new HashSet<>(setTiles);
        this.random = new Random();
    }

    public Tile DrawTile() {
        int index = random.nextInt(setTiles.size()); // Seleziona un indice casuale
        Iterator<Tile> iterator = setTiles.iterator();

        for (int i = 0; i < index; i++) {
            iterator.next(); // Itera fino alla tessera scelta
        }

        Tile drawnTile = iterator.next();
        setTiles.remove(drawnTile); // Rimuove la tessera estratta
        return drawnTile;
    }

    public Set<Tile> getVisibleTiles(){
        Set<Tile> visibleTiles = new HashSet<Tile>();
        for(Tile tile : setTiles){
            if(tile.isVisible()){
                visibleTiles.add(tile);

            }
        }
        return visibleTiles;
    }
    public void removeVisibleTile(Tile t){
        getVisibleTiles().remove(t);
    }
    public void addTile(Tile t){
        getVisibleTiles().add(t);
    }
}
