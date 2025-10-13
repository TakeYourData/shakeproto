package org.takeyourdata.service.server.handlers;

import org.jetbrains.annotations.NotNull;
import org.takeyourdata.protocol.packets.HandshakePacket;

public class HandshakeHandler implements Handler {
    private final HandshakePacket packet;
    private final String clientId;
    private final String hardwareId;
    private final String location;

    public HandshakeHandler(@NotNull HandshakePacket packet) {
        this.packet = packet;
        this.clientId = packet.getClientId();
        this.hardwareId = packet.getHardwareId();
        this.location = packet.getLocation();
    }

    @Override
    public void handle() {
    }
}
