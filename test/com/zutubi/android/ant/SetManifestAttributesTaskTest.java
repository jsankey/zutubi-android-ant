package com.zutubi.android.ant;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;
import de.pdark.decentxml.Attribute;

import org.apache.tools.ant.BuildException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SetManifestAttributesTaskTest extends ManifestTaskTestSupport {
    private static final String ORIGINAL_FILE = "original";

    private static final Map<String, Attribute> ORIGINAL_ATTRIBUTES = new HashMap<String, Attribute>();
    static {
        ORIGINAL_ATTRIBUTES.put("xmlns:android", new Attribute("xmlns:android", "http://schemas.android.com/apk/res/android"));
        ORIGINAL_ATTRIBUTES.put("package", new Attribute("package", "com.zutubi.android.example"));
        ORIGINAL_ATTRIBUTES.put("android:versionCode", new Attribute("android:versionCode", "1"));
        ORIGINAL_ATTRIBUTES.put("android:versionName", new Attribute("android:versionName", "1.0"));
    }

    private SetManifestAttributesTask task = new SetManifestAttributesTask();

    @Override
    protected SetManifestAttributesTask getTask() {
        return task;
    }

    @Test
    public void testNoName() {
        final SetManifestAttributesTask.Attribute attribute = new SetManifestAttributesTask.Attribute(null, null);
        invalidAttributeTest(attribute, "Attribute name must be specified and not empty");
    }

    @Test
    public void testEmptyName() {
        final SetManifestAttributesTask.Attribute attribute = new SetManifestAttributesTask.Attribute("", null);
        invalidAttributeTest(attribute, "Attribute name must be specified and not empty");
    }

    @Test
    public void testNoVersion() {
        final SetManifestAttributesTask.Attribute attribute = new SetManifestAttributesTask.Attribute("a", null);
        invalidAttributeTest(attribute, "Attribute value must be specified");
    }

    private void invalidAttributeTest(final SetManifestAttributesTask.Attribute attribute, final String expectedError) {
        try {
            task.addConfiguredAttribute(attribute);
            fail("Expected attribute to be rejected");
        } catch (final BuildException e) {
            assertThat(e.getMessage(), containsString(expectedError));
        }
    }

    @Test
    public void testNoChanges() throws ParseException {
        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));
        assertAttributes(ORIGINAL_ATTRIBUTES, manifest.getAttributes());
    }

    @Test
    public void testSetExisting() throws ParseException {
        task.addConfiguredAttribute(new SetManifestAttributesTask.Attribute("package", "updated"));

        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));

        final Map<String, Attribute> expected = new HashMap<String, Attribute>(ORIGINAL_ATTRIBUTES);
        expected.put("package", new Attribute("package", "updated"));
        assertAttributes(expected, manifest.getAttributes());
    }

    @Test
    public void testSetNew() throws ParseException {
        task.addConfiguredAttribute(new SetManifestAttributesTask.Attribute("newatt", "newval"));

        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));

        final Map<String, Attribute> expected = new HashMap<String, Attribute>(ORIGINAL_ATTRIBUTES);
        expected.put("newatt", new Attribute("newatt", "newval"));
        assertAttributes(expected, manifest.getAttributes());
    }

    @Test
    public void testSetMultiple() throws ParseException {
        task.addConfiguredAttribute(new SetManifestAttributesTask.Attribute("a1", "v1"));
        task.addConfiguredAttribute(new SetManifestAttributesTask.Attribute("a2", "v2"));

        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));

        final Map<String, Attribute> expected = new HashMap<String, Attribute>(ORIGINAL_ATTRIBUTES);
        expected.put("a1", new Attribute("a1", "v1"));
        expected.put("a2", new Attribute("a2", "v2"));
        assertAttributes(expected, manifest.getAttributes());
    }

    @Test
    public void testSetEmptyValue() throws ParseException {
        task.addConfiguredAttribute(new SetManifestAttributesTask.Attribute("package", ""));

        final Manifest manifest = runTaskAndParseResult(copyInputFile(ORIGINAL_FILE));

        final Map<String, Attribute> expected = new HashMap<String, Attribute>(ORIGINAL_ATTRIBUTES);
        expected.put("package", new Attribute("package", ""));
        assertAttributes(expected, manifest.getAttributes());
    }


    private void assertAttributes(final Map<String, Attribute> expected,
            final Map<String, Attribute> got) {
        assertEquals(expected.size(), got.size());
        assertEquals(expected.keySet(), got.keySet());
        for (final String key : expected.keySet()) {
            final Attribute e = expected.get(key);
            final Attribute g = got.get(key);
            assertEquals(e.getName(), g.getName());
            assertEquals(e.getValue(), g.getValue());
        }
    }
}
