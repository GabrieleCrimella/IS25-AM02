package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Gameboard;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.SpecialStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.is25am02.model.enumerations.StateCardType.DECISION;

public class Planet extends Card_with_box{
    private final int daysLost;
    private final ArrayList<ArrayList<Box>> planetOffers;
    private final ArrayList<ArrayList<BoxType>> planetOffersTypes;
    private final ArrayList<Integer> occupied; //tiene conto di quali pianeti sono occupati
    private final LinkedList<Player> landed;
    private CardType cardType;


    public Planet(int level, BoxStore store, int daysLost, ArrayList<ArrayList<Box>> planetOffers, ArrayList<ArrayList<BoxType>> planetOffersTypes) {
        super(level, store, StateCardType.DECISION);
        this.daysLost = daysLost;
        this.planetOffers = planetOffers;
        this.occupied = new ArrayList<>();
        this.landed = new LinkedList<>();
        this.planetOffersTypes = planetOffersTypes;
        this.cardType = CardType.PLANET;

        for(ArrayList<Box> boxes : planetOffers) {
            occupied.add(0);
        }
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void clearPlanetOffers() {
        planetOffers.clear();
    }

    @Override
    public void addPlanetOffers(ArrayList<Box> boxes) {
        planetOffers.add(boxes);
    }

    @Override
    public ArrayList<ArrayList<Box>> getPlanetOffers() {
        return planetOffers;
    }

    @Override
    public ArrayList<ArrayList<BoxType>> getPlanetOffersTypes() {
        return planetOffersTypes;
    }

    @Override
    public List<Box> choicePlanet(Game game, Player player, int index) throws IllegalArgumentException{
        if(index >= 0 && index <= planetOffers.size()-1 && occupied.get(index) == 0) {
            occupied.set(index, 1);
            landed.add(player);
            setStateCard(StateCardType.BOXMANAGEMENT);
            return planetOffers.get(index);
        }
        else if (player.equals(game.getGameboard().getRanking().getLast())){
            Iterator<Player> it = landed.descendingIterator();
            while(it.hasNext()) {
                Player temp = it.next();
                game.getGameboard().move((-1)*daysLost, temp);
            }
            game.getCurrentCard().setStateCard(DECISION);
            game.nextPlayer();
            return null;
        }
        else if(index == -1){ //The player doesn't want to land
            game.nextPlayer();
            return null;
        }
        else{
            throw new IllegalArgumentException("Index out of bounds");
        }

    }

    @Override
    public void moveBox(Game game, Player player, List<Box> start, List<Box> end, Box box, boolean on) {
        /*
        mancano vari controlli
        1. li sto passando ad uno storage o a uno special storage? posso mettere i rossi nella lista end?
        2. massimo numero di box che posso mettere nello storage? liste non sono limitate
        Non so come puoi fare a controllare queste cose perchè al posto di passare il riferimento alla lista
        dovresti passare il riferimento al tile/pianeta.
        Ovviamente questo crea problemi perchè sono di tipi diversi.
         */
        if(on) {
            start.remove(box);
            end.add(box);
        }
        else {
            if(player.equals(game.getGameboard().getRanking().getLast())) {
                Iterator<Player> it = landed.descendingIterator();
                while(it.hasNext()) {
                    Player temp = it.next();
                    game.getGameboard().move((-1)*daysLost, temp);
                }
            }
            game.getCurrentCard().setStateCard(DECISION);
            game.nextPlayer();
        }
    }
}
