package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import it.polimi.ingsw.is25am02.model.tiles.Cabin;
import it.polimi.ingsw.is25am02.model.tiles.DoubleCannon;
import it.polimi.ingsw.is25am02.model.tiles.DoubleMotor;
import javafx.util.Pair;

import java.util.*;

import static it.polimi.ingsw.is25am02.model.enumerations.StateGameType.TAKE_CARD;

public class WarZone_I extends Card {
    private final int flyback;
    private final int aliveLost;
    private int aliveRemoved;
    private ArrayList<Pair<Integer, RotationType>> shots;
    private LinkedHashMap<Player, Integer> declarationCrew;
    private LinkedHashMap<Player, Double> declarationCannon;
    private LinkedHashMap<Player, Integer> declarationMotor;
    private int currentIndex;
    private int currentPhase;

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
    }

    public WarZone_I createCard(){
        //Here the code for reading on file the card's values
        return new WarZone_I(getLevel(), flyback, aliveLost, shots);
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    @Override
    public void choiceCrew(Game game, Player player) {
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
        else throw new IllegalStateException();
    }

    @Override
    public void choiceDoubleMotor(Game game, Player player, Optional<List<Pair<DoubleMotor, BatteryStorage>>> choices){
        if(currentPhase == 2) {
            ArrayList<DoubleMotor> doubleMotors = new ArrayList<>();
            if(choices.isPresent()){
                for(Pair<DoubleMotor, BatteryStorage> pair: choices.get()){
                    doubleMotors.add(pair.getKey());
                    pair.getValue().removeBattery();
                }
                declarationMotor.put(player, player.getSpaceship().calculateMotorPower(doubleMotors));
            }
            else declarationMotor.put(player, player.getSpaceship().calculateMotorPower(new ArrayList<DoubleMotor>()));

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
        else throw new IllegalStateException();
    }

    @Override
    public void removeCrew(Game game, Player player, Cabin cabin){
        if(currentPhase == 2) {
            try {
                cabin.removeCrew();            //todo togliere il parametro dalla remove di cabin
                aliveRemoved++;
            } catch (IllegalStateException e) {  //todo qui sarà IllegalRemoveException()
                //gestisco eccezione non c'è equipaggio sulla cabin passata
            }

            if (aliveRemoved == aliveLost) {
                game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
                game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
                currentPhase++;
            }
        }
        else throw new IllegalStateException();
    }

    @Override
    public void choiceDoubleCannon(Game game, Player player, Optional<List<Pair<DoubleCannon, BatteryStorage>>> choices){
        if(currentPhase == 3) {
            List<DoubleCannon> dCannon = new ArrayList<>();
            if (choices.isPresent()) {
                for (Pair<DoubleCannon, BatteryStorage> pair : choices.get()) {
                    dCannon.add(pair.getKey());
                    pair.getValue().removeBattery();  //rimuovo la batteria che è stata usata
                }
                declarationCannon.put(player, player.getSpaceship().calculateCannonPower(dCannon));
            }
            else declarationCannon.put(player, player.getSpaceship().calculateCannonPower(new ArrayList<DoubleCannon>()));

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
        else throw new IllegalStateException();
    }

    @Override
    public void calculateDamage(Game game, Player player, Optional<BatteryStorage> storage){
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
        else throw new IllegalStateException();
    }

    @Override
    public void holdSpaceship(Game game, Player player, int x, int y){
        if(currentPhase == 4) {
            player.getSpaceship().holdSpaceship(x,y);
            game.getCurrentCard().setStateCard(StateCardType.ROLL);
        }
        else throw new IllegalStateException();
    }
}
