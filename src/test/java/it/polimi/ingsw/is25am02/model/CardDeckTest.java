package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.cards.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTest {
    @Test
    public void loadCardTest() {
        CardDeck cardDeck = new CardDeck();

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

        for (Card card : cardDeck.getInitialDeck()) {
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
                    assertEquals(1, level);
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
}
