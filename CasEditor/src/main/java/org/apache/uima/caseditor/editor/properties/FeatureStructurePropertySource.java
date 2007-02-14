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

package org.apache.uima.caseditor.editor.properties;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;


import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.caseditor.core.util.Primitives;
import org.apache.uima.caseditor.editor.ModelFeatureStructure;
import org.apache.uima.caseditor.editor.properties.validator.CellEditorValidatorFacotory;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * This {@link IPropertySource} Source provides information about a {@link FeatureStructure}.
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.2.2.2 $, $Date: 2007/01/04 15:00:57 $
 */
class FeatureStructurePropertySource implements IPropertySource {
  private ModelFeatureStructure mStructure;

  /**
   * Initializes a new instance.
   * 
   * @param featureStructure
   */
  FeatureStructurePropertySource(ModelFeatureStructure featureStructure) {
    Assert.isNotNull(featureStructure);

    mStructure = featureStructure;
  }

  /**
   * Return always null.
   * 
   * A {@link FeatureStructurePropertySource} has no editable value.
   * 
   * @return null
   */
  public Object getEditableValue() {
    return "test";
  }

  public IPropertyDescriptor[] getPropertyDescriptors() {
    Collection<IPropertyDescriptor> descriptors = new LinkedList<IPropertyDescriptor>();

    Type annotationType = mStructure.getStructre().getType();

    Vector featureTypes = annotationType.getAppropriateFeatures();

    for (Object featureObject : featureTypes) {
      Feature feature = (Feature) featureObject;

      if (Primitives.isPrimitive(feature)) {
        TextPropertyDescriptor primitiveProperty = new TextPropertyDescriptor(feature, feature
                .getShortName());

        primitiveProperty.setValidator(CellEditorValidatorFacotory.createValidator(Primitives
                .getPrimitiveClass(feature)));

        descriptors.add(primitiveProperty);
      } else {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(feature, feature
                .getShortName());

        descriptors.add(propertyDescriptor);
      }
    }

    IPropertyDescriptor descriptorArray[] = new IPropertyDescriptor[descriptors.size()];
    descriptors.toArray(descriptorArray);

    return descriptorArray;
  }

  public Object getPropertyValue(Object id) {
    Assert.isLegal(id instanceof Feature);

    Feature feature = (Feature) id;

    Object result;

    if (Primitives.isPrimitive(feature)) {
      result = Primitives.getPrimitiv(mStructure.getStructre(), feature);

      if (result == null) {
        result = "";
      }

      result = result.toString();
    } else {
      FeatureStructure value = mStructure.getStructre().getFeatureValue(feature);

      if (value == null) {
        result = "null"; // nothing set
      } else {
        result = FSPropertySourceFactory.create(new ModelFeatureStructure(mStructure.getDocument(),
                value));
      }
    }

    return result;
  }

  public boolean isPropertySet(Object id) {
    return true;
  }

  public void resetPropertyValue(Object id) {
  }

  public void setPropertyValue(Object id, Object value) {
    Assert.isLegal(id instanceof Feature);

    Feature feature = (Feature) id;

    if (Primitives.isPrimitive(feature)) {
      mStructure.getStructre().setFeatureValueFromString((Feature) id, (String) value);

      mStructure.getDocument().update(mStructure.getStructre());
    }
  }
}