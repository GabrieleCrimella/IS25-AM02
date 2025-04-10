package it.polimi.ingsw.is25am02.network.rmi.server;

import it.polimi.ingsw.is25am02.controller.ServerController;
import it.polimi.ingsw.is25am02.network.rmi.client.VirtualServerRmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the server logic implemented with RMI technology.
 */
public class RmiServer extends UnicastRemoteObject implements VirtualServerRmi {
    final ServerController controller;
    final List<VirtualViewRmi> clients = new ArrayList<>(); //todo va a finire nel model assieme al player

    public RmiServer() throws RemoteException {
        super();
        this.controller = new ServerController();
    }

    public static void main(String[] args) throws RemoteException {
        final String serverName = "AdderServer";

        VirtualServerRmi server = new RmiServer();

        Registry registry = LocateRegistry.createRegistry(1234);

        registry.rebind(serverName, server);

        System.out.println("Server bound");
    }

    @Override
    public void connect(VirtualViewRmi client) throws RemoteException {
        //todo gestire la concorrenza, più client possono invocare questo metodo simultaneamente
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }
}
