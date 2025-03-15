package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.enumerations.BoxType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.tiles.*;
import javafx.util.Pair;

import java.util.*;

import static java.lang.Integer.min;

public class Spaceship {
    private final SpaceshipIterator spaceshipIterator;
    private int numOfWastedTiles;
    private int cosmicCredits;
    private final Tile currentTile;
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

    public void removeTile(int x, int y) { //chiamo quando il gioco è iniziato e perdo un pezzo perchè mi colpiscono
        spaceshipIterator.removeTile(x, y);
        numOfWastedTiles++;
    }

    public void returnTile() {//todo durante la fase di costruzione se scarto una carta, rimetto la current tile nel heaptile
        //dovrà chiamare addTile dell'heap
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
            if (tile.get().getType().equals(TileType.D_CANNON)) {
                doubleCannons.add((DoubleCannon) tile.get());
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
        //calcola la potenza sinoglola dei cannoni singoli contando l'orientazione e quella dei cannoni doppi contando l'orientazione
        return 0.0;
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

    //todo: fare il metodo
    public int calculateExposedConnectors() {
        return 0;
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

    /*
     * todo: il client nella fase di assemblaggio può aggiungere anche tiles dove la maschera contiene 0.
     * todo: è durante la fase di controllo che si fa il controllo con la maschera
     */
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

        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && (t.get().getType().equals(TileType.D_CANNON) || t.get().getType().equals(TileType.CANNON))) {
                if (spaceshipIterator.getFrontTile(t.get()).isPresent()) {
                    return false;
                }
            }
        }


        //todo: controllare che non ci sia niente nella tile dopo dove punta un cannone

        //todo: guardare anche le cose double
        return true;
    }

    public List<BatteryStorage> getbatteryStorage() {
        List<BatteryStorage> batteryStorages = new ArrayList<>();

        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && t.get().getType().equals(TileType.BATTERY)) {
                batteryStorages.add((BatteryStorage) t.get());
            }
        }

        return batteryStorages;
    }

    public void removeBattery(BatteryStorage t) {
        t.removeBattery();
    }

    //todo: fare il metodo
    public boolean isExposed(RotationType rotationType) {
        return true;
    }

    public List<Cabin> getHumanCabins() {
        List<Cabin> humanCabins = new ArrayList<>();

        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && t.get().getType().equals(TileType.CABIN)) {
                humanCabins.add((Cabin) t.get());
            }
        }

        return humanCabins;
    }

    public List<Cabin> getPurpleCabins() {
        List<Cabin> purpleCabins = new ArrayList<>();

        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && t.get().getType().equals(TileType.PURPLE_CABIN)) {
                purpleCabins.add((Cabin) t.get());
            }
        }

        return purpleCabins;
    }

    public List<Cabin> getBrownCabins() {
        List<Cabin> brownCabins = new ArrayList<>();

        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && t.get().getType().equals(TileType.BROWN_CABIN)) {
                brownCabins.add((Cabin) t.get());
            }
        }
        return brownCabins;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }// è la tile che sto guardando

    public List<SpecialStorage> getStorageTiles() {
        List<SpecialStorage> storageTiles = new ArrayList<>();

        for (Optional<Tile> t : spaceshipIterator) {
            if (t.isPresent() && ((t.get().getType().equals(TileType.SPECIAL_STORAGE) || t.get().getType().equals(TileType.STORAGE)))) {
                storageTiles.add((SpecialStorage) t.get());
            }
        }

        return storageTiles;
    }

    //todo: fare il metodo - non sono sicura che serva
    public void calculateDamageMeteorites(ArrayList<Integer> meteorites, int line) {

    }

    //todo: calcolare la distruzione della nave in base a dove è arrivato il meteorie
    //può essere che non ci sia damage perchè il num e la rotation non fanno male alla spaceship
    public void meteoriteDamage(int bigOrSmall, RotationType rotationType, int num) {
    }

    //todo: fare il metodo
    public void calculateDamageShots(ArrayList<Integer> shots, int line) {
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

    //todo: fare il metodo. ritorna il numero di vivi sulla nave (aliens e umani). gli alieni contano doppio
    public int crewMember() {
        return 0;
    }

    //todo: fare il metodo. permette di gestire il fatto che l'utente
    public void boxManage() {
    }

    public int calculateNumAlive() {
        int alive = 0;
        for (Cabin cabin : getHumanCabins()) {
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
        for(Optional<Tile> t : spaceshipIterator){
            if(t.isPresent() && t.get().getType().equals(TileType.CABIN)){
                //todo devo controllare che le tessere intorno siano cabine connesse, se si elimino un alive
            }
        }
    }

}
