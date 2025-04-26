package nl.zerofiftyit.model;

/**
 * Represents a POM (Project Object Model) element with a specific path and value.
 * This class is immutable and designed to encapsulate a simple mapping between
 * a path string and its corresponding value within a POM structure.
 */
public final class PomElement {
    /**
     * The path of the POM element, representing its location in the POM structure.
     */
    private final String path;

    /**
     * The value associated with this POM element.
     */
    private final Object value;

    /**
     * Constructs a new PomElement with the specified path and value.
     *
     * @param path the path representing the location of this element in the POM structure
     * @param value the value associated with this element
     */
    public PomElement(final String path, final Object value) {
        this.path = path;
        this.value = value;
    }

    /**
     * Retrieves the path associated with this POM element.
     *
     * @return the path as a string
     */
    public String getPath() {
        return path;
    }

    /**
     * Retrieves the value associated with this POM element.
     *
     * @return the value assigned to this POM element, which can be any Object type.
     */
    public Object getValue() {
        return value;
    }
}
