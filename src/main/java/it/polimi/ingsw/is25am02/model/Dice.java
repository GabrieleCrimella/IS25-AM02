package it.polimi.ingsw.is25am02.model;

public class Dice {
    private int result;
    public Dice() {
        super();
    }

    public int getResult() {
        return result;
    }

    public int pickRandomNumber(){
        result = (int)(Math.random() * 11) + 2;
        return result;
    }

    public void setManuallyResult(int res){
        result = res;
    }
}
