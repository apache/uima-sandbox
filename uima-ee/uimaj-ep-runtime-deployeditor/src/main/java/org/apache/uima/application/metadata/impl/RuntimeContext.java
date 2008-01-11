/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jul 8, 2006, 10:47:33 AM
 * source:  RuntimeContext.java
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

import org.apache.uima.application.metadata.IRuntimeContext;

/**
 *  Runtime context for UIMA application which includes:
 *      - classpath of the project
 *      - datapath
 *
 */
public class RuntimeContext implements IRuntimeContext {

    protected String                projectName;
    protected String                projectClasspath;
    protected String                projectDatapath;
    
    public RuntimeContext() {
        // NOT to be used
    }

    /**
     * @return the projectClasspath
     */
    public String getProjectClasspath() {
        return projectClasspath;
    }

    /**
     * @param projectClasspath the projectClasspath to set
     */
    public void setProjectClasspath(String projectClasspath) {
        this.projectClasspath = projectClasspath;
    }

    /**
     * @return the projectDatapath
     */
    public String getProjectDatapath() {
        return projectDatapath;
    }

    /**
     * @param projectDatapath the projectDatapath to set
     */
    public void setProjectDatapath(String projectDatapath) {
        this.projectDatapath = projectDatapath;
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
