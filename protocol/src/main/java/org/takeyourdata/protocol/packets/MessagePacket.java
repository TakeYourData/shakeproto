package org.takeyourdata.protocol.packets;

import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

public class MessagePacket extends Packet {
    private final byte[] nonce;
    private int chatId;
    private int msgId;
    private int senderId;
    private int recipientId;
    private final byte[] authId;
    // private byte[] msgKey;
    private final byte[] encryptedContent;

    public MessagePacket(@NotNull DataInputStream dis) throws Exception {
        super(PacketType.MESSAGE.getValue());

        this.nonce = dis.readNBytes(32);
        this.chatId = dis.readInt();
        this.msgId = dis.readInt();
        this.senderId = dis.readInt();
        this.recipientId = dis.readInt();
        this.authId = dis.readNBytes(20); // sha-1 hash = 20 bytes
    //    this.msgKey = dis.readNBytes(16);
        this.encryptedContent = dis.readAllBytes();
    }

    public MessagePacket(
            byte[] nonce,
            byte[] authId,
            byte[] encryptedContent
            ) {
        this.nonce = nonce;
        this.authId = authId;
        this.encryptedContent = encryptedContent;
    }

    public byte[] encryptMessage(byte[] content, byte[] sessionKey) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(sessionKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(content);

        byte[] result = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

        return result;
    }

    public byte[] decryptMessage(byte[] sessionKey) throws Exception {
        byte[] iv = Arrays.copyOfRange(encryptedContent, 0, 16);
        byte[] encrypted = Arrays.copyOfRange(encryptedContent, 16, encryptedContent.length);

        SecretKeySpec keySpec = new SecretKeySpec(sessionKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(encrypted);
    }

    @Override
    public void writeData(@NotNull DataOutputStream dos) throws IOException {
        dos.write(nonce);
        dos.write(authId);
        dos.write(encryptedContent);
    }

    public byte[] getNonce() {
        return nonce;
    }
    public int getChatId() {
        return chatId;
    }
    public int getMsgId() {
        return msgId;
    }
    public int getSenderId() {
        return senderId;
    }
    public int getRecipientId() {
        return recipientId;
    }
    public byte[] getAuthId() {
        return authId;
    }
    /* public byte[] getMsgKey() {
        return msgKey;
    }
    */
    public byte[] getEncryptedContent() {
        return encryptedContent;
    }
}
