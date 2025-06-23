package it.polimi.ingsw.is25am02.network;

import it.polimi.ingsw.is25am02.controller.server.ServerController;
import it.polimi.ingsw.is25am02.network.rmi.server.RmiServer;
import it.polimi.ingsw.is25am02.network.socket.server.SocketServer;

import java.io.IOException;
import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {
        int activeServers = 2;
        try {
            //Controller creation
            ServerController controller = new ServerController();

            //RmiServer on
            RmiServer rmiServer = new RmiServer();
            rmiServer.startServer(controller);

            //SocketServer on
            SocketServer socketServer = new SocketServer();
            socketServer.startServer(controller);

            Scanner scanner = new Scanner(System.in);
            while(activeServers > 0){
                System.out.println("Write 1 to stop RMI Server, 2 to stop Socket Server");
                int ris = scanner.nextInt();

                if(ris == 1){
                    rmiServer.stopServer(controller);
                    activeServers--;
                } else if (ris == 2) {
                    socketServer.stopServer();
                    activeServers--;
                } else {
                    System.out.println("Wrong input");
                }
            }
            controller.shutdown();
            System.out.println("Server stopped");
        } catch (IOException e) {
            System.err.println("Error while starting the Server.");
        }
    }
}
