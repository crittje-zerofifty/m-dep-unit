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

    public T and() {
        if (!negateNext.isNegateNext()) {
            return value;
        }

        resultCaller.clearErrors();

        return value;
    }

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

    public void validate() {
        resultCaller.validate();
    }

    public void validate(final String reason) {
        resultCaller.validate(reason);
    }
}
