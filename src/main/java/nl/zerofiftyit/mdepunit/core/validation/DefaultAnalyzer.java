package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.dsl.Statement;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import nl.zerofiftyit.mdepunit.model.PomElement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultAnalyzer {

    private final String givenNode;
    private Set<PomElement> pomElements;
    private final ResultCaller resultCaller;
    private final NegateNext negateNext;
    private final Set<String> errorMessages;

    public DefaultAnalyzer(final String givenNode, final Set<PomElement> pomElements, final ResultCaller resultCaller,
                           final NegateNext negateNext, final Set<String> errorMessages) {
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
    public Statement<DefaultAnalyzer> haveTag(final String tagName) {
        Set<PomElement> elements = pomElements.stream()
                .filter(element -> element.getPath().startsWith(givenNode))
                .filter(element -> element.getPath().endsWith(tagName))
                .collect(Collectors.toSet());

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

        return new Statement<>(this, resultCaller, negateNext);
    }

    /**
     * Checks if the given value exists within the given node of the analyzed POM elements.
     * This method searches for the specified value in all elements under the given node.
     *
     * @param value The value to search for within the given node.
     * @return a {@code ResultCaller} for validating the result and handling errors.
     */
    public Statement<DefaultAnalyzer> containValue(final String value) {
        Set<PomElement> elements = pomElements.stream()
                .filter(element -> element.getPath().startsWith(givenNode))
                .filter(element -> {
                    Object elementValue = element.getValue();
                    return elementValue != null && elementValue.toString().contains(value);
                })
                .collect(Collectors.toSet());

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
}
