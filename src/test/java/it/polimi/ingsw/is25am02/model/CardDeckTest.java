package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.*;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalPhaseException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.Cabin;
import it.polimi.ingsw.is25am02.model.tiles.Motors;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.is25am02.utils.enumerations.StateCardType.FINISH;
import static it.polimi.ingsw.is25am02.utils.enumerations.StateGameType.TAKE_CARD;
import static it.polimi.ingsw.is25am02.utils.enumerations.StatePlayerType.NOT_FINISHED;
import static org.junit.jupiter.api.Assertions.*;

class CardDeckTest {
    @Test
    public void loadCardTest() {
        //CardDeck cardDeck = new CardDeck(2);
        List<Player> players = new ArrayList<>();
        Spaceship spaceship = new Spaceship(0);
        players.add(new Player(spaceship, "mario", PlayerColor.YELLOW));

        Game game = new Game(players,2);

        boolean foundAbShip = false;
        boolean foundAbSta = false;
        boolean foundPirate = false;
        boolean foundTrafficker = false;
        boolean foundSlave = false;
        boolean foundOSpace = false;
        boolean foundStar = false;
        boolean foundEp = false;
        boolean foundMeteor = false;
        boolean foundPlan = false;
        boolean foundWar1 = false;
        boolean foundWar2 = false;

        for (Card card : game.getDeck().getInitialDeck()) {
            int level = card.getLevel();

            if (card instanceof AbbandonedShip) {
                foundAbShip = true;
                if (level == 1) {
                    assertEquals(1, level);
                } else if (level == 2) {
                    assertEquals(2, level);
                }
            }
            else if (card instanceof AbbandonedStation) {
                foundAbSta = true;
                if (level == 1) {
                    LinkedList<BoxType> typesExpected = new LinkedList<>();
                    typesExpected.add(BoxType.YELLOW);
                    typesExpected.add(BoxType.GREEN);
                    assertEquals(1, level);
                    if(typesExpected.equals(card.getBoxesWonTypes())){
                        assertEquals(typesExpected, card.getBoxesWonTypes());
                    }
                    typesExpected.clear();
                    typesExpected.add(BoxType.RED);
                    typesExpected.add(BoxType.RED);
                    if(typesExpected.equals(card.getBoxesWonTypes())){
                        assertEquals(typesExpected, card.getBoxesWonTypes());
                    }
                } else if (level == 2) {
                    assertEquals(2, level);
                }
            }

            else if (card instanceof Pirate) {
                foundPirate = true;
                if (level == 1) {
                    assertEquals(1, level);
                } else if (level == 2) {
                    assertEquals(2, level);
                }
            }

            else if (card instanceof Trafficker) {
                foundTrafficker = true;
                if (level == 1) {
                    assertEquals(1, level);
                } else if (level == 2) {
                    assertEquals(2, level);
                }
            }

            else if (card instanceof SlaveOwner) {
                foundSlave = true;
                if (level == 1) {
                    assertEquals(1, level);
                } else if (level == 2) {
                    assertEquals(2, level);
                }
            }

            else if (card instanceof OpenSpace) {
                foundOSpace = true;
                if (level == 1) {
                    assertEquals(1, level);
                } else if (level == 2) {
                    assertEquals(2, level);
                }
            }

            else if (card instanceof Stardust) {
                foundStar = true;
                if (level == 1) {
                    assertEquals(1, level);
                } else if (level == 2) {
                    assertEquals(2, level);
                }
            }

            else if (card instanceof Epidemy) {
                foundEp = true;
                if (level == 1) {
                    assertEquals(1, level);
                } else if (level == 2) {
                    assertEquals(2, level);
                }
            }

            else if (card instanceof MeteoritesStorm) {
                foundMeteor = true;
                if (level == 1) {
                    assertEquals(1, level);
                } else if (level == 2) {
                    assertEquals(2, level);
                }
            }

            else if (card instanceof Planet) {
                foundPlan = true;
                if (level == 1) {
                    assertEquals(1, level);
                } else if (level == 2) {
                    assertEquals(2, level);
                }
            }

            else if (card instanceof WarZone_I) {
                foundWar1 = true;
                if (level == 1) {
                    assertEquals(1, level);
                } else if (level == 2) {
                    assertEquals(2, level);
                }
            }

            else if (card instanceof WarZone_II) {
                foundWar2 = true;
                if (level == 1) {
                    assertEquals(1, level);
                } else if (level == 2) {
                    assertEquals(2, level);
                }
            }

        }
        assertTrue(foundAbShip);
        assertTrue(foundAbSta);
        assertTrue(foundPirate);
        assertTrue(foundTrafficker);
        assertTrue(foundSlave);
        assertTrue(foundStar);
        assertTrue(foundEp);
        assertTrue(foundMeteor);
        assertTrue(foundPlan);
        assertTrue(foundWar1);
        assertTrue(foundWar2);
        assertTrue(foundOSpace);
    }

    @Test
    public void playNextCardTest() {
        List<Player> players = new ArrayList<>();
        Spaceship spaceship = new Spaceship(2);
        players.add(new Player(spaceship, "mario", PlayerColor.YELLOW));
        Game game = new Game(players,2); //todo anche testflight
        game.getGameboard().initializeGameBoard(players);

        TileType t1 = TileType.CABIN;
        ConnectorType[] connectors1 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        RotationType rotationType1 = RotationType.NORTH;
        int id1 = 1;
        Tile cabin1 = new Cabin(t1, connectors1, rotationType1, id1);
        try {
            spaceship.addTile(7,7, cabin1);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 2 - cabin
        TileType t2 = TileType.CABIN;
        ConnectorType[] connectors2 = {ConnectorType.DOUBLE, ConnectorType.DOUBLE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType2 = RotationType.NORTH;
        int id2 = 1;
        Tile cabin2 = new Cabin(t2, connectors2, rotationType2, id2);
        try {
            spaceship.addTile(8,7, cabin2);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        //tile 3 - motor
        TileType t3 = TileType.MOTOR;
        ConnectorType[] connectors3 = {ConnectorType.SINGLE, ConnectorType.NONE, ConnectorType.NONE, ConnectorType.UNIVERSAL};
        RotationType rotationType3 = RotationType.NORTH;
        int id3 = 1;
        Tile motor3 = new Motors(t3, connectors3, rotationType3, id3);
        try {
            spaceship.addTile(7,8, motor3);
        } catch (IllegalAddException e) {
            System.out.println(e.getMessage());
        }

        game.getPlayers().getFirst().setStatePlayer(NOT_FINISHED);
        game.shipFinished(players.getFirst());

        Coordinate position = new Coordinate(7,7);
        game.getPlayers().getFirst().setStatePlayer(StatePlayerType.CORRECT_SHIP);
        game.getCurrentState().setPhase(StateGameType.INITIALIZATION_SPACESHIP);
        game.addCrew(game.getPlayers().getFirst(), position, AliveType.HUMAN);

        position = new Coordinate(8,7);
        game.addCrew(game.getPlayers().getFirst(), position, AliveType.HUMAN);

        game.getPlayers().getFirst().setStatePlayer(StatePlayerType.IN_GAME);
        game.getCurrentState().setPhase(StateGameType.TAKE_CARD);

        game.getGameboard().move(3,players.getFirst());

        game.getCurrentCard().setStateCard(FINISH);

        game.playNextCard(game.getPlayers().getFirst());
        Card nextCard = game.getCurrentCard();
        while(nextCard!=null && game.getCurrentState().getPhase().equals(TAKE_CARD)) {
            if(nextCard.getCardType().equals(CardType.TRAFFICKER) || nextCard.getCardType().equals(CardType.ABANDONED_STATION) ) {
                assertEquals(nextCard.getBoxesWonTypes().size(), nextCard.getBoxesWon().size());
                assertNotNull(nextCard.getBoxesWon());
            }
            if(nextCard.getCardType().equals(CardType.OPENSPACE) ) {
                try {
                    nextCard.choiceDoubleMotor(game,players.getFirst(),null,null);
                } catch (IllegalPhaseException | IllegalRemoveException e) {
                    throw new RuntimeException(e);
                }
            }
            game.getCurrentCard().setStateCard(FINISH);
            game.getCurrentState().setPhase(StateGameType.TAKE_CARD);
            game.playNextCard(game.getPlayers().getFirst());
        }
    }
}
