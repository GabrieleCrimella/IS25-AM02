package it.polimi.ingsw.is25am02.view.tui.utils;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.GameV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.util.HashMap;
import java.util.Map;

public class GraphicPrinter {
    private GameV game;
    private String myName;

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

    public void setGame(GameV game) {
        this.game = game;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public void print() {
        if (game.getCurrentState().getPhase() == StateGameType.BUILD) {
            printCurrentSpaceship(myName);
            printSpaceship(myName);
            printCurrentTile(myName);
            if(game.getLevel() != 0) printBookedTile(myName);
            printTileOrientation(myName);
        } else if (game.getCurrentState().getPhase() == StateGameType.CHECK || game.getCurrentState().getPhase() == StateGameType.CORRECTION) {
            printCurrentSpaceship(myName);
            printSpaceship(myName);
            printTileOrientation(myName);
        } else {
            printCurrentSpaceship(myName);
            printSpaceship(myName);
            printTileOrientation(myName);
            //printTileOccupation(myName);
        }
    }

    public void printCurrentSpaceship(String myName) {
        System.out.print("Current spaceship: ");
        for( PlayerV p : game.getPlayers()) {
            if( p.getNickname().equals(myName)) {
                System.out.printf(" %s ", CIANO + p.getNickname() + RESET);
            } else {
                System.out.printf(" %s ", p.getNickname());
            }
        }
        System.out.println();
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

        //todo pensaci a come sistemare
        if (pl == null) {
            return;
        }

        //Column numbers
        System.out.print("      ");
        for (int j = START_COL; j <= END_COL; j++) {
            if(j < 10) System.out.printf("  %d   ", j);
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
                if(pl.getSpaceshipMask()[i][j]) {
                    if (pl.getSpaceshipBoard()[i][j].isEmpty()) {
                        System.out.print("     |");
                    } else {
                        char westConnector = connectorSymbol(pl.getSpaceshipBoard()[i][j].get(), RotationType.WEST);
                        String tile = stringTile(pl, i, j);
                        char eastConnector = connectorSymbol(pl.getSpaceshipBoard()[i][j].get(), RotationType.EAST);
                        System.out.printf(" %c%s%c |", westConnector, tile, eastConnector);
                    }
                } else {
                    System.out.print("  0  |");
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
        if(bookedTiles.get(1) == null) System.out.print(" - ");
        else { System.out.print(bookedTiles.get(1).getRotationType()); }
        if(bookedTiles.get(2) == null) System.out.print(" - ");
        else { System.out.print(bookedTiles.get(2).getRotationType()); }
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
                System.out.printf(" %c%s%c  |", westConnector, tile, eastConnector);
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
        System.out.println("Tile orientations: (X,Y) Direction");

        HashMap<Coordinate, TileV> engines = new HashMap<>();
        HashMap<Coordinate, TileV> cannons = new HashMap<>();
        HashMap<Coordinate, TileV> shields = new HashMap<>();

        for(int i=0; i<12; i++){
            for(int j=0; j<12; j++){
                if(pl.getSpaceshipBoard()[i][j].isPresent()){
                    switch(pl.getSpaceshipBoard()[i][j].get().getType()){
                        case MOTOR, D_MOTOR -> engines.put(new Coordinate(i,j), pl.getSpaceshipBoard()[i][j].get());
                        case CANNON, D_CANNON -> cannons.put(new Coordinate(i,j), pl.getSpaceshipBoard()[i][j].get());
                        case SHIELD -> shields.put(new Coordinate(i,j), pl.getSpaceshipBoard()[i][j].get());
                    }
                }
            }
        }
        StateGameType phase = game.getCurrentState().getPhase();

        if(phase == StateGameType.BUILD || phase == StateGameType.CHECK || phase == StateGameType.CORRECTION){
            System.out.print("Engines:  ");
            for(Map.Entry<Coordinate, TileV> entry : engines.entrySet()){
                System.out.printf("(%d,%d) %s\t  ", entry.getKey().x(), entry.getKey().y(), entry.getValue().getRotationType());
            }
        }
        System.out.println();
        System.out.print("Cannons:  ");
        for(Map.Entry<Coordinate, TileV> entry : engines.entrySet()){
            System.out.printf("(%d,%d) %s\t  ", entry.getKey().x(), entry.getKey().y(), entry.getValue().getRotationType());
        }
        System.out.println();
        System.out.print("Shields:  ");
        for(Map.Entry<Coordinate, TileV> entry : shields.entrySet()){
            StringBuilder sb = new StringBuilder();
            switch(entry.getValue().getRotationType()){
                case NORTH -> sb.append("N-E");
                case WEST -> sb.append("N-W");
                case EAST -> sb.append("S-E");
                case SOUTH -> sb.append("S-W");
            }
            System.out.printf("(%d,%d) %s\t  ", entry.getKey().x(), entry.getKey().y(), sb.toString());
        }
        System.out.println();
    }

    private char connectorSymbol (TileV tile, RotationType direction){
        ConnectorType[] connectors = tile.getConnectors();
        RotationType rotation = tile.getRotationType();

        int index = (direction.getNum() - rotation.getNum() + 4) % 4;
        ConnectorType connector = connectors[index];

        switch (connector) {
            case SINGLE -> { return '·';}
            case DOUBLE -> {return ':';}
            case UNIVERSAL -> {return '!';}
            default -> {return ' ';}
        }
    }

    private String stringTile (PlayerV player, int x, int y) {
        if(x == 7 && y == 7 && player.getSpaceshipBoard()[x][y].isPresent() && player.getSpaceshipBoard()[x][y].get().getType() == TileType.CABIN) {
            PlayerColor playerColor = player.getColor();
            switch (playerColor) {
                case RED -> {return ROSSO + "C" + RESET;}
                case BLUE -> {return BLU + "C" + RESET;}
                case GREEN -> {return VERDE + "C" + RESET;}
                case YELLOW -> {return GIALLO + "C" + RESET;}
                default -> {return BIANCO + "C" + RESET;}
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
            case CABIN -> { return BIANCO + "C" + RESET;}
            case CANNON -> { return MAGENTA + "G" + RESET;}
            case D_CANNON -> { return VERDE_FOGLIA + "G" + RESET;}
            case MOTOR -> { return MARRONE + "M" + RESET;}
            case D_MOTOR -> { return VERDE_FOGLIA + "M" + RESET;}
            case STRUCTURAL -> { return BIANCO + "#" + RESET;}
            case SHIELD -> { return VERDE_FOGLIA + "S" + RESET;}
            case BROWN_CABIN -> { return MARRONE + "O" + RESET;}
            case PURPLE_CABIN -> { return MAGENTA + "O" + RESET;}
            case BATTERY -> { return VERDE + "E" + RESET;}
            case STORAGE -> { return CIANO + "T" + RESET;}
            case SPECIAL_STORAGE -> { return   ROSA + "D" + RESET;}
            default -> { return RESET;}
        }
    }
}
