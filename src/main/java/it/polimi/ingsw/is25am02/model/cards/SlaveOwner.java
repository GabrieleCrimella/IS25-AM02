package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.TAKE_CARD;

public class SlaveOwner extends Enemies{
    private final int AliveLost;
    private int AliveRemoved;
    private final CardType cardType;

    public SlaveOwner(int level, int cannonPowers, int daysLost, int credit, int humanLost) {
        super(level, cannonPowers, daysLost, credit, StateCardType.DECISION);
        this.AliveLost = humanLost;
        this.AliveRemoved = 0;
        this.cardType = CardType.SLAVE_OWNER;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void choiceDoubleCannon(Game game, Player player, Optional<List<Pair<Tile, Tile>>> whichDCannon) throws IllegalRemoveException {
        List<Tile> dCannon = new ArrayList<>();
        double playerPower;
        if(whichDCannon.isPresent()){  // il giocatore ha scelto di usare almeno un motore doppio
            for(Pair<Tile, Tile> pair: whichDCannon.get()){
                dCannon.add(pair.getKey());
                pair.getValue().removeBattery();  //rimuovo la batteria che è stata usata
            }
            playerPower= player.getSpaceship().calculateCannonPower(dCannon);
        }
        else playerPower = player.getSpaceship().calculateCannonPower(new ArrayList<>()); //se uso solo motori singoli

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
    public void removeCrew(Game game, Player player, Tile cabin) throws IllegalRemoveException {
        cabin.removeCrew();
        AliveRemoved++;

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
