package org.apache.uima.casviewer.ui.internal.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.casviewer.core.internal.AnnotationObject;
import org.apache.uima.casviewer.ui.internal.style.TypeStyle;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.texteditor.DefaultMarkerAnnotationAccess;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

public class AnnotatedTextComposite {
    
    static boolean debug_show_performance = false;
    
    static final String VISUAL_DECO_TYPE_ADDED      = "DECO_TYPE_ADDED";
    static final String VISUAL_DECO_TYPE_DELETED    = "DECO_TYPE_DELETED";
    static final String VISUAL_DECO_TYPE_CHANGED    = "DECO_TYPE_CHANGED";
    static final String VISUAL_DECO_TYPE_SIMILAR    = "DECO_TYPE_SIMILAR";
    static final String VISUAL_DECO_TYPE_MATCHED    = "DECO_TYPE_MATCHED";
    
    protected ScrolledComposite scrolledComposite;
    
    private StyledText              styledText;
    protected TypeSystemStyle       typeSystemStyle;
    private IStatusLineManager      statusLine;


    /** The width of the vertical ruler. */
    protected final static int VERTICAL_RULER_WIDTH= 10;
    protected ISourceViewer   sourceViewer;
    protected Document        documentObject;
    protected IAnnotationModel model = new AnnotationModel();
    protected IAnnotationAccess fAnnotationAccess;
    protected TextPresentation textPresentation = new TextPresentation();
    private VerticalRuler   verticalRuler;

    private CasViewerSourceViewerConfiguration casViewerSourceViewerConfiguration;
    private SourceViewerDecorationSupport fSourceViewerDecorationSupport;

    // For OverviewRuler
    private Color color= new Color(Display.getCurrent(), new RGB(128,0,0));
    private String annotationType = "annotationperformance.wordHighlight";
    protected IOverviewRuler fOverviewRuler;
    private Color overViewerRulerTypeColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);

    
    /*************************************************************************/
    /*          The following methods can be override by subclass            */
    /*************************************************************************/
    
    /**
     * Adds the list of annotation types and their drawing color whose
     * annotations should be painted by this painter using the given drawing strategy.
     * If the annotation type is already in this list, the old drawing strategy gets replaced.
     *
     */
    protected void addAnnotationTypesAndColorsToPainter () {        
    }
   
    /**
     * Set the painter's type (kind of paintings) to the specified annotation
     * 
     * @param annot
     * @param annotation
     * @return void
     */
    protected void setPainterTypeToAnnotation (AnnotationObject annot, VisualDecoration annotation) {        
    }
    
    /**
     * Clears the list of annotation types whose annotations are
     * painted by this painter.
     */
    protected void removeAllAnnotationTypesFromPainter () {        
    }
    
    /*************************************************************************/

    protected AnnotatedTextComposite () {        
    }
    
    static public AnnotatedTextComposite createInstance (Composite parent, 
                    TypeSystemStyle tsStyle, boolean enableVisualDecorationPainter) {
        AnnotatedTextComposite a = new AnnotatedTextComposite();
        a.typeSystemStyle = tsStyle;
//        a.enableDecorationPainter = enableVisualDecorationPainter;
        a.scrolledComposite = a.createComposite(parent);
        
        return a;
    }
    
    /*************************************************************************/

    // Set by AnnotatedTextSectionPart.setInput
    public void setTypeSystemStyle (TypeSystemStyle tsStyle) {
        typeSystemStyle = tsStyle;
        if (casViewerSourceViewerConfiguration != null) {
          casViewerSourceViewerConfiguration.setTypeSystemStyle(tsStyle);
        }
    }

    public void setStatusLineManager (IStatusLineManager statusLine)
    {
        this.statusLine = statusLine;
    }

    /**
     * Get the visual decoration type (e.g., under-line, upper-line)
     * of the specified annotation status
     */
    public String getVisualDecorationType (int status) {
        if (status == AnnotationObject.DIFF_STATUS_MATCH) {
            return VISUAL_DECO_TYPE_MATCHED;
        } else if (status == AnnotationObject.DIFF_STATUS_ADDED) {            
            return VISUAL_DECO_TYPE_ADDED;
        } else if (status == AnnotationObject.DIFF_STATUS_DELETED) {
            return VISUAL_DECO_TYPE_DELETED;
        }
        return null;
    }

    public void showMoreAnnotations (List<AnnotationObject> annotationList) {
        // Create StyleRange for the AnnotationObject(s)
        List<StyleRange> styleRangeList = new ArrayList<StyleRange>(); // List of StyleRanges to be shown
        for (AnnotationObject annot: annotationList) {
            // Get color from Type instead of using the one set with the annotation
            TypeStyle style = typeSystemStyle.getTypeStyle(annot.getTypeName());
            // Create new StyleRange
            styleRangeList.add(new StyleRange(annot.begin, annot.end - annot.begin, 
                    style.getForeground(), style.getBackground()));
        }
        if (styleRangeList.size() > 0) {
            // Merge StyleRanges
            textPresentation.mergeStyleRanges((StyleRange[]) styleRangeList
                    .toArray(new StyleRange[styleRangeList.size()]));  
            updateAnnotationMarkersInModel(annotationList, false); // Don't delete OLD annotations
        }        

        // Save "Top" position of StyledText
        int top = ((SourceViewer) sourceViewer).getTextWidget().getTopPixel();
        
        sourceViewer.getTextWidget().setRedraw(false);            
        sourceViewer.setDocument(documentObject, model); // required, otherwise no redraw 
        sourceViewer.changeTextPresentation(textPresentation, false);
        
        // Restore "Top" position of StyledText
        ((SourceViewer) sourceViewer).getTextWidget().setTopPixel(top);
        sourceViewer.getTextWidget().setRedraw(true);       
    }

    public void showAnnotations (List<AnnotationObject> annotationList, boolean refreshView) {
        
        long start = System.currentTimeMillis();
        
        // Create list of visible and overlapped annotations
        List<StyleRange> styleRangeList = new ArrayList<StyleRange>(); // List of StyleRanges to be shown
        List<AnnotationObject> visibleAnnotationList = new ArrayList<AnnotationObject>();
        List<AnnotationObject> overlapList = new ArrayList<AnnotationObject>();
        
        int lastOffset = 0; // for checking overlapped annotation
        for (AnnotationObject annot: annotationList) {
            if (annot.show) {                
                if (lastOffset > annot.begin) {
                    // Overlapped Annotation
                    overlapList.add(annot);
                    continue;
                }
                lastOffset = annot.end;
                visibleAnnotationList.add(annot);

                // Get color from Type instead of using the one set with the annotation
                TypeStyle style = typeSystemStyle.getTypeStyle(annot.getTypeName());
                // Create new StyleRange
                styleRangeList.add(new StyleRange(annot.begin, annot.end - annot.begin, 
                        style.getForeground(), style.getBackground()));
            }
        }
        if (debug_show_performance)
            Trace.trace("Perf for visibleAnnotationList-1 " + visibleAnnotationList.size()
                + " annotations : " + (System.currentTimeMillis()-start));
        
        // Check validity of StyleRange
//        lastOffset = 0;
//        for (int i = 0; i < visibleAnnotationList.size(); i ++) {
//            annot = (AnnotationObject) visibleAnnotationList.get(i);
//            if (lastOffset > annot.begin) {
//                Trace.trace("Range error by annotation " + annot.getTypeName() 
//                      + " lastOffset=" + lastOffset  + " begin=" + annot.begin + " end=" + annot.end);    
//            }
//            lastOffset = annot.end;
//        }
        
        if (refreshView) {
            // long startTime = System.currentTimeMillis();
            
//            AnnotationScanner sc = AnnotationScanner.getInstance();
//            sc.setAnnotations(visibleAnnotationList);
            // sourceViewer.setDocument(document, model);
            updateAnnotationMarkersInModel(visibleAnnotationList, true);
            textPresentation.clear();
            if (styleRangeList.size() > 0) {
                textPresentation.replaceStyleRanges((StyleRange[]) styleRangeList
                        .toArray(new StyleRange[styleRangeList.size()]));  
                
                // Is there any "overlapped" range ?
                if (overlapList.size() > 0) {
                    // Create StyleRange of "overlapped" ranges 
                    List<StyleRange> extraStyleRangeList = new ArrayList<StyleRange>();
                    for (AnnotationObject annot: overlapList) {
                        // Get color from Type instead of using the one set with the annotation
                        TypeStyle style = typeSystemStyle.getTypeStyle(annot.getTypeName());
                        extraStyleRangeList.add(new StyleRange(annot.begin, annot.end - annot.begin, 
                                style.getForeground(), style.getBackground()));                        
                    }
                    
                    // Merge StyleRanges
                    textPresentation.mergeStyleRanges((StyleRange[]) extraStyleRangeList
                            .toArray(new StyleRange[extraStyleRangeList.size()]));  
                    updateAnnotationMarkersInModel(overlapList, false); // Don't delete OLD annotations
                }
                
                // Painter Support
                addAnnotationTypesAndColorsToPainter ();
            }
            // Save "Top" position of StyledText
            int top = ((SourceViewer) sourceViewer).getTextWidget().getTopPixel();
            
            sourceViewer.getTextWidget().setRedraw(false);            
            sourceViewer.setDocument(documentObject, model); // required, otherwise no redraw 
            sourceViewer.changeTextPresentation(textPresentation, false);
            
            // Restore "Top" position of StyledText
            ((SourceViewer) sourceViewer).getTextWidget().setTopPixel(top);
            sourceViewer.getTextWidget().setRedraw(true);
            
            if (debug_show_performance)
                Trace.trace("Perf showAnnotationsForView for " + annotationList.size()
                   + " annotations : " + (System.currentTimeMillis()-start));
        }
    } // showAnnotations
    
    /*************************************************************************/
 
    /*************************************************************************/

    protected void updateAnnotationMarkersInModel (final List<AnnotationObject> markers, 
            final boolean removeOldAnnotations)
    {
        // Tried wrapping it in an operation
//        IRunnableWithProgress op = new IRunnableWithProgress() {                                               
//            public void run(IProgressMonitor monitor)
//            throws InvocationTargetException, InterruptedException  
//            {
                long start = System.currentTimeMillis();
                // Remove Annotations from "model" and "OverviewRuler"
                // from OLD view
                if (removeOldAnnotations) {
                    removeAllAnnotationsFromCurrentView(false);
                }
                // Add Annotations to "model" and "OverviewRuler"
                Map<Annotation, Position> annotationsToAdd = new HashMap<Annotation, Position>();
                int count = 0;
                for (AnnotationObject annot: markers) {
                    if (annot.end < annot.begin || annot.begin == annot.end) {
                        continue;
                    }
//                  Trace.trace("R#" + i + " : start=" + annot.begin 
//                  + " ; length=" + (annot.end - annot.begin));
                    // Annotation annotation = new Annotation(
                    //      annot.getAnnotationTypeShortName(), false, 
                    //      sourceViewer.getTextWidget().getText(annot.begin, annot.end-1));
                    // Use "DefaultRangeIndicator" instead of "Annotation" to enable Range Indicator
                    // DefaultRangeIndicator annotation = new DefaultRangeIndicator();
                    count++;
                    VisualDecoration annotation = new VisualDecoration(null, false,
                                    sourceViewer.getTextWidget().getText(annot.begin, annot.end-1), 
                                    annot.getTypeName());
                    annotationsToAdd.put(annotation, new Position(annot.begin, annot.end - annot.begin));
                    
                    // Painter Support
                    setPainterTypeToAnnotation (annot, annotation);
                    
                    // Add annotation to OverviewRuler
                    if (fOverviewRuler != null) {
                        fOverviewRuler.setAnnotationTypeColor(annot.getAnnotationTypeShortName(), 
                                overViewerRulerTypeColor);
                        fOverviewRuler.setAnnotationTypeLayer(annot.getAnnotationTypeShortName(), 0);
                        fOverviewRuler.addAnnotationType(annot.getAnnotationTypeShortName());
                    }
                }
                ((IAnnotationModelExtension) model).replaceAnnotations(null, annotationsToAdd);
                // fOverviewRuler.update();
                // Trace.trace("markers.size(): " + markers.size());
                if (debug_show_performance)
                    Trace.err("Performance updateAnnotationMarkersInModel for " + markers.size()
                        + " annotations: " + (System.currentTimeMillis() - start) + " ms");
//            }
//        };
//
//        try {
//            op.run(new NullProgressMonitor());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    
    
    /**
     *  Show 1 Annotation when user select a FS in FSIndexSectionPart
     * 
     * @param fs
     * @param color
     * @param show
     * @return void
     */
    public void show_1_Annotation (FeatureStructure fs, boolean show)
    {
        StyleRange sr = null;
        if ( show ) {
            // Create StyleRange from FeatureStructure
            Type type = fs.getType();
            Feature beginF = type.getFeatureByBaseName("begin");
            Feature endF   = type.getFeatureByBaseName("end");
            int begin = fs.getIntValue(beginF);
            int end   = fs.getIntValue(endF);
            TypeStyle c = typeSystemStyle.getTypeStyle(type.getName());
            sr = new StyleRange(begin, end - begin, c.fgColor, c.bgColor);                        
        }
        styledText.setRedraw(false);
        styledText.setStyleRange((StyleRange) sr);
        styledText.setRedraw(true);
    }
    
    public void removeAllAnnotationsFromCurrentView (boolean clearStyleRange) {
        // Remove ALL annotations from OverViewer Ruler
        // showAnnotationMarkers(null);
        if (fOverviewRuler != null) {
            Iterator annotationIterator = model.getAnnotationIterator();
            while (annotationIterator.hasNext()) {
                Annotation annotation = (Annotation) annotationIterator.next();
                fOverviewRuler.setAnnotationTypeColor(annotation.getType(), null);
                fOverviewRuler.setAnnotationTypeLayer(annotation.getType(), 0);
                fOverviewRuler.removeAnnotationType(annotation.getType());
            }
        }

        // Painter Support
        removeAllAnnotationTypesFromPainter ();

        // Better perf than remove 1-by-1
        ((IAnnotationModelExtension) model).removeAllAnnotations();
        
        if (clearStyleRange) {
            styledText.setRedraw(false);
            styledText.setStyleRange(null);
            // Required to remove AnnotationPaint drawing from StyledText
            sourceViewer.setDocument(documentObject, model); // required, otherwise no redraw 
            styledText.setRedraw(true);
        }
    } // removeAllAnnotationsFromCurrentView
    
    // Merge with removeAllAnnotationsFromCurrentView ?
    public void hideAllAnnotationsForView (boolean refreshSourceViewer)
    {                
//        AnnotationScanner sc = AnnotationScanner.getInstance();
//        sc.setAnnotations(null);
        
        // Remove ALL annotations from OverViewer Ruler
        // showAnnotationMarkers(null);
        if (fOverviewRuler != null) {
            Iterator annotationIterator = model.getAnnotationIterator();
            while (annotationIterator.hasNext()) {
                Annotation annotation = (Annotation) annotationIterator.next();
                // fOverviewRuler.setAnnotationTypeColor(annotation.getType(), null);
                // fOverviewRuler.setAnnotationTypeLayer(annotation.getType(), 0);
                fOverviewRuler.removeAnnotationType(annotation.getType());
            }
        }
        // Better perf than remove 1-by-1
        ((IAnnotationModelExtension) model).removeAllAnnotations();
        
        textPresentation.clear();
        // TODO Tong Try to improve Perf
        if (refreshSourceViewer) {
            // styledText.setStyleRange(null);
            sourceViewer.setDocument(documentObject, model); // required, otherwise no redraw 
            Trace.err("NOT Calling sourceViewer.changeTextPresentation(textPresentation, true)");
//            sourceViewer.changeTextPresentation(textPresentation, true);   
        }
    }

    /*************************************************************************/   
    
    /**
     * Called by AnnotatedTextSectionPart to set GridData.
     */
    public ScrolledComposite getScrolledComposite () {
        return scrolledComposite;
    }
    
    /**
     * Called by AnnotatedTextSectionPart 
     */
    public ISourceViewer getSourceViewer() {
        return sourceViewer;
    }
    
//    public IAnnotationModel getAnnotationModel() {
//        return model;
//    }
    

//    public void enableAnnotationPainter (boolean enable) {
//        enableDecorationPainter = enable;
//    }

    protected ScrolledComposite createComposite (Composite parent) {
        
        ScrolledComposite composite = new ScrolledComposite(parent, 
                SWT.V_SCROLL|SWT.BORDER);
        composite.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);;        
//        scrolledComposite.addControlListener(new ControlAdapter() {
//            public void controlResized(ControlEvent e) {
//                Rectangle r = scrolledClient.getClientArea();
//                // scrolledClient.setMinSize(((SourceViewer)sourceViewer).getControl().computeSize(r.width, SWT.DEFAULT));
//                r.width -= verticalRuler.getWidth() + 32;
//                ((SourceViewer)sourceViewer).getTextWidget().computeSize(r.width, SWT.DEFAULT);
//            }
//        });

        composite.setExpandHorizontal(true);
        composite.setExpandVertical(true);
        
        /*********************************************************************/    
        
        verticalRuler = new VerticalRuler(8);
        sourceViewer = createSourceViewer (composite, verticalRuler,
                SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);

        styledText = ((SourceViewer)sourceViewer).getTextWidget();
        styledText.setLayoutData(new GridData(GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING));

        // Create Document object and set it to SourceViewer
        documentObject = new Document("(No Text)");
        sourceViewer.setDocument(documentObject);
        
        composite.setContent(((SourceViewer)sourceViewer).getControl());

        return composite;
    }

    /*************************************************************************/


    protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {

        fAnnotationAccess= getAnnotationAccess(); // needed by MyAnnotationPainter
//        fOverviewRuler = createOverviewRuler(getSharedColors());
        casViewerSourceViewerConfiguration = new CasViewerSourceViewerConfiguration(typeSystemStyle);
        ISourceViewer viewer= new CasSourceViewer(parent, ruler, getOverviewRuler(), 
                true, styles); // true: show Overview Ruler
        viewer.configure(casViewerSourceViewerConfiguration);
        viewer.setRangeIndicator(new DefaultRangeIndicator());
        
        // ensure decoration support has been created and configured.
//        getSourceViewerDecorationSupport(viewer);
//        annotationPainter = ((CustomSourceViewerDecorationSupport)fSourceViewerDecorationSupport)
//                    .createAnnotationPainter();
        
//        annotationPainter = new MyAnnotationPainter(viewer, fAnnotationAccess);
//        ((SourceViewer) viewer).addPainter(annotationPainter);


//        if (enableDecorationPainter) {
//            // ensure decoration support has been created and configured.
//            getSourceViewerDecorationSupport(viewer);
//            annotationPainter = new VisualAnnotationPainter(viewer, fAnnotationAccess);
//            ((SourceViewer) viewer).addPainter(annotationPainter);
//            // Trace.err("LineSpacing: " + viewer.getTextWidget().getLineSpacing());
//            viewer.getTextWidget().setLineSpacing(10);
//        }
        
        // Testing hot keys
//        ((CasSourceViewer)viewer).prependVerifyKeyListener(new VerifyKeyListener() {
//            public void verifyKey(VerifyEvent event) {
//                handleVerifyKeyPressed(event);               
//            }
//        });
                
        return viewer;
    }
    

    // event.keyCode = SWT.F2, ... for function key
//    private void handleVerifyKeyPressed(VerifyEvent event) {
//        Trace.err("Key pressed: stateMask=" + event.stateMask 
//                + " ; char=[" + event.toString() + "]");
//        // Trace.err("char: " + String.(event.character));
//        Trace.err("keycode: " + Integer.toHexString(event.keyCode));
//        if (!event.doit)
//            return;
//
//        if (event.stateMask != SWT.MOD1)
//            return;
//
//        switch (event.character) {
//        case ' ':
//            Trace.err("SHOW_OUTLINE");
//            // ((SourceViewer) sourceViewer).doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
//            ((CasSourceViewer) sourceViewer).doOperation(CasSourceViewer.SHOW_OUTLINE);
//            event.doit= true;
//            break;
//
//            // CTRL-Z
//        case 'z' - 'a' + 1:
//
//            event.doit= false;
//            break;
//        }
//        
//    }

    
    protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
        Trace.trace();
//        Iterator e= fAnnotationPreferences.getAnnotationPreferences().iterator();
//        while (e.hasNext())
//            support.setAnnotationPreference((AnnotationPreference) e.next());
//
//        support.setCursorLinePainterPreferenceKeys(CURRENT_LINE, CURRENT_LINE_COLOR);
//        support.setMarginPainterPreferenceKeys(PRINT_MARGIN, PRINT_MARGIN_COLOR, PRINT_MARGIN_COLUMN);
//        support.setSymbolicFontName(getFontPropertyPreferenceKey());
    }
    
    /**
     * Returns the source viewer decoration support.
     *
     * @param viewer the viewer for which to return a decoration support
     * @return the source viewer decoration support
     */
    protected SourceViewerDecorationSupport getSourceViewerDecorationSupport(ISourceViewer viewer) {
        if (fSourceViewerDecorationSupport == null) {
            Trace.trace();
//            fSourceViewerDecorationSupport= new SourceViewerDecorationSupport(viewer, getOverviewRuler(), getAnnotationAccess(), getSharedColors());
//            configureSourceViewerDecorationSupport(fSourceViewerDecorationSupport);
            fSourceViewerDecorationSupport= new CustomSourceViewerDecorationSupport(viewer, 
                    getOverviewRuler(), getAnnotationAccess(), getSharedColors());
            configureSourceViewerDecorationSupport(fSourceViewerDecorationSupport);
        }
        return fSourceViewerDecorationSupport;
    }

    
    /**
     * Creates the annotation access for this editor.
     *
     * @return the created annotation access
     */
    protected IAnnotationAccess createAnnotationAccess() {
        return new DefaultMarkerAnnotationAccess();
    }

    
    protected IOverviewRuler createOverviewRuler(ISharedTextColors sharedColors) {
        IOverviewRuler ruler= new OverviewRuler(getAnnotationAccess(), VERTICAL_RULER_WIDTH, sharedColors);
//        Iterator e= fAnnotationPreferences.getAnnotationPreferences().iterator();
//        while (e.hasNext()) {
//            AnnotationPreference preference= (AnnotationPreference) e.next();
//            if (preference.contributesToHeader())
//                ruler.addHeaderAnnotationType(preference.getAnnotationType());
//        }
        
//        ruler.addAnnotationType("org.eclipse.ui.workbench.texteditor.quickdiffChange"); //$NON-NLS-1$
//        ruler.addAnnotationType("org.eclipse.ui.workbench.texteditor.quickdiffAddition"); //$NON-NLS-1$
//        ruler.addAnnotationType("org.eclipse.ui.workbench.texteditor.quickdiffDeletion"); //$NON-NLS-1$
//        ruler.update();
        
        if (ruler != null) {
            ruler.setAnnotationTypeColor(annotationType, color);
            ruler.setAnnotationTypeLayer(annotationType, 0);
            ruler.addAnnotationType(annotationType);
//            if (update)
//                fOverviewRuler.update();
        }
        
        return ruler;
    }
    
    /**
     * Returns the annotation access.
     *
     * @return the annotation access
     * 
     * Note: Needed by OverviewRuler, MyAnnotationPainter, and CustomSourceViewerDecorationSupport
     */
    protected IAnnotationAccess getAnnotationAccess() {
        if (fAnnotationAccess == null)
            fAnnotationAccess= createAnnotationAccess();
        return fAnnotationAccess;
    }

    /**
     * Returns the overview ruler.
     *
     * @return the overview ruler
     */
    protected IOverviewRuler getOverviewRuler() {
        if (fOverviewRuler == null) {
//            fOverviewRuler= createOverviewRuler(getSharedColors());
        }
        return fOverviewRuler;
    }
    
    protected ISharedTextColors getSharedColors() {
        return null; // colorManager;
    }
    
    /*************************************************************************/   
    
    public Annotation gotoAnnotation(boolean forward) {
        ITextSelection selection= (ITextSelection) sourceViewer.getSelectionProvider().getSelection();
        Position position= new Position(0, 0);
        Annotation annotation= findAnnotation(selection.getOffset(), selection.getLength(), forward, position);
//        setStatusLineErrorMessage(null);
//        setStatusLineMessage(null);
        
        if (annotation != null) {
            StyledText widget= sourceViewer.getTextWidget();
            widget.setRedraw(false);
            // selectAndReveal(position.getOffset(), position.getLength());
            // setStatusLineMessage(annotation.getText());
            sourceViewer.setSelectedRange(position.getOffset(), position.getLength());
            sourceViewer.revealRange(position.getOffset(), position.getLength());
            sourceViewer.getTextWidget().setSelection(position.getOffset(), 
                    position.getOffset()+position.getLength());
            widget.setRedraw(true);
            if (statusLine != null) {
                statusLine.setMessage(annotation.getText());
            }
            
            // Show selected annotaions in Annotation Section
//            showSelectedAnnotations(position.getOffset());
        }
        return annotation;
    }
    
    protected Annotation findAnnotation(final int offset, final int length, boolean forward, Position annotationPosition) {

        Annotation nextAnnotation = null;
        Position nextAnnotationPosition = null;
        Annotation containingAnnotation = null;
        Position containingAnnotationPosition = null;
        boolean currentAnnotation = false;

        // IDocument document= getDocumentProvider().getDocument(getEditorInput());
        int endOfDocument = documentObject.getLength();
        int distance = Integer.MAX_VALUE;

        // IAnnotationModel model= getDocumentProvider().getAnnotationModel(getEditorInput());
        Iterator e = model.getAnnotationIterator();
        while (e.hasNext()) {
            Annotation a = (Annotation) e.next();
//            if (!isNavigationTarget(a))
//                continue;

            Position p = model.getPosition(a);
            if (p == null)
                continue;

            if (forward && p.offset == offset || !forward && p.offset + p.getLength() == offset + length) {// || p.includes(offset)) {
                if (containingAnnotation == null || (forward && p.length >= containingAnnotationPosition.length || !forward && p.length >= containingAnnotationPosition.length)) {
                    containingAnnotation = a;
                    containingAnnotationPosition = p;
                    currentAnnotation = p.length == length;
                }
            } else {
                int currentDistance = 0;

                if (forward) {
                    currentDistance = p.getOffset() - offset;
                    if (currentDistance < 0)
                        currentDistance = endOfDocument + currentDistance;

                    if (currentDistance < distance || currentDistance == distance && p.length < nextAnnotationPosition.length) {
                        distance = currentDistance;
                        nextAnnotation = a;
                        nextAnnotationPosition = p;
                    }
                } else {
                    currentDistance = offset + length - (p.getOffset() + p.length);
                    if (currentDistance < 0)
                        currentDistance = endOfDocument + currentDistance;

                    if (currentDistance < distance || currentDistance == distance && p.length < nextAnnotationPosition.length) {
                        distance = currentDistance;
                        nextAnnotation = a;
                        nextAnnotationPosition = p;
                    }
                }
            }
        }
        if (containingAnnotationPosition != null && (!currentAnnotation || nextAnnotation == null)) {
            annotationPosition.setOffset(containingAnnotationPosition.getOffset());
            annotationPosition.setLength(containingAnnotationPosition.getLength());
            return containingAnnotation;
        }
        if (nextAnnotationPosition != null) {
            annotationPosition.setOffset(nextAnnotationPosition.getOffset());
            annotationPosition.setLength(nextAnnotationPosition.getLength());
        }

        return nextAnnotation;
    }

    private int currOffset = 0;
    private int currLength = 0;
    
    protected void gotoPrevious ()
    {
        int newLength;
        Point selectedRange = sourceViewer.getSelectedRange();
        currOffset= selectedRange.x;
        currLength= selectedRange.y;
         
        IRegion lineInfo = null;
        try {
            lineInfo = documentObject.getLineInformationOfOffset(currOffset);
        } catch (BadLocationException e) {
            e.printStackTrace();
            return;
        }
        int rangeStart = lineInfo.getOffset();
        int rangeEnd   = rangeStart + lineInfo.getLength();
        // Trace.err("rangeStart: " + rangeStart + " ; rangeEnd: " + rangeEnd);
        
        ArrayList<Annotation> allAnnotations = new ArrayList<Annotation>();
        ArrayList<Position> allPositions   = new ArrayList<Position>();
        int bestOffset           = Integer.MAX_VALUE;
        newLength = currLength;
        Iterator annotationIterator = model.getAnnotationIterator();
        while (annotationIterator.hasNext()) {
            Annotation annot= (Annotation) annotationIterator.next();
            Position pos= model.getPosition(annot);
            if (pos != null && isInside(pos.offset, rangeStart, rangeEnd)) { // inside our range?
                allAnnotations.add(annot);
                allPositions.add(pos);
                bestOffset= processAnnotation(annot, pos, currOffset, bestOffset);
                newLength = pos.length;
            }
        }
        
        int newOffset;
        ArrayList<Annotation> resultingAnnotations= new ArrayList<Annotation>(20);
        
        if (bestOffset == Integer.MAX_VALUE) {
            newOffset =  currOffset;
        } else {
            int i=0;
            for (Position pos: allPositions) {
                if (isInside(bestOffset, pos.offset, pos.offset + pos.length)) {
                    resultingAnnotations.add(allAnnotations.get(i));
                }
                ++i;
            }
            newOffset = bestOffset;
        }

        if (newOffset != currOffset) {
            sourceViewer.setSelectedRange(newOffset, 0);
            sourceViewer.revealRange(newOffset, 0);
            // sourceViewer.setTextColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED), newOffset, 10, false);
            sourceViewer.getTextWidget().setSelection(newOffset, newOffset+newLength);
        }
    }
    
    private static int processAnnotation(Annotation annot, Position pos, int invocationLocation, int bestOffset) {
        int posBegin= pos.offset;
        int posEnd= posBegin + pos.length;
        if (isInside(invocationLocation, posBegin, posEnd)) { // covers invocation location?
            return invocationLocation;
        } else if (bestOffset != invocationLocation) {
            int newClosestPosition= computeBestOffset(posBegin, invocationLocation, bestOffset);
            if (newClosestPosition != -1) { 
                if (newClosestPosition != bestOffset) { // new best
                        return newClosestPosition;
                }
            }
        }
        return bestOffset;
    }


    private static boolean isInside(int offset, int start, int end) {
        return offset == start || (offset > start && offset < end); // make sure to handle 0-length ranges
    }

    private static int computeBestOffset(int newOffset, int invocationLocation, int bestOffset) {
        if (newOffset <= invocationLocation) {
            if (bestOffset > invocationLocation) {
                return newOffset; // closest was on the right, prefer on the left
            } else if (bestOffset <= newOffset) {
                return newOffset; // we are closer or equal
            }
            return -1; // further away
        }

        if (newOffset <= bestOffset)
            return newOffset; // we are closer or equal

        return -1; // further away
    }

    /*************************************************************************/

    /**
     * Returns the offset of the given source viewer's document that corresponds
     * to the given widget offset or <code>-1</code> if there is no such offset.
     *
     * @param viewer the source viewer
     * @param widgetOffset the widget offset
     * @return the corresponding offset in the source viewer's document or <code>-1</code>
     */
    protected final static int widgetOffset2ModelOffset(ISourceViewer viewer, int widgetOffset) {
        if (viewer instanceof ITextViewerExtension5) {
            ITextViewerExtension5 extension= (ITextViewerExtension5) viewer;
            return extension.widgetOffset2ModelOffset(widgetOffset);
        }
        return widgetOffset + viewer.getVisibleRegion().getOffset();
    }

    public String getCursorPosition() {
        String fErrorLabel = "Error in getCursorPosition";
        if (sourceViewer == null)
            return fErrorLabel;

        StyledText styledText= sourceViewer.getTextWidget();
        int caret = widgetOffset2ModelOffset(sourceViewer, styledText.getCaretOffset());
        IDocument document= sourceViewer.getDocument();

        if (document == null)
            return fErrorLabel;

        try {
            int line= document.getLineOfOffset(caret);

            int lineOffset= document.getLineOffset(line);
            int tabWidth= styledText.getTabs();
            int column= 0;
            for (int i= lineOffset; i < caret; i++)
                if ('\t' == document.getChar(i))
                    column += tabWidth - (tabWidth == 0 ? 0 : column % tabWidth);
                else
                    column++;

//            fLineLabel.fValue= line + 1;
//            fColumnLabel.fValue= column + 1;
//            return NLSUtility.format(fPositionLabelPattern, fPositionLabelPatternArguments);
            return "Line: " + (line+1) + "  Column: " + (column+1) + "  Offset: " + caret;
        } catch (BadLocationException x) {
            return fErrorLabel;
        }
    }

}
