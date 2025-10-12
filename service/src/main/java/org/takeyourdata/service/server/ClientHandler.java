package org.takeyourdata.service.server;

import org.takeyourdata.protocol.packets.HandshakePacket;
import org.takeyourdata.protocol.packets.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Base64;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                int length = in.readInt();
                byte[] data = new byte[length];
                in.readFully(data);

                HandshakePacket packet = (HandshakePacket) Packet.getData(data);

                System.out.println("CLIENT ID: " + packet.getClientId());
                System.out.println("CLIENT NONCE: " + Base64.getEncoder().encodeToString(packet.getClientNonce()));
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
}
