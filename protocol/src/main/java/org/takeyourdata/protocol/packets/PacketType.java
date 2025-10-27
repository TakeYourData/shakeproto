package org.takeyourdata.protocol.packets;

public enum PacketType {
    HANDSHAKE (0x01),
    KEY_EXCHANGE(0x02),
    MESSAGE (0x03),
    ACK (0x04),
    ERROR (0x05),
    SESSION (0x06),
    SYNC(0x07);

    private final byte value;

    PacketType(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
