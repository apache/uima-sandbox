/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Apr 6, 2007, 1:25:31 AM
 * source:  UimaService_Impl.java
 */
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

package org.apache.uima.application.metadata.impl;

import org.apache.uima.application.metadata.UimaCasProcessor;
import org.apache.uima.resource.Parameter;
import org.apache.uima.resource.URISpecifier;


/**
 * 
 *
 */
public class UimaService_Impl extends AbstractUimaCasProcessor 
{
    private static final long serialVersionUID = 1L;
    
    public static final int SERVICE_PROTOCOL_SOAP               = 1;
    public static final int SERVICE_PROTOCOL_VINCI              = 2;
    public static final int SERVICE_RESOURCE_TYPE_AE            = 3;
    public static final int SERVICE_RESOURCE_TYPE_CAS_CONSUMER  = 4;
    
    protected URISpecifier  uriSpecifier;
    
    protected int           serviceProtocol;
    protected int           serviceResourceType;
    protected int           serviceUri;
    protected Parameter[]   parameters;
    
    
    /*************************************************************************/

    public UimaService_Impl () {
        super(UimaCasProcessor.CASPROCESSOR_CAT_UNKNOWN, null);
//        parameters = uriSpecifier.getParameters();
    }

    /*************************************************************************/


    /**
     * @return the serviceProtocol
     */
    public int getServiceProtocol() {
        return serviceProtocol;
    }

    /**
     * @param serviceProtocol the serviceProtocol to set
     */
    public void setServiceProtocol(int serviceProtocol) {
        this.serviceProtocol = serviceProtocol;
    }

    /**
     * @return the serviceResourceType
     */
    public int getServiceResourceType() {
        return serviceResourceType;
    }

    /**
     * @param serviceResourceType the serviceResourceType to set
     */
    public void setServiceResourceType(int serviceResourceType) {
        this.serviceResourceType = serviceResourceType;
    }

    /**
     * @return the serviceUri
     */
    public int getServiceUri() {
        return serviceUri;
    }

    /**
     * @param serviceUri the serviceUri to set
     */
    public void setServiceUri(int serviceUri) {
        this.serviceUri = serviceUri;
    }
    
    /*************************************************************************/

    
    /*************************************************************************/


}
