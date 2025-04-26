package nl.zerofiftyit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PomValidationExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "Invalid POM configuration";
        PomValidationException exception = new PomValidationException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionInheritance() {
        PomValidationException exception = new PomValidationException("Test");
        assertInstanceOf(RuntimeException.class, exception);
    }
}
