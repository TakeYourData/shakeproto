package org.takeyourdata.protocol.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessagePacket extends Packet {
    private String senderId;
    private String recipientId;
    private byte[] encryptedContent;

    public MessagePacket(DataInputStream dis) throws Exception {
        super(PacketType.MESSAGE.getValue());

        this.senderId = dis.readUTF();
        this.recipientId = dis.readUTF();
        this.encryptedContent = dis.readAllBytes();
    }

    public MessagePacket() {}

//    public byte[] encryptMessage(byte[] content, byte[] sessionKey) throws Exception {
//    }
//
//    public byte[] decryptMessage(byte[] sessionKey) throws Exception {
//    }

    @Override
    public void serializeData(DataOutputStream dos) throws IOException {
        dos.write(encryptedContent);
    }

    public String getSenderId() {
        return senderId;
    }
    public String getRecipientId() {
        return recipientId;
    }
    public byte[] getEncryptedContent() {
        return encryptedContent;
    }
}
