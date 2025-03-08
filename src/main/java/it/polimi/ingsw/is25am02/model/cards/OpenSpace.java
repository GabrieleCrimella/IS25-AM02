package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;

public class OpenSpace extends Card {
    int level;

    public OpenSpace(int level) {
        super(level);
    }

    public OpenSpace createCard(){
        return new OpenSpace(level);
    }

    public void effect(Gameboard gb){
        for (Player i : gb.getRanking()){
            gb.move(i.getSpaceship().calculateMotorPower(), i);
        }
    }
}
