package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.SpecialStorage;

import java.util.ArrayList;
import java.util.List;

public class Planet extends Card_with_box{
    private int level;
    private BoxStore store;
    private final int daysLost;
    private final ArrayList<ArrayList<Box>> planetOffers;
    private ArrayList<Integer> occupied; //tiene conto di quali pianeti sono occupati

    public Planet(int level, BoxStore store, int daysLost, ArrayList<ArrayList<Box>> planetOffers) {
        super(level, store, StateCardType.DECISION);
        this.daysLost = daysLost;
        this.planetOffers = planetOffers;
        this.occupied = new ArrayList<>();

        for(ArrayList<Box> boxes : planetOffers) {
            occupied.add(0);
        }
    }

    public Planet createCard(){
        //Here the code for reading on file the card's values
        return new Planet(level, store, daysLost, planetOffers);
    }

    List<Box> choicePlanet(Game game, Player p, int index) throws Exception {
        if(index == -1){   //Il player ha deciso di non atterrare
            game.nextPlayer();
            return null;
        }
        else if(index>=0 && index <= planetOffers.size() && occupied.get(index) == 0) {
            occupied.set(index, 1);
            setStateCard(StateCardType.BOXMANAGEMENT);
            return planetOffers.get(index);
        }
        throw new Exception(); //todo creare exception per pianeta occupato
    }

}
