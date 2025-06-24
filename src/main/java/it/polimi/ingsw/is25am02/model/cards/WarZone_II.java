package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;

import static it.polimi.ingsw.is25am02.utils.enumerations.StateGameType.TAKE_CARD;

public class WarZone_II extends Card{
    private final int flyback;
    private final int boxesLost;
    private int boxesRemoved;
    private final LinkedHashMap<Player, Integer> declarationCrew;
    private final LinkedHashMap<Player, Double> declarationCannon;
    private final LinkedHashMap<Player, Integer> declarationMotor;
    private int currentIndex;
    private int currentPhase;
    private final ArrayList<Pair<Integer, RotationType>> shots;
    private final CardType cardType;

    public WarZone_II(int level, int flyback, int boxesLost, ArrayList<Pair<Integer, RotationType>> shots, String imagepath,String comment,boolean testFlight) {
        super(level, StateCardType.DECISION, imagepath,comment,testFlight);
        this.flyback = flyback;
        this.boxesLost = boxesLost;
        this.shots = shots;
        this.currentPhase = 1;
        this.currentIndex = 0;
        this.boxesRemoved = 0;
        this.declarationCrew = new LinkedHashMap<>();
        this.declarationCannon = new LinkedHashMap<>();
        this.declarationMotor = new LinkedHashMap<>();
        this.cardType = CardType.WARZONE2;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void choiceDoubleCannon(Game game, Player player, List<Coordinate> cannons, List<Coordinate> batteries) throws IllegalRemoveException {
        if (currentPhase == 1) {
            List<Tile> dCannon = new ArrayList<>();
            for(Coordinate cannon : cannons) {
                dCannon.add(player.getSpaceship().getTile(cannon.x(), cannon.y()).get());
            }
            declarationCannon.put(player, player.getSpaceship().calculateCannonPower(dCannon));
            for(Coordinate battery : batteries) {
                player.getSpaceship().getTile(battery.x(), battery.y()).get().removeBattery();
                if (observers != null){
                for (String nick:observers.keySet()) {
                    try {
                        Coordinate pos = new Coordinate (battery.x(),battery.y());
                        observers.get(nick).showBatteryRemoval(pos, player.getNickname(), player.getSpaceship().getSpaceshipIterator().getTile(battery.x(), battery.y()).get().getNumBattery());
                    } catch (RemoteException e) {
                        ServerController.logger.log(Level.SEVERE, "error in method choicedoublecannon", e);
                    }
                }}
            }
            if (player.getNickname().equals(getCurrentOrder().getLast())) {
            //if (player.equals(game.getGameboard().getRanking().getLast())) {
                Player p = null;
                double minCannon = Double.MAX_VALUE;
                for (Map.Entry<Player, Double> entry : declarationCannon.entrySet()) {
                    if (entry.getValue() < minCannon) {
                        minCannon = entry.getValue();
                        p = entry.getKey();
                    }
                }
                game.getGameboard().move((-1) * flyback, p);
                for (String nick:observers.keySet()) {
                    try {
                        observers.get(nick).showPositionUpdate(player.getNickname(), game.getGameboard().getPositions().get(player));
                    } catch (RemoteException e) {
                        ServerController.logger.log(Level.SEVERE, "error in method choicedoublecannon", e);
                    }
                }
                //game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
                game.getCurrentState().setCurrentPlayer(game.getPlayerFromNickname(getCurrentOrder().getFirst()));
                currentPhase++;
            } else game.nextPlayer();
        } else throw new IllegalStateException();
    }

    @Override
    public void choiceDoubleMotor(Game game, Player player, List<Coordinate> motors, List<Coordinate> batteries) throws IllegalRemoveException {
        if(currentPhase == 2) {
            ArrayList<Tile> dMotors = new ArrayList<>();
            for(Coordinate motor : motors) {
                dMotors.add(player.getSpaceship().getTile(motor.x(), motor.y()).get());
            }
            declarationMotor.put(player, player.getSpaceship().calculateMotorPower(dMotors));
            for(Coordinate battery : batteries) {
                player.getSpaceship().getTile(battery.x(), battery.y()).get().removeBattery();
                for (String nick:observers.keySet()) {
                    try {
                        Coordinate pos = new Coordinate (battery.x(),battery.y());
                        observers.get(nick).showBatteryRemoval(pos, player.getNickname(), player.getSpaceship().getSpaceshipIterator().getTile(battery.x(), battery.y()).get().getNumBattery());
                    } catch (RemoteException e) {
                        ServerController.logger.log(Level.SEVERE, "error in method choicedoublemotor", e);
                    }
                }
            }
            if(player.getNickname().equals(getCurrentOrder().getLast())) {
            //if (player.equals(game.getGameboard().getRanking().getLast())) {
                Player p = null;
                int minMotor = Integer.MAX_VALUE;
                for (Map.Entry<Player, Integer> entry : declarationMotor.entrySet()) {
                    if (entry.getValue() < minMotor) {
                        minMotor= entry.getValue();
                        p = entry.getKey();
                    }
                }
                game.getCurrentCard().setStateCard(StateCardType.REMOVE);
                game.getCurrentState().setCurrentPlayer(p);
            }
            else game.nextPlayer();
        }
        else throw new IllegalStateException();
    }

    //se non ho abbastanza box allora tolgo le batterie
    @Override
    public void removeBox(Game game, Player player, Tile storage, BoxType type) throws IllegalRemoveException {
        if(currentPhase == 2) {
            if(!player.getSpaceship().noBox() && player.getSpaceship().isMostExpensive(type)) {
                List<Box> boxes = storage.getOccupation();
                for (Box box : boxes) {
                    if (box.getType() == type) {
                        storage.removeBox(box);
                        for (String nick:observers.keySet()) {
                            Coordinate pos = new Coordinate (player.getSpaceship().getSpaceshipIterator().getX(storage), player.getSpaceship().getSpaceshipIterator().getY(storage));
                            try {
                                observers.get(nick).showBoxUpdate(pos,player.getNickname(), player.getSpaceship().getTile(pos.x(), pos.y()).get().getOccupationTypes());
                            } catch (RemoteException e) {
                                ServerController.logger.log(Level.SEVERE, "error in method removebox", e);
                            }
                        }
                        boxesRemoved++;
                        break;
                    }
                }


                if (boxesRemoved == boxesLost) {
                    setStateCard(StateCardType.CHOICE_ATTRIBUTES);
                    game.getCurrentState().setCurrentPlayer(game.getPlayerFromNickname(getCurrentOrder().getFirst()));
                    //game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
                    currentPhase++;
                }
            } else throw new RuntimeException();
        }
        else throw new IllegalStateException();
    }

    @Override
    public void removeBattery(Game game, Player player, Tile storage) throws IllegalRemoveException {
        if(currentPhase == 2) {
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
                boxesRemoved++;

                if (boxesRemoved == boxesLost) {
                    setStateCard(StateCardType.CHOICE_ATTRIBUTES);
                    game.getCurrentState().setCurrentPlayer(game.getPlayerFromNickname(getCurrentOrder().getFirst()));
                    //game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
                    currentPhase++;
                }
            }
            else
                throw new IllegalRemoveException("You are trying to remove a battery but you still have boxes");

        }
        else throw new IllegalStateException();
    }

    //solo per testing
    @Override
    public void setCurrentPhase(int phase){
        currentPhase = phase;
    }


    public void choiceCrew(Game game, Player player) {
        if(currentPhase == 3) {
            declarationCrew.put(player, player.getSpaceship().calculateNumAlive());
            if (player.getNickname().equals(getCurrentOrder().getLast())) {
            //if (player.equals(game.getGameboard().getRanking().getLast())) {
                Player p = null;
                int minCrew = Integer.MAX_VALUE;
                for (Map.Entry<Player, Integer> entry : declarationCrew.entrySet()) {
                    if (entry.getValue() < minCrew) {
                        minCrew = entry.getValue();
                        p = entry.getKey();
                    }
                }
                currentPhase++;
                game.getCurrentState().setCurrentPlayer(p);
                game.setDiceResultManually(0);
                game.getCurrentCard().setStateCard(StateCardType.ROLL);
            }
            else game.nextPlayer();
        }
        else throw new IllegalStateException();
    }

    @Override
    public void calculateDamage(Game game, Player player, Optional<Tile> storage) throws IllegalRemoveException {
        if(currentPhase == 4) {
            boolean res = player.getSpaceship().shotDamage(player.getNickname(), shots.get(currentIndex).getKey(), shots.get(currentIndex).getValue(), game.getDiceResult(), storage);

            if (res) {
                game.getCurrentCard().setStateCard(StateCardType.DECISION);
            } else {
                game.setDiceResultManually(0);
                game.getCurrentCard().setStateCard(StateCardType.ROLL);
            }
            currentIndex++;

            if(currentIndex == shots.size()){
                game.getCurrentCard().setStateCard(StateCardType.FINISH);
                game.getCurrentState().setPhase(TAKE_CARD);
            }
        }
        else throw new IllegalStateException();
    }

    @Override
    public void keepBlocks(Game game, Player player, Coordinate pos){
        if(currentPhase == 4) {
            player.getSpaceship().keepBlock(player.getNickname(), pos);
            game.setDiceResultManually(0);
            game.getCurrentCard().setStateCard(StateCardType.ROLL);
        }
        else throw new IllegalStateException();
    }
}
