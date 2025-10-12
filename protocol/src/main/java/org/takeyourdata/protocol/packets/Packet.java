package org.takeyourdata.protocol.packets;

import org.takeyourdata.protocol.ShakeProtocol;

import java.io.*;
import java.time.Instant;
import java.util.Base64;

public abstract class Packet {
    protected byte type;
    protected int version;
    protected long timestamp;

    public Packet(byte type) {
        this.type = type;
        this.version = ShakeProtocol.VERSION;
        this.timestamp = Instant.now().toEpochMilli();
    }

    public Packet() {}

    public byte[] sendData(byte type) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        dos.write(type);
        dos.writeInt(this.version);
        dos.writeLong(this.timestamp);

        serializeData(dos);

        return out.toByteArray();
    }

    public static Packet getData(byte[] data) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(in);

        byte type = dis.readByte();
        int version = dis.readInt();
        long timestamp = dis.readLong();

        System.out.println(type);
        System.out.println(version);
        System.out.println(timestamp);

        Packet packet = switch (type) {
            case 0x01 -> new HandshakePacket(dis);
            case 0x02 -> new SessionPacket(dis);
            case 0x03 -> new MessagePacket(dis);
            default -> null;
        };

        packet.version = version;
        packet.timestamp = timestamp;

        return packet;
    }

    protected abstract void serializeData(DataOutputStream dos) throws Exception;

    public int getVersion() {
        return version;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
