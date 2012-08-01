package com.zutubi.android.ant;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;

/**
 * A task to generate .properties files alongside jars, pointing to the
 * corresponding source jars where available.  This allows the ADT tools to
 * link the sources to the libraries in Eclipse.
 */
public class LibraryPropertiesTask extends Task {
    private String libsDir = "libs";
    private String jarPattern = "(.+)\\.jar";
    private String srcReplacement = "src/$1-sources.jar";
    private boolean failOnMissingSrc = false;

    /**
     * Sets the path of the directory where library jars are found.
     *
     * @param libsDir path, relative to the build base, of the directory
     *                including the library jars
     */
    public void setLibsDir(final String libsDir) {
        this.libsDir = libsDir;
    }

    /**
     * Sets the regular expression used to match jar files.  Any file within
     * libsDir with a name matching this pattern will be processed as a jar.
     * Typically the pattern includes capturing groups referenced in the
     * srcReplacement.
     *
     * @param jarPattern regular expression used to identify jar files and
     *                   capture name components for later use
     */
    public void setJarPattern(final String jarPattern) {
        this.jarPattern = jarPattern;
    }

    /**
     * Sets the replacement string used to generate the path of a source jar
     * from the name of the corresponding library jar.  Typically includes
     * references to groups captured by the jarPattern.
     *
     * @param srcReplacement replacement string used to generate the path of a
     *                       source jar based on the matched library jar name
     */
    public void setSrcReplacement(final String srcReplacement) {
        this.srcReplacement = srcReplacement;
    }

    /**
     * Sets a flag indicating if the build should be failed when a library
     * jar is found without a corresponding sources jar.
     *
     * @param failOnMissingSrc if true, fail the build when a library jar
     *                         without corresponding sources is found
     */
    public void setFailOnMissingSrc(final boolean failOnMissingSrc) {
        this.failOnMissingSrc = failOnMissingSrc;
    }

    @Override
    public void execute() throws BuildException {
        File libs = new File(getProject().getBaseDir(), libsDir);
        if (!libs.isDirectory()) {
            throw new BuildException("Libs directory '" + libsDir + "' does not exist");
        }

        final Pattern jarRegex = Pattern.compile(jarPattern);
        for (final File file : libs.listFiles()) {
            if (file.isFile()) {
                final Matcher matcher = jarRegex.matcher(file.getName());
                if (matcher.matches()) {
                    final String srcPath = matcher.replaceFirst(srcReplacement);
                    final File srcFile = new File(libs, srcPath);
                    if (srcFile.isFile()) {
                        createPropertiesLinking(file, srcPath);
                    } else if (failOnMissingSrc) {
                        throw new BuildException("No sources jar found at '" + srcPath + "' for library '" + file.getName() + "'.");
                    }
                }
            }
        }
    }

    private void createPropertiesLinking(final File jar, final String srcJarPath) {
        final File propertiesFile = new File(jar.getAbsolutePath() + ".properties");
        final java.util.Properties properties = new Properties();
        if (propertiesFile.isFile()) {
            FileReader reader = null;
            try {
                reader = new FileReader(propertiesFile);
                properties.load(reader);
            } catch (IOException e) {
                throw new BuildException("Could not read existing properties", e);
            } finally {
                FileUtils.close(reader);
            }
        }

        properties.put("src", srcJarPath);

        FileWriter writer = null;
        try {
            writer = new FileWriter(propertiesFile);
            properties.store(writer, null);
        } catch (final IOException e) {
            throw new BuildException("Could not write out properties", e);
        } finally {
            FileUtils.close(writer);
        }
    }
}
