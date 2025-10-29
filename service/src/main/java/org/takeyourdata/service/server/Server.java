package org.takeyourdata.service.server;

import org.takeyourdata.service.server.databases.PostgresqlClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 443;

    public Server() {
        try {
            File file = new File("./config.properties");
            if (!file.exists()) {
                file.createNewFile();
                ConfigProperties.create();
            }

            connectDb();

            ServerSocket serverSocket = new ServerSocket(PORT);

            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                new Thread(() -> {
                    try {
                        new ClientHandler(client).run();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Server();
    }

    private void connectDb() {
        PostgresqlClient postgresqlClient = new PostgresqlClient();

        postgresqlClient.connect();
    }
}
