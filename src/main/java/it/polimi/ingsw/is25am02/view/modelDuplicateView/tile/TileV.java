package it.polimi.ingsw.is25am02.view.modelDuplicateView.tile;

import it.polimi.ingsw.is25am02.utils.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;

public class TileV {
    private final TileType tType;
    //private final String imagePath;
    private RotationType rotationType;
    private ConnectorType[] connectors;
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
    private int numMaxBattery;
    private int numMaxBox;



    public TileV(TileType tType, ConnectorType[] connectors, RotationType rotationType, boolean visible, String imagePath, int numMaxBattery, int numMaxBox) {
        this.tType = tType;
        this.connectors = connectors;
        this.rotationType = rotationType;
        this.visible = visible;
        this.imagePath = imagePath;
        this.numBattery = 0;
        this.numHumans = 0;
        this.numPAliens = 0;
        this.numBAliens = 0;
        this.numRedBox = 0;
        this.numYellowBox = 0;
        this.numGreenBox = 0;
        this.numBlueBox = 0;
        this.numMaxBattery = numMaxBattery;
        this.numMaxBox = numMaxBox;
    }

    public TileType getType() {
        return tType;
    }

    public RotationType getRotationType() {
        return rotationType;
    }

    public ConnectorType[] getConnectors() {
        return connectors;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getNumBattery() {
        return numBattery;
    }

    public int getNumHumans() {
        return numHumans;
    }

    public int getNumPAliens() {
        return numPAliens;
    }

    public int getNumBAliens() {
        return numBAliens;
    }

    public int getNumRedBox() {
        return numRedBox;
    }

    public int getNumYellowBox() {
        return numYellowBox;
    }

    public int getNumGreenBox() {
        return numGreenBox;
    }

    public int getNumBlueBox() {
        return numBlueBox;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setRotationType(RotationType rotationType) {
        this.rotationType = rotationType;
    }

    public void setConnectors(ConnectorType[] connectors) {
        this.connectors = connectors;
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
