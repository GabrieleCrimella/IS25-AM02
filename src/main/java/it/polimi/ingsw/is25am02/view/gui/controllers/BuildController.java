package it.polimi.ingsw.is25am02.view.gui.controllers;

import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BuildController {
    @FXML
    private ImageView backgroundImageView;

    private int level;

    public void setLevel(int level) {
        this.level = level;
    }

    @FXML
    public void initialize(int level, PlayerColor color) {

        String imagePath;
        switch (level) {
            case 0:
                imagePath = "/image/cardboard/cardboard-1.jpg";
                break;
            case 2:
                imagePath = "/image/cardboard-1b.jpg";
                break;
            default:
                imagePath = "/image/background.jpg"; // fallback
        }

        backgroundImageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
    }

}
