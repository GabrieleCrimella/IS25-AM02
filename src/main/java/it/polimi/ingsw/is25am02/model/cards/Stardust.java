package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

import java.util.ListIterator;

public class Stardust extends Card {
    private int level;

    public Stardust(int level) {

        super(level);
    }

    public Stardust createCard(){
        return new Stardust(level);
    }

    public void effect(Game game, Player player){

        game.getGameboard().move((-1)*player.getSpaceship().calculateExposedConnectors(), player);
        game.previousPlayer();

        }

}
