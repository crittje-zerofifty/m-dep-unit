package nl.zerofiftyit.core.validation;

import nl.zerofiftyit.model.NegateNext;
import nl.zerofiftyit.model.PomElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PomAnalyzerTest {

    private PomAnalyzer testable;
    private ArrayList<PomElement> propertyPomElements;
    private ArrayList<String> errorMessages;
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
    public void testHavePropertyWhenShouldAndHas() {

        testable = new PomAnalyzer("properties",
                propertyPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new ArrayList<>());

        when(negateNext.isNegateNext()).thenReturn(false);

        ResultCaller result = testable.haveProperty("java.version");

        assertEquals(result, resultCaller);
        assertTrue(errorMessages.isEmpty());
    }

    @Test
    public void testHavePropertyWhenShouldAndHasNot() {

        testable = new PomAnalyzer("properties",
                propertyPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new ArrayList<>());

        when(negateNext.isNegateNext()).thenReturn(false);

        ResultCaller result = testable.haveProperty("not.existing.property");

        assertEquals(result, resultCaller);
        assertEquals(1, errorMessages.size());

    }

    @Test
    public void testHavePropertyWhenShouldNotAndHas() {

        testable = new PomAnalyzer("properties",
                propertyPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new ArrayList<>());

        when(negateNext.isNegateNext()).thenReturn(true);

        ResultCaller result = testable.haveProperty("java.version");

        assertEquals(result, resultCaller);
        assertEquals(1, errorMessages.size());
    }

    @Test
    public void testHavePropertyWhenShouldNotAndHasNot() {

        testable = new PomAnalyzer("properties",
                propertyPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new ArrayList<>());

        when(negateNext.isNegateNext()).thenReturn(true);

        ResultCaller result = testable.haveProperty("not.existing.version");

        assertEquals(result, resultCaller);
        assertTrue(errorMessages.isEmpty());
    }
    
}
