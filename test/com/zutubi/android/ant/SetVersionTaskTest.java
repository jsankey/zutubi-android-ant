package com.zutubi.android.ant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class SetVersionTaskTest extends ManifestUpdateTaskTestSupport {
    private static final String ORIGINAL_CODE = "1";
    private static final String ORIGINAL_NAME = "1.0";
    private static final String ORIGINAL_DEBUGGABLE = "false";
    private static final String TEST_CODE = "8";
    private static final String TEST_NAME = "2.3";
    private static final String TEST_DEBUGGABLE = "true";

    private static final String ORIGINAL_FILE = "original";

    private final SetVersionTask task = new SetVersionTask();

    @Override
    protected AbstractManifestUpdateTask getTask() {
        return task;
    }

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
        failureTest("Unable to parse manifest");
    }

    @Test
    public void testNoManifestElement() {
        failureTest("Unable to locate <manifest> element");
    }

    @Test
    public void testEmptyManifestElement() throws ParseException {
        task.setCode(TEST_CODE);
        task.setName(TEST_NAME);
        task.setDebuggable(TEST_DEBUGGABLE);

        final Manifest manifest = runTaskAndParseResult();
        assertEquals(TEST_CODE, manifest.getVersionCode());
        assertEquals(TEST_NAME, manifest.getVersionName());
        assertEquals(TEST_DEBUGGABLE, manifest.getDebuggable());
    }

    @Test
    public void testRewriteNeither() throws ParseException {
        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));
        assertEquals(ORIGINAL_CODE, manifest.getVersionCode());
        assertEquals(ORIGINAL_NAME, manifest.getVersionName());
        assertEquals(ORIGINAL_DEBUGGABLE, manifest.getDebuggable());
    }

    @Test
    public void testRewriteName() throws ParseException {
        task.setName(TEST_NAME);

        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));
        assertEquals(ORIGINAL_CODE, manifest.getVersionCode());
        assertEquals(TEST_NAME, manifest.getVersionName());
        assertEquals(ORIGINAL_DEBUGGABLE, manifest.getDebuggable());
    }

    @Test
    public void testRewriteCode() throws ParseException {
        task.setCode(TEST_CODE);

        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));
        assertEquals(TEST_CODE, manifest.getVersionCode());
        assertEquals(ORIGINAL_NAME, manifest.getVersionName());
        assertEquals(ORIGINAL_DEBUGGABLE, manifest.getDebuggable());
    }

    @Test
    public void testRewriteDebuggable() throws ParseException {
        task.setDebuggable(TEST_DEBUGGABLE);

        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));
        assertEquals(ORIGINAL_CODE, manifest.getVersionCode());
        assertEquals(ORIGINAL_NAME, manifest.getVersionName());
        assertEquals(TEST_DEBUGGABLE, manifest.getDebuggable());
    }

    @Test
    public void testRewriteNameAndCodeAndDebuggable() throws ParseException {
        task.setCode(TEST_CODE);
        task.setName(TEST_NAME);
        task.setDebuggable(TEST_DEBUGGABLE);

        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));
        assertEquals(TEST_CODE, manifest.getVersionCode());
        assertEquals(TEST_NAME, manifest.getVersionName());
        assertEquals(TEST_DEBUGGABLE, manifest.getDebuggable());
    }

    @Test
    public void testContentAndFormattingPreserved() throws IOException {
        final File originalFile = getInputFile(ORIGINAL_FILE);
        final File tempFile = copyToTempFile(originalFile);
        task.setManifestfile(tempFile.getAbsolutePath());
        task.execute();
        assertTrue(fileUtils.contentEquals(originalFile, tempFile));
    }
}
