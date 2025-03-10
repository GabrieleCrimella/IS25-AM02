package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.cards.Boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.Boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.tiles.SpecialStorage;

import java.util.ArrayList;

public class Planet extends Card_with_box{
    private int level;
    private BoxStore store;
    private final int daysLost;
    private final ArrayList<ArrayList<Box>> planetOffers;

    public Planet(int level, BoxStore store, int daysLost, ArrayList<ArrayList<Box>> planetOffers) {
        super(level, store);
        this.daysLost = daysLost;
        this.planetOffers = planetOffers;
    }

    public Planet createCard(){
        //Here the code for reading on file the card's values
        return new Planet(level, store, daysLost, planetOffers);
    }
//ogni giocatore può decidere se atterrare o meno su un pianeta, quando sta su un pianeta può decidere quante merci prendere e dove metterle
    public void effect(Gameboard gb){
        for (Player i : gb.getRanking()){
            gb.move(-daysLost,i); //perdo dei giorni (sono negativi perchè vanno tolti)
            for(SpecialStorage storageTile : i.getSpaceship().getStorageTiles()) { //ciclo sulle caselle e devo dire dove aggiungere le cose
                for (ArrayList<Box> boxList : planetOffers) {
                    for (Box box : boxList) {
                        storageTile.addBox(box);
                    }
                }
            }
        }
    }
}
