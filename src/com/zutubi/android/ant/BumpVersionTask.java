
package com.zutubi.android.ant;

import org.apache.tools.ant.BuildException;

/**
 * Ant task to increment the version in an AndroidManifest.xml file.
 */
public class BumpVersionTask extends AbstractManifestUpdateTask {
    private boolean bumpname = false;

    /**
     * Sets whether the last component of the android:versionName value is also
     * incremented.
     * 
     * @param bumpname if true, also increment the versionName, if false leave
     *            the versionName unchanged
     */
    public void setBumpname(final boolean bumpname) {
        this.bumpname = bumpname;
    }

    @Override
    protected void updateManifest(final Manifest manifest) {
        updateCode(manifest);

        if (bumpname) {
            updateName(manifest);
        }
    }

    private void updateCode(final Manifest manifest) {
        final String codeString = manifest.getVersionCode();
        try {
            final int code = Integer.parseInt(codeString);
            manifest.setVersionCode(Integer.toString(code + 1));
        } catch (final NumberFormatException e) {
            throw new BuildException("Invalid version code '" + codeString
                    + "': expected an integer");
        }
    };

    private void updateName(final Manifest manifest) {
        final String versionName = manifest.getVersionName();
        if (!Util.stringSet(versionName)) {
            throw new BuildException("Invalid version name '" + versionName + "': name is empty");
        }

        String prefix;
        String lastElement;
        final int lastDotIndex = versionName.lastIndexOf('.');
        if (lastDotIndex < 0) {
            prefix = "";
            lastElement = versionName;
        } else {
            prefix = versionName.substring(0, lastDotIndex + 1);
            lastElement = versionName.substring(lastDotIndex + 1);
        }

        if (Util.stringSet(lastElement)) {
            try {
                final int n = Integer.parseInt(lastElement);
                lastElement = Integer.toString(n + 1);
                manifest.setVersionName(prefix + lastElement);
            } catch (final NumberFormatException e) {
                throw new BuildException("Invalid version name '" + versionName
                        + "': last element is not an integer");
            }
        } else {
            throw new BuildException("Invalid version name '" + versionName
                    + "': last element is empty");
        }
    }
}
