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

package org.apache.uima.caseditor.ui.corpusview;


import org.apache.uima.caseditor.core.TaeCorePlugin;
import org.apache.uima.caseditor.core.model.INlpElement;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * TODO: add javadoc here
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.4.2.1 $, $Date: 2007/01/04 14:37:51 $
 */
public class CorpusExplorerContentProvider implements
        ITreeContentProvider
{
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }    
    
    public Object[] getElements(Object inputElement)
    {
        IWorkbenchAdapter workbenchAdapter = 
                getWorkbenchAdapter(inputElement);
            
        return workbenchAdapter != null ? 
                workbenchAdapter.getChildren(inputElement) :
                new Object[]{};
    }

    public Object[] getChildren(Object parentElement)
    {
        IWorkbenchAdapter workbenchAdapter = 
            getWorkbenchAdapter(parentElement);
        
        return workbenchAdapter != null ? 
                workbenchAdapter.getChildren(parentElement) :
                new Object[]{};
    }

    public Object getParent(Object element)
    {
        IWorkbenchAdapter workbenchAdapter = 
            getWorkbenchAdapter(element);

        Object result;
        
        if (element instanceof INlpElement)
        {
            result = workbenchAdapter.getParent(element);
        }
        else
        {
            if (element instanceof IResource)
            {
                IResource resource = (IResource) element;
                
                try
                {
                    result = TaeCorePlugin.getNlpModel().getParent(resource);
                }
                catch (CoreException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }
                
                if (result == null)
                {
                    result = resource.getParent();
                }
            }
            else
            {
                result = null;
            }
        }
        
        return result;
    }

    public boolean hasChildren(Object element)
    {
        IWorkbenchAdapter workbenchAdapter = 
                getWorkbenchAdapter(element);
        
        Object childs[] = workbenchAdapter.getChildren(element);
        
        boolean result;
        
        if (childs != null && childs.length == 0)
        {
            result = false;
        }
        else
        {
            result = true;
        }
        
        return result;
    }
    
    private static IWorkbenchAdapter getWorkbenchAdapter(Object input)
    {
        IWorkbenchAdapter result;
        
        if (input instanceof IAdaptable)
        {
            IAdaptable adapter = (IAdaptable) input;
            
            IWorkbenchAdapter workbenchAdapter = (IWorkbenchAdapter)
                    adapter.getAdapter(IWorkbenchAdapter.class);
            
            result = workbenchAdapter;
        }
        else
        {
            result = null; 
        }
        
//      TODO: log this as error if result == null
        
        
        return result;
    }
    
    /**
     * Disposes allocated resources.
     */
    public void dispose()
    {
        // currently there are no resources allocated
    }
}
