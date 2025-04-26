package nl.zerofiftyit.api;

import nl.zerofiftyit.dsl.Inclusion;
import nl.zerofiftyit.core.validation.PomAnalyzer;

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

    /*
     * Provides access to an {@code Inclusion<DependenciesDependencyAnalyzer>}
     * to perform checks and validations on the "dependencies.dependency"
     * elements within a Maven POM file. This method allows the caller
     * to perform specific dependency-related analyses and obtain results
     * or errors if any discrepancies exist. This method contains additional convenience methods.
     *
     * @return an {@code Inclusion<DependenciesDependencyAnalyzer>} that enables
     *         further dependency-related checks and validations.
     */
//    Inclusion<DependenciesDependencyAnalyzer> checkingDependenciesDependency();
}
