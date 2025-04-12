package it.polimi.ingsw.is25am02.network.rmi.server;

import it.polimi.ingsw.is25am02.controller.server.ServerController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer {

    public void startServer(ServerController controller) throws RemoteException {
        final String serverName = "RMIServer";
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind(serverName, controller);
        System.out.println(">> RMI Server ready.");
    }

    public void stopServer(ServerController controller) throws RemoteException {
        UnicastRemoteObject.unexportObject(controller, true);
        System.out.println(">> RMI Server stopped.");
    }
}
