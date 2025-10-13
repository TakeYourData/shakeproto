package org.takeyourdata.service.server;

import org.jetbrains.annotations.NotNull;
import org.takeyourdata.protocol.packets.HandshakePacket;
import org.takeyourdata.protocol.packets.Packet;
import org.takeyourdata.service.server.handlers.HandshakeHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public ClientHandler(@NotNull Socket socket) throws IOException {
        this.socket = socket;

        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {

                int length = in.readInt();
                byte[] data = new byte[length];
                in.readFully(data);

                Packet packet = Packet.deserialize(data);
                handlePacket(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendPacket(@NotNull Packet packet) throws Exception {
        byte[] data = packet.serialize(packet.getType());
        out.writeInt(data.length);
        out.write(data);
        out.flush();
    }

    private void handlePacket(Packet packet) {
        if (packet instanceof HandshakePacket) {
            HandshakeHandler handshakeHandler = new HandshakeHandler((HandshakePacket) packet);

            handshakeHandler.handle();
        }
    }
}
