package it.polimi.ingsw.is25am02.model;

import java.util.Timer;

public class Hourglass {
    private Timer timer;
    private long durata;
    public Hourglass(Timer timer, long durata) {
        this.timer = timer;
        this.durata = durata;
    }
    public long getTimeLeft(){
        return durata;
    }
    public void flip(){//todo

    }
}
