package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.StateCardType;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import it.polimi.ingsw.is25am02.model.tiles.DoubleCannon;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Trafficker extends Card_with_box{
    private int level;
    private BoxStore store;
    private final int cannonPowers;
    private final int daysLost;
    private final int boxesLost;
    private ArrayList<Box> boxesWon;
    private StateCardType stateCardType;
    private ArrayList<Integer> playerPower; //mi salvo la potenza dei giocatori

    public Trafficker(int level, BoxStore store, int cannonPowers, int daysLost, int boxesLost, ArrayList<Box> boxesWon) {
        super(level, store);
        this.cannonPowers = cannonPowers;
        this.daysLost = daysLost;
        this.boxesLost = boxesLost;
        this.boxesWon = boxesWon;
    }

    public Trafficker createCard(){
        //Here the code for reading on file the card's values
        return new Trafficker(level, store, cannonPowers, daysLost, boxesLost, boxesWon);
    }

    void choiceDCannon(Player p, List<Pair<DoubleCannon, BatteryStorage>> whichDCannon){

    }

    public void effect(Gameboard gb){

    }
}
