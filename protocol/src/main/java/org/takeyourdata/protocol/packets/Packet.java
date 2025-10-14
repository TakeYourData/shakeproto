package org.takeyourdata.protocol.packets;

import org.jetbrains.annotations.NotNull;
import org.takeyourdata.protocol.ShakeProtocol;

import java.io.*;
import java.time.Instant;

public abstract class Packet implements Serializable {
    protected byte type;
    protected int version;
    protected long timestamp;

    public Packet(byte type) {
        this.type = type;
        this.version = ShakeProtocol.VERSION;
        this.timestamp = Instant.now().toEpochMilli();
    }

    public Packet() {}

    public byte[] serialize(byte type) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        dos.write(type);
        dos.writeInt(this.version);
        dos.writeLong(this.timestamp);

        writeData(dos);

        return out.toByteArray();
    }

    public static @NotNull Packet deserialize(byte[] data) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(in);

        byte type = dis.readByte();
        int version = dis.readInt();
        long timestamp = dis.readLong();


        Packet packet = switch (type) {
            case 0x01 -> new HandshakePacket(dis);
            case 0x02 -> new KeyExchangePacket(dis);
            case 0x03 -> new MessagePacket(dis);
            case 0x06 -> new SessionPacket();
            default -> null;
        };

        packet.type = type;
        packet.version = version;
        packet.timestamp = timestamp;

        return packet;
    }

    protected abstract void writeData(DataOutputStream dos) throws Exception;

    public int getVersion() {
        return version;
    }

    public byte getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
