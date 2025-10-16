package org.takeyourdata.protocol.exceptions;

import java.io.IOException;

public class NonceException extends IOException {
    public NonceException(String message) {
        super(message);
    }
}
