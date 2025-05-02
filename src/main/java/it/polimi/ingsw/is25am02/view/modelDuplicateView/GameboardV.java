package it.polimi.ingsw.is25am02.view.modelDuplicateView;



import java.util.HashMap;

public class GameboardV {
    private HashMap<Integer, PlayerV> positions;
    private DiceV dice;
    private HourglassV hourGlass;


    public GameboardV(HashMap<Integer, PlayerV> positions) {
        this.positions = positions;
        this.dice = new DiceV();
        this.hourGlass = new HourglassV();
    }


    public void setPosition(PlayerV playerV, int position){
        positions.put(position,playerV);
    }

    public HashMap<Integer, PlayerV> getPositions() {
        return positions;
    }
}
