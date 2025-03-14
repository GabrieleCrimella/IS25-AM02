package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.model.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.model.enumerations.StatePlayerType;

import static it.polimi.ingsw.is25am02.model.enumerations.StatePlayerType.NOT_FINISHED;

public class Player {
    private Spaceship spaceship;
    private String nickname;
    private PlayerColor color;
    private StatePlayerType statePlayer;

    public Player(Spaceship spaceship, String nickname, PlayerColor color) {
        this.spaceship = spaceship;
        this.nickname = nickname;
        this.color = color;
        this.statePlayer = NOT_FINISHED;
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerColor getColor() {
        return color;
    }

    public StatePlayerType getStatePlayer() {
        return statePlayer;
    }
}
