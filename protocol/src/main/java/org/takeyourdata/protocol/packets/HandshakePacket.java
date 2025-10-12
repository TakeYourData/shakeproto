package org.takeyourdata.protocol.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

public class HandshakePacket extends Packet {
    private String clientId;
    private byte[] clientNonce;

    public HandshakePacket(DataInputStream dis) throws IOException {
        super(PacketType.HANDSHAKE.getValue());
        this.clientNonce = new byte[32];
        new SecureRandom().nextBytes(clientNonce);
        this.clientId = dis.readUTF();
    }

    public HandshakePacket() {}

    @Override
    public void serializeData(DataOutputStream dos) {}

    public String getClientId() {
        return clientId;
    }

    public byte[] getClientNonce() {
        return clientNonce;
    }
}
