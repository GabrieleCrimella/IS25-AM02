package it.polimi.ingsw.is25am02.model;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.utils.enumerations.StatePlayerType;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

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

    public long getTimeLeft() {
        return timeLeft;

    }

    public boolean getRunning() {
        return running;
    }

    public void flip(Game game) {
        if (running) {
            timer.cancel();
            running = false;
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

                    System.out.println("Timer scaduto, stato clessidra: " + game.getGameboard().getHourGlassFlip());

                    // SOLO al secondo giro (quando flip == 0)
                    if (game.getGameboard().getHourGlassFlip() == 0) {
                        game.setBuildTimeIsOver();
                        for (Player p : game.getPlayers()) {
                            try {
                                if (p.getStatePlayer().equals(StatePlayerType.NOT_FINISHED)) {
                                    game.shipFinished(p);
                                }
                            } catch (Exception e) {
                                ServerController.logger.log(Level.SEVERE, "Errore in shipFinished", e);
                            }
                        }
                    }
                }
            }
        };

        // Avvia il timer ogni secondo
        timer.scheduleAtFixedRate(task, 1000, 1000);  // saltiamo il primo tick a 0
    }

}



