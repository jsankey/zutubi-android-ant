package com.zutubi.android.ant;

import org.apache.tools.ant.BuildException;
import org.junit.Test;

public class SetVersionTaskTest {
    private final SetVersionTask task = new SetVersionTask();

    @Test(expected = BuildException.class)
    public void testManifestNull() {
        task.setManifestFile(null);
        task.execute();
    }

    @Test(expected = BuildException.class)
    public void testManifestEmpty() {
        task.setManifestFile("");
        task.execute();
    }
}
