package org.takeyourdata.service.server.handlers;

import org.jetbrains.annotations.NotNull;
import org.takeyourdata.protocol.packets.HandshakePacket;
import org.takeyourdata.protocol.packets.SessionPacket;
import org.takeyourdata.service.server.JedisClient;
import org.takeyourdata.service.server.ProcessedPacket;
import redis.clients.jedis.JedisPooled;

import java.util.Base64;

public class HandshakeHandler implements Handler {
    private final int userId;
    private final String clientId;
    private final String hardwareId;
    private final String location;
    private final byte[] clientNonce;

    public HandshakeHandler(@NotNull HandshakePacket packet) {
        this.userId = packet.getUserId();
        this.clientId = packet.getClientId();
        this.hardwareId = packet.getHardwareId();
        this.location = packet.getLocation();
        this.clientNonce = packet.getClientNonce();
    }

    @Override
    public void handle(@NotNull ProcessedPacket packet) throws Exception {
        SessionPacket sessionPacket = new SessionPacket();
        JedisPooled jedis = new JedisClient().getJedis();
        String token = Base64.getEncoder().withoutPadding().encodeToString(sessionPacket.getSessionToken());

        jedis.hset(
                token,
                "clientNonce",
                Base64.getEncoder().withoutPadding().encodeToString(clientNonce)
        );
        jedis.hset(
                token,
                "userId",
                String.valueOf(userId)
        );
        jedis.hset(
                token,
                "clientId",
                clientId
        );
        jedis.hset(
                token,
                "hardwareId",
                hardwareId
        );
        jedis.hset(
                token,
                "location",
                location
        );

        packet.process(sessionPacket);
    }
}
