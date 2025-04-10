package it.polimi.ingsw.is25am02.network.rmi.client;

import it.polimi.ingsw.is25am02.network.rmi.server.VirtualViewRmi;
import it.polimi.ingsw.is25am02.network.VirtualServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServerRmi extends Remote, VirtualServer {

    void connect(VirtualViewRmi client) throws RemoteException;

    //todo metodi controller
}
