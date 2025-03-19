package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import it.polimi.ingsw.is25am02.model.tiles.DoubleCannon;
import it.polimi.ingsw.is25am02.model.tiles.SpecialStorage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.TAKE_CARD;

public class Trafficker extends Card_with_box{
    private final int cannonPowers;
    private final int daysLost;
    private final int boxesLost;
    private int boxesRemove;
    private final LinkedList<Box> boxesWon;

    public Trafficker(int level, BoxStore store, int cannonPowers, int daysLost, int boxesLost, LinkedList boxesWon) {
        super(level, store, StateCardType.CHOICE_ATTRIBUTES);
        this.cannonPowers = cannonPowers;
        this.daysLost = daysLost;
        this.boxesLost = boxesLost;
        this.boxesWon = boxesWon;
        this.boxesRemove = 0;
    }

    @Override
    public void choiceDoubleCannon(Game game, Player player, Optional<List<Pair<DoubleCannon, BatteryStorage>>> choices) throws UnsupportedOperationException {
        //Calcolo potenza Player
        if(choices.isPresent()) {
            ArrayList<DoubleCannon> doubleCannons = new ArrayList<>();
            for (Pair<DoubleCannon, BatteryStorage> pair : choices.get()) {
                doubleCannons.add(pair.getKey());
                pair.getValue().removeBattery();
            }
            double playerPower = player.getSpaceship().calculateCannonPower(doubleCannons);
        }
        double playerPower = player.getSpaceship().calculateCannonPower(new ArrayList<DoubleCannon>());

        //Paragoni
        if(playerPower > cannonPowers){
            setStateCard(StateCardType.DECISION);
        }
        else if(playerPower == cannonPowers){
            game.nextPlayer();
        }
        else{
            setStateCard(StateCardType.REMOVE);
        }

    }

    @Override
    public List<Box> choiceBox(Game game, Player player, boolean choice){
        if(choice){
            setStateCard(StateCardType.BOXMANAGEMENT);
            game.getGameboard().move((-1)*daysLost, player);
            return boxesWon;
        }
        else {
            game.getCurrentCard().setStateCard(StateCardType.FINISH);
            game.getCurrentState().setPhase(TAKE_CARD);
            return null;
        }
    }

    @Override
    public void moveBox(Game game, Player player, List<Box> start, List<Box> end, Box box, boolean on){
        if(on) {
            start.remove(box);
            end.add(box);
        }
        else {
            game.getCurrentCard().setStateCard(StateCardType.FINISH);
            game.getCurrentState().setPhase(TAKE_CARD);
        }
    }

    @Override
    public void removeBox(Game game, Player player, SpecialStorage storage, BoxType type){
        if(player.getSpaceship().isMostExpensive(type)){
            ArrayList<Box> boxes = storage.getOccupation();
            for(Box box : boxes){
                if(box.getType() == type){
                    storage.removeBox(box);
                    boxesRemove++;
                    break;
                }
            }

            if(boxesRemove == boxesLost){
                setStateCard(StateCardType.CHOICE_ATTRIBUTES);
                game.nextPlayer();
            }
        }
        else throw new RuntimeException();
    }
}
