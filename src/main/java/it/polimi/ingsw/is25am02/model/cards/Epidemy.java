package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;


public class Epidemy extends Card {

    private final CardType cardType;

    public Epidemy(int level, String imagepath) {
        super(level, StateCardType.DECISION, imagepath);
        this.cardType = CardType.EPIDEMY;
    }

    @Override
    public CardType getCardType(){
        return cardType;
    }

    @Override
    public void effect(Game game){
        for(Player p : game.getGameboard().getRanking()){
            try {
                p.getSpaceship().epidemyRemove();
            } catch (IllegalRemoveException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
