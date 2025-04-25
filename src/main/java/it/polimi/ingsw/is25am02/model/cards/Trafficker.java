package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.Tile;

import java.util.*;

import static it.polimi.ingsw.is25am02.utils.enumerations.StateGameType.TAKE_CARD;

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
    public void choiceDoubleCannon(Game game, Player player, List<Coordinate> cannons, List<Coordinate> batteries) throws UnsupportedOperationException, IllegalRemoveException {
        //Calculate Player Power
        List<Tile> dCannon = new ArrayList<>();
        for(Coordinate cannon : cannons) {
            dCannon.add(player.getSpaceship().getTile(cannon.x(), cannon.y()).get());
        }
        double playerPower = player.getSpaceship().calculateCannonPower(dCannon);
        for(Coordinate battery : batteries) {
            player.getSpaceship().getTile(battery.x(), battery.y()).get().removeBattery();
        }

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
    public void choiceBox(Game game, Player player, boolean choice){
        if(choice){
            setStateCard(StateCardType.BOXMANAGEMENT);
            game.getGameboard().move((-1)*daysLost, player);
        }
        else {
            game.getCurrentCard().setStateCard(StateCardType.FINISH);
            game.getCurrentState().setPhase(TAKE_CARD);
        }
    }

    @Override
    public void moveBox(Game game, Player player, List<Box> start, List<Box> end, BoxType boxType, boolean on){
        if(on) {
            Iterator<Box> it = start.iterator();
            while (it.hasNext()) {
                Box box = it.next();
                if (box.getType().equals(boxType)) {
                    it.remove();
                    end.add(box);
                    break;
                }
            }
        }
        else {
            game.getCurrentCard().setStateCard(StateCardType.FINISH);
            game.getCurrentState().setPhase(TAKE_CARD);
        }
    }

    //se non ho abbastanza box allora tolgo le batterie
    @Override
    public void removeBox(Game game, Player player, Tile storage, BoxType type) throws IllegalRemoveException {
        if(player.getSpaceship().isMostExpensive(type) && !player.getSpaceship().noBox() ){
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

    @Override
    public void removeBattery(Game game, Player player, Tile storage) throws IllegalRemoveException {
        if(player.getSpaceship().noBox()){
            storage.removeBattery();
            boxesRemove++;

            if (boxesRemove == boxesLost) {
                setStateCard(StateCardType.CHOICE_ATTRIBUTES);
                game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
            }
        }
        else throw new IllegalStateException();
    }
}
