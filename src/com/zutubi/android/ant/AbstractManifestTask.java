package com.zutubi.android.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.File;
import java.io.IOException;

/**
 * A helper base class for tasks that use an Android manifest file.
 */
public abstract class AbstractManifestTask extends Task {
    private String manifestfile = "AndroidManifest.xml";

    /**
     * @return the path of the manifest file to use
     */
    public String getManifestfile() {
        return manifestfile;
    }

    /**
     * Sets the path of the manifest file to use.
     *
     * @param manifestFile path of the manifest file to use
     */
    public void setManifestfile(final String manifestFile) {
        this.manifestfile = manifestFile;
    }

    /**
     * Parses and returns the manifest defined by manifestfile.
     *
     * @return the parsed manifest
     * @throws BuildException if manifestfile is not set or the file cannot be parsed
     */
    public Manifest parseManifest() throws BuildException {
        final File file = getFile();
        if (!file.exists()) {
            throw new BuildException("Manifest file '" + file.getAbsolutePath() + "' does not exist");
        }

        try {
            return new Manifest(file);
        } catch (final ParseException e) {
            throw new BuildException("Unable to parse manifest file '" + file.getAbsolutePath() + "': " + e.getMessage(), e);
        }
    }

    /**
     * Saves the given manifest to manifestfile.
     *
     * @param manifest the manifest to save
     * @throws BuildException if manifestfile is not set or there is an I/O error on save
     */
    public void saveManifest(final Manifest manifest) throws BuildException {
        final File file = getFile();
        try {
            manifest.serialise(file);
        } catch (final IOException e) {
            throw new BuildException("Unable to save manifest file '" + file.getAbsolutePath() + "': " + e.getMessage(), e);
        }
    }

    private File getFile() {
        final String manifestfile = getManifestfile();
        if (!Util.stringSet(manifestfile)) {
            throw new BuildException("Manifest file name is empty");
        }

        return new File(manifestfile);
    }
}
