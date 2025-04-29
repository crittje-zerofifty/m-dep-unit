package nl.zerofiftyit.core.parse;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.zerofiftyit.model.PomElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class for reading Maven POM (Project Object Model) files.
 * This class provides the ability to parse a given POM file into a structured
 * format and extract detailed information about its elements.
 */
public final class PomReader {

    private final Map<String, Object> pomData;

    /**
     * Constructs a new instance of the {@code PomReader} class and reads the
     * Maven POM file from the specified file path. The POM is parsed and stored
     * as a map structure for further analysis or processing.
     *
     * @param filePath the file path of the Maven POM file to be read and parsed
     * @throws IOException if the POM file cannot be read or is not accessible
     */
    public PomReader(final String filePath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        pomData = xmlMapper.readValue(new File(filePath), Map.class);
    }

    /**
     * Retrieves all elements from the parsed Maven POM file as a list of PomElement
     * objects. Each element represents a specific path-value pair within the structure
     * of the POM file.
     *
     * @return a list of PomElement objects representing all paths and their corresponding
     * values from the POM file. The list will be non-null and will include nested
     * elements if present in the POM file.
     */
    public List<PomElement> getAllElements() {
        List<PomElement> elements = new ArrayList<>();
        traverseMap("", pomData, elements);
        return elements;
    }

    private void traverseMap(final String path, final Object value,
                             final List<PomElement> elements) {
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            map.forEach((key, val) -> {
                String newPath = path.isEmpty() ? key : path + "." + key;
                elements.add(new PomElement(newPath, val));
                traverseMap(newPath, val, elements);
            });
        } else if (value instanceof List) {
            List<?> list = (List<?>) value;
            for (int i = 0; i < list.size(); i++) {
                String newPath = path + "[" + i + "]";
                elements.add(new PomElement(newPath, list.get(i)));
                traverseMap(newPath, list.get(i), elements);
            }
        }
    }
}
