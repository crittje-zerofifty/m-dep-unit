package nl.zerofiftyit.core.validation;

import lombok.RequiredArgsConstructor;
import nl.zerofiftyit.model.NegateNext;
import nl.zerofiftyit.model.PomElement;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class DefaultAnalyzer {

    private final String givenNode;
    private final List<PomElement> allElements;
    private final ResultCaller resultCaller;
    private final NegateNext negateNext;
    private final List<String> errorMessages;

    /**
     * Checks if the given tag name exists within the given node of the analyzed POM elements.
     *
     * @param tagName The name of the tag to look for within the given node, e.g., "version" or
     *                "artifactId".
     * @return a {@code ResultCaller} for validating the result and handling errors.
     */
    public ResultCaller haveTag(final String tagName) {
        List<PomElement> elements = allElements.stream()
                .filter(element -> element.getPath().startsWith(givenNode))
                .filter(element -> element.getPath().endsWith(tagName))
                .collect(Collectors.toList());

        boolean hasTag = !elements.isEmpty();

        if (negateNext.isNegateNext() && hasTag) {
            errorMessages.add(String.format(
                    "Version found in %s where it is not allowed. \nFound disallowed value: ",
                    givenNode));
            elements.forEach(element -> errorMessages.add(
                    "- " + element.getValue().toString()));
        } else if (!negateNext.isNegateNext() && !hasTag) {
            errorMessages.add(String.format(
                    "No version found in %s where it is required", givenNode));
        }

        resultCaller.checkForErrors();

        return resultCaller;
    }

    /**
     * Checks if the given value exists within the given node of the analyzed POM elements.
     * This method searches for the specified value in all elements under the given node.
     *
     * @param value The value to search for within the given node.
     * @return a {@code ResultCaller} for validating the result and handling errors.
     */
    public ResultCaller containValue(final String value) {
        List<PomElement> elements = allElements.stream()
                .filter(element -> element.getPath().startsWith(givenNode))
                .filter(element -> {
                    Object elementValue = element.getValue();
                    return elementValue != null && elementValue.toString().contains(value);
                })
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

        return resultCaller;
    }
}
