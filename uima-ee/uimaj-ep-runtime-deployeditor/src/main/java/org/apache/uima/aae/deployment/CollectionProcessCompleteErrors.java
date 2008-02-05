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

package org.apache.uima.aae.deployment;

public interface CollectionProcessCompleteErrors 
{
    final static public int        KIND_TIMEOUT                = 1;
    final static public int        KIND_ADDITIONA_ERROR_ACTION = 2;
    
    // String name used in the table view
    final static String NAME_TIMEOUT                = "Timeout (in millisec)";    
    final static String NAME_ADDITIONA_ERROR_ACTION = "Additional Error Action";    

    public CollectionProcessCompleteErrors clone(AsyncAEErrorConfiguration parent);
    
    public AsyncAEErrorConfiguration getParent();

    public void setValueById (int id, Object value);

    /**
     * @return the additionalErrorAction
     */
    public String getAdditionalErrorAction();

    /**
     * @param additionalErrorAction the additionalErrorAction to set
     */
    public void setAdditionalErrorAction(String additionalErrorAction);

    /**
     * @return the timeout
     */
    public int getTimeout();

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout);

}