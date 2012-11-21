package com.zutubi.android.ant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JsonVersionTaskTest extends ManifestTaskTestSupport {
    private static final String TEST_URL = "http://example.com/update";

    private final JsonVersionTask task = new JsonVersionTask();

    @Override
    protected AbstractManifestTask getTask() {
        return task;
    }

    @Test
    public void testNoJsonFile() {
        task.setUpdateUrl(TEST_URL);
        try {
            task.execute();
            fail("Should required a jsonFile");
        } catch (final BuildException e) {
            assertThat(e.getMessage(), containsString("jsonFile is required"));
        }
    }

    @Test
    public void testNoUpdateUrl() {
        task.setJsonFile(new File("test.json"));
        try {
            task.execute();
            fail("Should required an updateURL");
        } catch (final BuildException e) {
            assertThat(e.getMessage(), containsString("updateUrl is required"));
        }
    }

    @Test
    public void testJsonOutput() throws IOException {
        final File jsonFile = fileUtils.createTempFile(getTestMethodName(), ".json", null, true, false);
        task.setUpdateUrl(TEST_URL);
        task.setJsonFile(jsonFile);
        runTask();
        final String got = FileUtils.readFully(new FileReader(jsonFile));
        assertEquals("{'versionCode':3, 'updateURL':'http://example.com/update'}", got);
    }
}
