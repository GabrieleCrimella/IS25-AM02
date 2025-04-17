package it.polimi.ingsw.is25am02.view.modelDuplicateView.tile;

import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;

public class TileV {
    private final TileType tType;
    //private final String imagePath;
    private RotationType rotationType;
    private boolean visible;
    private final int id;
    private int numBattery;
    private int numHumans;
    private int numPAliens;
    private int numBAliens;
    private int numRedBox;
    private int numYellowBox;
    private int numGreenBox;
    private int numBlueBox;

    public TileV(TileType tType, RotationType rotationType, boolean visible, int id, int numBattery, int numHumans, int numPAliens, int numBAliens, int numRedBox, int numYellowBox, int numGreenBox, int numBlueBox) {
        this.tType = tType;
        this.rotationType = rotationType;
        this.visible = visible;
        this.id = id;
        this.numBattery = numBattery;
        this.numHumans = numHumans;
        this.numPAliens = numPAliens;
        this.numBAliens = numBAliens;
        this.numRedBox = numRedBox;
        this.numYellowBox = numYellowBox;
        this.numGreenBox = numGreenBox;
        this.numBlueBox = numBlueBox;
    }
}
