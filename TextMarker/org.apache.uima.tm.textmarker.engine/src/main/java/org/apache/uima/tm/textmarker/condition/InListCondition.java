package org.apache.uima.tm.textmarker.condition;

import java.util.List;

import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.tm.textmarker.kernel.TextMarkerStream;
import org.apache.uima.tm.textmarker.kernel.expression.bool.BooleanExpression;
import org.apache.uima.tm.textmarker.kernel.expression.list.StringListExpression;
import org.apache.uima.tm.textmarker.kernel.expression.number.NumberExpression;
import org.apache.uima.tm.textmarker.kernel.expression.resource.WordListExpression;
import org.apache.uima.tm.textmarker.kernel.rule.EvaluatedCondition;
import org.apache.uima.tm.textmarker.kernel.rule.TextMarkerRuleElement;
import org.apache.uima.tm.textmarker.kernel.type.TextMarkerBasic;
import org.apache.uima.tm.textmarker.kernel.visitor.InferenceCrowd;
import org.apache.uima.tm.textmarker.resource.TextMarkerWordList;


public class InListCondition extends TerminalTextMarkerCondition {

  private BooleanExpression relative;

  private final NumberExpression distance;

  private WordListExpression listExpr;

  private StringListExpression stringList;

  public InListCondition(WordListExpression listExpr, NumberExpression distance,
          BooleanExpression relative) {
    super();
    this.listExpr = listExpr;
    this.distance = distance;
    this.relative = relative;
  }

  public InListCondition(StringListExpression list, NumberExpression distance,
          BooleanExpression relative) {
    super();
    this.distance = distance;
    this.relative = relative;
    this.stringList = list;
  }

  @Override
  public EvaluatedCondition eval(TextMarkerBasic annotation, Type matchedType,
          TextMarkerRuleElement element, TextMarkerStream stream, InferenceCrowd crowd) {
    AnnotationFS matchedAnnotation = stream.expandAnchor(annotation, matchedType);
    String coveredText = matchedAnnotation.getCoveredText();
    if (stringList == null) {
      TextMarkerWordList wordList = listExpr.getList(element.getParent());
      return new EvaluatedCondition(this, wordList.contains(coveredText,
              false, 0, null, 0));
    }
    List<String> sList = stringList.getList(element.getParent());
    boolean contains = sList.contains(coveredText);
    return new EvaluatedCondition(this, contains);
  }

  public BooleanExpression getRelative() {
    return relative;
  }

  public NumberExpression getDistance() {
    return distance;
  }

  public WordListExpression getListExpression() {
    return listExpr;
  }

  public StringListExpression getStringList() {
    return stringList;
  }

}
