package org.apache.uima.casviewer.ui.internal.hover;

public class KeyValuePair {
  
  protected Object parent;

  protected String label;

  protected String key;
  
  protected Object value;
  
  public KeyValuePair (String key, Object value) {
    this.parent = parent;
    this.label = label;
    this.key = key;
    this.value = value;
  }

  public KeyValuePair (String key, Object value, Object parent, String label) {
    this.parent = parent;
    this.label = label;
    this.key = key;
    this.value = value;
  }

  /**
   * @return the key
   */
  public String getKey() {
    return key;
  }

  /**
   * @param key the key to set
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @param label the label to set
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * @return the parent
   */
  public Object getParent() {
    return parent;
  }

  /**
   * @param parent the parent to set
   */
  public void setParent(Object parent) {
    this.parent = parent;
  }

  /**
   * @return the value
   */
  public Object getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(Object value) {
    this.value = value;
  }

}
