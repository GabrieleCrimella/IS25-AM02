package it.polimi.ingsw.is25am02.model;

import java.util.Timer;
import java.util.TimerTask;

public class Hourglass {
    private Timer timer;
    private final long durata;
    private long timeLeft;
    private boolean running;
    private TimerTask task;

    public Hourglass(long durata) {
        this.durata = durata;
        this.timeLeft = durata;
        this.running = false;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void flip() {
        if (running) {
            timer.cancel();
        }
        timer = new Timer();
        timeLeft = durata;
        running = true;

        task = new TimerTask() {
            @Override
            public void run() {
                timeLeft--;
                if (timeLeft <= 0) {
                    timer.cancel();
                    running = false;
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
}


