package nl.zerofiftyit.mdepunit.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a map of execution details for a Maven plugin.
 * This class extends LinkedHashMap to provide specific access methods for
 * execution phase and goals information.
 */
public final class ExecutionMap extends LinkedHashMap<String, Object> {

    /**
     * Constructs a new ExecutionMap from the provided value object.
     *
     * @param value the object containing execution information, expected to be a LinkedHashMap
     */
    public ExecutionMap(final Object value) {
        super((LinkedHashMap<String, Object>) value);
    }

    /**
     * Retrieves the phase associated with this execution.
     *
     * @return the phase name as a string
     */
    public String getPhase() {
        return (String) get("phase");
    }

    /**
     * Retrieves the goals map associated with this execution.
     *
     * @return a map of goal names to their values
     */
    private Map<String, String> getGoals() {
        return new LinkedHashMap<String, String>((LinkedHashMap) get("goals"));
    }

    /**
     * Retrieves the goal associated with this execution.
     *
     * @return the goal name as a string
     */
    public String getGoal() {
        final Map<String, String> goals = getGoals();
        return goals.get("goal");
    }

}
