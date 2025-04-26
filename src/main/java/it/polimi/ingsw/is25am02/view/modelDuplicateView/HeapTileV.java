package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.util.Set;

public class HeapTileV {
    private Set<TileV> setTileV;

    public Set<TileV> getSetTileV() {
        return setTileV;
    }

    public void removeTile(TileV tileV){
        setTileV.remove(tileV);
    }
}
