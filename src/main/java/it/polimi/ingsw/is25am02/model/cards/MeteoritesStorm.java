package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;

import java.util.ArrayList;

public class MeteoritesStorm extends Card {
    private int level;
    private ArrayList<ArrayList<Integer>> meteorites;

    public MeteoritesStorm(int level, ArrayList<ArrayList<Integer>> meteorites) {
        super(level);
        this.meteorites = meteorites;
    }

    public MeteoritesStorm createCard(){
        //Here the code for reading on file the card's values
        return new MeteoritesStorm(level, meteorites);
    }

    public void effect(Gameboard gb){}
}
