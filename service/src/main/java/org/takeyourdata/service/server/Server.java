package org.takeyourdata.service.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 443;

    public Server() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                new Thread(() -> new ClientHandler(client).run()).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
