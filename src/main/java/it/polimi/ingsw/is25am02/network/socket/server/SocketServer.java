package it.polimi.ingsw.is25am02.network.socket.server;

import it.polimi.ingsw.is25am02.controller.server.ServerController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SocketServer {
    private ServerSocket serverSocket;
    private boolean running = false;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public void startServer(ServerController controller) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(8080);
                running = true;
                int port = 8080;
                System.out.println(">> Socket Server ready on port : " + port + "...");

                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println(">> Accepted connection from " + clientSocket.getRemoteSocketAddress());
                    ClientHandler handler = new ClientHandler(clientSocket, controller);
                    clients.add(handler);
                    threadPool.submit(handler);
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error Socket Server: " + e.getMessage());
                } else {
                    System.out.println(">> Socket Server closed gracefully");
                }
            }
        }).start();
    }

    public void stopServer() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                synchronized (clients) {
                    for (ClientHandler client : clients) {
                        client.closeConnection();
                    }
                }
                threadPool.shutdown();
                if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
                System.out.println(">> Socket Server stopped.");
            } catch (IOException | InterruptedException e) {
                System.err.println("Error during method stopServer for socket: " + e.getMessage());
            }
        }
    }
}
