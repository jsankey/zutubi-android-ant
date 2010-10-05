
package com.zutubi.android.ant;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Support base class for implementing tests that update manifest files.
 */
public abstract class ManifestUpdateTaskTestSupport {
    protected final FileUtils fileUtils = FileUtils.getFileUtils();

    protected abstract AbstractManifestUpdateTask getTask();

    protected Manifest runTaskAndParseResult() throws ParseException {
        return runTaskAndParseResult(copyInputFile(getTestMethodName()));
    }

    protected Manifest runTaskAndParseResult(final File file) throws ParseException {
        final AbstractManifestUpdateTask task = getTask();
        task.setManifestfile(file.getAbsolutePath());
        task.execute();
        return new Manifest(file);
    }

    protected void failureTest(final String expectedError) {
        failureTest(copyInputFile(getTestMethodName()).getAbsolutePath(), expectedError);
    }

    protected void failureTest(final String filePath, final String expectedError) {
        try {
            final AbstractManifestUpdateTask task = getTask();
            task.setManifestfile(filePath);
            task.execute();
            fail("Expected task to fail.");
        } catch (final BuildException e) {
            assertThat(e.getMessage(), containsString(expectedError));
        }
    }

    protected String getTestMethodName() {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (final StackTraceElement element : stackTrace) {
            try {
                final Class<?> clazz = Class.forName(element.getClassName());
                final Method method = clazz.getMethod(element.getMethodName());
                if (method.getAnnotation(Test.class) != null) {
                    return method.getName();
                }
            } catch (final NoSuchMethodException e) {
                // Can happen when the method has params, just continue.
                continue;
            } catch (final Exception e) {
                fail(e.getMessage());
            }
        }

        fail("Could not locate test method name");
        return null;
    }

    protected File copyInputFile(final String name) {
        return copyToTempFile(getInputFile(name));
    }

    protected File getInputFile(final String name) {
        final Class<?> clazz = getClass();

        final String resourceName = clazz.getSimpleName() + "." + name + ".xml";
        final URL resource = clazz.getResource(resourceName);
        if (resource == null) {
            fail("Required resource '" + resourceName + "' not found");
        }

        try {
            return new File(resource.toURI());
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected File copyToTempFile(final File inputFile) {
        final File tempFile = fileUtils.createTempFile(getClass().getName(), ".tmp", null, true, false);
        try {
            fileUtils.copyFile(inputFile, tempFile);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }
}
