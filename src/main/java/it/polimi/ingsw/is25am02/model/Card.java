package it.polimi.ingsw.is25am02.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am02.model.cards.AbbandonedShip;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class Card {
    private final int level;
    private StateCardType stateCard;
    private CardDeck deck;

    public Card(int level, StateCardType stateCard) {
        this.level = level;
        this.stateCard = stateCard;
        loadCard();
    }

    private void loadCard() {
        final String JSON_FILE_PATH = "src/main/resources/json/cards.json";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(new File(JSON_FILE_PATH));
        } catch (IOException e) {
            System.out.println("Error in reading Card JSON file");
            e.printStackTrace();
        }
        int level;
        int aliveLost;
        int creditWin;
        int flyBack;
        for (JsonNode levelNode : rootNode) {  //foreach JSON node
            level = levelNode.get("level").asInt();

            switch (levelNode.get("type").asText()) {
                case "ABANDONED_SHIP":
                    aliveLost = levelNode.get("aliveLost").asInt();
                    creditWin = levelNode.get("creditWin").asInt();
                    flyBack = levelNode.get("flyBack").asInt();
                    deck.addCard(new AbbandonedShip(level, aliveLost, creditWin, flyBack));
                    break;
                case "ABANDONED_STATION":
                    setTiles.add(new Structural(TileType.STRUCTURAL, pos, RotationType.NORTH, 1));
                    break;
                case "PIRATE":
                    maxBattery = levelNode.get("maxBattery").asInt();
                    setTiles.add(new BatteryStorage(TileType.BATTERY, pos, RotationType.NORTH, 1, maxBattery));
                    break;
                case "TRAFFICKER":
                    setTiles.add(new BrownCabin(TileType.BROWN_CABIN, pos, RotationType.NORTH, 1));
                    break;
                case "SLAVEOWNER":
                    setTiles.add(new PurpleCabin(TileType.PURPLE_CABIN, pos, RotationType.NORTH, 1));
                    break;
                case "OPENSPACE":
                    setTiles.add(new Cabin(TileType.CABIN, pos, RotationType.NORTH, 1));
                    break;
                case "STARDUST":
                    setTiles.add(new Cannon(TileType.CANNON, pos, RotationType.NORTH, 1));
                    break;
                case "EPIDEMY":
                    setTiles.add(new Motors(TileType.MOTOR, pos, RotationType.NORTH, 1));
                    break;
                case "METEORITES":
                    setTiles.add(new DoubleMotor(TileType.D_MOTOR, pos, RotationType.NORTH, 1));
                    break;
                case "PLANETS":
                    maxBox = levelNode.get("maxBox").asInt();
                    setTiles.add(new SpecialStorage(TileType.SPECIAL_STORAGE, pos, RotationType.NORTH, 1, maxBox));
                    break;
                case "WARZONE1":
                    maxBox = levelNode.get("maxBox").asInt();
                    setTiles.add(new Storage(TileType.STORAGE, pos, RotationType.NORTH, 1, maxBox));
                    break;
                case "WARZONE2":
                    color = levelNode.get("color").asText();
                    cabinStartPlayer.put(color, new Cabin(TileType.CABIN, pos, RotationType.NORTH, 1));
                    break;
            }
        }
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
