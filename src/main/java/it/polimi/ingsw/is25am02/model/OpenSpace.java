package it.polimi.ingsw.is25am02.model;

public class OpenSpace extends Card{

    private int level;
    public OpenSpace(int level){
        super(level);

    }
    public OpenSpace createCard(){//use costructor
        return new OpenSpace(level);


    }
    public void effect(Gameboard gb){

        int motorPower;
        for (Player player : gb.getRanking()){
            motorPower = player.getSpaceship().calculateMotorPower();
            gb.move(motorPower, player);
        }

    }

}
