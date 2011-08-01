package org.apache.uima.tm.textruler.core;

import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;

/**
 * 
 * This class encapsulates the for us important information about an Annotation, since AnnotationFS
 * keeps its corresponding CAS alive or gets killed when the CAS gets reset.
 * 
 */
public class TextRulerAnnotation {

  private TextRulerExampleDocument document; // we also keep a reference to

  // our document, but this does
  // not hold the CAS forever, it
  // loads it on demand!
  private int begin;

  private int end;

  private Type type;

  private String coveredText;

  public TextRulerAnnotation(AnnotationFS afs, TextRulerExampleDocument document) {
    this.document = document;
    this.begin = afs.getBegin();
    this.end = afs.getEnd();
    this.type = afs.getType();
    this.coveredText = afs.getCoveredText();
  }

  public TextRulerAnnotation(AnnotationFS afs) {
    this.document = null;
    this.begin = afs.getBegin();
    this.end = afs.getEnd();
    this.type = afs.getType();
    this.coveredText = afs.getCoveredText();
  }

  public TextRulerExampleDocument getDocument() {
    return document;
  }

  public int getBegin() {
    return begin;
  }

  public int getEnd() {
    return end;
  }

  public Type getType() {
    return type;
  }

  public String getCoveredText() {
    return coveredText;
  }

  @Override
  public String toString() {
    return getCoveredText();
  }

  @Override
  public boolean equals(Object o) {
    TextRulerAnnotation a = (TextRulerAnnotation) o;
    return (document == a.document) && (begin == a.begin) && (end == a.end)
            && coveredText.equals(a.coveredText);
  }

  @Override
  public int hashCode() {
    return begin * 17 * end + coveredText.hashCode();
  }

}
