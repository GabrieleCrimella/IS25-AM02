@startuml
package "Model"{

    class Spaceship{
        -SpaceshipIterator it
        -int numOfWastedTiles
        -int cosmicCredits
        -Tile currentTile
        -int x_start, y_start
        --
        +Spaceship(int level)
        --
        GESTIONE TILE
        +void addTile(int x, int y, Tile t)
        +Optional<Tile> getTile(int x, int y)
        +void removeTile(int x, int y)
        +void returnTile()
        +Tile getCurrentTile()
        +void setCurrentTile(Tile t)
        --
        CALCOLO VALORI
        +double calculateCannonPower()
        +int calculateMotorPower()
        +int calculateExposedConnectors()
        +int calculateMember()
        --
        GESTIONE COSMIC CREDITS
        +int getCosmicCredits()
        +void addCosmicCredits(int num)
        +void removeCosmicCredits(int num)
        --
        GESTIONE SCARTI
        +int getNumOfWastedTiles()
        +void addNumOfWastedTiles(int num)
        --
        CONTROLLO VALORI
        +boolean checkSpaceship()
        +boolean isShielded(RotationType side)
        +boolean isExposed(RotationType, int num)
        +boolean isCannonPresent(RotationType side, int num)
        +boolean isDoubleCannonPresent(RotationType side, int num)
        +boolean isMostExpensive(BoxType type)
        --
        GESTIONE COLPI / METEORITI
        +boolean meteoriteDamage(int size, RotationType rot, int num, Optional<BatteryStorage> storage)
        +boolean shotDamage(int size, RotationType rot, int num, Optional<BatteryStorage> storage)
        --
        FUNZIONI EXTRA
        +void epidemyRemove()
        +void removeBattery(BatteryStorage t)
        +List<Tile> getList(TileType)
        +void holdSpaceship(int x, int y)
        +boolean own(Tile tile)
    }

    class SpaceIterator{}

    Spaceship --> SpaceIterator

    Interface Game_Interface{
        Phase START
        +Game GameCreator(List<Player> p, int level);
        --
        Phase BUILD
        +void flipHourglass();
        +Tile takeTile(Player player);
        +Tile takeTile(Player player, Tile tile);
        +void returnTile(Player player, Tile tile);
        +void addTile(Player player, Tile tile, int x, int y);
        +void shipFinished(Player player);
        --
        Phase CHECK
        +boolean checkSpaceship(Player player);
        --
        Phase CORRECTION
        +void removeTile(Player player, int x, int y);
        --
        Phase INITIALIZATION_SPACESHIP (Automatica se nessun player ha supporti vitali per alieni sulla nave)
        +void addCrew(Player player, int x, int y, AliveType type);
        --
        Phase TAKE_CARD
        +void playNextCard(Player player);
        --
        Phase EFFECT_ON_PLAYERS
        +HashMap<Integer, Player> getPosition();
        +HashMap<Player, StatePlayerType> getState();
        +List<Tile> possibleChoice(Player player, TileType type);
        +void choice(Player player, boolean choice);
        +void removeCrew(Player player, Cabin cabin);
        +List<Box> choiceBox(Player player, boolean choice) throws Exception;
        +void moveBox(Player player, List<Box> start, List<Box> end, Box box, boolean on) throws Exception;
        +List<Box> choicePlanet(Player player, int index);
        +void choiceDoubleMotor(Player player, List<Pair<DoubleMotor, BatteryStorage>> choices);
        +void choiceDoubleCannon(Player player, List<Pair<DoubleCannon, BatteryStorage>> choices);
        +void removeBox(Player player, SpecialStorage storage, BoxType type);
        +void removeBattery(Player player, BatteryStorage storage);
        +void rollDice(Player player);
        +void calculateDamage(Player player, Optional<BatteryStorage> batteryStorage);
        +void holdSpaceship(Player player, int x, int y);
        --
        Phase RESULT
        +ArrayList<Player> getWinners();
    }

    Game_Interface <|.. Game

    class Game{
        -String gameName
        -int level
        -int maxAllowedPlayers
        -GameBoard globalBoard
        -CardDeck deck
        -Hourglass hourglass
        -HeapTiles heapTiles
        -State state
        -List<Player> Players
        -int diceResult
        --
        +Game(List<Player> p, int level)
        --
        +Getter degli attributi
        +Setter per dice
        +Player getCurrentPlayer()
        +Card getCurrentCard()
        +void nextPlayer()
    }

    class State{
        -Card currentCard
        -Player currentPlayer
        -StateGameType phase
        --
        +Getter
        +Setter
    }

    enum StatePlayerType{
        FINISHED
        NOT_FINISHED
        CORRECT_SHIP
        WRONG_SHIP
        IN_GAME
        OUT_GAME
    }

    enum StateGameType{
        START
        INITIALIZATION_GAME
        BUILD
        CHECK
        CORRECTION
        INITIALIZATION_SPACESHIP
        TAKE_CARD
        EFFECT_ON_PLAYER
        CHANGE_CONDITION
        RESULT
    }

    enum StateCardType{
        DECISION
        CHOICE_ATTRIBUTES
        REMOVE
        BOXMANAGEMENT
    }

    Game --> State

    Game --> Hourglass

    Game --> HeapTiles

    Game "2..4" o-- "1" Player

    Game --> CardDeck

    Player --> Spaceship

    Game --> Gameboard

    class Gameboard{
        -int numStep
        -int[4] startingPositions
        -HashMap<Player, int> position
        --
        +Gameboard(int level)
        +void movePosition(int step, Player p)
        +List<Player> getRanking()
    }

    enum TileType{
        CANNON
        CABIN
        MOTOR
        BATTERY
        SHIELD
        STORAGE
        SPECIAL_STORAGE
        D_CANNON
        D_MOTOR
        STRUCTURAL
        PURPLE_CABIN
        BROWN_CABIN
    }

    enum ConnectorType{
        NONE
        SINGLE
        DOUBLE
        UNIVERSAL
    }

    enum RotationType{
        NORTH
        EAST
        SOUTH
        WEST
    }

    abstract class Tile{
        -TileType tType
        -ConnectorType[4] connectors
        -RotationType rotation
        -boolean visible
        -int id
        --
        +Tile(TileType t, ConnectorType[4] connectors, RotationType r, boolean v, int id)
        --
        GETTER
        +int getId()
        +boolean getVisible()
        +RotationType getRotations()
        +ConnectorType[4] getConnectors()
        +TileType getTileType()
        --
        SETTER
        +void setVisible()
        --
        +boolean checkConnections(Tile t, RotationType sideToCheck)
        ---
        Battery
        +int getNumBattery()
        +void removeBattery()
        --
        Shield
        +boolean isShielded(RotationType side)
        --
        Cabin
        +List<Alive> getNumCrew()
        +void removeCrew()
        +void addCrew(Alive member)
        --
        Storage
        +void addBox(Box)
        +List<Box> getOccupation()
        +void removeBox(Box box)
        +int getNumOccupied()
    }

    class Alive{
        -AliveType race
        --
        +AliveType getRace()
    }

    enum AliveType{
        HUMAN
        BROWN_ALIEN
        PURPLE_ALIEN
    }

    Spaceship "1..n" o-- "1" Tile

    class BatteryStorage extends Tile{
        -int Battery
        -int maxBattery
        --
        +BatteryStorage(int maxBattery)
    }

    class Shield extends Tile{
        -HashMap<RotationType, boolean> shielded
        --
        +Shield(HashMap<RotationType, boolean> shielded)
    }

    class Structural extends Tile{
        +Structural()
    }

    class Cabin extends Tile{
        List<Alive> Crew
        --
        +Cabin(int human, int p_alien, int o_alien)
    }

    class PurpleCabin extends Tile{
        +PurpleCabin()
    }

    class BrownCabin extends Tile{
        +BrownCabin()
    }

    class SpecialStorage extends Tile{
        -int maxNum
        -List<Box> occupation
        --
        +SpecialStorage(int maxNum)
    }

    class Storage extends SpecialStorage{
        +Storage(int maxNum)
    }

    enum BoxType{
        RED 4
        BLUE 1
        GREEN 2
        YELLOW 3
        NONE 0
        --
        -int power
        +int getPower()
        --
        +boolean isValid()
    }

    class Motor extends Tile{
        +Motor()
    }

    class DoubleMotor extends Tile{
        +DoubleMotor()
    }

    class Cannon extends Tile{
        +Cannon()
    }

    class DoubleCannon extends Tile{
        +DoubleCannon()
    }

    class Dice{
        +int pickRandomNumber()
    }

    class Hourglass{
        -Timer timer
        -long durata
        --
        +Hourglass()
        +long getTimeLeft()
        +void flip()
    }

    class HeapTiles{
        -Set<Tile> setTiles
        --
        +HeapTiles()
        +Tile DrawTile()
        +Set<Tile> getVisibleTiles()
        +void removeVisibleTile(Tile t)
        +void addTile(Tile t)
    }

    class CardDeck{
        -HashMap<int numDeck, List<Card> miniDeck,boolean occupied> deck
        -List<Card> finalDeck
        --
        +CardDeck(int level)
        +List<Card> createFinalDeck()
        +Card giveCard()
        +List<Card> giveDeck(int numDeck)
        +void returnDeck(int numDeck)
    }

    abstract class Card{
        -int level
        --
        +Card card(int level)
        +Card newCard()
        {abstract}+Card createCard()
    }

    class OpenSpace extends Card{
        +Openspace()
        +OpenSpace createCard()
        +void choiceDoubleMotor( Game game, Player player, List<Pair<DoubleMotor, BatteryStorage>> Motor)
    }

    class Stardust extends Card{
        +Stardust()
        +Stardust createCard()
        +void effect(Game game)
    }

    class AbbandonedShip extends Card{
        -int humanLost
        -int AliveRemoved
        -int creditwin
        -int flyBack
        --
        +AbbandonedShip(...)
        +AbbandonedShip createCard()
        +void choice(Game game, Player player, boolean choice)
        +void removeCrew(Game game, Player player, Cabin cabin)
    }

    class Planet extends Card_with_box{
        -int daysLost
        -List<List<Box>> planetOffers
        -ArrayList<Integer> occupied
        -LinkedList<Player> landed;
        --
        +Planet(...)
        +Planet createCard()
        +List<Box> choicePlanet(Game game, Player player, int index)
        +void moveBox(Game g, Player p, List<Box> start, List<Box> end, Box box, boolean on)
    }

    class MeteoritesStorm extends Card{
        -ArrayList<Pair<Integer, RotationType>> meteorites
        -int currentIndex
        --
        +MeteoritesStorm(...)
        +MeteoritesStorm createCard()
        +void holdSpaceship(Game game, Player player, int x, int y)
        +void calculateDamage(Game g, Player p, Optional<BatteryStorage> storage)

    }

    abstract class Enemies extends Card{
        -int cannonPowers
        -int daysLost
        -int credit
        --
        +Enemies(int c, int d, int cr)
        +int getCannonPowers()
        +int getDaysLost()
        +int getCredit()
    }

    class Pirates extends Enemies{
        -boolean[3] shots
        --
        +Pirates(...)
        +Pirates createCard()
    }

    class SlaveOwner extends Enemies{
        -int AliveLost
        -int AliveRemoved
        --
        +SlaveOwner(...)
        +SlaveOwner createCard()
        +void choiceDoubleCannon(Game g, Player p, List<Pair<DoubleCannon, BatteryStorage>> Cannon)
        +void choice(Game game, Player player, boolean choice)
        +void removeCrew(Game game, Player player, Cabin cabin)
    }

    class Trafficker extends Card_with_box{
        -int cannonPowers
        -int daysLost
        -int boxesLost
        -int boxesRemove
        -ArrayList<Box> boxesWon
        --
        +Trafficker(...)
        +Trafficker createCard()
        +void choiceDoubleCannon(Game g, Player p, List<Pair<DoubleCannon, BatteryStorage>> choices)
        +List<Box> choiceBox(Game game, Player player, boolean choice)
        +void moveBox(Game g, Player p, List<Box> start, List<Box> end, Box box, boolean on)
        +void removeBox(Game g, Player p, SpecialStorage storage, BoxType type)
    }

    class AbbandonedStation extends Card_with_box{
        -int AliveNeeded
        -int daysLost
        -LinkedList<Box> boxesWon
        --
        +AbbandonedStation(...)
        +AbbandonedStation createCard()
        +List<Box> choiceBox(Game game, Player player, boolean choice)
        +void moveBox(Game g, Player p, List<Box> start, List<Box> end, Box box, boolean on)
    }

    class WarZoneI extends Card{
    }

    class WarZoneII extends Card{
    }

    class Epidemy extends Card{
        +Epidemy()
        +Epidemy createCard()
        +void effect(Game game)
    }

    CardDeck *-- Card
    HeapTiles *-- Tile
    Gameboard --> Dice

    class Player{
        -Spaceship spaceship
        -String nickname
        -PlayerColor playerColor
        -StatePlayerType state
        --
        +Player(Spaceship s, String nickname, PlayerColor pColor)
        +Getter
        +void setStatePlayer(StatePlayerType statePlayer)
    }

    enum PlayerColor{
        RED
        YELLOW
        GREEN
        BLUE
    }

    abstract class Box {
        -int value
        -BoxType type

        +Box(int value, BoxType type)
        +int getValue()
        +BoxType getBoxType()
    }

    class RedBox extends Box{
    }

    class GreenBox extends Box{
    }

    class BlueBox extends Box{
    }

    class YellowBox extends Box{
    }

    class BoxStore{
        -Hashmap<int value, List<Box> boxline> Store

        +void addBox(Box box)
        +Box removeBox(int value)
    }

    BoxStore *-- Box
    Card_with_box --> BoxStore

    Abstract class Card_with_box extends Card{
        -BoxStore Store

        +BoxStore getBoxStore()
    }

    Cabin --> Alive
@enduml