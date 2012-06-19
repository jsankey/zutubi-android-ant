
package com.zutubi.android.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * A helper base for implementing tasks that update an Android manifest file.
 */
public abstract class AbstractManifestUpdateTask extends Task {
    private String manifestfile = "AndroidManifest.xml";

    /**
     * To be implemented by subclasses. Makes the desired updates to the
     * manifest, which will then be saved by this base class.
     *
     * @param manifest the manifest to update
     */
    protected abstract void updateManifest(final Manifest manifest);

    /**
     * Sets the path of the manifest file to update.
     *
     * @param manifestFile path of the manifest file to update
     */
    public void setManifestfile(final String manifestFile) {
        this.manifestfile = manifestFile;
    }

    @Override
    public void execute() throws BuildException {
        if (!Util.stringSet(manifestfile)) {
            throw new BuildException("Manifest file name is empty");
        }

        final File file = new File(manifestfile);
        if (!file.exists()) {
            throw new BuildException("Manifest file '" + file + "' does not exist");
        }

        try {
            final Manifest manifest = new Manifest(file);
            updateManifest(manifest);
            manifest.serialise(file);
        } catch (final Exception e) {
            throw new BuildException(e);
        }
    }
}
