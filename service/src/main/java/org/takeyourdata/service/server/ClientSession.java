package org.takeyourdata.service.server;

import java.net.Socket;

public class ClientSession {
    private final Socket socket;
    private final byte[] clientNonce;
    private final int userId;
    private final String clientId;
    private final String hardwareId;
    private final String location;
    private final byte[] secretKey;

    public ClientSession(Socket socket,
                         byte[] clientNonce,
                         int userId,
                         String clientId,
                         String hardwareId,
                         String location,
                         byte[] secretKey) {
        this.socket = socket;
        this.clientNonce = clientNonce;
        this.userId = userId;
        this.clientId = clientId;
        this.hardwareId = hardwareId;
        this.location = location;
        this.secretKey = secretKey;
    }

    public void writeSession() {
    }

    public void readSession() {

    }
}
