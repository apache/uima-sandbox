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

package org.apache.uima.application.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UIMA_IllegalStateException;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLizable;


/**
 * 
 * 
 *
 */
public class UimaXmlParsingUtil {

    
    // Duplicate in UimaApplication_Impl
    public static XMLizable parseUimaXmlDescriptor (String xmlDescriptorFileName) 
                throws IOException, InvalidXMLException
    {
        XMLizable descriptionObject = null;
        
        File descFile = new File(xmlDescriptorFileName);;
        boolean validArgs = descFile.exists() && !descFile.isDirectory();
        if (!validArgs) {
            Trace.err("Cannot find: " +xmlDescriptorFileName);
            return null;
        }
        
//        try {
            // Redirect UIMA log messages to file
            UIMAFramework.getLogger().setOutputStream(
                    new PrintStream(new FileOutputStream("uima.log")));

            // turn off xi:include and environment variable expansion
//            XMLParser.ParsingOptions parsingOptions = new XMLParser.ParsingOptions(
//                    false, false);
            
            // Get Resource Specifier from XML file
            XMLInputSource in;
            in = new XMLInputSource(descFile);
            // descriptionObject = UIMAFramework.getXMLParser().parse(in, parsingOptions);
            descriptionObject = UIMAFramework.getXMLParser().parse(in);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        } catch (InvalidXMLException e) {
//            Trace.err("xmlDescriptorFileName: " + xmlDescriptorFileName);
//            e.printStackTrace();
//            return null;
//        } catch (UIMA_IllegalStateException e) {
//            Trace.err("xmlDescriptorFileName: " + xmlDescriptorFileName);
//            // e.printStackTrace();
//            return null;
//        }
        
        return descriptionObject;
    } // parseUimaXmlDescriptor

    public static XMLizable parseUimaXmlDescriptorFromString (String xmlDescriptorString)
    {
        XMLizable descriptionObject = null;
        
        
        try {
            // Redirect UIMA log messages to file
            UIMAFramework.getLogger().setOutputStream(
                    new PrintStream(new FileOutputStream("uima.log")));

            // turn off xi:include and environment variable expansion
//            XMLParser.ParsingOptions parsingOptions = new XMLParser.ParsingOptions(
//                    false, false);
            
            // Get Resource Specifier from XML file
            XMLInputSource in = new XMLInputSource(new ByteArrayInputStream(xmlDescriptorString.getBytes()), null);
            // descriptionObject = UIMAFramework.getXMLParser().parse(in, parsingOptions);
            descriptionObject = UIMAFramework.getXMLParser().parse(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (InvalidXMLException e) {
            Trace.err("xmlDescriptorString: " + xmlDescriptorString);
            e.printStackTrace();
            return null;
        } catch (UIMA_IllegalStateException e) {
            Trace.err("xmlDescriptorString: " + xmlDescriptorString);
            // e.printStackTrace();
            return null;
        }
        
        return descriptionObject;
    } // parseUimaXmlDescriptorFromString
    
}
