package com.anish.wallet_api.exception;

public class InvalidWalletStateException extends RuntimeException {

    public InvalidWalletStateException(String message) {
        super(message);
    }
}