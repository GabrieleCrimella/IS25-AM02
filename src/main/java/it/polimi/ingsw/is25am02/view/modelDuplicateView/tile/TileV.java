package it.polimi.ingsw.is25am02.view.modelDuplicateView.tile;

import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;

public class TileV {
    private final TileType tType;
    //private final String imagePath;
    private RotationType rotationType;
    private boolean visible;
    private final String imagePath;
    private int numBattery;
    private int numHumans;
    private int numPAliens;
    private int numBAliens;
    private int numRedBox;
    private int numYellowBox;
    private int numGreenBox;
    private int numBlueBox;

    public TileType getType() {
        return tType;
    }

    public TileV(TileType tType, RotationType rotationType, boolean visible, String imagePath, int numBattery, int numHumans, int numPAliens, int numBAliens, int numRedBox, int numYellowBox, int numGreenBox, int numBlueBox) {
        this.tType = tType;
        this.rotationType = rotationType;
        this.visible = visible;
        this.imagePath = imagePath;
        this.numBattery = numBattery;
        this.numHumans = numHumans;
        this.numPAliens = numPAliens;
        this.numBAliens = numBAliens;
        this.numRedBox = numRedBox;
        this.numYellowBox = numYellowBox;
        this.numGreenBox = numGreenBox;
        this.numBlueBox = numBlueBox;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setRotationType(RotationType rotationType) {
        this.rotationType = rotationType;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setNumBattery(int numBattery) {
        this.numBattery = numBattery;
    }

    public void setNumHumans(int numHumans) {
        this.numHumans = numHumans;
    }

    public void setNumPAliens(int numPAliens) {
        this.numPAliens = numPAliens;
    }

    public void setNumBAliens(int numBAliens) {
        this.numBAliens = numBAliens;
    }

    public void setNumRedBox(int numRedBox) {
        this.numRedBox = numRedBox;
    }

    public void setNumYellowBox(int numYellowBox) {
        this.numYellowBox = numYellowBox;
    }

    public void setNumGreenBox(int numGreenBox) {
        this.numGreenBox = numGreenBox;
    }

    public void setNumBlueBox(int numBlueBox) {
        this.numBlueBox = numBlueBox;
    }
}
