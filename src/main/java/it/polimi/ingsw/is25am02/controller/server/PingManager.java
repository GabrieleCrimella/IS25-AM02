package it.polimi.ingsw.is25am02.controller.server;

import java.util.Map;
import java.util.concurrent.*;

public class PingManager {
    private final Map<String, Integer> pingMap = new ConcurrentHashMap<>();
    private final int TIMEOUT_SECONDS = 7;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public PingManager(ServerController controller) {
        scheduler.scheduleAtFixedRate(() -> {
            for (String nickname : pingMap.keySet()) {
                if(pingMap.get(nickname) != TIMEOUT_SECONDS + 1) {
                    int timeLeft = pingMap.get(nickname) - 1;
                    if (timeLeft <= 0) {
                        pingMap.remove(nickname);
                        controller.disconnectClient(nickname);
                    } else {
                        pingMap.put(nickname, timeLeft);
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void registerClient(String nickname) {
        pingMap.put(nickname, TIMEOUT_SECONDS + 1);
    }

    public void ping(String nickname) {
        if (pingMap.containsKey(nickname)) {
            pingMap.put(nickname, TIMEOUT_SECONDS);
        }
    }

    public void stop() {
        scheduler.shutdownNow();

        //todo logger per gabri
        System.out.println("PingManager was stopped");
    }
}
