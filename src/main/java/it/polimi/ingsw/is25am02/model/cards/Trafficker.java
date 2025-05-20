package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
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
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;

import static it.polimi.ingsw.is25am02.utils.enumerations.StateGameType.TAKE_CARD;

public class Trafficker extends Card {
    private final int cannonPowers;
    private final int daysLost;
    private final int boxesLost;
    private int boxesRemove;
    private final LinkedList<Box> boxesWon;
    private final LinkedList<BoxType> boxesWonTypes;
    private final CardType cardType;

    public Trafficker(int level, BoxStore store, int cannonPowers, int daysLost, int boxesLost, LinkedList<Box> boxesWon, LinkedList<BoxType> boxesWonTypes, String imagepath,String comment,boolean testFlight) {
        super(level, StateCardType.CHOICE_ATTRIBUTES, imagepath,comment,testFlight);
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
            for (String nick:observers.keySet()) {
                try {
                    Coordinate pos = new Coordinate (battery.x(),battery.y());
                    observers.get(nick).showBatteryRemoval(pos, player.getNickname(), player.getSpaceship().getSpaceshipIterator().getTile(battery.x(), battery.y()).get().getNumBattery());
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method choicedoublecannon", e);
                }
            }
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
            for (String nick:observers.keySet()) {
                try {
                    observers.get(nick).showPositionUpdate(player.getNickname(), game.getGameboard().getPositions().get(player));
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method choicebox", e);
                }
            }
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

    @Override
    public void removeBox(Game game, Player player, Tile storage, BoxType type) throws IllegalRemoveException {
        if(player.getSpaceship().isMostExpensive(type) && !player.getSpaceship().noBox() ){
            List<Box> boxes = storage.getOccupation();
            for(Box box : boxes){
                if(box.getType() == type){
                    storage.removeBox(box);
                    for (String nick:observers.keySet()) {
                        Coordinate pos = new Coordinate (player.getSpaceship().getSpaceshipIterator().getX(storage), player.getSpaceship().getSpaceshipIterator().getY(storage));
                        try {
                            observers.get(nick).showBoxUpdate(pos,player.getNickname(), player.getSpaceship().getTile(pos.x(), pos.y()).get().getOccupationTypes());
                        } catch (RemoteException e) {
                            ServerController.logger.log(Level.SEVERE, "error in method removebox", e);
                        }
                    }
                    boxesRemove++;
                    break;
                }
            }

            if(boxesRemove == boxesLost){
                setStateCard(StateCardType.CHOICE_ATTRIBUTES);
                game.nextPlayer();
                return;
            }

            if(noMore(player)){
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
            for (String nick:observers.keySet()) {
                try {
                    Coordinate pos = new Coordinate (player.getSpaceship().getSpaceshipIterator().getX(storage), player.getSpaceship().getSpaceshipIterator().getY(storage));
                    observers.get(nick).showBatteryRemoval(pos, player.getNickname(), player.getSpaceship().getSpaceshipIterator().getTile(pos.x(), pos.y()).get().getNumBattery());
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method removebattery", e);
                }
            }
            boxesRemove++;

            if (boxesRemove == boxesLost) {
                setStateCard(StateCardType.CHOICE_ATTRIBUTES);
                game.nextPlayer();
            }

            if(noMore(player)){
                setStateCard(StateCardType.CHOICE_ATTRIBUTES);
                game.nextPlayer();
            }
        }
        else throw new IllegalStateException();
    }

    private boolean noMore(Player player) {
        if(player.getSpaceship().noBox()){
            List<Tile> batteries = player.getSpaceship().getTilesByType(TileType.BATTERY);
            for(Tile tile : batteries){
                if(tile.getNumBattery() != 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
