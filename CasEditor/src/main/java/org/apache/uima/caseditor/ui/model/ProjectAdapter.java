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

package org.apache.uima.caseditor.ui.model;

import java.util.Collections;
import java.util.LinkedList;

import org.apache.uima.caseditor.core.model.NlpProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE.SharedImages;

/**
 * The <code>IWorkbenchAdapter</code> for the <code>NLPProject</code>.
 */
class ProjectAdapter extends
        AbstractElementAdapter
{
    /**
     * Retrives the children of the current <code>NLPProject</code>.
     */
    public Object[] getChildren(Object o)
    {
        Object result[];
        
        NlpProject nlpProject = (NlpProject) o;
        
        if (nlpProject.getProject().isOpen())
        {
            LinkedList<Object> childrenList = new LinkedList<Object>();
            
            childrenList.addAll(nlpProject.getCorpora());
            
            if (nlpProject.getTypesystem() != null)
            {
                childrenList.add(nlpProject.getTypesystem());
            }
            
            IResource[] resources;
            try
            {
                resources = nlpProject.getResources();
            }
            catch (CoreException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return new Object[]{};
            }
            
            if (resources != null)
            {
                Collections.addAll(childrenList, resources);
            }
            
            childrenList.addAll(nlpProject.getUimaSourceFolder());
            
            result = childrenList.toArray();

        }
        else
        {
            // no elements cause project is closed
            result = new Object[]
                                {};
        }
        
        return result;
    }
    
    /**
     * Retrives the <code>ImageDescriptor</code> for the
     * <code>NLPProject</code>.
     * 
     * @return the <code>ImageDescriptor</code>
     */
    public ImageDescriptor getImageDescriptor(Object object)
    {
        NlpProject nlpProject = (NlpProject) object;
        
        IProject project = nlpProject.getProject();
        
        IWorkbench workbench = PlatformUI.getWorkbench();
        ISharedImages sharedImages = workbench.getSharedImages();
        
        ImageDescriptor result;
        
        if (project.isOpen())
        {
            result = sharedImages
                   .getImageDescriptor(SharedImages.IMG_OBJ_PROJECT);
        }
        else
        {
            result = sharedImages.getImageDescriptor(
                    SharedImages.IMG_OBJ_PROJECT_CLOSED);
        }
        
        return result;
    }
}