package it.polimi.ingsw.is25am02.model.tiles;

import it.polimi.ingsw.is25am02.model.enumerations.ConnectorType;
import it.polimi.ingsw.is25am02.model.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.enumerations.TileType;

public final class Shield extends Tile{
    boolean[] shielded;

    public Shield(TileType t, ConnectorType[] connectors, RotationType rotationType, int id, boolean[] shielded) {
        super(t, connectors, rotationType, id);
        this.shielded = shielded;
    }

    @Override
    public boolean isShielded(RotationType side){
        int[] thisTile = new int[4];
        boolean[] rotatedShield = new boolean[4];


        //Same logic as CheckConnectors in Tile
        for (int i = 0; i < 4; i++) {
            int tilePosition = (i+this.getRotationType().getNum())%4;
            thisTile[i] = this.getConnectors()[tilePosition].getNum();
            int shieldPosition = (i+this.getRotationType().getNum())%4;
            rotatedShield[i] = this.shielded[shieldPosition];

        }
        return switch (side) {
            case NORTH -> rotatedShield[0];
            case EAST -> rotatedShield[1];
            case SOUTH -> rotatedShield[2];
            case WEST -> rotatedShield[3];
        };

    }
}
