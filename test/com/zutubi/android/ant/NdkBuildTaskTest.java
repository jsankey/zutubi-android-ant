package com.zutubi.android.ant;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class NdkBuildTaskTest {
    private NdkBuildTask mNdkBuildTask;

    private File mFakeNdkDir;
    private File mFakeBuildScript;
    private File mFakeBuildCmd;

    @Before
    public void setUp() throws IOException {
        mNdkBuildTask = new NdkBuildTask();
        mNdkBuildTask.setProject(new Project());

        mFakeNdkDir = File.createTempFile(getClass().getName(), ".tmp");
        assertTrue(mFakeNdkDir.delete());
        assertTrue(mFakeNdkDir.mkdir());

        mFakeBuildScript = new File(mFakeNdkDir, "ndk-build");
        assertTrue(mFakeBuildScript.createNewFile());

        mFakeBuildCmd = new File(mFakeNdkDir, "ndk-build.cmd");
        assertTrue(mFakeBuildCmd.createNewFile());
    }

    @After
    public void tearDown() {
        assertTrue(mFakeBuildCmd.delete());
        assertTrue(mFakeBuildScript.delete());
        assertTrue(mFakeNdkDir.delete());
    }

    @Test
    public void testGetNdkBuildCommandNoNdkDirProperty() {
        assertEquals(NdkBuildTask.COMMAND_NDK_BUILD, mNdkBuildTask.getNdkBuildCommand());
    }

    @Test
    public void testGetNdkBuildCommandBadNdkDirProperty() {
        mNdkBuildTask.getProject().setProperty(Properties.PROPERTY_NDK_DIR, "/no/such/dir");
        assertEquals(NdkBuildTask.COMMAND_NDK_BUILD, mNdkBuildTask.getNdkBuildCommand());
    }

    @Test
    public void testGetNdkBuildCommandNdkDirProperty() {
        mNdkBuildTask.getProject().setProperty(Properties.PROPERTY_NDK_DIR, mFakeNdkDir.getAbsolutePath());
        File expectedCommand = Os.isFamily(Os.FAMILY_WINDOWS) ? mFakeBuildCmd : mFakeBuildScript;
        assertEquals(expectedCommand.getAbsolutePath(), mNdkBuildTask.getNdkBuildCommand());
    }
}
