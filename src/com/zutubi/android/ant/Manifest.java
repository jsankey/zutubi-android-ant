
package com.zutubi.android.ant;

import de.pdark.decentxml.Attribute;
import de.pdark.decentxml.Document;
import de.pdark.decentxml.Element;
import de.pdark.decentxml.Node;
import de.pdark.decentxml.Text;
import de.pdark.decentxml.XMLIOSource;
import de.pdark.decentxml.XMLParser;
import de.pdark.decentxml.XMLTokenizer.Type;
import de.pdark.decentxml.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        final List<String> result = new LinkedList<String>();
        for (final Element child : manifestElement.getChildren(ELEMENT_USES_PERMISSION)) {
            final String permission = child.getAttributeValue(ATTRIBUTE_NAME);
            if (permission != null) {
                result.add(permission);
            }
        }

        return result;
    }

    /**
     * Adds a uses-permissions element with the given permission name to the
     * manifest.  If the manifest already contains some uses-permission
     * elements, an attempt is made to add the new element just after those
     * elements with the same indent.
     *
     * @param name name of the permission to add, e.g.
     *             android.permission.WRITE_EXTERNAL_STORAGE
     */
    public void addUsedPermission(final String name) {
        // If we don't find existing elements, just add at the start (Android
        // docs list uses-permission first).  Also use a 4-space indent as a
        // default when we can't do better.
        int insertIndex = 0;
        String indentString = "\n    ";

        final List<Element> existingElements = manifestElement.getChildren(ELEMENT_USES_PERMISSION);
        if (!existingElements.isEmpty()) {
            final Element lastExistingElement = existingElements.get(existingElements.size() - 1);
            insertIndex = manifestElement.nodeIndexOf(lastExistingElement) + 1;

            final Text prefix = findPrefix(lastExistingElement);
            if (prefix != null) {
                indentString = prefix.getText();
                // Note a newline must exist for us to have a prefix.
                final int lastNewlineIndex = indentString.lastIndexOf('\n');
                indentString = indentString.substring(lastNewlineIndex);
            }
        }

        final Element elementToAdd = new Element(ELEMENT_USES_PERMISSION);
        elementToAdd.addAttribute(new Attribute(ATTRIBUTE_NAME, name));
        manifestElement.addNodes(insertIndex, new Text(indentString), elementToAdd);
    }

    /**
     * Removes any uses-permissions elements with the given permission name
     * from the manifest.
     *
     * @param name name of the permission to remove, e.g.
     *             android.permission.WRITE_EXTERNAL_STORAGE
     */
    public void removeUsedPermission(final String name) {
        final List<Element> elementsToRemove = new LinkedList<Element>();
        for (final Element child : manifestElement.getChildren(ELEMENT_USES_PERMISSION)) {
            if (name.equals(child.getAttributeValue(ATTRIBUTE_NAME))) {
                elementsToRemove.add(child);
            }
        }

        for (final Element element : elementsToRemove) {
            removeElementAndPrefix(element);
        }
    }

    /**
     * Removes an element, with an attempt to also remove the newline and
     * indent that precede it.  Only predictable prefixes are removed, as it
     * is not critical to removing the element.
     *
     * @param element the element to be removed
     */
    private void removeElementAndPrefix(final Element element) {
        final Element parentElement = element.getParentElement();
        final Text prefix = findPrefix(element);
        if (prefix != null) {
            final String newText = prefix.getText().replaceFirst("(?s)\\n[ \\t]*$", "");
            if (newText.length() > 0) {
                prefix.setText(newText);
            } else {
                parentElement.removeNode(prefix);
            }
        }

        parentElement.removeNode(element);
    }

    private Text findPrefix(final Element element) {
        // We only have the limited ability to detect a prefix if it is within
        // a single text node - it might be nice to generalise to cases where
        // there are multiple text nodes but I'm not sure if they occur with
        // this parser.
        Text prefix = null;
        for (final Node node : element.getParentElement().getNodes()) {
            if (node == element) {
                break;
            }

            // Look for text nodes where the text ends with a newline followed
            // by a whitespace indent.  (?s) enables single line mode - so dot
            // will match newlines and $ only matches the end of the string.
            if (node.getType() == Type.TEXT && ((Text) node).getText().matches("(?s).*\\n[ \\t]*$")) {
                prefix = (Text) node;
            } else {
                prefix = null;
            }
        }

        return prefix;
    }

    /**
     * @return a mapping from attribute name to attribute for all attributes
     *         of the manifest element
     */
    public Map<String, Attribute> getAttributes() {
        return manifestElement.getAttributeMap();
    }

    /**
     * Sets the value of a given attribute, adding it if it does not already
     * exist.
     *
     * @param name the name of the attribute to set
     * @param value the new attribute value
     */
    public void setAttributeValue(final String name, final String value) {
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
