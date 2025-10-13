package org.takeyourdata.protocol.packets;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;

public class KeyExchangePacket extends Packet {
    private byte[] publicKey;
    private byte[] signature;
    private byte[] privateKey;

    private final Signature sig = Signature.getInstance("SHA256withRSA");

    public KeyExchangePacket(@NotNull DataInputStream dis) throws Exception {
        super(PacketType.KEY_EXCHANGE.getValue());

        this.publicKey = dis.readNBytes(256);
        this.signature = dis.readNBytes(32);
    }

    public KeyExchangePacket() throws Exception {}

    public boolean verifySignature(PublicKey verifyingKey, byte[] clientSign) throws Exception {
        sig.initVerify(verifyingKey);
        sig.update(publicKey);
        return sig.verify(clientSign);
    }

    @Override
    public void writeData(@NotNull DataOutputStream dos) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey serverPk = keyPair.getPublic();
        PrivateKey serverSk = keyPair.getPrivate();
        this.privateKey = serverSk.getEncoded();
        sig.initSign(serverSk);
        sig.update(serverPk.getEncoded());

        dos.write(serverPk.getEncoded());
        dos.write(sig.sign());
    }

    public byte[] getPublicKey() { return publicKey; } // Client Public Key
    public byte[] getPrivateKey() { return privateKey; } // Server Private Key
    public byte[] getSignature() { return signature; } // Client Signature
}
