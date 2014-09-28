package hudson.plugins.analysis.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *  Tests the class {@link JavaPackageDetector}.
 */
public class JavaPackageDetectorTest {
    /** The classifier under test. */
    private final JavaPackageDetector classifier = new JavaPackageDetector();

    /**
     * Checks whether we could identify a java package name.
     *
     * @throws IOException
     *             in case of an error
     */
    @Test
    public void checkPackage() throws IOException {
        InputStream stream;
        stream = JavaPackageDetectorTest.class.getResourceAsStream("MavenJavaTest.txt");
        String packageName = classifier.detectPackageName(stream, "UTF-8");

        try {
            assertEquals("Wrong package name guessed.", "hudson.plugins.tasks.util", packageName);
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * Checks whether we do not detect a namespace in a text file.
     *
     * @throws IOException
     *             in case of an error
     */
    @Test
    public void checkEmptyPackageName() throws IOException {
        String fileName = "pom.xml";
        InputStream stream = JavaPackageDetectorTest.class.getResourceAsStream(fileName);

        try {
            assertEquals("Wrong namespace name guessed.", "-", classifier.detectPackageName(stream, "UTF-8"));
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * Checks whether we detect package names that use some different formatting.
     *
     * @throws IOException
     *             in case of an error
     */
    @Test
    public void checkComplicatedPackageName() throws IOException {
        String fileName = "complicated-package-declaration.txt";
        InputStream stream = JavaPackageDetectorTest.class.getResourceAsStream(fileName);

        try {
            assertEquals("Wrong namespace name guessed.", "hudson.plugins.findbugs.util",
                    classifier.detectPackageName(stream, "UTF-8"));
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * Checks whether we correctly accept C# files.
     */
    @Test
    public void testFileSuffix() {
        assertTrue("Does not accept a Java file.", classifier.accepts("Action.java"));
        assertFalse("Accepts a non-Java file.", classifier.accepts("Action.java.old"));
    }
}
