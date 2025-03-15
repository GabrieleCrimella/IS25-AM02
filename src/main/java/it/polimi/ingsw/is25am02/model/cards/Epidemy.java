package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.Optional;

public class Epidemy extends Card {

    public Epidemy(int level) {
        super(level, StateCardType.DECISION);
    }

    public Epidemy createCard(){
        return new Epidemy(getLevel());
    }

    @Override
    public void effect(Game game){
        for(Player p : game.getGameboard().getRanking()){
            p.getSpaceship().epidemyRemove();
        }
    }
}
