package nl.zerofiftyit.mdepunit.core.validation;

import lombok.NonNull;
import nl.zerofiftyit.mdepunit.api.DefaultAnalyzer;
import nl.zerofiftyit.mdepunit.dsl.Statement;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import nl.zerofiftyit.mdepunit.model.PomElement;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DefaultAnalyzerImpl implements DefaultAnalyzer {

    private final String givenNode;
    private List<PomElement> pomElements;
    private final ResultCaller resultCaller;
    private final NegateNext negateNext;
    private final List<String> errorMessages;

    /**
     * Constructs an instance of DefaultAnalyzer with the given parameters.
     *
     * @param givenNode the name of the node to be analyzed, typically representing
     *                  a specific segment of a POM (Project Object Model) structure.
     * @param pomElements a list of POM elements to analyze, each containing a path
     *                    and its associated value within the POM structure.
     * @param resultCaller a utility class responsible for handling and validating
     *                     error states during the analysis process.
     * @param negateNext a utility flag indicating whether the next operation
     *                   should be negated during the analysis.
     * @param errorMessages a list to capture and store any error messages
     *                      encountered throughout the analysis.
     */
    public DefaultAnalyzerImpl(final String givenNode, final List<PomElement> pomElements,
                               final ResultCaller resultCaller, final NegateNext negateNext,
                               final List<String> errorMessages) {
        this.givenNode = givenNode;
        this.pomElements = pomElements;
        this.resultCaller = resultCaller;
        this.negateNext = negateNext;
        this.errorMessages = errorMessages;
    }

    /**
     * Checks if the given tag name exists within the given node of the analyzed POM elements.
     *
     * @param tagName The name of the tag to look for within the given node, e.g., "version" or
     *                "artifactId".
     * @return a {@code ResultCaller} for validating the result and handling errors.
     */
    @Override
    public Statement<DefaultAnalyzerImpl> haveTag(@NonNull final String tagName) {
        List<PomElement> elements = pomElements.stream()
                .filter(element -> element.getPath().startsWith(givenNode))
                .filter(element -> element.getPath().endsWith(tagName))
                .collect(Collectors.toList());

        boolean hasTag = !elements.isEmpty();

        if (negateNext.isNegateNext() && hasTag) {
            errorMessages.add(String.format(
                    "%s found in %s where it is not allowed. \nFound disallowed value: ",
                    tagName, givenNode));
            elements.forEach(element -> errorMessages.add(
                    "- " + element.getValue().toString()));
        } else if (!negateNext.isNegateNext() && !hasTag) {
            errorMessages.add(String.format(
                    "No %s found in %s where it is required", tagName, givenNode));
        }

        resultCaller.checkForErrors();
        pomElements = elements;

        return new Statement<>(this, resultCaller, negateNext);
    }

    /**
     * Checks if the given value exists within the given node of the analyzed POM elements.
     * This method searches for the specified value in all elements under the given node.
     *
     * @param value The value to search for within the given node.
     * @return a {@code ResultCaller} for validating the result and handling errors.
     */
    @Override
    public Statement<DefaultAnalyzerImpl> containValue(@NonNull final String value) {
        return filterValue(element -> {
            Object elementValue = element.getValue();
            return elementValue != null && elementValue.toString().contains(value);
        }, value);
    }

    @Override
    public final Statement<DefaultAnalyzerImpl> equalsValue(@NonNull final String value) {
        return filterValue(element -> {
            Object elementValue = element.getValue();
            return elementValue != null && elementValue.toString().contains(value);
        }, value);
    }

    private Statement<DefaultAnalyzerImpl> filterValue(final Predicate<PomElement> valuePredicate,
                                                       final String value) {

        List<PomElement> elements = pomElements.stream()
                .filter(element -> element.getPath().startsWith(givenNode))
                .filter(valuePredicate)
                .collect(Collectors.toList());

        boolean containsValue = !elements.isEmpty();

        if (negateNext.isNegateNext() && containsValue) {
            errorMessages.add(String.format(
                    "Value '%s' found in %s where it is not allowed", value, givenNode));

        } else if (!negateNext.isNegateNext() && !containsValue) {
            errorMessages.add(String.format(
                    "Value '%s' not found in %s where it is required", value, givenNode));
        }

        resultCaller.checkForErrors();
        pomElements = elements;

        return new Statement<>(this, resultCaller, negateNext);
    }

    protected final List<PomElement> getPomElements() {
        return pomElements;
    }
}
