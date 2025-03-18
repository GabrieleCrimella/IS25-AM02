package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import it.polimi.ingsw.is25am02.model.tiles.DoubleCannon;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.is25am02.model.enumerations.StateCardType.CHOICE_ATTRIBUTES;
import static it.polimi.ingsw.is25am02.model.enumerations.StateCardType.DECISION;

public class Pirate extends Enemies{
    private ArrayList<Pair<Integer, RotationType>> shots;
    private int currentIndex;
    private ArrayList<Player> losers;
    private int phase;

    public Pirate(int level, int cannonPowers, int daysLost, int credit, ArrayList<Pair<Integer, RotationType>> shots) {
        super(level, cannonPowers, daysLost, credit, CHOICE_ATTRIBUTES);
        this.shots = shots;
        this.losers = new ArrayList<>();
        this.currentIndex = 0;
        this.phase = 1;
    }

    public Pirate createCard(){
        //Here the code for reading on file the card's values
        return new Pirate(getLevel(), getCannonPowers(), getDaysLost(), getCredit(), shots);
    }

    @Override
    public void choiceDoubleCannon(Game game, Player player, Optional<List<Pair<DoubleCannon, BatteryStorage>>> choices){
        if(phase == 1) {
            List<DoubleCannon> dCannon = new ArrayList<>();
            double playerPower;
            if (choices.isPresent()) {
                for (Pair<DoubleCannon, BatteryStorage> pair : choices.get()) {
                    dCannon.add(pair.getKey());
                    pair.getValue().removeBattery();
                }
                playerPower = player.getSpaceship().calculateCannonPower(dCannon);
            } else
                playerPower = player.getSpaceship().calculateCannonPower(new ArrayList<DoubleCannon>()); //se uso solo motori singoli

            if (playerPower > getCannonPowers()) {
                setStateCard(StateCardType.DECISION);
            } else if (playerPower == getCannonPowers()) {
                if (player.equals(game.getGameboard().getRanking().getLast()) && !losers.isEmpty()) {
                    setStateCard(StateCardType.ROLL);
                    phase++;
                } else {
                    game.nextPlayer();
                }
            } else {
                losers.add(player);
                if (player.equals(game.getGameboard().getRanking().getLast())) {
                    setStateCard(StateCardType.ROLL);
                    phase++;
                }
            }
        }
        else throw new IllegalStateException();
    }

    @Override
    public void choice(Game game, Player player, boolean choice){
        if(phase == 1) {
            if (choice) {
                //Applico effetti (Volo e Crediti)
                player.getSpaceship().addCosmicCredits(getCredit());
                game.getGameboard().move((-1) * getDaysLost(), player);
            }

            if (losers.isEmpty()) {
                setStateCard(StateCardType.FINISH);
                game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
            } else {
                setStateCard(StateCardType.ROLL);
                game.getCurrentState().setCurrentPlayer(losers.getFirst());
                phase++;
            }
        }
        else throw new IllegalStateException();
    }

    @Override
    public void calculateDamage(Game game, Player player, Optional<BatteryStorage> storage){
        if(losers.contains(player) && phase == 2) {
            boolean res = player.getSpaceship().shotDamage(shots.get(currentIndex).getKey(), shots.get(currentIndex).getValue(), game.getDiceResult(), storage);

            if (res) {
                game.getCurrentCard().setStateCard(StateCardType.DECISION);
            } else {
                if (player.equals(losers.getLast()) && currentIndex < shots.size() - 1) {
                    currentIndex++;
                    game.getCurrentCard().setStateCard(StateCardType.ROLL);
                    game.getCurrentState().setCurrentPlayer(losers.getFirst());
                } else if (player.equals(losers.getLast()) && currentIndex == shots.size() - 1) {
                    game.nextPlayer();
                } else {
                    game.nextPlayer();
                }
            }
        }
        else throw new IllegalArgumentException();
    }

    @Override
    public void holdSpaceship(Game game, Player player, int x, int y){
        if(losers.contains(player) && phase == 2){
            player.getSpaceship().holdSpaceship(x, y);

            if (player.equals(losers.getLast()) && currentIndex < losers.size() - 1) {
                currentIndex++;
                game.getCurrentCard().setStateCard(StateCardType.ROLL);
                game.getCurrentState().setCurrentPlayer(losers.getFirst());
            } else if (player.equals(losers.getLast()) && currentIndex == shots.size() - 1) {
                game.nextPlayer();
            } else {
                game.nextPlayer();
                game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
            }
        }
        else throw new IllegalStateException();
    }
}
