package it.polimi.ingsw.is25am02.utils.enumerations;

public enum TileType {
    CANNON, CABIN, MOTOR, BATTERY, SHIELD, STORAGE, SPECIAL_STORAGE, D_CANNON, D_MOTOR, STRUCTURAL, PURPLE_CABIN, BROWN_CABIN;

    @Override
    public String toString(){
        if(this.equals(CANNON)){
            return "cannon";
        }
        else if(this.equals(CABIN)){
            return "cabin";
        }
        else if(this.equals(MOTOR)){
            return "motor";
        }
        else if(this.equals(BATTERY)){
            return "battery";
        }
        else if(this.equals(SHIELD)){
            return "shield";
        }
        else if(this.equals(STORAGE)){
            return "storage";
        }
        else if(this.equals(SPECIAL_STORAGE)){
            return "special storage";
        }
        else if(this.equals(D_CANNON)){
            return "double cannon";
        }
        else if(this.equals(D_MOTOR)){
            return "double motor";
        }
        else if(this.equals(STRUCTURAL)){
            return "structural";
        }
        else if(this.equals(PURPLE_CABIN)){
            return "purple cabin";
        }
        else
            return "brown cabin";
    }
}
