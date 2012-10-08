
package com.zutubi.android.ant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Support base class for implementing tests that update manifest files.
 */
public abstract class ManifestTaskTestSupport {
    protected final FileUtils fileUtils = FileUtils.getFileUtils();

    protected abstract AbstractManifestTask getTask();

    protected Manifest runTaskAndParseResult() throws ParseException, IOException {
        return runTaskAndParseResult(getTestMethodName());
    }

    protected Manifest runTaskAndParseResult(final String name) throws ParseException, IOException {
        File tempFile = copyInputFile(name);
        Manifest manifest = runTaskAndParseResult(tempFile);
        File expectedFile = getInputFile(name + ".expected", false);
        if (expectedFile != null) {
            String expectedContent = FileUtils.readFully(new FileReader(expectedFile));
            String gotContent = FileUtils.readFully(new FileReader(tempFile));
            assertEquals(expectedContent, gotContent);
        }
        return manifest;
    }

    protected Manifest runTaskAndParseResult(final File file) throws ParseException {
        runTask(file);
        return new Manifest(file);
    }

    protected void runTask() {
        runTask(copyInputFile(getTestMethodName()));
    }

    protected void runTask(final String name) {
        runTask(copyInputFile(name));
    }

    protected void runTask(final File file) {
        final AbstractManifestTask task = getTask();
        task.setManifestfile(file.getAbsolutePath());
        task.execute();
    }

    protected void failureTest(final String expectedError) {
        failureTest(copyInputFile(getTestMethodName()).getAbsolutePath(), expectedError);
    }

    protected void failureTest(final String filePath, final String expectedError) {
        try {
            final AbstractManifestTask task = getTask();
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
        return copyToTempFile(getInputFile(name, true));
    }

    protected File getInputFile(final String name, final boolean required) {
        final Class<?> clazz = getClass();

        final String resourceName = clazz.getSimpleName() + "." + name + ".xml";
        final URL resource = clazz.getResource(resourceName);
        if (resource == null) {
            if (required) {
                fail("Required resource '" + resourceName + "' not found");
            } else {
                return null;
            }
        }

        try {
            return new File(resource.toURI());
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected File copyToTempFile(final File inputFile) {
        final File tempFile = fileUtils.createTempFile(getClass().getName(), ".tmp", null, true,
                false);
        try {
            fileUtils.copyFile(inputFile, tempFile);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }
}
