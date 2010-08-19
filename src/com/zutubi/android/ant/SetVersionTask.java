
package com.zutubi.android.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.File;

/**
 * Ant task to set a version in an AndroidManifest.xml file.
 */
public class SetVersionTask extends Task {
    private String manifestFile = "AndroidManifest.xml";
    private String name = "";
    private String code = "";

    /**
     * Sets the path of the manifest file to update.
     *
     * @param manifestFile path of the manifest file to update
     */
    public void setManifestFile(final String manifestFile) {
        this.manifestFile = manifestFile;
    }

    /**
     * Sets the new android:versionName value.
     *
     * @param name the new version name, may be empty or null to leave the name
     *             unchanged
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the new android:versionCode value.
     *
     * @param code the new version code, may be empty or null to leave the code
     *             unchanged
     */
    public void setCode(final String code) {
        this.code = code;
    }

    @Override
    public void execute() throws BuildException {
        if (!stringSet(manifestFile)) {
            throw new BuildException("Manifest file name is empty");
        }

        final File file = new File(manifestFile);
        if (!file.exists()) {
            throw new BuildException("Manifest file '" + file + "' does not exist");
        }

        try {
            final Manifest manifest = new Manifest(file);
            updateVersion(manifest);
            manifest.serialise(file);
        } catch (final Exception e) {
            throw new BuildException(e);
        }
    }

    private void updateVersion(final Manifest manifest) {
        if (stringSet(name)) {
            manifest.setVersionName(name);
        }

        if (stringSet(code)) {
            manifest.setVersionCode(code);
        }
    };

    private boolean stringSet(final String s) {
        return s != null && s.length() > 0;
    }
}
