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
 * A representation of the model object '<em><b>Pattern Feature Values XML</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.apache.uima.tools.cfe.config.PatternFeatureValuesXML#getPattern <em>Pattern</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.apache.uima.tools.cfe.config.ConfigPackage#getPatternFeatureValuesXML()
 * @model extendedMetaData="name='PatternFeatureValuesXML' kind='empty'"
 * @generated
 */
public interface PatternFeatureValuesXML extends EObject
{
  /**
     * Returns the value of the '<em><b>Pattern</b></em>' attribute.
     * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pattern</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
     * @return the value of the '<em>Pattern</em>' attribute.
     * @see #setPattern(String)
     * @see org.apache.uima.tools.cfe.config.ConfigPackage#getPatternFeatureValuesXML_Pattern()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='pattern'"
     * @generated
     */
  String getPattern();

  /**
     * Sets the value of the '{@link org.apache.uima.tools.cfe.config.PatternFeatureValuesXML#getPattern <em>Pattern</em>}' attribute.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @param value the new value of the '<em>Pattern</em>' attribute.
     * @see #getPattern()
     * @generated
     */
  void setPattern(String value);

} // PatternFeatureValuesXML