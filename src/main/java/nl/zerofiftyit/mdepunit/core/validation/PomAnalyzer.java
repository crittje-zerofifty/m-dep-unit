package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.dsl.Statement;
import nl.zerofiftyit.mdepunit.model.ExecutionMap;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import nl.zerofiftyit.mdepunit.model.PomElement;

import java.util.Set;
import java.util.stream.Collectors;

public final class PomAnalyzer extends DefaultAnalyzer {

    private final String givenNode;
    private Set<PomElement> pomElements;
    private final ResultCaller resultCaller;
    private final NegateNext negateNext;
    private final Set<String> errorMessages;

    /**
     * Constructs a new instance of the {@code PomAnalyzer} class, which extends the functionality
     * of {@code DefaultAnalyzer}. The {@code PomAnalyzer} is designed to analyze Maven POM
     * (Project Object Model) structures and validate plugin executions, phases, and goals
     * based on specific rules and conditions.
     *
     * @param givenNode the initial node within the POM structure where analysis should begin
     * @param pomElements a set of {@code PomElement} objects representing POM nodes for analysis
     * @param resultCaller a {@code ResultCaller} instance used for validation and error management
     * @param negateNext a {@code NegateNext} utility class controlling conditional negation logic
     * @param errorMessages a set of error messages for recording validation errors during analysis
     */
    public PomAnalyzer(final String givenNode, final Set<PomElement> pomElements,
                       final ResultCaller resultCaller, final NegateNext negateNext,
                       final Set<String> errorMessages) {
        super(givenNode, pomElements, resultCaller, negateNext, errorMessages);
        this.givenNode = givenNode;
        this.pomElements = pomElements;
        this.resultCaller = resultCaller;
        this.negateNext = negateNext;
        this.errorMessages = errorMessages;
    }

    /**
     * Checks if a specified plugin execution exists in the given Maven phase for a specific goal.
     * Based on the analyzer's expected state (should or should not), it validates the presence or
     * absence of the plugin execution and records an error message if the condition is violated.
     *
     * @param pluginArtifactId the artifact ID of the plugin to check for.
     * @param phase the Maven lifecycle phase to check within (e.g., "compile", "test").
     * @param goal the specific goal of the plugin to validate.
     * @return a {@code ResultCaller} instance allowing further validation and error handling
     *         for the analysis.
     */
    public Statement<PomAnalyzer> havePluginExecutionInPhaseForGoal(final String pluginArtifactId,
                                                       final String phase, final String goal) {
        Set<ExecutionMap> plugins = pomElements.stream()
                .filter(element -> element.getPath().startsWith(givenNode))
                .filter(element -> element.getPath().endsWith("executions.execution"))
                .map(PomElement::getValue)
                .map(ExecutionMap::new)
                .filter(e -> goal.equals(e.getGoal()) && phase.equals(e.getPhase()))
                .collect(Collectors.toSet());

        boolean hasPluginInPhase = !plugins.isEmpty();

        if (negateNext.isNegateNext() && hasPluginInPhase) {
            errorMessages.add(String.format(
                    "Plugin '%s' found in phase '%s' for goal '%s' where this is not permitted",
                    pluginArtifactId, phase, goal));
        } else if (!negateNext.isNegateNext() && !hasPluginInPhase) {
            errorMessages.add(String.format(
                    "Plugin '%s' not found in given phase '%s' for goal '%s'",
                    pluginArtifactId, phase, goal));
        }

        resultCaller.checkForErrors();

        return new Statement<>(this, resultCaller, negateNext);
    }

    /**
     * Checks whether the Maven POM contains a specific property.
     * Depending on the current state of `should` or `shouldNot`, it determines
     * if the property is expected to exist or not and records an error message if the condition
     * is violated.
     * <p>
     * Example usage:
     * XML:
     * {@code
     * &lt;properties&gt;
     *     &lt;java.version&gt;11&lt;/java.version&gt;
     * &lt;/properties&gt;
     * }
     *
     * Java:
     * {@code should().haveProperty("java.version")}
     * </p>
     *
     * @param propertyName the name of the property to check for in the POM.
     * @return the current instance of {@code ResultCaller}, allowing further method chaining.
     */
    public Statement<PomAnalyzer> haveProperty(final String propertyName) {
        Set<PomElement> properties = pomElements.stream()
                .filter(element -> element.getPath().startsWith("properties"))
                .filter(element -> element.getPath().contains(propertyName))
                .collect(Collectors.toSet());

        boolean hasProperty = !properties.isEmpty();

        if (negateNext.isNegateNext() && hasProperty) {
            errorMessages.add(String.format(
                    "Property '%s' found where not allowed", propertyName));
        } else if (!negateNext.isNegateNext() && !hasProperty) {
            errorMessages.add(String.format(
                    "Required property '%s' not found", propertyName));
        }

        resultCaller.checkForErrors();
        pomElements = properties;

        return new Statement<>(this, resultCaller, negateNext);
    }
}
