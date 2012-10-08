
package com.zutubi.android.ant;

import org.apache.tools.ant.BuildException;

/**
 * A helper base for implementing tasks that update an Android manifest file.
 */
public abstract class AbstractManifestUpdateTask extends AbstractManifestTask {
    /**
     * To be implemented by subclasses. Makes the desired updates to the
     * manifest, which will then be saved by this base class.
     *
     * @param manifest the manifest to update
     */
    protected abstract void updateManifest(final Manifest manifest);

    @Override
    public void execute() throws BuildException {
        final Manifest manifest = parseManifest();
        updateManifest(manifest);
        saveManifest(manifest);
    }
}
