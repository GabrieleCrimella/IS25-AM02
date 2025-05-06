package it.polimi.ingsw.is25am02.utils.enumerations;

public enum StatePlayerType {
    FINISHED,
    NOT_FINISHED,
    CORRECT_SHIP,
    WRONG_SHIP,
    IN_GAME,
    OUT_GAME;

    @Override
    public String toString(){
        if(this.equals(FINISHED)){
            return "finished";
        }
        else if(this.equals(NOT_FINISHED)){
            return "not finished";
        }
        else if(this.equals(CORRECT_SHIP)){
            return "correct ship";
        }
        else if(this.equals(WRONG_SHIP)){
            return "wrong ship";
        }
        else if(this.equals(IN_GAME)){
            return "in game";
        }
        else
            return "out game";
    }

}
