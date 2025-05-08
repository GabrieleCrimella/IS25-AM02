package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.util.ArrayList;
import java.util.List;

public class HeapTileV {
    private List<TileV> listTileV;

    public HeapTileV() {
        this.listTileV = new ArrayList<>();
    }

    public List<TileV> getListTileV() {
        return listTileV;
    }

    public void addToHeapTile(TileV tileV){
        listTileV.add(tileV);
    }

    public void removeFromHeapTile(TileV tileV){
        listTileV.remove(tileV);
    }

    public void removeTile(TileV tileV){
        listTileV.remove(tileV);
    }
}
