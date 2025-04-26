package it.polimi.ingsw.is25am02.view.modelDuplicateView;

import it.polimi.ingsw.is25am02.model.Game;

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

    public boolean getRunning() {
        return running;
    }


}
