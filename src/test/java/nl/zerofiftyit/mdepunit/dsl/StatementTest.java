package nl.zerofiftyit.mdepunit.dsl;

import nl.zerofiftyit.mdepunit.core.validation.ResultCaller;
import nl.zerofiftyit.mdepunit.exception.PomValidationException;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatementTest {

    private Statement<String> statement;
    private ResultCaller resultCaller;
    private NegateNext negateNext;
    private final String testValue = "testValue";

    @BeforeEach
    void setUp() {
        resultCaller = Mockito.mock(ResultCaller.class);
        negateNext = Mockito.mock(NegateNext.class);
        statement = new Statement<>(testValue, resultCaller, negateNext);
    }

    @Test
    void testAndWhenNegateNextIsFalse() {
        when(negateNext.isNegateNext()).thenReturn(false);

        String result = statement.and();

        assertEquals(testValue, result);
        verify(resultCaller, never()).clearErrors();
    }

    @Test
    void testAndWhenNegateNextIsTrue() {
        // Arrange
        when(negateNext.isNegateNext()).thenReturn(true);

        String result = statement.and();

        assertEquals(testValue, result);
        verify(resultCaller).clearErrors();
    }

    @Test
    void testOrWhenNegateNextIsFalse() {
        when(negateNext.isNegateNext()).thenReturn(false);

        String result = statement.or();

        assertEquals(testValue, result);
        verify(resultCaller, never()).clearErrors();
        verify(resultCaller, never()).validate();
    }

    @Test
    void testsOrWhenNegateNextIsTrue() {

        when(negateNext.isNegateNext()).thenReturn(true);

        String result = statement.or();

        assertEquals(testValue, result);
        verify(resultCaller, never()).clearErrors();
        verify(resultCaller).validate();
    }


    @Test
    void testOrWhenPomValidationThrowsIsIgnored() {

        when(negateNext.isNegateNext()).thenReturn(true);
        doThrow(new PomValidationException("test")).when(resultCaller).validate();

        String result = statement.or();

        assertEquals(testValue, result);
        verify(resultCaller, never()).clearErrors();
        verify(resultCaller).validate();
    }

    @Test
    void testValidate() {
        statement.validate();
        verify(resultCaller).validate();
    }

    @Test
    void testValidateWithReason() {
        String reason = "test";
        statement.validate(reason);
        verify(resultCaller).validate(reason);
    }
}
