package org.takeyourdata.service.server.handlers;

import io.github.jopenlibs.vault.Vault;
import org.jetbrains.annotations.NotNull;
import org.takeyourdata.protocol.X25519;
import org.takeyourdata.protocol.packets.KeyExchangePacket;
import org.takeyourdata.service.server.ConfigProperties;
import org.takeyourdata.service.server.ProcessedPacket;
import org.takeyourdata.service.server.databases.VaultClient;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;

public class KeyExchangeHandler implements Handler {
    private final PublicKey cpk;
    private final PrivateKey sk;
    private final int userId;

    public KeyExchangeHandler(@NotNull KeyExchangePacket keyExchangePacket, int userId) throws Exception {
        this.cpk = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(keyExchangePacket.getPublicKey()));
        this.sk = keyExchangePacket.getPrivateKey();
        this.userId = userId;
    }

    @Override
    public void handle(@NotNull ProcessedPacket packet) throws Exception {
        byte[] secretKey = X25519.dH(cpk, sk);

        Vault vault = new VaultClient().getVault();
        Properties config = new ConfigProperties().get();

        String stringSecretKey = Base64.getEncoder().withoutPadding().encodeToString(secretKey);

        vault.logical().write(config.getProperty("database.vault.path" + "/users/"
                + userId + "/" + Base64.getEncoder().withoutPadding().encodeToString(hash(secretKey))),
                Map.of("key", stringSecretKey));

        KeyExchangePacket keyExchangePacket = new KeyExchangePacket(secretKey);

        packet.process(keyExchangePacket);
    }

    private byte[] hash(byte[] key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return md.digest(key);
    }
}
