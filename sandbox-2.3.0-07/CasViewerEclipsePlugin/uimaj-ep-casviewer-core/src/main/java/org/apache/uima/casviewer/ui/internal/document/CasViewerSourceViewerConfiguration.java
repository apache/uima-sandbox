package org.apache.uima.casviewer.ui.internal.document;

import java.util.Iterator;
import java.util.List;

import org.apache.uima.casviewer.ui.internal.hover.AnnotationHover;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class CasViewerSourceViewerConfiguration extends SourceViewerConfiguration 
{
    protected TypeSystemStyle       typeSystemStyle;
    
    protected AnnotationHover fTextHover;

    public CasViewerSourceViewerConfiguration(TypeSystemStyle tsStyle) {
        super();
        if (tsStyle != null) {
            typeSystemStyle = tsStyle;
        }
    }
    
    public void setTypeSystemStyle (TypeSystemStyle tsStyle) {
        typeSystemStyle = tsStyle;
    }
    
    public IContentAssistant getContentAssistant_(ISourceViewer sourceViewer) {

        ContentAssistant assistant= new ContentAssistant();
//        assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
//        assistant.setContentAssistProcessor(new JavaCompletionProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
//
//
//        assistant.enableAutoActivation(true);
//        assistant.setAutoActivationDelay(500);
//        assistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
//        assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
//        assistant.setContextInformationPopupBackground(getJavaColorProvider().getColor(new RGB(150, 150, 0)));

        return assistant;
    }
    

    /**
     * This is the Hover support for text in editor area.
     * see JavaTextHover for Java editor example or see "JavaSourceHover" for JDT Java editor
     * 
     * see HTMLTextPresenter, DefaultInformationControl
     * TextSourceViewerConfiguration, DefaultAnnotationHover
     */
    
    public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
        if (fTextHover == null) {
            fTextHover = new AnnotationHover(sourceViewer, typeSystemStyle);
        }
        return fTextHover;
    }
    
    public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
        // Trace.trace();
        return new DefaultAnnotationHover() {
            /**
             * Hook method to formats the given messages.
             * <p>
             * Subclasses can change this to create a different
             * format like HTML.
             * </p>
             * 
             * @param messages the messages to format 
             * @return the formatted message
             */
            protected String formatMultipleMessages(List messages, int lineNumber) {
                StringBuffer buffer= new StringBuffer();
                buffer.append("Multiple annotations at line#" + lineNumber); //$NON-NLS-1$
                
                Iterator e= messages.iterator();
                while (e.hasNext()) {
                    buffer.append('\n');
                    String listItemText= (String) e.next();
                    buffer.append(listItemText); //$NON-NLS-1$
                }
                return buffer.toString();
            }
        };
    }
    
    public IAnnotationHover getOverviewRulerAnnotationHover(ISourceViewer sourceViewer) {
        return new DefaultAnnotationHover() {
            /**
             * Hook method to formats the given messages.
             * <p>
             * Subclasses can change this to create a different
             * format like HTML.
             * </p>
             * 
             * @param messages the messages to format 
             * @return the formatted message
             */
            protected String formatMultipleMessages(List messages, int lineNumber) {
                StringBuffer buffer= new StringBuffer();
                buffer.append("Multiple annotations at line#" + lineNumber); //$NON-NLS-1$
                
                Iterator e= messages.iterator();
                while (e.hasNext()) {
                    buffer.append('\n');
                    String listItemText= (String) e.next();
                    buffer.append(listItemText); //$NON-NLS-1$
                }
                return buffer.toString();
            }
        };
    }
    
    /**
     * Returns the presentation reconciler ready to be used with the given source viewer.
     * This implementation always returns <code>null</code>.
     *
     * @param sourceViewer the source viewer
     * @return the presentation reconciler or <code>null</code> if presentation reconciling should not be supported
     */
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler= new PresentationReconciler();
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer (new AnnotationScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

        return reconciler;
//        return null;
    }
    

    /**
     * Returns the type system presenter control creator. The creator is a factory creating
     * type system presenter controls for the given source viewer. 
     * This implementation always returns a creator
     * for <code>TypeSystemInfoControl</code> instances.
     *
     * @param sourceViewer the source viewer to be configured by this configuration
     * @param commandId the ID of the command that opens this control
     * @return an information control creator
     */
//    private IInformationControlCreator getTypeSystemPresenterControlCreator(final ISourceViewer sourceViewer, final String commandId) {
//        return new IInformationControlCreator() {
//            public IInformationControl createInformationControl(Shell parent) 
//            {
//                if (sourceViewer == null) {
//                    Trace.trace("sourceViewer == null");
//                } else {
//                    Trace.trace("sourceViewer != null");
//                }
//                // Trace.err();
//                int shellStyle= SWT.RESIZE;
//                int treeStyle= SWT.V_SCROLL | SWT.H_SCROLL;
//                return new TypeSystemInfoControl(sourceViewer, parent, shellStyle, treeStyle, commandId);
//            }
//        };
//    }

    /**
     * Returns the type system presenter which will determine and shown
     * information requested for the current cursor position.
     *
     * @param sourceViewer the source viewer to be configured by this configuration
     * @return an information presenter
     * 
     * Called from CASSourceViewer.configure
     */
    public IInformationPresenter getTypeSystemPresenter(final ISourceViewer sourceViewer) {
//        Trace.trace();
//        InformationPresenter presenter;
//        presenter= new InformationPresenter(getTypeSystemPresenterControlCreator(sourceViewer, IJavaEditorActionDefinitionIds.SHOW_OUTLINE));
//        presenter.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
//        presenter.setAnchor(AbstractInformationControlManager.ANCHOR_BOTTOM);
//        
//        IInformationProvider provider= new TypeSystemProvider ((ICASSourceViewer) sourceViewer);
//        presenter.setInformationProvider(provider, IDocument.DEFAULT_CONTENT_TYPE);
//        presenter.setSizeConstraints(50, 20, true, false);
//        return presenter;
        return null;
    }

    
}
