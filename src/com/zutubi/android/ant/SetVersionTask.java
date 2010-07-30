
package com.zutubi.android.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Ant task to set a version in an AndroidManifest.xml file.
 */
public class SetVersionTask extends Task {
    private static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";

    private static final String ATTRIBUTE_VERSION_NAME = "android:versionName";
    private static final String ATTRIBUTE_VERSION_CODE = "android:versionCode";

    private String manifestFile = "AndroidManifest.xml";
    private String name = "";
    private String code = "";

    /**
     * Sets the path of the manifest file to update.
     *
     * @param manifestFile path of the manifest file to update
     */
    public void setManifestFile(final String manifestFile) {
        this.manifestFile = manifestFile;
    }

    /**
     * Sets the new android:versionName value.
     *
     * @param name the new version name, may be empty or null to leave the name
     *             unchanged
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the new android:versionCode value.
     *
     * @param code the new version code, may be empty or null to leave the code
     *             unchanged
     */
    public void setCode(final String code) {
        this.code = code;
    }

    @Override
    public void execute() throws BuildException {
        if (!stringSet(manifestFile)) {
            throw new BuildException("Manifest file name is empty");
        }

        final File file = new File(manifestFile);
        if (!file.exists()) {
            throw new BuildException("Manifest file '" + file + "' does not exist");
        }

        final Document doc = readDocument(file);
        updateVersion(doc);
        writeDocument(doc, file);
    }

    private void updateVersion(final Document doc) {
        final NodeList elements = doc.getElementsByTagName("manifest");
        if (elements.getLength() == 0) {
            throw new BuildException("Unable to locate <manifest> element");
        }

        final Element manifestElement = (Element) elements.item(0);
        if (stringSet(name)) {
            manifestElement.setAttributeNS(ANDROID_NAMESPACE, ATTRIBUTE_VERSION_NAME, name);
        }

        if (stringSet(code)) {
            manifestElement.setAttributeNS(ANDROID_NAMESPACE, ATTRIBUTE_VERSION_CODE, code);
        }
    };

    private boolean stringSet(final String s) {
        return s != null && s.length() > 0;
    }

    private Document readDocument(final File file) {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(file);
        } catch (final Exception e) {
            throw new BuildException("Unable to parse manifest: " + e.getMessage(), e);
        }
    }

    private void writeDocument(final Document doc, final File file) {
        try {
            final Source source = new DOMSource(doc);
            final Result result = new StreamResult(file);
            final Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (final Exception e) {
            throw new BuildException("Unable to write out manifest: " + e.getMessage(), e);
        }
    }
}
