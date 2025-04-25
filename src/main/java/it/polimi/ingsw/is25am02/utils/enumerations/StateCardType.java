package it.polimi.ingsw.is25am02.utils.enumerations;

public enum StateCardType {
    DECISION,    //decisioni si/no o utilizzo di batteria
    CHOICE_ATTRIBUTES,  //scelta di cannoni e motori doppi da usare
    REMOVE,  //rimozione di equipaggio o merce
    BOXMANAGEMENT, //gestione carico e scarico merci
    ROLL, //tiro dadi
    FINISH //effetto carta finito si aspetta che il leader invochi playnextcard()

}
