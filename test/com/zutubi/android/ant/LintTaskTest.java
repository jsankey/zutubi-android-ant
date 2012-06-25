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

public class LintTaskTest {
    private LintTask mLintTask;

    private File mFakeSdkDir;
    private File mFakeToolsDir;
    private File mFakeLint;
    private File mFakeLintBatch;

    @Before
    public void setUp() throws IOException {
        mLintTask = new LintTask();
        mLintTask.setProject(new Project());

        mFakeSdkDir = File.createTempFile(getClass().getName(), ".tmp");
        assertTrue(mFakeSdkDir.delete());
        assertTrue(mFakeSdkDir.mkdir());

        mFakeToolsDir = new File(mFakeSdkDir, "tools");
        assertTrue(mFakeToolsDir.mkdir());
        
        mFakeLint = new File(mFakeToolsDir, "lint");
        assertTrue(mFakeLint.createNewFile());

        mFakeLintBatch = new File(mFakeToolsDir, "lint.bat");
        assertTrue(mFakeLintBatch.createNewFile());
    }

    @After
    public void tearDown() {
        assertTrue(mFakeLintBatch.delete());
        assertTrue(mFakeLint.delete());
        assertTrue(mFakeToolsDir.delete());
        assertTrue(mFakeSdkDir.delete());
    }

    @Test
    public void testGetLintCommandNoSdkDirProperty() {
        assertEquals(LintTask.COMMAND_LINT, mLintTask.getLintCommand());
    }

    @Test
    public void testGetListCommandBadSdkDirProperty() {
        mLintTask.getProject().setProperty(Properties.PROPERTY_SDK_DIR, "/no/such/dir");
        assertEquals(LintTask.COMMAND_LINT, mLintTask.getLintCommand());
    }

    @Test
    public void testGetLintCommandSdkDirProperty() {
        mLintTask.getProject().setProperty(Properties.PROPERTY_SDK_DIR, mFakeSdkDir.getAbsolutePath());
        File expectedCommand = Os.isFamily(Os.FAMILY_WINDOWS) ? mFakeLintBatch : mFakeLint;
        assertEquals(expectedCommand.getAbsolutePath(), mLintTask.getLintCommand());
    }
}
