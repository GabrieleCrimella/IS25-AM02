package it.polimi.ingsw.is25am02.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am02.model.cards.*;
import it.polimi.ingsw.is25am02.model.cards.boxes.*;
import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.CardType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.exception.AlreadyViewingException;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CardDeck {
    private final HashMap<Integer , Pair<List<Card>,Boolean>> deck; //l'intero è il numero di deck, il boolean è vero se è occupato
    private final List<Card> finalDeck;
    private final List<Card> initialDeck;
    private final BoxStore store;
    private int level;

    public CardDeck(int level){
        this.deck = new HashMap<>();
        this.finalDeck = new ArrayList<>();
        this.initialDeck = new ArrayList<>();
        this.store = new BoxStore();
        this.level = level;
        loadCard();
        if(level == 0){
            createFinalDeckTestFlight();
        } else createDecks();
    }

    public List<Card> getInitialDeck() {
        return initialDeck;
    }

    public List<Card> getFinalDeck() {
        return finalDeck;
    }

    public void createFinalDeckTestFlight() { //nel caso del livello di prova

        for (Card card : initialDeck) {
            if (card.getTestFlight()) {
                finalDeck.add(card);
            }

            Collections.shuffle(finalDeck);
        }
    }

    public void createDecks(){//il metodo crea i mazzetti usando initial deck in cui sono presenti tutte le carte

        List<Card> livello1 = new ArrayList<>();
        List<Card> livello2 = new ArrayList<>();

        for (Card card : initialDeck) {
            if (card.getLevel() == 1 ) {
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
            }

            if (!livello2.isEmpty()) {
                mazzetto.add(livello2.removeFirst());
                mazzetto.add(livello2.removeFirst());
            }
            deck.put(i, new Pair<>(mazzetto, false)); //di default metto a false l'occupazione del mazzetto
        }
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
        boolean testFlight;
        LinkedList<Box> boxesWon = new LinkedList<>();
        LinkedList<BoxType> boxesWonType = new LinkedList<>();
        ArrayList<Pair<Integer, RotationType>> shots = new ArrayList<>();
        ArrayList<Pair<Integer, RotationType>> meteorites = new ArrayList<>();
        ArrayList<ArrayList<Box>> planetOffers = new ArrayList<>();
        ArrayList<ArrayList<BoxType>> planetOffersTypes = new ArrayList<>();


        for (JsonNode levelNode : rootNode) {  //foreach JSON node
            level = levelNode.get("level").asInt();
            testFlight = levelNode.get("testFlight").asBoolean();

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
                    boxesWonType.clear();
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
                    shots.clear();
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
                    boxesWonType.clear();
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
                    meteorites.clear();
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
                    planetOffersTypes.clear();
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
                    shots.clear();
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
                    shots.clear();
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

    public void createFinalDeck(){
        for(Pair<List<Card>,Boolean> cards : deck.values()){
            finalDeck.addAll(cards.getKey());
        }
        Collections.shuffle(finalDeck);//mischio le carte
    }

    public List<Card> giveDeck(int numDeck) throws AlreadyViewingException {
        if(deck.get(numDeck).getValue()){
            throw new AlreadyViewingException("someone else is already looking at the deck " + numDeck);
        }
        else if(numDeck < 0 || numDeck > 2){
            throw new AlreadyViewingException("numDeck is out of bounds");
        }
        else {
            Pair<List<Card>, Boolean> pair = deck.get(numDeck);
            deck.put(numDeck, new Pair<>(pair.getKey(), true));
            return pair.getKey();
        }
    }

    public void returnDeck(int numDeck) throws AlreadyViewingException {
        if(numDeck < 0 || numDeck > 2){
            throw new AlreadyViewingException("numDeck is out of bounds");
        }
        else {
            deck.computeIfPresent(numDeck, (k, pair) -> new Pair<>(pair.getKey(), false));
        }
    }

    public Card playnextCard(Game game){//deve prendere la prossima carta da final deck
        Card nextCard;
        //se la carta corrente è una di quelle con le box metto gli scarti in store
        if(finalDeck.getFirst().getCardType().equals(CardType.ABANDONED_STATION) || finalDeck.getFirst().getCardType().equals(CardType.TRAFFICKER)){
            if(!finalDeck.getFirst().getBoxesWon().isEmpty()){
                for(Box box : finalDeck.getFirst().getBoxesWon()){
                    store.addBox(box);
                }
                finalDeck.getFirst().clearBoxWon();
            }
        } else if (finalDeck.getFirst().getCardType().equals(CardType.PLANET)) {
            if(!( finalDeck.getFirst()).getPlanetOffers().isEmpty()){
                for(ArrayList<Box> boxlist : finalDeck.getFirst().getPlanetOffers()){
                    for(Box box : boxlist){
                        store.addBox(box);
                    }
                }
                finalDeck.getFirst().clearPlanetOffers();
            }
        }
        finalDeck.removeFirst();
        if(finalDeck.isEmpty()){
            return null;
        }
        nextCard = finalDeck.getFirst();
        game.getCurrentState().setCurrentCard(nextCard);


        //only 1 player is IN_GAME, he skips WarZone
        if(game.getGameboard().getPositions().size() == 1){
            while(nextCard.getCardType() == CardType.WARZONE1 || nextCard.getCardType() == CardType.WARZONE2){
                finalDeck.removeFirst();
                if(finalDeck.isEmpty()){
                    return null;
                }
                nextCard = finalDeck.getFirst();
            }
        }

        //se la prossima carta è una di quelle con i box allora devo riempirla con i box effettivi
        if(nextCard.getCardType().equals(CardType.ABANDONED_STATION) || nextCard.getCardType().equals(CardType.TRAFFICKER) ){
            for(BoxType boxType : nextCard.getBoxesWonTypes()){
                if(!store.getStore().isEmpty()){
                    Box box;
                    if(boxType.equals(BoxType.RED)){
                        if(store.getStore().containsKey(BoxType.RED)){
                            box = new RedBox(BoxType.RED);
                            nextCard.addBoxWon(box);
                        }
                    } else if(boxType.equals(BoxType.BLUE)){
                        if(store.getStore().containsKey(BoxType.BLUE)){
                            box = new BlueBox(BoxType.BLUE);
                            nextCard.addBoxWon(box);
                        }
                    } else if(boxType.equals(BoxType.GREEN)){
                        if(store.getStore().containsKey(BoxType.GREEN)){
                            box = new GreenBox(BoxType.GREEN);
                            nextCard.addBoxWon(box);
                        }
                    } else if(boxType.equals(BoxType.YELLOW)){
                        if(store.getStore().containsKey(BoxType.YELLOW)){
                            box = new YellowBox(BoxType.YELLOW);
                            nextCard.addBoxWon(box);
                        }
                    } else throw new IllegalArgumentException("I cannot add a box");
                }
            }
        }
        return nextCard;
    }
}
