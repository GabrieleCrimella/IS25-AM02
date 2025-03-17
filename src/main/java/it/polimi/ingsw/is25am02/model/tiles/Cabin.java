package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.Alive;
import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;

import java.util.ArrayList;

public final class Cabin extends Tile{
    ArrayList<Alive> crew = new ArrayList<>();

    public Cabin(TileType t, ConnectorType[] connectors, RotationType rotationType, int id) {
        super(t, connectors, rotationType, id);
    }

    @Override
    public ArrayList<Alive> getNumCrew(){
        return crew;
    }

    @Override
    public void addCrew(Alive member){
        crew.add(member);
    }

    @Override
    public void removeCrew(){
        crew.removeLast();
    }
}
