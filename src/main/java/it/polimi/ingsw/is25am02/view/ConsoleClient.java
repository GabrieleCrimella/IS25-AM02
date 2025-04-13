package it.polimi.ingsw.is25am02.view;

import it.polimi.ingsw.is25am02.controller.client.ClientController;

public interface ConsoleClient {
    void setController(ClientController controller);
    void start();
}
