package org.apache.uima.casviewer.core.internal.style;



public class BaseTypeStyle extends BaseColor {
  protected String            typeName;   // full name
  protected String            typeShortName;   // lazy short name
  protected boolean           isChecked = false; //true if type is to be initially checked
  protected boolean           isHidden  = false;

  /*************************************************************************/
  
  protected BaseTypeStyle() {
    super(0, 0);    
  }
  
  protected BaseTypeStyle(String typeName, BaseColor baseColor) {
    super(baseColor.fgColorValue, baseColor.bgColorValue);
    this.typeName = typeName;
  }
  
  /*************************************************************************/
  
  public static String getShortName (String fullName) {
      if (fullName == null) return null;
      
      int index = fullName.lastIndexOf('.');
      if ( index < 0 ) {
          return fullName;
      } else {
          return fullName.substring(index+1);
      }
  }

  /**
   * @return the typeName
   */
  public String getTypeName() {
      return typeName;
  }

  /**
   * @param typeName the typeName to set
   */
  public void setTypeName(String typeName) {
      this.typeName = typeName;
  }

  /**
   * @return the typeShortName
   */
  public String getTypeShortName() {
      if (typeShortName == null) {
          typeShortName = getShortName(typeName);
      }
      return typeShortName; 
  }

  /**
   * @param typeShortName the typeShortName to set
   */
  public void setTypeShortName(String typeShortName) {
      this.typeShortName = typeShortName;
  }

  /**
   * @return the isChecked
   */
  public boolean isChecked() {
      return isChecked;
  }

  /**
   * @param isChecked the isChecked to set
   */
  public void setChecked(boolean isChecked) {
      this.isChecked = isChecked;
  }

  /**
   * @return the isHidden
   */
  public boolean isHidden() {
      return isHidden;
  }

  /**
   * @param isHidden the isHidden to set
   */
  public void setHidden(boolean isHidden) {
      this.isHidden = isHidden;
  }

}
