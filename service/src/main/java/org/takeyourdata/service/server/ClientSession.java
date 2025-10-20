package org.takeyourdata.service.server;

import org.jetbrains.annotations.NotNull;
import org.takeyourdata.protocol.packets.MessagePacket;
import org.takeyourdata.protocol.packets.Packet;
import org.takeyourdata.service.server.databases.VaultClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientSession {
    private final Socket socket;
    private final byte[] clientNonce;
    private final int userId;
    private final String clientId;
    private final String hardwareId;
    private final String location;
    private final byte[] secretKey;
    private final DataInputStream in;
    private final DataOutputStream out;

    public ClientSession(@NotNull Socket socket,
                         byte[] clientNonce,
                         int userId,
                         String clientId,
                         String hardwareId,
                         String location,
                         byte[] secretKey) throws IOException {
        this.socket = socket;
        this.clientNonce = clientNonce;
        this.userId = userId;
        this.clientId = clientId;
        this.hardwareId = hardwareId;
        this.location = location;
        this.secretKey = secretKey;

        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public void entry() {
        try {
            while (!socket.isClosed()) {
                int length = in.readInt();
                byte[] data = new byte[length];
                in.readFully(data);

                MessagePacket packet = (MessagePacket) Packet.deserialize(data);

                read(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void write() {
    }

    public void read(@NotNull MessagePacket messagePacket) throws Exception {
        VaultClient vault = new VaultClient();

        byte[] recipientKey = vault.getAuthKey(messagePacket.getRecipientId(), messagePacket.getAuthId());

        byte[] message = messagePacket.decryptMessage(secretKey);
        String msg = new String(message, StandardCharsets.UTF_8);

        byte[] writeMessage = messagePacket.encryptMessage(message, recipientKey);
    }

    public void sync() {

    }

    public Session getSession() {
        return new Session(
                userId,
                clientId,
                hardwareId,
                location
        );
    }

    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
