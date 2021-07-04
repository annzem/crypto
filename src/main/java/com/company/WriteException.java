package com.company;

public class WriteException extends Exception {
    public WriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public WriteException(String message) {
        super(message);
    }

    public WriteException(Throwable cause) {
        super(cause);
    }
}
