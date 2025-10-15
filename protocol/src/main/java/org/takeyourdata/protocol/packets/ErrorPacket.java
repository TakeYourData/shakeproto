package org.takeyourdata.protocol.packets;

import java.io.DataOutputStream;

public class ErrorPacket extends Packet {
    private final String error;

    public ErrorPacket(Exception error) {
        super(PacketType.ERROR.getValue());
        this.error = error.getMessage();
    }

    public ErrorPacket(String error) {
        super(PacketType.ERROR.getValue());
        this.error = error;
    }

    @Override
    public void writeData(DataOutputStream dos) throws Exception {
        dos.writeUTF(error);
    }
}
