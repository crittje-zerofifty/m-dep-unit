package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.dsl.Statement;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import nl.zerofiftyit.mdepunit.model.PomElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PomAnalyzerTest {

    private PomAnalyzer testable;
    private List<PomElement> propertyPomElements;
    private List<String> errorMessages;
    private ResultCaller resultCaller;
    private NegateNext negateNext;

    @BeforeEach
    void setUp() {
        propertyPomElements = new ArrayList<>();
        propertyPomElements.add(new PomElement("properties", new ArrayList<>()));
        propertyPomElements.add(new PomElement("properties.java.version", "11"));

        testable = new PomAnalyzer("properties",
                propertyPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new ArrayList<>());
    }

    @AfterEach
    void tearDown() {
        verify(resultCaller).checkForErrors();
    }

    @Test
    void testHavePropertyWhenShouldAndHas() {

        testable = new PomAnalyzer("properties",
                propertyPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new ArrayList<>());

        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<PomAnalyzer> result = testable.haveProperty("java.version");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
    }

    @Test
    void testHavePropertyWhenShouldAndHasNot() {

        testable = new PomAnalyzer("properties",
                propertyPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new ArrayList<>());

        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<PomAnalyzer> result = testable.haveProperty("not.existing.property");

        assertNotNull(result);
        assertEquals(1, errorMessages.size());

    }

    @Test
    void testHavePropertyWhenShouldNotAndHas() {

        testable = new PomAnalyzer("properties",
                propertyPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new ArrayList<>());

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<PomAnalyzer> result = testable.haveProperty("java.version");

        assertNotNull(result);
        assertEquals(1, errorMessages.size());
    }

    @Test
    void testHavePropertyWhenShouldNotAndHasNot() {

        testable = new PomAnalyzer("properties",
                propertyPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new ArrayList<>());

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<PomAnalyzer> result = testable.haveProperty("not.existing.version");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
    }
}
