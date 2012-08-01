package com.zutubi.android.ant;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class AddUsedPermissionTaskTest extends ManifestUpdateTaskTestSupport {
    private static final String PERMISSION = "android.permission.INTERNET";

    private final AddUsedPermissionTask task = new AddUsedPermissionTask();

    @Before
    public void setUp() {
        task.setPermission(PERMISSION);
    }

    @Override
    protected AbstractManifestUpdateTask getTask() {
        return task;
    }

    @Test
    public void testNoExisting() throws ParseException, IOException {
        Manifest manifest = runTaskAndParseResult();
        assertEquals(asList(PERMISSION), manifest.getUsedPermissions());
    }

    @Test
    public void testExistingNotMatching() throws ParseException, IOException {
        Manifest manifest = runTaskAndParseResult();
        assertEquals(asList("android.permission.DEVICE_POWER", PERMISSION), manifest.getUsedPermissions());
    }

    @Test
    public void testExistingAtEnd() throws ParseException, IOException {
        Manifest manifest = runTaskAndParseResult();
        assertEquals(asList("android.permission.DEVICE_POWER", PERMISSION), manifest.getUsedPermissions());
    }
}
