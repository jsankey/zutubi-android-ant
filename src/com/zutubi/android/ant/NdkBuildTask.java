
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
    public static final String PROPERTY_NDK_DIR = "ndk.dir";
    /**
     * Usual ndk-build command.
     */
    public static final String NDK_BUILD = "ndk-build";
    /**
     * Custom ndk-build command for Windows OS.
     */
    public static final String NDK_BUILD_WINDOWS = "ndk-build.cmd";
    /**
     * Name of the ndk-build command, which depends on the OS.
     */
    public static final String COMMAND_NDK_BUILD = Os.isFamily(Os.FAMILY_WINDOWS) ? NDK_BUILD_WINDOWS
            : NDK_BUILD;

    /**
     * Determines the ndk-build command.  Looks for a platform-specific file
     * within a directory defined by ndk.build.  If found, the absolute path to
     * the file is returned.  If not found the platform-specific name is
     * returned, and the NDK directory must be on the path for this task to
     * work.
     *
     * @return the most specific ndk-build command that can be found
     */
    String getNdkBuildCommand() {
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
            setExecutable(getNdkBuildCommand());
        }

        super.execute();
    }
}
