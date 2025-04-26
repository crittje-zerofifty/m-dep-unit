package nl.zerofiftyit.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DependencyScopeTest {

    @Test
    void valueOfScope_ValidInput_ReturnsCorrectScope() {
        assertEquals(DependencyScope.COMPILE, DependencyScope.valueOf("COMPILE"));
        assertEquals(DependencyScope.TEST, DependencyScope.valueOf("TEST"));
        assertEquals(DependencyScope.PROVIDED, DependencyScope.valueOf("PROVIDED"));
        assertEquals(DependencyScope.RUNTIME, DependencyScope.valueOf("RUNTIME"));
    }

    @Test
    void toString_ReturnsCorrectString() {
        assertEquals("compile", DependencyScope.COMPILE.toString());
        assertEquals("test", DependencyScope.TEST.toString());
        assertEquals("provided", DependencyScope.PROVIDED.toString());
        assertEquals("runtime", DependencyScope.RUNTIME.toString());
    }

}
