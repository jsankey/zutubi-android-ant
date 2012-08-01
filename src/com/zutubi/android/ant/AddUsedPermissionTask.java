package com.zutubi.android.ant;

/**
 * Adds a uses-permission element with a given permission name to the manifest.
 */
public class AddUsedPermissionTask extends AbstractManifestUpdateTask {
    private String permission;

    /**
     * Sets the name of the permission to add.
     *
     * @param permission name of the permission to add
     */
    public void setPermission(final String permission) {
        this.permission = permission;
    }

    @Override
    protected void updateManifest(final Manifest manifest) {
        manifest.addUsedPermission(permission);
    }
}
