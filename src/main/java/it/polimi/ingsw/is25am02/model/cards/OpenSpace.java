package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
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

    public void effect(Game game, Gameboard gb, Player player) {
        //bisgona capire bene il metodo calculateMotorPower e in base a quello aggiustare questo.
        game.getGameboard().move(game.getCurrentPlayer().getSpaceship().calculateMotorPower(), game.getCurrentPlayer());
        game.nextPlayer();

        /*Anti-pattern
            for (Player i : gb.getRanking()){
            gb.move(i.getSpaceship().calculateMotorPower(), i);
        }
         */
    }

}
