package it.polimi.ingsw.is25am02.view;

import it.polimi.ingsw.is25am02.controller.client.ClientController;
import it.polimi.ingsw.is25am02.network.ConnectionClient;
import it.polimi.ingsw.is25am02.network.rmi.client.RmiClient;
import it.polimi.ingsw.is25am02.network.socket.client.SocketClient;
import it.polimi.ingsw.is25am02.view.tui.TuiConsole;

public class ClientMain {
    public static void main(String[] args) {
        ConnectionClient connection;
        ConsoleClient console;
        try {
            if (args.length == 3) {
                connection = switch (args[0]) {
                    case "rmi" -> new RmiClient();
                    case "socket" -> new SocketClient();
                    default -> throw new Exception("Wrong ConnectionType");
                };
                console = switch (args[1]) {
                    case "tui" -> new TuiConsole();
                    case "gui" -> new TuiConsole(); //da cambiare
                    default -> throw new Exception("Wrong UI");
                };
                ClientController controller = new ClientController(connection);

                connection.startConnection(args[2]);
                connection.setView(console);

                console.start();
                console.setController(controller);
            } else {
                System.out.println("Usage: java -jar client.jar <UI> <ConnectionType>");
                throw new Exception("...");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("Error while starting the Client.");
        }
    }
}
