package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.Alive;
import it.polimi.ingsw.is25am02.model.enumerations.AliveType;
import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;

import java.util.ArrayList;

public final class Cabin extends Tile{
    ArrayList<Alive> crew;

    public Cabin(TileType t, ConnectorType[] connectors, RotationType rotationType, int id) {
        super(t, connectors, rotationType, id);
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
    public void removeCrew(){
        crew.removeLast();
    }
}
