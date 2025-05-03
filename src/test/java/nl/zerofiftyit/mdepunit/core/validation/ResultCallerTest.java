package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.exception.PomValidationException;
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

        Exception e = assertThrows(PomValidationException.class, () -> testable.validate("reason"));

        assertTrue(e.getMessage().contains("reason"));
        assertEquals(1, errorMessages.size());

    }

    @Test
    void testCheckForErrorAppendsToErrorMessagesIfApplied() {

        errorMessages.add("error1");

        testable.checkForErrors();

        Exception e = assertThrows(PomValidationException.class, () -> testable.validate("reason"));

        assertTrue(e.getMessage().contains("reason"));
        assertTrue(e.getMessage().contains("error1"));
        assertEquals(1, errorMessages.size());
    }

    @Test
    void testCheckForErrorDoesNotAppendToErrorMessagesIfNotApplied() {

        testable.checkForErrors();

        assertDoesNotThrow(() -> testable.validate("reason"),
                "validate() should not throw an exception when no errors are present");

        assertEquals(0, errorMessages.size());

    }
}
