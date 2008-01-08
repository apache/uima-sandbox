package org.apache.uima.aae.deploymentDescriptor;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.impl.Import_impl;
import org.apache.uima.util.InvalidXMLException;

public class XsltImportByName {
  
  public static String resolveByName(String input) {
    ResourceManager resourceManager = UIMAFramework.newDefaultResourceManager();
    Import theImport = new Import_impl();
    theImport.setName(input);
    try {
      return theImport.findAbsoluteUrl(resourceManager).toExternalForm();
    } catch (InvalidXMLException e) {
      e.printStackTrace();
      return "ERROR converting import by name to absolute path";
    }
  }


private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
