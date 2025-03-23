package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import it.polimi.ingsw.is25am02.model.tiles.DoubleMotor;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OpenSpace extends Card {

    private CardType cardType;

    public OpenSpace(int level) {
        super(level, StateCardType.CHOICE_ATTRIBUTES);
        this.cardType = CardType.OPENSPACE;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void choiceDoubleMotor( Game game, Player player, Optional<List<Pair<Tile, Tile>>> whichDMotor){ //primo tile è double motor il secondo è battery storage
        List<Tile> dMotors = new ArrayList<>();
        int flyForward;
        if(whichDMotor.isPresent()){  // il giocatore ha scelto di usare almeno un motore doppio
            for(Pair<Tile, Tile> pair: whichDMotor.get()){
                dMotors.add(pair.getKey());
                pair.getValue().removeBattery();//rimuovo la batteria che è stata usata
            }
            flyForward= player.getSpaceship().calculateMotorPower(dMotors);
        }
        else flyForward = player.getSpaceship().calculateMotorPower(new ArrayList<Tile>()); //se uso solo motori singoli

        game.getGameboard().move(flyForward, player);
        game.nextPlayer();
    }
}
