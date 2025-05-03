package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.exception.PomValidationException;

import java.util.List;
import java.util.Set;

/**
 * The {@code ResultCaller} class serves as a utility for managing and validating
 * potential error states during operations. It allows for the accumulation of error messages
 * and provides mechanisms to validate these errors, throwing exceptions when the
 * validation fails.
 * <p>
 * This class is always its methods are always terminator of an analysis.
 * Without it won't display the potential errors.
 */
public final class ResultCaller {

    private final List<String> errorMessages;
    private final StringBuilder stringBuilder;

    private boolean hasErrors;

    /**
     * Constructs a {@code ResultCaller} instance with the provided list of error messages.
     * This constructor initializes an internal {@code StringBuilder} used for assembling
     * error details when validation methods are invoked.
     *
     * @param errorMessages the list of error messages to be managed by this instance
     */
    public ResultCaller(final List<String> errorMessages) {
        this.errorMessages = errorMessages;
        this.stringBuilder = new StringBuilder();
    }

    /**
     * Validates the current instance by checking for any accumulated error messages.
     * If error messages are present, invokes the error display mechanism.
     * <p>
     * This method is typically used to ensure the integrity of the instance
     * based on predefined validation rules. It throws an exception
     * with details about the validation errors if any are found.
     *
     * @throws PomValidationException if validation fails due to the presence of error messages.
     */
    public void validate() {
        if (hasErrors) {
            displayError();
        }
    }
    /**
     * Validates the current instance with a specific reason for validation.
     * This method prepends the provided reason to any error messages before
     * performing the validation check.
     *
     * @param reason the reason for validation, which will be included in any error message
     * @throws PomValidationException if validation fails due to the presence of error messages
     */
    public void validate(final String reason) {
        stringBuilder.insert(0, reason + "\n");
        validate();
    }

    /**
     * Appends all error messages to the internal string builder if any error messages
     * are present in the instance.
     * <p>
     * This method does not clear the list of error messages or throw an exception.
     * It simply collects and formats the errors into the internal string builder.
     * <p>
     * This method is typically used internally to prepare error messages for
     * further operations such as validation or displaying errors.
     */
    public void checkForErrors() {
        if (!errorMessages.isEmpty()) {
            hasErrors = true;
            stringBuilder.append("Error(s) occurred:\n");
            errorMessages.stream()
                    .map(errorMessage -> errorMessage + "\n")
                    .forEach(stringBuilder::append);
            errorMessages.clear();
        }
    }

    /**
     * Clears error messages and resets the internal state.
     */
    public void clearErrors() {
        errorMessages.clear();
        stringBuilder.setLength(0);
        hasErrors = false;
    }

    private void displayError() {
        throw new PomValidationException(stringBuilder.toString());
    }
}
