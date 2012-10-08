package com.zutubi.android.ant;

import org.apache.tools.ant.BuildException;

/**
 * A task to capture the versionCode and versionName from an Android manifest
 * into Ant properties.
 */
public class GetVersionTask extends AbstractManifestTask {
    private String codeproperty = "versionCode";
    private String nameproperty = "versionName";

    /**
     * @return the name of the property the versionCode will be captured to
     */
    public String getCodeproperty() {
        return codeproperty;
    }

    /**
     * Sets the name of the property to capture the versionCode value into.
     *
     * @param codeproperty name of the property to use
     */
    public void setCodeproperty(final String codeproperty) {
        this.codeproperty = codeproperty;
    }

    /**
     * @return the name of the property the versionName will be captured to
     */
    public String getNameproperty() {
        return nameproperty;
    }

    /**
     * Sets the name of the property to capture the versionName value into.
     *
     * @param nameproperty name of the property to use
     */
    public void setNameproperty(final String nameproperty) {
        this.nameproperty = nameproperty;
    }

    @Override
    public void execute() throws BuildException {
        final Manifest manifest = parseManifest();
        getProject().setProperty(codeproperty, manifest.getVersionCode());
        getProject().setProperty(nameproperty, manifest.getVersionName());
    }
}
