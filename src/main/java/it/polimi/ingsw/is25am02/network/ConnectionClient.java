package it.polimi.ingsw.is25am02.network;

import it.polimi.ingsw.is25am02.view.ConsoleClient;

public interface ConnectionClient {
    void startConnection(String ip) throws Exception;
    void closeConnection() throws Exception;
    void setView(ConsoleClient view);
    VirtualServer getServer();
    ConsoleClient getConsole();
}
