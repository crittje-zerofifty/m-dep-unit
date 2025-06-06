package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.exception.PomValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResultCallerTest {

    private ResultCaller testable;
    private List<String> errorMessages;

    @BeforeEach
    void setup() {

        errorMessages = new ArrayList<>();

        testable = new ResultCaller(errorMessages);
    }

    @Test
    void testValidateWhenNoErrorsDoesNotThrowException() {

        assertDoesNotThrow(() -> testable.validate(),
                "validate() should not throw an exception when no errors are present");
    }

    @Test
    void testValidateWhenErrorsArePresentDoesThrow() {

        errorMessages.add("error1");

        testable.checkForErrors();

        assertThrows(PomValidationException.class, () -> testable.validate());
    }

    @Test
    void testValidateWithReasonWhenNoErrorsDoesNotThrowException() {

        assertDoesNotThrow(() -> testable.validate("reason"),
                "validate() should not throw an exception when no errors are present");
    }

    @Test
    void testValidateWithReasonWhenErrorsArePresentDoesThrow() {

        errorMessages.add("error1");

        testable.checkForErrors();

        Exception e = assertThrows(PomValidationException.class, () -> testable.validate("reason"));

        assertTrue(e.getMessage().contains("reason"));
        assertTrue(errorMessages.isEmpty());

    }

    @Test
    void testCheckForErrorAppendsToErrorMessagesIfApplied() {

        errorMessages.add("error1");

        testable.checkForErrors();

        Exception e = assertThrows(PomValidationException.class, () -> testable.validate("reason"));

        assertTrue(e.getMessage().contains("reason"));
        assertTrue(e.getMessage().contains("error1"));
        assertTrue(errorMessages.isEmpty());
    }

    @Test
    void testCheckForErrorDoesNotAppendToErrorMessagesIfNotApplied() {

        testable.checkForErrors();

        assertDoesNotThrow(() -> testable.validate("reason"),
                "validate() should not throw an exception when no errors are present");

        assertEquals(0, errorMessages.size());

    }

    @Test
    void testClearErrors() {

        errorMessages.add("error1");

        testable.clearErrors();

        assertEquals(0, errorMessages.size());
        assertDoesNotThrow(() -> testable.validate());
    }

    @Test
    void testValidateWhenReasonIsNull() {

        Assertions.assertThrows(NullPointerException.class, () -> testable.validate(null));
    }
}
