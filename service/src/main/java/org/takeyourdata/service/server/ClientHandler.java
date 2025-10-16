package org.takeyourdata.service.server;

import org.jetbrains.annotations.NotNull;
import org.takeyourdata.protocol.exceptions.NonceException;
import org.takeyourdata.protocol.exceptions.SignatureException;
import org.takeyourdata.protocol.packets.*;
import org.takeyourdata.service.server.handlers.HandshakeHandler;
import org.takeyourdata.service.server.handlers.KeyExchangeHandler;
import org.takeyourdata.service.server.handlers.SessionHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

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
            closeConnection();
        }
    }

    private void sendPacket(@NotNull Packet packet) throws Exception {
        byte[] data = packet.serialize(packet.getType());
        out.writeInt(data.length);
        out.write(data);
        out.flush();
    }

    private void handlePacket(Packet packet) throws Exception {
        if (packet instanceof HandshakePacket handshakePacket) {
            HandshakeHandler handshakeHandler = new HandshakeHandler(handshakePacket);

            handshakeHandler.handle(result -> {
                SessionPacket sessionPacket = (SessionPacket) result;
                sessionPacket.writeData(out);
                sendPacket(handshakePacket);
                sendPacket(result);
            });
        } else if (packet instanceof KeyExchangePacket keyExchangePacket) {
            PublicKey publicKey = KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(keyExchangePacket.getPublicKey()));

            if (!keyExchangePacket.verifySignature(publicKey, keyExchangePacket.getSignature())) {
                ErrorPacket errorPacket = new ErrorPacket(new SignatureException("Signature is invalid"));
                sendPacket(errorPacket);
                closeConnection();
            }

            KeyExchangeHandler keyExchangeHandler = new KeyExchangeHandler(keyExchangePacket);

            keyExchangeHandler.handle(result -> {
                // soon
            });
        } else if (packet instanceof SessionPacket sessionPacket) {
            SessionHandler sessionHandler = new SessionHandler(sessionPacket);

            sessionHandler.handle(result -> {
                HandshakePacket handshakePacket = (HandshakePacket) result;
                byte[] nonce = handshakePacket.getClientNonce();

                if (nonce != sessionPacket.getPacketNonce()) {
                    ErrorPacket errorPacket = new ErrorPacket(new NonceException("Nonce is invalid"));
                    sendPacket(errorPacket);
                    closeConnection();
                }

                new ClientSession(
                        socket,
                        nonce,
                        handshakePacket.getUserId(),
                        handshakePacket.getClientId(),
                        handshakePacket.getHardwareId(),
                        handshakePacket.getLocation()
                );
            });
        } else if (packet instanceof ErrorPacket errorPacket) {
            sendPacket(errorPacket);
        }
    }

    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
