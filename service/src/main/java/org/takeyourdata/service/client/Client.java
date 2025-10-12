package org.takeyourdata.service.client;

import java.io.*;
import java.net.Socket;
import java.security.*;
import java.time.Instant;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket client = new Socket("127.0.0.1", 443);

        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        // test
        long timestamp = Instant.now().toEpochMilli();
        String clientId = "ID";

        out.writeInt(1 + 4 + 4 + clientId.length() + 2 + 4);
        out.writeByte(0x01);
        out.writeInt(1);
        out.writeLong(timestamp);
        out.writeUTF(clientId);
        out.flush();
    }
}
