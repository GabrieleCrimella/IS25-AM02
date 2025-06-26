package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class MeteoritesStorm extends Card {
    private final ArrayList<Pair<Integer, RotationType>> meteorites;
    private int currentIndex;
    private final CardType cardType;


    public MeteoritesStorm(int level, ArrayList<Pair<Integer,RotationType>> meteorites, String imagepath,String comment,boolean testFlight) {
        super(level,StateCardType.ROLL, imagepath, comment,testFlight);
        this.meteorites = meteorites;
        currentIndex = 0;
        this.cardType = CardType.METEORITES_STORM;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void keepBlocks(Game game, Player player, Coordinate pos){
        player.getSpaceship().keepBlock(player.getNickname(), pos);

        for(Player p: game.getPlayers()) {
            try {
                p.getObserver().displayMessage("ingame.meteoritesIndex", Map.of("index", String.valueOf(currentIndex)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if(player.equals(game.getGameboard().getRanking().getLast()) && currentIndex < meteorites.size()-1){
            currentIndex++;
            for(Player p: game.getPlayers()) {
                try {
                    p.getObserver().displayMessage("ingame.meteoritesIndex", Map.of("index", String.valueOf(currentIndex)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            game.setDiceResultManually(0);
            game.getCurrentCard().setStateCard(StateCardType.ROLL);
            game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
        }
        else if(player.equals(game.getGameboard().getRanking().getLast()) && currentIndex == meteorites.size()-1){
            game.nextPlayer();
        }
        else{
            game.nextPlayer();
            game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
        }
    }

    @Override
    public void calculateDamage(Game game, Player player, Optional<Tile> storage) throws IllegalRemoveException {
        boolean res = player.getSpaceship().meteoriteDamage(player.getNickname(), meteorites.get(currentIndex).getKey(), meteorites.get(currentIndex).getValue(), game.getDiceResult(), storage);

        if(!storage.isPresent()){
            res=false;
        }
        for(Player p: game.getPlayers()) {
            try {
                p.getObserver().displayMessage("ingame.meteoritesIndex", Map.of("index", String.valueOf(currentIndex)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if(res){
            game.getCurrentCard().setStateCard(StateCardType.DECISION);
        }
        else{
            if(player.equals(game.getGameboard().getRanking().getLast()) && currentIndex < meteorites.size()-1){
                currentIndex++;
                for(Player p: game.getPlayers()) {
                    try {
                        p.getObserver().displayMessage("ingame.meteoritesIndex", Map.of("index", String.valueOf(currentIndex)));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                //game.setDiceResultManually(0);
                game.getCurrentCard().setStateCard(StateCardType.ROLL);
                game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
            }
            else if(player.equals(game.getGameboard().getRanking().getLast()) && currentIndex == meteorites.size()-1){
                game.nextPlayer();
                /*game.setDiceResultManually(0);
                for (Player p : game.getPlayers()) {
                    p.onDiceUpdate(player.getNickname(), game.getGameboard().getDice());
                }*/
            }
            else{
                game.nextPlayer();
            }
        }
    }
}
