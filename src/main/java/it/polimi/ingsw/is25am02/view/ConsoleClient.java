package it.polimi.ingsw.is25am02.view;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.model.Lobby;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.view.tui.utils.GraphicPrinter;

import java.util.Map;

public interface ConsoleClient {
    void setController(ClientController controller);
    ClientController getController();
    void setNickname(String nickname);
    String getNickname();
    void start();
    void reportError(String keys, Map<String, String> params);
    void displayMessage(String keys, Map<String, String> params);
    void setLobbiesView(Map<Integer, LobbyView> lobbies);
    void setBuildView(int level, PlayerColor color);
    GraphicPrinter getPrinter();
    void startCountdown();
    void spaceshipBrokenUpdate(String details, Coordinate[][] ships);
}
