package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;

public abstract class Card_with_box extends Card {
    private BoxStore store;

    public Card_with_box(int level, BoxStore store){
        super(level);
        this.store = store;
    }

    public BoxStore getBoxStore() {
        return store;
    }
}
