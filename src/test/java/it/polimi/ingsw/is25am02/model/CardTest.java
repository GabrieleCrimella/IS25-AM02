package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.InitialCard;
import it.polimi.ingsw.is25am02.model.cards.boxes.GreenBox;
import it.polimi.ingsw.is25am02.model.tiles.Storage;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    @Test
    void test_should_check_All_UnsupportedMethods() {
        Card card = new InitialCard(2,null,null,false);

        //4 spaceship
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
        //4 player
        Player dummyPlayer = new Player(spaceship1, "Rosso", PlayerColor.RED, null, 1);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE, null, 1);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN, null, 1);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW, null, 1);
        List<Player> players = new ArrayList<Player>();
        players.add(dummyPlayer);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        //game di livello 2 con i 4 players
        Game dummyGame = new Game(players,2);

        TileType t = TileType.STORAGE;
        ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType = RotationType.NORTH;
        int maxNum = 3;
        Tile dummyTile = new Storage(t, connectors, rotationType, null, maxNum);

        assertThrows(UnsupportedOperationException.class, () -> card.choice(dummyGame, dummyPlayer, true));
        assertThrows(UnsupportedOperationException.class, () -> card.removeCrew(dummyGame, dummyPlayer, dummyTile));
        assertThrows(UnsupportedOperationException.class, () -> card.choiceBox(dummyGame, dummyPlayer, true));
        assertThrows(UnsupportedOperationException.class, () -> card.moveBox(dummyGame, dummyPlayer, new ArrayList<>(), new ArrayList<>(), BoxType.GREEN, true));
        assertThrows(UnsupportedOperationException.class, () -> card.choicePlanet(dummyGame, dummyPlayer, 0));
        assertThrows(UnsupportedOperationException.class, () -> card.choiceDoubleMotor(dummyGame, dummyPlayer, new ArrayList<>(), new ArrayList<>()));
        assertThrows(UnsupportedOperationException.class, () -> card.setCurrentPhase(1));
        assertThrows(UnsupportedOperationException.class, () -> card.setPhase(2));
        assertThrows(UnsupportedOperationException.class, () -> card.choiceDoubleCannon(dummyGame, dummyPlayer, new ArrayList<>(), new ArrayList<>()));
        assertThrows(UnsupportedOperationException.class, () -> card.choiceCrew(dummyGame, dummyPlayer));
        assertThrows(UnsupportedOperationException.class, () -> card.removeBox(dummyGame, dummyPlayer, dummyTile, BoxType.YELLOW));
        assertThrows(UnsupportedOperationException.class, () -> card.removeBattery(dummyGame, dummyPlayer, dummyTile));
        assertThrows(UnsupportedOperationException.class, () -> card.calculateDamage(dummyGame, dummyPlayer, Optional.empty()));
        assertThrows(UnsupportedOperationException.class, () -> card.effect(dummyGame));
        assertThrows(UnsupportedOperationException.class, () -> card.keepBlocks(dummyGame, dummyPlayer, new Coordinate(0, 0)));
        assertThrows(UnsupportedOperationException.class, () -> card.getBoxesWon());
        assertThrows(UnsupportedOperationException.class, () -> card.getPlanetOffers());
        assertThrows(UnsupportedOperationException.class, () -> card.getBoxesWonTypes());
        assertThrows(UnsupportedOperationException.class, () -> card.getPlanetOffersTypes());
        assertThrows(UnsupportedOperationException.class, () -> card.addBoxWon(new GreenBox(BoxType.GREEN)));
        assertThrows(UnsupportedOperationException.class, () -> card.addPlanetOffers(new LinkedList<>()));
        assertThrows(UnsupportedOperationException.class, () -> card.clearBoxWon());
        assertThrows(UnsupportedOperationException.class, () -> card.clearPlanetOffers());
        assertThrows(UnsupportedOperationException.class, () -> card.getFly());
    }
}