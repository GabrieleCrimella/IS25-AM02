package it.polimi.ingsw.is25am02.view;

import it.polimi.ingsw.is25am02.controller.client.ClientController;

import java.rmi.RemoteException;

public interface ConsoleClient {
    void setController(ClientController controller);
    void start();
    void displayMessage(String message);
    void reportError(String details);
}
