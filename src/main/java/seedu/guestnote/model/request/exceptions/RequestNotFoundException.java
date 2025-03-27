package seedu.guestnote.model.request.exceptions;

/**
 * Signals that the operation is unable to find the specified request.
 */
public class RequestNotFoundException extends RequestException {

    public RequestNotFoundException(String request) {
        super("Operation would result in duplicate requests", request);
    }
}
