package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Coordinate;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;

import java.util.*;

import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.TAKE_CARD;

public class WarZone_I extends Card {
    private final int flyback;
    private final int aliveLost;
    private int aliveRemoved;
    private final ArrayList<Pair<Integer, RotationType>> shots;
    private final LinkedHashMap<Player, Integer> declarationCrew;
    private final LinkedHashMap<Player, Double> declarationCannon;
    private final LinkedHashMap<Player, Integer> declarationMotor;
    private int currentIndex;
    private int currentPhase;
    private final CardType cardType;

    public WarZone_I(int level, int flyback, int aliveLost, ArrayList<Pair<Integer, RotationType>> shots) {
        super(level, StateCardType.CHOICE_ATTRIBUTES);
        this.flyback = flyback;
        this.aliveLost = aliveLost;
        this.aliveRemoved = 0;
        this.shots = shots;
        this.declarationCrew = new LinkedHashMap<>();
        this.declarationCannon = new LinkedHashMap<>();
        this.declarationMotor = new LinkedHashMap<>();
        this.currentIndex = 0;
        this.currentPhase = 1;
        this.cardType = CardType.WARZONE1;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void choiceCrew(Game game, Player player) throws IllegalPhaseException {
        if(currentPhase == 1) {
            declarationCrew.put(player, player.getSpaceship().calculateNumAlive());

            if (player.equals(game.getGameboard().getRanking().getLast())) {
                Player p = null;
                int minCrew = Integer.MAX_VALUE;
                for (Map.Entry<Player, Integer> entry : declarationCrew.entrySet()) {
                    if (entry.getValue() < minCrew) {
                        minCrew = entry.getValue();
                        p = entry.getKey();
                    }
                }
                game.getGameboard().move((-1)*flyback, p);
                currentPhase++;
                game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
            }
            else game.nextPlayer();
        }
        else throw new IllegalPhaseException("Should be phase 1, instead is " + currentPhase);
    }

    @Override
    public void choiceDoubleMotor(Game game, Player player, List<Coordinate> motors, List<Coordinate> batteries) throws IllegalPhaseException, IllegalRemoveException { //primo è dmotor secondo è batterystorage
        if(currentPhase == 2) {
            ArrayList<Tile> dMotors = new ArrayList<>();

            for(Coordinate motor : motors) {
                dMotors.add(player.getSpaceship().getTile(motor.x(), motor.y()).get());
            }
            declarationMotor.put(player, player.getSpaceship().calculateMotorPower(dMotors));
            for(Coordinate battery : batteries) {
                player.getSpaceship().getTile(battery.x(), battery.y()).get().removeBattery();
            }

            if (player.equals(game.getGameboard().getRanking().getLast())) {
                Player p = null;
                int minMotor = Integer.MAX_VALUE;
                for (Map.Entry<Player, Integer> entry : declarationMotor.entrySet()) {
                    if (entry.getValue() < minMotor) {
                        minMotor= entry.getValue();
                        p = entry.getKey();
                    }
                }
                game.getCurrentState().setCurrentPlayer(p);
                game.getCurrentCard().setStateCard(StateCardType.REMOVE);
            }
            else game.nextPlayer();
        }
        else throw new IllegalPhaseException("Should be phase 2, instead is " + currentPhase);
    }

    @Override
    public void removeCrew(Game game, Player player, Tile cabin) throws IllegalPhaseException {
        if(currentPhase == 2) {
            try {
                cabin.removeCrew();
                aliveRemoved++;
            } catch (IllegalRemoveException e) {
                System.out.println(e.getMessage());
            }

            if (aliveRemoved == aliveLost) {
                game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
                game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
                currentPhase++;
            }
        }
        else throw new IllegalPhaseException("Should be phase 2, instead is " + currentPhase);
    }

    @Override
    public void choiceDoubleCannon(Game game, Player player, List<Coordinate> cannons, List<Coordinate> batteries) throws IllegalPhaseException, IllegalRemoveException {
        if(currentPhase == 3) {
            List<Tile> dCannon = new ArrayList<>();
            for(Coordinate cannon : cannons) {
                dCannon.add(player.getSpaceship().getTile(cannon.x(), cannon.y()).get());
            }
            declarationCannon.put(player, player.getSpaceship().calculateCannonPower(dCannon));
            for(Coordinate battery : batteries) {
                player.getSpaceship().getTile(battery.x(), battery.y()).get().removeBattery();
            }

            if (player.equals(game.getGameboard().getRanking().getLast())) {
                Player p = null;
                double minCannon = Double.MAX_VALUE;
                for (Map.Entry<Player, Double> entry : declarationCannon.entrySet()) {
                    if (entry.getValue() < minCannon) {
                        minCannon = entry.getValue();
                        p = entry.getKey();
                    }
                }
                game.getCurrentState().setCurrentPlayer(p);
                game.getCurrentCard().setStateCard(StateCardType.ROLL);
                currentPhase++;
            }
            else game.nextPlayer();
        }
        else throw new IllegalPhaseException("Should be phase 3, instead is " + currentPhase);
    }

    @Override
    public void calculateDamage(Game game, Player player, Optional<Tile> storage) throws IllegalPhaseException, IllegalRemoveException {
        if(currentPhase == 4) {
            boolean res = player.getSpaceship().shotDamage(shots.get(currentIndex).getKey(), shots.get(currentIndex).getValue(), game.getDiceResult(), storage);

            if (res) {
                game.getCurrentCard().setStateCard(StateCardType.DECISION);
            } else {
                game.getCurrentCard().setStateCard(StateCardType.ROLL);
            }
            currentIndex++;

            if(currentIndex == shots.size()){
                game.getCurrentCard().setStateCard(StateCardType.FINISH);
                game.getCurrentState().setPhase(TAKE_CARD);
            }
        }
        else throw new IllegalPhaseException("Should be phase 4, instead is " + currentPhase);
    }

    @Override
    public void keepBlocks(Game game, Player player, boolean[][] mask) throws IllegalPhaseException {
        if(currentPhase == 4) {
            player.getSpaceship().keepBlock(mask);
            game.getCurrentCard().setStateCard(StateCardType.ROLL);
        }
        else throw new IllegalPhaseException("Should be phase 4, instead is " + currentPhase);
    }
}
