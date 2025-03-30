package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.cards.boxes.*;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.is25am02.model.enumerations.StateCardType.DECISION;

public class Planet extends Card {
    private final int daysLost;
    private final ArrayList<LinkedList<Box>> planetOffers;
    private final ArrayList<LinkedList<BoxType>> planetOffersTypes;
    private final ArrayList<Integer> occupied; //tiene conto di quali pianeti sono occupati
    private final LinkedList<Player> landed;
    private final CardType cardType;
    private final BoxStore store;
    private LinkedList<Box> boxesWon;

    public Planet(int level, BoxStore store, int daysLost, ArrayList<LinkedList<Box>> planetOffers, ArrayList<LinkedList<BoxType>> planetOffersTypes) {
        super(level, StateCardType.DECISION);
        this.store = store;
        this.daysLost = daysLost;
        this.planetOffers = planetOffers;
        this.occupied = new ArrayList<>();
        this.landed = new LinkedList<>();
        this.planetOffersTypes = planetOffersTypes;
        this.cardType = CardType.PLANET;
        this.boxesWon = null;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public LinkedList<Box> getBoxesWon() { return boxesWon; }

    @Override
    public void clearPlanetOffers() {
        planetOffers.clear();
    }

    @Override
    public void addPlanetOffers(LinkedList<Box> boxes) {
        planetOffers.add(boxes);
    }

    @Override
    public ArrayList<LinkedList<Box>> getPlanetOffers() {
        return planetOffers;
    }

    @Override
    public ArrayList<LinkedList<BoxType>> getPlanetOffersTypes() {
        return planetOffersTypes;
    }

    @Override
    public List<Box> choicePlanet(Game game, Player player, int index) throws IllegalArgumentException{
        if(index >= 0 && index <= planetOffers.size()-1 && occupied.get(index) == 0) {
            occupied.set(index, 1);
            landed.add(player);
            for(BoxType type : getPlanetOffersTypes().get(index)){
                if(!store.getStore().isEmpty()){
                    Box box;
                    if(type.equals(BoxType.RED)){
                        if(store.getStore().containsKey(BoxType.RED)){
                            box = new RedBox(BoxType.RED);
                            planetOffers.get(index).add(box);
                        }
                    } else if(type.equals(BoxType.BLUE)){
                        if(store.getStore().containsKey(BoxType.BLUE)){
                            box = new BlueBox(BoxType.BLUE);
                            planetOffers.get(index).add(box);
                        }
                    } else if(type.equals(BoxType.GREEN)){
                        if(store.getStore().containsKey(BoxType.GREEN)){
                            box = new GreenBox(BoxType.GREEN);
                            planetOffers.get(index).add(box);
                        }
                    } else if(type.equals(BoxType.YELLOW)){
                        if(store.getStore().containsKey(BoxType.YELLOW)){
                            box = new YellowBox(BoxType.YELLOW);
                            planetOffers.get(index).add(box);
                        }
                    } else throw new IllegalArgumentException("I cannot add a box to planetoffer");
                } else { //se lo store è vuoto rimuovo i blocchetti rimasti sui pianeti già occupati
                    for (int i : occupied) { //vedo sui pianeti occupati
                        for (Box box : planetOffers.get(i)) {
                            if (box.getType().equals(BoxType.RED)) {
                                planetOffers.get(index).add(box);
                            } else if (box.getType().equals(BoxType.BLUE)) {
                                planetOffers.get(index).add(box);
                            } else if (box.getType().equals(BoxType.GREEN)) {
                                planetOffers.get(index).add(box);
                            } else if (box.getType().equals(BoxType.YELLOW)) {
                                planetOffers.get(index).add(box);
                            } else throw new IllegalArgumentException("I cannot add a box to planetoffer");
                        }
                    }
                }
            }
            setStateCard(StateCardType.BOXMANAGEMENT);
            boxesWon = planetOffers.get(index);
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
            boxesWon = null;
            return null;
        }
        else if(index == -1){ //The player doesn't want to land
            game.nextPlayer();
            boxesWon = null;
            return null;
        }
        else{
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    @Override
    public void moveBox(Game game, Player player, List<Box> start, List<Box> end, BoxType boxType, boolean on) {
        if(on) {
            Iterator<Box> it = start.iterator();
            while (it.hasNext()) {
                Box box = it.next();
                if (box.getType().equals(boxType)) {
                    it.remove();
                    end.add(box);
                    break;
                }
            }
        }
        else {
            if(player.equals(game.getGameboard().getRanking().getLast())) {
                Iterator<Player> it = landed.descendingIterator();
                while(it.hasNext()) {
                    Player temp = it.next();
                    game.getGameboard().move((-1)*daysLost, temp);
                }
            }
            boxesWon = null;
            game.getCurrentCard().setStateCard(DECISION);
            game.nextPlayer();
        }
    }
}
