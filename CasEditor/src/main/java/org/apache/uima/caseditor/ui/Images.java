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

package org.apache.uima.caseditor.ui;

/**
 * This enumeration contains all images supplyed by the 
 * tae ui plugin.
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.2.2.1 $, $Date: 2007/01/04 14:37:53 $
 */
public enum Images
{
    /**
     * The corpus image.
     */
    MODEL_CORPUS("model/corpus.gif"),
    
    /**
     * The document image.
     */
    MODEL_DOCUMENT("model/document.png"),
    
    /**
     * The source folder image.
     */
    MODEL_SOURCE_FOLDER("model/uima-source-folder.png"),
    
    /**
     * The config folder image.
     */
    MODEL_CONFIG_FOLDER("model/config.png"),
    
    /**
     * The enabled refresh icon.
     */
    EXPLORER_E_REFRESH("eceview16/refresh_nav.gif"),
    
    /**
     * The disabled refresh icon.
     */
    EXPLORER_D_REFRESH("dceview16/refresh_nav.gif");
    
    private String mPath;
    
    /**
     * Initializes a new instance.
     */
    private Images(String path)
    {
        mPath = path;
    }
    
    /**
     * Retrives the path
     * 
     * @return the path
     */
    public String getPath()
    {
        return mPath;
    }
}