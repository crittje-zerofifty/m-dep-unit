package nl.zerofiftyit.mdepunit.api;

import nl.zerofiftyit.mdepunit.dsl.Statement;

public interface DefaultAnalyzer {

    /**
     * Checks if the specified tag name exists within the corresponding node of the
     * analyzed POM elements.
     * This method searches for a tag in the structure of the POM elements
     * and determines whether it matches the provided tag name.
     *
     * @param tagName The name of the tag to search for, such as "version" or "artifactId".
     * @return a {@code Statement<?>} instance enabling further validation or operations based on
     *         the result of this check.
     */
    Statement<?> haveTag(String tagName);

    /**
     * Checks whether the specified value is present within the given node of the
     * analyzed POM elements.
     * Searches for the specified value in all elements under the given node of the structure.
     *
     * @param value The value to search for within the analyzed node.
     * @return a {@code Statement<?>} instance allowing for further validations or operations
     *         based on the result of this check.
     */
    Statement<?> containValue(String value);

    /**
     * Checks whether the specified value exactly matches the value present in the corresponding
     * node of the analyzed POM elements. This method performs a match comparison between
     * the given value and the element's value.
     *
     * @param value The value to be matched against the value of the analyzed node.
     * @return a {@code Statement<?>} instance allowing for further validations or operations
     *         based on the result of this check.
     */
    Statement<?> equalsValue(String value);


}
