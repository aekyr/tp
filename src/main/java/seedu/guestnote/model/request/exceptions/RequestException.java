package seedu.guestnote.model.request.exceptions;

/**
 * Signals that an operation targeting a request in the model has some error.
 */
public class RequestException extends RuntimeException {

    private final String request;

    /**
     * Constructs a new RequestException with the specified detail {@code message} and {@code request}.
     */
    public RequestException(String message, String request) {
        super(message);
        this.request = request;
    }

    public String getErrorRequest() {
        return request;
    }
}
