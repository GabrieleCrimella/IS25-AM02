package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.enumerations.*;
//import it.polimi.ingsw.is25am02.model.exception.AlreadyViewingTileException;
import it.polimi.ingsw.is25am02.model.exception.AlreadyViewingException;
import it.polimi.ingsw.is25am02.model.exception.IllegalAddException;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.*;

import java.util.*;

public class Spaceship {
    private final SpaceshipIterator spaceshipIterator;
    private int numOfWastedTiles;
    private int cosmicCredits;
    private Tile currentTile;
    private int x_start, y_start;
    private int targetTileX, targetTileY;

    public Spaceship(int level) {
        this.spaceshipIterator = new SpaceshipIterator(level);
        this.numOfWastedTiles = 0;
        this.cosmicCredits = 0;
        currentTile = null;
    }

    public SpaceshipIterator getSpaceshipIterator() {
        return spaceshipIterator.reference();
    }

    //serve solo per il testing, per vedere che la legge correttamente da json
    /*
     * public boolean[][] getMaskSpaceship() {
     *    return maskSpaceship;
     * }
     */


    public void addTile(int x, int y, Tile t) throws IllegalAddException {
        spaceshipIterator.addTile(t, x, y);
    }

    public Optional<Tile> getTile(int x, int y) {
        return spaceshipIterator.getTile(x, y);
    }

    //mi dovrebbe tornare una lista di liste di tile che compongono blocchi indipendenti della nave.
    //vedere se la tile che rimuovo fa togliere altre tiles e poi aumentare wastedtiles, controllare se si stacca un pezzo di nave e capire di qaunte tiles è fatto questo pezzo
    public Optional<List<boolean[][]>> removeTile(int x, int y) throws IllegalRemoveException { //chiamo quando il gioco è iniziato e perdo un pezzo perchè mi colpiscono
        if(spaceshipIterator.getTile(x, y).isEmpty()) {
            throw new IllegalRemoveException("There is no tile on (" + x + ", " + y + ")");
        }
        else {
            Tile toRemove = spaceshipIterator.getTile(x, y).get();
            List<Tile> up = new ArrayList<>();
            List<Tile> right = new ArrayList<>();
            List<Tile> down = new ArrayList<>();
            List<Tile> left = new ArrayList<>();

            if (spaceshipIterator.getUpTile(toRemove).isPresent() && toRemove.checkConnectors(spaceshipIterator.getUpTile(toRemove).get(), RotationType.NORTH)) {
                up = startVisit(toRemove, RotationType.NORTH);
            }
            if (spaceshipIterator.getRightTile(toRemove).isPresent() && toRemove.checkConnectors(spaceshipIterator.getRightTile(toRemove).get(), RotationType.EAST)) {
                right = startVisit(toRemove, RotationType.EAST);
            }
            if (spaceshipIterator.getDownTile(toRemove).isPresent() && toRemove.checkConnectors(spaceshipIterator.getDownTile(toRemove).get(), RotationType.SOUTH)) {
                down = startVisit(toRemove, RotationType.SOUTH);
            }
            if (spaceshipIterator.getLeftTile(toRemove).isPresent() && toRemove.checkConnectors(spaceshipIterator.getLeftTile(toRemove).get(), RotationType.SOUTH)) {
                left = startVisit(toRemove, RotationType.WEST);
            }

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
            if (booleanBlocks.isEmpty()) {
                return Optional.empty();
            }
            else {
                return Optional.of(booleanBlocks);
            }
        }
    }

    private void generateBlocks(List<Tile> toAdd, List<List<Tile>> containers) {
        if (!toAdd.isEmpty()) {
            boolean flag = false;
            if (containers.size() > 0) {
                for (int i = 0; i < containers.size(); i++) {
                    if (containers.get(i).containsAll(toAdd) && toAdd.containsAll(containers.get(i))) {
                        flag = true;
                    }
                }
            }
            if (flag == false)
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
    public void keepBlock(boolean[][] tilesToKeep) {
        for(Optional<Tile> t : spaceshipIterator.reference()){
            if(t.isPresent() && !tilesToKeep[spaceshipIterator.getX(t.get())][spaceshipIterator.getY(t.get())]){
                spaceshipIterator.removeOneTile(spaceshipIterator.getX(t.get()), spaceshipIterator.getY(t.get()));
                addNumOfWastedTiles(1);
            }
        }
        //controllare che gli alieni valgono
        if (calculateNumAlive()<=0){
            //todo eccezione che è morta la nave
            //todo ma non qui, bisogna controllare da qualche parte se la nave non ha più tiles
        }

    }

    public void returnTile() {
        currentTile = null;
    }

    public void setCurrentTile(Tile t) throws AlreadyViewingException{
        if (currentTile == null) {
            currentTile = t;
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
                if (cabin.getCrew().getFirst().getRace().equals(AliveType.PURPLE_ALIEN)) {
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
                if (cabin.getCrew().getFirst().getRace().equals(AliveType.BROWN_ALIEN)) {
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
    }

    public void removeCosmicCredits(int numCosmicCredits) {
        cosmicCredits -= numCosmicCredits;
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
        for (Optional<Tile> t : spaceshipIterator) {
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
        return true;
    }

    public void removeBattery(BatteryStorage t) {
        t.removeBattery();
    }

    //il metodo controlla se è esposto un certo lato nella riga/colonna num
    public boolean isExposed(RotationType rotationType, int num) {
        if(rotationType==RotationType.NORTH){
            for(int t=0; t<12;t++){
                if(getTile(num,t).isPresent() && getTile(num,t).get().connectorOnSide(RotationType.NORTH)!=ConnectorType.NONE){
                    targetTileX = num;
                    targetTileY = t;
                    return true;
                }
            }
        }
        else if(rotationType==RotationType.SOUTH){
            for(int t=12; t>0;t--){
                if(getTile(num,t).isPresent() && getTile(num,t).get().connectorOnSide(RotationType.SOUTH)!=ConnectorType.NONE){
                    targetTileX = num;
                    targetTileY = t;
                    return true;
                }
            }
        }
        else if(rotationType==RotationType.EAST){
            for(int t=12; t>0;t--){
                if(getTile(t,num).isPresent() && getTile(t,num).get().connectorOnSide(RotationType.EAST)!=ConnectorType.NONE){
                    targetTileX = t;
                    targetTileY = num;
                    return true;
                }
            }
        }
        else if(rotationType==RotationType.WEST){
            for(int t=0; t<12;t++){
                if(getTile(t,num).isPresent() && getTile(t,num).get().connectorOnSide(RotationType.WEST)!=ConnectorType.NONE){
                    targetTileX = t;
                    targetTileY = num;
                    return true;
                }
            }
        }
        return false;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }// è la tile che sto guardando


    //todo da controllare
    //todo bisogna gestire il fatto che se viene colpito un supporto vitale allora va rimosso l'alieno nella cabina vicino
    //calcola la distruzione della nave in base a dove è arrivato il meteorite
    //può essere che non ci sia damage perchè il num e la rotation non fanno male alla spaceship
    //ritorna 0 se la nave non è stata divisa in sotto parti
    //ritorna 1 se la nave si è divisa in varie parti
    public boolean meteoriteDamage(int bigOrSmall, RotationType rotationType, int num, Optional<BatteryStorage> storage){
        try {
            switch (bigOrSmall) {
                case 0: //Small
                    if (isExposed(rotationType, num)) {
                        if (isShielded(rotationType) && storage.isPresent()) {
                            storage.get().removeBattery();
                            removeTile(targetTileX, targetTileY);
                            return DividedSpaceship();
                        }
                    } else return false;
                case 1: //Big
                    Optional<Tile> target = targetTile(rotationType, num);
                    if (target.isPresent()) {
                        Optional<Tile> cannon = CoveredByWhatCannon(rotationType, num);
                        if (cannon.isPresent() && cannon.get().getType().equals(TileType.CANNON)) {
                            return false;
                        } else if (cannon.isPresent() && cannon.get().getType().equals(TileType.D_CANNON)) {
                            return false;
                        } else {
                            removeTile(targetTileX, targetTileY);
                            return DividedSpaceship();
                        }
                    } else return false;
                default:
                    return false;
            }
        } catch (IllegalRemoveException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    //todo controllare batterie
    public boolean shotDamage(int bigOrSmall, RotationType rotationType, int num, Optional<BatteryStorage> storage) {
        try {
            Optional<Tile> target = targetTile(rotationType, num);
            switch (bigOrSmall) {
                case 0: //Small
                    if (target.isPresent()) {
                        if (!isShielded(rotationType)) {
                            removeTile(targetTileX, targetTileY);
                            return DividedSpaceship();
                        }
                    } else return false;
                case 1: //Big
                    if (target.isPresent()) {
                        removeTile(targetTileX, targetTileY);
                        return DividedSpaceship();
                    } else return false;
                default:
                    return false;
            }
        } catch (IllegalRemoveException e){
            System.out.println(e.getMessage());
            return false;
        }

    }

    public int calculateNumAlive() {
        int alive = 0;
        for (Tile cabin : getTilesByType(TileType.CABIN)) {
            alive += cabin.getCrew().size();
        }
        return alive;
    }

    //Mi dice se la tile appartiene alla nave
    public boolean own(Tile tile) {
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && t.get().equals(tile)) {
                return true;
            }
        }
        return false;
    }


    // Mi dice se il tipo di box passato è al pari del blocco più pregiato della nave
    //se non ci sono box ritorno false, rosso giallo verde blu
    public boolean isMostExpensive(BoxType type) {
        int numType = BoxType.getNumByTypeBox(type);
        int numBestType = BoxType.getNumByTypeBox(BoxType.BLUE);
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && t.get().getType().equals(TileType.SPECIAL_STORAGE) && t.get().getOccupation() != null) {
                return type.equals(BoxType.RED);
            } else if (t.isPresent() && t.get().getType().equals(TileType.STORAGE)) {
                if (numBestType < BoxType.getNumByTypeBox(t.get().getOccupation().getFirst().getType())) {
                    numBestType = BoxType.getNumByTypeBox(t.get().getOccupation().getFirst().getType());//getOccupation ritorna una lista ordinata dal box più pregiato al meno
                }
            }
        }
        return BoxType.getBoxTypeByNum(numBestType).equals(type);
    }

    //todo controllare che sia corretto
    public void epidemyRemove() throws IllegalRemoveException {// devo controllare che le tessere intorno siano cabine connesse, se si elimino un alive
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && t.get().getType().equals(TileType.CABIN)) {
                if (spaceshipIterator.getUpTile(t.get()).isPresent() && (spaceshipIterator.getUpTile(t.get()).get().getType().equals(TileType.CABIN)) ||
                        spaceshipIterator.getDownTile(t.get()).isPresent() && (spaceshipIterator.getDownTile(t.get()).get().getType().equals(TileType.CABIN)) ||
                        spaceshipIterator.getLeftTile(t.get()).isPresent() && (spaceshipIterator.getLeftTile(t.get()).get().getType().equals(TileType.CABIN)) ||
                        spaceshipIterator.getRightTile(t.get()).isPresent() && (spaceshipIterator.getRightTile(t.get()).get().getType().equals(TileType.CABIN))) {
                    t.get().removeCrew();
                }
            }
        }
    }


    //todo mantiene il pezzo di nave con la tile nella posizione (x,y), tutti i pezzi rimossi sono messi eliminati e si
    //aggiunge +1 per ogni pezzo al contatore degli scarti
    public void holdSpaceship(int x, int y) {
    }


    // ritorno 1 se non ci sono box sulla nave, 0 altrimenti
    public boolean noBox() {
        for (Tile t : getTilesByType(TileType.STORAGE)) {
            if (t.getOccupation() != null) {
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

    //A fronte della rimozione di una tile mi dice se ho vari rami o solo 1
    private boolean DividedSpaceship(){
        return false;
    }

    private Optional<Tile> targetTile(RotationType type, int num) {
        targetTileX = 0;
        targetTileY = 0;

        if(type == RotationType.NORTH){
            for(int t=0; t<12;t++){
                if(getTile(num,t).isPresent()){
                    targetTileX = num;
                    targetTileY = t;
                    return getTile(num,t);
                }
            }
        }
        else if(type==RotationType.SOUTH){
            for(int t=12; t>0;t--){
                if(getTile(num,t).isPresent()){
                    targetTileX = num;
                    targetTileY = t;
                    return getTile(num,t);
                }
            }
        }
        else if(type==RotationType.EAST){
            for(int t=12; t>0;t--){
                if(getTile(t,num).isPresent()){
                    targetTileX = t;
                    targetTileY = num;
                    return getTile(t,num);
                }
            }
        }
        else if(type==RotationType.WEST){
            for(int t=0; t<12;t++){
                if(getTile(t,num).isPresent()){
                    targetTileX = t;
                    targetTileY = num;
                    return getTile(t,num);
                }
            }
        }
        return Optional.empty();
    }

    //Mi dice se ho un cannone che può sparare al meteorite e mi passa il cannone
    private Optional<Tile> CoveredByWhatCannon(RotationType type, int num) {
        return Optional.empty();
    }
}
