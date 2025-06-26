package it.polimi.ingsw.is25am02.model.cards;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.model.Game;
import it.polimi.ingsw.is25am02.model.Player;
import it.polimi.ingsw.is25am02.utils.enumerations.CardType;
import it.polimi.ingsw.is25am02.utils.enumerations.StateCardType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static it.polimi.ingsw.is25am02.utils.enumerations.StateGameType.TAKE_CARD;

public class SlaveOwner extends Enemies{
    private final int AliveLost;
    private int AliveRemoved;
    private final CardType cardType;

    public SlaveOwner(int level, int cannonPowers, int daysLost, int credit, int humanLost, String imagepath,String comment,boolean testFlight) {
        super(level, cannonPowers, daysLost, credit, StateCardType.CHOICE_ATTRIBUTES, imagepath,comment,testFlight);
        this.AliveLost = humanLost;
        this.AliveRemoved = 0;
        this.cardType = CardType.SLAVE_OWNER;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public void choiceDoubleCannon(Game game, Player player, List<Coordinate> cannons, List<Coordinate> batteries) throws IllegalRemoveException {
        //Calculate Player Power
        List<Tile> dCannon = new ArrayList<>();
        for(Coordinate cannon : cannons) {
            dCannon.add(player.getSpaceship().getTile(cannon.x(), cannon.y()).get());
        }
        double playerPower = player.getSpaceship().calculateCannonPower(dCannon);
        for(Coordinate battery : batteries) {
            player.getSpaceship().getTile(battery.x(), battery.y()).get().removeBattery();
            if (observers != null){
                for (String nick:observers.keySet()) {
                    try {
                        Coordinate pos = new Coordinate (battery.x(),battery.y());
                        observers.get(nick).showBatteryRemoval(pos, player.getNickname(), player.getSpaceship().getSpaceshipIterator().getTile(battery.x(), battery.y()).get().getNumBattery());
                    } catch (RemoteException e) {
                        ServerController.logger.log(Level.SEVERE, "error in method choicedoublecannon", e);
                    }
                }
            }
        }

        //Paragoni
        if(playerPower > getCannonPowers()){ //se il giocatore è piu forte dei schiavisti allora li sconfigge
            setStateCard(StateCardType.DECISION);
        }
        else if(playerPower == getCannonPowers()){ //non succede niente al player current ma passo al player successivo
            game.nextPlayer();
        }
        else{  //viene colpito il giocatore
            setStateCard(StateCardType.REMOVE);
        }
    }

    @Override
    public void choice(Game game, Player player, boolean choice){
        if(choice){
            player.getSpaceship().addCosmicCredits(getCredit());
            game.getGameboard().move((-1)*getDaysLost(), player);
            if(observers != null) {
                for (String nick : observers.keySet()) {
                    try {
                        observers.get(nick).showCreditUpdate(player.getNickname(), player.getSpaceship().getCosmicCredits());
                        observers.get(nick).showPositionUpdate(player.getNickname(), game.getGameboard().getPositions().get(player));
                        try {
                            observers.get(nick).displayMessage("ingame.moveongameboard", Map.of("nick", player.getNickname(), "pos", String.valueOf(game.getGameboard().getPositions().get(player))));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } catch (RemoteException e) {
                        ServerController.logger.log(Level.SEVERE, "error in method choice", e);
                    }
                }
            }
        }
        setStateCard(StateCardType.FINISH);
        game.getCurrentState().setPhase(TAKE_CARD);
    }

    @Override
    public void removeCrew(Game game, Player player, Tile cabin) throws IllegalRemoveException {
        cabin.removeCrew();
        if(observers != null) {
            for (String nick : observers.keySet()) {
                Coordinate pos = new Coordinate(player.getSpaceship().getSpaceshipIterator().getX(cabin), player.getSpaceship().getSpaceshipIterator().getY(cabin));
                try {
                    observers.get(nick).showCrewRemoval(pos, player.getNickname());
                } catch (RemoteException e) {
                    ServerController.logger.log(Level.SEVERE, "error in method removeCrew", e);
                }
            }
        }
        AliveRemoved++;

        if (AliveRemoved == AliveLost) {
            game.getCurrentCard().setStateCard(StateCardType.CHOICE_ATTRIBUTES);
            game.nextPlayer();
            AliveRemoved = 0;
            return;
        }

        if(noMore(player)){
            setStateCard(StateCardType.CHOICE_ATTRIBUTES);
            game.nextPlayer();
        }
    }

    private boolean noMore(Player player) {
        List<Tile> cabins = player.getSpaceship().getTilesByType(TileType.CABIN);
        for(Tile tile : cabins) {
            if(!tile.getCrew().isEmpty()) {
                return false;
            }
        }
        AliveRemoved = 0;
        return true;
    }

}
