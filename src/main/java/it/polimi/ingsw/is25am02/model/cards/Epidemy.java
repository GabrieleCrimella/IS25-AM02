package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;

public class Epidemy extends Card {
    private int level;

    public Epidemy(int level) {
        super(level);
    }

    public Epidemy createCard(){
        return new Epidemy(level);
    }

    public void effect(Game game){
        for(Player i : game.getGameboard().getRanking()){
            Spaceship s = i.getSpaceship();
            for(int x =0; x<11; x++){
                for(int y =0; y<11; y++){
                    //work in progress
                }
            }
        }
    }
}
