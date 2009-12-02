package org.apache.uima.casviewer.ui.internal.hover;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.casviewer.ui.internal.style.TypeStyle;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class CasViewerInformationControl extends PopupDialog implements IInformationControl,
        IInformationControlExtension2 {
    private static final int INNER_BORDER = 1;

  private Font boldTextFont;

  private String hoverText;
  
  protected TypeSystemStyle       typeSystemStyle;
  
  private StyledText styledText;

  /** ********************************************************************** */

  public CasViewerInformationControl(Shell parent, int shellStyle, int treeStyle,
              TypeSystemStyle tsStyle) {
    this(parent, shellStyle, treeStyle, null, false);
    typeSystemStyle = tsStyle;
  }

  public CasViewerInformationControl(Shell parent, int shellStyle, int treeStyle,
          String invokingCommandId, boolean showStatusField) {
    super(parent, shellStyle | SWT.NO_FOCUS | SWT.ON_TOP, false, false, false, false, null,
            null /*"Info Area"*/);
    create();
  }

  public boolean isDisposed() {
      return getShell().isDisposed();
  }

  public void setVisible(boolean visible) {

    if (visible) {
        if (styledText.getWordWrap()) {
            Point currentSize= getShell().getSize();
            getShell().pack(true);
            Point newSize= getShell().getSize();
            if (newSize.x > currentSize.x || newSize.y > currentSize.y)
                setSize(currentSize.x, currentSize.y); // restore previous size
        }
      open();
    } else {
      saveDialogBounds(getShell());
      getShell().setVisible(false);
    }
  }

  /** ********************************************************************** */

  public void addDisposeListener(DisposeListener listener) {
    // TODO Auto-generated method stub

  }

  public void addFocusListener(FocusListener listener) {
    // TODO Auto-generated method stub

  }

  public Point computeSizeHint() {
    int widthHint = 400; // SWT.DEFAULT;

    if (getShell() != null) {
      Point pt = getShell().computeSize(widthHint, SWT.DEFAULT, true);
      pt.y += 10;
      return pt;
    } else {
      return new Point(400, 200);
    }
  }

  public void dispose() {
    if (boldTextFont != null) {
      boldTextFont.dispose();
    }
  }

  public boolean isFocusControl() {
    Trace.err();
    return false;
  }

  public void removeDisposeListener(DisposeListener listener) {
    // TODO Auto-generated method stub

  }

  public void removeFocusListener(FocusListener listener) {
    // TODO Auto-generated method stub

  }

  public void setBackgroundColor(Color background) {
    // TODO Auto-generated method stub

  }

  public void setFocus() {
    // TODO Auto-generated method stub

  }

  public void setForegroundColor(Color foreground) {
    // TODO Auto-generated method stub

  }

  public void setInformation(String information) {
    // hoverText = information;
    // formText.setText(information, true, false);
  }

  public void setLocation(Point location) {
    // Trace.err(location.toString());
    getShell().setLocation(location);
  }

  public void setSize(int width, int height) {
    getShell().setSize(width, height);
    // Trace.err("width: " + width + " height:" + height);
  }

  public void setSizeConstraints(int maxWidth, int maxHeight) {
  }

  public void setInput (Object input) {
      // Trace.err();
      StringBuffer buf = new StringBuffer();
      if (input != null) {
        if (input instanceof String) {          
          HoverInfoReader reader = new HoverInfoReader((String) input);
          HoverInfo hover;
          List list = new ArrayList(3);
          int offset = 0;
          int length;
          while ( (hover = reader.getAnnotationHover()) != null ) {
              TypeStyle style = typeSystemStyle.getTypeStyle(hover.type);
              int typeLength = style.getTypeShortName().length();
              buf.append("[" + style.getTypeShortName() + "] "
                      + hover.span + "\n");
              length = typeLength + hover.span.length() + 4;
              
              list.add(new StyleRange(offset+1, typeLength, 
                      TypeSystemStyle.SYSTEM_COLOR_BLACK, TypeSystemStyle.SYSTEM_COLOR_WHITE,
                      SWT.BOLD));

              list.add(new StyleRange(offset + typeLength + 3, 
                      length - typeLength - 3, 
                      style.getForeground(), style.getBackground()));
              offset += length;
          }
          styledText.setText(buf.toString());   
          styledText.setStyleRanges((StyleRange[]) list.toArray(new StyleRange[list.size()]));
        }
      }
    }
  
  public static Font createBoldFont(Display display, Font regularFont) {
    FontData[] fontDatas = regularFont.getFontData();
    for (int i = 0; i < fontDatas.length; i++) {
      fontDatas[i].setStyle(fontDatas[i].getStyle() | SWT.BOLD);
    }
    return new Font(display, fontDatas);
  }

  /** ********************************************************************** */

  protected Control createDialogArea(Composite parent) {
      parent.setLayout(new GridLayout());
      styledText = new StyledText(parent, SWT.WRAP | SWT.READ_ONLY);
      GridData gd = new GridData(GridData.BEGINNING | GridData.FILL_BOTH);
      gd.horizontalIndent = INNER_BORDER;
      gd.verticalIndent = INNER_BORDER;
      styledText.setLayoutData(gd);
      styledText.addKeyListener(new KeyListener() {

        public void keyPressed(KeyEvent e) {
          if (e.character == 0x1B) // ESC
            close();
        }

        public void keyReleased(KeyEvent e) {
        }
      });
      return styledText;
  }

}
