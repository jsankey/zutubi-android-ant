
package com.zutubi.android.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.taskdefs.condition.Os;

/**
 * Ant task to run the android lint tool.  A custom ExecTask with location of
 * the lint command.
 */
public class LintTask extends ExecTask {
    /**
     * Usual lint command.
     */
    public static final String LINT = "lint";
    /**
     * Custom lint command for Windows OS.
     */
    public static final String LINT_WINDOWS = "lint.bat";
    /**
     * Name of the lint command, which depends on the OS.
     */
    public static final String COMMAND_LINT = Os.isFamily(Os.FAMILY_WINDOWS) ? LINT_WINDOWS
            : LINT;

    /**
     * Determines the lint command.  Looks for a platform-specific file
     * within the directory ${sdk.build}/tools.  If found, the absolute path to
     * the file is returned.  If not found the platform-specific name is
     * returned, and the SDK directory must be on the path for this task to
     * work.
     *
     * @return the most specific lint command that can be found
     */
    String getLintCommand() {
        String sdkDir = getProject().getProperty(Properties.PROPERTY_SDK_DIR);
        if (Util.stringSet(sdkDir)) {
            File toolsDir = new File(sdkDir, "tools");
            File lint = new File(toolsDir, COMMAND_LINT);
            if (lint.exists()) {
                return lint.getAbsolutePath();
            }
        }

        return COMMAND_LINT;
    }

    @Override
    public void execute() throws BuildException {
        if (!Util.stringSet(cmdl.getExecutable())) {
            setExecutable(getLintCommand());
        }

        super.execute();
    }
}
