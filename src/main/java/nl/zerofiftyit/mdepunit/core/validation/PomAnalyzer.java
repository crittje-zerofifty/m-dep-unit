package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.dsl.Statement;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import nl.zerofiftyit.mdepunit.model.PomElement;

import java.util.List;
import java.util.stream.Collectors;

public final class PomAnalyzer extends DefaultAnalyzerImpl {

    private List<PomElement> pomElements;
    private final ResultCaller resultCaller;
    private final NegateNext negateNext;
    private final List<String> errorMessages;

    /**
     * Constructs a new instance of the {@code PomAnalyzer} class, which extends the functionality
     * of {@code DefaultAnalyzer} with additional convenience methods for analyzing and validating.
     *
     * @param givenNode the initial node within the POM structure where analysis should begin
     * @param pomElements a set of {@code PomElement} objects representing POM nodes for analysis
     * @param resultCaller a {@code ResultCaller} instance used for validation and error management
     * @param negateNext a {@code NegateNext} utility class controlling conditional negation logic
     * @param errorMessages a set of error messages for recording validation errors during analysis
     */
    public PomAnalyzer(final String givenNode, final List<PomElement> pomElements,
                       final ResultCaller resultCaller, final NegateNext negateNext,
                       final List<String> errorMessages) {
        super(givenNode, pomElements, resultCaller, negateNext, errorMessages);
        this.pomElements = pomElements;
        this.resultCaller = resultCaller;
        this.negateNext = negateNext;
        this.errorMessages = errorMessages;
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
        List<PomElement> properties = pomElements.stream()
                .filter(element -> element.getPath().startsWith("properties"))
                .filter(element -> element.getPath().contains(propertyName))
                .collect(Collectors.toList());

        boolean hasProperty = !properties.isEmpty();

        boolean expectationMismatch = negateNext.isNegateNext() == hasProperty;

        if (expectationMismatch) {
            String template = negateNext.isNegateNext()
                ? "Property '%s' found where not allowed"
                : "Required property '%s' not found";
            errorMessages.add(String.format(template, propertyName));
        }

        resultCaller.checkForErrors();
        pomElements = properties;

        return new Statement<>(this, resultCaller, negateNext);
    }
}
