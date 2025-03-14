package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.model.*;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.tiles.BatteryStorage;
import javafx.util.Pair;

import java.util.ArrayList;

public class MeteoritesStorm extends Card {
    private ArrayList<Pair<Integer, RotationType>> meteorites;
    private int currentIndex;


    public MeteoritesStorm(int level, ArrayList<Pair<Integer,RotationType>> meteorites) {
        super(level,StateCardType.DECISION);
        this.meteorites = meteorites;
        currentIndex = 0;
    }

    public MeteoritesStorm createCard(){
        //Here the code for reading on file the card's values
        return new MeteoritesStorm(getLevel(), meteorites);
    }

    public void effect(Game game, BatteryStorage batteryStorage, boolean Battery){
        //meteorite piccolo

        Pair<Integer, RotationType> currentMeteor = meteorites.get(currentIndex);

        if (currentMeteor.getKey() == 0) {
            //se non ha connettori esposti
            if (!game.getCurrentPlayer().getSpaceship().isExposed(currentMeteor.getValue())){
                return;
            }
            //se ha connettori esposti e uno scudo che può utilizzare, serve exception se lo scudo non c'è o non è nella posizione giusta
            else if (game.getCurrentPlayer().getSpaceship().isShielded((currentMeteor.getValue())) && Battery){
                game.getCurrentPlayer().getSpaceship().removeBattery(batteryStorage);
            }
            //se no devo calcolare il danno del meteorie passandogli il lato da cui arriva e il numero
            else{
                game.getCurrentPlayer().getSpaceship().meteoriteDamage(currentMeteor.getKey(), currentMeteor.getValue(), game.getDiceResult());
            }
        }
        //meteorite grande
        else {
            //utilizzo cannone normale
            if (game.getCurrentPlayer().getSpaceship().isCannonPresent(currentMeteor.getValue(), game.getDiceResult())) {
            }
            //utilizzo double cannon
            else if (Battery) {
                game.getCurrentPlayer().getSpaceship().isDoubleCannonPresent(currentMeteor.getValue(), game.getDiceResult());
            }
            //non ha ne cannone ne doppio cannone, quindi si distrugge
            else {
                game.getCurrentPlayer().getSpaceship().meteoriteDamage(currentMeteor.getKey(), currentMeteor.getValue(), game.getDiceResult());
            }
        }
        //avanza al prossimo meteorite
        if (currentIndex >= meteorites.size()) {
            game.playNextCard();
        }
        else {
            //todo: qui in veirtà bisogna controllare che abbia già agito con questo meteorite su tutti i player presenti
            /*un'opzione può essere mettere un int[4] dentro ogni arrayList di meteorites dove scrivi per che player hanno già subito
            l'effetto.
             */
            currentIndex++;
        }
        /* Anti-Pattern
            for(ArrayList<Integer> meteorite : meteorites){
            int num = gb.getDice().pickRandomNumber();
            for(Player i : gb.getRanking()){
                i.getSpaceship().calculateDamageMeteorites(meteorite, num);
            }
        }
         */

    }

}
