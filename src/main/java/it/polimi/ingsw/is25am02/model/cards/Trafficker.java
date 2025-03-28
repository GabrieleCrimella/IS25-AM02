package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.TAKE_CARD;

public class Trafficker extends Card {
    private final int cannonPowers;
    private final int daysLost;
    private final int boxesLost;
    private int boxesRemove;
    private final LinkedList<Box> boxesWon;
    private final LinkedList<BoxType> boxesWonTypes;
    private final CardType cardType;

    public Trafficker(int level, BoxStore store, int cannonPowers, int daysLost, int boxesLost, LinkedList<Box> boxesWon, LinkedList<BoxType> boxesWonTypes) {
        super(level, StateCardType.CHOICE_ATTRIBUTES);
        this.cannonPowers = cannonPowers;
        this.daysLost = daysLost;
        this.boxesLost = boxesLost;
        this.boxesWon = boxesWon;
        this.boxesWonTypes = boxesWonTypes;
        this.boxesRemove = 0;
        this.cardType = CardType.TRAFFICKER;
    }

    @Override
    public void addBoxWon(Box box){boxesWon.add(box);}

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void clearBoxWon(){
        boxesWon.clear();
    }

    @Override
    public void choiceDoubleCannon(Game game, Player player, Optional<List<Pair<Tile, Tile>>> choices) throws UnsupportedOperationException, IllegalRemoveException {
        //Calcolo potenza Player
        if(choices.isPresent()) {
            ArrayList<Tile> doubleCannons = new ArrayList<>();
            for (Pair<Tile, Tile> pair : choices.get()) {
                doubleCannons.add(pair.getKey());
                pair.getValue().removeBattery();
            }
            double playerPower = player.getSpaceship().calculateCannonPower(doubleCannons);
        }
        double playerPower = player.getSpaceship().calculateCannonPower(new ArrayList<>());

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
    public LinkedList<BoxType> getBoxesWonTypes() {
        return boxesWonTypes;
    }

    @Override
    public LinkedList<Box> getBoxesWon() {
        return boxesWon;
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
    public void removeBox(Game game, Player player, Tile storage, BoxType type) throws IllegalRemoveException {
        if(player.getSpaceship().isMostExpensive(type)){
            List<Box> boxes = storage.getOccupation();
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
