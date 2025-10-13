package org.takeyourdata.protocol.packets;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

public class HandshakePacket extends Packet {
    private String clientId;
    private byte[] clientNonce;
    private String hardwareId;
    private String location;

    public HandshakePacket(@NotNull DataInputStream dis) throws IOException {
        super(PacketType.HANDSHAKE.getValue());
        this.clientNonce = new byte[32];
        new SecureRandom().nextBytes(clientNonce);
        this.clientId = dis.readUTF();
        this.hardwareId = dis.readUTF();
        this.location = dis.readUTF();
    }

    public HandshakePacket() {}

    @Override
    public void writeData(@NotNull DataOutputStream dos) throws IOException {
        dos.writeUTF(clientId);
        dos.writeInt(clientNonce.length);
        dos.write(clientNonce);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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
