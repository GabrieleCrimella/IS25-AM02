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
import java.util.Optional;

import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.TAKE_CARD;

public class SlaveOwner extends Enemies{
    private final int AliveLost;
    private int AliveRemoved;

    public SlaveOwner(int level, int cannonPowers, int daysLost, int credit, int humanLost) {
        super(level, cannonPowers, daysLost, credit, StateCardType.DECISION);
        this.AliveLost = humanLost;
        this.AliveRemoved = 0;
    }

    public SlaveOwner createCard(){
        //Here the code for reading on file the card's values
        return new SlaveOwner(getLevel(), getCannonPowers(), getDaysLost(), getCredit(), AliveLost);
    }

    @Override
    public void choiceDoubleCannon(Game game, Player player, Optional<List<Pair<DoubleCannon, BatteryStorage>>> whichDCannon){
        List<DoubleCannon> dCannon = new ArrayList<>();
        double playerPower;
        if(whichDCannon.isPresent()){  // il giocatore ha scelto di usare almeno un motore doppio
            for(Pair<DoubleCannon, BatteryStorage> pair: whichDCannon.get()){
                dCannon.add(pair.getKey());
                pair.getValue().removeBattery();  //rimuovo la batteria che è stata usata
            }
            playerPower= player.getSpaceship().calculateCannonPower(dCannon);
        }
        else playerPower = player.getSpaceship().calculateCannonPower(new ArrayList<DoubleCannon>()); //se uso solo motori singoli

        if(playerPower > getCannonPowers()){ //se il giocatore è piu forte dei schiavisti allora li sconfigge
            setStateCard(StateCardType.DECISION);
        }
        else if(playerPower == getCannonPowers()){ //non succede niente al player current ma passo al player successivo
            game.nextPlayer();
        }
        else{  //viene colpito il giocatore
            setStateCard(StateCardType.REMOVE);
        }
    }

    @Override
    public void choice(Game game, Player player, boolean choice){
        if(choice){
            player.getSpaceship().addCosmicCredits(getCredit());
            game.getGameboard().move((-1)*getDaysLost(), player);
        }
        setStateCard(StateCardType.FINISH);
        game.getCurrentState().setPhase(TAKE_CARD);
    }

    @Override
    public void removeCrew(Game game, Player player, Cabin cabin){
        try {
            cabin.remove(1);            //todo togliere il parametro dalla remove di cabin
            AliveRemoved++;
        } catch (IllegalStateException e) {  //todo qui sarà IllegalRemoveException()
            //gestisco eccezione non c'è equipaggio sulla cabin passata
        }

        if (AliveRemoved == AliveLost) {
            if(player.equals(game.getGameboard().getRanking().getLast())){
                game.getCurrentCard().setStateCard(StateCardType.FINISH);
                game.getCurrentState().setPhase(TAKE_CARD);
            }
            else{
                game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
                game.nextPlayer();
            }
        }
    }

}
