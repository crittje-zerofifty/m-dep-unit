package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.dsl.Statement;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import nl.zerofiftyit.mdepunit.model.PomElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PomAnalyzerTest {

    private PomAnalyzer testable;
    private Set<PomElement> propertyPomElements;
    private Set<String> errorMessages;
    private ResultCaller resultCaller;
    private NegateNext negateNext;

    @BeforeEach
    void setUp() {
        propertyPomElements = new HashSet<>();
        propertyPomElements.add(new PomElement("properties", new ArrayList<>()));
        propertyPomElements.add(new PomElement("properties.java.version", "11"));

        testable = new PomAnalyzer("properties",
                propertyPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new HashSet<>());
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
                errorMessages = new HashSet<>());

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
                errorMessages = new HashSet<>());

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
                errorMessages = new HashSet<>());

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
                errorMessages = new HashSet<>());

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<PomAnalyzer> result = testable.haveProperty("not.existing.version");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
    }

    @Test
    @Disabled
    void testHavePluginExecutionInPhaseAndGoalWhenShouldAndHas() {

        Set<PomElement> pluginPomElements = new HashSet<>();
        pluginPomElements.add(new PomElement("build.plugins.plugin[0].artifactId", "spotless-maven-plugin"));
        pluginPomElements.add(new PomElement("build.plugins.plugin[0].executions.execution.goals.goal", "check"));
        pluginPomElements.add(new PomElement("build.plugins.plugin[0].executions.execution.phase", "compile"));

        testable = new PomAnalyzer("build.plugins.plugin",
                pluginPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new HashSet<>());

        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<PomAnalyzer> result = testable
                .havePluginExecutionInPhaseForGoal("spotless-maven-plugin", "compile", "check");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
    }

    @Test
    @Disabled
    void testHavePluginExecutionInPhaseAndGoalWhenShouldAndHasNot() {
        LinkedHashMap<String, Object> goals = new LinkedHashMap<>();
        LinkedHashMap<String, Object> execution = new LinkedHashMap<>();
        execution.put("phase", "compile");
        execution.put("goals", goals);
        goals.put("goal", "check");

        Set<PomElement> pluginPomElements = new HashSet<>();
        pluginPomElements.add(new PomElement("build.plugins.plugin[0].artifactId", "spotless-maven-plugin"));
        pluginPomElements.add(new PomElement("build.plugins.plugin[0].executions.execution", execution));
        pluginPomElements.add(new PomElement("build.plugins.plugin.executions.execution.goals", goals));
        pluginPomElements.add(new PomElement("build.plugins.plugin[0].executions.execution.goals.goal", "check"));
        pluginPomElements.add(new PomElement("build.plugins.plugin[0].executions.execution.phase", "compile"));

        testable = new PomAnalyzer("build.plugins.plugin",
                pluginPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new HashSet<>());

        when(negateNext.isNegateNext()).thenReturn(false);

        Statement<PomAnalyzer> result = testable.havePluginExecutionInPhaseForGoal("BLA", "compile", "check");

        assertNotNull(result);
        assertEquals(1, errorMessages.size());
    }

    @Test
    @Disabled
    void testHavePluginExecutionInPhaseAndGoalWhenShouldNotAndHas() {
        Set<PomElement> pluginPomElements = new HashSet<>();
        pluginPomElements.add(new PomElement("build.plugins.plugin.groupId", "com.diffplug.spotless"));
        pluginPomElements.add(new PomElement("build.plugins.plugin.artifactId", "spotless-maven-plugin"));
        pluginPomElements.add(new PomElement("build.plugins.plugin.version", "${spotless.version}"));
        pluginPomElements.add(new PomElement("build.plugins.plugin.executions.execution.goals.goal", "check"));
        pluginPomElements.add(new PomElement("build.plugins.plugin.executions.execution.phase", "compile"));

        testable = new PomAnalyzer("build.plugins.plugin",
                pluginPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new HashSet<>());

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<PomAnalyzer> result = testable.havePluginExecutionInPhaseForGoal("test", "compile", "check");

        assertNotNull(result);
        assertEquals(1, errorMessages.size());
    }

    @Test
    @Disabled
    void testHavePluginExecutionInPhaseAndGoalWhenShouldNotAndHasNot() {
        Set<PomElement> pluginPomElements = new HashSet<>();
        pluginPomElements.add(new PomElement("build.plugins.plugin.groupId", "com.diffplug.spotless"));
        pluginPomElements.add(new PomElement("build.plugins.plugin.artifactId", "spotless-maven-plugin"));

        testable = new PomAnalyzer("build.plugins.plugin",
                pluginPomElements,
                resultCaller = mock(ResultCaller.class),
                negateNext = mock(NegateNext.class),
                errorMessages = new HashSet<>());

        when(negateNext.isNegateNext()).thenReturn(true);

        Statement<PomAnalyzer> result = testable.havePluginExecutionInPhaseForGoal("test", "compile", "check");

        assertNotNull(result);
        assertTrue(errorMessages.isEmpty());
    }
}
