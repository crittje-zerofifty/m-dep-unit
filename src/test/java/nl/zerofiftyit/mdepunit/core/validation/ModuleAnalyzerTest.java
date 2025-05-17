package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.dsl.Statement;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import nl.zerofiftyit.mdepunit.model.PomElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
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
    void testModuleExistsWhenShouldNotAndExists() {
        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<ModuleAnalyzer> result = testable.moduleExists(GIVEN_NODE_VALUE); // "src" exists

        assertFalse(errorMessages.isEmpty());
        assertEquals(1, errorMessages.size());
        assertTrue(errorMessages.get(0).contains(GIVEN_NODE_VALUE));
        assertTrue(errorMessages.get(0).contains("found, but not expected"));
        assertNotNull(result);
    }

    @Test
    void testModuleExistsWhenShouldNotAndNotExists() {
        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<ModuleAnalyzer> result = testable.moduleExists("non-existing-module");

        assertTrue(errorMessages.isEmpty()); // No error should be added
        assertNotNull(result);
    }

    @Test
    void testModuleExistsWhenDirectoryExistsButNoPomFile() {
        when(negateNext.isNegateNext()).thenReturn(false);
        File tempDir = new File("temp-test-dir");
        if (!tempDir.mkdir()) {
            throw new RuntimeException("Could not create temp dir");
        }

        try {
            Statement<ModuleAnalyzer> result = testable.moduleExists("temp-test-dir");

            assertTrue(errorMessages.isEmpty()); // No error should be added since directory exists
            assertNotNull(result);
        } finally {
            tempDir.delete();
        }
    }

    @Test
    void testModuleExistsWhenPomFileExistsButNoDirectory() {
        when(negateNext.isNegateNext()).thenReturn(false);
        // Create a temporary pom.xml file in a non-existent directory
        File tempDir = new File("temp-test-dir2");
        if (!tempDir.mkdir()) {
            throw new RuntimeException("Could not create temp dir");
        }
        File pomFile = new File(tempDir, "pom.xml");

        try {
            if (!pomFile.createNewFile()) {
                throw new RuntimeException("Could not create file ");
            }

            Statement<ModuleAnalyzer> result = testable.moduleExists("temp-test-dir2");

            assertTrue(errorMessages.isEmpty()); // No error should be added since pom.xml exists
            assertNotNull(result);
        } catch (IOException e) {
            fail("Failed to create test files: " + e.getMessage());
        } finally {
            pomFile.delete();
            tempDir.delete();
        }
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

    @Test
    void testContainModuleNodeWhenArgumentIsNull() {

        Assertions.assertThrows(NullPointerException.class, () -> testable.containModuleNode(null));
    }

    @Test
    void testContainModuleExistsWhenArgumentIsNull() {

        Assertions.assertThrows(NullPointerException.class, () -> testable.moduleExists(null));
    }
}
