package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class WarZone_I extends Card {
    private final int flyback;
    private final int humanLost;
    private ArrayList<Pair<Integer, RotationType>> shots;
    private LinkedHashMap<Player, Integer> declarationCrew;
    private LinkedHashMap<Player, Double> declarationCannon;
    private LinkedHashMap<Player, Integer> declarationMotor;
    private int currentIndex;
    private int currentPhase;

    public WarZone_I(int level, int flyback, int humanLost, ArrayList<Pair<Integer, RotationType>> shots) {
        super(level, StateCardType.CHOICE_ATTRIBUTES);
        this.flyback = flyback;
        this.humanLost = humanLost;
        this.shots = shots;
        this.declarationCrew = new LinkedHashMap<>();
        this.declarationCannon = new LinkedHashMap<>();
        this.declarationMotor = new LinkedHashMap<>();
        this.currentIndex = 0;
        this.currentPhase = 1;
    }

    public WarZone_I createCard(){
        //Here the code for reading on file the card's values
        return new WarZone_I(getLevel(), flyback, humanLost, shots);
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public void choiceCrew(Game game, Player player) {
        if(currentPhase == 1) {
            declarationCrew.put(player, player.getSpaceship().calculateNumAlive());

            if (player.equals(game.getGameboard().getRanking().getLast())) {
                //todo trovo il primo player con la crew più piccola
                //Player minCrew =
                //todo lo metto come currentPlayer
                //todo lo sposto indietro di flyback
                //todo aggiorno phase di warzone
            } else {
                game.nextPlayer();
            }
        }
        else{
            throw new IllegalStateException();
        }
    }
}
