package org.takeyourdata.service.server;

import org.jetbrains.annotations.NotNull;
import org.takeyourdata.protocol.exceptions.NonceException;
import org.takeyourdata.protocol.packets.ErrorPacket;
import org.takeyourdata.protocol.packets.MessagePacket;
import org.takeyourdata.protocol.packets.Packet;
import org.takeyourdata.protocol.packets.SyncPacket;
import org.takeyourdata.service.server.databases.structure.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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

                Packet packet = Packet.deserialize(data);
                if (packet instanceof MessagePacket messagePacket) {
                    read(messagePacket);
                } else if (packet instanceof SyncPacket syncPacket) {
                    sync(syncPacket);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void write(@NotNull Packet packet) throws Exception {
        byte[] data = packet.serialize(packet.getType());
        out.writeInt(data.length);
        out.write(data);
        out.flush();
    }

    public void read(@NotNull MessagePacket messagePacket) throws Exception {
        if (clientNonce != messagePacket.getNonce()) {
            write(new ErrorPacket(new NonceException("Nonce is invalid")));
            closeConnection();
        }

        byte[] message = messagePacket.decryptMessage(secretKey);

        int msgId = messagePacket.getMsgId();
        int chatId = messagePacket.getChatId();
        int senderId = messagePacket.getSenderId();
        int recipientId = messagePacket.getRecipientId();
        long timestamp = messagePacket.getTimestamp();

        Message.Companion.create(
                chatId,
                msgId,
                senderId,
                recipientId,
                timestamp,
                message
        );
    }

    public void sync(@NotNull SyncPacket syncPacket) {
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
