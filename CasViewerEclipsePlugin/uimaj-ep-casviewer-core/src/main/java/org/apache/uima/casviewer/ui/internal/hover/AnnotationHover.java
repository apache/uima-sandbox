package org.apache.uima.casviewer.ui.internal.hover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.casviewer.ui.internal.document.IVisualDecoration;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


// see InformationDispatchAction on how to make Hover focusable
public class AnnotationHover implements ITextHoverExtension, ITextHover {
  
  final static int  MAX_LINE_LENGTH = 80;
  final static int  LINE_LENGTH_BEFORE = 38;
  final static int  LINE_LENGTH_AFTER  = 38;

  private ISourceViewer fSourceViewer;
  protected TypeSystemStyle       typeSystemStyle;

  public AnnotationHover(ISourceViewer sourceViewer, TypeSystemStyle tsStyle) {
    fSourceViewer = sourceViewer;
    typeSystemStyle = tsStyle;
  }

  public IInformationControlCreator getHoverControlCreator() {
    return getInformationControlCreator();
  }

  public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
    IAnnotationModel model = fSourceViewer.getAnnotationModel();
    if (model == null) {
      Trace.err("No annotation model");
      return null;
    }
    
    // Find all annotations inside hoverRegion
    // Trace.err(hoverRegion.getOffset() + " " + hoverRegion.getLength());
    Iterator e = model.getAnnotationIterator();
    List list = new ArrayList();
    while (e.hasNext()) {
      Annotation a = (Annotation) e.next();
      Position p = model.getPosition(a);
      // Trace.err(a.getText() + " type:" + a.getType() + " offset:" + p.offset + " lenght:" + p.length);
      if (p != null && p.overlapsWith(hoverRegion.getOffset(), hoverRegion.getLength())) {
        String t = a.getText();
        if (t != null && t.trim().length() > 0) {
          list.add(a);
        }
      }
    }
    
    // Sort list of annotations
    if (list.size() > 0) {
        Collections.sort(list, new MyComparator());
        Annotation a;
        String msg = "";
        for (int i=0; i<list.size(); ++i) {
            a = (Annotation) list.get(i);
            String t = a.getText();
            t = t.replace("\n", " "); // need to handle splitted text of annotation
            if (t.length() > MAX_LINE_LENGTH) {
                t = t.substring(0, LINE_LENGTH_BEFORE) + " ... " 
                + t.substring(t.length() - LINE_LENGTH_AFTER);
            }
            // msg += "<li>[<b>" + a.getType() + "</b>] " + t + "</li>"; 
            if (a instanceof IVisualDecoration) {
                msg += "[" + ((IVisualDecoration)a).getUimaTypeName() + "]" + t + "\n"; 
            } else {
                msg += "[" + a.getType() + "]" + t + "\n";       
            }
        }
        return msg;
    }
    
    return null;
  }
    
  public static class MyComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        if ( ((Annotation)o1).getText().length() < ((Annotation)o2).getText().length() ) {
            return -1;
        } else if ( ((Annotation)o1).getText().length() > ((Annotation)o2).getText().length() ) {
            return 1;
        } else {
            return 0;
        }
    }
  }

  public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
    return new Region(offset, 0);
  }

  /***************************************************************************/


  public IInformationControlCreator getInformationControlCreator() {
    // Trace.err("getInformationControlCreator");
    return new IInformationControlCreator() {
      public IInformationControl createInformationControl(Shell parent) {
        // Trace.err("createInformationControl");
        return new CasViewerInformationControl(parent, SWT.TOOL | SWT.NO_TRIM, 
                        0, typeSystemStyle); 
      }
    };
  }

  /**
   * @param infoControl
   * @param control
   * @param provider
   */
  public static void addHoverListenerToControl(
          final IInformationControl infoControl, final Control control,
          final IControlHoverContentProvider provider) {

    control.addMouseTrackListener(new MouseTrackListener() {
      public void mouseEnter(MouseEvent e) {
      }
      public void mouseExit(MouseEvent e) {
        if (infoControl instanceof CasViewerInformationControl && ((CasViewerInformationControl)infoControl).isDisposed())
          return;
        infoControl.setVisible(false);
      }
      public void mouseHover(MouseEvent e) {
        if (infoControl instanceof CasViewerInformationControl && ((CasViewerInformationControl)infoControl).isDisposed())
          return;
        String text = provider.getHoverContent(control);
        if (text == null || text.trim().length() == 0)
          return;
        updateHover(infoControl, text);
        infoControl.setLocation(control.toDisplay(new Point(10, 25)));
        infoControl.setVisible(true);
      }
    });
  }

  /**
   * @param infoControl
   * @param text
   */
  public static void updateHover(IInformationControl infoControl, String text) {
    infoControl.setInformation(text);
    Point p = infoControl.computeSizeHint();
    infoControl.setSize(p.x, p.y);
    if (text == null || text.trim().length() == 0)
      infoControl.setVisible(false);
  }



}
