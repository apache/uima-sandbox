package org.apache.uima.casviewer.core.internal.style;

/**
 * 
 * 
 *
 */
public class BaseColor {
  protected int fgColorValue;     // Hex value of color (e.g., #C0C0C0)
  protected int bgColorValue;
  
  protected BaseColor (int fg, int bg) {
    fgColorValue = fg;
    bgColorValue = bg;
  }

  /**
   * @return the bgColorValue
   */
  public int getBgColorValue() {
    return bgColorValue;
  }

  /**
   * @param bgColorValue the bgColorValue to set
   */
  public void setBgColorValue(int bgColorValue) {
    this.bgColorValue = bgColorValue;
  }

  /**
   * @return the fgColorValue
   */
  public int getFgColorValue() {
    return fgColorValue;
  }

  /**
   * @param fgColorValue the fgColorValue to set
   */
  public void setFgColorValue(int fgColorValue) {
    this.fgColorValue = fgColorValue;
  }
  
  public String getFgColorString() {
    // return Integer.toHexString(fgColorValue);
    return String.format("%06X", fgColorValue);
  }

  public String getBgColorString() {
    // return Integer.toHexString(bgColorValue);
    return String.format("%06X", bgColorValue);
  }


}
