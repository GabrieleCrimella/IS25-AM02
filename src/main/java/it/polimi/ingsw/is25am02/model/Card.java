package it.polimi.ingsw.is25am02.model;

public abstract class Card {
    private final int level;

    public Card(int level) {
        this.level = level;
    }

    public Card newCard() {
        return createCard();
    }

    public abstract Card createCard();
    public abstract void effect(Gameboard gb);
}
