package nl.zerofiftyit.mdepunit.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NegateNextTest {

    private NegateNext testable;

    @Test
    void testApplyNegateNext() {
        testable = new NegateNext();
        testable.applyNegateNext();
        assertTrue(testable.isNegateNext());
    }

    @Test
    void testApplyNegateNextTwice() {
        testable = new NegateNext();
        testable.applyNegateNext();
        testable.applyNegateNext();
        assertFalse(testable.isNegateNext());
    }

    @Test
    void testIsNegateNext() {
        testable = new NegateNext();
        assertFalse(testable.isNegateNext());
    }

    @Test
    void testToString() {
        testable = new NegateNext();
        assertEquals("NegateNext{"
                + "negateNext="
                + false
                + '}', testable.toString());
    }

}
