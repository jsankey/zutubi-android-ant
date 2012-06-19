
package com.zutubi.android.ant;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BumpVersionTaskTest extends ManifestUpdateTaskTestSupport {
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
        failureTest("Invalid version name 'x': last element is not an integer");
    }

    @Test
    public void testEmptyLastElementName() {
        task.setBumpname(true);
        failureTest("Invalid version name '1.': last element is empty");
    }

    @Test
    public void testNonIntegerLastElementName() {
        task.setBumpname(true);
        failureTest("Invalid version name '1.x': last element is not an integer");
    }

    @Test
    public void testIgnoreInvalidName() throws ParseException {
        final Manifest manifest = runTaskAndParseResult();
        assertEquals("2", manifest.getVersionCode());
        assertEquals("invalid", manifest.getVersionName());
    }

    @Test
    public void testCodeAndName() throws ParseException {
        task.setBumpname(true);
        final Manifest manifest = runTaskAndParseResult();
        assertEquals("4", manifest.getVersionCode());
        assertEquals("1.1", manifest.getVersionName());
    }

    @Test
    public void testIgnoreName() throws ParseException {
        final Manifest manifest = runTaskAndParseResult();
        assertEquals("4", manifest.getVersionCode());
        assertEquals("1.0", manifest.getVersionName());
    }

    @Test
    public void testSimpleIntegerName() throws ParseException {
        task.setBumpname(true);
        final Manifest manifest = runTaskAndParseResult();
        assertEquals("11", manifest.getVersionCode());
        assertEquals("4", manifest.getVersionName());
    }

    @Test
    public void testOnlyLastElementIsIntegerName() throws ParseException {
        task.setBumpname(true);
        final Manifest manifest = runTaskAndParseResult();
        assertEquals("1", manifest.getVersionCode());
        assertEquals("x.2", manifest.getVersionName());
    }

    @Test
    public void testEmptyElementsName() throws ParseException {
        task.setBumpname(true);
        final Manifest manifest = runTaskAndParseResult();
        assertEquals("43", manifest.getVersionCode());
        assertEquals(".1..2", manifest.getVersionName());
    }
}
