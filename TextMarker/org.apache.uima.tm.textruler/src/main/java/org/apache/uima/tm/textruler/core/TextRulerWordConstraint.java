package org.apache.uima.tm.textruler.core;

import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;

/**
 * 
 * This class was moved from one of the algorithms to the core framework since it gets used in
 * almost every algorithm. It encapsulates the word constraint e.g. of a TextMarker rule item.
 */
public class TextRulerWordConstraint {

  protected TextRulerAnnotation tokenAnnotation;

  protected boolean isRegExpType; // indicates wheter this token can have

  // different contens according to its type

  // (PERIOD e.g. is NOT such a token, it is bound to the content '.'; NUM is
  // such a token, it can be any number!

  public TextRulerWordConstraint(TextRulerWordConstraint copyFrom) {
    super();
    tokenAnnotation = copyFrom.tokenAnnotation;
    isRegExpType = copyFrom.isRegExpType;
  }

  public TextRulerWordConstraint(TextRulerAnnotation tokenAnnotation) {
    super();
    this.tokenAnnotation = tokenAnnotation;
    TypeSystem ts = tokenAnnotation.getDocument().getCAS().getTypeSystem();
    Type wType = ts.getType(TextRulerToolkit.TM_WORD_TYPE_NAME);
    Type numType = ts.getType(TextRulerToolkit.TM_NUM_TYPE_NAME);
    Type markupType = ts.getType(TextRulerToolkit.TM_MARKUP_TYPE_NAME);
    // TODO special auch wieder einschalten!? !!!!!
    // Type specialType = ts.getType(TextRulerToolkit.TM_SPECIAL_TYPE_NAME);
    isRegExpType = ts.subsumes(wType, tokenAnnotation.getType())
            || ts.subsumes(markupType, tokenAnnotation.getType())
            || ts.subsumes(numType, tokenAnnotation.getType())
    // || ts.subsumes(specialType, tokenAnnotation.getType()
    ;
  }

  protected TextRulerWordConstraint(TextRulerAnnotation tokenAnnotation, boolean isRegExpType) {
    this.tokenAnnotation = tokenAnnotation;
    this.isRegExpType = isRegExpType;
  }

  public Type annotationType() {
    return tokenAnnotation.getType();
  }

  public String typeShortName() {
    return tokenAnnotation.getType().getShortName();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null)
      return false;
    return toString().equals(((TextRulerWordConstraint) o).toString());
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  public boolean isRegExpConstraint() {
    return isRegExpType;
  }

  @Override
  public String toString() {
    if (isRegExpConstraint())
      return TextRulerToolkit.escapeForTMStringParameter(TextRulerToolkit
              .escapeForRegExp(tokenAnnotation.getCoveredText()));
    else
      return tokenAnnotation.getType().getShortName();
  }

  public TextRulerWordConstraint copy() {
    return new TextRulerWordConstraint(this);
  }

  public TextRulerAnnotation getTokenAnnotation() {
    return tokenAnnotation;
  }
}
