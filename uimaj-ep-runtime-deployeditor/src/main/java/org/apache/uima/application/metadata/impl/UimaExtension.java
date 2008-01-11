/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jan 17, 2006, 11:51:43 AM
 * source:  UimaApp.java
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
