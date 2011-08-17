package com.zutubi.android.ant;

/**
 * Ant task to set a version in an AndroidManifest.xml file.
 */
public class SetVersionTask extends AbstractManifestUpdateTask {
    private String name = "";
    private String code = "";
    private String debuggable = "";

    /**
     * Sets the new android:debuggable value.
     *
     * @param name the new debuggable value, may be empty or null to leave the
     *             debuggable value unchanged
     */
    public void setDebuggable(final String debuggable) {
        this.debuggable = debuggable;
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
    protected void updateManifest(final Manifest manifest) {
        if (Util.stringSet(name)) {
            manifest.setVersionName(name);
        }

        if (Util.stringSet(code)) {
            manifest.setVersionCode(code);
        }

        if (Util.stringSet(debuggable)) {
            manifest.setDebuggable(Boolean.valueOf(debuggable).booleanValue());
        }
    };
}
