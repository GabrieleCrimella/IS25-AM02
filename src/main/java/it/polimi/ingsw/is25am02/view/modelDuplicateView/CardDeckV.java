package it.polimi.ingsw.is25am02.view.modelDuplicateView;


import java.util.HashMap;
import java.util.List;

public class CardDeckV {
    private HashMap<Integer , List<CardV>> deck;

    public CardDeckV(HashMap<Integer , List<CardV>> deck) {
        this.deck = deck;
    }
}
