package nl.zerofiftyit.mdepunit.core.validation;

import nl.zerofiftyit.mdepunit.dsl.Inclusion;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class InitAnalyzerTest {

    @Test
    void testAnalyzePomWithDefaultPomFile() throws IOException {

        assertNotNull(InitAnalyzer.analyzePom());
    }

    @Test
    void testAnalyzePomWithGivenPomFile() throws IOException, URISyntaxException {

        String pomFilePath = Objects.requireNonNull(InitAnalyzerTest.class.getClassLoader()
                        .getResource("dist/pom.xml")).toURI().getPath();

        assertNotNull(InitAnalyzer.analyzePom(pomFilePath));
    }

    @Test
    void testCheckingForGivenNodeReturnsInclusion() throws IOException {

        Inclusion<PomAnalyzer> result = InitAnalyzer.analyzePom().checking("node");

        assertEquals(Inclusion.class, result.getClass());
    }
}
