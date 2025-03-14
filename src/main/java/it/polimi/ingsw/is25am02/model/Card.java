package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;

import java.util.List;

public abstract class Card {
    private final int level;

    public Card(int level) {
        this.level = level;
    }

    public Card newCard() {
        return createCard();
    }

    public int getLevel() {
        return level;
    }

    public abstract Card createCard();

    void choice(Player player, boolean choice) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    void removeCrew(Cabin cabin) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    List<Box> choiceBox(Player player, boolean choice) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    void moveBox(List<Box> start, List<Box> end, BoxType type) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    List<Box> choicePlanet(Player player, int index) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    void choiceDoubleMotor(Player player, List<Pair<DoubleMotor, BatteryStorage>> choices) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    void choiceDoubleCannon(Player player, List<Pair<DoubleCannon, BatteryStorage>> choices) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    void removeBox(Player player, SpecialStorage storage, BoxType type) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

    void removeBattery(Player player, BatteryStorage storage) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported method");
    }

}
