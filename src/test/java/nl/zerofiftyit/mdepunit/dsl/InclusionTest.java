package nl.zerofiftyit.mdepunit.dsl;

import nl.zerofiftyit.mdepunit.model.NegateNext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class InclusionTest {

    private Inclusion<String> testable;
    private final String testValue = "testValue";

    private NegateNext negateNext;

    @BeforeEach
    void setUp() {
        negateNext = mock(NegateNext.class);
        testable = new Inclusion<>(testValue, negateNext);
    }

    @Test
    void testShouldSetsNegateNextToFalse() {
        String result = testable.should();

        assertEquals(result, testValue);
    }

    @Test
    void testShouldNotSetsNegateNextToTrue() {
        String result = testable.shouldNot();

        verify(negateNext).applyNegateNext();
        assertEquals(result, testValue);
    }
}
