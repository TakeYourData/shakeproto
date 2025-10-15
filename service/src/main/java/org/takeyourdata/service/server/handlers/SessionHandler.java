package org.takeyourdata.service.server.handlers;

import org.jetbrains.annotations.NotNull;
import org.takeyourdata.protocol.packets.HandshakePacket;
import org.takeyourdata.protocol.packets.SessionPacket;
import org.takeyourdata.service.server.JedisClient;
import org.takeyourdata.service.server.ProcessedPacket;
import redis.clients.jedis.JedisPooled;

import java.util.Base64;

public class SessionHandler implements Handler {
    private final byte[] sessionToken;

    public SessionHandler(@NotNull SessionPacket packet) {
        this.sessionToken = packet.getSessionToken();
    }

    @Override
    public void handle(@NotNull ProcessedPacket packet) throws Exception {
        String token = Base64.getEncoder().withoutPadding().encodeToString(sessionToken);

        JedisPooled jedis = new JedisClient().getJedis();

        byte[] clientNonce = Base64.getDecoder().decode(jedis.hget(token, "clientNonce"));
        int userId = Integer.parseInt(jedis.hget(token, "userId"));
        String clientId = jedis.hget(token, "clientId");
        String hardwareId = jedis.hget(token, "hardwareId");
        String location = jedis.hget(token, "location");

        HandshakePacket handshakePacket = new HandshakePacket(
                clientNonce,
                userId,
                clientId,
                hardwareId,
                location
        );

        packet.process(handshakePacket);
    }
}
