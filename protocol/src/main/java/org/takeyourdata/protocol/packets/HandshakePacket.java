package org.takeyourdata.protocol.packets;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

public class HandshakePacket extends Packet {
    private final int userId;
    private final String clientId;
    private final byte[] clientNonce;
    private final String hardwareId;
    private final String location;

    public HandshakePacket(@NotNull DataInputStream dis) throws IOException {
        super(PacketType.HANDSHAKE.getValue());
        this.clientNonce = new byte[32];
        new SecureRandom().nextBytes(clientNonce);
        this.userId = dis.readInt();
        this.clientId = dis.readUTF();
        this.hardwareId = dis.readUTF();
        this.location = dis.readUTF();
    }

    public HandshakePacket(byte[] clientNonce,
                           int userId,
                           String clientId,
                           String hardwareId,
                           String location) {
        this.clientNonce = clientNonce;
        this.userId = userId;
        this.clientId = clientId;
        this.hardwareId = hardwareId;
        this.location = location;
    }

    @Override
    public void writeData(@NotNull DataOutputStream dos) throws IOException {
        dos.writeInt(clientNonce.length);
        dos.write(clientNonce);
    }

    public int getUserId() {
        return userId;
    }

    public String getClientId() {
        return clientId;
    }

    public byte[] getClientNonce() {
        return clientNonce;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public String getLocation() {
        return location;
    }
}
