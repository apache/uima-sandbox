package org.apache.uima.tm.textmarker.kernel.rule;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.tm.textmarker.kernel.TextMarkerBlock;
import org.apache.uima.tm.textmarker.kernel.TextMarkerStream;
import org.apache.uima.tm.textmarker.kernel.expression.TextMarkerExpression;
import org.apache.uima.tm.textmarker.kernel.expression.string.StringExpression;
import org.apache.uima.tm.textmarker.kernel.type.TextMarkerBasic;


public class TextMarkerLiteralMatcher implements TextMarkerMatcher {

  private final StringExpression expression;

  public TextMarkerLiteralMatcher(StringExpression expression) {
    super();
    this.expression = expression;
  }

  public List<TextMarkerBasic> getMatchingBasics(TextMarkerStream stream, TextMarkerBlock parent) {
    List<TextMarkerBasic> result = new ArrayList<TextMarkerBasic>();
    AnnotationFS windowAnnotation = stream.getDocumentAnnotation();
    List<TextMarkerBasic> list = stream.getBasicsInWindow(windowAnnotation);
    for (TextMarkerBasic each : list) {
      if (each.getCoveredText().equals(expression.getStringValue(parent))) {
        result.add(each);
      }
    }
    return result;
  }

  @Override
  public FSIterator<AnnotationFS> getMatchingBasics2(TextMarkerStream stream, TextMarkerBlock parent) {
    assert (false);
    return null;
  }

  public Type getType(TextMarkerStream stream) {
    return null;
  }

  public boolean match(TextMarkerBasic annotation, TextMarkerStream stream, TextMarkerBlock parent) {
    if (annotation == null) {
      return false;
    }
    return annotation.getCoveredText().equals(expression.getStringValue(parent));
  }

  @Override
  public String toString() {
    return "\"" + expression.toString() + "\"";
  }

  public TextMarkerExpression getExpression() {
    return expression;
  }

  public Type getType(TextMarkerBlock parent, TextMarkerStream stream) {
    return null;
  }

}
