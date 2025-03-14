package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.enumerations.AliveType;

public class Alive {
    private final AliveType race;

    public Alive(AliveType race) {
        this.race = race;
    }

    public AliveType getRace() {
        return race;
    }
}
