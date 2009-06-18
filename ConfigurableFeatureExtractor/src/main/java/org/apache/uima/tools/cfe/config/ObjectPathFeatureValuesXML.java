/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.uima.tools.cfe.config;

import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object Path Feature Values XML</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.apache.uima.tools.cfe.config.ObjectPathFeatureValuesXML#getObjectPath <em>Object Path</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.apache.uima.tools.cfe.config.ConfigPackage#getObjectPathFeatureValuesXML()
 * @model extendedMetaData="name='ObjectPathFeatureValuesXML' kind='empty'"
 * @generated
 */
public interface ObjectPathFeatureValuesXML extends EObject
{
  /**
     * Returns the value of the '<em><b>Object Path</b></em>' attribute.
     * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Object Path</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
     * @return the value of the '<em>Object Path</em>' attribute.
     * @see #setObjectPath(String)
     * @see org.apache.uima.tools.cfe.config.ConfigPackage#getObjectPathFeatureValuesXML_ObjectPath()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='objectPath'"
     * @generated
     */
  String getObjectPath();

  /**
     * Sets the value of the '{@link org.apache.uima.tools.cfe.config.ObjectPathFeatureValuesXML#getObjectPath <em>Object Path</em>}' attribute.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @param value the new value of the '<em>Object Path</em>' attribute.
     * @see #getObjectPath()
     * @generated
     */
  void setObjectPath(String value);

} // ObjectPathFeatureValuesXML