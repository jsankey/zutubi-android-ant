package com.zutubi.android.ant;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Ant task to set a version in an AndroidManifest.xml file.
 */
public class SetVersionTask extends Task
{
    private static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
    private static final String ATTRIBUTE_VERSION_NAME = "android:versionName";
    private static final String ATTRIBUTE_VERSION_CODE = "android:versionCode";
   
    private String manifestFile = "AndroidManifest.xml";
    private String name = "";
    private String code = "";
    
    public void setManifestFile(String manifestFile)
    {
        this.manifestFile = manifestFile;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public void execute() throws org.apache.tools.ant.BuildException
    {
        File file = new File(manifestFile);
        if (!file.exists())
        {
            throw new BuildException("Manifest file '" + file + "' does not exist");
        }
        
        Document doc = readDocument(file);
        updateVersion(doc);
        writeDocument(doc, file);
    }

    private void updateVersion(Document doc)
    {
        NodeList elements = doc.getElementsByTagName("manifest");
        if (elements.getLength() == 0)
        {
            throw new BuildException("Unable to locate <manifest> element");
        }
        
        Element manifestElement = (Element) elements.item(0);
        manifestElement.setAttributeNS(ANDROID_NAMESPACE, ATTRIBUTE_VERSION_NAME, name);
        manifestElement.setAttributeNS(ANDROID_NAMESPACE, ATTRIBUTE_VERSION_CODE, code);
    };
    
    private Document readDocument(File file)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(file);
        }
        catch (Exception e)
        {
            throw new BuildException("Unable to parse manifest: " + e.getMessage(), e);
        }
    }
    
    private void writeDocument(Document doc, File file)
    {
        try
        {
            Source source = new DOMSource(doc);
            Result result = new StreamResult(file);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        }
        catch (Exception e)
        {
            throw new BuildException("Unable to write out manifest: " + e.getMessage(), e);
        }
    }
}
