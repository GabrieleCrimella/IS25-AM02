package it.polimi.ingsw.is25am02.view.modelDuplicateView;


import java.util.Timer;
import java.util.TimerTask;

public class HourglassV {

    private Timer timer;
    private final long durata;
    private long timeLeft;
    private boolean running;

    public HourglassV() {
        this.durata = 60;
        this.timeLeft = durata;
        this.running = false;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    //viene reimpostata la clessidra da capo
    public void flip(){
        timeLeft = durata;
    }


    public boolean getRunning() {
        return running;
    }


}
