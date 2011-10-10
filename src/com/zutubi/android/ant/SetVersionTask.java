package com.zutubi.android.ant;

/**
 * Ant task to set a version in an AndroidManifest.xml file.
 */
public class SetVersionTask extends AbstractManifestUpdateTask {
    private String name = "";
    private String code = "";
    private String debuggable = "";
    private String packageValue = "";
    private String icon = "";
    private String label = "";

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

    /**
     * Sets the new package attribute value.
     *
     * @param packageValue the new package attribute value
     */
    public void setPackage(final String packageValue) {
        this.packageValue = packageValue;
    }
    

    /**
     * Sets the new label attribute value.
     *
     * @param code the new label attribute value
     */
    public void setLabel(final String label) {
        this.label = label;
    }
    
    /**
     * Sets the new icon attribute value.
     *
     * @param code the new icon attribute value
     */
    public void setIcon(final String icon) {
        this.icon = icon;
    }

    @Override
    protected void updateManifest(final Manifest manifest) {
        if (Util.stringSet(name)) {
            manifest.setVersionName(name);
        }
        if (Util.stringSet(code)) {
            manifest.setVersionCode(code);
        }
        if (Util.stringSet(packageValue)) {
            manifest.setPackage(packageValue);
        }
        if (Util.stringSet(debuggable)) {
            manifest.setDebuggable(Boolean.valueOf(debuggable).booleanValue());
        }
        if (Util.stringSet(label)) {
            manifest.setLabel(label);
        }
        if (Util.stringSet(icon)) {
            manifest.setIcon(icon);
        }
    };
}
