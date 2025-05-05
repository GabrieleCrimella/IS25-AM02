package it.polimi.ingsw.is25am02.view.modelDuplicateView;



import java.util.HashMap;

public class GameboardV {
    private int[] startingpositions;
    private HashMap<PlayerV, Integer> positions;
    private DiceV dice;
    private HourglassV hourGlass;


    public GameboardV(int[] startingpositions) {
        this.startingpositions = startingpositions;
        this.dice = new DiceV();
        this.hourGlass = new HourglassV();
        this.positions = new HashMap<>();
    }

    public void setPosition(PlayerV playerV, int position) {
        positions.put(playerV,position);
    }
}
