package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;

public abstract class Enemies extends Card {
    private final int cannonPowers;
    private final int daysLost;
    private final int credit;

    public Enemies(int level, int cannonPowers, int daysLost, int credit, StateCardType stateCard, String imagepath, String comment) {
        super(level, stateCard, imagepath,comment);
        this.cannonPowers = cannonPowers;
        this.daysLost = daysLost;
        this.credit = credit;
    }

    public int getCannonPowers() {
        return cannonPowers;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public int getCredit() {
        return credit;
    }
}
