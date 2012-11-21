
package com.zutubi.android.ant;

import org.apache.tools.ant.BuildException;

import java.io.File;
import java.io.FileWriter;

/**
 * Ant task to dump the version details into a json file.  Suitable for use
 * with cwac-updater: https://github.com/commonsguy/cwac-updater.
 */
public class JsonVersionTask extends AbstractManifestTask {
    private File jsonFile;
    private String updateUrl;

    /**
     * Sets the path of the JSON output file.
     *
     * @param jsonFile path of the output file
     */
    public void setJsonFile(final File jsonFile) {
        this.jsonFile = jsonFile;
    }

    /**
     * Sets the update URL to include in the JSON file.
     *
     * @param updateUrl value to set updateURL to
     */
    public void setUpdateUrl(final String updateUrl) {
        this.updateUrl = updateUrl;
    }

    @Override
    public void execute() throws BuildException {
        if (jsonFile == null) {
            throw new BuildException("jsonFile is required");
        }

        if (!Util.stringSet(updateUrl)) {
            throw new BuildException("updateUrl is required");
        }

        final Manifest manifest = parseManifest();
        try {
            final FileWriter writer = new FileWriter(jsonFile);
            try {
                writer.write("{'versionCode':" + manifest.getVersionCode()
                          + ", 'updateURL':'" + updateUrl + "'}");
            } finally {
                writer.close();
            }
        } catch (final Exception e) {
            throw new BuildException(e);
        }
    }
}
