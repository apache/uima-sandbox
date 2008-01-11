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

package org.apache.uima.cpe.model;


/**
 * 
 *
 */
public class Node {
   
    /*************************************************************************/
    
    protected String        label;
    protected int           nodeKind;
    protected Object        object;
    
    /*************************************************************************/

    public Node (int nodeKind, String label, Object objectModel) {
        this.nodeKind   = nodeKind;
        this.label      = label;
        this.object     = objectModel;
    }
    public Node (int nodeKind, String label, int objectModel) {
        this.nodeKind   = nodeKind;
        this.label      = label;
        this.object     = Integer.valueOf(objectModel);
    }

    public boolean equals(Object obj) {
        // Trace.trace("label:" + label);
        if (obj == null) return false;
        if (! (obj instanceof Node)) return false;
        Node n = (Node) obj;
        if (label.equals(n.getLabel()) && nodeKind == n.getNodeKind()) {
            if (object != null
             && object.equals(n.getObject())) {
                return true;
            } else if (object == null && n.getObject() == null) {
                return true;
            }
        }
        return false;
    }
    
    public int hashCode() {
        // Trace.trace("label:" + label);
        return label.hashCode() + nodeKind;
    }
    
    /**
     * @return Returns the label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label The label to set.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return Returns the nodeKind.
     */
    public int getNodeKind() {
        return nodeKind;
    }

    /**
     * @param nodeKind The nodeKind to set.
     */
    public void setNodeKind(int nodeKind) {
        this.nodeKind = nodeKind;
    }

    /**
     * @return Returns the objectModel.
     */
    public Object getObject() {
        return object;
    }

    /**
     * @param objectModel The objectModel to set.
     */
    public void setObject(Object objectModel) {
        this.object = objectModel;
    }

}
