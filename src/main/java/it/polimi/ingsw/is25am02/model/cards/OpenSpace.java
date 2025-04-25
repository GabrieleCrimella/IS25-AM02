package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenSpace extends Card {
    private final CardType cardType;
    HashMap<Player,Integer> fly;

    public OpenSpace(int level) {
        super(level, StateCardType.CHOICE_ATTRIBUTES);
        this.cardType = CardType.OPENSPACE;
        this.fly = new HashMap<>();
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void choiceDoubleMotor(Game game, Player player, List<Coordinate> motors, List<Coordinate> batteries) throws IllegalRemoveException {
        List<Tile> dMotors = new ArrayList<>();
        int flyForward;
        if(!motors.isEmpty()){
            for(Coordinate motor : motors) {
                dMotors.add(player.getSpaceship().getTile(motor.x(), motor.y()).get());
            }
        }
        flyForward= player.getSpaceship().calculateMotorPower(dMotors);
        if(!batteries.isEmpty()){
            for(Coordinate battery : batteries) {
                player.getSpaceship().getTile(battery.x(), battery.y()).get().removeBattery();
            }
        }
        fly.put(player,flyForward);
        game.getGameboard().move(flyForward, player);
        game.nextPlayer();
    }

    public HashMap<Player,Integer> getFly(){
        return fly;
    }
}
