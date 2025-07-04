package nl.zerofiftyit.mdepunit.core.parse;

import nl.zerofiftyit.mdepunit.model.PomElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PomReaderTest {

    private static final String SAMPLE_POM_PATH = "dist/pom.xml";
    private String pomFilePath;

    @BeforeEach
    void setUp() throws URISyntaxException {
        pomFilePath =
                Objects.requireNonNull(PomReaderTest.class.getClassLoader().getResource(SAMPLE_POM_PATH)).toURI().getPath();
    }

    @Test
    void testConstructorWithValidPomFileShouldNotThrowException() {
        assertDoesNotThrow(() -> new PomReader(pomFilePath),
                "Constructor should not throw an exception with a valid POM file");
    }

    @Test
    void testConstructorWithInvalidPomFileShouldThrowIOException() {
        assertThrows(IOException.class, () -> new PomReader("non-existent-pom.xml"),
                "Constructor should throw an IOException with an invalid POM file");
    }

    @Test
    void testConstructorWithCorruptedPomFileShouldThrowException(@TempDir Path tempDir) throws IOException {
        Path corruptedPomPath = tempDir.resolve("corrupted-pom.xml");
        Files.write(corruptedPomPath, "<project>This is not valid XML</project".getBytes());

        assertThrows(Exception.class, () -> new PomReader(corruptedPomPath.toString()),
                "Constructor should throw an exception with a corrupted POM file");
    }

    @Test
    void testGetAllElementsShouldReturnNonEmptySet() throws IOException {
        PomReader pomReader = new PomReader(pomFilePath);

        List<PomElement> elements = pomReader.getAllElements();

        assertNotNull(elements, "getAllElements() should not return null");
        assertFalse(elements.isEmpty(), "getAllElements() should not return an empty set");
    }

    @Test
    void testGetAllElementsShouldContainExpectedElements() throws IOException {
        PomReader pomReader = new PomReader(pomFilePath);

        List<PomElement> elements = pomReader.getAllElements();

        assertTrue(elements.stream().anyMatch(e -> e.getPath().equals("modelVersion")),
                "Elements should contain modelVersion");
        assertTrue(elements.stream().anyMatch(e -> e.getPath().endsWith("groupId")),
                "Elements should contain groupId");
        assertTrue(elements.stream().anyMatch(e -> e.getPath().endsWith("artifactId")),
                "Elements should contain artifactId");
        assertTrue(elements.stream().anyMatch(e -> e.getPath().endsWith("version")),
                "Elements should contain version");
    }

    @Test
    void testGetAllElementsShouldContainNestedElements() throws IOException {
        PomReader pomReader = new PomReader(pomFilePath);

        List<PomElement> elements = pomReader.getAllElements();

        assertTrue(elements.stream().anyMatch(e -> e.getPath().startsWith("parent")),
                "Elements should contain parent elements");
        assertTrue(elements.stream().anyMatch(e -> e.getPath().startsWith("dependencies")),
                "Elements should contain dependencies elements");
        assertTrue(elements.stream().anyMatch(e -> e.getPath().startsWith("properties")),
                "Elements should contain properties elements");
    }

    @Test
    void testGetAllElementsShouldContainElementsWithCorrectValues() throws IOException {
        // Setup
        PomReader pomReader = new PomReader(pomFilePath);

        // Execute
        List<PomElement> elements = pomReader.getAllElements();

        // Verify
        elements.stream()
                .filter(e -> e.getPath().equals("modelVersion"))
                .findFirst()
                .ifPresent(e -> assertEquals("4.0.0", e.getValue().toString(),
                        "modelVersion should be 4.0.0"));

        elements.stream()
                .filter(e -> e.getPath().equals("packaging"))
                .findFirst()
                .ifPresent(e -> assertEquals("pom", e.getValue().toString(),
                        "packaging should be pom"));
    }

    @Test
    void testGetAllElementsWithSimplePomShouldTraverseAllElements(@TempDir Path tempDir) throws IOException {
        // Setup
        Path simplePomPath = tempDir.resolve("simple-pom.xml");
        String simplePomContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project>\n" +
                "  <modelVersion>4.0.0</modelVersion>\n" +
                "  <groupId>test</groupId>\n" +
                "  <artifactId>test</artifactId>\n" +
                "  <version>1.0</version>\n" +
                "  <properties>\n" +
                "    <prop1>value1</prop1>\n" +
                "    <prop2>value2</prop2>\n" +
                "  </properties>\n" +
                "  <dependencies>\n" +
                "    <dependency>\n" +
                "      <groupId>test</groupId>\n" +
                "      <artifactId>dep1</artifactId>\n" +
                "      <version>1.0</version>\n" +
                "    </dependency>\n" +
                "  </dependencies>\n" +
                "</project>";
        Files.write(simplePomPath, simplePomContent.getBytes());

        PomReader pomReader = new PomReader(simplePomPath.toString());

        // Execute
        List<PomElement> elements = pomReader.getAllElements();

        // Verify
        assertNotNull(elements, "getAllElements() should not return null");
        assertTrue(elements.size() > 10, "getAllElements() should return multiple elements");

        // Check basic elements
        assertTrue(elements.stream().anyMatch(e -> e.getPath().equals("modelVersion") && "4.0.0".equals(e.getValue().toString())),
                "Elements should contain modelVersion with value 4.0.0");

        // Check properties
        assertTrue(elements.stream().anyMatch(e -> e.getPath().equals("properties.prop1") && "value1".equals(e.getValue().toString())),
                "Elements should contain properties.prop1 with value value1");
        assertTrue(elements.stream().anyMatch(e -> e.getPath().equals("properties.prop2") && "value2".equals(e.getValue().toString())),
                "Elements should contain properties.prop2 with value value2");

        // Check dependency
        assertTrue(elements.stream().anyMatch(e -> e.getPath().contains("dependencies.dependency") && e.getPath().endsWith("artifactId") && "dep1".equals(e.getValue().toString())),
                "Elements should contain dependency with artifactId dep1");
    }
}
