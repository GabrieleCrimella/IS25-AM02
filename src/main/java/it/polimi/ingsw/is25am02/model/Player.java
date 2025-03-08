package it.polimi.ingsw.is25am02.model;

public class Player {
    private Spaceship spaceship;
    private String nickname;
    private PlayerColor color;

    public Player(Spaceship spaceship, String nickname, PlayerColor color) {
        this.spaceship = spaceship;
        this.nickname = nickname;
        this.color = color;
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

    //manages player interaction for yes or no answers
    public boolean choose(){
        return false;
    }
}
