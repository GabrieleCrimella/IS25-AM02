package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.Spaceship;

public class Epidemy extends Card {
    private int level;

    public Epidemy(int level) {
        super(level);
    }

    public Epidemy createCard(){
        return new Epidemy(level);
    }

    public void effect(Gameboard gb){
        for(Player i : gb.getRanking()){
            Spaceship s = i.getSpaceship();
            for(int x =0; x<11; x++){
                for(int y =0; y<11; y++){
                    //work in progress
                }
            }
        }
    }
}
