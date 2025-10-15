package org.takeyourdata.protocol;

import java.io.IOException;

public class NonceException extends IOException {
    public NonceException(String message) {
        super(message);
    }
}
