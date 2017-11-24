package com.chimpler.simtick;

public class SimtickException extends RuntimeException {
    public SimtickException() {
        super();
    }

    public SimtickException(String message) {
        super(message);
    }

    public SimtickException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimtickException(Throwable cause) {
        super(cause);
    }

    protected SimtickException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
