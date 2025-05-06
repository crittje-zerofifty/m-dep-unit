package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.dsl.Statement;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import nl.zerofiftyit.mdepunit.model.PomElement;

import java.io.File;
import java.util.List;

public final class ModuleAnalyzer extends DefaultAnalyzerImpl {

    private List<PomElement> pomElements;
    private final ResultCaller resultCaller;
    private final NegateNext negateNext;
    private final List<String> errorMessages;

    /**
     * Constructs a new instance of {@code ModuleAnalyzer} with the specified parameters.
     * This class is responsible for analyzing modules and their associated POM elements,
     * validating their existence and structure, and collecting error messages if any issues
     * are detected during the analysis process.
     *
     * @param givenNode the identifier or context node for this analyzer
     * @param pomElements a list of {@code PomElement} instances representing elements in a POM file
     * @param resultCaller an instance of {@code ResultCaller} for managing and validating errors
     * @param negateNext an instance of {@code NegateNext} specifying if the next
     *                   operation should negate its behavior
     * @param errorMessages a list to accumulate error messages during the analysis
     */
    public ModuleAnalyzer(final String givenNode, final List<PomElement> pomElements,
                          final ResultCaller resultCaller, final NegateNext negateNext,
                          final List<String> errorMessages) {
        super(givenNode, pomElements, resultCaller, negateNext, errorMessages);

        this.pomElements = pomElements;
        this.resultCaller = resultCaller;
        this.negateNext = negateNext;
        this.errorMessages = errorMessages;
    }

    /**
     * Validates if a module with the given name exists and contains a `pom.xml` file.
     * Adds an error message if the module is not a directory or does not include the `pom.xml`
     * file.
     * Uses the {@code ResultCaller} for error validation after the check.
     *<p>
     * For example,
     * <pre>
     * &lt;modules&gt;
     *     &lt;module&gt;my-module&lt;/module&gt;
     * &lt;/modules&gt;
     * </pre>
     * </p>
     * could be verified as follows:
     *
     * <p>
     * <pre>
     * {@code
     * pomAnalyzer()
     *   .checkingModules()
     *   .should()
     *   .containValue("my-module")
     *   .and()
     *   .moduleExists("my-module")
     *   .validate();
     * }
     * </pre>
     * </p>
     * @param moduleName the name of the module to check for existence and validity.
     * @return a {@code Statement<ModuleAnalyzer>} instance for further method chaining.
     */
    public Statement<ModuleAnalyzer> moduleExists(final String moduleName) {
        final boolean moduleFound =
                new File(moduleName).isDirectory() || new File(moduleName + "/pom.xml").exists();

        if (!negateNext.isNegateNext() && !moduleFound) {
            errorMessages.add(String.format("Module '%s' directory or pom file not found while "
                    + "expected", moduleName));
        } else if (negateNext.isNegateNext() && moduleFound) {
            errorMessages.add(String.format("Module '%s' directory or pom file found, but not "
                    + "expected", moduleName));
        }
        resultCaller.checkForErrors();

        return new Statement<>(this, resultCaller, negateNext);
    }

    /**
     * Validates the existence of all registered modules listed in the POM elements
     * and checks if they are properly structured with a `pom.xml` file.
     *
     * @return a {@code Statement<ModuleAnalyzer>} instance allowing for further method chaining
     * within the {@code ModuleAnalyzer} class.
     */
    public Statement<ModuleAnalyzer> allRegisteredModulesExist() {

        pomElements.stream()
                .filter(element -> element.getPath().equals("module"))
                .map(PomElement::getValue)
                .map(Object::toString)
                .forEach(this::moduleExists);

        resultCaller.checkForErrors(); // todo miss niet nodig want in module exists

        return new Statement<>(this, resultCaller, negateNext);
    }

    /**
     * Checks if the specified module node exists within the analyzed POM elements.
     *
     * @param moduleName the name of the module to check for existence within the module nodes.
     * @return a {@code Statement<ModuleAnalyzer>} instance, enabling further method chaining
     *         within the {@code ModuleAnalyzer} class.
     */
    public Statement<ModuleAnalyzer> containModuleNode(final String moduleName) {

        super.containValue(moduleName);
        pomElements = super.getPomElements();

        return new Statement<>(this, resultCaller, negateNext);
    }
}
