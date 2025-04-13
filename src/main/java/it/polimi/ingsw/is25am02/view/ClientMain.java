package it.polimi.ingsw.is25am02.view;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.rmi.client.RmiClient;
import it.polimi.ingsw.is25am02.view.tui.commandLine.CliReader;

public class ClientMain {
    public static void main(String[] args) {
        ClientController controller;
        try {
            if (args.length == 2) {
                switch (args[0]) {
                    case "rmi":
                        RmiClient rmiClient = new RmiClient();
                        rmiClient.startRmiClient();
                        controller = new ClientController((VirtualServer) rmiClient.getServer());
                        break;
                    case "socket":
                        //todo crea socketclient e avvia la connessione
                        controller = new ClientController((VirtualServer) null); //da cambiare
                        break;
                    default:
                        System.out.println("Wrong ConnectionType");
                        throw new Exception("Wrong ConnectionType");
                }
                switch (args[1]) {
                    case "tui":
                        CliReader reader = new CliReader(controller);
                        reader.start();
                        break;
                    case "gui":
                        break;
                    default:
                        System.out.println("Wrong UI");
                        throw new Exception("Wrong ConnectionType");
                }

            } else {
                System.out.println("Usage: java -jar client.jar <UI> <ConnectionType>");
            }

            //todo chiusura client
        } catch (Exception e) {
            System.err.println("Error while starting the Client.");
        }
    }
}
