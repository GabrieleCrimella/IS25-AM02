package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class Card {
    private final int level;
    private StateCardType stateCard;

    public Card(int level, StateCardType stateCard) {
        this.level = level;
        this.stateCard = stateCard;
    }

    public int getLevel() {
        return level;
    }

    public StateCardType getStateCard() {
        return stateCard;
    }

    public void setStateCard(StateCardType stateCardType) {
        this.stateCard = stateCardType;
    }

    public void choice(Game game, Player player, boolean choice) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public void removeCrew(Game game, Player player, Cabin cabin) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public List<Box> choiceBox(Game game, Player player, boolean choice) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public void moveBox(Game game, Player player, List<Box> start, List<Box> end, Box box, boolean on) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public List<Box> choicePlanet(Game game, Player player, int index) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public void choiceDoubleMotor(Game game, Player player, Optional<List<Pair<DoubleMotor, BatteryStorage>>> choices) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public void choiceDoubleCannon(Game game, Player player, Optional<List<Pair<DoubleCannon, BatteryStorage>>> choices) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public void choiceCrew(Game game, Player player) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public void removeBox(Game game, Player player, SpecialStorage storage, BoxType type) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public void removeBattery(Game game, Player player, BatteryStorage storage) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public void calculateDamage(Game game, Player player, Optional<BatteryStorage> storage) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public void holdSpaceship(Game game, Player player, int x, int y) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public void effect(Game game) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    public LinkedList<Box> getBoxesWon() {
        return new LinkedList<Box>();
    }

}
