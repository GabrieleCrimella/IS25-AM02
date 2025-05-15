package it.polimi.ingsw.is25am02.network.socket.client;

import it.polimi.ingsw.is25am02.network.ConnectionClient;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.view.ConsoleClient;

public class SocketClient implements ConnectionClient {
    private ServerHandler handler;
    @Override
    public void startConnection(String ip) throws Exception {

    }

    @Override
    public void closeConnection() throws Exception {

    }

    @Override
    public void setView(ConsoleClient view) {

    }

    @Override
    public VirtualServer getServer() {
        return handler;
    }

    @Override
    public ConsoleClient getConsole() {
        return null;
    }
}
