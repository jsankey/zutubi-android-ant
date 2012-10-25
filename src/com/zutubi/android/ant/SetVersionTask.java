
package com.zutubi.android.ant;

import org.apache.tools.ant.BuildException;

import java.util.List;

/**
 * Ant task to set a version in an AndroidManifest.xml file.
 */
public class SetVersionTask extends AbstractManifestUpdateTask {
    static final String CODE_AUTO = "auto";

    private String name = "";
    private String code = "";
    private int codemultiplier = 1000;

    /**
     * Sets the new android:versionName value.
     *
     * @param name the new version name, may be empty or null to leave the name
     *            unchanged
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the new android:versionCode value.
     *
     * @param code the new version code, may be empty or null to leave the code
     *            unchanged
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * Sets the multiplier to apply to each version component when generating
     * the android:versionCode from the android:versionName.
     *
     * @param codemultiplier the factor by which to multiply each version
     *                       element when generating version codes
     */
    public void setCodemultiplier(final int codemultiplier) {
        this.codemultiplier = codemultiplier;
    }

    @Override
    protected void updateManifest(final Manifest manifest) {
        if (Util.stringSet(name)) {
            manifest.setVersionName(name);
        }

        if (Util.stringSet(code)) {
            if (CODE_AUTO.equals(code)) {
                manifest.setVersionCode(generateVersionCode(manifest.getVersionName()));
            } else {
                manifest.setVersionCode(code);
            }
        }
    }

    private String generateVersionCode(final String versionName) {
        try {
            int code = 0;
            final Version version = new Version(versionName);
            final List<Integer> elements = version.getElements();
            if (elements.size() > 0) {
                final int lastIndex = elements.size() - 1;
                for (int i = 0; i < lastIndex; i++) {
                    code += elements.get(i);
                    code *= codemultiplier;
                }
                code += elements.get(lastIndex);
            }

            return Integer.toString(code);
        } catch (final IllegalArgumentException e) {
            throw new BuildException("Cannot generate version code from invalid name: " + e.getMessage(), e);
        }
    };
}
