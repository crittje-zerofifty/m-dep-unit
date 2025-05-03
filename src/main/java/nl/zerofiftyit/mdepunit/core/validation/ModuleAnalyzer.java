package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.dsl.Statement;
import nl.zerofiftyit.mdepunit.model.NegateNext;
import nl.zerofiftyit.mdepunit.model.PomElement;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleAnalyzer extends DefaultAnalyzer {

    private final String givenNode;
    private List<PomElement> pomElements;
    private final ResultCaller resultCaller;
    private final NegateNext negateNext;
    private final List<String> errorMessages;

    public ModuleAnalyzer(final String givenNode, final List<PomElement> pomElements, final ResultCaller resultCaller,
                          final NegateNext negateNext, final List<String> errorMessages) {
        super(givenNode, pomElements, resultCaller, negateNext, errorMessages);

        this.givenNode = givenNode;
        this.pomElements = pomElements;
        this.resultCaller = resultCaller;
        this.negateNext = negateNext;
        this.errorMessages = errorMessages;
    }

    /**
     * Validates if a module with the given name exists and contains a `pom.xml` file.
     * Adds an error message if the module is not a directory or does not include the `pom.xml` file.
     * Utilizes the {@code ResultCaller} for error validation after the check.
     *<p>
     * For example,
     * ```xml
     * <modules>
     *    <module>my-module</module>
     * </modules>
     * ```
     * could be verified as follows:
     * ```java
     * pomAnalyzer()
     *   .checkingModules()
     *   .should()
     *   .containValue("my-module")
     *   .and()
     *   .moduleExists("my-module")
     *   .validate();
     * ```
     *</p>
     * @param moduleName the name of the module to check for existence and validity.
     * @return a {@code Statement<ModuleAnalyzer>} instance for further method chaining.
     */
    public Statement<ModuleAnalyzer> moduleExists(final String moduleName) {
        final boolean moduleFound =
                new File(moduleName).isDirectory() || new File(moduleName + "/pom.xml").exists();

        if (!negateNext.isNegateNext() && !moduleFound) {
            errorMessages.add(String.format("Module '%s' directory or pom file not found while expected", moduleName));
        } else if (negateNext.isNegateNext() && moduleFound) {
            errorMessages.add(String.format("Module '%s' directory or pom file found, but not expected", moduleName));
        }

        resultCaller.checkForErrors();

        return new Statement<>(this, resultCaller, negateNext);
    }
    
    public Statement<ModuleAnalyzer> allRegisteredModulesExist() {

        pomElements.stream()
                .filter(element -> element.getPath().equals("module"))
                .map(PomElement::getValue)
                .map(Object::toString)
                .forEach(this::moduleExists);

        resultCaller.checkForErrors();// todo miss niet nodig want in module exists

        return new Statement<>(this, resultCaller, negateNext);
    }

    public Statement<ModuleAnalyzer> containModuleNode(final String moduleName) {

        super.containValue(moduleName);
        pomElements = super.getPomElements();

        return new Statement<>(this, resultCaller, negateNext);
    }
}
