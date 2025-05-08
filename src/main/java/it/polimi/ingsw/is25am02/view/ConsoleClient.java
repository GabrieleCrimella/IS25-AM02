package it.polimi.ingsw.is25am02.view;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.view.tui.utils.GraphicPrinter;

import java.util.Map;

public interface ConsoleClient {
    void setController(ClientController controller);
    ClientController getController();
    void setNickname(String nickname);
    void start();
    void reportError(String keys, Map<String, String> params);
    void displayMessage(String keys, Map<String, String> params);
    GraphicPrinter getPrinter();
    void startCountdown();

}
