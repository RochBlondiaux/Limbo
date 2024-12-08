package me.rochblondiaux.limbo.exception.configuration;

public class ConfigurationSaveException extends RuntimeException {

    public ConfigurationSaveException() {
    }

    public ConfigurationSaveException(String message) {
        super(message);
    }

    public ConfigurationSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationSaveException(Throwable cause) {
        super(cause);
    }

    public ConfigurationSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
