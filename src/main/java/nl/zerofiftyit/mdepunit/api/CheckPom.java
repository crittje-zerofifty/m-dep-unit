package nl.zerofiftyit.mdepunit.api;

import nl.zerofiftyit.mdepunit.core.validation.ModuleAnalyzer;
import nl.zerofiftyit.mdepunit.dsl.Inclusion;
import nl.zerofiftyit.mdepunit.core.validation.PomAnalyzer;

/**
 * The {@code CheckPom} interface provides methods for analyzing and validating
 * specific aspects of a Maven POM file. It facilitates calling a generic function or convenience
 * methods to ensure the POM structure adheres to expected rules.
 */
public interface CheckPom {

    /**
     * Analyzes and validates the current context of a Maven POM file
     * using the {@code ParentPomAnalyzer}.
     *
     * @param givenNode the node to analyze within the POM,
     *                       typically a specific node or section (e.g., "dependencies").
     * @return an {@code Inclusion<PomAnalyzer>} object allowing
     *         further validation or analysis on the specified context.
     */
    Inclusion<PomAnalyzer> checking(String givenNode);

    /**
     * Provides an {@code Inclusion<ModuleAnalyzer>} instance for analyzing
     * and validating modules defined in a Maven POM file. This method enables
     * checking whether modules exist, ensuring they contain a `pom.xml` file,
     * and performing additional validations on the specified modules.
     *
     * @return an {@code Inclusion<ModuleAnalyzer>} instance, allowing for further
     *         validation, checks, or operations related to the defined modules.
     */
    Inclusion<ModuleAnalyzer> checkingModule();
}
