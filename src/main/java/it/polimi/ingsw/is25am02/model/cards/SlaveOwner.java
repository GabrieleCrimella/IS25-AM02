package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import it.polimi.ingsw.is25am02.model.tiles.Cabin;
import it.polimi.ingsw.is25am02.model.tiles.DoubleCannon;
import it.polimi.ingsw.is25am02.model.tiles.DoubleMotor;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class SlaveOwner extends Enemies{
    private int level;
    private int cannonPowers;
    private int daysLost;
    private int credit;
    private final int AliveLost;
    private int AliveRemoved;
    private StateCardType stateCardType;

    public SlaveOwner(int level, int cannonPowers, int daysLost, int credit, int humanLost) {
        super(level, cannonPowers, daysLost, credit);
        this.AliveRemoved = humanLost;
        this.AliveLost = humanLost;
        this.stateCardType = StateCardType.DECISION;
    }

    public SlaveOwner createCard(){
        //Here the code for reading on file the card's values
        return new SlaveOwner(level, cannonPowers, daysLost, credit, AliveLost);
    }

    void choiceDCannon(Player player, Game game, List<Pair<DoubleCannon, BatteryStorage>> whichDCannon){
        List<DoubleCannon> dCannon = new ArrayList<>();
        double playerPower;
        if(whichDCannon!=null){// il giocatore ha scelto di usare almeno un motore doppio
            for(Pair<DoubleCannon, BatteryStorage> pair: whichDCannon){
                dCannon.add(pair.getKey());
                pair.getValue().removeBattery();//rimuovo la batteria che è stata usata
            }
            playerPower= player.getSpaceship().calculateCannonPower(dCannon);
        }
        else playerPower = player.getSpaceship().calculateMotorPower(null); //se uso solo motori singoli

        if(playerPower>cannonPowers){ //se il giocatore è piu forte dei schiavisti allora li sconfigge
            player.getSpaceship().addCosmicCredits(credit);
            game.getGameboard().move((-1)*daysLost, player);

        }
        else if(playerPower==cannonPowers){//non succede niente al player current ma passo al player successivo
            game.nextPlayer();
        }
        else{//viene colpito il giocatore e si passa al prossimo
            stateCardType = StateCardType.REMOVE;
            game.nextPlayer();

        }
    }
    void removeCrew(Game game, Player player, Cabin cabin){
        try {
            cabin.remove(1);            //todo togliere il parametro dalla remove di cabin
            AliveRemoved++;
        } catch (IllegalStateException e) {  //todo qui sarà IllegalRemoveException()
            //gestisco eccezione non c'è equipaggio sulla cabin passata
        }

        if (AliveRemoved == AliveLost) {
            game.playNextCard();
        }
    }

}
