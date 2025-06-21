package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.model.exception.AlreadyViewingException;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.*;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@SuppressWarnings("all")
public class Spaceship {
    private final SpaceshipIterator spaceshipIterator;
    private int numOfWastedTiles;
    private int cosmicCredits;
    private Tile currentTile;
    private int targetTileX, targetTileY;
    private final HashMap<Integer, Tile> bookedTiles;
    private List<boolean[][]> branches;
    private UpdateListener listener;
    private ConcurrentHashMap<String, VirtualView> observers;

    public Spaceship(int level) {
        this.spaceshipIterator = new SpaceshipIterator(level);
        this.numOfWastedTiles = 0;
        this.cosmicCredits = 0;
        this.bookedTiles = new HashMap<>();
        currentTile = null;
        branches = new ArrayList<>();

        bookedTiles.put(1, null);
        bookedTiles.put(2, null);
    }

    public void setObservers(ConcurrentHashMap<String, VirtualView> observers) {
        this.observers = observers;
    }

    public SpaceshipIterator getSpaceshipIterator() {
        return spaceshipIterator.reference();
    }

    public void setListener(UpdateListener listener) {
        this.listener = listener;
    }

    public HashMap<Integer, Tile> getBookedTiles() {
        return bookedTiles;
    }

    public void addTile(String nicknameP, int x, int y, Tile t) throws IllegalAddException {
        spaceshipIterator.addTile(nicknameP, t, x, y);
        currentTile = null;
    }

    public void addInitialTile(int x, int y, Tile t) throws IllegalAddException {
        spaceshipIterator.addInitialTile(t, x, y);
        currentTile = null;
    }

    public void bookTile(Player player) throws IllegalAddException {
        if (currentTile == null) {
            throw new IllegalAddException("CurrentTile is empty");
        } else if (bookedTiles.values().stream().filter(Objects::nonNull).count() == 2) {
            throw new IllegalAddException("BookedTile is full. Player " + player.getNickname() + " has " + bookedTiles.size() + " booked tiles");
        } else {
            if (bookedTiles.get(1) == null) {
                bookedTiles.put(1, currentTile);
            } else if (bookedTiles.get(2) == null) {
                bookedTiles.put(2, currentTile);
            }
            currentTile = null;
            for (String nick : observers.keySet()) {
                try {
                    observers.get(nick).showBookTileUpdate(player.getNickname());
                    observers.get(nick).showCurrentTileNullityUpdate(player.getNickname());
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method returnTile", e);
                }
            }
        }
    }

    public void addBookedTile(String nicknameP, int index, int x, int y, RotationType rotation) throws IllegalAddException {
        if (index < 1 || index > 2) {
            throw new IllegalAddException("index must be between 1 and 2");
        } else if (currentTile != null) {
            throw new IllegalAddException("You're trying to addbookedtile while currentTile is full");
        } else {
            addTile(nicknameP, x, y, bookedTiles.get(index));
            getTile(x, y).get().setRotationType(rotation);
            bookedTiles.put(index, null);
            for (String nick : observers.keySet()) {
                Coordinate pos = new Coordinate(x, y);
                try {
                    observers.get(nick).showBookedTileNullityUpdate(nicknameP, index, pos);
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method showbookedtilenullity update", e);
                }
            }
        }
    }

    public Optional<Tile> getTile(int x, int y) {
        return spaceshipIterator.getTile(x, y);
    }

    public void removeTile(String nicknameP, int x, int y) throws IllegalRemoveException { //chiamo quando il gioco è iniziato e perdo un pezzo perchè mi colpiscono
        if (spaceshipIterator.getTile(x, y).isEmpty()) {
            throw new IllegalRemoveException("There is no tile on (" + x + ", " + y + ")");
        } else {
            Tile toRemove = spaceshipIterator.getTile(x, y).get();
            List<Tile> up = new ArrayList<>();
            List<Tile> right = new ArrayList<>();
            List<Tile> down = new ArrayList<>();
            List<Tile> left = new ArrayList<>();

            if (spaceshipIterator.getUpTile(toRemove).isPresent() && toRemove.checkConnectorsforAttachedTiles(spaceshipIterator.getUpTile(toRemove).get(), RotationType.NORTH)) {
                up = startVisit(toRemove, RotationType.NORTH);
            }
            if (spaceshipIterator.getRightTile(toRemove).isPresent() && toRemove.checkConnectorsforAttachedTiles(spaceshipIterator.getRightTile(toRemove).get(), RotationType.EAST)) {
                right = startVisit(toRemove, RotationType.EAST);
            }
            if (spaceshipIterator.getDownTile(toRemove).isPresent() && toRemove.checkConnectorsforAttachedTiles(spaceshipIterator.getDownTile(toRemove).get(), RotationType.SOUTH)) {
                down = startVisit(toRemove, RotationType.SOUTH);
            }
            if (spaceshipIterator.getLeftTile(toRemove).isPresent() && toRemove.checkConnectorsforAttachedTiles(spaceshipIterator.getLeftTile(toRemove).get(), RotationType.WEST)) {
                left = startVisit(toRemove, RotationType.WEST);
            }

            if (spaceshipIterator.getTile(x, y).get().getType().equals(TileType.PURPLE_CABIN)) {
                alienCheck(x, y, AliveType.PURPLE_ALIEN);
            } else if (spaceshipIterator.getTile(x, y).get().getType().equals(TileType.BROWN_CABIN)) {
                alienCheck(x, y, AliveType.BROWN_ALIEN);
            }
            spaceshipIterator.removeOneTile(x, y);
            for (String nick : observers.keySet()) {
                try {
                    observers.get(nick).showTileRemoval(new Coordinate(x, y), nicknameP);
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method removeTile update", e);
                }
            }
            numOfWastedTiles++;

            List<List<Tile>> effectiveBlocks = new LinkedList<>();
            generateBlocks(up, effectiveBlocks);
            generateBlocks(right, effectiveBlocks);
            generateBlocks(down, effectiveBlocks);
            generateBlocks(left, effectiveBlocks);

            List<boolean[][]> booleanBlocks = new LinkedList<>();
            for (List<Tile> block : effectiveBlocks) {
                boolean[][] booleanBlock = new boolean[12][12];
                for (Tile t : block) {
                    booleanBlock[spaceshipIterator.getX(t)][spaceshipIterator.getY(t)] = true;
                }
                booleanBlocks.add(booleanBlock);
            }
            if (booleanBlocks.isEmpty() || booleanBlocks.size() == 1) {
                branches = null;
            } else {
                branches = booleanBlocks;
            }
        }
    }

    private void generateBlocks(List<Tile> toAdd, List<List<Tile>> containers) {
        if (!toAdd.isEmpty()) {
            boolean flag = false;
            if (!containers.isEmpty()) {
                for (List<Tile> container : containers) {
                    if (new HashSet<>(container).containsAll(toAdd) && new HashSet<>(toAdd).containsAll(container)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag)
                containers.add(toAdd);
        }
    }

    private List<Tile> startVisit(Tile toRemove, RotationType rotationType) {
        List<Tile> visited = new LinkedList<>();
        //visited.add(spaceshipIterator.getTileInDirection(toRemove, rotationType).get());
        Tile current = spaceshipIterator.getTileInDirection(toRemove, rotationType).get();

        List<Tile> toVisit = new ArrayList<>();
        toVisit.add(current);
        toVisit.addAll(spaceshipIterator.getConnectedNearTiles(current));
        toVisit.remove(toRemove);
        while (!toVisit.isEmpty()) {
            visited.add(current);
            toVisit.remove(current);
            if (!toVisit.isEmpty()) {
                current = toVisit.getFirst();
                List<Tile> newTiles = spaceshipIterator.getConnectedNearTiles(current);
                newTiles.forEach(t -> {
                    if (!visited.contains(t) && !toVisit.contains(t)) {
                        toVisit.add(t);
                    }
                });
            }
        }
        return visited;
    }

    //tiene le tiles passate come parametro sulla spaceshipe e aumenta le wastedTiles. Rimuovi i tiles
    public void keepBlock(String nicknameP, Coordinate pos) {
        boolean[][] rightMask = new boolean[0][0];
        for (boolean[][] mask : branches) {
            if (mask[pos.x()][pos.y()]) {
                rightMask = mask;
                break;
            }
        }
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && !rightMask[spaceshipIterator.getX(t.get())][spaceshipIterator.getY(t.get())]) {
                spaceshipIterator.removeOneTile(spaceshipIterator.getX(t.get()), spaceshipIterator.getY(t.get()));
                for (String nick : observers.keySet()) {
                    try {
                        observers.get(nick).showTileRemoval(new Coordinate(spaceshipIterator.getX(t.get()), spaceshipIterator.getY(t.get())), nicknameP);
                    } catch (RemoteException e) {
                        ServerController.logger.log(Level.SEVERE, "error in method keepBlock update", e);
                    }
                }
                addNumOfWastedTiles(1);
            }
        }
    }

    public void returnTile(String nicknameP) {
        currentTile = null;
        for (String nick : observers.keySet()) {
            try {
                observers.get(nick).showCurrentTileNullityUpdate(nicknameP);
            } catch (RemoteException e) {
                ServerController.logger.log(Level.SEVERE, "error in method returnTile", e);
            }
        }
        //listener.onCurrentTileNullityUpdate();
    }

    public void setCurrentTile(String nicknameP, Tile t) throws AlreadyViewingException {
        if (currentTile == null) {
            currentTile = t;
            for (String nick : observers.keySet()) {
                try {
                    observers.get(nick).showCurrentTileUpdate(t.getImagePath(), t.getConnectors(), t.getRotationType(), t.getType(), t.getNumMaxBattery(), t.getNumMaxBox(), nicknameP);
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method set current tile", e);
                }
            }
        } else {
            throw new AlreadyViewingException("CurrentTile already set");
        }
    }

    public boolean isShielded(RotationType side) {
        for (Tile t : getTilesByType(TileType.SHIELD)) {
            if (t.isShielded(side)) {
                return true;
            }
        }
        return false;
    }

    public double calculateCannonPower(List<Tile> doubleCannons) {
        //calcola la potenza singola dei cannoni singoli contando l'orientazione e quella dei cannoni doppi contando l'orientazione
        double power = 0.0;

        for (Tile cannon : getTilesByType(TileType.CANNON)) {
            if (cannon.getRotationType() == RotationType.NORTH) {
                power++;
            } else power += 0.5;
        }

        if (!doubleCannons.isEmpty()) {
            for (Tile doubleCannon : doubleCannons) {
                if (doubleCannon.getRotationType() == RotationType.NORTH) {
                    power += 2.0;
                } else power += 1.0;
            }
        }

        if (power > 0) { //ci deve essere almeno un cannone
            List<Tile> cabins = getTilesByType(TileType.CABIN);
            for (Tile cabin : cabins) {
                if (!cabin.getCrew().isEmpty() && cabin.getCrew().getFirst().race().equals(AliveType.PURPLE_ALIEN)) {
                    power = power + 2;
                    break;
                }
            }

        }
        return power;
    }

    public int calculateMotorPower(List<Tile> doubleMotors) {
        int power = 0;

        power += getTilesByType(TileType.MOTOR).size() + doubleMotors.size() * 2;

        if (power > 0) {
            for (Tile cabin : getTilesByType(TileType.CABIN)) {
                if (!cabin.getCrew().isEmpty() && cabin.getCrew().getFirst().race().equals(AliveType.BROWN_ALIEN)) {
                    power = power + 2;
                    break;
                }
            }
        }
        return power;
    }

    public int calculateExposedConnectors() {
        //What this method should do is iterate for all the tiles.
        //For each tile if the one above it is empty [x][y+1] you should call connectorsOnSide(RotationType.NORTH)
        //If this method returns ConnectorType.NONE you shouldn't add 1, if not add 1.
        //Same for all the other sides
        int exposedConnectors = 0;
        for (Optional<Tile> optionalTile : spaceshipIterator.reference()) {
            if (optionalTile.isPresent()) {
                Tile tile = optionalTile.get();
                //north
                if (spaceshipIterator.getUpTile(tile).isEmpty() &&
                        tile.connectorOnSide(RotationType.NORTH) != ConnectorType.NONE) {
                    exposedConnectors++;
                }
                //south
                if (spaceshipIterator.getDownTile(tile).isEmpty() &&
                        tile.connectorOnSide(RotationType.SOUTH) != ConnectorType.NONE) {
                    exposedConnectors++;
                }
                //east
                if (spaceshipIterator.getRightTile(tile).isEmpty() &&
                        tile.connectorOnSide(RotationType.EAST) != ConnectorType.NONE) {
                    exposedConnectors++;
                }
                //west
                if (spaceshipIterator.getLeftTile(tile).isEmpty() &&
                        tile.connectorOnSide(RotationType.WEST) != ConnectorType.NONE) {
                    exposedConnectors++;
                }
            }
        }
        return exposedConnectors;
    }

    public int getCosmicCredits() {
        return cosmicCredits;
    }

    public void addCosmicCredits(int numCosmicCredits) {
        cosmicCredits += numCosmicCredits;
        listener.onCreditUpdate(numCosmicCredits);
    }

    public void removeCosmicCredits(int numCosmicCredits) {
        cosmicCredits -= numCosmicCredits;
        listener.onCreditUpdate(numCosmicCredits);
    }

    public int getNumOfWastedTiles() {
        return numOfWastedTiles;
    }

    public void addNumOfWastedTiles(int num) {
        numOfWastedTiles += num;
    }

    public boolean checkSpaceship() {
        //controllo delle connessioni delle varie tiles
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && spaceshipIterator.getUpTile(t.get()).isPresent()) {
                if (!t.get().checkConnectors(spaceshipIterator.getUpTile(t.get()).get(), RotationType.NORTH)) {
                    return false;
                }
            }
        }
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && spaceshipIterator.getDownTile(t.get()).isPresent()) {
                if (!t.get().checkConnectors(spaceshipIterator.getDownTile(t.get()).get(), RotationType.SOUTH)) {
                    return false;
                }
            }
        }
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && spaceshipIterator.getRightTile(t.get()).isPresent()) {
                if (!t.get().checkConnectors(spaceshipIterator.getRightTile(t.get()).get(), RotationType.EAST)) {
                    return false;
                }
            }
        }
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && spaceshipIterator.getLeftTile(t.get()).isPresent()) {
                if (!t.get().checkConnectors(spaceshipIterator.getLeftTile(t.get()).get(), RotationType.WEST)) {
                    return false;
                }
            }
        }

        /*
         * controllo che i motori siano rivolti verso south, quindi, supponendo che
         * i motori siano orientati in modo standard verso SOUTH, nella loro posizione relativa standard,
         * quindi verso NORTH.
         */
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && (t.get().getType().equals(TileType.D_MOTOR) || t.get().getType().equals(TileType.MOTOR))) {
                if (!t.get().getRotationType().equals(RotationType.NORTH)) {
                    //NORTH cioè il motore NON è nella sua posizione standard"
                    return false;
                }

                //qui controllo che dietro un motore non ci sia nulla
                if (spaceshipIterator.getDownTile(t.get()).isPresent()) {
                    return false;
                }
            }
        }

        //controlla che davanti ai cannoni non ci sia nulla
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && (t.get().getType().equals(TileType.D_CANNON) || t.get().getType().equals(TileType.CANNON))) {
                if (spaceshipIterator.getFrontTile(t.get()).isPresent()) {
                    return false;
                }
            }
        }

        if (spaceshipIterator.getTileInStartingPosition().isEmpty())
            return false;

        Tile current = spaceshipIterator.getTileInStartingPosition().get();
        List<Tile> visited = new LinkedList<>();
        List<Tile> toVisit = new ArrayList<>();

        toVisit.add(current);
        if ((spaceshipIterator.getX(current)!=7||spaceshipIterator.getY(current)!=7)&&spaceshipIterator.getConnectedNearTiles(current).size()==0){
            return false;
        }
        toVisit.addAll(spaceshipIterator.getConnectedNearTiles(current));
        while (!toVisit.isEmpty()) {
            visited.add(current);
            toVisit.remove(current);
            if (!toVisit.isEmpty()) {
                current = toVisit.getFirst();
                if ((spaceshipIterator.getX(current)!=7||spaceshipIterator.getY(current)!=7) && spaceshipIterator.getConnectedNearTiles(current).size()==0){
                    return false;
                }
                List<Tile> newTiles = spaceshipIterator.getConnectedNearTiles(current);
                newTiles.forEach(t -> {
                    if (!visited.contains(t) && !toVisit.contains(t)) {
                        toVisit.add(t);
                    }
                });
            }
        }

        return visited.size() == spaceshipIterator.returnAllTiles().size();
    }

    public void removeBattery(BatteryStorage t) throws IllegalRemoveException {
        t.removeBattery();
        listener.onRemoveBatteryUpdate(t.getNumBattery(), new Coordinate(spaceshipIterator.getX(t), spaceshipIterator.getY(t)));
    }
    //dovrebbe essere girato giusto
    //il metodo controlla se è esposto un certo lato nella riga/colonna num
    public boolean isExposed(RotationType rotationType, int num) {
        if (rotationType == RotationType.NORTH) {
            for (int t = 0; t < 12; t++) {
                if (getTile(t, num).isPresent() && getTile(t, num).get().connectorOnSide(RotationType.NORTH) == ConnectorType.NONE) {
                    targetTileX = t;
                    targetTileY = num;
                    return false;
                } else if (getTile(t, num).isPresent() && getTile(t, num).get().connectorOnSide(RotationType.NORTH) != ConnectorType.NONE) {
                    targetTileX = t;
                    targetTileY = num;
                    return true;
                }
            }
        } else if (rotationType == RotationType.SOUTH) {
            for (int t = 12; t > 0; t--) {
                if (getTile(t, num).isPresent() && getTile(t, num).get().connectorOnSide(RotationType.SOUTH) != ConnectorType.NONE) {
                    targetTileX = t;
                    targetTileY = num;
                    return true;
                } else if (getTile(t, num).isPresent() && getTile(t, num).get().connectorOnSide(RotationType.NORTH) == ConnectorType.NONE) {
                    targetTileX = t;
                    targetTileY = num;
                    return false;
                }
            }
        } else if (rotationType == RotationType.EAST) {
            //for (int t = 0; t < 12; t++) {
            for (int t = 12; t > 0; t--) { //original
                if (getTile(num, t).isPresent() && getTile(num, t).get().connectorOnSide(RotationType.EAST) != ConnectorType.NONE) {
                    targetTileX = num;
                    targetTileY = t;
                    return true;
                } else if (getTile(num, t).isPresent() && getTile(num, t).get().connectorOnSide(RotationType.EAST) == ConnectorType.NONE) {
                    targetTileX = num;
                    targetTileY = t;
                    return false;
                }
            }
        } else if (rotationType == RotationType.WEST) {
            //for (int t = 12; t > 0; t--) {
            for (int t = 0; t < 12; t++) { //original
                if (getTile(num, t).isPresent() && getTile(num, t).get().connectorOnSide(RotationType.WEST) != ConnectorType.NONE) {
                    targetTileX = num;
                    targetTileY = t;
                    return true;
                } else if (getTile(num, t).isPresent() && getTile(num, t).get().connectorOnSide(RotationType.WEST) == ConnectorType.NONE) {
                    targetTileX = num;
                    targetTileY = t;
                    return false;
                }
            }
        }
        return false;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }// è la tile che sto guardando


    //calcola la distruzione della nave in base a dove è arrivato il meteorite
    //può essere che non ci sia damage perchè il num e la rotation non fanno male alla spaceship
    //ritorna 0 se la nave non è stata divisa in sotto parti
    //ritorna 1 se la nave si è divisa in varie parti
    public boolean meteoriteDamage(String nicknameP, int bigOrSmall, RotationType rotationType, int num, Optional<Tile> storage) throws IllegalRemoveException {
        switch (bigOrSmall) {
            case 0: //Small
                if (isExposed(rotationType, num)) {
                    if (isShielded(rotationType) && storage.isPresent()) {
                        storage.get().removeBattery();
                        for (String nick:observers.keySet()) {
                            try {
                                Coordinate pos = new Coordinate (getSpaceshipIterator().getX(storage.get()),getSpaceshipIterator().getY(storage.get()));
                                observers.get(nick).showBatteryRemoval(pos, nicknameP, storage.get().getNumBattery());
                            } catch (RemoteException e) {
                                ServerController.logger.log(Level.SEVERE, "error in method removeBattery", e);
                            }
                        }
                        return false;
                    } else {
                        removeTile(nicknameP, targetTileX, targetTileY);
                        return isSpaceshipDivided();
                    }
                } else return false;
            case 1: //Big
                Optional<Tile> target = targetTile(rotationType, num);
                if (target.isPresent()) {
                    Optional<Tile> cannon = CoveredByWhatCannon(rotationType, num);
                    if (cannon.isPresent() && cannon.get().getType().equals(TileType.CANNON)) {
                        return false;
                    } else if (cannon.isPresent() && cannon.get().getType().equals(TileType.D_CANNON) && storage.isPresent()) {
                        storage.get().removeBattery();
                        for (String nick:observers.keySet()) {
                            try {
                                Coordinate pos = new Coordinate (getSpaceshipIterator().getX(storage.get()),getSpaceshipIterator().getY(storage.get()));
                                observers.get(nick).showBatteryRemoval(pos, nicknameP, storage.get().getNumBattery());
                            } catch (RemoteException e) {
                                ServerController.logger.log(Level.SEVERE, "error in method removebattery", e);
                            }
                        }
                        return false;
                    } else {
                        removeTile(nicknameP, targetTileX, targetTileY);
                        return isSpaceshipDivided();
                    }
                } else return false;
            default:
                return false;
        }
    }

    public boolean shotDamage(String nicknameP, int bigOrSmall, RotationType rotationType, int num, Optional<Tile> storage) throws IllegalRemoveException {
        Optional<Tile> target = targetTile(rotationType, num);
        switch (bigOrSmall) {
            case 0: //Small
                if (target.isPresent()) {
                    if (!isShielded(rotationType) || !storage.isPresent()) {
                        removeTile(nicknameP, targetTileX, targetTileY);
                        return isSpaceshipDivided();
                    } else {
                        if (storage.isPresent()) {
                            storage.get().removeBattery();
                        }
                        for (String nick:observers.keySet()) {
                            try {
                                Coordinate pos = new Coordinate (getSpaceshipIterator().getX(storage.get()),getSpaceshipIterator().getY(storage.get()));
                                observers.get(nick).showBatteryRemoval(pos, nicknameP, storage.get().getNumBattery());
                            } catch (RemoteException e) {
                                ServerController.logger.log(Level.SEVERE, "error in method removeCrew", e);
                            }
                        }
                        return false;
                    }
                } else return false;
            case 1: //Big
                if (target.isPresent()) {
                    removeTile(nicknameP, targetTileX, targetTileY);
                    return isSpaceshipDivided();
                } else return false;
            default:
                return false;
        }
    }

    private boolean isSpaceshipDivided() {
        if (branches==null || branches.isEmpty() || branches.size()==1){
            return false;
        }
        return true;
    }

    public int calculateNumAlive() {
        int alive = 0;
        for (Tile cabin : getTilesByType(TileType.CABIN)) {
            alive += cabin.getCrew().size();
        }
        return alive;
    }

    public int calculateNumHuman() {
        int human = 0;
        for (Tile cabin : getTilesByType(TileType.CABIN)) {
            for (Alive alive : cabin.getCrew()) {
                if (alive.race().equals(AliveType.HUMAN)) {
                    human++;
                }
            }
        }
        return human;
    }

    // Mi dice se il tipo di box passato è al pari del blocco più pregiato della nave
    //se non ci sono box ritorno false, rosso giallo verde blu
    public boolean isMostExpensive(BoxType type) {
        int numType = BoxType.getNumByTypeBox(type);
        int numBestType = BoxType.getNumByTypeBox(BoxType.BLUE);
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && t.get().getType().equals(TileType.STORAGE) || t.get().getType().equals(TileType.SPECIAL_STORAGE)) {
                if (numBestType < BoxType.getNumByTypeBox(t.get().getOccupation().getFirst().getType())) {
                    numBestType = BoxType.getNumByTypeBox(t.get().getOccupation().getFirst().getType());//getOccupation ritorna una lista ordinata dal box più pregiato al meno
                }
            }
        }
        return BoxType.getBoxTypeByNum(numBestType).equals(type);
    }

    public void epidemyRemove(String nicknameP) throws IllegalRemoveException {// devo controllare che le tessere intorno siano cabine connesse, se si elimino un alive
        //per ogni cabina piena controlla se ha una cabina piena affiancata, se si e non è già nella lista, allora insierisci nella lista
        //dopo togli un umano/alieno da ogni cabina nella lista
        List<Tile> cabinAffected = new ArrayList<>();

        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && t.get().getType().equals(TileType.CABIN) && !t.get().getCrew().isEmpty()) {
                if (spaceshipIterator.getUpTile(t.get()).isPresent() && t.get().checkConnectorsforAttachedTiles(spaceshipIterator.getUpTile(t.get()).get(), RotationType.NORTH)
                        && spaceshipIterator.getUpTile(t.get()).get().getType().equals(TileType.CABIN) &&
                        !spaceshipIterator.getUpTile(t.get()).get().getCrew().isEmpty()) {
                    cabinAffected.add(t.get());
                }
                if (spaceshipIterator.getRightTile(t.get()).isPresent() && t.get().checkConnectorsforAttachedTiles(spaceshipIterator.getRightTile(t.get()).get(), RotationType.EAST)
                        && spaceshipIterator.getRightTile(t.get()).get().getType().equals(TileType.CABIN) &&
                        !spaceshipIterator.getRightTile(t.get()).get().getCrew().isEmpty()) {
                    cabinAffected.add(t.get());
                }
                if (spaceshipIterator.getDownTile(t.get()).isPresent() && t.get().checkConnectorsforAttachedTiles(spaceshipIterator.getDownTile(t.get()).get(), RotationType.SOUTH)
                        && spaceshipIterator.getDownTile(t.get()).get().getType().equals(TileType.CABIN) &&
                        !spaceshipIterator.getDownTile(t.get()).get().getCrew().isEmpty()) {
                    cabinAffected.add(t.get());
                }
                if (spaceshipIterator.getLeftTile(t.get()).isPresent() && t.get().checkConnectorsforAttachedTiles(spaceshipIterator.getLeftTile(t.get()).get(), RotationType.WEST)
                        && spaceshipIterator.getLeftTile(t.get()).get().getType().equals(TileType.CABIN) &&
                        !spaceshipIterator.getLeftTile(t.get()).get().getCrew().isEmpty()) {
                    cabinAffected.add(t.get());
                }
            }
        }
        if (!cabinAffected.isEmpty()) {
            for (Tile cabin : cabinAffected) {
                cabin.removeCrew();
                for (String nick:observers.keySet()) {
                    Coordinate pos = new Coordinate (getSpaceshipIterator().getX(cabin),getSpaceshipIterator().getY(cabin));
                    try {
                        observers.get(nick).showCrewRemoval(pos, nicknameP);
                    } catch (RemoteException e) {
                        ServerController.logger.log(Level.SEVERE, "error in method removeCrew", e);
                    }
                }
            }
        }
    }

    // ritorno 1 se non ci sono box sulla nave, 0 altrimenti
    public boolean noBox() {
        for (Tile t : getTilesByType(TileType.STORAGE)) {
            if (!t.getOccupation().isEmpty()) {
                return false;
            }
        }
        for (Tile t : getTilesByType(TileType.SPECIAL_STORAGE)) {
            if (!t.getOccupation().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public List<Tile> getTilesByType(TileType type) {
        List<Tile> temp = new ArrayList<>();
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && t.get().getType().equals(type)) {
                temp.add(t.get());
            }
        }
        return temp;
    }


    private Optional<Tile> targetTile(RotationType type, int num) {
        targetTileX = 0;
        targetTileY = 0;

        if (type == RotationType.NORTH) {
            for (int t = 0; t < 12; t++) {
                if (getTile(t, num).isPresent()) {
                    targetTileX = t;
                    targetTileY = num;
                    return getTile(t,num);
                }
            }
        } else if (type == RotationType.SOUTH) {
            for (int t = 12; t > 0; t--) {
                if (getTile(t, num).isPresent()) {
                    targetTileX = t;
                    targetTileY = num;
                    return getTile(t, num);
                }
            }
        } else if (type == RotationType.EAST) {
            for (int t = 12; t > 0; t--) {
                if (getTile(num, t).isPresent()) {
                    targetTileX = num;
                    targetTileY = t;
                    return getTile(num, t);
                }
            }
        } else if (type == RotationType.WEST) {
            for (int t = 0; t < 12; t++) {
                if (getTile(num, t).isPresent()) {
                    targetTileX = num;
                    targetTileY = t;
                    return getTile(num, t);
                }
            }
        }
        return Optional.empty();
    }

    //Mi dice se ho un cannone che può sparare al meteorite e mi passa il cannone
    private Optional<Tile> CoveredByWhatCannon(RotationType type, int num) {
        if (type == RotationType.NORTH) {
            for (int t = 0; t < 11; t++) {
                if (getTile(t, num).isPresent() && (getTile(t, num).get().getType().equals(TileType.CANNON)
                        || getTile(t, num).get().getType().equals(TileType.D_CANNON))) {
                    return getTile(t, num);
                } else if (getTile(t, num).isPresent() && !getTile(t, num).get().getType().equals(TileType.CANNON)
                        && !getTile(t, num).get().getType().equals(TileType.D_CANNON)) {
                    return Optional.empty();
                }
            }
        } else if (type == RotationType.SOUTH) {
            for (int t = 11; t > 0; t--) {
                if (getTile(t, num).isPresent() && (getTile(t, num).get().getType().equals(TileType.CANNON)
                        || getTile(t, num).get().getType().equals(TileType.D_CANNON))) {
                    return getTile(t, num);
                } else if (getTile(t, num).isPresent() && !getTile(t, num).get().getType().equals(TileType.CANNON)
                        && !getTile(t, num).get().getType().equals(TileType.D_CANNON)) {
                    return Optional.empty();
                }
            }
        } else if (type == RotationType.EAST) {
            for (int t = 11; t > 0; t--) {
                if (getTile(num, t).isPresent() && (getTile(num, t).get().getType().equals(TileType.CANNON)
                        || getTile(num, t).get().getType().equals(TileType.D_CANNON))) {
                    return getTile(num, t);
                } else if (getTile(num, t).isPresent() && !getTile(num, t).get().getType().equals(TileType.CANNON)
                        && !getTile(num, t).get().getType().equals(TileType.D_CANNON)) {
                    return Optional.empty();
                }
            }
        } else if (type == RotationType.WEST) {
            for (int t = 0; t < 11; t++) {
                if (getTile(num, t).isPresent() && (getTile(num, t).get().getType().equals(TileType.CANNON)
                        || getTile(num, t).get().getType().equals(TileType.D_CANNON))) {
                    return getTile(num, t);
                } else if (getTile(num, t).isPresent() && !getTile(num, t).get().getType().equals(TileType.CANNON)
                        && !getTile(num, t).get().getType().equals(TileType.D_CANNON)) {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }

    private void alienCheck(int x, int y, AliveType type) {
        List<Tile> near = spaceshipIterator.getConnectedNearTiles(spaceshipIterator.getTile(x, y).get());
        for (Tile t : near) {
            if (t.getType().equals(TileType.CABIN) && t.getCrew().getFirst().race().equals(type)) {
                //devo controllare che nelle cabine connesse vicine a questa t ci sia un cabina per alieni diversa da quella che
                //sta per essere distrutta
                List<Tile> neighbors = spaceshipIterator.getConnectedNearTiles(t);
                for (Tile neighbor : neighbors) {
                    if (type.equals(AliveType.PURPLE_ALIEN) && neighbor.getType().equals(TileType.PURPLE_CABIN) &&
                            spaceshipIterator.getX(neighbor) != x && spaceshipIterator.getY(neighbor) != y) {
                        return; //va tutto bene nessun alieno deve abbandonare la nave
                    }
                    if (type.equals(AliveType.BROWN_ALIEN) && neighbor.getType().equals(TileType.BROWN_CABIN) &&
                            spaceshipIterator.getX(neighbor) != x && spaceshipIterator.getY(neighbor) != y) {
                        return; //va tutto bene nessun alieno deve abbandonare la nave
                    }
                }
                t.getCrew().removeFirst(); //tolgo l'alieno
                return;
            }
        }
    }
}
