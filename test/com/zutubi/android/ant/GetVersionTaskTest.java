package com.zutubi.android.ant;

import org.apache.tools.ant.Project;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetVersionTaskTest extends ManifestTaskTestSupport {
    private GetVersionTask task = new GetVersionTask();

    @Override
    protected AbstractManifestTask getTask() {
        return task;
    }

    @Before
    public void setUp() {
        task.setProject(new Project());
    }

    @Test
    public void testPropertiesSet() {
        runTask();
        assertEquals("4", task.getProject().getProperty(task.getCodeproperty()));
        assertEquals("1.2.3", task.getProject().getProperty(task.getNameproperty()));
    }
}
