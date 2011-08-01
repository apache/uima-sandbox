package org.apache.uima.tm.textmarker.kernel.rule.quantifier;

import java.util.List;

import org.apache.uima.tm.textmarker.kernel.TextMarkerStatement;
import org.apache.uima.tm.textmarker.kernel.TextMarkerStream;
import org.apache.uima.tm.textmarker.kernel.rule.RuleElement;
import org.apache.uima.tm.textmarker.kernel.rule.RuleElementMatch;
import org.apache.uima.tm.textmarker.kernel.type.TextMarkerBasic;
import org.apache.uima.tm.textmarker.kernel.visitor.InferenceCrowd;


public class StarReluctant implements RuleElementQuantifier {

  public boolean continueMatch(int index, List<RuleElement> elements, TextMarkerBasic next,
          RuleElementMatch match, List<RuleElementMatch> matches, TextMarkerStream stream,
          InferenceCrowd crowd) {
    if (index == elements.size() - 1) {
      // reluctant = minimal ... last element never needs to match.
      return false;
    }
    boolean nextMatched = false;
    if (index + 1 < elements.size()) {
      RuleElement element = elements.get(index + 1);
      RuleElementMatch nextMatch = element.match(next, stream, crowd);
      if (nextMatch.matched()) {
        nextMatched = true;
      }
    }
    return !nextMatched && next != null;
  }

  public List<RuleElementMatch> evaluateMatches(List<RuleElementMatch> matches,
          TextMarkerStatement element, InferenceCrowd crowd) {
    return matches;
  }
}
