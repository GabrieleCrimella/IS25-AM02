package it.polimi.ingsw.is25am02.model;

public class Dice {
    public Dice() {
        super();
    }

    public int pickRandomNumber(){
        return (int)(Math.random() * 11) + 2;
    }
}
