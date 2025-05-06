package it.polimi.ingsw.is25am02.utils.enumerations;

public enum StateCardType {
    DECISION,    //decisioni si/no o utilizzo di batteria
    CHOICE_ATTRIBUTES,  //scelta di cannoni e motori doppi da usare
    REMOVE,  //rimozione di equipaggio o merce
    BOXMANAGEMENT, //gestione carico e scarico merci
    ROLL, //tiro dadi
    FINISH;

    //effetto carta finito si aspetta che il leader invochi playnextcard()

    @Override
    public String toString(){
        if(this.equals(DECISION)){
            return "decisione";
        }
        else if(this.equals(CHOICE_ATTRIBUTES)){
            return "choice attributes";
        }
        else if(this.equals(REMOVE)){
            return "remove";
        }
        else if(this.equals(BOXMANAGEMENT)){
            return "boxmanagement";
        }
        else if(this.equals(ROLL)){
            return "roll";
        }
        else
            return "finish";
    }

}
