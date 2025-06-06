package nl.zerofiftyit.mdepunit.model;

/**
 * Defines the various dependency scopes that can be assigned to Maven dependencies.
 * Each scope determines the accessibility and visibility of a dependency during
 * the build lifecycle and runtime of an application.
 * <p>
 * The supported scopes are:
 * - COMPILE: The default scope, used for dependencies required during both compilation and runtime.
 * - TEST: Used for dependencies only required for testing purposes, not included in the final
 *   artifact.
 * - RUNTIME: Specifies dependencies required during execution but not for compilation.
 * - PROVIDED: Specifies dependencies required at compile time but expected to be provided at
 *   runtime by the runtime environment.
 * </p>
 * The {@code toString()} method returns the name of the scope in lowercase.
 */
public enum DependencyScope {
    /**
     * The default scope, used for dependencies required during both compilation and runtime.
     */
    COMPILE,

    /**
     * Used for dependencies only required for testing purposes, not included in the final artifact.
     */
    TEST,

    /**
     * Specifies dependencies required during execution but not for compilation.
     */
    RUNTIME,

    /**
     * Specifies dependencies required at compile time but expected to be provided at runtime.
     */
    PROVIDED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
