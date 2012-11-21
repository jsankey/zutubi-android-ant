
package com.zutubi.android.ant;

import org.apache.tools.ant.BuildException;

import java.io.File;
import java.io.FileWriter;

/**
 * Ant task to dump the version details into a json file.  Suitable for use
 * with cwac-updater: https://github.com/commonsguy/cwac-updater.
 */
public class JsonVersionTask extends AbstractManifestTask {
    private File jsonfile;
    private String updateurl;

    /**
     * Sets the path of the JSON output file.
     *
     * @param jsonfile path of the output file
     */
    public void setJsonfile(final File jsonfile) {
        this.jsonfile = jsonfile;
    }

    /**
     * Sets the update URL to include in the JSON file.
     *
     * @param updateurl value to set updateURL to
     */
    public void setUpdateurl(final String updateurl) {
        this.updateurl = updateurl;
    }

    @Override
    public void execute() throws BuildException {
        if (jsonfile == null) {
            throw new BuildException("jsonfile is required");
        }

        if (!Util.stringSet(updateurl)) {
            throw new BuildException("updateurl is required");
        }

        final Manifest manifest = parseManifest();
        try {
            final FileWriter writer = new FileWriter(jsonfile);
            try {
                writer.write("{'versionCode':" + manifest.getVersionCode()
                          + ", 'updateURL':'" + updateurl + "'}");
            } finally {
                writer.close();
            }
        } catch (final Exception e) {
            throw new BuildException(e);
        }
    }
}
