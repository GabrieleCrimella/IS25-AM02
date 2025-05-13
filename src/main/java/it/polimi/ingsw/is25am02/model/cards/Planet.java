package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Card;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.cards.boxes.*;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.is25am02.utils.enumerations.StateCardType.DECISION;

public class Planet extends Card {
    private final int daysLost;
    private final ArrayList<LinkedList<Box>> planetOffers;
    private final ArrayList<LinkedList<BoxType>> planetOffersTypes;
    private final ArrayList<Integer> occupied; //tiene conto di quali pianeti sono occupati
    private final LinkedList<Player> landed;
    private final CardType cardType;
    private final BoxStore store;
    private LinkedList<Box> boxesWon;

    public Planet(int level, BoxStore store, int daysLost, ArrayList<LinkedList<Box>> planetOffers, ArrayList<LinkedList<BoxType>> planetOffersTypes, String imagepath,String comment,boolean testFlight) {
        super(level, StateCardType.DECISION, imagepath,comment,testFlight);
        this.store = store;
        this.daysLost = daysLost;
        this.planetOffers = planetOffers;
        this.occupied = new ArrayList<>();
        this.landed = new LinkedList<>();
        this.planetOffersTypes = planetOffersTypes;
        this.cardType = CardType.PLANET;
        this.boxesWon = null;
        initializeOccupied();
    }

    private void initializeOccupied(){
        for (int i = 0; i < planetOffersTypes.size(); i++) {
            this.occupied.add(0);
        }
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
    public void choicePlanet(Game game, Player player, int index) throws IllegalArgumentException{
        LinkedList<Box> planetList = new LinkedList<>();
        if(index >= 0 && index <= planetOffersTypes.size()-1 && occupied.get(index) == 0) {
            occupied.set(index, 1);
            landed.add(player);
            for(BoxType type : getPlanetOffersTypes().get(index)){
                if(!store.getStore().isEmpty()){
                    Box box;
                    if(type.equals(BoxType.RED)){
                        if(store.getStore().containsKey(BoxType.RED)){
                            box = new RedBox(BoxType.RED);
                            planetList.add(box);
                        }
                    } else if(type.equals(BoxType.BLUE)){
                        if(store.getStore().containsKey(BoxType.BLUE)){
                            box = new BlueBox(BoxType.BLUE);
                            planetList.add(box);
                        }
                    } else if(type.equals(BoxType.GREEN)){
                        if(store.getStore().containsKey(BoxType.GREEN)){
                            box = new GreenBox(BoxType.GREEN);
                            planetList.add(box);
                        }
                    } else if(type.equals(BoxType.YELLOW)){
                        if(store.getStore().containsKey(BoxType.YELLOW)){
                            box = new YellowBox(BoxType.YELLOW);
                            planetList.add(box);
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
            planetOffers.add(index, planetList);
            setStateCard(StateCardType.BOXMANAGEMENT);
            boxesWon = planetOffers.get(index);
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
        }
        else if(index == -1){ //The player doesn't want to land
            game.nextPlayer();
            boxesWon = null;
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
