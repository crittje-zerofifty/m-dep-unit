package nl.zerofiftyit.mdepunit.dsl;

import lombok.RequiredArgsConstructor;
import nl.zerofiftyit.mdepunit.core.validation.ResultCaller;
import nl.zerofiftyit.mdepunit.exception.PomValidationException;
import nl.zerofiftyit.mdepunit.model.NegateNext;

@RequiredArgsConstructor
public final class Statement<T> {

    private final T value;
    private final ResultCaller resultCaller;
    private final NegateNext negateNext;

    /**
     * Allows for a and condition to apply to validations.
     *
     * @return the value of type {@code T} associated with this statement
     */
    public T and() {
        if (!negateNext.isNegateNext()) {
            return value;
        }

        resultCaller.clearErrors();

        return value;
    }

    /**
     * Defines an "or" conditional in the validation chain.
     *
     * @return the value of type {@code T} associated with this statement
     */
    public T or() {
        if (!negateNext.isNegateNext()) {
            return value;
        }

        try {
            resultCaller.validate();
        } catch (PomValidationException e) {
            // do nothing
        }

        return value;
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
        resultCaller.validate();
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
        resultCaller.validate(reason);
    }
}
