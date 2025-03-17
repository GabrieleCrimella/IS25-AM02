package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.exception.AlreadyViewingTileException;
import it.polimi.ingsw.is25am02.model.tiles.*;

import java.util.*;

import static java.lang.Integer.min;

public class Spaceship {
    private final SpaceshipIterator spaceshipIterator;
    private int numOfWastedTiles;
    private int cosmicCredits;
    private Tile currentTile;
    private int x_start, y_start;

    public Spaceship(int level) {
        this.spaceshipIterator = new SpaceshipIterator(level);
        this.numOfWastedTiles = 0;
        this.cosmicCredits = 0;
        currentTile = null;
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

    //todo vedere se la tile che rimuovo fa togliere altre tiles e poi aumentare wastedtiles, controllare se si stacca un pezzo di nave e capire diq aunte tiles è fatto questo pezzo
    public void removeTile(int x, int y) { //chiamo quando il gioco è iniziato e perdo un pezzo perchè mi colpiscono
        spaceshipIterator.removeTile(x, y);
        numOfWastedTiles++; //todo da togliere
    }

    //todo durante la fase di costruzione se scarto una carta, rimetto la current tile nel heaptile
    //todo questo metodo però non vede heaptiles, è Game che deve gestire l'operazione: questo metodo ritorna currentTile, il Game lo rimette nell'heapTile
    public void returnTile() {
        currentTile = null;
    }

    public void setCurrentTile(Tile t) throws AlreadyViewingTileException {
        if(currentTile==null){
            currentTile = t;
        }else {
            throw new AlreadyViewingTileException();
        }
    }

    public boolean isShielded(RotationType side) {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (spaceshipIterator.getTile(i, j).isPresent() && spaceshipIterator.getTile(i, j).get().getType().equals(TileType.SHIELD)) {
                    Shield cur = (Shield) spaceshipIterator.getTile(i, j).get();
                    if (cur.isShielded(side)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //todo: ritorna i cannoni doppi che può usare il giocatore
    public List<DoubleCannon> getNumOfDoubleCannon() {
        List<DoubleCannon> doubleCannons = new ArrayList<>();
        for (Optional<Tile> tile : spaceshipIterator) {
            if (tile.isPresent() && tile.get().getType().equals(TileType.D_CANNON)) {
                doubleCannons.add((DoubleCannon) tile.get()); //todo c'è cast!!!!
            }
        }
        return doubleCannons;
    }

    //todo: metodo che controlla se c'è un cannone
    public boolean isCannonPresent(RotationType side, int num) {
        return false;
    }

    //todo: metodo che controllo se c'è un doppio cannone
    //qui la batteria viene utilizzata
    public boolean isDoubleCannonPresent(RotationType side, int num) {
        return false;
    }

    //todo: fare il metodo, aggiungere a UML che VA AGGIUNTA UNA firma (overload) del metodo e che ho aggiunto il metodo getNumOfDoubleCannon
    public double calculateCannonPower(List<DoubleCannon> doubleCannons) {
        //calcola la potenza singola dei cannoni singoli contando l'orientazione e quella dei cannoni doppi contando l'orientazione
        double power = 0.0;

        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && t.get().getType().equals(TileType.CANNON)) {
                // se è rivolto davanti ha punteggio pieno
                if (t.get().getRotationType() == RotationType.NORTH) {
                    power++;
                } else if(t.get().getRotationType() == RotationType.SOUTH || t.get().getRotationType() == RotationType.EAST || t.get().getRotationType() == RotationType.WEST) {
                    power+=0.5;
                }
            }
            else if (t.isPresent() && t.get().getType().equals(TileType.D_CANNON)) {
                // se è rivolto davanti ha punteggio pieno
                if (t.get().getRotationType() == RotationType.NORTH) {
                    power+= 2.0;
                } else if(t.get().getRotationType() == RotationType.SOUTH || t.get().getRotationType() == RotationType.EAST || t.get().getRotationType() == RotationType.WEST) {
                    power++;
                }
            }
        }

        return power;
    }


    public int calculateMotorPower(List<DoubleMotor> doubleMotors) {
        int power = 0;

        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && t.get().getType().equals(TileType.MOTOR)) {
                power++;
            }
        }

        return power + doubleMotors.size() * 2;
    }

    //ritorna i motori doppi che può usare il giocatore
    public List<DoubleMotor> getNumOfDoubleMotor() {
        List<DoubleMotor> doubleMotor = new ArrayList<>();
        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && t.get().getType().equals(TileType.D_MOTOR)) {
                doubleMotor.add((DoubleMotor) t.get());
            }
        }
        return doubleMotor;
    }

    public int calculateExposedConnectors() {
        //Gabri non so come si usa iterator
        //Quello che deve fare questo metodo e iterare per tutti i tile.
        //Per ogni tile se quello sopra di lui è vuoto [x][y+1] devi chiamare connectorsOnSide(RotationType.NORTH)
        //Se questo metodo ti ritorna ConnectorType.NONE non devi aggiungere 1, se no aggiungi 1.
        //Uguale per tutti gli altri lati
        int exposedConnectors = 0;
        for(Optional<Tile> optionalTile : spaceshipIterator){
            if(optionalTile.isPresent()){
                Tile tile = optionalTile.get();
                //north
                if(spaceshipIterator.getUpTile(tile).isEmpty() &&
                        tile.connectorOnSide(RotationType.NORTH)!= ConnectorType.NONE){
                    exposedConnectors++;
                }
                //south
                if(spaceshipIterator.getDownTile(tile).isEmpty() &&
                        tile.connectorOnSide(RotationType.SOUTH)!= ConnectorType.NONE){
                    exposedConnectors++;
                }
                //east
                if(spaceshipIterator.getRightTile(tile).isEmpty() &&
                        tile.connectorOnSide(RotationType.EAST)!= ConnectorType.NONE){
                    exposedConnectors++;
                }
                //west
                if(spaceshipIterator.getLeftTile(tile).isEmpty() &&
                        tile.connectorOnSide(RotationType.WEST)!= ConnectorType.NONE){
                    exposedConnectors++;
                }

            }
        }
        return exposedConnectors;
    }

    public int getCosmicCredits() {
        return cosmicCredits;
    }

    public void addCosmicCredits(int numCosmicCredits) {cosmicCredits += numCosmicCredits;
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
        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && spaceshipIterator.getUpTile(t.get()).isPresent()) {
                if (!t.get().checkConnectors(spaceshipIterator.getUpTile(t.get()).get(), RotationType.NORTH)) {
                    return false;
                }
            }
        }
        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && spaceshipIterator.getDownTile(t.get()).isPresent()) {
                if (!t.get().checkConnectors(spaceshipIterator.getDownTile(t.get()).get(), RotationType.SOUTH)) {
                    return false;
                }
            }
        }
        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && spaceshipIterator.getRightTile(t.get()).isPresent()) {
                if (!t.get().checkConnectors(spaceshipIterator.getRightTile(t.get()).get(), RotationType.EAST)) {
                    return false;
                }
            }
        }
        for (Optional<Tile> t : spaceshipIterator) {
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
        for (Optional<Tile> t : spaceshipIterator) {
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
                if(getTile(t,num).isPresent() && getTile(t,num).get().connectorOnSide(RotationType.NORTH)!=ConnectorType.NONE){
                    return true;
                }
            }
        }
        else if(rotationType==RotationType.SOUTH){
            for(int t=12; t>0;t--){
                if(getTile(t,num).isPresent() && getTile(t,num).get().connectorOnSide(RotationType.SOUTH)!=ConnectorType.NONE){
                    return true;
                }
            }
        }
        else if(rotationType==RotationType.EAST){
            for(int t=12; t>0;t--){
                if(getTile(num,t).isPresent() && getTile(num,t).get().connectorOnSide(RotationType.EAST)!=ConnectorType.NONE){
                    return true;
                }
            }
        }
        else if(rotationType==RotationType.WEST){
            for(int t=0; t<12;t++){
                if(getTile(num,t).isPresent() && getTile(num,t).get().connectorOnSide(RotationType.WEST)!=ConnectorType.NONE){
                    return true;
                }
            }
        }
        return false;
    }


    public List<Cabin> getAliveCabins() {//todo tilebytype con cabin come parametro, toglierlo e sostituirlo contilebytytpe
        List<Cabin> humanCabins = new ArrayList<>();

        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && t.get().getType().equals(TileType.CABIN)) {
                humanCabins.add((Cabin) t.get());
            }
        }

        return humanCabins;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }// è la tile che sto guardando

    //calcola la distruzione della nave in base a dove è arrivato il meteorite
    //può essere che non ci sia damage perchè il num e la rotation non fanno male alla spaceship
    //ritorna 0 se la nave non è stata divisa in sotto parti
    //ritorna 1 se la nave si è divisa in varie parti
    public boolean meteoriteDamage(int bigOrSmall, RotationType rotationType, int num, Optional<BatteryStorage> storage) {
        //ho un meteorite piccolo
        if(bigOrSmall==0){
            if(!isExposed(rotationType,num)){
                return false; //se il lato non ha cavi esposti non succede niente
            }
            else if(isShielded(rotationType) && storage.isPresent()){ //se c'è uno scudo su quel lato e il layer vuole usarlo
                removeBattery(storage.get());
                return false;
            }
        }
        else{ //ho un meteorite grande
            // devo controllare se c'è un cannone e se voglio attivarlo nel caso sia doppio
            // se non c'è un cannone allora devo togliere la prima tile che incontro ed eventualmente tutti i pezzi attaccati ad esso
            if(rotationType==RotationType.NORTH){
                for(int t=0; t<12;t++){
                    if(getTile(t, num).isPresent() && getTile(t, num).get().getType().equals(TileType.CANNON)){ //ho un cannone singolo in quel punto
                        return false;
                    } else if(getTile(t, num).isPresent() && getTile(t, num).get().getType().equals(TileType.D_CANNON)&& storage.isPresent()){ //Ho un cannone doppio e voglio usarlo
                        removeBattery(storage.get());
                        return false;
                    } else if(getTile(t, num).isPresent()){ //si distrugge la prima cosa che incontro
                        removeTile(t, num);
                        return true;
                    }
                }
            }
            else if(rotationType==RotationType.SOUTH){
                for(int t=12; t>0;t--){
                    if(getTile(t, num).isPresent() && getTile(t, num).get().getType().equals(TileType.CANNON)){ //ho un cannone singolo in quel punto
                        return false;
                    } else if(getTile(t, num).isPresent() && getTile(t, num).get().getType().equals(TileType.D_CANNON)&& storage.isPresent()){ //Ho un cannone doppio e voglio usarlo
                        removeBattery(storage.get());
                        return false;
                    } else if(getTile(t, num).isPresent()){ //si distrugge la prima cosa che incontro
                        removeTile(t, num);
                        return true;
                    }
                }
            }
            else if(rotationType==RotationType.EAST) {
                for (int t = 12; t > 0; t--) {
                    if (getTile(num, t).isPresent() && getTile(num, t).get().getType().equals(TileType.CANNON)) {
                        return false;
                    } else if (getTile(num, t).isPresent() && getTile(num, t).get().getType().equals(TileType.D_CANNON) && storage.isPresent()) {
                        removeBattery(storage.get());
                        return false;
                    } else if (getTile(num, t).isPresent()) {
                        removeTile(num, t);
                        return true;
                    }
                }
            }
            if(rotationType==RotationType.WEST){
                for(int t=0; t<12;t++){
                    if (getTile(num, t).isPresent() && getTile(num, t).get().getType().equals(TileType.CANNON)) {
                        return false;
                    } else if (getTile(num, t).isPresent() && getTile(num, t).get().getType().equals(TileType.D_CANNON) && storage.isPresent()) {
                        removeBattery(storage.get());
                        return false;
                    } else if (getTile(num, t).isPresent()) {
                        removeTile(num, t);
                        return true;
                    }
                }
            }

        }

        return false;
    }

    public boolean shotDamage(int bigOrSmall, RotationType rotationType, int num, Optional<BatteryStorage> storage) {
        //ho un colpo piccolo
        if(bigOrSmall==0){
            if(isShielded(rotationType) && storage.isPresent()){ //se c'è lo scudo
                removeBattery(storage.get());
                return false;
            }
            //todo se non c'è lo scudo allora fa danno
        }
        else{ //ho un meterite grande
            //todo rimuovo la tile colpita, non ho modo di salvarmi

        }
        return true;
    }

    //todo: rifare il metodo, creandone prima un altro che ritorna le tile che contengono delle persone vive.
    //todo: in questo metodo non passare il numero di vivi da rimuovere, ma passare le tile da cui rimuovere i vivi
    public void removeCrew(int alive) {

        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && t.get().getType().equals(TileType.CABIN)) {
                Cabin temp = (Cabin) t.get();
                int num = temp.getNumBrownAlien() + temp.getNumPurpleAlien() + temp.getNumHuman();
                if (num > 0 && alive > 0) {
                    temp.remove(min(alive, num));
                }
            }
        }
    }

    //todo: fare il metodo. ritorna il numero di vivi sulla nave (aliens e umani)
    public int crewMember() {
        return 0;
    }

    //todo: fare il metodo. permette di gestire il fatto che l'utente
    public void boxManage() {
    }

    public int calculateNumAlive() {
        int alive = 0;
        for (Cabin cabin : getAliveCabins()) {
            alive += cabin.getNumHuman();
            alive += 2 * cabin.getNumPurpleAlien();
            alive += 2 * cabin.getNumBrownAlien();
        }

        return alive;
    }

    //todo Mi dice se la tile appartiene alla nave
    public boolean own(Tile tile) {  //todo il metodo controlla che la tile passata appartenga alla nave
        return true;
    }


    //todo Mi dice se il tipo di box passato è al pari del blocco più pregiato della nave
    //se non ci sono box ritorno false
    public boolean isMostExpensive(BoxType type) {
        return true;
    }

    public void epidemyRemove() {
        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && t.get().getType().equals(TileType.CABIN)) {
                //todo devo controllare che le tessere intorno siano cabine connesse, se si elimino un alive
            }
        }
    }
    //todo mantiene il pezzo di nave con la tile nella posizione (x,y), tutti i pezzi rimossi sono messi eliminati e si
    //aggiunge +1 per ogni pezzo al contatore degli scarti
    public void holdSpaceship(int x, int y) {}


    //todo ritorno 1 se non ci sono box sulla nave, 0 altrimenti
    public boolean noBox(){
        return false;
    }
}
