
package com.zutubi.android.ant;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.tools.ant.BuildException;

/**
 * Ant task to dump the version details into a json file.
 */
public class JsonVersionTask extends AbstractManifestTask {
    private File jsonFile;
    private String url;

    public void setJsonFile(File jsonFile) {
	this.jsonFile = jsonFile;
    }
    
    public void setUpdateUrl(String url) {
	this.url = url;
    }
    
    @Override
    public void execute() throws BuildException {
        final Manifest manifest = parseManifest();
        try {
	    FileOutputStream out;
	    out = new FileOutputStream(jsonFile);
	    try {
		out.write(("{\"versionCode\":"+manifest.getVersionCode()+",\"updateURL\":"+url+"}").getBytes());
	    } finally {
		out.close();
	    }
	} catch (Exception e) {
	    throw new BuildException(e);
	}
    }

}
