package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.Alive;
import it.polimi.ingsw.is25am02.utils.enumerations.AliveType;
import it.polimi.ingsw.is25am02.utils.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.utils.enumerations.TileType;
import it.polimi.ingsw.is25am02.model.exception.IllegalRemoveException;

import java.util.ArrayList;

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
    public void addCrew(AliveType type){
        Alive member = new Alive(type);
        crew.add(member);
    }

    @Override
    public void removeCrew() throws IllegalRemoveException {
        if(crew.isEmpty()){
            throw new IllegalRemoveException("This cabin is empty");
        }
        crew.removeLast();
    }
}
