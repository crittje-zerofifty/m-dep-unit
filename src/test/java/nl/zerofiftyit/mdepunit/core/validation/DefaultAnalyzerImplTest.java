package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.dsl.Statement;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import nl.zerofiftyit.mdepunit.model.PomElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultAnalyzerImplTest {

    private DefaultAnalyzerImpl testable;
    private List<PomElement> pomElements;
    private ResultCaller resultCaller;
    private NegateNext negateNext;
    private List<String> errorMessages;

    @BeforeEach
    void setup() {

        pomElements = new ArrayList<>();
        resultCaller = mock(ResultCaller.class);
        negateNext = mock(NegateNext.class);
        errorMessages = new ArrayList<>();

        testable = new DefaultAnalyzerImpl("dependencies.dependency", pomElements, resultCaller, negateNext, errorMessages);
    }

    @Test
    void testShouldHaveTagAndHas() {

        List<Object> dependencies = new ArrayList<>();
        PomElement dependenciesElement = new PomElement("dependencies.dependency", dependencies);
        PomElement artifactElement = new PomElement("dependencies.dependency[0].artifactId", "artifact");
        PomElement versionElement = new PomElement("dependencies.dependency[0].version", "1.0.0");

        pomElements.add(dependenciesElement);
        pomElements.add(artifactElement);
        pomElements.add(versionElement);

        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<DefaultAnalyzerImpl> result  = testable.haveTag("version");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
        verify(resultCaller).checkForErrors();

    }

    @Test
    void testShouldNotHaveTagButHas() {

        List<Object> dependencies = new ArrayList<>();
        PomElement dependenciesElement = new PomElement("dependencies.dependency", dependencies);
        PomElement artifactElement = new PomElement("dependencies.dependency[0].artifactId", "artifact");
        PomElement versionElement = new PomElement("dependencies.dependency[0].version", "1.0.0");

        pomElements.add(dependenciesElement);
        pomElements.add(artifactElement);
        pomElements.add(versionElement);

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<DefaultAnalyzerImpl> result = testable.haveTag("version");

        assertNotNull(result);
        assertEquals(2, errorMessages.size());
        verify(resultCaller).checkForErrors();
    }

    @Test
    void testShouldHaveTagAndHasNot() {

        List<Object> dependencies = new ArrayList<>();
        PomElement dependenciesElement = new PomElement("dependencies.dependency", dependencies);
        PomElement artifactElement = new PomElement("dependencies.dependency[0].artifactId", "artifact");
        PomElement versionElement = new PomElement("dependencies.dependency[0].groupId", "groupId");

        pomElements.add(dependenciesElement);
        pomElements.add(artifactElement);
        pomElements.add(versionElement);

        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<DefaultAnalyzerImpl> result  = testable.haveTag("version");

        assertNotNull(result);
        assertEquals(1, errorMessages.size());
        verify(resultCaller).checkForErrors();
    }

    @Test
    void testShouldNotHaveTagAndHasNot() {

        List<Object> dependencies = new ArrayList<>();
        PomElement dependenciesElement = new PomElement("dependencies.dependency", dependencies);
        PomElement artifactElement = new PomElement("dependencies.dependency[0].artifactId", "artifact");
        PomElement versionElement = new PomElement("dependencies.dependency[0].groupId", "groupId");

        pomElements.add(dependenciesElement);
        pomElements.add(artifactElement);
        pomElements.add(versionElement);

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<DefaultAnalyzerImpl> result = testable.haveTag("version");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
        verify(resultCaller).checkForErrors();
    }

    @Test
    void testContainsValueWhileItExpected() {

        List<Object> modules = new ArrayList<>();
        PomElement mainModules = new PomElement("modules.module", modules);
        PomElement moduleCustomer = new PomElement("modules.module[0]", "customer");
        PomElement moduleProduct = new PomElement("modules.module[1]", "product");

        pomElements.add(mainModules);
        pomElements.add(moduleCustomer);
        pomElements.add(moduleProduct);

        testable = new DefaultAnalyzerImpl("modules.module", pomElements, resultCaller, negateNext, errorMessages);

        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<DefaultAnalyzerImpl> result = testable.containValue("customer");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
        verify(resultCaller).checkForErrors();
    }

    @Test
    void testContainsValueWhileItNotExpected() {

        List<Object> modules = new ArrayList<>();
        PomElement mainModules = new PomElement("modules.module", modules);
        PomElement moduleCustomer = new PomElement("modules.module[0]", "customer");
        PomElement moduleProduct = new PomElement("modules.module[1]", "product");

        pomElements.add(mainModules);
        pomElements.add(moduleCustomer);
        pomElements.add(moduleProduct);

        testable = new DefaultAnalyzerImpl("modules.module", pomElements, resultCaller, negateNext, errorMessages);

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<DefaultAnalyzerImpl> result = testable.containValue("customer");

        assertNotNull(result);
        assertFalse(errorMessages.isEmpty());
        verify(resultCaller).checkForErrors();
    }

    @Test
    void testContainsValueWhileExpectedHasNot() {

        List<Object> modules = new ArrayList<>();
        PomElement mainModules = new PomElement("modules.module", modules);
        PomElement moduleCustomer = new PomElement("modules.module[0]", "customer");
        PomElement moduleProduct = new PomElement("modules.module[1]", "product");

        pomElements.add(mainModules);
        pomElements.add(moduleCustomer);
        pomElements.add(moduleProduct);

        when(negateNext.isNegateNext()).thenReturn(false);

        testable = new DefaultAnalyzerImpl("modules.module", pomElements, resultCaller, negateNext, errorMessages);

        Statement<DefaultAnalyzerImpl> result = testable.containValue("notexisting");

        assertNotNull(result);
        assertFalse(errorMessages.isEmpty());
        verify(resultCaller).checkForErrors();
    }

    @Test
    void testContainsValueWhileItNotExpectedAndShouldNot() {

        List<Object> modules = new ArrayList<>();
        PomElement mainModules = new PomElement("modules.module", modules);
        PomElement moduleCustomer = new PomElement("modules.module[0]", "customer");
        PomElement moduleProduct = new PomElement("modules.module[1]", "product");

        pomElements.add(mainModules);
        pomElements.add(moduleCustomer);
        pomElements.add(moduleProduct);

        when(negateNext.isNegateNext()).thenReturn(true);

        testable = new DefaultAnalyzerImpl("modules.module", pomElements, resultCaller, negateNext, errorMessages);

        Statement<DefaultAnalyzerImpl> result = testable.containValue("notexisting");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
        verify(resultCaller).checkForErrors();

    }

    @Test
    void testEqualsValueWhileItExpected() {
        PomElement element = new PomElement("project.version", "1.0.0");
        pomElements.add(element);

        testable = new DefaultAnalyzerImpl("project.version", pomElements, resultCaller, negateNext, errorMessages);

        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<DefaultAnalyzerImpl> result = testable.equalsValue("1.0.0");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
        verify(resultCaller).checkForErrors();
    }

    @Test
    void testEqualsValueWhileItIsNotExpected() {
        PomElement element = new PomElement("project.version", "1.0.0");
        pomElements.add(element);

        testable = new DefaultAnalyzerImpl("project.version", pomElements, resultCaller, negateNext, errorMessages);

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<DefaultAnalyzerImpl> result = testable.equalsValue("1.0.0");

        assertNotNull(result);
        assertFalse(errorMessages.isEmpty());
        verify(resultCaller).checkForErrors();
    }

    @Test
    void testEqualsValueWhileExpectedHasNot() {
        PomElement element = new PomElement("project.version", "1.0.0");
        pomElements.add(element);

        testable = new DefaultAnalyzerImpl("project.version", pomElements, resultCaller, negateNext, errorMessages);

        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<DefaultAnalyzerImpl> result = testable.equalsValue("2.0.0");

        assertNotNull(result);
        assertFalse(errorMessages.isEmpty());
        verify(resultCaller).checkForErrors();
    }

    @Test
    void testEqualsValueWhileItNotExpectedAndShouldNot() {
        PomElement element = new PomElement("project.version", "1.0.0");
        pomElements.add(element);

        testable = new DefaultAnalyzerImpl("project.version", pomElements, resultCaller, negateNext, errorMessages);

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<DefaultAnalyzerImpl> result = testable.equalsValue("2.0.0");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
        verify(resultCaller).checkForErrors();
    }

    @Test
    void testEqualsWhenPomElementIsNull() {
        PomElement element = new PomElement("project.version", null);
        pomElements.add(element);

        testable = new DefaultAnalyzerImpl("project.version", pomElements, resultCaller, negateNext, errorMessages);

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<DefaultAnalyzerImpl> result = testable.equalsValue("1.0.0");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
        verify(resultCaller).checkForErrors();
    }

    @Test
    void testHaveTagWhenArgumentIsNull() {

        Assertions.assertThrows(NullPointerException.class, () -> testable.haveTag(null));
    }

    @Test
    void testContainValueWhenArgumentIsNull() {

        Assertions.assertThrows(NullPointerException.class, () -> testable.containValue(null));

    }

    @Test
    void testEqualsValueWhenArgumentIsNull() {

        Assertions.assertThrows(NullPointerException.class, () -> testable.equalsValue(null));
    }
}

