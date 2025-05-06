package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.dsl.Statement;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import nl.zerofiftyit.mdepunit.model.PomElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ModuleAnalyzerTest {

    private static final String GIVEN_NODE = "modules";
    private static final String GIVEN_NODE_VALUE = "src";

    private ModuleAnalyzer testable;
    private List<PomElement> pomElements;
    private List<String> errorMessages;

    @Mock
    private ResultCaller resultCaller;
    @Mock
    private NegateNext negateNext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pomElements = new ArrayList<>();
        errorMessages = new ArrayList<>();
        testable = new ModuleAnalyzer(GIVEN_NODE, pomElements, resultCaller, negateNext, errorMessages);
    }

    @Test
    void testModuleExistsWhenShouldAndExists() {
        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<ModuleAnalyzer> result = testable.moduleExists(GIVEN_NODE_VALUE);

        assertTrue(errorMessages.isEmpty());
        assertNotNull(result);
    }

    @Test
    void testModuleExistsWhenShouldAndNotExists() {
        List<String> modules = List.of("test-module");
        PomElement moduleElement = new PomElement("modules.module", modules);
        pomElements.add(moduleElement);
        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<ModuleAnalyzer> result = testable.moduleExists("non-existing-module");

        assertFalse(errorMessages.isEmpty());
        assertEquals(1, errorMessages.size());
        assertTrue(errorMessages.get(0).contains("non-existing-module"));
        assertNotNull(result);
    }

    @Test
    void testAllRegisteredModulesExistWhenNoModules() {
        Statement<ModuleAnalyzer> result = testable.allRegisteredModulesExist();

        assertTrue(errorMessages.isEmpty());
        assertNotNull(result);
    }

    @Test
    void testAllRegisteredModulesExistWhenModulesExist() {
        List<String> modules = List.of("test-module");
        PomElement moduleElement = new PomElement("modules.module", modules);
        pomElements.add(moduleElement);
        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<ModuleAnalyzer> result = testable.allRegisteredModulesExist();

        assertTrue(errorMessages.isEmpty());
        assertNotNull(result);
    }

    @Test
    void testContainModuleNodeWhenExists() {
        List<String> modules = List.of("test-module");
        PomElement moduleElement = new PomElement("modules.module", modules);
        pomElements.add(moduleElement);

        Statement<ModuleAnalyzer> result = testable.containModuleNode("test-module");

        assertTrue(errorMessages.isEmpty());
        assertNotNull(result);
    }

    @Test
    void testContainModuleNodeWhenNotExists() {
        List<String> modules = List.of("test-module");
        PomElement moduleElement = new PomElement("modules.module", modules);
        pomElements.add(moduleElement);
        Statement<ModuleAnalyzer> result = testable.containModuleNode("non-existing-module");

        assertFalse(errorMessages.isEmpty());
        assertNotNull(result);
    }
}
