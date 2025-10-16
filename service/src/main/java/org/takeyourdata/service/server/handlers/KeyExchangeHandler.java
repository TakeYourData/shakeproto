package org.takeyourdata.service.server.handlers;

import org.jetbrains.annotations.NotNull;
import org.takeyourdata.protocol.X25519;
import org.takeyourdata.protocol.packets.KeyExchangePacket;
import org.takeyourdata.service.server.ProcessedPacket;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class KeyExchangeHandler implements Handler {
    private final PublicKey cpk;
    private final PrivateKey sk;

    public KeyExchangeHandler(@NotNull KeyExchangePacket keyExchangePacket) throws Exception {
        this.cpk = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(keyExchangePacket.getPublicKey()));
        this.sk = keyExchangePacket.getPrivateKey();
    }

    @Override
    public void handle(ProcessedPacket packet) throws Exception {
        byte[] secretKey = X25519.dH(cpk, sk);
        // soon
    }
}
