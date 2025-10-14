package org.takeyourdata.protocol.packets;

import org.jetbrains.annotations.NotNull;
import org.takeyourdata.protocol.Token;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SessionPacket extends Packet {
    private final byte[] sessionToken;

    public SessionPacket(DataInputStream dis) throws IOException {
        super(PacketType.SESSION.getValue());
        this.sessionToken = dis.readNBytes(32);
    }

    public SessionPacket() {
        Token token = new Token();
        this.sessionToken = token.getSessionToken();
    }

    @Override
    public void writeData(@NotNull DataOutputStream dos) throws IOException {
        int length = sessionToken.length;

        dos.writeInt(length);
        dos.write(sessionToken);
    }

    public byte[] getSessionToken() {
        return sessionToken;
    }
}
