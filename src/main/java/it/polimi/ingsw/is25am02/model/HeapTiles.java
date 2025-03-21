package it.polimi.ingsw.is25am02.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HeapTiles {
    private final Set<Tile> setTiles;
    private final Random random;
    private final String JSON_FILE_PATH = "src/main/resources/json/tiles.json";
    private HashMap<String, Cabin> cabinStartPlayer;

    public HeapTiles() {
        this.setTiles = new HashSet<>();
        this.random = new Random();
        cabinStartPlayer = new HashMap<>();
        loadTiles();
    }

    public Set<Tile> getSetTiles() {
        return setTiles;
    }

    //todo: sistema gli ID, per ora li hai messi a 1 per test
    private void loadTiles() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(new File(JSON_FILE_PATH));
        } catch (IOException e) {
            System.out.println("Error in reading JSON file");
            e.printStackTrace();
        }

        ConnectorType[] pos = new ConnectorType[4];
        JsonNode connections;
        int maxBox;
        int maxBattery;
        JsonNode shildedNode;
        boolean[] shilded;
        String color;
        for (JsonNode levelNode : rootNode) {  //foreach JSON node
            connections = levelNode.get("connectors");

            pos[0] = ConnectorType.getConnectorTypeByNum(connections.get("NORTH").asInt());
            pos[1] = ConnectorType.getConnectorTypeByNum(connections.get("EAST").asInt());
            pos[2] = ConnectorType.getConnectorTypeByNum(connections.get("SOUTH").asInt());
            pos[3] = ConnectorType.getConnectorTypeByNum(connections.get("WEST").asInt());

            switch (levelNode.get("type").asText()) {
                case "SHIELD":
                    shildedNode = levelNode.get("isShield");
                    shilded = new boolean[4];
                    shilded[0] = shildedNode.get("NORTH").asBoolean();
                    shilded[1] = shildedNode.get("EAST").asBoolean();
                    shilded[2] = shildedNode.get("SOUTH").asBoolean();
                    shilded[3] = shildedNode.get("WEST").asBoolean();
                    setTiles.add(new Shield(TileType.SHIELD, pos, RotationType.NORTH, 1, shilded));
                    break;
                case "STRUCTURAL":
                    setTiles.add(new Structural(TileType.STRUCTURAL, pos, RotationType.NORTH, 1));
                    break;
                case "BATTERY":
                    maxBattery = levelNode.get("maxBattery").asInt();
                    setTiles.add(new BatteryStorage(TileType.BATTERY, pos, RotationType.NORTH, 1, maxBattery));
                    break;
                case "BROWN_CABIN":
                    setTiles.add(new BrownCabin(TileType.BROWN_CABIN, pos, RotationType.NORTH, 1));
                    break;
                case "PURPLE_CABIN":
                    setTiles.add(new PurpleCabin(TileType.PURPLE_CABIN, pos, RotationType.NORTH, 1));
                    break;
                case "CABIN":
                    setTiles.add(new Cabin(TileType.CABIN, pos, RotationType.NORTH, 1));
                    break;
                case "CANNON":
                    setTiles.add(new Cannon(TileType.CANNON, pos, RotationType.NORTH, 1));
                    break;
                case "D_CANNON":
                    setTiles.add(new DoubleMotor(TileType.D_CANNON, pos, RotationType.NORTH, 1));
                    break;
                case "MOTOR":
                    setTiles.add(new Motors(TileType.MOTOR, pos, RotationType.NORTH, 1));
                    break;
                case "D_MOTOR":
                    setTiles.add(new DoubleMotor(TileType.D_MOTOR, pos, RotationType.NORTH, 1));
                    break;
                case "SPECIAL_STORAGE":
                    maxBox = levelNode.get("maxBox").asInt();
                    setTiles.add(new SpecialStorage(TileType.SPECIAL_STORAGE, pos, RotationType.NORTH, 1, maxBox));
                    break;
                case "STORAGE":
                    maxBox = levelNode.get("maxBox").asInt();
                    setTiles.add(new Storage(TileType.STORAGE, pos, RotationType.NORTH, 1, maxBox));
                    break;
                case "COLORED_CABIN":
                    color = levelNode.get("color").asText();
                    cabinStartPlayer.put(color, new Cabin(TileType.CABIN, pos, RotationType.NORTH, 1));
                    break;
            }
        }
    }

    public Tile drawTile() {
        int index = random.nextInt(setTiles.size()); // Seleziona un indice casuale
        Iterator<Tile> iterator = setTiles.iterator();

        for (int i = 0; i < index; i++) {
            iterator.next(); // Itera fino alla tessera scelta
        }

        Tile drawnTile = iterator.next();
        setTiles.remove(drawnTile); // Rimuove la tessera estratta
        return drawnTile;
    }

    public Set<Tile> getVisibleTiles() {
        Set<Tile> visibleTiles = new HashSet<>();
        for (Tile tile : setTiles) {
            if (tile.isVisible()) {
                visibleTiles.add(tile);

            }
        }
        return visibleTiles;
    }

    public void removeVisibleTile(Tile t) {
        setTiles.remove(t);
    }

    public void addTile(Tile t, boolean visible) {
        if (visible)
            t.setVisible();
        setTiles.add(t);
    }
}
