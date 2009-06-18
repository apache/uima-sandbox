/*
 * XML Type:  FeatureObjectMatcherXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.FeatureObjectMatcherXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans.impl;
/**
 * An XML FeatureObjectMatcherXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public class FeatureObjectMatcherXMLImpl extends org.apache.uima.tools.cfe.config.xmlBeans.impl.PartialObjectMatcherXMLImpl implements org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML
{
    
    public FeatureObjectMatcherXMLImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName WINDOWSIZELEFT$0 = 
        new javax.xml.namespace.QName("", "windowsizeLeft");
    private static final javax.xml.namespace.QName WINDOWSIZEINSIDE$2 = 
        new javax.xml.namespace.QName("", "windowsizeInside");
    private static final javax.xml.namespace.QName WINDOWSIZERIGHT$4 = 
        new javax.xml.namespace.QName("", "windowsizeRight");
    private static final javax.xml.namespace.QName WINDOWSIZEENCLOSED$6 = 
        new javax.xml.namespace.QName("", "windowsizeEnclosed");
    private static final javax.xml.namespace.QName WINDOWFLAGS$8 = 
        new javax.xml.namespace.QName("", "windowFlags");
    private static final javax.xml.namespace.QName ORIENTATION$10 = 
        new javax.xml.namespace.QName("", "orientation");
    private static final javax.xml.namespace.QName DISTANCE$12 = 
        new javax.xml.namespace.QName("", "distance");
    
    
    /**
     * Gets the "windowsizeLeft" attribute
     */
    public int getWindowsizeLeft()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(WINDOWSIZELEFT$0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "windowsizeLeft" attribute
     */
    public org.apache.xmlbeans.XmlInt xgetWindowsizeLeft()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(WINDOWSIZELEFT$0);
            return target;
        }
    }
    
    /**
     * True if has "windowsizeLeft" attribute
     */
    public boolean isSetWindowsizeLeft()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(WINDOWSIZELEFT$0) != null;
        }
    }
    
    /**
     * Sets the "windowsizeLeft" attribute
     */
    public void setWindowsizeLeft(int windowsizeLeft)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(WINDOWSIZELEFT$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(WINDOWSIZELEFT$0);
            }
            target.setIntValue(windowsizeLeft);
        }
    }
    
    /**
     * Sets (as xml) the "windowsizeLeft" attribute
     */
    public void xsetWindowsizeLeft(org.apache.xmlbeans.XmlInt windowsizeLeft)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(WINDOWSIZELEFT$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(WINDOWSIZELEFT$0);
            }
            target.set(windowsizeLeft);
        }
    }
    
    /**
     * Unsets the "windowsizeLeft" attribute
     */
    public void unsetWindowsizeLeft()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(WINDOWSIZELEFT$0);
        }
    }
    
    /**
     * Gets the "windowsizeInside" attribute
     */
    public int getWindowsizeInside()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(WINDOWSIZEINSIDE$2);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "windowsizeInside" attribute
     */
    public org.apache.xmlbeans.XmlInt xgetWindowsizeInside()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(WINDOWSIZEINSIDE$2);
            return target;
        }
    }
    
    /**
     * True if has "windowsizeInside" attribute
     */
    public boolean isSetWindowsizeInside()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(WINDOWSIZEINSIDE$2) != null;
        }
    }
    
    /**
     * Sets the "windowsizeInside" attribute
     */
    public void setWindowsizeInside(int windowsizeInside)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(WINDOWSIZEINSIDE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(WINDOWSIZEINSIDE$2);
            }
            target.setIntValue(windowsizeInside);
        }
    }
    
    /**
     * Sets (as xml) the "windowsizeInside" attribute
     */
    public void xsetWindowsizeInside(org.apache.xmlbeans.XmlInt windowsizeInside)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(WINDOWSIZEINSIDE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(WINDOWSIZEINSIDE$2);
            }
            target.set(windowsizeInside);
        }
    }
    
    /**
     * Unsets the "windowsizeInside" attribute
     */
    public void unsetWindowsizeInside()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(WINDOWSIZEINSIDE$2);
        }
    }
    
    /**
     * Gets the "windowsizeRight" attribute
     */
    public int getWindowsizeRight()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(WINDOWSIZERIGHT$4);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "windowsizeRight" attribute
     */
    public org.apache.xmlbeans.XmlInt xgetWindowsizeRight()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(WINDOWSIZERIGHT$4);
            return target;
        }
    }
    
    /**
     * True if has "windowsizeRight" attribute
     */
    public boolean isSetWindowsizeRight()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(WINDOWSIZERIGHT$4) != null;
        }
    }
    
    /**
     * Sets the "windowsizeRight" attribute
     */
    public void setWindowsizeRight(int windowsizeRight)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(WINDOWSIZERIGHT$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(WINDOWSIZERIGHT$4);
            }
            target.setIntValue(windowsizeRight);
        }
    }
    
    /**
     * Sets (as xml) the "windowsizeRight" attribute
     */
    public void xsetWindowsizeRight(org.apache.xmlbeans.XmlInt windowsizeRight)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(WINDOWSIZERIGHT$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(WINDOWSIZERIGHT$4);
            }
            target.set(windowsizeRight);
        }
    }
    
    /**
     * Unsets the "windowsizeRight" attribute
     */
    public void unsetWindowsizeRight()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(WINDOWSIZERIGHT$4);
        }
    }
    
    /**
     * Gets the "windowsizeEnclosed" attribute
     */
    public int getWindowsizeEnclosed()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(WINDOWSIZEENCLOSED$6);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "windowsizeEnclosed" attribute
     */
    public org.apache.xmlbeans.XmlInt xgetWindowsizeEnclosed()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(WINDOWSIZEENCLOSED$6);
            return target;
        }
    }
    
    /**
     * True if has "windowsizeEnclosed" attribute
     */
    public boolean isSetWindowsizeEnclosed()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(WINDOWSIZEENCLOSED$6) != null;
        }
    }
    
    /**
     * Sets the "windowsizeEnclosed" attribute
     */
    public void setWindowsizeEnclosed(int windowsizeEnclosed)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(WINDOWSIZEENCLOSED$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(WINDOWSIZEENCLOSED$6);
            }
            target.setIntValue(windowsizeEnclosed);
        }
    }
    
    /**
     * Sets (as xml) the "windowsizeEnclosed" attribute
     */
    public void xsetWindowsizeEnclosed(org.apache.xmlbeans.XmlInt windowsizeEnclosed)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(WINDOWSIZEENCLOSED$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(WINDOWSIZEENCLOSED$6);
            }
            target.set(windowsizeEnclosed);
        }
    }
    
    /**
     * Unsets the "windowsizeEnclosed" attribute
     */
    public void unsetWindowsizeEnclosed()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(WINDOWSIZEENCLOSED$6);
        }
    }
    
    /**
     * Gets the "windowFlags" attribute
     */
    public int getWindowFlags()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(WINDOWFLAGS$8);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "windowFlags" attribute
     */
    public org.apache.xmlbeans.XmlInt xgetWindowFlags()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(WINDOWFLAGS$8);
            return target;
        }
    }
    
    /**
     * True if has "windowFlags" attribute
     */
    public boolean isSetWindowFlags()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(WINDOWFLAGS$8) != null;
        }
    }
    
    /**
     * Sets the "windowFlags" attribute
     */
    public void setWindowFlags(int windowFlags)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(WINDOWFLAGS$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(WINDOWFLAGS$8);
            }
            target.setIntValue(windowFlags);
        }
    }
    
    /**
     * Sets (as xml) the "windowFlags" attribute
     */
    public void xsetWindowFlags(org.apache.xmlbeans.XmlInt windowFlags)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(WINDOWFLAGS$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(WINDOWFLAGS$8);
            }
            target.set(windowFlags);
        }
    }
    
    /**
     * Unsets the "windowFlags" attribute
     */
    public void unsetWindowFlags()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(WINDOWFLAGS$8);
        }
    }
    
    /**
     * Gets the "orientation" attribute
     */
    public boolean getOrientation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ORIENTATION$10);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "orientation" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetOrientation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(ORIENTATION$10);
            return target;
        }
    }
    
    /**
     * True if has "orientation" attribute
     */
    public boolean isSetOrientation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(ORIENTATION$10) != null;
        }
    }
    
    /**
     * Sets the "orientation" attribute
     */
    public void setOrientation(boolean orientation)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ORIENTATION$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ORIENTATION$10);
            }
            target.setBooleanValue(orientation);
        }
    }
    
    /**
     * Sets (as xml) the "orientation" attribute
     */
    public void xsetOrientation(org.apache.xmlbeans.XmlBoolean orientation)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(ORIENTATION$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(ORIENTATION$10);
            }
            target.set(orientation);
        }
    }
    
    /**
     * Unsets the "orientation" attribute
     */
    public void unsetOrientation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(ORIENTATION$10);
        }
    }
    
    /**
     * Gets the "distance" attribute
     */
    public boolean getDistance()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DISTANCE$12);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "distance" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetDistance()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(DISTANCE$12);
            return target;
        }
    }
    
    /**
     * True if has "distance" attribute
     */
    public boolean isSetDistance()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(DISTANCE$12) != null;
        }
    }
    
    /**
     * Sets the "distance" attribute
     */
    public void setDistance(boolean distance)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DISTANCE$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DISTANCE$12);
            }
            target.setBooleanValue(distance);
        }
    }
    
    /**
     * Sets (as xml) the "distance" attribute
     */
    public void xsetDistance(org.apache.xmlbeans.XmlBoolean distance)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(DISTANCE$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(DISTANCE$12);
            }
            target.set(distance);
        }
    }
    
    /**
     * Unsets the "distance" attribute
     */
    public void unsetDistance()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(DISTANCE$12);
        }
    }
}
