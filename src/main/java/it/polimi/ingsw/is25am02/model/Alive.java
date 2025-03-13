package it.polimi.ingsw.is25am02.model;

public class Alive {
    private AliveType race;

    public Alive(AliveType race) {
        this.race = race;
    }

    public AliveType getRace() {
        return race;
    }
}
