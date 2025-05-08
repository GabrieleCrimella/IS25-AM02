package it.polimi.ingsw.is25am02.view.modelDuplicateView;



import java.util.HashMap;

public class GameboardV {
    private int[] startingpositions;
    private HashMap<PlayerV, Integer> positions;
    private DiceV dice;
    private HourglassV hourGlass;
    private int numstep;


    public GameboardV(int[] startingpositions, int level) {
        if (level == 0||level==1){
            this.numstep = 18;
        } else if (level == 2)  this.numstep = 24;
        this.startingpositions = startingpositions;
        this.dice = new DiceV();
        this.hourGlass = new HourglassV();
        this.positions = new HashMap<>();
    }

    public void setPosition(PlayerV playerV, int position) {
        positions.put(playerV,position);
    }

    public int getPosition(PlayerV playerV) {
        return positions.get(playerV);
    }
}
