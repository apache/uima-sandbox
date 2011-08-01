package org.apache.uima.tm.textmarker.cev.explain.element;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.tm.textmarker.cev.TextMarkerCEVPlugin;
import org.apache.uima.tm.textmarker.cev.explain.tree.ConditionNode;
import org.apache.uima.tm.textmarker.cev.explain.tree.ExplainTree;
import org.apache.uima.tm.textmarker.cev.explain.tree.IExplainTreeNode;
import org.apache.uima.tm.textmarker.cev.explain.tree.RuleElementMatchNode;
import org.apache.uima.tm.textmarker.cev.explain.tree.RuleElementMatchesNode;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


public class ElementTreeLabelProvider extends LabelProvider implements ILabelProvider {

  private ElementViewPage owner;

  public ElementTreeLabelProvider(ElementViewPage owner) {
    super();
    this.owner = owner;
  }

  @Override
  public Image getImage(Object element) {
    if (element instanceof RuleElementMatchNode) {
      RuleElementMatchNode rem = (RuleElementMatchNode) element;
      boolean matched = rem.matched();
      return owner.getImage(TextMarkerCEVPlugin.RULE_ELEMENT_MATCH_TYPE + matched);
    } else if (element instanceof RuleElementMatchesNode) {
      RuleElementMatchesNode rems = (RuleElementMatchesNode) element;
      boolean matched = rems.matched();
      return owner.getImage(TextMarkerCEVPlugin.RULE_ELEMENT_MATCHES_TYPE + matched);
    } else if (element instanceof ConditionNode) {
      ConditionNode rems = (ConditionNode) element;
      boolean matched = rems.matched();
      return owner.getImage(TextMarkerCEVPlugin.EVAL_CONDITION_TYPE + matched);
    }
    return owner.getImage("element");
  }

  @Override
  public String getText(Object element) {
    if (element instanceof IExplainTreeNode) {
      IExplainTreeNode debugNode = (IExplainTreeNode) element;
      TypeSystem ts = debugNode.getTypeSystem();

      if (element instanceof RuleElementMatchesNode) {
        Type type = ts.getType(TextMarkerCEVPlugin.RULE_ELEMENT_MATCHES_TYPE);
        FeatureStructure fs = debugNode.getFeatureStructure();
        Feature f = type.getFeatureByBaseName(ExplainTree.ELEMENT);
        if (f != null) {
          String v = fs.getStringValue(f);
          return v;
        }
      } else if (element instanceof RuleElementMatchNode) {
        FeatureStructure fs = debugNode.getFeatureStructure();
        if (fs instanceof AnnotationFS) {
          String s = ((AnnotationFS) fs).getCoveredText();
          s = s.replaceAll("[\\n\\r]", "");
          return s;
        }
      } else if (element instanceof ConditionNode) {
        Type type = ts.getType(TextMarkerCEVPlugin.EVAL_CONDITION_TYPE);
        FeatureStructure fs = debugNode.getFeatureStructure();
        Feature f = type.getFeatureByBaseName(ExplainTree.ELEMENT);
        if (f != null) {
          String v = fs.getStringValue(f);
          return v;
        }
      }
    }
    return element.toString();
  }
}
