package nl.zerofiftyit.mdepunit.model;

/**
 * Represents a utility class for managing a "negate next" boolean flag.
 * This flag is often used to dictate whether the next operation should
 * be negated. The class provides methods to toggle the state of the flag
 * and query its current value.
 * <p>
 * The state of this flag can be changed dynamically using the provided
 * {@code applyNegateNext} method. This functionality is commonly used in
 * scenarios where conditional logic needs to be applied based on whether
 * negation is enabled or disabled.
 * </p>
 * Instances of this class are designed for simple and stateless operations
 * around the "negate next" flag.
 */
@SuppressWarnings("LombokGetterMayBeUsed")
public final class NegateNext {

    private boolean negateNext;

    /**
     * Toggles the "negate next" flag, reversing its current state.
     * If the flag is currently set to {@code true}, invoking this method
     * will change it to {@code false}, and vice versa.
     *
     * This method is typically used as part of a sequence of operations
     * where subsequent behavior depends on whether a negation is applied.
     */
    public void applyNegateNext() {
        this.negateNext = !this.negateNext;
    }

    /**
     * Checks if the "negate next" flag is currently set.
     *
     * @return true if the "negate next" flag is set, false otherwise
     */
    public boolean isNegateNext() {
        return negateNext;
    }

    /**
     * Returns a string representation of the "NegateNext" object, including
     * the current state of the "negateNext" flag.
     *
     * @return a string in the format "NegateNext{negateNext=<state>}",
     *         where <state> is the current boolean value of the "negateNext" flag
     */
    @Override
    public String toString() {
        return "NegateNext{"
                + "negateNext="
                + negateNext
                + '}';
    }
}
