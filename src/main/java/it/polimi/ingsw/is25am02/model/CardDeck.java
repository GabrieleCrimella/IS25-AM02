package it.polimi.ingsw.is25am02.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am02.model.cards.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.*;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CardDeck {
    private HashMap<Integer , Pair<List<Card>,Boolean>> deck; //l'intero è il numero di deck, il boolean è vero se è occupato
    private List<Card> finalDeck;
    private List<Card> initialDeck;
    private BoxStore store;

    public CardDeck(){
        this.deck = new HashMap<>();
        this.finalDeck = new ArrayList<>();
        this.initialDeck = new ArrayList<>();
        this.store = new BoxStore();
        loadCard();
    }

    public List<Card> getInitialDeck() {
        return initialDeck;
    }

    public HashMap<Integer , Pair<List<Card>,Boolean>> createDecks(){//il metodo crea i mazzetti usando initial deck in cui sono presenti tutte le carte
        HashMap<Integer , Pair<List<Card>,Boolean>> deck = new HashMap<>();

        List<Card> livello1 = new ArrayList<>();
        List<Card> livello2 = new ArrayList<>();

        for (Card card : initialDeck) {
            if (card.getLevel() == 1) {
                livello1.add(card);
            } else if (card.getLevel() == 2) {
                livello2.add(card);
            }
        }

        Collections.shuffle(livello1);
        Collections.shuffle(livello2);

        for (int i = 0; i < 4; i++) {
            List<Card> mazzetto = new ArrayList<>();

            if (livello1.size() >= 2) {
                mazzetto.add(livello1.removeFirst());
                mazzetto.add(livello1.removeFirst());
            }

            if (!livello2.isEmpty()) {
                mazzetto.add(livello2.removeFirst());
            }

            deck.put(i, new Pair<>(mazzetto, false)); //di default metto a false l'occupazione del mazzetto
        }

        return deck;
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
        LinkedList<BoxType> boxesWonType = new LinkedList<>();
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        ArrayList<Pair<Integer, RotationType>> meteorites = new ArrayList<>();
        ArrayList<ArrayList<Box>> planetOffers = new ArrayList<>();
        ArrayList<ArrayList<BoxType>> planetOffersTypes = new ArrayList<>();


        for (JsonNode levelNode : rootNode) {  //foreach JSON node
            level = levelNode.get("level").asInt();

            switch (levelNode.get("type").asText()) {
                case "ABANDONED_SHIP":
                    aliveLost = levelNode.get("aliveLost").asInt();
                    creditWin = levelNode.get("creditWin").asInt();
                    daysLost = levelNode.get("daysLost").asInt();
                    initialDeck.add(new AbbandonedShip(level, aliveLost, creditWin, daysLost));
                    break;
                case "ABANDONED_STATION":
                    aliveNeeded = levelNode.get("aliveNeeded").asInt();
                    daysLost = levelNode.get("daysLost").asInt();
                    for(JsonNode node: levelNode.get("box")){
                        BoxType box;
                        if(node.asText().equals("RED")){
                            box = BoxType.RED;
                        } else if(node.asText().equals("BLUE")){
                            box = BoxType.BLUE;
                        } else if(node.asText().equals("YELLOW")){
                            box = BoxType.YELLOW;
                        } else if(node.asText().equals("GREEN")){
                            box = BoxType.GREEN;
                        } else throw new IllegalArgumentException("I cannot add a box");
                        boxesWonType.add(box);
                    }
                    initialDeck.add(new AbbandonedStation(level, store, aliveNeeded, daysLost,boxesWon, boxesWonType));
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
                    initialDeck.add(new Pirate(level, cannonPower, daysLost, creditWin, shots));
                    break;
                case "TRAFFICKER":
                    cannonPower = levelNode.get("cannonPower").asInt();
                    daysLost = levelNode.get("daysLost").asInt();
                    boxesLost = levelNode.get("boxesLost").asInt();
                    for(JsonNode node: levelNode.get("box")){
                        BoxType box;
                        if(node.asText().equals("RED")){
                            box = BoxType.RED;
                        } else if(node.asText().equals("BLUE")){
                            box = BoxType.BLUE;
                        } else if(node.asText().equals("YELLOW")){
                            box = BoxType.YELLOW;
                        } else if(node.asText().equals("GREEN")){
                            box = BoxType.GREEN;
                        } else throw new IllegalArgumentException("I cannot add a box");
                        boxesWonType.add(box);
                    }
                    initialDeck.add(new Trafficker(level,store,cannonPower, daysLost, boxesLost, boxesWon, boxesWonType));
                    break;
                case "SLAVEOWNER":
                    cannonPower = levelNode.get("cannonPower").asInt();
                    daysLost = levelNode.get("daysLost").asInt();
                    aliveLost = levelNode.get("aliveLost").asInt();
                    creditWin = levelNode.get("creditWin").asInt();
                    initialDeck.add(new SlaveOwner(level, cannonPower, daysLost,creditWin,aliveLost));
                    break;
                case "OPENSPACE":
                    initialDeck.add(new OpenSpace(level));
                    break;
                case "STARDUST":
                    initialDeck.add(new Stardust(level));
                    break;
                case "EPIDEMY":
                    initialDeck.add(new Epidemy(level));
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
                    initialDeck.add(new MeteoritesStorm(level,meteorites));
                    break;
                case "PLANETS":
                    daysLost = levelNode.get("daysLost").asInt();
                    for (JsonNode boxListNode : levelNode.get("boxes")) {
                        ArrayList<BoxType> boxList = new ArrayList<>();
                        for (JsonNode boxNode : boxListNode) {
                            BoxType box;
                            if(boxNode.asText().equals("RED")){
                                box = BoxType.RED;
                            } else if(boxNode.asText().equals("BLUE")){
                                box = BoxType.BLUE;
                            } else if(boxNode.asText().equals("YELLOW")){
                                box = BoxType.YELLOW;
                            } else if(boxNode.asText().equals("GREEN")){
                                box = BoxType.GREEN;
                            } else throw new IllegalArgumentException("I cannot add a box to planetoffer from JSON");
                            boxList.add(box);
                        }

                        planetOffersTypes.add(boxList); // Aggiungi la lista alla lista principale
                    }
                    initialDeck.add(new Planet(level,store,daysLost,planetOffers,planetOffersTypes));
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
                    initialDeck.add(new WarZone_I(level,daysLost,aliveLost, shots));
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
                    initialDeck.add(new WarZone_II(level,daysLost,boxesLost, shots));
                    break;
            }
        }
    }

    public List<Card> createFinalDeck(){
        finalDeck = new ArrayList<>();
        for(Pair<List<Card>,Boolean> cards : deck.values()){
            finalDeck.addAll(cards.getKey());
        }
        Collections.shuffle(finalDeck);//mischio le carte
        return finalDeck;
    }
    public Card giveCard(){
        return null;
    }
    public List<Card> giveDeck(int numDeck){//il deck viene occupato perchè è nelle mani di qualcun altro
        Pair<List<Card>, Boolean> pair = deck.get(numDeck);
        deck.put(numDeck, new Pair<>(pair.getKey(), true));
        return pair.getKey();
    }
    public void returnDeck(int numDeck){ //il deck viene liberato
        deck.computeIfPresent(numDeck, (k, pair) -> new Pair<>(pair.getKey(), false));
    }

    public Card playnextCard(){//deve prendere la prossima carta da final deck
        Card nextCard;
        //se la carta corrente è una di quelle con le box metto gli scarti in store
        if(finalDeck.getFirst() instanceof AbbandonedStation || finalDeck.getFirst() instanceof Trafficker){
            if(!finalDeck.getFirst().getBoxesWon().isEmpty()){
                for(Box box : finalDeck.getFirst().getBoxesWon()){
                    store.addBox(box);
                }
            }
        } else if (finalDeck.getFirst() instanceof Planet) {
            if(!( finalDeck.getFirst()).getPlanetOffers().isEmpty()){
                for(ArrayList<Box> boxlist : finalDeck.getFirst().getPlanetOffers()){
                    for(Box box : boxlist){
                        store.addBox(box);
                    }
                }
            }
        }
        nextCard = finalDeck.removeFirst();
        //se quello che sto rimuovendo è una di quelle con i box allora devo riempirle con i box effettivi
        if(nextCard instanceof AbbandonedStation || nextCard instanceof Trafficker){
            for(BoxType boxType : nextCard.getBoxesWonTypes()){
                Box box;
                if(boxType.equals(BoxType.RED)){
                    box = new RedBox(BoxType.RED);
                } else if(boxType.equals(BoxType.BLUE)){
                    box = new BlueBox(BoxType.BLUE);
                } else if(boxType.equals(BoxType.GREEN)){
                    box = new GreenBox(BoxType.GREEN);
                } else if(boxType.equals(BoxType.YELLOW)){
                    box = new YellowBox(BoxType.YELLOW);
                } else throw new IllegalArgumentException("I cannot add a box");
                nextCard.getBoxesWon().add(box);
            }
        } else if (nextCard instanceof Planet) {
            for (List<BoxType> boxTypeList : nextCard.getPlanetOffersTypes()) {
                ArrayList<Box> boxList = new ArrayList<>();
                for (BoxType type : boxTypeList) {
                    Box box;
                    if(type.equals(BoxType.RED)){
                        box = new RedBox(BoxType.RED);
                    } else if(type.equals(BoxType.BLUE)){
                        box = new RedBox(BoxType.BLUE);
                    } else if(type.equals(BoxType.YELLOW)){
                        box = new RedBox(BoxType.YELLOW);
                    } else if(type.equals(BoxType.GREEN)){
                        box = new RedBox(BoxType.GREEN);
                    } else throw new IllegalArgumentException("I cannot add a box to planetoffer");
                }
                nextCard.getPlanetOffers().add(boxList); // Aggiungi la lista alla lista principale
            }
        }
        return nextCard;

    }
}
