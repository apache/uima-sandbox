/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.uima.tools.cfe.config.impl;

import org.apache.uima.tools.cfe.config.ConfigPackage;
import org.apache.uima.tools.cfe.config.ObjectPathFeatureValuesXML;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Object Path Feature Values XML</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.apache.uima.tools.cfe.config.impl.ObjectPathFeatureValuesXMLImpl#getObjectPath <em>Object Path</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ObjectPathFeatureValuesXMLImpl extends EObjectImpl implements ObjectPathFeatureValuesXML
{
  /**
     * The default value of the '{@link #getObjectPath() <em>Object Path</em>}' attribute.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @see #getObjectPath()
     * @generated
     * @ordered
     */
  protected static final String OBJECT_PATH_EDEFAULT = null;

  /**
     * The cached value of the '{@link #getObjectPath() <em>Object Path</em>}' attribute.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @see #getObjectPath()
     * @generated
     * @ordered
     */
  protected String objectPath = OBJECT_PATH_EDEFAULT;

  /**
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  protected ObjectPathFeatureValuesXMLImpl()
  {
        super();
    }

  /**
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  @Override
protected EClass eStaticClass()
  {
        return ConfigPackage.Literals.OBJECT_PATH_FEATURE_VALUES_XML;
    }

  /**
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  public String getObjectPath()
  {
        return objectPath;
    }

  /**
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  public void setObjectPath(String newObjectPath)
  {
        String oldObjectPath = objectPath;
        objectPath = newObjectPath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.OBJECT_PATH_FEATURE_VALUES_XML__OBJECT_PATH, oldObjectPath, objectPath));
    }

  /**
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  @Override
public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
        switch (featureID)
        {
            case ConfigPackage.OBJECT_PATH_FEATURE_VALUES_XML__OBJECT_PATH:
                return getObjectPath();
        }
        return super.eGet(featureID, resolve, coreType);
    }

  /**
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  @Override
public void eSet(int featureID, Object newValue)
  {
        switch (featureID)
        {
            case ConfigPackage.OBJECT_PATH_FEATURE_VALUES_XML__OBJECT_PATH:
                setObjectPath((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

  /**
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  @Override
public void eUnset(int featureID)
  {
        switch (featureID)
        {
            case ConfigPackage.OBJECT_PATH_FEATURE_VALUES_XML__OBJECT_PATH:
                setObjectPath(OBJECT_PATH_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

  /**
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  @Override
public boolean eIsSet(int featureID)
  {
        switch (featureID)
        {
            case ConfigPackage.OBJECT_PATH_FEATURE_VALUES_XML__OBJECT_PATH:
                return OBJECT_PATH_EDEFAULT == null ? objectPath != null : !OBJECT_PATH_EDEFAULT.equals(objectPath);
        }
        return super.eIsSet(featureID);
    }

  /**
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  @Override
public String toString()
  {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (objectPath: ");
        result.append(objectPath);
        result.append(')');
        return result.toString();
    }

} //ObjectPathFeatureValuesXMLImpl