package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

import java.util.Iterator;

public class Stardust extends Card {

    private final CardType cardType;

    public Stardust(int level) {
        super(level, StateCardType.DECISION);
        this.cardType = CardType.STARDUST;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void effect(Game game){
        Iterator<Player> it = game.getGameboard().getRanking().descendingIterator();
        while (it.hasNext()) {
            Player player = it.next();
            game.getGameboard().move((-1) * player.getSpaceship().calculateExposedConnectors(), player );
        }
        setStateCard(StateCardType.FINISH);
    }
}
