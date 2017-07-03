package com.example.bilalekremharmansa.calculator;

/**
 * Created by bilalekremharmansa on 3.7.2017.
 */

public class SyntaxErrorException extends RuntimeException {
    public SyntaxErrorException() {

    }

    public SyntaxErrorException(String message) {
        super(message);
    }

    public SyntaxErrorException(String message, Throwable cause) {
        super(message, cause);
    }



}
