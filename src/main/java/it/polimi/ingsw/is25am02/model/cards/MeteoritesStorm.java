package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Optional;

public class MeteoritesStorm extends Card {
    private ArrayList<Pair<Integer, RotationType>> meteorites;
    private int currentIndex;


    public MeteoritesStorm(int level, ArrayList<Pair<Integer,RotationType>> meteorites) {
        super(level,StateCardType.ROLL);
        this.meteorites = meteorites;
        currentIndex = 0;
    }

    public MeteoritesStorm createCard(){
        //Here the code for reading on file the card's values
        return new MeteoritesStorm(getLevel(), meteorites);
    }

    @Override
    public void holdSpaceship(Game game, Player player, int x, int y){
        player.getSpaceship().holdSpaceship(x,y);

        if(player.equals(game.getGameboard().getRanking().getLast()) && currentIndex < meteorites.size()-1){
            currentIndex++;
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
    public void calculateDamage(Game game, Player player, Optional<BatteryStorage> storage){
        boolean res = player.getSpaceship().meteoriteDamage(meteorites.get(currentIndex).getKey(), meteorites.get(currentIndex).getValue(), game.getDiceResult(), storage);

        if(res){
            game.getCurrentCard().setStateCard(StateCardType.DECISION);
        }
        else{
            if(player.equals(game.getGameboard().getRanking().getLast()) && currentIndex < meteorites.size()-1){
                currentIndex++;
                game.getCurrentCard().setStateCard(StateCardType.ROLL);
                game.getCurrentState().setCurrentPlayer(game.getGameboard().getRanking().getFirst());
            }
            else if(player.equals(game.getGameboard().getRanking().getLast()) && currentIndex == meteorites.size()-1){
                game.nextPlayer();
            }
            else{
                game.nextPlayer();
            }
        }
    }
}
