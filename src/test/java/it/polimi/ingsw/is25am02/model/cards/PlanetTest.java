package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.model.Spaceship;
import it.polimi.ingsw.is25am02.model.cards.boxes.BlueBox;
import it.polimi.ingsw.is25am02.model.cards.boxes.Box;
import it.polimi.ingsw.is25am02.model.cards.boxes.BoxStore;
import it.polimi.ingsw.is25am02.model.cards.boxes.RedBox;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.tiles.Storage;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlanetTest {

    @Test
    void test_should_take_box_from_planet_to_tile(){
        //initialize game with 4 platers
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        Game game = new Game(players,2);
        game.getGameboard().initializeGameBoard(players);

        //tile 1
        TileType t1 = TileType.STORAGE;
        ConnectorType[] connectors1 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.SINGLE};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        int maxNum1 = 3;
        Tile storage1 = new Storage(t1, connectors1, rotationType1, id1, maxNum1);

        //tile 2
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum2 = 3;
        Tile storage2 = new Storage(t2, connectors2, rotationType2, id2, maxNum2);
        BlueBox tilebluebox = new BlueBox(BoxType.BLUE);
        try {
            storage2.addBox(tilebluebox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //CreateCard
        int level = 2;
        BoxStore store = new BoxStore();
        int daysLost = 1;
        RedBox planet1redbox1 = new RedBox(BoxType.RED);
        RedBox planet1redbox2 = new RedBox(BoxType.RED);
        BlueBox planet2bluebox = new BlueBox(BoxType.BLUE);
        LinkedList<Box> planet1boxes = new LinkedList<>();
        planet1boxes.add(planet1redbox1);
        planet1boxes.add(planet1redbox2);
        LinkedList<Box> planet2boxes = new LinkedList<>();
        planet2boxes.add(planet2bluebox);
        ArrayList<LinkedList<Box>> planetOffers = new ArrayList<>();
        ArrayList<LinkedList<BoxType>> planetOffersTypes = new ArrayList<>();
        planetOffers.add(planet1boxes);
        planetOffers.add(planet2boxes);
        //Nel pianeta 1 ci sono due box rossi
        //Nel pianeta 2 c'è un box blu
        Planet planet = new Planet(level, store, daysLost, planetOffers, planetOffersTypes);

        //todo qui ho cambiato il box con boxtype
        planet.moveBox(game, player1, planetOffers.get(0), storage1.getOccupation(), BoxType.RED, true );

        //correct start
        ArrayList<Box> correctPlanet1boxes = new ArrayList<Box>();
        correctPlanet1boxes.add(planet1redbox2);

        //correct end
        ArrayList<Box> correctStorage = new ArrayList<Box>();
        correctStorage.add(planet1redbox1);

        assertEquals(true, storage1.getOccupation().equals(correctStorage));
        assertEquals(true, planet.getPlanetOffers().get(0).equals(correctPlanet1boxes));
    }

    @Test
    void test_should_make_spaceships_move_back(){
        //initialize game with 4 platers
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        Game game = new Game(players,2);
        game.getGameboard().initializeGameBoard(players);

        //tile 1
        TileType t1 = TileType.STORAGE;
        ConnectorType[] connectors1 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.SINGLE};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        int maxNum1 = 3;
        Tile storage1 = new Storage(t1, connectors1, rotationType1, id1, maxNum1);

        //tile 2
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum2 = 3;
        Tile storage2 = new Storage(t2, connectors2, rotationType2, id2, maxNum2);
        BlueBox tilebluebox = new BlueBox(BoxType.BLUE);
        try {
            storage2.addBox(tilebluebox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //CreateCard
        int level = 2;
        BoxStore store = new BoxStore();
        int daysLost = 1;
        RedBox planet1redbox1 = new RedBox(BoxType.RED);
        RedBox planet1redbox2 = new RedBox(BoxType.RED);
        BlueBox planet2bluebox = new BlueBox(BoxType.BLUE);

        LinkedList<Box> planet1boxes = new LinkedList<>();
        planet1boxes.add(planet1redbox1);
        planet1boxes.add(planet1redbox2);

        LinkedList<Box> planet2boxes = new LinkedList<>();
        planet2boxes.add(planet2bluebox);

        ArrayList<LinkedList<Box>> planetOffers = new ArrayList<>();
        ArrayList<LinkedList<BoxType>> planetOffersTypes = new ArrayList<>();
        //planetOffers.add(planet1boxes);
        //planetOffers.add(planet2boxes);
        LinkedList<BoxType> redred = new LinkedList<>();
        redred.add(BoxType.RED);
        redred.add(BoxType.RED);
        LinkedList<BoxType> blu = new LinkedList<>();
        blu.add(BoxType.BLUE);
        planetOffersTypes.add(redred);
        planetOffersTypes.add(blu);
        //Nel pianeta 1 ci sono due box rossi
        //Nel pianeta 2 c'è un box blu
        Planet planet = new Planet(level, store, daysLost, planetOffers, planetOffersTypes);
        game.getCurrentState().setCurrentCard(planet);
        game.getCurrentState().setCurrentPlayer(player1);

        //player 2 e 3 non atterrano su nessun pianeta
        //todo qui ho cambiato il box con boxtype
        planet.choicePlanet(game,player1,0);
        planet.moveBox(game, player1,  planet.getPlanetOffers().get(0), storage1.getOccupation(), BoxType.RED, true );
        planet.choicePlanet(game, player2, -1);
        planet.choicePlanet(game, player3, -1);
        planet.choicePlanet(game,player4,1);
        //todo qui ho cambiato il box con boxtype
        planet.moveBox(game, player4, planet.getPlanetOffers().get(1), storage1.getOccupation(), BoxType.RED, false );


        //correct start
        ArrayList<Box> correctPlanet1boxes = new ArrayList<Box>();
        correctPlanet1boxes.add(planet1redbox2);

        //correct end
        ArrayList<Box> correctStorage = new ArrayList<Box>();
        correctStorage.add(planet1redbox1);


        //correct positioning
        HashMap<Player, Integer> correct = new HashMap<Player, Integer>();
        correct.put(player1, 5);
        correct.put(player2, 3);
        correct.put(player3, 1);
        correct.put(player4, -1);

        assertEquals(storage1.getOccupation().size(), correctStorage.size());
        assertEquals(planet.getPlanetOffers().get(0).size(), correctPlanet1boxes.size());
        assertEquals(game.getGameboard().getPositions(), correct);


    }

    @Test
    void test_should_make_spaceships_move_back_even_if_last_does_not_land(){
        //initialize game with 4 platers
        Spaceship spaceship1 = new Spaceship(2);
        Spaceship spaceship2 = new Spaceship(2);
        Spaceship spaceship3 = new Spaceship(2);
        Spaceship spaceship4 = new Spaceship(2);
        Player player1 = new Player(spaceship1, "Rosso", PlayerColor.RED);
        Player player2 = new Player(spaceship2, "Blu", PlayerColor.BLUE);
        Player player3 = new Player(spaceship3, "Verde", PlayerColor.GREEN);
        Player player4 = new Player(spaceship4, "Giallo", PlayerColor.YELLOW);
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        Game game = new Game(players,2);
        game.getGameboard().initializeGameBoard(players);

        //tile 1
        TileType t1 = TileType.STORAGE;
        ConnectorType[] connectors1 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.SINGLE};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        int maxNum1 = 3;
        Tile storage1 = new Storage(t1, connectors1, rotationType1, id1, maxNum1);

        //tile 2
        TileType t2 = TileType.STORAGE;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.SINGLE, ConnectorType.SINGLE};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        int maxNum2 = 3;
        Tile storage2 = new Storage(t2, connectors2, rotationType2, id2, maxNum2);
        BlueBox tilebluebox = new BlueBox(BoxType.BLUE);
        try {
            storage2.addBox(tilebluebox);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //CreateCard
        int level = 2;
        BoxStore store = new BoxStore();
        int daysLost = 1;
        RedBox planet1redbox1 = new RedBox(BoxType.RED);
        RedBox planet1redbox2 = new RedBox(BoxType.RED);
        BlueBox planet2bluebox = new BlueBox(BoxType.BLUE);
        ArrayList<LinkedList<Box>> planetOffers = new ArrayList<>();
        ArrayList<LinkedList<BoxType>> planetOffersTypes = new ArrayList<>();
        //planetOffers.add(planet1boxes);
        //planetOffers.add(planet2boxes);
        LinkedList<BoxType> redred = new LinkedList<>();
        redred.add(BoxType.RED);
        redred.add(BoxType.RED);
        LinkedList<BoxType> blu = new LinkedList<>();
        blu.add(BoxType.BLUE);
        planetOffersTypes.add(redred);
        planetOffersTypes.add(blu);
        //Nel pianeta 1 ci sono due box rossi
        //Nel pianeta 2 c'è un box blu
        Planet planet = new Planet(level, store, daysLost, planetOffers, planetOffersTypes);
        game.getCurrentState().setCurrentCard(planet);
        game.getCurrentState().setCurrentPlayer(player1);


        //todo qui ho cambiato il box con boxtype
        planet.choicePlanet(game, player1, 0);
        planet.moveBox(game, player1, planet.getPlanetOffers().get(0), storage1.getOccupation(), BoxType.RED, true );
        planet.choicePlanet(game, player2, -1);
        planet.choicePlanet(game, player3, -1);
        planet.choicePlanet(game, player4, -1);


        //correct start
        ArrayList<Box> correctPlanet1boxes = new ArrayList<Box>();
        correctPlanet1boxes.add(planet1redbox2);

        //correct end
        ArrayList<Box> correctStorage = new ArrayList<Box>();
        correctStorage.add(planet1redbox1);


        //correct positioning
        HashMap<Player, Integer> correct = new HashMap<Player, Integer>();
        correct.put(player1, 5);
        correct.put(player2, 3);
        correct.put(player3, 1);
        correct.put(player4, 0);

        assertEquals(storage1.getOccupation().size(), correctStorage.size());
        assertEquals(planet.getPlanetOffers().get(0).size(), correctPlanet1boxes.size());
        assertEquals(game.getGameboard().getPositions(), correct);


    }

}