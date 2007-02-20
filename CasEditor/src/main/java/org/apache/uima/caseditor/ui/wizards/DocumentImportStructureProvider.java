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

package org.apache.uima.caseditor.ui.wizards;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.uima.caseditor.CasEditorPlugin;
import org.apache.uima.caseditor.core.model.NlpProject;
import org.apache.uima.caseditor.core.uima.DocumentUimaImpl;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;



/**
 * TODO: add javadoc here
 */
final class DocumentImportStructureProvider implements IImportStructureProvider
{
    private IProject mProject;
    
    /**
     * Constructs a new DocumentImportStructureProvider object.
     * 
     * @param containerFullPath
     */
    public DocumentImportStructureProvider(IPath containerFullPath)
    {
        // TODO: fix this ...
        mProject = ResourcesPlugin.getWorkspace().getRoot().findMember(
                containerFullPath).getProject();
    }
    
    public List getChildren(Object element)
    {
        return null;
    }
    
    public InputStream getContents(Object element)
    {
        File fileToImport = (File) element;
        
        String fileName = fileToImport.getName();
        
        if (fileName.endsWith(".rtf"))
        {
            InputStream in = null;
            
            try
            {
                in = new FileInputStream((File) element);
                String text = convert(in);
                
                return getDocument(text);
            }
            catch (FileNotFoundException e)
            {
                return null;
            }
            catch (IOException e)
            {
                return null;
            }
            finally
            {
                try
                {
                    if (in != null)
                    {
                        in.close();
                    }
                }
                catch (IOException e)
                {
                    // sorry that this can happen
                }
            }
            
        }
        else if (fileName.endsWith(".txt"))
        {
            InputStream in = null;
            try
            {
                in = new FileInputStream((File) element);
                
                StringBuffer textStringBuffer = new StringBuffer();
                
                byte[] readBuffer = new byte[2048];
                
                while (in.available() > 0)
                {
                    int length = in.read(readBuffer);
                    
                    textStringBuffer.append(new String(readBuffer, 0, length, 
                            "UTF-8"));
                }
                
                return getDocument(textStringBuffer.toString());
            }
            catch (FileNotFoundException e)
            {
                return null;
            }
            catch (IOException e)
            {
                return null;
            }
            finally
            {
                if (in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch (IOException e)
                    {
                        // sorry that this can fail
                    }
                }
            }
        }
        else
        {
            try
            {
                return new FileInputStream((File) element);
            }
            catch (FileNotFoundException e)
            {
                return null;
            }
        }
    }
    
    private String convert(InputStream rtfDocumentInputStream)
            throws IOException
    {
        RTFEditorKit aRtfEditorkit = new RTFEditorKit();
        
        StyledDocument styledDoc = new DefaultStyledDocument();
        
        String textDocument;
        
        try
        {
            aRtfEditorkit.read(rtfDocumentInputStream, styledDoc, 0);
            
            textDocument = styledDoc.getText(0, styledDoc.getLength());
        }
        catch (BadLocationException e)
        {
            throw new IOException("Error during parsing");
        }
        
        return textDocument;
    }
    
    public String getFullPath(Object element)
    {
        return ((File) element).getPath();
    }
    
    public String getLabel(Object element)
    {
        File fileToImport = (File) element;
        
        String fileName = fileToImport.getName();
        
        if (fileName.endsWith(".rtf") || fileName.endsWith(".txt"))
        {
            int nameWithouEndingLength = fileName.lastIndexOf(".");
            String nameWithouEnding = fileName.substring(0,
                    nameWithouEndingLength);
            
            return nameWithouEnding + ".xcas";
        }
        else
        {
            return fileName;
        }
    }
    
    public boolean isFolder(Object element)
    {
        return ((File) element).isDirectory();
    }
    
    private InputStream getDocument(String text)
    {
        NlpProject nlpProject = (NlpProject) CasEditorPlugin.getNlpModel()
                .findMember(mProject);
        
        DocumentUimaImpl gateDocument = new DocumentUimaImpl(nlpProject);
        
        gateDocument.getCAS().setDocumentText(text);
        
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(40000);
        try
        {
            gateDocument.serialize(outStream);
        }
        catch (CoreException e)
        {
            // TODO handle this exception
            e.printStackTrace();
            return null;
        }
        
        InputStream stream = new ByteArrayInputStream(outStream.toByteArray());
        
        return stream;
    }
}