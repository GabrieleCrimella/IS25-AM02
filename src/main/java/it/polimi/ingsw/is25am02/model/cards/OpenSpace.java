package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import it.polimi.ingsw.is25am02.model.tiles.DoubleMotor;
import javafx.util.Pair;

import java.util.List;

public class OpenSpace extends Card {
    int level;
    StateCardType stateCardType;

    public OpenSpace(int level) {
        super(level);
    }

    public OpenSpace createCard(){
        return new OpenSpace(level);
    }

    void choiceDMotor(Player p, List<Pair<DoubleMotor, BatteryStorage>> whichDMotor){

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
