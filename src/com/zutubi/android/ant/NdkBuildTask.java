
package com.zutubi.android.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.taskdefs.condition.Os;

/**
 * Ant task to run ndk-build. Just a simple exec task with custom executable
 * location.
 */
public class NdkBuildTask extends ExecTask {
    /**
     * Name of the Android Ant build property that points to the NDK install
     * directory.
     */
    private static final String PROPERTY_NDK_DIR = "ndk.dir";
    /**
     * Name of the ndk-build command, which depends on the OS.
     */
    private static final String COMMAND_NDK_BUILD = Os.isFamily(Os.FAMILY_WINDOWS) ? "ndk-build.cmd"
            : "ndk-build";

    protected String getNdkCommand() {
        String ndkDir = getProject().getProperty(PROPERTY_NDK_DIR);
        if (Util.stringSet(ndkDir)) {
            File ndkBuild = new File(ndkDir, COMMAND_NDK_BUILD);
            if (ndkBuild.exists()) {
                return ndkBuild.getAbsolutePath();
            }
        }

        return COMMAND_NDK_BUILD;
    }

    @Override
    public void execute() throws BuildException {
        if (!Util.stringSet(cmdl.getExecutable())) {
            setExecutable(getNdkCommand());
        }

        super.execute();
    }
}
