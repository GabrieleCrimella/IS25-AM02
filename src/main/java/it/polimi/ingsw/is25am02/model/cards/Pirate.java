package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Coordinate;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.is25am02.model.enumerations.StateCardType.CHOICE_ATTRIBUTES;

public class Pirate extends Enemies {
    private ArrayList<Pair<Integer, RotationType>> shots;
    private int currentIndex;
    private ArrayList<Player> losers;
    private int phase;
    private final CardType cardType;

    public Pirate(int level, int cannonPowers, int daysLost, int credit, ArrayList<Pair<Integer, RotationType>> shots) {
        super(level, cannonPowers, daysLost, credit, CHOICE_ATTRIBUTES);
        this.shots = shots;
        this.losers = new ArrayList<>();
        this.currentIndex = 0;
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
            }

            //Paragoni
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
        } else throw new IllegalPhaseException("Should be phase 1, instead is " + phase);
    }

    @Override
    public void choice(Game game, Player player, boolean choice) throws IllegalPhaseException {
        if (phase == 1) {
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
        } else throw new IllegalPhaseException("Should be phase 1, instead is " + phase);
    }

    @Override
    public void calculateDamage(Game game, Player player, Optional<Tile> storage) throws IllegalRemoveException, IllegalPhaseException {
        if (losers.contains(player) && phase == 2) {
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
        } else throw new IllegalPhaseException("Should be phase 2, instead is " + phase);
    }

    @Override
    public void keepBlocks(Game game, Player player, Coordinate pos) throws IllegalPhaseException {
        if (losers.contains(player) && phase == 2) {
            player.getSpaceship().keepBlock(pos);

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
        } else throw new IllegalPhaseException("Should be phase 2, instead is " + phase);
    }
}
