package org.apache.uima.dde.internal.hover;

import org.eclipse.jface.text.AbstractHoverInformationControlManager;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.TreeItem;

public class HoverManager extends AbstractHoverInformationControlManager {

  private TreeViewer viewer;

  public HoverManager(TreeViewer viewer, IInformationControlCreator creator) {
    super(creator);
    this.viewer = viewer;
  }

  @Override
  protected void computeInformation() {
    Point pt = getHoverEventLocation();
    // Trace.err("x: " + pt.x + " y: " + pt.y);
    TreeItem item = viewer.getTree().getItem(pt);
    if (item != null) {
      // Trace.err("Hover");
      setInformation(item, item.getBounds());
    } else {
      // Trace.err("NOT Hover");
      setInformation(null, null);
    }
  }

}
