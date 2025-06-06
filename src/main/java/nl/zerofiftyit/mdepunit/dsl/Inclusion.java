package nl.zerofiftyit.mdepunit.dsl;

import nl.zerofiftyit.mdepunit.model.NegateNext;

/**
 * Represents an inclusion rule with optional negation logic.
 * The class provides methods for defining an action with the ability
 * to toggle whether the next operation should be negated.
 *
 * @param <T> the type of the value associated with the inclusion rule
 */
public final class Inclusion<T> {

    private final NegateNext negateNext;
    private final T value;

    /**
     * Constructs an Inclusion instance with the specified value and negation logic.
     *
     * @param value the value associated with this inclusion rule
     * @param negateNext the NegateNext instance for managing negation logic
     */
    public Inclusion(final T value, final NegateNext negateNext) {
        this.value = value;
        this.negateNext = negateNext;
    }

    /**
     * Returns the value associated with this inclusion rule.
     *
     * @return the value of type {@code T} attached to this inclusion rule
     */
    public T should() {
        return value;
    }

    /**
     * Applies negation logic to the subsequent operation and returns the
     * associated value.
     *
     * @return the value of type {@code T} attached to this inclusion rule
     */
    public T shouldNot() {
        negateNext.applyNegateNext();
        return value;
    }
}
