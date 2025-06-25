package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.utils.enumerations.StateGameType;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import static it.polimi.ingsw.is25am02.utils.enumerations.StateCardType.CHOICE_ATTRIBUTES;

public class Pirate extends Enemies {
    private ArrayList<Pair<Integer, RotationType>> shots;
    private int currentIndexMeteor;
    private int currentIndexLosers;
    private ArrayList<Player> losers;
    private int phase;
    private final CardType cardType;

    public Pirate(int level, int cannonPowers, int daysLost, int credit, ArrayList<Pair<Integer, RotationType>> shots, String imagepath,String comment,boolean testFlight) {
        super(level, cannonPowers, daysLost, credit, CHOICE_ATTRIBUTES, imagepath,comment,testFlight);
        this.shots = shots;
        this.losers = new ArrayList<>();
        this.currentIndexMeteor = 0;
        this.currentIndexLosers = 0;
        this.phase = 1;
        this.cardType = CardType.PIRATE;
    }

    @Override
    public void setPhase(int phase) {
        this.phase = phase;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void choiceDoubleCannon(Game game, Player player, List<Coordinate> cannons, List<Coordinate> batteries) throws IllegalRemoveException, IllegalPhaseException {
        if (phase == 1) {
            //Calculate Player Power
            List<Tile> dCannon = new ArrayList<>();
            for(Coordinate cannon : cannons) {
                dCannon.add(player.getSpaceship().getTile(cannon.x(), cannon.y()).get());
            }
            double playerPower = player.getSpaceship().calculateCannonPower(dCannon);
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
                    }
                }

            }

            //Paragoni
            if (playerPower > getCannonPowers()) {
                setStateCard(StateCardType.DECISION);
            } else if (playerPower == getCannonPowers()) {
                //if (player.equals(game.getGameboard().getRanking().getLast()) && !losers.isEmpty()) {
                if (player.getNickname().equals(getCurrentOrder().getLast()) && !losers.isEmpty()){
                    game.setDiceResultManually(0);
                    game.getCurrentCard().setStateCard(StateCardType.ROLL);
                    game.getCurrentState().setCurrentPlayer(losers.getFirst());
                    phase++;
                } else if (player.getNickname().equals(getCurrentOrder().getLast()) && losers.isEmpty()){
                    game.nextPlayer();
                }
                else {
                    game.nextPlayer();
                }
            } else {
                losers.add(player);
                //if (player.equals(game.getGameboard().getRanking().getLast())) {
                if (player.getNickname().equals(getCurrentOrder().getLast())){
                    game.setDiceResultManually(0);
                    game.getCurrentState().setCurrentPlayer(losers.getFirst());
                    game.getCurrentCard().setStateCard(StateCardType.ROLL);
                    phase++;
                }
                else {
                    game.nextPlayer();
                }
            }
        } else throw new IllegalPhaseException("Should be phase 1, instead is " + phase);
    }

    @Override
    public void choice(Game game, Player player, boolean choice) throws IllegalPhaseException {
        if (phase == 1) {
            if (choice) {
                //Applico effetti (Volo e Crediti)
                player.getSpaceship().addCosmicCredits(getCredit());
                game.getGameboard().move((-1) * getDaysLost(), player);
                if(observers != null) {
                    for (String nick : observers.keySet()) {
                        try {
                            observers.get(nick).showCreditUpdate(player.getNickname(), player.getSpaceship().getCosmicCredits());
                        } catch (RemoteException e) {
                            ServerController.logger.log(Level.SEVERE, "error in method choice", e);
                        }
                        try {
                            observers.get(nick).showPositionUpdate(player.getNickname(), game.getGameboard().getPositions().get(player));
                            try {
                                observers.get(nick).displayMessage("ingame.moveongameboard", Map.of("nick", player.getNickname(), "pos", String.valueOf(game.getGameboard().getPositions().get(player))));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        } catch (RemoteException e) {
                            ServerController.logger.log(Level.SEVERE, "error in method choice", e);
                        }
                    }
                }
            }

            if (losers.isEmpty()) {
                setStateCard(StateCardType.FINISH);
                game.getCurrentState().setPhase(StateGameType.TAKE_CARD);
                game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
            } else {
                game.setDiceResultManually(0);
                game.getCurrentCard().setStateCard(StateCardType.ROLL);
                game.getCurrentState().setCurrentPlayer(losers.getFirst());
                phase++;
            }
        } else throw new IllegalPhaseException("Should be phase 1, instead is " + phase);
    }

    @Override
    public void calculateDamage(Game game, Player player, Optional<Tile> storage) throws IllegalRemoveException, IllegalPhaseException {
        if (losers.contains(player) && phase == 2) {
            boolean res = player.getSpaceship().shotDamage(player.getNickname(), shots.get(currentIndexMeteor).getKey(), shots.get(currentIndexMeteor).getValue(), game.getDiceResult(), storage);

            if (res) {
                game.getCurrentCard().setStateCard(StateCardType.DECISION);
            } else {
                if (player.equals(losers.getLast()) && currentIndexMeteor < shots.size() - 1) {
                    currentIndexMeteor++;
                    currentIndexLosers = 0;
                    game.setDiceResultManually(0);
                    game.getCurrentCard().setStateCard(StateCardType.ROLL);
                    game.getCurrentState().setCurrentPlayer(losers.get(currentIndexLosers));
                } else if (player.equals(losers.getLast()) && currentIndexMeteor == shots.size() - 1) {
                    game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
                    game.getCurrentCard().setStateCard(StateCardType.FINISH);
                    game.getCurrentState().setPhase(StateGameType.TAKE_CARD);
                } else {
                    currentIndexLosers++;
                    game.getCurrentState().setCurrentPlayer(losers.get(currentIndexLosers));
                }
            }
        } else throw new IllegalPhaseException("Should be phase 2, instead is " + phase);
    }

    @Override
    public void keepBlocks(Game game, Player player, Coordinate pos) throws IllegalPhaseException {
        if (losers.contains(player) && phase == 2) {
            player.getSpaceship().keepBlock(player.getNickname(), pos);

            if (player.equals(losers.getLast()) && currentIndexMeteor < shots.size() - 1) {
                currentIndexMeteor++;
                currentIndexLosers = 0;
                game.setDiceResultManually(0);
                game.getCurrentCard().setStateCard(StateCardType.ROLL);
                game.getCurrentState().setCurrentPlayer(losers.get(currentIndexLosers));
            } else if (player.equals(losers.getLast()) && currentIndexMeteor == shots.size() - 1) {
                game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
                game.getCurrentCard().setStateCard(StateCardType.FINISH);
                game.getCurrentState().setPhase(StateGameType.TAKE_CARD);
            } else {
                currentIndexLosers++;
                game.getCurrentState().setCurrentPlayer(losers.get(currentIndexLosers));
            }
        } else throw new IllegalPhaseException("Should be phase 2, instead is " + phase);
    }
}
