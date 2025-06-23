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
                    if (game.getGameboard().getHourGlassFlip() == 1) {
                        game.setBuildTimeIsOver();
                        for (Player p : game.getPlayers()) {
                            try {
                                if (p.getStatePlayer().equals(StatePlayerType.NOT_FINISHED)){
                                    game.shipFinished(p);
                                    //p.setStatePlayer(StatePlayerType.FINISHED);
                                }
                                //p.onPlayerStateUpdate(p.getNickname(), p.getStatePlayer());
                                //p.getObserver().showBuildTimeIsOverUpdate();
                                //p.getObserver().displayMessage("hourglass.finished",null);
                            } catch (Exception e) {
                                ServerController.logger.log(Level.SEVERE, "error in fliphourglass", e);
                            }
                        }
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
}


