package me.rochblondiaux.limbo.exception.command;

public class CommandRegistrationException extends RuntimeException {

    public CommandRegistrationException() {
    }

    public CommandRegistrationException(String message) {
        super(message);
    }

    public CommandRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandRegistrationException(Throwable cause) {
        super(cause);
    }

    public CommandRegistrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
