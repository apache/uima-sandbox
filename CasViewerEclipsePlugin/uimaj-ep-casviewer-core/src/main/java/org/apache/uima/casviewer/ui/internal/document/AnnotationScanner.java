package org.apache.uima.casviewer.ui.internal.document;

import java.util.List;

import org.apache.uima.casviewer.core.internal.AnnotationObject;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * Used by CasViewerSourceViewerConfiguration.getPresentationReconciler
 *
 */
public class AnnotationScanner implements ITokenScanner {

    /** The document to be scanned */
    protected IDocument fDocument;

    /** The offset of the next character to be read */
    protected int fOffset;
    /** The end offset of the range to be scanned */
    protected int fRangeEnd;
    /** The offset of the last read token */
    protected int fTokenOffset;
    protected int fTokenLength;
    
    private List        annotations;
    private int         lastIndex = 0;  // next available token
    
    // For testing
    static private AnnotationScanner   instance = null;
    
    
    /**
     */
    public AnnotationScanner() {
        instance = this;
    }
    
    static public AnnotationScanner getInstance () {
        return instance;
    }
    
    public void setAnnotations (List list)
    {
        annotations = list;
        lastIndex = 0;
    }

    public void setRange(IDocument document, int offset, int length) {
        // Trace.trace("offset: " + offset + " ; length: " + length);
        fDocument= document;
        fOffset= offset;
        fRangeEnd= Math.min(fDocument.getLength(), offset + length);
    }

    private TextAttribute green = new TextAttribute (Display.getCurrent().getSystemColor(SWT.COLOR_GREEN), null, SWT.BOLD);
    private TextAttribute red   = new TextAttribute (Display.getCurrent().getSystemColor(SWT.COLOR_RED), null, SWT.BOLD);
    private boolean isGreen = true;
    
    static private int count = 0;
    public IToken nextToken() 
    {
        // Trace.trace();
        // fTokenOffset= fOffset;
        
        if (annotations != null && annotations.size() > 0) {
            if (lastIndex < annotations.size()) {
                IToken t;
                if (isGreen) {
                    t = new Token (green);
                } else {                    
                    t = new Token (red);
                }
                isGreen = !isGreen;
                AnnotationObject annot = (AnnotationObject) annotations.get(lastIndex);
                fTokenOffset = annot.begin;
                fTokenLength = annot.end - annot.begin;
                ++lastIndex;
                
                // Trace.trace("NEXT Token start=" + annot.begin + " ; length=" + (annot.end - annot.begin));

                return t;
            }
        }
//        if (++count < 10) {
//            IToken t;
//            if (isGreen) {
//                t = new Token (green);
//            } else {                    
//                t = new Token (red);
//            }
//            return t;
//        }
        return Token.EOF;
    }

    public int getTokenOffset() {
        Trace.trace("fTokenOffset: " + fTokenOffset);
        return fTokenOffset;
    }

    public int getTokenLength() {
        // Trace.trace("fTokenLength: " + fTokenLength);
        return fTokenLength;
//        if (fOffset < fRangeEnd)
//            return fOffset - fTokenOffset;
//        return fRangeEnd - fTokenOffset;
    }

}
