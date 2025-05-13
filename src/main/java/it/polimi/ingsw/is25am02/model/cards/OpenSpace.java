package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class OpenSpace extends Card {
    private final CardType cardType;
    HashMap<Player,Integer> fly;

    public OpenSpace(int level, String imagepath, String comment,boolean testFlight) {
        super(level, StateCardType.CHOICE_ATTRIBUTES, imagepath,comment,testFlight);
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
                for (String nick:observers.keySet()) {
                    try {
                        Coordinate pos = new Coordinate (battery.x(),battery.y());
                        observers.get(nick).showBatteryRemoval(pos, player.getNickname(), player.getSpaceship().getSpaceshipIterator().getTile(battery.x(), battery.y()).get().getNumBattery());
                    } catch (RemoteException e) {
                        ServerController.logger.log(Level.SEVERE, "error in method removebattery in choice double motor", e);
                    }
                }
            }
        }
        fly.put(player,flyForward);
        game.getGameboard().move(flyForward, player);
        for (String nick:observers.keySet()) {
            try {
                observers.get(nick).showPositionUpdate(player.getNickname(),game.getGameboard().getPositions().get(player));
            } catch (RemoteException e) {
                ServerController.logger.log(Level.SEVERE, "error in method choicedoublemotor", e);
            }
        }
        game.nextPlayer();
    }

    public HashMap<Player,Integer> getFly(){
        return fly;
    }
}
