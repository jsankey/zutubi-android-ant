package com.zutubi.android.ant;

/**
 * Removes a uses-permission element with a given permission name from the
 * manifest, if present.
 */
public class RemoveUsedPermissionTask extends AbstractManifestUpdateTask {
    private String permission;

    /**
     * Sets the name of the permission to remove.
     *
     * @param permission name of the permission to remove
     */
    public void setPermission(final String permission) {
        this.permission = permission;
    }

    @Override
    protected void updateManifest(final Manifest manifest) {
        manifest.removeUsedPermission(permission);
    }
}
