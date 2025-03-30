package it.polimi.ingsw.is25am02.model;

import java.util.Timer;
import java.util.TimerTask;

public class Hourglass {
    private Timer timer;
    private final long durata;
    private long timeLeft;
    private boolean running;

    public Hourglass() {
        this.durata = 60;
        this.timeLeft = durata;
        this.running = false;
    }

    public boolean getRunning() {
        return running;
    }

    public void flip(Game game) {
        if (running) {
            timer.cancel();
        }
        timer = new Timer();
        timeLeft = durata;
        running = true;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeLeft--;
                if (timeLeft <= 0) {
                    timer.cancel();
                    running = false;
                    if (game.getGameboard().getHourGlassFlip() == 0) {
                        game.setBuildTimeIsOver();
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
}


