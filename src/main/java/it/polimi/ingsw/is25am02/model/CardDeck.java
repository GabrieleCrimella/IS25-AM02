package it.polimi.ingsw.is25am02.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardDeck {
    private HashMap<Integer , Pair<List<Card>,Boolean>> deck; //l'intero è il numero di deck
    private List<Card> finalDeck;

    public CardDeck(){
        this.deck = new HashMap<>();
    }
    public List<Card> createFinalDeck(){
        finalDeck = new ArrayList<>();
        for(Pair<List<Card>,Boolean> cards : deck.values()){
            finalDeck.addAll(cards.getKey());
        }
        return finalDeck;
    }
    public Card giveCard(){
        return null;
    }
    public List<Card> giveDeck(int numDeck){//il deck viene occupato perchè è nelle mani di qualcun altro
        Pair<List<Card>, Boolean> pair = deck.get(numDeck);
        deck.put(numDeck, new Pair<>(pair.getKey(), true));
        return pair.getKey();
    }
    public void returnDeck(int numDeck){ //il deck
        Pair<List<Card>, Boolean> pair = deck.get(numDeck);
        deck.put(numDeck, new Pair<>(pair.getKey(), false));
    }
    public Card playnextCard(){
        return null; //todo

    }
}
