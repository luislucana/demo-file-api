package br.com.exemplo.demofileapi.util.exception;

public class InvalidLayoutException extends RuntimeException {

    public InvalidLayoutException() {
        super();
    }

    public InvalidLayoutException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidLayoutException(final String message) {
        super(message);
    }

    public InvalidLayoutException(final Throwable cause) {
        super(cause);
    }
}
