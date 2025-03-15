package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

import java.util.Iterator;
import java.util.ListIterator;

public class Stardust extends Card {

    public Stardust(int level) {
        super(level, StateCardType.DECISION);
    }

    public Stardust createCard(){
        return new Stardust(getLevel());
    }

    @Override
    public void effect(Game game) {
        Iterator<Player> it = game.getGameboard().getRanking().descendingIterator();
        while (it.hasNext()) {
            Player player = it.next();
            game.getGameboard().move((-1) * player.getSpaceship().calculateExposedConnectors(), it.next());
        }
        setStateCard(StateCardType.FINISH);
    }
}
