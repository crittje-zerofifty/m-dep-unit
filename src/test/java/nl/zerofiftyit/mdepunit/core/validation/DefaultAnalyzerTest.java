package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.dsl.Statement;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import nl.zerofiftyit.mdepunit.model.PomElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultAnalyzerTest {

    private DefaultAnalyzer testable;
    private Set<PomElement> pomElements;
    private ResultCaller resultCaller;
    private NegateNext negateNext;
    private Set<String> errorMessages;

    @BeforeEach
    public void setup() {

        pomElements = new HashSet<>();
        resultCaller = mock(ResultCaller.class);
        negateNext = mock(NegateNext.class);
        errorMessages = new HashSet<>();

        testable = new DefaultAnalyzer("dependencies.dependency", pomElements, resultCaller, negateNext, errorMessages);
    }

    @AfterEach
    public void tearDown() {
        verify(resultCaller).checkForErrors();
    }

    @Test
    public void testShouldHaveTagAndHas() {

        List<Object> dependencies = new ArrayList<>();
        PomElement dependenciesElement = new PomElement("dependencies.dependency", dependencies);
        PomElement artifactElement = new PomElement("dependencies.dependency[0].artifactId", "artifact");
        PomElement versionElement = new PomElement("dependencies.dependency[0].version", "1.0.0");

        pomElements.add(dependenciesElement);
        pomElements.add(artifactElement);
        pomElements.add(versionElement);

        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<DefaultAnalyzer> result  = testable.haveTag("version");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
    }

    @Test
    public void testShouldNotHaveTagButHas() {

        List<Object> dependencies = new ArrayList<>();
        PomElement dependenciesElement = new PomElement("dependencies.dependency", dependencies);
        PomElement artifactElement = new PomElement("dependencies.dependency[0].artifactId", "artifact");
        PomElement versionElement = new PomElement("dependencies.dependency[0].version", "1.0.0");

        pomElements.add(dependenciesElement);
        pomElements.add(artifactElement);
        pomElements.add(versionElement);

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<DefaultAnalyzer> result = testable.haveTag("version");

        assertNotNull(result);
        assertEquals(2, errorMessages.size());
    }

    @Test
    public void testShouldHaveTagAndHasNot() {

        List<Object> dependencies = new ArrayList<>();
        PomElement dependenciesElement = new PomElement("dependencies.dependency", dependencies);
        PomElement artifactElement = new PomElement("dependencies.dependency[0].artifactId", "artifact");
        PomElement versionElement = new PomElement("dependencies.dependency[0].groupId", "groupId");

        pomElements.add(dependenciesElement);
        pomElements.add(artifactElement);
        pomElements.add(versionElement);

        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<DefaultAnalyzer> result  = testable.haveTag("version");

        assertNotNull(result);
        assertEquals(1, errorMessages.size());
    }

    @Test
    public void testShouldNotHaveTagAndHasNot() {

        List<Object> dependencies = new ArrayList<>();
        PomElement dependenciesElement = new PomElement("dependencies.dependency", dependencies);
        PomElement artifactElement = new PomElement("dependencies.dependency[0].artifactId", "artifact");
        PomElement versionElement = new PomElement("dependencies.dependency[0].groupId", "groupId");

        pomElements.add(dependenciesElement);
        pomElements.add(artifactElement);
        pomElements.add(versionElement);

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<DefaultAnalyzer> result = testable.haveTag("version");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
    }

    @Test
    public void testContainsValueWhileItExpected() {

        List<Object> modules = new ArrayList<>();
        PomElement mainModules = new PomElement("modules.module", modules);
        PomElement moduleCustomer = new PomElement("modules.module[0]", "customer");
        PomElement moduleProduct = new PomElement("modules.module[1]", "product");

        pomElements.add(mainModules);
        pomElements.add(moduleCustomer);
        pomElements.add(moduleProduct);

        testable = new DefaultAnalyzer("modules.module", pomElements, resultCaller, negateNext, errorMessages);

        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<DefaultAnalyzer> result = testable.containValue("customer");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
    }

    @Test
    public void testContainsValueWhileItNotExpected() {

        List<Object> modules = new ArrayList<>();
        PomElement mainModules = new PomElement("modules.module", modules);
        PomElement moduleCustomer = new PomElement("modules.module[0]", "customer");
        PomElement moduleProduct = new PomElement("modules.module[1]", "product");

        pomElements.add(mainModules);
        pomElements.add(moduleCustomer);
        pomElements.add(moduleProduct);

        testable = new DefaultAnalyzer("modules.module", pomElements, resultCaller, negateNext, errorMessages);

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<DefaultAnalyzer> result = testable.containValue("customer");

        assertNotNull(result);
        assertFalse(errorMessages.isEmpty());
    }

    @Test
    public void testContainsValueWhileExpectedHasNot() {

        List<Object> modules = new ArrayList<>();
        PomElement mainModules = new PomElement("modules.module", modules);
        PomElement moduleCustomer = new PomElement("modules.module[0]", "customer");
        PomElement moduleProduct = new PomElement("modules.module[1]", "product");

        pomElements.add(mainModules);
        pomElements.add(moduleCustomer);
        pomElements.add(moduleProduct);

        when(negateNext.isNegateNext()).thenReturn(false);

        testable = new DefaultAnalyzer("modules.module", pomElements, resultCaller, negateNext, errorMessages);

        Statement<DefaultAnalyzer> result = testable.containValue("notexisting");

        assertNotNull(result);
        assertFalse(errorMessages.isEmpty());
    }

    @Test
    public void testContainsValueWhileItNotExpectedAndShouldNot() {

        List<Object> modules = new ArrayList<>();
        PomElement mainModules = new PomElement("modules.module", modules);
        PomElement moduleCustomer = new PomElement("modules.module[0]", "customer");
        PomElement moduleProduct = new PomElement("modules.module[1]", "product");

        pomElements.add(mainModules);
        pomElements.add(moduleCustomer);
        pomElements.add(moduleProduct);

        when(negateNext.isNegateNext()).thenReturn(true);

        testable = new DefaultAnalyzer("modules.module", pomElements, resultCaller, negateNext, errorMessages);

        Statement<DefaultAnalyzer> result = testable.containValue("notexisting");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
        verify(resultCaller).checkForErrors();

    }

}
