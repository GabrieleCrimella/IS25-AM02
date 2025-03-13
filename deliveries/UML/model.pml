@startuml
package "Model"{

    class Spaceship{
        -Tile[12][12] spaceshipBoard
        -boolean[12][12] maskSpaceship
        -int numOfWastedTiles
        -int cosmicCredits
        -Tile currentTile
        -int x_start, y_start
        --
        +Spaceship(int level)
        --
        +void addTile(int x, int y, Tile t)
        +Tile getTile(int x, int y)
        +void removeTile(int x, int y)
        --
        +double calculateCannonPower()
        +double calculateMotorPower()
        +int calculateExposedConnectors()
        +boolean checkSpaceship()
        --
        +int getCosmicCredits()
        +void addCosmicCredits(int num)
        +void removeCosmicCredits(int num)
        --
        +int getNumOfWastedTiles()
        +void addNumOfWastedTiles(int num)
        --
        +void removeBattery(BatteryStorage t)
        +boolean isExposed(RotationType, int num)
        +Tile getCurrentTile()
        +List<Tile> getList(TileType)
    }

    Interface Game_Interface{
        +void flipHourglass()
        +Tile takeTile(Player p)
        +Tile takeTile(Player p, Tile t)
        +void returnTile(Tile t)
        +void addTile(Player p, Tile t, int x, int y)
        +void removeTile(Player p, int x, int y)
        +void shipFinished(Player p)
        +boolean checkSpaceship(Player p)
        --
        +void playNextCard()
        +HashMap<int i, Player p> getPositions()
        +HashMap<Player p, StatePlayerType> getState()
        +List<Tile> possibleChoice(Player p, TypeTile t)
        +List<Player> getWinners()
        --
        effetti carte
        void choice(Player p, boolean choice)
        void removeCrew(Cabin cabin, int numCrew)
        List<Box> choiceBox(Player p, boolean choice)
        void moveBox(List<Box> start, List<Box> end, BoxType type)
        List<Box> choicePlanet(Player p, int index)
        void choiceDMotor(Player p, List<Pair<DoubleMotor, BatteryStorage>>)
        void choiceDCannon(Player p, List<Pair<DoubleCannon, BatteryStorage>>)
        void removeBox(Player p, SpecialStorage storage, BoxType type)
        void removeBattery(Player p, BatteryStorage storage)


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
        --
        +Game(List<Player> p, int level)
        --
        +CardDeck getDeck()
        +GlobalBoard getGlobalBoard()
        +List<Player> getPlayers()
        +String getName()
        +int getLevel()
        +HeapTiles getHeapTiles()
        +Hourglass getHourglass()
    }

    class State{
        +Card currentCard
        +Player currentPlayer
        +HashMap<Player p, StatePlayerType> state
        +StateGameType phase
        --
        Tutti i getter e setter per vedere i dati
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
        +int getId()
        +boolean getVisible()
        +RotationType getRotations()
        +ConnectorType[4] getConnectors()
        +TileType getTileType()
        +boolean checkConnections(Tile t, RotationType sideToCheck)
        +void setVisible()
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
        +void removeCrew(int num)
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
        -int power
        +int getPower()
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
        - int level
        --
        +Card card(int level)
        +Card newCard()
        {abstract}+Card createCard()
    }

    Note right : createCard() farà solo una return creatore_di_this()

    class OpenSpace extends Card{
        +Openspace()
        +OpenSpace createCard()
        +void effect(Gameboard gb)
    }

    class Stardust extends Card{
        +Stardust()
        +Stardust createCard()
        +void effect(Gameboard gb)
    }

    class AbbandonedShip extends Card{
        -int humanLost
        -int creditwin
        -int flyBack

        +AbbandonedShip(int h, int c, int fB)
        +AbbandonedShip createCard()
        +void effect(Gameboard gb)
    }

    class Planet extends Card_with_box{
        -int daysLost
        -List<List<Box>> planetOffers

        +Planet(int daysLost, List<List<Box>> planetOffers)
        +Planet createCard()
        +void effect(Gameboard gb)
    }

    class MeteoritesStorm extends Card{
        -List<int[2]> meteorites

        +MeteoritesStorm(List<int[2]> m)
        +MeteoritesStorm createCard()
        +void effect(Gameboard gb)
    }

    abstract class Enemies extends Card{
        -int cannonPowers
        -int daysLost
        -int credit

        +Enemies(int c, int d, int cr)
        +int getCannonPowers()
        +int getDaysLost()
        +int getCredit()
    }

    class Pirates extends Enemies{
        -boolean[3] shots

        +Pirates(int c, int d, int cr, boolean[3] shots)
        +Pirates createCard()
        +void effect(Gameboard gb)
    }

    class SlaveOwner extends Enemies{
        -int humansLost

        +SlaveOwner(int c, int d, int cr, int human)
        +SlaveOwner createCard()
        +void effect(Gameboard gb)
    }

    class Trafficker extends Card_with_box{
        -int cannonPowers
        -int daysLost
        -int boxesLost
        -List<Box> boxesWon

        +Trafficker(int c, int d, int b)
        +Trafficker createCard()
        +void effect(Gameboard gb)
    }

    class AbbandonedStation extends Card_with_box{
        -int humansNeeded
        -int daysLost
        -List<Box> boxesWon

        +AbbandonedStation(int h, int d)
        +AbbandonedStation createCard()
        +void effect(Gameboard gb)
    }

    class WarZoneI extends Card{
        +void effect(Gameboard gb)
    }

    class WarZoneII extends Card{
        +void effect(Gameboard gb)
    }

    class Epidemy extends Card{
        +void effect(Gameboard gb)
    }

    CardDeck *-- Card
    HeapTiles *-- Tile
    Gameboard --> Dice

    class Player{
        -Spaceship spaceship
        -String nickname
        -PlayerColor playerColor

        +Player(Spaceship s, String nickname, PlayerColor pColor)
        +Spaceship getSpaceship()
        +String getNickName()
        +PlayerColor getPlayerColor()
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