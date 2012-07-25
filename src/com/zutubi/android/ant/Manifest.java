
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
import java.util.LinkedList;
import java.util.List;

/**
 * A wrapper around an AndroidManifest.xml DOM tree that provides access to a
 * useful subset of its contents.
 */
public class Manifest {
    private static final String ELEMENT_MANIFEST = "manifest";
    private static final String ELEMENT_USES_PERMISSION = "uses-permission";

    private static final String ATTRIBUTE_NAME = "android:name";
    private static final String ATTRIBUTE_VERSION_CODE = "android:versionCode";
    private static final String ATTRIBUTE_VERSION_NAME = "android:versionName";

    private final Document document;
    private final Element manifestElement;

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
            throw new ParseException("Unable to locate <manifest> element");
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
     * Retrieves the android:versionCode.
     *
     * @return the version code
     */
    public String getVersionCode() {
        return manifestElement.getAttributeValue(ATTRIBUTE_VERSION_CODE);
    }

    /**
     * Sets the android:versionCode, adding the attribute if it does not already
     * exist.
     *
     * @param versionCode the new version code
     */
    public void setVersionCode(final String versionCode) {
        setAttributeValue(ATTRIBUTE_VERSION_CODE, versionCode);
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
        setAttributeValue(ATTRIBUTE_VERSION_NAME, versionName);
    }

    /**
     * Returns the names of all permissions referenced from uses-permissions
     * elements, in the order they appear.
     *
     * @return the names of all used permissions
     */
    public List<String> getUsedPermissions() {
        List<String> result = new LinkedList<String>();
        for (Element child : manifestElement.getChildren(ELEMENT_USES_PERMISSION)) {
            String permission = child.getAttributeValue(ATTRIBUTE_NAME);
            if (permission != null) {
                result.add(permission);
            }
        }

        return result;
    }

    /**
     * Removes any uses-permissions elements with the given permission name
     * from the manifest.
     *
     * @param name name of the permission to remove, e.g.
     *             android.permission.WRITE_EXTERNAL_STORAGE
     */
    public void removeUsedPermission(final String name) {
        for (Element child : manifestElement.getChildren(ELEMENT_USES_PERMISSION)) {
            if (name.equals(child.getAttributeValue(ATTRIBUTE_NAME))) {
                manifestElement.removeNode(child);
            }
        }
    }

    private void setAttributeValue(final String name, final String value) {
        final Attribute attribute = manifestElement.getAttribute(name);
        if (attribute == null) {
            manifestElement.addAttribute(new Attribute(name, value));
        } else {
            attribute.setAttributeValue(value);
        }
    }

    /**
     * Writes this manifest out to the given file. As far as possible all
     * whitespace and formatting from the original file is preserved.
     *
     * @param file the file to write out to
     * @throws IOException if there is any error writing to the file
     */
    public void serialise(final File file) throws IOException {
        XMLWriter writer = null;
        try {
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
