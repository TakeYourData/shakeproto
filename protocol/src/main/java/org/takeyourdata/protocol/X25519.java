package org.takeyourdata.protocol;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;

import javax.crypto.KeyAgreement;
import java.security.*;

public class X25519 {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte @NotNull [] dH(PublicKey publicKey, PrivateKey privateKey) throws Exception {
        KeyAgreement ka = KeyAgreement.getInstance("X25519", "BC");
        SecureRandom secureRandom = new SecureRandom();

        ka.init(privateKey);
        ka.doPhase(publicKey, true);
        byte[] rawSecret = ka.generateSecret();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        byte[] info = "Shakeproto AES-GCM key derivation".getBytes();
        return derive(rawSecret, salt, info);
    }

    private static byte @NotNull [] derive(byte[] sharedSecret, byte[] salt, byte[] info) {
        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
        HKDFParameters params = new HKDFParameters(sharedSecret, salt, info);

        hkdf.init(params);
        byte[] aesKey = new byte[32];
        hkdf.generateBytes(aesKey, 0, 32);
        return aesKey;
    }
}
