package it.polimi.ingsw.is25am02.network.rmi.client;

import it.polimi.ingsw.is25am02.network.rmi.server.VirtualViewRmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;


//todo da modificare completamente
public class RmiClient extends UnicastRemoteObject implements VirtualViewRmi {
    final VirtualServerRmi server;

    public RmiClient(VirtualServerRmi server) throws RemoteException {
        super();
        this.server = server;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final String serverName = "AdderServer";

        Registry registry = LocateRegistry.getRegistry(args[0], 1234);

        VirtualServerRmi server = (VirtualServerRmi) registry.lookup(serverName);

        new RmiClient(server).run();
    }

    private void run() throws RemoteException {
        this.server.connect(this);
        this.runCli();
    }

    private void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            int command = scan.nextInt();
/*
            if (command == 0) {
                server.reset();
            } else {
                server.add(command);
            }
 */
        }
    }

    @Override
    public void reportError(String details) throws RemoteException {
        // TODO. Attenzione, questo può causare data race con il thread dell'interfaccia o un altro thread!
        System.err.print("\n[ERROR] " + details + "\n> ");
    }
}
