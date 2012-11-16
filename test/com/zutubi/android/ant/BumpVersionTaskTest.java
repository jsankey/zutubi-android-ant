
package com.zutubi.android.ant;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.IOException;

public class BumpVersionTaskTest extends ManifestTaskTestSupport {
    private final BumpVersionTask task = new BumpVersionTask();

    @Override
    protected AbstractManifestUpdateTask getTask() {
        return task;
    }

    @Test
    public void testEmptyCode() {
        failureTest("Invalid version code '': expected an integer");
    }

    @Test
    public void testNonIntegerCode() {
        failureTest("Invalid version code 'ha': expected an integer");
    }

    @Test
    public void testEmptyName() {
        task.setBumpname(true);
        failureTest("Invalid version name '': name is empty");
    }

    @Test
    public void testNonIntegerName() {
        task.setBumpname(true);
        failureTest("Invalid version: element 'x' at index 0 is not an integer");
    }

    @Test
    public void testEmptyLastElementName() {
        task.setBumpname(true);
        failureTest("Invalid version: empty element at index 1");
    }

    @Test
    public void testNonIntegerLastElementName() {
        task.setBumpname(true);
        failureTest("Invalid version: element 'x' at index 1 is not an integer");
    }

    @Test
    public void testIgnoreInvalidName() throws ParseException, IOException {
        final Manifest manifest = runTaskAndParseResult();
        assertEquals("2", manifest.getVersionCode());
        assertEquals("invalid", manifest.getVersionName());
    }

    @Test
    public void testCodeAndName() throws ParseException, IOException {
        task.setBumpname(true);
        final Manifest manifest = runTaskAndParseResult();
        assertEquals("4", manifest.getVersionCode());
        assertEquals("1.1", manifest.getVersionName());
    }

    @Test
    public void testIgnoreName() throws ParseException, IOException {
        final Manifest manifest = runTaskAndParseResult();
        assertEquals("4", manifest.getVersionCode());
        assertEquals("1.0", manifest.getVersionName());
    }

    @Test
    public void testSimpleIntegerName() throws ParseException, IOException {
        task.setBumpname(true);
        final Manifest manifest = runTaskAndParseResult();
        assertEquals("11", manifest.getVersionCode());
        assertEquals("4", manifest.getVersionName());
    }
}
