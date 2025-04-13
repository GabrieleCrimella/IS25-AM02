package it.polimi.ingsw.is25am02.modelDuplicateView.enumeration;

public enum StateGameType {
    INITIALIZATION_GAME, //can be deleted. it's used to initialize all the stuff.Iy can be done in Game Creator
    BUILD, //the spaceship is being built
    CHECK, //the user can only call checkSpaceship
    CORRECTION, //if the check phase went wrong, the user can correct the spaceship with the remove method. at the end of correction, if the spaceship is correct, the next sstate will be the inizializazion one, else ...
    INITIALIZATION_SPACESHIP, //the user can add the crew. the battery tile is filled.
    TAKE_CARD, //the user can call nextCard. if the deck is empty, the next state will be the result.
    EFFECT_ON_PLAYER,
    RESULT
}
