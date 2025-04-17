package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.model.Dice;
import it.polimi.ingsw.is25am02.model.Player;

import java.util.HashMap;

public class GameboardV {
    private final HashMap<Player, Integer> positions;
    private final Dice dice;
    private int hourGlassFlip;


    public GameboardV(HashMap<Player, Integer> positions, Dice dice, int hourGlassFlip) {
        this.positions = positions;
        this.dice = dice;
        this.hourGlassFlip = hourGlassFlip;
    }
}
