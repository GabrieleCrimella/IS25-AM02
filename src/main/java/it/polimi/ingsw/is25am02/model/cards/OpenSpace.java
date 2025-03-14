package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import it.polimi.ingsw.is25am02.model.tiles.DoubleMotor;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class OpenSpace extends Card {
    int level;
    StateCardType stateCardType;

    public OpenSpace(int level, StateCardType stateCardType) {

        super(level, stateCardType);
        this.stateCardType=StateCardType.CHOICE_ATTRIBUTES;
    }

    public OpenSpace createCard(){
        return new OpenSpace(level,stateCardType);
    }

    void choiceDMotor(Player player, Game game, List<Pair<DoubleMotor, BatteryStorage>> whichDMotor){
        List<DoubleMotor> dMotors = new ArrayList<>();
        int flyBack;
        if(whichDMotor!=null){// il giocatore ha scelto di usare almeno un motore doppio
            for(Pair<DoubleMotor, BatteryStorage> pair: whichDMotor){
                dMotors.add(pair.getKey());
                pair.getValue().removeBattery();//rimuovo la batteria che è stata usata
            }
            flyBack= player.getSpaceship().calculateMotorPower(dMotors);
        }
        else flyBack = player.getSpaceship().calculateMotorPower(null); //se uso solo motori singoli

        game.getGameboard().move(flyBack,player);
        game.nextPlayer();

    }


}
