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


import org.apache.uima.caseditor.ui.Images;
import org.apache.uima.caseditor.ui.TaeUiPlugin;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * The <code>IWorkbenchAdapter</code> for the <code>AnnotatorElement</code>.
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.1.2.1 $, $Date: 2007/01/04 14:37:52 $
 */
class SingleElementAdapter extends
        AbstractElementAdapter
{
    /**
     * A <code>DocumentElement</code> has no children, just an emtpy array
     * will be retrived.
     */
    public Object[] getChildren(Object o)
    {
        return new Object[]
        {};
    }
    
    /**
     * Retrives the document element <code>ImageDescriptor</code>.
     */
    public ImageDescriptor getImageDescriptor(Object object)
    {
        return TaeUiPlugin.getTaeImageDescriptor(Images.MODEL_DOCUMENT);
    }
}