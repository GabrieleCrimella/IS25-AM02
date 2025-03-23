package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.enumerations.*;
import it.polimi.ingsw.is25am02.model.exception.AlreadyViewingTileException;
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


    public void addTile(int x, int y, Tile t) {
        spaceshipIterator.addTile(t, x, y);
    }

    public Optional<Tile> getTile(int x, int y) {
        return spaceshipIterator.getTile(x, y);
    }

    //mi dovrebbe tornare una lista di liste di tile che compongono blocchi indipendenti della nave.
    //todo vedere se la tile che rimuovo fa togliere altre tiles e poi aumentare wastedtiles, controllare se si stacca un pezzo di nave e capire diq aunte tiles è fatto questo pezzo
    public List<List<Tile>> removeTile(int x, int y) { //chiamo quando il gioco è iniziato e perdo un pezzo perchè mi colpiscono
        List<List<Tile>> blocks = new LinkedList<>();
        int numBlocks = 0;

        List<Tile> remainingTiles = spaceshipIterator.returnAllTiles();
        Optional<Tile> current = spaceshipIterator.getFirstTile();

        List<Tile> willBeVisitedInAMoment = new LinkedList<>();
        while (remainingTiles.size() > 0) {
            List<Tile> block = new LinkedList<>();
            block.add(current.get());
            remainingTiles.remove(current.get());
            spaceshipIterator.getConnectedNearTiles(current.get()).forEach(t -> willBeVisitedInAMoment.add(t));
            while (willBeVisitedInAMoment.size() > 0) {
                Tile t = willBeVisitedInAMoment.get(0);
                willBeVisitedInAMoment.remove(0);
                for (RotationType r : RotationType.values()) {
                    Optional<Tile> next = spaceshipIterator.getTileInDirection(t, r);
                    if (next.isPresent() && remainingTiles.contains(next.get())) {
                        block.add(next.get());
                        willBeVisitedInAMoment.add(next.get());
                        remainingTiles.remove(next.get());
                    }
                }
            }
            blocks.add(block);
            numBlocks++;
            if (remainingTiles.size() > 0) {
                current = remainingTiles.get(0);
            }
        }
    }

    //todo durante la fase di costruzione se scarto una carta, rimetto la current tile nel heaptile
    //todo questo metodo però non vede heaptiles, è Game che deve gestire l'operazione: questo metodo ritorna currentTile, il Game lo rimette nell'heapTile
    public void returnTile() {
        currentTile = null;
    }

    public void setCurrentTile(Tile t) throws AlreadyViewingTileException {
        if (currentTile == null) {
            currentTile = t;
        } else {
            throw new AlreadyViewingTileException();
        }
    }

    public boolean isShielded(RotationType side) {
        for(Tile t : getTilesByType(TileType.SHIELD)){
            if(t.isShielded(side)){
                return true;
            }
        }
        return false;
    }

    public double calculateCannonPower(List<Tile> doubleCannons) {
        //calcola la potenza singola dei cannoni singoli contando l'orientazione e quella dei cannoni doppi contando l'orientazione
        double power = 0.0;

        for(Tile cannon : getTilesByType(TileType.CANNON)){
            if (cannon.getRotationType() == RotationType.NORTH) {
                power++;
            } else power+=0.5;
        }

        if(!doubleCannons.isEmpty()){
            for(Tile doubleCannon : doubleCannons){
                if(doubleCannon.getRotationType() == RotationType.NORTH){
                    power += 2.0;
                } else power += 1.0;
            }
        }

        if(power >0){ //ci deve essere almeno un cannone
            List<Tile> cabins = getTilesByType(TileType.CABIN);
            for(Tile cabin : cabins){
                if(cabin.getCrew().getFirst().getRace().equals(AliveType.PURPLE_ALIEN)){
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

        if(power > 0){
            for(Tile cabin : getTilesByType(TileType.CABIN)){
                if(cabin.getCrew().getFirst().getRace().equals(AliveType.BROWN_ALIEN)){
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
    public boolean meteoriteDamage(int bigOrSmall, RotationType rotationType, int num, Optional<BatteryStorage> storage) {
        switch(bigOrSmall){
            case 0: //Small
                if(isExposed(rotationType,num)){
                    if(isShielded(rotationType) && storage.isPresent() ){
                        storage.get().removeBattery();
                        removeTile(targetTileX, targetTileY);
                        return DividedSpaceship();
                    }
                } else return false;
            case 1: //Big
                Optional<Tile> target = targetTile(rotationType,num);
                if(target.isPresent()){
                    Optional<Tile> cannon = CoveredByWhatCannon(rotationType,num);
                    if(cannon.isPresent() && cannon.get().getType().equals(TileType.CANNON)){
                        return false;
                    }
                    else if(cannon.isPresent() && cannon.get().getType().equals(TileType.D_CANNON)){
                        return false;
                    }
                    else{
                        removeTile(targetTileX, targetTileY);
                        return DividedSpaceship();
                    }
                }
                else return false;
            default:
                return false;
        }
    }

    //todo controllare batterie
    public boolean shotDamage(int bigOrSmall, RotationType rotationType, int num, Optional<BatteryStorage> storage) {
        Optional<Tile> target = targetTile(rotationType,num);
        switch(bigOrSmall) {
            case 0: //Small
                if(target.isPresent()) {
                    if (!isShielded(rotationType)) {
                        removeTile(targetTileX, targetTileY);
                        return DividedSpaceship();
                    }
                } else return false;
            case 1: //Big
                if(target.isPresent()){
                    removeTile(targetTileX, targetTileY);
                    return DividedSpaceship();
                } else return false;
            default:
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
        for(Optional<Tile> t : spaceshipIterator.reference()){
            if(t.isPresent() && t.get().getType().equals(TileType.SPECIAL_STORAGE) && t.get().getOccupation()!=null){
                return type.equals(BoxType.RED);
            }
            else if(t.isPresent() && t.get().getType().equals(TileType.STORAGE)){
                if(numBestType < BoxType.getNumByTypeBox(t.get().getOccupation().getFirst().getType())){
                    numBestType = BoxType.getNumByTypeBox(t.get().getOccupation().getFirst().getType());//getOccupation ritorna una lista ordinata dal box più pregiato al meno
                }
            }
        }
        return BoxType.getBoxTypeByNum(numBestType).equals(type);
    }

    //todo controllare che sia corretto
    public void epidemyRemove() {// devo controllare che le tessere intorno siano cabine connesse, se si elimino un alive
        for (Optional<Tile> t : spaceshipIterator.reference()) {
            if (t.isPresent() && t.get().getType().equals(TileType.CABIN)) {
                if(spaceshipIterator.getUpTile(t.get()).isPresent() && (spaceshipIterator.getUpTile(t.get()).get().getType().equals(TileType.CABIN)) ||
                        spaceshipIterator.getDownTile(t.get()).isPresent() && (spaceshipIterator.getDownTile(t.get()).get().getType().equals(TileType.CABIN))  ||
                                spaceshipIterator.getLeftTile(t.get()).isPresent() && (spaceshipIterator.getLeftTile(t.get()).get().getType().equals(TileType.CABIN))||
                                        spaceshipIterator.getRightTile(t.get()).isPresent() && (spaceshipIterator.getRightTile(t.get()).get().getType().equals(TileType.CABIN))){
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
            if (t.get().getType().equals(type)) {
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
