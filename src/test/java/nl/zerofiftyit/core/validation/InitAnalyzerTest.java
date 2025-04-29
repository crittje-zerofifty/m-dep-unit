package nl.zerofiftyit.core.validation;

import nl.zerofiftyit.core.parse.PomReaderTest;
import nl.zerofiftyit.dsl.Inclusion;
import nl.zerofiftyit.model.NegateNext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class InitAnalyzerTest {

    private List<String> errorMessages;
    private NegateNext negateNext;

    @BeforeEach
    void setUp() {

        errorMessages = new ArrayList<>();
        negateNext = mock(NegateNext.class);

    }

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
