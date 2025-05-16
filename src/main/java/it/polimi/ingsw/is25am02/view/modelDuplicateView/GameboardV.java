package it.polimi.ingsw.is25am02.view.modelDuplicateView;



import java.util.ArrayList;
import java.util.HashMap;

public class GameboardV {
    private int[] startingpositions;
    private HashMap<PlayerV, Integer> positions;
    private DiceV dice;
    private HourglassV hourGlass;
    private int numstep;

    public HashMap<PlayerV, Integer> getPositions() {
        return positions;
    }

    public GameboardV(int[] startingpositions, int level, ArrayList<PlayerV> players) {
        if (level == 0||level==1){
            this.numstep = 18;
        } else if (level == 2)  this.numstep = 24;
        this.startingpositions = startingpositions;
        this.dice = new DiceV();
        this.hourGlass = new HourglassV();
        this.positions = new HashMap<>();
        for(PlayerV player : players){
            positions.put(player, -1);
        }
    }

    public void setPosition(PlayerV playerV, int position) {
        positions.put(playerV,position);
    }

    public int getPosition(PlayerV playerV) {
        return positions.get(playerV);
    }

    public int getNumstep() {
        return numstep;
    }
}
