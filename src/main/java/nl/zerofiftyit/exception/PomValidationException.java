package nl.zerofiftyit.exception;

/**
 * Exception thrown when POM validation fails.
 * This exception is used to indicate errors during the validation of Maven POM files,
 * such as missing required elements or invalid configurations.
 */
public class PomValidationException extends RuntimeException {

    /**
     * Constructs a new PomValidationException with the specified error message.
     *
     * @param message the error message detailing the validation failure
     */
    public PomValidationException(final String message) {
        super(message);
    }
}
