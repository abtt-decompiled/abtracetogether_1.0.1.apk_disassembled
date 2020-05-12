package com.worklight.common;

public class NoSuchResourceException extends RuntimeException {
    private static final long serialVersionUID = 7561423853842800353L;

    public NoSuchResourceException(String str, Exception exc) {
        super(str, exc);
    }
}
