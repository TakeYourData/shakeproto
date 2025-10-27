package org.takeyourdata.protocol.packets;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SyncPacket extends Packet {
    private final int chatId;

    public SyncPacket(@NotNull DataInputStream dis) throws IOException {
        super(PacketType.SYNC.getValue());
        this.chatId = dis.readInt();
    }

    @Override
    public void writeData(DataOutputStream dos) throws Exception {
    }

    public int getChatId() {
        return chatId;
    }
}
