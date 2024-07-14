package com.vanguard.exception;

public class ResultsNotFoundException extends RuntimeException {
    public ResultsNotFoundException(String message) {
        super(message);
    }
}
