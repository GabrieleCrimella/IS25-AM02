package it.polimi.ingsw.is25am02.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am02.model.cards.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.*;
import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        }
        int level;
        int aliveLost;
        int aliveNeeded;
        int creditWin;
        int daysLost;
        int cannonPower;
        int boxesLost;
        LinkedList<Box> boxesWon = new LinkedList<>();
        BoxStore store = new BoxStore();
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        ArrayList<Pair<Integer, RotationType>> meteorites = new ArrayList<>();
        ArrayList<ArrayList<Box>> planetOffers = new ArrayList<>();

        for (JsonNode levelNode : rootNode) {  //foreach JSON node
            level = levelNode.get("level").asInt();

            switch (levelNode.get("type").asText()) {
                case "ABANDONED_SHIP":
                    aliveLost = levelNode.get("aliveLost").asInt();
                    creditWin = levelNode.get("creditWin").asInt();
                    daysLost = levelNode.get("daysLost").asInt();
                    deck.addCard(new AbbandonedShip(level, aliveLost, creditWin, daysLost));
                    break;
                case "ABANDONED_STATION":
                    aliveNeeded = levelNode.get("aliveNeeded").asInt();
                    daysLost = levelNode.get("daysLost").asInt();
                    for(JsonNode node: levelNode.get("box")){
                        Box box;
                        if(node.asText().equals("RED")){
                            box = new RedBox(BoxType.RED);
                        } else if(node.asText().equals("BLUE")){
                            box = new BlueBox(BoxType.BLUE);
                        } else if(node.asText().equals("YELLOW")){
                            box = new YellowBox(BoxType.YELLOW);
                        } else if(node.asText().equals("GREEN")){
                            box = new GreenBox(BoxType.GREEN);
                        } else throw new IllegalArgumentException("I cannot add a box");
                        boxesWon.add(box);
                    }
                    //todo store
                    deck.addCard(new AbbandonedStation(level, store, aliveNeeded, daysLost,boxesWon));
                    break;
                case "PIRATE":
                    cannonPower = levelNode.get("cannonPower").asInt();
                    creditWin = levelNode.get("creditWin").asInt();
                    daysLost = levelNode.get("daysLost").asInt();
                    for (JsonNode shotNode : levelNode.get("shots")) {
                        int smallOrBig = shotNode.get(0).asInt();
                        RotationType rotation;
                        if(shotNode.get(1).asText().equals("north")){
                            rotation = RotationType.NORTH;
                        } else if(shotNode.get(1).asText().equals("south")){
                            rotation = RotationType.SOUTH;
                        } else if(shotNode.get(1).asText().equals("east")) {
                            rotation = RotationType.EAST;
                        }else if(shotNode.get(1).asText().equals("west")){
                            rotation = RotationType.WEST;
                        } else throw new IllegalArgumentException("I cannot add a shot from JSON");
                        shots.add(new Pair<>(smallOrBig, rotation));
                    }
                    deck.addCard(new Pirate(level, cannonPower, daysLost, creditWin, shots));
                    break;
                case "TRAFFICKER":
                    cannonPower = levelNode.get("cannonPower").asInt();
                    daysLost = levelNode.get("daysLost").asInt();
                    boxesLost = levelNode.get("boxesLost").asInt();
                    for(JsonNode node: levelNode.get("box")){
                        Box box;
                        if(node.asText().equals("RED")){
                            box = new RedBox(BoxType.RED);
                        } else if(node.asText().equals("BLUE")){
                            box = new BlueBox(BoxType.BLUE);
                        } else if(node.asText().equals("YELLOW")){
                            box = new YellowBox(BoxType.YELLOW);
                        } else if(node.asText().equals("GREEN")){
                            box = new GreenBox(BoxType.GREEN);
                        } else throw new IllegalArgumentException("I cannot add a box");
                        boxesWon.add(box);
                    }
                    //todo store
                    deck.addCard(new Trafficker(level,store,cannonPower, daysLost, boxesLost, boxesWon));
                    break;
                case "SLAVEOWNER":
                    cannonPower = levelNode.get("cannonPower").asInt();
                    daysLost = levelNode.get("daysLost").asInt();
                    aliveLost = levelNode.get("aliveLost").asInt();
                    creditWin = levelNode.get("creditWin").asInt();
                    deck.addCard(new SlaveOwner(level, cannonPower, daysLost,creditWin,aliveLost));
                    break;
                case "OPENSPACE":
                    deck.addCard(new OpenSpace(level));
                    break;
                case "STARDUST":
                    deck.addCard(new Stardust(level));
                    break;
                case "EPIDEMY":
                    deck.addCard(new Epidemy(level));
                    break;
                case "METEORITES":
                    for (JsonNode node : levelNode.get("meteorites")) {
                        int smallOrBig = node.get(0).asInt();
                        RotationType rotation;
                        if(node.get(1).asText().equals("north")){
                            rotation = RotationType.NORTH;
                        } else if(node.get(1).asText().equals("south")){
                            rotation = RotationType.SOUTH;
                        } else if(node.get(1).asText().equals("east")) {
                            rotation = RotationType.EAST;
                        }else if(node.get(1).asText().equals("west")){
                            rotation = RotationType.WEST;
                        } else throw new IllegalArgumentException("I cannot add a meteorites from JSON");
                        meteorites.add(new Pair<>(smallOrBig, rotation));
                    }
                    deck.addCard(new MeteoritesStorm(level,meteorites));
                    break;
                case "PLANETS":
                    daysLost = levelNode.get("daysLost").asInt();
                    for (JsonNode boxListNode : levelNode.get("boxes")) {
                        ArrayList<Box> boxList = new ArrayList<>();
                        for (JsonNode boxNode : boxListNode) {
                            Box box;
                            if(boxNode.asText().equals("RED")){
                                box = new RedBox(BoxType.RED);
                            } else if(boxNode.asText().equals("BLUE")){
                                box = new BlueBox(BoxType.BLUE);
                            } else if(boxNode.asText().equals("YELLOW")){
                                box = new YellowBox(BoxType.YELLOW);
                            } else if(boxNode.asText().equals("GREEN")){
                                box = new GreenBox(BoxType.GREEN);
                            } else throw new IllegalArgumentException("I cannot add a box to planetoffer from JSON");
                            boxList.add(box);
                        }

                        planetOffers.add(boxList); // Aggiungi la lista alla lista principale
                    }
                    deck.addCard(new Planet(level,store,daysLost,planetOffers));
                    break;
                case "WARZONE1":
                    daysLost = levelNode.get("daysLost").asInt();
                    aliveLost = levelNode.get("aliveLost").asInt();
                    for (JsonNode shotNode : levelNode.get("shots")) {
                        int smallOrBig = shotNode.get(0).asInt();
                        RotationType rotation;
                        if(shotNode.get(1).asText().equals("north")){
                            rotation = RotationType.NORTH;
                        } else if(shotNode.get(1).asText().equals("south")){
                            rotation = RotationType.SOUTH;
                        } else if(shotNode.get(1).asText().equals("east")) {
                            rotation = RotationType.EAST;
                        }else if(shotNode.get(1).asText().equals("west")){
                            rotation = RotationType.WEST;
                        } else throw new IllegalArgumentException("I cannot add a shot from JSON");
                        shots.add(new Pair<>(smallOrBig, rotation));
                    }
                    deck.addCard(new WarZone_I(level,daysLost,aliveLost, shots));
                    break;
                case "WARZONE2":
                    daysLost = levelNode.get("daysLost").asInt();
                    boxesLost = levelNode.get("boxesLost").asInt();
                    for (JsonNode shotNode : levelNode.get("shots")) {
                        int smallOrBig = shotNode.get(0).asInt();
                        RotationType rotation;
                        if(shotNode.get(1).asText().equals("north")){
                            rotation = RotationType.NORTH;
                        } else if(shotNode.get(1).asText().equals("south")){
                            rotation = RotationType.SOUTH;
                        } else if(shotNode.get(1).asText().equals("east")) {
                            rotation = RotationType.EAST;
                        }else if(shotNode.get(1).asText().equals("west")){
                            rotation = RotationType.WEST;
                        } else throw new IllegalArgumentException("I cannot add a shot from JSON");
                        shots.add(new Pair<>(smallOrBig, rotation));
                    }
                    deck.addCard(new WarZone_II(level,daysLost,boxesLost, shots));
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
