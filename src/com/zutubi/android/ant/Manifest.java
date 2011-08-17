package com.zutubi.android.ant;

import de.pdark.decentxml.Attribute;
import de.pdark.decentxml.Document;
import de.pdark.decentxml.Element;
import de.pdark.decentxml.XMLIOSource;
import de.pdark.decentxml.XMLParser;
import de.pdark.decentxml.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A wrapper around an AndroidManifest.xml DOM tree that provides access to a
 * useful subset of its contents.
 */
public class Manifest {
	private static final String ELEMENT_MANIFEST = "manifest";
	private static final String ELEMENT_APPLICATION = "application";

    private static final String ATTRIBUTE_VERSION_NAME = "android:versionName";
    private static final String ATTRIBUTE_VERSION_CODE = "android:versionCode";
    private static final String ATTRIBUTE_DEBUGGABLE = "android:debuggable";

    private final Document document;
    private final Element manifestElement;
    private final Element applicationElement;

    /**
     * Creates a new manifest by parsing the given AndroidManifest.xml file.
     *
     * @param inputFile the manifest file
     * @throws ParseException on any parse error
     */
    public Manifest(final File inputFile) throws ParseException {
        document = readDocument(inputFile);
        manifestElement = document.getRootElement();
        if (manifestElement == null || !manifestElement.getName().equals(ELEMENT_MANIFEST)) {
            throw new ParseException("Unable to locate <"+ELEMENT_MANIFEST+"> element");
        }
        applicationElement = manifestElement.getChild(ELEMENT_APPLICATION);
        if (applicationElement == null) {
            throw new ParseException("Unable to locate <"+ELEMENT_APPLICATION+"> element");
        }  
    }

    private Document readDocument(final File file) throws ParseException {
        try {
            final XMLParser parser = new XMLParser();
            return parser.parse(new XMLIOSource(file));
        } catch (final Exception e) {
            throw new ParseException("Unable to parse manifest: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves the android:debuggable value.
     * (aapt executable only works with first application node)
     *
     * @return the application debuggable value
     */
    public String getDebuggable() {
        return applicationElement.getAttributeValue(ATTRIBUTE_DEBUGGABLE);
    }

    /**
     * Sets the android:debuggable, adding the attribute if it does not
     * already exist.
     *
     * @param debuggable the new application debuggable value
     */
    public void setDebuggable(final boolean debuggable) {
        setAttributeValue(applicationElement, ATTRIBUTE_DEBUGGABLE, String.valueOf(debuggable));
    }

    /**
     * Retrieves the android:versionCode.
     *
     * @return the version code
     */
    public String getVersionCode() {
        return manifestElement.getAttributeValue(ATTRIBUTE_VERSION_CODE);
    }

    /**
     * Sets the android:versionCode, adding the attribute if it does not
     * already exist.
     *
     * @param versionCode the new version code
     */
    public void setVersionCode(final String versionCode) {
        setAttributeValue(manifestElement, ATTRIBUTE_VERSION_CODE, versionCode);
    }

    /**
     * Retrieves the android:versionName.
     *
     * @return the version name
     */
    public String getVersionName() {
        return manifestElement.getAttributeValue(ATTRIBUTE_VERSION_NAME);
    }

    /**
     * Sets the android:versionName, adding the attribute if it does not
     * already exist.
     *
     * @param versionName the new version name
     */
    public void setVersionName(final String versionName) {
        setAttributeValue(manifestElement, ATTRIBUTE_VERSION_NAME, versionName);
    }

    private void setAttributeValue(final Element element, final String name, final String value) {
        final Attribute attribute = element.getAttribute(name);
        if (attribute == null) {
            element.addAttribute(new Attribute(name, value));
        } else {
            attribute.setAttributeValue(value);
        }
    }

    /**
     * Writes this manifest out to the given file.  As far as possible all
     * whitespace and formatting from the original file is preserved.
     *
     * @param file the file to write out to
     * @throws IOException if there is any error writing to the file
     */
    public void serialise(final File file) throws IOException {
        XMLWriter writer = null;
        try {
        	System.out.println("Updating manifest "+file.getAbsolutePath());
            writer = new XMLWriter(new FileWriter(file));
            document.toXML(writer);
            writer.close();
        } catch (final Exception e) {
            throw new IOException("Unable to write out manifest: " + e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
