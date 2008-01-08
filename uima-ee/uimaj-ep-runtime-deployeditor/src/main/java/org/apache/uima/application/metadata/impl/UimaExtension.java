/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jan 17, 2006, 11:51:43 AM
 * source:  UimaApp.java
 */
package org.apache.uima.application.metadata.impl;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;
import org.xml.sax.SAXException;


/**
 * 
 *
 */
public class UimaExtension {
    
    protected XMLParser     uimaXMLParser;

    /**
     * 
     */
    public UimaExtension() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    public void parse (String fileName) {
        try {
            UimaApplication_Impl u = (UimaApplication_Impl) uimaXMLParser.parse(new XMLInputSource(fileName));
            u.printMe();
            
            StringWriter w = new StringWriter();
            try {
                u.toXML(w);
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Trace.trace(w.toString());
        } catch (InvalidXMLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param args
     * @return void
     */
    public static void main(String[] args) {
        UimaExtension app = new UimaExtension();
        app.uimaXMLParser = org.apache.uima.UIMAFramework.getXMLParser();
        UimaApplication_Impl.initUimaApplicationFramework ();

//            app.uimaXMLParser.addMapping("uimaApplication", 
//                    "com.ibm.uima.cddeditor.metadata.UimaApplication_Impl");
//            app.uimaXMLParser.addMapping("configParamSettingsSets", 
//                    "com.ibm.uima.cddeditor.metadata.ConfigParamSettingsSets_impl");
//            app.uimaXMLParser.addMapping("configParamSettingsSet", 
//                    "com.ibm.uima.cddeditor.metadata.ConfigParamSettingsSet_impl");
            
            app.parse("c:/uima/Test/testApp.xml");
            

    }

}
