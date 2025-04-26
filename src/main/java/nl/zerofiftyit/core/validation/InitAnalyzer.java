package nl.zerofiftyit.core.validation;

import nl.zerofiftyit.api.CheckPom;
import nl.zerofiftyit.core.parse.PomReader;
import nl.zerofiftyit.dsl.Inclusion;
import nl.zerofiftyit.model.NegateNext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private final List<String> errorMessages;
    private final PomReader pomReader;
    private final NegateNext negateNext;

    private InitAnalyzer(final String pomFile) throws IOException {
        this.errorMessages = new ArrayList<>();
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

    @Override
    public Inclusion<PomAnalyzer> checking(final String givenNode) {
        ResultCaller resultCaller = new ResultCaller(errorMessages);
        return new Inclusion<>(
                new PomAnalyzer(givenNode, pomReader.getAllElements(), resultCaller, negateNext,
                        errorMessages), negateNext
        );
    }
}
