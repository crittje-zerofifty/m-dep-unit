package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.api.CheckPom;
import nl.zerofiftyit.mdepunit.core.parse.PomReader;
import nl.zerofiftyit.mdepunit.dsl.Inclusion;
import nl.zerofiftyit.mdepunit.model.NegateNext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The {@code PomAnalyzer} class provides functionality for analyzing and
 * validating Maven POM (Project Object Model) files. This class is designed
 * to read and process the structure of a POM file and identify potential
 * issues or discrepancies in its configuration.
 * <p>
 * It supports operations for extracting POM content and performing
 * validations as defined by the {@code CheckPom} interface. The class is
 * immutable and safe for concurrent use.
 * <p>
 * Instances of {@code PomAnalyzer} are created through static factory
 * methods, which allow analysis of default or custom POM file paths.
 *
 */
public final class InitAnalyzer implements CheckPom {

    private final Set<String> errorMessages;
    private final PomReader pomReader;
    private final NegateNext negateNext;

    private InitAnalyzer(final String pomFile) throws IOException {
        this.errorMessages = new HashSet<>();
        this.negateNext = new NegateNext();
        pomReader = new PomReader(pomFile);
    }

    /**
     * Analyzes the POM file located at the default path "pom.xml"
     * and returns an instance of PomAnalyzer to facilitate further checks
     * or validations.
     *
     * @return an instance of {@code PomAnalyzer} for analyzing and
     * validating the contents of the "pom.xml" file.
     * @throws IOException if the POM file cannot be read or accessed.
     */
    public static InitAnalyzer analyzePom() throws IOException {
        return new InitAnalyzer("pom.xml");
    }

    /**
     * Analyzes the specified POM file and returns an instance of
     * PomAnalyzer to facilitate further checks or validations on the POM
     * file's content.
     *
     * @param pomPath the path to the POM file to be analyzed
     * @return an instance of PomAnalyzer for analyzing and validating the
     * specified POM file
     * @throws IOException if the specified POM file cannot be read or accessed
     */
    public static InitAnalyzer analyzePom(final String pomPath)
            throws IOException {
        return new InitAnalyzer(pomPath);
    }

    /**
     * Analyzes the provided POM structure from the given node and returns an
     * {@code Inclusion} object containing a {@code PomAnalyzer} instance for
     * further validation or analysis. This is the most generic method for
     * analysis.
     *
     * @param givenNode the initial node within the POM structure where analysis should begin
     * @return an {@code Inclusion<PomAnalyzer>} instance that wraps a {@code PomAnalyzer}
     *         configured to analyze the specified POM node with the current state of
     *         negation and error handling logic
     */
    @Override
    public Inclusion<PomAnalyzer> checking(final String givenNode) {
        ResultCaller resultCaller = new ResultCaller(errorMessages);
        return new Inclusion<>(
                new PomAnalyzer(givenNode, pomReader.getAllElements(), resultCaller, negateNext,
                        errorMessages), negateNext
        );
    }
}
