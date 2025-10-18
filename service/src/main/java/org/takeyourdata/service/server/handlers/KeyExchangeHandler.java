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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KeyExchangeHandler implements Handler {
    private final PublicKey cpk;
    private final PrivateKey sk;

    public KeyExchangeHandler(@NotNull KeyExchangePacket keyExchangePacket) throws Exception {
        this.cpk = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(keyExchangePacket.getPublicKey()));
        this.sk = keyExchangePacket.getPrivateKey();
    }

    @Override
    public void handle(@NotNull ProcessedPacket packet) throws Exception {
        byte[] secretKey = X25519.dH(cpk, sk);

        Vault vault = new VaultClient().getVault();
        Properties config = new ConfigProperties().get();

        Map<String, Object> data = new HashMap<>();

        data.put("id", hash(secretKey));
        data.put("key", secretKey);

        vault.logical().write(config.getProperty("database.vault.path"), data);

        packet.process(null);
    }

    private static byte[] hash(byte[] key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return md.digest(key);
    }
}
