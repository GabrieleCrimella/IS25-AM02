package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

public abstract class Card_with_box extends Card {
    private final BoxStore store;

    public Card_with_box(int level, BoxStore store, StateCardType stateCard) {
        super(level,stateCard);
        this.store = store;
    }

    public BoxStore getBoxStore() {
        return store;
    }
}
