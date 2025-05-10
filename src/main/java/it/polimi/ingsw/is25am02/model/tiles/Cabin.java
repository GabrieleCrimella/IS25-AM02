package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.model.Alive;
import it.polimi.ingsw.is25am02.utils.enumerations.AliveType;
import it.polimi.ingsw.is25am02.utils.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;

public final class Cabin extends Tile{
    ArrayList<Alive> crew;

    public Cabin(TileType t, ConnectorType[] connectors, RotationType rotationType, String imagePath) {
        super(t, connectors, rotationType, imagePath);
        this.crew = new ArrayList<>();
    }

    @Override
    public ArrayList<Alive> getCrew(){
        return crew;
    }


    @Override
    public void addCrew(String nicknameP, AliveType type){
        Alive member = new Alive(type);
        crew.add(member);
        //todo controlla che tutte le volte che viene utilizzata questa funzione venga chiamato l'update
    }

    @Override
    public void removeCrew() throws IllegalRemoveException {
        if(crew.isEmpty()){
            throw new IllegalRemoveException("This cabin is empty");
        }
        crew.removeLast();
    }
}
