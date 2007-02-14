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

import java.util.Collection;
import java.util.LinkedList;


import org.apache.uima.caseditor.core.model.UimaConfigurationElement;
import org.apache.uima.caseditor.ui.Images;
import org.apache.uima.caseditor.ui.TaeUiPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * The <code>IWorkbenchAdapter</code> for the
 * <code>UIMAConfigurationElement</code>.
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 */
class UimaConfigurationAdapter extends
        AbstractElementAdapter
{
    
    /**
     * Retrives the childreens of <code>UIMAConfigurationElement</code>.
     */
    public Object[] getChildren(Object o)
    {
        UimaConfigurationElement element = (UimaConfigurationElement) o;
        
        Collection<Object> childs = new LinkedList<Object>();
        
        childs.addAll(element.getAnnotators());
        
        childs.addAll(element.getConsumers());
        
        try
        {
            childs.addAll(element.getNonNlpResources());
        }
        catch (CoreException e)
        {
            // TODO: handle it
            e.printStackTrace();
        }
        
        return childs.toArray();
    }
    
    /**
     * Retrives the <code>ImageDescriptor</code> for the
     * <code>UIMAConfigurationElement</code>.
     */
    public ImageDescriptor getImageDescriptor(Object object)
    {
        return TaeUiPlugin.getTaeImageDescriptor(
                Images.MODEL_CONFIG_FOLDER);
    }
}