package it.polimi.ingsw.is25am02.network.socket.server;

import it.polimi.ingsw.is25am02.controller.server.ServerController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SocketServer {
    private ServerSocket serverSocket;
    private boolean running = false;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    public void startServer(ServerController controller) {
        try {
            serverSocket = new ServerSocket(8080);
            running = true;
            int port = 8080;
            System.out.println(">> Socket Server ready on port : " + port + "...");

            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(">> Accepted connection from " + clientSocket.getRemoteSocketAddress());
                ClientHandler handler = new ClientHandler(clientSocket, controller);
                threadPool.submit(handler);
            }
        } catch (IOException e) {
            System.err.println("Error Socket Server: " + e.getMessage());
        }
    }

    public void stopServer() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                threadPool.shutdown();
                if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
                serverSocket.close();
                System.out.println(">> Socket Server stopped.");
            } catch (IOException | InterruptedException e) {
                System.err.println("Error during method stopServer for socket: " + e.getMessage());
            }
        }
    }
}
