package com.zutubi.android.ant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class SetVersionTaskTest {
    private static final String ORIGINAL_CODE = "1";
    private static final String ORIGINAL_NAME = "1.0";
    private static final String TEST_CODE = "8";
    private static final String TEST_NAME = "2.3";

    private static final String ORIGINAL_FILE = "original";

    private final SetVersionTask task = new SetVersionTask();
    private final FileUtils fileUtils = FileUtils.getFileUtils();

    @Test
    public void testManifestNull() {
        failureTest(null, "Manifest file name is empty");
    }

    @Test
    public void testManifestEmpty() {
        failureTest("", "Manifest file name is empty");
    }

    @Test
    public void testManifestDoesNotExist() {
        failureTest("badfilepath", "does not exist");
    }

    @Test
    public void testInvalidXml() {
        failureTest(copyInputFile(getMethodName()).getAbsolutePath(), "Unable to parse manifest");
    }

    @Test
    public void testNoManifestElement() {
        failureTest(copyInputFile(getMethodName()).getAbsolutePath(), "Unable to locate <manifest> element");
    }

    @Test
    public void testEmptyManifestElement() throws ParseException {
        task.setCode(TEST_CODE);
        task.setName(TEST_NAME);

        final Manifest manifest = runTaskAndParseResult(copyInputFile(getMethodName()));
        assertEquals(TEST_CODE, manifest.getVersionCode());
        assertEquals(TEST_NAME, manifest.getVersionName());
    }

    @Test
    public void testRewriteNeither() throws ParseException {
        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));
        assertEquals(ORIGINAL_CODE, manifest.getVersionCode());
        assertEquals(ORIGINAL_NAME, manifest.getVersionName());
    }

    @Test
    public void testRewriteName() throws ParseException {
        task.setName(TEST_NAME);

        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));
        assertEquals(ORIGINAL_CODE, manifest.getVersionCode());
        assertEquals(TEST_NAME, manifest.getVersionName());
    }

    @Test
    public void testRewriteCode() throws ParseException {
        task.setCode(TEST_CODE);

        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));
        assertEquals(TEST_CODE, manifest.getVersionCode());
        assertEquals(ORIGINAL_NAME, manifest.getVersionName());
    }

    @Test
    public void testRewriteNameAndCode() throws ParseException {
        task.setCode(TEST_CODE);
        task.setName(TEST_NAME);

        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));
        assertEquals(TEST_CODE, manifest.getVersionCode());
        assertEquals(TEST_NAME, manifest.getVersionName());
    }

    @Test
    public void testContentAndFormattingPreserved() throws IOException {
        final File originalFile = getInputFile(ORIGINAL_FILE);
        final File tempFile = copyToTempFile(originalFile);
        task.setManifestFile(tempFile.getAbsolutePath());
        task.execute();
        assertTrue(fileUtils.contentEquals(originalFile, tempFile));
    }

    private Manifest runTaskAndParseResult(final File file) throws ParseException {
        task.setManifestFile(file.getAbsolutePath());
        task.execute();
        return new Manifest(file);
    }

    private void failureTest(final String filePath, final String expectedError) {
        try {
            task.setManifestFile(filePath);
            task.execute();
            fail("Expected task to fail.");
        } catch (final BuildException e) {
            assertThat(e.getMessage(), containsString(expectedError));
        }
    }

    private String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    private File copyInputFile(final String name) {
        return copyToTempFile(getInputFile(name));
    }

    private File getInputFile(final String name) {
        final Class<? extends SetVersionTaskTest> clazz = getClass();

        final String resourceName = clazz.getSimpleName() + "." + name + ".xml";
        final URL resource = clazz.getResource(resourceName);
        if (resource == null) {
            fail("Required resource '" + resourceName + "' not found");
        }

        try {
            return new File(resource.toURI());
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private File copyToTempFile(final File inputFile) {
        final File tempFile = fileUtils.createTempFile(getClass().getName(), ".tmp", null, true, false);
        try {
            fileUtils.copyFile(inputFile, tempFile);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }
}
