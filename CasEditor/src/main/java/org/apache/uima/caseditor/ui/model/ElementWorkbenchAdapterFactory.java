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


import org.apache.uima.caseditor.core.model.CorpusElement;
import org.apache.uima.caseditor.core.model.NlpModel;
import org.apache.uima.caseditor.core.model.NlpProject;
import org.apache.uima.caseditor.core.model.UimaConfigurationElement;
import org.apache.uima.caseditor.core.model.UimaSourceFolder;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * TODO: add javadoc here
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.1.2.1 $, $Date: 2007/01/04 14:37:52 $
 */
public class ElementWorkbenchAdapterFactory implements IAdapterFactory
{
    private IWorkbenchAdapter mModelAdapter = new ModelAdapter();
    private IWorkbenchAdapter mProjectAdapter = new ProjectAdapter();
    private IWorkbenchAdapter mCorpusAdapter = new CorpusAdapter();
    private IWorkbenchAdapter mUimaSourceFolderAdapter = 
        new UimaSourceFolderAdapter();
    private IWorkbenchAdapter mUimaConfigurationAdapter = 
        new UimaConfigurationAdapter();
    private IWorkbenchAdapter mSingleElementAdapter = 
        new SingleElementAdapter();
    
    public Object getAdapter(Object adaptableObject, Class adapterType)
    {
        if (adaptableObject instanceof NlpModel)
        {
            return mModelAdapter;
        }
        else if (adaptableObject instanceof NlpProject)
        {
            return mProjectAdapter;
        }
        else if (adaptableObject instanceof CorpusElement)
        {
            return mCorpusAdapter;
        }
        else if (adaptableObject instanceof UimaSourceFolder)
        {
            return mUimaSourceFolderAdapter;
        }
        else if (adaptableObject instanceof UimaConfigurationElement)
        {
            return mUimaConfigurationAdapter;
        }
        else
        {
            return mSingleElementAdapter;
        }
    }

    public Class[] getAdapterList()
    {
        return new Class[] {IWorkbenchAdapter.class};

    }
}