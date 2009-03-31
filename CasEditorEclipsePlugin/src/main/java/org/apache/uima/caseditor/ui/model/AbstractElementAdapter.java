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


import org.apache.uima.caseditor.core.model.INlpElement;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * The abstract base class for all NLPElement based
 * <code>IWorkbenchAdapter</code>s.
 */
abstract class AbstractElementAdapter implements IWorkbenchAdapter
{
    /**
     * Retrieves the name of the element.
     */
    public String getLabel(Object o)
    {
        INlpElement element = (INlpElement) o;

        return element.getName();
    }

    /**
     * Retrieves the parent element;
     */
    public Object getParent(Object o)
    {
        INlpElement element = (INlpElement) o;

        return element.getParent();
    }
}
