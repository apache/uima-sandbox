package org.apache.uima.casviewer.ui.internal.document;

import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

public class CustomSourceViewerDecorationSupport extends
		SourceViewerDecorationSupport {

	public CustomSourceViewerDecorationSupport(ISourceViewer sourceViewer, IOverviewRuler overviewRuler, IAnnotationAccess annotationAccess, ISharedTextColors sharedTextColors) {
		super(sourceViewer,overviewRuler,annotationAccess,sharedTextColors);
	}
	
	protected AnnotationPainter createAnnotationPainter() {
        Trace.trace();
		AnnotationPainter painter = super.createAnnotationPainter();

//		painter.addAnnotationType("annotationperformance.wordHighlight",AnnotationPreference.STYLE_BOX);
//		painter.setAnnotationTypeColor("annotationperformance.wordHighlight", new Color(null,255,0,0));

		return painter;
	}

}
