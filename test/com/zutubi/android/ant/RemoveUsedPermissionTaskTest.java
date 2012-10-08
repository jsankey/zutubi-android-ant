package com.zutubi.android.ant;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class RemoveUsedPermissionTaskTest extends ManifestTaskTestSupport {
    private static final String FILE_ORIGINAL = "original";
    private static final List<String> ORIGINAL_PERMISSIONS = asList("android.permission.READ_EXTERNAL_STORAGE", "android.permission.INTERNET");

    private RemoveUsedPermissionTask task = new RemoveUsedPermissionTask();

    @Override
    protected AbstractManifestUpdateTask getTask() {
        return task;
    }

    @Test
    public void testPermissionNotPresent() throws ParseException {
        task.setPermission("nosuchpermission");
        final Manifest manifest = runTaskAndParseResult(copyInputFile(FILE_ORIGINAL));
        assertEquals(ORIGINAL_PERMISSIONS, manifest.getUsedPermissions());
    }

    @Test
    public void testRemoveFirst() throws ParseException, IOException {
        String permission = ORIGINAL_PERMISSIONS.get(0);
        task.setPermission(permission);
        final Manifest manifest = runTaskAndParseResult();
        assertEquals(filter(permission), manifest.getUsedPermissions());
    }

    @Test
    public void testRemoveLast() throws ParseException {
        String permission = ORIGINAL_PERMISSIONS.get(ORIGINAL_PERMISSIONS.size() - 1);
        task.setPermission(permission);
        final Manifest manifest = runTaskAndParseResult(copyInputFile(FILE_ORIGINAL));
        assertEquals(filter(permission), manifest.getUsedPermissions());
    }

    public List<String> filter(final String permission) {
        List<String> filtered = new LinkedList<String>(ORIGINAL_PERMISSIONS);
        while (filtered.remove(permission)) {
            // Empty
        }
        return filtered;
    }
}
