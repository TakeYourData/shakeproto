package org.takeyourdata.protocol;

import java.security.SecureRandom;

public class Token {
    private final byte[] sessionToken;

    public Token() {
        SecureRandom sr = new SecureRandom();
        byte[] bytes = new byte[32];
        sr.nextBytes(bytes);

        this.sessionToken = bytes;
    }

    public byte[] getSessionToken() {
        return sessionToken;
    }
}
