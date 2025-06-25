package it.polimi.ingsw.is25am02.view.tui.utils;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.view.ConsoleClient;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.CardV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.GameV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicPrinter {
    private GameV game;
    private String myName;
    private ConsoleClient console;

    // Codici ANSI
    private final String RESET = "\u001B[0m";
    private final String ROSSO = "\u001B[31m";
    private final String VERDE = "\u001B[32m";
    private final String VERDE_FOGLIA = "\u001B[38;5;28m";
    private final String GIALLO = "\u001B[33m";
    private final String BLU = "\u001B[34m";
    private final String MAGENTA = "\u001B[35m";
    private final String CIANO = "\u001B[36m";
    private final String BIANCO = "\u001B[37m";
    private final String MARRONE = "\u001B[38;5;130m";
    private final String ROSA = "\u001B[38;5;217m";
    private final String GRIGIO = "\u001B[38;5;245m";

    public GraphicPrinter(ConsoleClient console) {
        this.console = console;
        this.game = null;
    }

    public void setGame(GameV game) {
        this.game = game;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public void print() {
        clearConsole();
        printRules();
        if (game.getCurrentState().getPhase() == StateGameType.BUILD) {
            printCurrentSpaceship(myName);
            printSpaceship(myName);
            printCurrentTile(myName);
            if (game.getLevel() != 0) printBookedTile(myName);
            printTileOrientation(myName);
        } else if (game.getCurrentState().getPhase() == StateGameType.CHECK || game.getCurrentState().getPhase() == StateGameType.CORRECTION) {
            printCurrentSpaceship(myName);
            printSpaceship(myName);
            printTileOrientation(myName);
        } else if (game.getCurrentState().getPhase() == StateGameType.RESULT) {
        } else {
            printCurrentSpaceship(myName);
            printSpaceship(myName);
            printTileOrientation(myName);
            printTileOccupation(myName);
            System.out.println();
            printMyState(myName);
            printGameStatus();
            printCardComment();
        }
    }

    //to print the spaceship of someone else, call printCurrentSpaceship with the name of the player and printSpaceship

    public void printCurrentSpaceship(String myName) {
        System.out.print("Current spaceship: ");
        for (PlayerV p : game.getPlayers()) {
            if (p.getNickname().equals(myName)) {
                System.out.printf(" %s ", CIANO + p.getNickname() + RESET);
            } else {
                System.out.printf(" %s ", p.getNickname());
            }
        }
        System.out.println();
    }

    public void printWinners(Map<String, Integer> winners) {
        System.out.println("Ranking:");
        for(Map.Entry<String, Integer> pos : winners.entrySet()){
            System.out.println(pos.getKey() + " has " + pos.getValue() + " points");
        }
        /*
        for (PlayerV p : game.getPlayers()) {
            if (p.getNickname().equals(myName)) {
                System.out.print("Position: \n");
                for (String nick : game.getWinners().keySet()) {
                    System.out.println(nick + " has " + game.getWinners().get(nick) + " points");

                }
                return;
            }
        }*/
    }

    public void printMyState(String name) {
        PlayerV pl = null;

        for (PlayerV player : game.getPlayers()) {
            if (player.getNickname().equals(name)) {
                pl = player;
                break;
            }
        }

        if (pl == null) {
            return;
        }

        System.out.println("Your Status | Cosmic points: " + pl.getCredits() + " | State: " + pl.getStatePlayer());
    }

    public void printGameStatus() {
        System.out.println("Game Status | Game phase: " + game.getCurrentState().getPhase() + "  | Card: " + game.getCurrentState().getCurrentCard().getCardType()
                + " in state " + game.getCurrentState().getCurrentCard().getStateCard() + "  | Player's turn : " + game.getCurrentState().getCurrentPlayer().getNickname());
    }

    public void printCardComment() {
        System.out.println("Card Comment | Comment: " + game.getCurrentState().getCurrentCard().getComment());
        if (game.getCurrentState().getCurrentCard().getCardType() == CardType.METEORITES_STORM || (game.getCurrentState().getCurrentCard().getCardType() == CardType.WARZONE1) || game.getCurrentState().getCurrentCard().getCardType() == CardType.WARZONE2) { //todo mettere le altre carte
            if (game.getDiceV().getResult() != 0) {
                System.out.println("Dice result: " + game.getDiceV().getResult());
            } else {
                System.out.println("Dice result: --");
            }
        }
    }

    public void printDeck(int num) {
        clearConsole();
        List<CardV> cards = game.getDeck().getDeck().get(num);
        System.out.println("Deck:");
        for (CardV card : cards) {
            System.out.println(card.getCardType() + " | " + card.getComment());
        }
    }

    public void printSpaceship(String name) {
        final int START_COL = 3;
        final int END_COL = 11;
        final int START_ROW = 4;
        final int END_ROW = 9;
        PlayerV pl = null;

        for (PlayerV player : game.getPlayers()) {
            if (player.getNickname().equals(name)) {
                pl = player;
                break;
            }
        }

        if (pl == null) {
            return;
        }

        //Column numbers
        System.out.print("      ");
        for (int j = START_COL; j <= END_COL; j++) {
            if (j < 10) System.out.printf("  %d   ", j);
            else System.out.printf(" %d   ", j);
        }
        System.out.println();

        //Border
        System.out.print("     +");
        for (int j = START_COL; j <= END_COL; j++) {
            System.out.print("-----+");
        }
        System.out.println();

        //Main loop
        for (int i = START_ROW; i <= END_ROW; i++) {
            // First line (top connectors)
            System.out.print("     |");
            for (int j = START_COL; j <= END_COL; j++) {
                if (pl.getSpaceshipBoard()[i][j].isEmpty()) {
                    System.out.print("     |");
                } else {
                    char northConnector = connectorSymbol(pl.getSpaceshipBoard()[i][j].get(), RotationType.NORTH);
                    System.out.printf("  %c  |", northConnector);
                }
            }
            System.out.println();

            //Second line (side connectors and type tile)
            System.out.printf("  %2d |", i);
            for (int j = START_COL; j <= END_COL; j++) {
                if (pl.getSpaceshipMask()[i][j]) {
                    if (pl.getSpaceshipBoard()[i][j].isEmpty()) {
                        System.out.print("     |");
                    } else {
                        char westConnector = connectorSymbol(pl.getSpaceshipBoard()[i][j].get(), RotationType.WEST);
                        String tile = stringTile(pl, i, j);
                        char eastConnector = connectorSymbol(pl.getSpaceshipBoard()[i][j].get(), RotationType.EAST);
                        System.out.printf(" %c%s%c |", westConnector, tile, eastConnector);
                    }
                } else {
                    System.out.printf("  %s  |", GRIGIO + "0" + RESET);
                }
            }
            System.out.println();

            //Third line (bottom connectors)
            System.out.print("     |");
            for (int j = START_COL; j <= END_COL; j++) {
                if (pl.getSpaceshipBoard()[i][j].isEmpty()) {
                    System.out.print("     |");
                } else {
                    char southConnector = connectorSymbol(pl.getSpaceshipBoard()[i][j].get(), RotationType.SOUTH);
                    System.out.printf("  %c  |", southConnector);
                }
            }
            System.out.println();

            //Horizontal line
            System.out.print("     +");
            for (int j = START_COL; j <= END_COL; j++) {
                System.out.print("-----+");
            }
            System.out.println();
        }
    }

    public void printCurrentTile(String name) {
        PlayerV pl = null;
        char northConnector = ' ';
        char westConnector = ' ';
        char southConnector = ' ';
        char eastConnector = ' ';
        String tile = " ";

        for (PlayerV player : game.getPlayers()) {
            if (player.getNickname().equals(name)) {
                pl = player;
                break;
            }
        }
        if (pl == null) {
            return;
        }

        System.out.print("Current tile with Rotation: ");
        if (pl.getCurrentTile().isPresent()) {
            TileV curr = pl.getCurrentTile().get();
            System.out.println(curr.getRotationType());
            northConnector = connectorSymbol(curr, RotationType.NORTH);
            westConnector = connectorSymbol(curr, RotationType.WEST);
            southConnector = connectorSymbol(curr, RotationType.SOUTH);
            eastConnector = connectorSymbol(curr, RotationType.EAST);
            tile = stringCurr(curr);
        } else {
            System.out.println(" - ");
        }

        System.out.println("+-----+");
        System.out.printf("|  %c  |\n", northConnector);
        System.out.printf("| %c%s%c |\n", westConnector, tile, eastConnector);
        System.out.printf("|  %c  |\n", southConnector);
        System.out.println("+-----+");
    }

    public void printBookedTile(String name) {
        PlayerV pl = null;

        for (PlayerV player : game.getPlayers()) {
            if (player.getNickname().equals(name)) {
                pl = player;
                break;
            }
        }
        if (pl == null) {
            return;
        }

        HashMap<Integer, TileV> bookedTiles = pl.getBookedTiles();

        System.out.print("Booked tile with Rotation: ");
        if (bookedTiles.get(1) == null) System.out.print(" - ");
        else {
            System.out.print(bookedTiles.get(1).getRotationType() + " ");
        }
        if (bookedTiles.get(2) == null) System.out.print(" - ");
        else {
            System.out.print(bookedTiles.get(2).getRotationType());
        }
        System.out.println();

        for (int j = 1; j <= 2; j++) {
            System.out.printf("   %d  ", j);
        }
        System.out.println();

        System.out.print("+");
        for (int j = 1; j <= 2; j++) {
            System.out.print("-----+");
        }
        System.out.println();
        System.out.print("|");
        for (int j = 1; j <= 2; j++) {
            if (bookedTiles.get(j) == null) {
                System.out.print("     |");
            } else {
                char northConnector = connectorSymbol(bookedTiles.get(j), RotationType.NORTH);
                System.out.printf("  %c  |", northConnector);
            }
        }
        System.out.println();
        System.out.print("|");
        for (int j = 1; j <= 2; j++) {
            if (bookedTiles.get(j) == null) {
                System.out.print("     |");
            } else {
                char westConnector = connectorSymbol(bookedTiles.get(j), RotationType.WEST);
                char eastConnector = connectorSymbol(bookedTiles.get(j), RotationType.EAST);
                String tile = stringCurr(bookedTiles.get(j));
                System.out.printf(" %c%s%c |", westConnector, tile, eastConnector);
            }
        }
        System.out.println();
        System.out.print("|");
        for (int j = 1; j <= 2; j++) {
            if (bookedTiles.get(j) == null) {
                System.out.print("     |");
            } else {
                char southConnector = connectorSymbol(bookedTiles.get(j), RotationType.SOUTH);
                System.out.printf("  %c  |", southConnector);
            }
        }
        System.out.println();
        System.out.print("+");
        for (int j = 1; j <= 2; j++) {
            System.out.print("-----+");
        }
        System.out.println();
    }

    public void printTileOrientation(String name) {
        PlayerV pl = null;
        for (PlayerV player : game.getPlayers()) {
            if (player.getNickname().equals(name)) {
                pl = player;
            }
        }
        if (pl == null) {
            return;
        }
        System.out.println("Tile orientations: (Row,Column) Direction");

        HashMap<Coordinate, TileV> engines = new HashMap<>();
        HashMap<Coordinate, TileV> cannons = new HashMap<>();
        HashMap<Coordinate, TileV> shields = new HashMap<>();

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (pl.getSpaceshipBoard()[i][j].isPresent()) {
                    switch (pl.getSpaceshipBoard()[i][j].get().getType()) {
                        case MOTOR, D_MOTOR -> engines.put(new Coordinate(i, j), pl.getSpaceshipBoard()[i][j].get());
                        case CANNON, D_CANNON -> cannons.put(new Coordinate(i, j), pl.getSpaceshipBoard()[i][j].get());
                        case SHIELD -> shields.put(new Coordinate(i, j), pl.getSpaceshipBoard()[i][j].get());
                    }
                }
            }
        }
        StateGameType phase = game.getCurrentState().getPhase();

        if (phase == StateGameType.BUILD || phase == StateGameType.CHECK || phase == StateGameType.CORRECTION) {
            System.out.print("Engines:  ");
            for (Map.Entry<Coordinate, TileV> entry : engines.entrySet()) {
                System.out.printf("(%d,%d) %s\t  ", entry.getKey().x(), entry.getKey().y(), entry.getValue().getRotationType());
            }
            System.out.println();
        }
        System.out.print("Cannons:  ");
        for (Map.Entry<Coordinate, TileV> entry : cannons.entrySet()) {
            System.out.printf("(%d,%d) %s\t  ", entry.getKey().x(), entry.getKey().y(), entry.getValue().getRotationType());
        }
        System.out.println();
        System.out.print("Shields:  ");
        for (Map.Entry<Coordinate, TileV> entry : shields.entrySet()) {
            StringBuilder sb = new StringBuilder();
            switch (entry.getValue().getRotationType()) {
                case NORTH -> sb.append("N-E");
                case WEST -> sb.append("N-W");
                case EAST -> sb.append("S-E");
                case SOUTH -> sb.append("S-W");
            }
            System.out.printf("(%d,%d) %s\t  ", entry.getKey().x(), entry.getKey().y(), sb.toString());
        }
        System.out.println();
    }

    public void printTileOccupation(String name) {
        PlayerV pl = null;
        for (PlayerV player : game.getPlayers()) {
            if (player.getNickname().equals(name)) {
                pl = player;
            }
        }
        if (pl == null) {
            return;
        }
        System.out.println("Tile Occupation: (Row,Column) | Quantity | Type ");

        HashMap<Coordinate, TileV> batteries = new HashMap<>();
        HashMap<Coordinate, TileV> cabins = new HashMap<>();
        HashMap<Coordinate, TileV> storage = new HashMap<>();

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (pl.getSpaceshipBoard()[i][j].isPresent()) {
                    switch (pl.getSpaceshipBoard()[i][j].get().getType()) {
                        case BATTERY -> batteries.put(new Coordinate(i, j), pl.getSpaceshipBoard()[i][j].get());
                        case CABIN -> cabins.put(new Coordinate(i, j), pl.getSpaceshipBoard()[i][j].get());
                        case SPECIAL_STORAGE, STORAGE ->
                                storage.put(new Coordinate(i, j), pl.getSpaceshipBoard()[i][j].get());
                    }
                }
            }
        }

        int maxOnLine = 0;
        System.out.print("Storage:  \t");
        for (Map.Entry<Coordinate, TileV> entry : storage.entrySet()) {
            if (maxOnLine < 4) {
                System.out.printf(" (%d,%d) | %d/%d | ", entry.getKey().x(), entry.getKey().y(), countBox(entry.getValue()), entry.getValue().getNumMaxBox());
                System.out.print(printItem(entry.getValue()) + "\t");
                maxOnLine++;
            } else {
                System.out.println();
                System.out.print("          ");
                maxOnLine = 0;
            }
        }

        maxOnLine = 0;
        System.out.println();
        System.out.print("Battery: \t");
        for (Map.Entry<Coordinate, TileV> entry : batteries.entrySet()) {
            if (maxOnLine < 4) {
                System.out.printf(" (%d,%d) | %d/%d | ", entry.getKey().x(), entry.getKey().y(), entry.getValue().getNumBattery(), entry.getValue().getNumMaxBattery());
                System.out.print(printItem(entry.getValue()) + "\t");
                maxOnLine++;
            } else {
                System.out.println();
                System.out.print("          ");
                maxOnLine = 0;
            }
        }

        maxOnLine = 0;
        System.out.println();
        System.out.print("Crew:     \t");
        for (Map.Entry<Coordinate, TileV> entry : cabins.entrySet()) {
            if (maxOnLine < 4) {
                if (entry.getValue().getNumPAliens() > 0) {
                    System.out.printf(" (%d,%d) | %d/%d | ", entry.getKey().x(), entry.getKey().y(), entry.getValue().getNumPAliens(), 1);
                } else if (entry.getValue().getNumBAliens() > 0) {
                    System.out.printf(" (%d,%d) | %d/%d | ", entry.getKey().x(), entry.getKey().y(), entry.getValue().getNumBAliens(), 1);
                } else {
                    System.out.printf(" (%d,%d) | %d/%d | ", entry.getKey().x(), entry.getKey().y(), entry.getValue().getNumHumans(), 2);
                }
                System.out.print(printItem(entry.getValue()) + "\t");
                maxOnLine++;
            } else {
                System.out.println();
                System.out.print("       ");
                maxOnLine = 0;
            }
        }
        System.out.println();
    }

    public void printHeapTiles() {
        List<TileV> tiles = game.getHeapTilesV().getListTileV();
        int num = 0;
        int index;

        while (num <= tiles.size()) {
            index = num;
            for (int j = 1; j <= 10; j++) {
                if (num < 10) System.out.printf("   %d    ", num);
                else if (num < 100) System.out.printf("  %d    ", num);
                else System.out.printf("  %d   ", num);
                num++;
            }
            System.out.println();
            for (int j = 1; j <= 10; j++) {
                System.out.print("+-----+ ");
            }
            System.out.println();
            for (int j = 1; j <= 10; j++) {
                if (index >= tiles.size() || tiles.get(index) == null) {
                    System.out.print("|     | ");
                } else {
                    char northConnector = connectorSymbol(tiles.get(index), RotationType.NORTH);
                    System.out.printf("|  %c  | ", northConnector);
                }
                index++;
            }
            System.out.println();
            index = num - 10;
            for (int j = 1; j <= 10; j++) {
                if (index >= tiles.size() || tiles.get(index) == null) {
                    System.out.print("|     | ");
                } else {
                    char westConnector = connectorSymbol(tiles.get(index), RotationType.WEST);
                    char eastConnector = connectorSymbol(tiles.get(index), RotationType.EAST);
                    String tile = stringCurr(tiles.get(index));
                    System.out.printf("| %c%s%c | ", westConnector, tile, eastConnector);
                }
                index++;
            }
            System.out.println();
            index = num - 10;
            for (int j = 1; j <= 10; j++) {
                if (index >= tiles.size() || tiles.get(index) == null) {
                    System.out.print("|     | ");
                } else {
                    char southConnector = connectorSymbol(tiles.get(index), RotationType.SOUTH);
                    System.out.printf("|  %c  | ", southConnector);
                }
                index++;
            }
            System.out.println();
            for (int j = 1; j <= 10; j++) {
                System.out.print("+-----+ ");
            }
            System.out.println();
        }
    }

    private int countBox(TileV tile) {
        return tile.getNumBlueBox() + tile.getNumRedBox() + tile.getNumGreenBox() + tile.getNumYellowBox();
    }

    private String printItem(TileV tile) {
        StringBuilder sb = new StringBuilder();

        if (tile.getType() == TileType.BATTERY) {
            int num = tile.getNumBattery();
            sb.append((VERDE + "#" + RESET).repeat(Math.max(0, num)));
            return sb.toString();
        } else if (tile.getType() == TileType.CABIN) {
            int crew = tile.getNumHumans();
            int pAlien = tile.getNumPAliens();
            int bAlien = tile.getNumBAliens();

            if (crew == 2) {
                sb.append(BIANCO + "#" + RESET + BIANCO + "#" + RESET);
                return sb.toString();
            } else if (crew == 1) {
                sb.append(BIANCO + "#" + RESET);
                return sb.toString();
            } else if (pAlien == 1) {
                sb.append(MAGENTA + "#" + RESET);
                return sb.toString();
            } else if (bAlien == 1) {
                sb.append(MARRONE + "#" + RESET);
                return sb.toString();
            } else return "";
        } else {
            int blue = tile.getNumBlueBox();
            int red = tile.getNumRedBox();
            int green = tile.getNumGreenBox();
            int yellow = tile.getNumYellowBox();

            sb.append((BLU + "#" + RESET).repeat(Math.max(0, blue)));
            sb.append((ROSSO + "#" + RESET).repeat(Math.max(0, red)));
            sb.append((VERDE + "#" + RESET).repeat(Math.max(0, green)));
            sb.append((GIALLO + "#" + RESET).repeat(Math.max(0, yellow)));
            return sb.toString();
        }
    }

    private char connectorSymbol(TileV tile, RotationType direction) {
        ConnectorType[] connectors = tile.getConnectors();
        RotationType rotation = tile.getRotationType();

        int index = (direction.getNum() - rotation.getNum() + 4) % 4;
        ConnectorType connector = connectors[index];
        switch (connector) {
            case SINGLE -> {
                return '·';
            }
            case DOUBLE -> {
                return ':';
            }
            case UNIVERSAL -> {
                return '!';
            }
            default -> {
                return ' ';
            }
        }
    }

    private String stringTile(PlayerV player, int x, int y) {
        if (x == 7 && y == 7 && player.getSpaceshipBoard()[x][y].isPresent() && player.getSpaceshipBoard()[x][y].get().getType() == TileType.CABIN) {
            PlayerColor playerColor = player.getColor();
            switch (playerColor) {
                case RED -> {
                    return ROSSO + "C" + RESET;
                }
                case BLUE -> {
                    return BLU + "C" + RESET;
                }
                case GREEN -> {
                    return VERDE + "C" + RESET;
                }
                case YELLOW -> {
                    return GIALLO + "C" + RESET;
                }
                default -> {
                    return BIANCO + "C" + RESET;
                }
            }
        } else {
            TileV tile = player.getSpaceshipBoard()[x][y].get();
            return getString(tile);
        }
    }

    private String stringCurr(TileV tile) {
        return getString(tile);
    }

    private String getString(TileV tile) {
        switch (tile.getType()) {
            case CABIN -> {
                return BIANCO + "C" + RESET;
            }
            case CANNON -> {
                return MAGENTA + "G" + RESET;
            }
            case D_CANNON -> {
                return VERDE_FOGLIA + "G" + RESET;
            }
            case MOTOR -> {
                return MARRONE + "M" + RESET;
            }
            case D_MOTOR -> {
                return VERDE_FOGLIA + "M" + RESET;
            }
            case STRUCTURAL -> {
                return BIANCO + "#" + RESET;
            }
            case SHIELD -> {
                return VERDE_FOGLIA + "S" + RESET;
            }
            case BROWN_CABIN -> {
                return MARRONE + "O" + RESET;
            }
            case PURPLE_CABIN -> {
                return MAGENTA + "O" + RESET;
            }
            case BATTERY -> {
                return VERDE + "E" + RESET;
            }
            case STORAGE -> {
                if (tile.getNumMaxBox() == 2) return CIANO + "D" + RESET;
                else return CIANO + "T" + RESET;
            }
            case SPECIAL_STORAGE -> {
                if (tile.getNumMaxBox() == 2) return ROSA + "D" + RESET;
                else return ROSA + "S" + RESET;
            }
            default -> {
                return RESET;
            }
        }
    }

    private void clearConsole() {
        try {
            for (int i = 0; i < 7; i++) {
                System.out.print("\n");
            }
            /*
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
             */
        } catch (Exception e) {
            console.reportError("error.terminal", null);
        }
    }

    public void printGameboard() {
        System.out.println("Gameboard with spaces: " + game.getGlobalBoard().getNumstep());
        for (int i = 0; i < game.getPlayers().size(); i++) {
            int step = game.getGlobalBoard().getPosition(game.getPlayers().get(i));
            if (step >= game.getGlobalBoard().getNumstep()) {
                step = step % game.getGlobalBoard().getNumstep();
            }
            System.out.println(game.getPlayers().get(i).getNickname() + ": " + step);
        }
        System.out.println("If the leader doubles you during the effect of a card and at the end of the card\nyou are still in a double situation you are eliminated from the game" +
                "\nIMPORTANT WARNING: during the effect of a card the ranking is not updated, \nwhen it reaches the finish state the ranking situation is correct");
    }

    public void printRules() {
        if (game.getCurrentState().getPhase() == StateGameType.BUILD) {
            if (game.getLevel() == 0) {
                console.displayMessage("helper.build.0", null);
            } else {
                console.displayMessage("helper.build.1", null);
            }
        } else if (game.getCurrentState().getPhase() == StateGameType.CHECK) {
            console.displayMessage("helper.check", null);
        } else if (game.getCurrentState().getPhase() == StateGameType.CORRECTION) {
            console.displayMessage("helper.correction", null);
        } else if (game.getCurrentState().getPhase() == StateGameType.INITIALIZATION_SPACESHIP) {
            if (game.getLevel() == 0) {
                console.displayMessage("helper.initialization.0", null);
            } else {
                console.displayMessage("helper.initialization.1", null);
            }
        } else {
            console.displayMessage("helper.match", null);
        }
        System.out.println();
    }

    public void printLegend() {
        System.out.println("\nLegend:");
        System.out.println("Connectors : (!) Universal    (:) Double    (.) Single");
        System.out.println("Tiles : ");
        System.out.println(BIANCO + "#" + RESET + " Structural\n" + MAGENTA + "G" + RESET + " Single cannon");
        System.out.println(VERDE_FOGLIA + "G" + RESET + " Double cannon\n" + MARRONE + "M" + RESET + " Single engine");
        System.out.println(VERDE_FOGLIA + "M" + RESET + " Double engine\n" + BIANCO + "C" + RESET + " Cabin");
        System.out.println(ROSA + "S" + RESET + " Single special storage\n" + ROSA + "D" + RESET + " Double special storage");
        System.out.println(CIANO + "D" + RESET + " Double storage\n" + CIANO + "T" + RESET + " Triple storage");
        System.out.println(MAGENTA + "O" + RESET + " Purple cabin\n" + MARRONE + "O" + RESET + " Brown cabin");
        System.out.println(VERDE + "E" + RESET + " Battery storage\n" + VERDE_FOGLIA + "S" + RESET + " Shield");
    }

    public void printStatus() {
        System.out.println("\nStatus:");
        for (PlayerV i : game.getPlayers()) {
            System.out.println(i.getNickname() + " status : " + i.getStatePlayer());
        }
        System.out.println("Game : " + game.getCurrentState().getPhase());
    }

    public void printHelp() {
        PlayerV myself = null;
        if (game == null) {
            console.displayMessage("command.pregame", null);
        } else {
            if (game.getCurrentState().getPhase() == StateGameType.BUILD) {
                console.displayMessage("command.build", null);
            } else if (game.getCurrentState().getPhase() == StateGameType.CHECK) {
                console.displayMessage("command.check", null);
            } else if (game.getCurrentState().getPhase() == StateGameType.CORRECTION) {
                for (PlayerV player : game.getPlayers()) {
                    if (player.getNickname().equals(myName)) {
                        myself = player;
                        break;
                    }
                }
                if (myself != null && myself.getStatePlayer().equals(StatePlayerType.CORRECT_SHIP)) {
                    console.displayMessage("command.correction.true", null);
                } else {
                    console.displayMessage("command.correction.false", null);
                }
            } else if (game.getCurrentState().getPhase() == StateGameType.INITIALIZATION_SPACESHIP) {
                console.displayMessage("command.initial", null);
            } else if (game.getCurrentState().getPhase() == StateGameType.TAKE_CARD) {
                console.displayMessage("command.take", null);
            } else if (game.getCurrentState().getPhase() == StateGameType.EFFECT_ON_PLAYER) {
                switch (game.getCurrentState().getCurrentCard().getCardType()) {
                    case PLANET:
                        console.displayMessage("command.effect.planet", null);
                        break;
                    case ABANDONED_STATION:
                        console.displayMessage("command.effect.ab_station", null);
                        break;
                    case TRAFFICKER:
                        console.displayMessage("command.effect.trafficker", null);
                        break;
                    case ABANDONED_SHIP:
                        console.displayMessage("command.effect.ab_ship", null);
                        break;
                    case SLAVE_OWNER:
                        console.displayMessage("command.effect.slave_owner", null);
                        break;
                    case OPENSPACE:
                        console.displayMessage("command.effect.openspace", null);
                        break;
                    case METEORITES_STORM:
                        console.displayMessage("command.effect.meteorites_storm", null);
                        break;
                    default:
                        console.displayMessage("error.command", null);
                        break;
                }
            }
        }
    }
}
