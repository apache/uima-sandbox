package org.apache.uima.casviewer.ui.internal.document;

import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;

public class CasSourceViewer extends SourceViewer {

  public CasSourceViewer(Composite parent, IVerticalRuler verticalRuler, IOverviewRuler overviewRuler, boolean showAnnotationsOverview, int styles) {
    super(parent, verticalRuler, overviewRuler, showAnnotationsOverview, styles);
  }


  public CasSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
    super(parent, ruler, styles);
  }
  

    /* (non-Javadoc)
     * @see org.eclipse.jface.text.TextViewer#findAndSelect(int, java.lang.String, boolean, boolean, boolean)
     * see also: FindReplaceDocumentAdapter
     */
    @Override
    protected int findAndSelect(int startPosition, String findString, boolean forwardSearch, boolean caseSensitive, boolean wholeWord) {
      int pos = super.findAndSelect(startPosition, findString, forwardSearch, caseSensitive, wholeWord);
      if (pos != -1) {
          revealRange(pos, findString.length());
      }      
      return pos;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.text.TextViewer#findAndSelectInRange(int, java.lang.String, boolean, boolean, boolean, int, int, boolean)
     */
    @Override
    protected int findAndSelectInRange(int startPosition, String findString, boolean forwardSearch, boolean caseSensitive, boolean wholeWord, int rangeOffset, int rangeLength, boolean regExSearch) {
        return super.findAndSelectInRange(startPosition, findString, forwardSearch, caseSensitive,
              wholeWord, rangeOffset, rangeLength, regExSearch);
    }

    public int findAndSelect (int startPos, String findString, boolean forwardSearch) {
        return findAndSelect (startPos, findString, forwardSearch, false, false);
    }
}
