@startuml

class AbbandonedShip {
  + AbbandonedShip(int, int, int, int):
  - AliveRemoved: int
  - flyBack: int
  - creditWin: int
  - AliveLost: int
  + removeCrew(Game, Player, Cabin): void
  + createCard(): AbbandonedShip
  + choice(Game, Player, boolean): void
}
class AbbandonedStation {
  + AbbandonedStation(int, BoxStore, int, int, LinkedList<Box>):
  - daysLost: int
  - AliveNeeded: int
  - boxesWon: LinkedList<Box>
  + createCard(): AbbandonedStation
  + moveBox(Game, Player, List<Box>, List<Box>, Box, boolean): void
  + choiceBox(Game, Player, boolean): List<Box>
  + getBoxesWon(): LinkedList<Box>
}
class Alive {
  + Alive(AliveType):
  - race: AliveType
  + getRace(): AliveType
}
class BatteryStorage {
  + BatteryStorage(TileType, ConnectorType[], RotationType, int, int):
  ~ maxBattery: int
  ~ battery: int
  + removeBattery(): void
  + getNumBattery(): int
}

class BoxStore {
  + BoxStore():
  ~ store: HashMap<BoxType, ArrayList<Box>>
  + addBox(Box): void
  + getStore(): HashMap<BoxType, ArrayList<Box>>
  + removeBox(BoxType): Box
}
class BrownCabin {
  + BrownCabin(TileType, ConnectorType[], RotationType, int):
}
class Cabin {
  + Cabin(TileType, ConnectorType[], RotationType, int):
  ~ crew: ArrayList<Alive>
  + getCrew(): ArrayList<Alive>
  + addCrew(AliveType): void
  + removeCrew(): void
}
class Cannon {
  + Cannon(TileType, ConnectorType[], RotationType, int):
}
class Card {
  + Card(int, StateCardType):
  - level: int
  - stateCard: StateCardType
  + choice(Game, Player, boolean): void
  + removeBattery(Game, Player, BatteryStorage): void
  + getLevel(): int
  + getBoxesWon(): LinkedList<Box>
  + choiceCrew(Game, Player): void
  + holdSpaceship(Game, Player, int, int): void
  + setStateCard(StateCardType): void
  + createCard(): Card
  + choicePlanet(Game, Player, int): List<Box>
  + calculateDamage(Game, Player, Optional<BatteryStorage>): void
  + removeBox(Game, Player, SpecialStorage, BoxType): void
  + effect(Game): void
  + newCard(): Card
  + getStateCard(): StateCardType
  + removeCrew(Game, Player, Cabin): void
  + choiceBox(Game, Player, boolean): List<Box>
  + moveBox(Game, Player, List<Box>, List<Box>, Box, boolean): void
  + choiceDoubleMotor(Game, Player, Optional<List<Pair<DoubleMotor, BatteryStorage>>>): void
  + choiceDoubleCannon(Game, Player, Optional<List<Pair<DoubleCannon, BatteryStorage>>>): void
}
class CardDeck {
  + CardDeck():
  - deck: HashMap<Integer, Pair<List<Card>, Boolean>>
  - finalDeck: List<Card>
  + createFinalDeck(): List<Card>
  + giveDeck(int): List<Card>
  + playnextCard(): Card
  + giveCard(): Card
  + returnDeck(int): void
}
class Card_with_box {
  + Card_with_box(int, BoxStore, StateCardType):
  - store: BoxStore
  + getBoxStore(): BoxStore
}
class Dice {
  + Dice():
  + pickRandomNumber(): int
}
class DoubleCannon {
  + DoubleCannon(TileType, ConnectorType[], RotationType, int):
}
class DoubleMotor {
  + DoubleMotor(TileType, ConnectorType[], RotationType, int):
}
class Enemies {
  + Enemies(int, int, int, int, StateCardType):
  - daysLost: int
  - cannonPowers: int
  - credit: int
  + getCredit(): int
  + getDaysLost(): int
  + getCannonPowers(): int
}
class Epidemy {
  + Epidemy(int):
  + createCard(): Epidemy
  + effect(Game): void
}
class Game {
  + Game(List<Player>, int):
  - heapTile: HeapTiles
  - alreadyChecked: int
  - diceResult: int
  - maxAllowedPlayers: int
  - currentState: State
  - globalBoard: Gameboard
  - players: List<Player>
  - level: int
  - hourglass: Hourglass
  - deck: CardDeck
  - alreadyFinished: int
  - gameName: String
  + choicePlanet(Player, int): List<Box>
  + getWinners(): ArrayList<Player>
  + getGameboard(): Gameboard
  + returnTile(Player, Tile): void
  + shipFinished(Player): void
  + calculateDamage(Player, Optional<BatteryStorage>): void
  + getDeck(): CardDeck
  + moveBox(Player, List<Box>, List<Box>, Box, boolean): void
  + flipHourglass(): void
  + choiceDoubleCannon(Player, Optional<List<Pair<DoubleCannon, BatteryStorage>>>): void
  + takeTile(Player): Tile
  + choice(Player, boolean): void
  + holdSpaceship(Player, int, int): void
  + getMaxAllowedPlayers(): int
  + addTile(Player, Tile, int, int): void
  + getPosition(): HashMap<Player, Integer>
  + choiceDoubleMotor(Player, Optional<List<Pair<DoubleMotor, BatteryStorage>>>): void
  + addCrew(Player, int, int, AliveType): void
  + removeBattery(Player, BatteryStorage): void
  + getHeapTile(): HeapTiles
  + setDiceResult(): void
  + checkSpaceship(Player): boolean
  + choiceCrew(Player): void
  + removeCrew(Player, Cabin): void
  + removeTile(Player, int, int): void
  + possibleChoice(Player, TileType): List<Tile>
  + getHourglass(): Hourglass
  + getPlayers(): List<Player>
  + nextPlayer(): void
  + getCurrentState(): State
  + rollDice(Player): void
  + getDiceResult(): int
  + playNextCard(Player): void
  + GameCreator(List<Player>, int): Game
  + getVisibleTiles(): Set<Tile>
  + choiceBox(Player, boolean): List<Box>
  + getName(): String
  + getCurrentPlayer(): Player
  + removeBox(Player, SpecialStorage, BoxType): void
  + takeTile(Player, Tile): Tile
  + getlevel(): int
  + getCurrentCard(): Card
}
interface Game_Interface << interface >> {
  + addCrew(Player, int, int, AliveType): void
  + removeBox(Player, SpecialStorage, BoxType): void
  + getWinners(): ArrayList<Player>
  + choiceDoubleCannon(Player, Optional<List<Pair<DoubleCannon, BatteryStorage>>>): void
  + flipHourglass(): void
  + choiceCrew(Player): void
  + addTile(Player, Tile, int, int): void
  + calculateDamage(Player, Optional<BatteryStorage>): void
  + choice(Player, boolean): void
  + moveBox(Player, List<Box>, List<Box>, Box, boolean): void
  + returnTile(Player, Tile): void
  + removeTile(Player, int, int): void
  + rollDice(Player): void
  + playNextCard(Player): void
  + choicePlanet(Player, int): List<Box>
  + shipFinished(Player): void
  + choiceDoubleMotor(Player, Optional<List<Pair<DoubleMotor, BatteryStorage>>>): void
  + removeCrew(Player, Cabin): void
  + takeTile(Player, Tile): Tile
  + holdSpaceship(Player, int, int): void
  + removeBattery(Player, BatteryStorage): void
  + choiceBox(Player, boolean): List<Box>
  + GameCreator(List<Player>, int): Game
  + takeTile(Player): Tile
  + possibleChoice(Player, TileType): List<Tile>
  + getPosition(): HashMap<Player, Integer>
  + checkSpaceship(Player): boolean
}
class Gameboard {
  + Gameboard(int):
  ~ dice: Dice
  ~ positions: HashMap<Player, Integer>
  - numStep: int
  - startingPosition: int[]
  + getPositions(): HashMap<Player, Integer>
  + move(int, Player): void
  + getNumStep(): int
  + initializeGameBoard(List<Player>): void
  + getStartingPosition(): int[]
  + getDice(): Dice
  + getRanking(): LinkedList<Player>
}
class HeapTiles {
  + HeapTiles():
  - cabinStartPlayer: HashMap<String, Cabin>
  - setTiles: Set<Tile>
  - random: Random
  - JSON_FILE_PATH: String
  + getVisibleTiles(): Set<Tile>
  + addTile(Tile, boolean): void
  - loadTiles(): void
  + removeVisibleTile(Tile): void
  + drawTile(): Tile
}
class Hourglass {
  + Hourglass(long):
  - durata: long
  - running: boolean
  - timer: Timer
  - task: TimerTask
  - timeLeft: long
  + flip(): void
  + getTimeLeft(): long
}
class InitialCard {
  + InitialCard(int, StateCardType):
  + createCard(): InitialCard
}
class MeteoritesStorm {
  + MeteoritesStorm(int, ArrayList<Pair<Integer, RotationType>>):
  - currentIndex: int
  - meteorites: ArrayList<Pair<Integer, RotationType>>
  + calculateDamage(Game, Player, Optional<BatteryStorage>): void
  + holdSpaceship(Game, Player, int, int): void
  + createCard(): MeteoritesStorm
}
class Motors {
  + Motors(TileType, ConnectorType[], RotationType, int):
}
class OpenSpace {
  + OpenSpace(int):
  + choiceDoubleMotor(Game, Player, Optional<List<Pair<DoubleMotor, BatteryStorage>>>): void
  + createCard(): OpenSpace
}
class Pirate {
  + Pirate(int, int, int, int, ArrayList<Pair<Integer, RotationType>>):
  - currentIndex: int
  - losers: ArrayList<Player>
  - shots: ArrayList<Pair<Integer, RotationType>>
  - phase: int
  + createCard(): Pirate
  + choiceDoubleCannon(Game, Player, Optional<List<Pair<DoubleCannon, BatteryStorage>>>): void
  + calculateDamage(Game, Player, Optional<BatteryStorage>): void
  + holdSpaceship(Game, Player, int, int): void
  + choice(Game, Player, boolean): void
}
class Planet {
  + Planet(int, BoxStore, int, ArrayList<ArrayList<Box>>):
  - planetOffers: ArrayList<ArrayList<Box>>
  - landed: LinkedList<Player>
  - occupied: ArrayList<Integer>
  - daysLost: int
  + createCard(): Planet
  + getPlanetOffers(): ArrayList<ArrayList<Box>>
  + choicePlanet(Game, Player, int): List<Box>
  + moveBox(Game, Player, List<Box>, List<Box>, Box, boolean): void
}
class Player {
  + Player(Spaceship, String, PlayerColor):
  - spaceship: Spaceship
  - nickname: String
  - color: PlayerColor
  - statePlayer: StatePlayerType
  + getSpaceship(): Spaceship
  + getColor(): PlayerColor
  + getStatePlayer(): StatePlayerType
  + setStatePlayer(StatePlayerType): void
  + getNickname(): String
}
class PurpleCabin {
  + PurpleCabin(TileType, ConnectorType[], RotationType, int):
}
class Shield {
  + Shield(TileType, ConnectorType[], RotationType, int, boolean[]):
  ~ shielded: boolean[]
  + isShielded(RotationType): boolean
}
class SlaveOwner {
  + SlaveOwner(int, int, int, int, int):
  - AliveLost: int
  - AliveRemoved: int
  + createCard(): SlaveOwner
  + removeCrew(Game, Player, Cabin): void
  + choiceDoubleCannon(Game, Player, Optional<List<Pair<DoubleCannon, BatteryStorage>>>): void
  + choice(Game, Player, boolean): void
}
class Spaceship {
  + Spaceship(int):
  - spaceshipIterator: SpaceshipIterator
  - numOfWastedTiles: int
  - cosmicCredits: int
  - currentTile: Tile
  - x_start: int
  - y_start: int
  + calculateExposedConnectors(): int
  + isMostExpensive(BoxType): boolean
  + epidemyRemove(): void
  + getTile(int, int): Optional<Tile>
  + addCosmicCredits(int): void
  + removeCosmicCredits(int): void
  + meteoriteDamage(int, RotationType, int, Optional<BatteryStorage>): boolean
  + removeBattery(BatteryStorage): void
  + getCosmicCredits(): int
  + isExposed(RotationType, int): boolean
  + calculateMotorPower(List<DoubleMotor>): int
  + calculateNumAlive(): int
  + setCurrentTile(Tile): void
  + removeTile(int, int): void
  + getNumOfWastedTiles(): int
  + addNumOfWastedTiles(int): void
  + own(Tile): boolean
  + shotDamage(int, RotationType, int, Optional<BatteryStorage>): boolean
  + calculateCannonPower(List<DoubleCannon>): double
  + getSpaceshipIterator(): SpaceshipIterator
  + addTile(int, int, Tile): void
  + getCurrentTile(): Tile
  + getTilesByType(TileType): List<Tile>
  + isShielded(RotationType): boolean
  + noBox(): boolean
  + returnTile(): void
  + checkSpaceship(): boolean
  + holdSpaceship(int, int): void
}
class SpaceshipIterator {
  + SpaceshipIterator(int):
  - start_pos: Pair<Integer, Integer>
  - spaceshipBoard: Optional<Tile>[][]
  - x_current: int
  - y_current: int
  - JSON_FILE_PATH: String
  - spaceshipMask: boolean[][]
  + getY_start(): int
  + reference(): SpaceshipIterator
  + iterator(): Iterator<Optional<Tile>>
  + removeTile(int, int): void
  + getY(Tile): int
  + getX_start(): int
  - loadSpaceshipMask(int): void
  + getFrontTile(Tile): Optional<Tile>
  + getDownTile(Tile): Optional<Tile>
  + addTile(Tile, int, int): void
  + hasNext(): boolean
  + getTile(int, int): Optional<Tile>
  + getX(Tile): int
  + next(): Optional<Tile>
  + getUpTile(Tile): Optional<Tile>
  + getLeftTile(Tile): Optional<Tile>
  + getRightTile(Tile): Optional<Tile>
}
class SpecialStorage {
  + SpecialStorage(TileType, ConnectorType[], RotationType, int, int):
  ~ maxNum: int
  ~ occupation: ArrayList<Box>
  + getOccupation(): ArrayList<Box>
  + addBox(Box): void
  + getNumOccupation(): int
  + removeBox(Box): void
}
class Stardust {
  + Stardust(int):
  + createCard(): Stardust
  + effect(Game): void
}
class State {
  + State(List<Player>):
  - phase: StateGameType
  - currentCard: Card
  - currentPlayer: Player
  + getCurrentCard(): Card
  + setPhase(StateGameType): void
  + getPhase(): StateGameType
  + setCurrentCard(Card): void
  + setCurrentPlayer(Player): void
  + getCurrentPlayer(): Player
}
class Storage {
  + Storage(TileType, ConnectorType[], RotationType, int, int):
  ~ occupation: List<Box>
  ~ maxNum: int
  + removeBox(Box): void
  + getOccupation(): List<Box>
  + addBox(Box): void
  + getNumOccupation(): int
}
class Structural {
  + Structural(TileType, ConnectorType[], RotationType, int):
}
class Tile {
  + Tile(TileType, ConnectorType[], RotationType, int):
  - connectors: ConnectorType[]
  - visible: boolean
  - id: int
  - tType: TileType
  - rotationType: RotationType
  + getCrew(): ArrayList<Alive>
  + addCrew(AliveType): void
  + addBox(Box): void
  + isVisible(): boolean
  + removeBattery(): void
  + getId(): int
  + getOccupation(): List<Box>
  + connectorOnSide(RotationType): ConnectorType
  + getNumOccupation(): int
  + setVisible(): void
  + getRotationType(): RotationType
  + getNumBattery(): int
  + getConnectors(): ConnectorType[]
  + removeCrew(): void
  + setRotationType(RotationType): void
  + getType(): TileType
  + compatible(int, int): boolean
  + checkConnectors(Tile, RotationType): boolean
  + removeBox(Box): void
  + isShielded(RotationType): boolean
}
class Trafficker {
  + Trafficker(int, BoxStore, int, int, int, ArrayList<Box>):
  - cannonPowers: int
  - daysLost: int
  - boxesLost: int
  - boxesWon: ArrayList<Box>
  - boxesRemove: int
  + createCard(): Trafficker
  + moveBox(Game, Player, List<Box>, List<Box>, Box, boolean): void
  + choiceDoubleCannon(Game, Player, Optional<List<Pair<DoubleCannon, BatteryStorage>>>): void
  + removeBox(Game, Player, SpecialStorage, BoxType): void
  + choiceBox(Game, Player, boolean): List<Box>
}
class WarZone_I {
  + WarZone_I(int, int, int, ArrayList<Pair<Integer, RotationType>>):
  - declarationCrew: LinkedHashMap<Player, Integer>
  - flyback: int
  - declarationMotor: LinkedHashMap<Player, Integer>
  - currentPhase: int
  - currentIndex: int
  - aliveLost: int
  - aliveRemoved: int
  - shots: ArrayList<Pair<Integer, RotationType>>
  - declarationCannon: LinkedHashMap<Player, Double>
  + removeCrew(Game, Player, Cabin): void
  + createCard(): WarZone_I
  + choiceDoubleCannon(Game, Player, Optional<List<Pair<DoubleCannon, BatteryStorage>>>): void
  + choiceDoubleMotor(Game, Player, Optional<List<Pair<DoubleMotor, BatteryStorage>>>): void
  + calculateDamage(Game, Player, Optional<BatteryStorage>): void
  + holdSpaceship(Game, Player, int, int): void
  + getCurrentPhase(): int
  + choiceCrew(Game, Player): void
}
class WarZone_II {
  + WarZone_II(int, int, int, ArrayList<Pair<Integer, RotationType>>):
  - declarationCannon: LinkedHashMap<Player, Double>
  - currentPhase: int
  - declarationMotor: LinkedHashMap<Player, Integer>
  - flyback: int
  - declarationCrew: LinkedHashMap<Player, Integer>
  - currentIndex: int
  - shots: ArrayList<Pair<Integer, RotationType>>
  - boxesLost: int
  - boxesRemoved: int
  + removeBox(Game, Player, SpecialStorage, BoxType): void
  + holdSpaceship(Game, Player, int, int): void
  + removeBattery(Game, Player, BatteryStorage): void
  + choiceCrew(Game, Player): void
  + choiceDoubleCannon(Game, Player, Optional<List<Pair<DoubleCannon, BatteryStorage>>>): void
  + calculateDamage(Game, Player, Optional<BatteryStorage>): void
  + createCard(): WarZone_II
  + choiceDoubleMotor(Game, Player, Optional<List<Pair<DoubleMotor, BatteryStorage>>>): void
}

AbbandonedShip               -up-^  Card
AbbandonedStation            -up-^  Card_with_box
BatteryStorage               -up-^  Tile
BlueBox                      -up-^  Box
BrownCabin                   -up-^  Tile
Cabin                        -up-^  Tile
Cannon                       -up-^  Tile
Card_with_box                -up-^  Card
DoubleCannon                 -up-^  Tile
DoubleMotor                  -up-^  Tile
Enemies                      -up-^  Card
Epidemy                      -up-^  Card
Game                         -up-^  Game_Interface
GreenBox                     -up-^  Box
InitialCard                  -up-^  Card
MeteoritesStorm              -up-^  Card
Motors                       -up-^  Tile
OpenSpace                    -up-^  Card
Pirate                       -up-^  Enemies
Planet                       -up-^  Card_with_box
PurpleCabin                  -up-^  Tile
RedBox                       -up-^  Box
Shield                       -up-^  Tile
SlaveOwner                   -up-^  Enemies
SpecialStorage               -up-^  Tile
Stardust                     -up-^  Card
Storage                      -up-^  Tile
Structural                   -up-^  Tile
Trafficker                   -up-^  Card_with_box
WarZone_I                    -up-^  Card
WarZone_II                   -up-^  Card
YellowBox                    -up-^  Box

Game --> Hourglass
Game "2..4" o-- "1  " Player
Game --> State
Game -right> Gameboard
Game --> CardDeck
Game --> HeapTiles
CardDeck *-- Card
HeapTiles *-- Tile
Spaceship "1..n" o-- "1  " Tile
Spaceship --> SpaceshipIterator
Cabin --> Alive
Gameboard --> Dice
Card_with_box --> BoxStore
BoxStore *-- Box
Player -up> Spaceship

package "Enum"{
    enum AliveType << enumeration >> {
      + AliveType():
      + PURPLE_ALIEN:
      + BROWN_ALIEN:
      + HUMAN:
      + values(): AliveType[]
      + valueOf(String): AliveType
    }
    enum BoxType << enumeration >> {
      - BoxType(int):
      + BLUE:
      + YELLOW:
      + RED:
      + NONE:
      - power: int
      + GREEN:
      + getPower(): int
      + isSpecial(): boolean
      + valueOf(String): BoxType
      + values(): BoxType[]
      + getBoxTypeByNum(int): BoxType
      + getNumByTypeBox(BoxType): int
    }
    enum PlayerColor << enumeration >> {
      + PlayerColor():
      + YELLOW:
      + GREEN:
      + RED:
      + BLUE:
      + values(): PlayerColor[]
      + valueOf(String): PlayerColor
    }
    enum StateCardType << enumeration >> {
      + StateCardType():
      + BOXMANAGEMENT:
      + ROLL:
      + FINISH:
      + CHOICE_ATTRIBUTES:
      + DECISION:
      + REMOVE:
      + valueOf(String): StateCardType
      + values(): StateCardType[]
    }
    enum StateGameType << enumeration >> {
      + StateGameType():
      + EFFECT_ON_PLAYER:
      + BUILD:
      + CORRECTION:
      + INITIALIZATION_GAME:
      + START:
      + RESULT:
      + TAKE_CARD:
      + INITIALIZATION_SPACESHIP:
      + CHANGE_CONDITION:
      + CHECK:
      + values(): StateGameType[]
      + valueOf(String): StateGameType
    }
    enum StatePlayerType << enumeration >> {
      + StatePlayerType():
      + FINISHED:
      + IN_GAME:
      + OUT_GAME:
      + CORRECT_SHIP:
      + NOT_FINISHED:
      + WRONG_SHIP:
      + values(): StatePlayerType[]
      + valueOf(String): StatePlayerType
    }
    enum TileType << enumeration >> {
      + TileType():
      + CABIN:
      + SPECIAL_STORAGE:
      + D_CANNON:
      + CANNON:
      + PURPLE_CABIN:
      + BATTERY:
      + MOTOR:
      + STORAGE:
      + D_MOTOR:
      + STRUCTURAL:
      + SHIELD:
      + BROWN_CABIN:
      + values(): TileType[]
      + valueOf(String): TileType
    }
    enum RotationType << enumeration >> {
      - RotationType(int):
      + EAST:
      ~ numRotationType: int
      + SOUTH:
      + WEST:
      + NORTH:
      + getNum(): int
      + values(): RotationType[]
      + valueOf(String): RotationType
    }
    enum ConnectorType << enumeration >> {
      - ConnectorType(int):
      + DOUBLE:
      + UNIVERSAL:
      - numConnectorType: int
      + NONE:
      + SINGLE:
      + values(): ConnectorType[]
      + getNum(): int
      + compatible(ConnectorType): boolean
      + getConnectorTypeByNum(int): ConnectorType
      + valueOf(String): ConnectorType
    }
}
    class BlueBox {
      + BlueBox(BoxType):
    }
    class GreenBox {
      + GreenBox(BoxType):
    }
    class RedBox {
      + RedBox(BoxType):
    }
    class YellowBox {
      + YellowBox(BoxType):
    }
    class Box {
      + Box(BoxType):
      - type: BoxType
      - value: int
      + getValue(): int
      + getType(): BoxType
    }

package "Exception" {
    class AlreadyViewingTileException {
      + AlreadyViewingTileException():
    }
    class IllegalRemoveException {
      + IllegalRemoveException(String):
    }
}
@enduml
