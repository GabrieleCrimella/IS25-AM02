package it.polimi.ingsw.is25am02.network.rmi.client;

import it.polimi.ingsw.is25am02.network.rmi.server.VirtualViewRmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;


public class RmiClient extends UnicastRemoteObject implements VirtualViewRmi {
    VirtualServerRmi server;

    public RmiClient() throws RemoteException {
        super();
    }

    public void startRmiClient() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("192.168.178.20");
        System.out.println("Tentativo di lookup...");
        server = (VirtualServerRmi) registry.lookup("RMIServer");
        System.out.println("Lookup riuscito...");

        System.out.println(">> RMI Client ready.");
    }

    public void stopRmiClient() throws RemoteException {
        UnicastRemoteObject.unexportObject(this, true);
        System.out.println(">> RMI Client stopped.");
    }

    public VirtualServerRmi getServer() { return server; }

    //todo da rivedere
    public void startProcessing() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Comando: ");
        }
    }

    //todo da rivedere
    @Override
    public void reportError(String details) throws RemoteException {
        // TODO. Attenzione, questo può causare data race con il thread dell'interfaccia o un altro thread!
        System.err.print("\n[ERROR] " + details + "\n> ");
    }
}
