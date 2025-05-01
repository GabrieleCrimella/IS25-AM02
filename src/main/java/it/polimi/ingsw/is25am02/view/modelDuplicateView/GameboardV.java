package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.model.Dice;
import it.polimi.ingsw.is25am02.model.Player;

import java.util.HashMap;

public class GameboardV {
    private HashMap<PlayerV, Integer> positions;
    private final Dice dice;
    private int hourGlassFlip;


    public GameboardV(HashMap<PlayerV, Integer> positions, Dice dice, int hourGlassFlip) {
        this.positions = positions;
        this.dice = dice;
        this.hourGlassFlip = hourGlassFlip;
    }

    public void setPositions(HashMap<PlayerV, Integer> positions) {
        this.positions = positions;
    }

    public void setPosition(PlayerV playerV, int position){
        positions.put(playerV,position);
    }

    public HashMap<PlayerV, Integer> getPositions() {
        return positions;
    }
}
