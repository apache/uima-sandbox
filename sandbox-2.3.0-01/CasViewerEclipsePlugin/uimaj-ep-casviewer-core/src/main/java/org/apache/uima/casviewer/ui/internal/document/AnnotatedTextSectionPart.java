package org.apache.uima.casviewer.ui.internal.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.casviewer.core.internal.AnnotationObject;
import org.apache.uima.casviewer.core.internal.ICASObjectView;
import org.apache.uima.casviewer.core.internal.UFeatureStructure;
import org.apache.uima.casviewer.ui.internal.Messages;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.casviewer.ui.internal.util.AbstractSectionPart;
import org.apache.uima.casviewer.ui.internal.util.FormSection;
import org.apache.uima.casviewer.ui.internal.util.IObjectSelectionListener;
import org.apache.uima.tools.common.internal.images.ImageLoader;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.texteditor.FindReplaceAction;
import org.eclipse.ui.texteditor.IUpdate;

public class AnnotatedTextSectionPart extends AbstractAnnotatedTextSectionPart 
                  implements MouseListener 
{    
    protected ICASObjectView        casViewObject;
    protected TypeSystemStyle       typeSystemStyle;
    protected CAS                   currentViewCAS = null;
    protected AnnotatedTextComposite annotatedTextComposite;
    protected ISourceViewer         sourceViewer;
    protected IDocument             documentObject;
    protected StyledText            styledTextDoc = null;

    protected LinkedList<AnnotationObject> sortedAnnotationList;

    private boolean                 _fireSelection = true;    

    private IStatusLineManager      statusLine;
    protected int                   currentView = TAB_VIEW_ANNOTATIONS;
    
    private ToolItem            searchNextToolItem;
    private ToolItem            searchPreviousToolItem;    
    private int                 lastModelOffset = 0;
            
    private IWorkbenchPart      workbenchPart;
    private IWorkbenchPartSite  workbenchPartSite;
    
    /***************************   Testing   *********************************/
    boolean enableDecorationPainter = true;
    private Browser webBrowser;
    /*************************************************************************/
    

    protected Map<String, IAction> fGlobalActions = new HashMap<String, IAction>();
    protected List<String> fSelectionActions = new ArrayList<String>();


    protected AnnotatedTextSectionPart (TypeSystemStyle tsStyle, 
            IWorkbenchPart part, IWorkbenchPartSite site, IManagedForm managedForm, Composite parent,
            Section section, int style, String title, String description,
            boolean fireSelection) 
    {
        super(managedForm, parent, section, style, title, description);
        if (tsStyle != null) {
            typeSystemStyle = tsStyle;
        }
        _fireSelection = fireSelection;

        ///////////////////////////////////////////////////////////////////////
        workbenchPart = part;
        workbenchPartSite = site;
        
        // final SectionPart spart = new SectionPart(section);
        managedForm.addPart(this);
        initialize (managedForm);  // Need this code. Otherwise, exception in SectionPart !!!        
    }
      
    static public AnnotatedTextSectionPart createInstance (TypeSystemStyle tsStyle, 
            IWorkbenchPart part, IWorkbenchPartSite site, final IManagedForm managedForm, 
            final Composite parent, int style, String title, String description,
            boolean fireTreeSelection) 
    {
        FormToolkit toolkit = managedForm.getToolkit();
        final Section section = FormSection.createGridDataSection(toolkit, parent,
                style, title, description, 2, 2,
                GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING,
                1, 1);
        section.addExpansionListener(new ExpansionAdapter() {
            public void expansionStateChanged(ExpansionEvent e) {
                //  parent.layout(true);
                managedForm.reflow(true);
                // section.layout(true);
            }
        });        
        
        AnnotatedTextSectionPart sectionPart = new AnnotatedTextSectionPart (tsStyle, 
                part, site, managedForm, parent, 
                section, style, title, description, fireTreeSelection);         
        sectionPart.createContents();
        
        return sectionPart;
    } // createInstance
    
    // Called from CASViewControl
    public void setStatusLineManager (IStatusLineManager statusLine)
    {
        this.statusLine = statusLine;
        annotatedTextComposite.setStatusLineManager(statusLine);
    }
    
    /*************************************************************************/
    
    public IAction getGlobalAction(String id) {
        return (IAction) fGlobalActions.get(id);
    }

    /**
     * Updates the global action with the given id
     * 
     * @param actionId action definition id
     */
    protected void updateAction(String actionId) {
        IAction action= (IAction)fGlobalActions.get(actionId);
        if (action instanceof IUpdate) {
            Trace.err("actionId: " + actionId);
            ((IUpdate) action).update();
        }
    }

    /**
     * Updates selection dependent actions.
     */
    protected void updateSelectionDependentActions() {
      Trace.err();
        while (fSelectionActions.iterator().hasNext()) {
            updateAction((String)fSelectionActions.iterator().next());      
        }
    }

    // text selection listener, used to update selection dependent actions on selection changes
    private ISelectionChangedListener selectionChangedListener =  new ISelectionChangedListener() {
        public void selectionChanged(SelectionChangedEvent event) {
          Trace.err();
            updateSelectionDependentActions();
        }
    };
    
    // updates the find replace action if the document length is > 0
    private ITextListener textListener = new ITextListener() {
        public void textChanged(TextEvent event) {
            // Trace.err();
            IUpdate findReplace = (IUpdate)fGlobalActions.get(ActionFactory.FIND.getId());
            if (findReplace != null) {
                findReplace.update();
            }
        }
    };

    protected void createActions() {
      if (workbenchPartSite == null) {
        return;
      }
      if (workbenchPartSite instanceof IEditorSite) {
        Trace.err();
        IActionBars actionBars = ((IEditorSite) workbenchPartSite).getActionBars();
        ResourceBundle bundle = Messages.getBundle();
        setGlobalAction(actionBars, ActionFactory.FIND.getId(), new FindReplaceAction(bundle, "find_replace_action_", workbenchPart)); //$NON-NLS-1$
        fSelectionActions.add(ActionFactory.FIND.getId());
        actionBars.updateActionBars();
      }
    }
    
    /**
     * Configures an action for key bindings.
     * 
     * @param actionBars action bars for this page
     * @param actionID action definition id
     * @param action associated action
     */
    protected void setGlobalAction(IActionBars actionBars, String actionID, IAction action) {
        fGlobalActions.put(actionID, action);  
        actionBars.setGlobalActionHandler(actionID, action);
    }

    /*************************************************************************/
    
    public Section createContents() 
    {
      Section s;
      s = createContents_OK();
      // s = createContents_Browser();
      // createActions();
      return s;
    }

    public Section createContents_OK() 
    {
        FormToolkit toolkit = managedForm.getToolkit();
        final Section section = getSection();

        ///////////////////////////////////////////////////////////////////////
        
        // Create ToolBar
        Composite sectionToolbarComposite = FormSection.createGridLayoutContainer (toolkit, section,
                2, 0, 0);
        section.setTextClient(sectionToolbarComposite);
        
        // Create "Show Next/Previous Annotation" buttons
        ImageDescriptor descriptor = ImageLoader.getInstance().getImageDescriptor(ImageLoader.ICON_NEXT_ANNOT);
        ImageHyperlink infoNext = AbstractSectionPart.createToolbarItem(toolkit, sectionToolbarComposite, 
                descriptor, "Show Next Annotation", section.getTitleBarGradientBackground());
        infoNext.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent e) {
                // gotoPrevious();
                annotatedTextComposite.gotoAnnotation(true);
            }
        }); 
        descriptor = ImageLoader.getInstance().getImageDescriptor(ImageLoader.ICON_PREV_ANNOT);
        ImageHyperlink info = AbstractSectionPart.createToolbarItem(toolkit, sectionToolbarComposite, 
                descriptor, "Show Previous Annotation", section.getTitleBarGradientBackground());
        info.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent e) {
                // gotoPrevious();
                annotatedTextComposite.gotoAnnotation(false);
            }
        });        
                
        ///////////////////////////////////////////////////////////////////////
        GridLayout layout   = new GridLayout(1, false);
        layout.marginWidth  = 10;
        layout.marginHeight = 10;
        section.setLayout(layout);    
        
        // Create Composite
        Composite sectionClient = FormSection.createGridLayoutContainer(toolkit, section,
                4, 0, 0);        
        GridData gd1 = new GridData(GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING);
        gd1.grabExcessVerticalSpace   = true;
        gd1.grabExcessHorizontalSpace = true;
        sectionClient.setLayoutData(gd1);
        section.setClient(sectionClient);   
        
        // Create Search field (needs 4 columns)
        createSearchField(sectionClient, toolkit);
       
        /*********************************************************************/     
        
        return section;
    } // createContents_OK

    /**
     * Set what class of AnnotatedTextComposite to used
     * 
     * Note: Called from CASViewControl 
     */
    public void setAnnotatedTextComposite (AnnotatedTextComposite textComposite) {
        annotatedTextComposite = textComposite;
       
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledComposite scrolledClient = annotatedTextComposite.getScrolledComposite();
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 4;
        scrolledClient.setLayoutData(gd);
        toolkit.paintBordersFor(scrolledClient);        
        
        sourceViewer = annotatedTextComposite.getSourceViewer();        
        // Why need ?
        toolkit.adapt(((SourceViewer)sourceViewer).getControl(), false, false);
        
        ((IEditorSite) workbenchPartSite).setSelectionProvider((SourceViewer)sourceViewer);       
        sourceViewer.getSelectionProvider().addSelectionChangedListener(selectionChangedListener);
        sourceViewer.addTextListener(textListener);
 
        styledTextDoc = ((SourceViewer)sourceViewer).getTextWidget();
        styledTextDoc.addMouseListener(this);
        
        documentObject = sourceViewer.getDocument();
        
   //     annotatedTextComposite.enableAnnotationPainter(false);
        
    } // createContents_OK

    private void createSearchField (Composite parent, FormToolkit toolkit) {
        Label label = toolkit.createLabel(parent, "Search text: ");
        label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
        
        final Text text = toolkit.createText(parent, "", SWT.SEARCH | SWT.CANCEL);
        GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
        gd1.grabExcessHorizontalSpace = true;
        text.setLayoutData(gd1);
        
        ToolBar toolBar = new ToolBar (parent, SWT.FLAT);
        searchNextToolItem = new ToolItem (toolBar, SWT.PUSH);
        ImageDescriptor descriptor = ImageLoader.getInstance().getImageDescriptor(ImageLoader.ICON_SEARCH_NEXT);
        searchNextToolItem.setImage(descriptor.createImage());
        searchNextToolItem.setToolTipText("Show Next Match");
        searchNextToolItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                // Find Next
                lastModelOffset = ((CasSourceViewer) sourceViewer).findAndSelect(sourceViewer.getTextWidget().getCaretOffset(), 
                        text.getText().trim(), true);
                if (lastModelOffset == -1) {
                    // String Not Found
                    lastModelOffset = 0;
                }
            }
        });
        
        searchPreviousToolItem = new ToolItem (toolBar, SWT.PUSH);
        descriptor = ImageLoader.getInstance().getImageDescriptor(ImageLoader.ICON_SEARCH_PREV);
        searchPreviousToolItem.setImage(descriptor.createImage());
        searchPreviousToolItem.setToolTipText("Show Previous Match");
        searchPreviousToolItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                // Find Previous
                Point selectedRange = sourceViewer.getSelectedRange();
                if (selectedRange != null) {
                    lastModelOffset = selectedRange.x;
                } else {
                    lastModelOffset = sourceViewer.getTextWidget().getCaretOffset();
                }
                lastModelOffset = ((CasSourceViewer) sourceViewer).findAndSelect(lastModelOffset, 
                        text.getText().trim(), false);
                if (lastModelOffset == -1) {
                    // String Not Found
                    lastModelOffset = 0;
                }
            }
        });
        // searchPreviousToolItem.setEnabled(false);
    }
   
    protected Section createContents_Browser () 
    {
      FormToolkit toolkit = managedForm.getToolkit();
//      final Section section = getSection();

      ///////////////////////////////////////////////////////////////////////
      
      // Create ToolBar
      Composite sectionToolbarComposite = FormSection.createGridLayoutContainer (toolkit, section,
              2, 0, 0);
      section.setTextClient(sectionToolbarComposite);
      
      ImageDescriptor descriptor = ImageLoader.getInstance().getImageDescriptor(ImageLoader.ICON_NEXT_ANNOT);
      ImageHyperlink infoNext = AbstractSectionPart.createToolbarItem(toolkit, sectionToolbarComposite, 
              descriptor, "Show Next Annotation", section.getTitleBarGradientBackground());
      infoNext.addHyperlinkListener(new HyperlinkAdapter() {
          public void linkActivated(HyperlinkEvent e) {
              // gotoPrevious();
              annotatedTextComposite.gotoAnnotation(true);
          }
      }); 
      descriptor = ImageLoader.getInstance().getImageDescriptor(ImageLoader.ICON_PREV_ANNOT);
      ImageHyperlink info = AbstractSectionPart.createToolbarItem(toolkit, sectionToolbarComposite, 
              descriptor, "Show Previous Annotation", section.getTitleBarGradientBackground());
      info.addHyperlinkListener(new HyperlinkAdapter() {
          public void linkActivated(HyperlinkEvent e) {
              // gotoPrevious();
              annotatedTextComposite.gotoAnnotation(false);
          }
      });        
              
      ///////////////////////////////////////////////////////////////////////
      GridLayout layout   = new GridLayout(1, false);
      layout.marginWidth  = 40;
      layout.marginHeight = 40;
      section.setLayout(layout);    
           
      ScrolledComposite scrolledClient = new ScrolledComposite(section, 
              SWT.V_SCROLL|SWT.BORDER);
      toolkit.adapt(scrolledClient);
      scrolledClient.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);;        
//      scrolledClient.addControlListener(new ControlAdapter() {
//          public void controlResized(ControlEvent e) {
//              Rectangle r = scrolledClient.getClientArea();
//              // scrolledClient.setMinSize(((SourceViewer)sourceViewer).getControl().computeSize(r.width, SWT.DEFAULT));
//              r.width -= verticalRuler.getWidth() + 32;
//              ((SourceViewer)sourceViewer).getTextWidget().computeSize(r.width, SWT.DEFAULT);
//          }
//      });

      scrolledClient.setExpandHorizontal(true);
      scrolledClient.setExpandVertical(true);
      // GridData gd = new GridData(GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING);
      GridData gd = new GridData(GridData.FILL_BOTH);
      scrolledClient.setLayoutData(gd);
      
      toolkit.paintBordersFor(scrolledClient);        
      section.setClient(scrolledClient);   
      
      /*********************************************************************/  
      String test = "Hello";
      webBrowser = new Browser(scrolledClient, SWT.NONE);      
      webBrowser.setText(test);
      
      scrolledClient.setContent(webBrowser);
      
      return section;
    } // createContents_Browser
    
    
    /*************************************************************************/   
    /*                          API to support CAS Diff                      */
    /*************************************************************************/   
    
//    public void showAnnotations (List<AnnotationObject> annotList, 
//                                boolean show, boolean refresh)
//    {
//        hideAllAnnotationsForView (TAB_VIEW_ANNOTATIONS, false);
//        // Set flag of each "Annot" to "show"
//        for (AnnotationObject obj: annotList) {
//            obj.show = show;
//        }
//        if (refresh) {
//            showAnnotationsForView (TAB_VIEW_ANNOTATIONS);
//        }
//    }
    
    /*************************************************************************/       

    public void setInput (ICASObjectView casView, String subTitle) 
    {
        currentViewCAS = casView.getCurrentView();
        casViewObject = casView;

        annotatedTextComposite.setTypeSystemStyle(typeSystemStyle);
        
        // Display Annotated Document
        String doc = casViewObject.getDocumentText();
        if (doc == null) {
            doc = "No Document Text";
        }
        documentObject.set(doc);
                 
        sortedAnnotationList = casViewObject.getIndexedAnnotations();
        
        // Testing hot keys
//        UimaTypeTree tsTree = casViewObject.getTypeTree();
//        ((CasSourceViewer) sourceViewer).setTypeSystem(tsTree);
//        ((CasSourceViewer) sourceViewer).setCASView((CASView)casViewObject);       
        
//        if (false) {
//            CasToInlineXhtml c = new CasToInlineXhtml(casViewObject);
//        }
//        try {
//            String s = c.generateXHTML(mTCAS, null);
//            Trace.trace(s);
//            if (webBrowser != null) {
//              webBrowser.setText(s);
//            }
//        } catch (CASException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    }

    public void refreshTypeSystemStyle (int forView)
    {
        showAnnotationsForView(forView);
    }
    
    /**
     *  Switch between Annotations and Index Repository Views when user clicks tab
     * 
     * @param tabIndex
     * @return void
     * 
     * Note: 
     *  - called from CASViewControl.tabFolder.addSelectionListener()
     *  - list4fsIndex is updated by FSIndexSectionPart which calls to 
     *       AbstractAnnotatedTextSectionPart.setListForAnnotationView
     */
    public void switchViewForAnnotations (int viewIndex)
    {
        if (currentView == viewIndex) {
            // Never happen !
            Trace.err("Try to switch to the SAME view");
            return;
        }
        
        List    list = null;
        if (viewIndex == TAB_VIEW_ANNOTATIONS) { 
            list = sortedAnnotationList;
        } else if (viewIndex == TAB_VIEW_INDEX_REPO) {
            list = list4fsIndex;
        } else if (viewIndex == TAB_VIEW_CASDIFF) {
            list = list4CasDiffAnnotations;
        }    
   //     annotatedTextComposite.enableAnnotationPainter(viewIndex == TAB_VIEW_CASDIFF ? true:false);
        
        currentView = viewIndex;
        if (list != null) {
            // annotatedTextComposite.deactivateAnnotationPainter(true);
            annotatedTextComposite.showAnnotations(list, true);
        } else {
            // For e.g. when switch to Index Repo tab and there is no annotation selections
            // Remove ALL annotations from model and fOverviewRuler
            // from OLD view
            annotatedTextComposite.removeAllAnnotationsFromCurrentView(true);
        }
    } // switchViewForAnnotations
    
    public List getAnnotationsAtPosition (int aPosition)
    {
        // Collect the annotations to be showed/hidden (independent of types)
        List list = new ArrayList(); // List of annotations to be returned
       
        List<AnnotationObject> annotationList = getAnnotationsForView(currentView);
        for (AnnotationObject annot: annotationList) {
            // TODO Tong Show ALL at clicked pos
            if (annot.getTypeShortName().equals("DocumentAnnotation")) {
                continue;
            }
//            if (annot.show) {
                if (annot.begin <= aPosition && annot.end > aPosition) {
                    list.add(new UFeatureStructure((AnnotationFS) annot.getFeatureStructure(), -1));
                } else if (annot.begin > aPosition) {
                    break;
                }               
//            }
        }
       
        return list.size() > 0 ? list:null;
    }
    
    /**
     *  Show annotations when user clicks annotations in document view
     * 
     * @param aPosition
     * @return void
     */
    private void showSelectedAnnotationTree(int aPosition)
    {        
        List    list = new ArrayList(); // list of annotations to be showed
        FSIterator annotIter = currentViewCAS.getAnnotationIndex().iterator();
        while (annotIter.isValid()) {
            AnnotationFS annot = (AnnotationFS) annotIter.get();
            if (annot.getBegin() <= aPosition && annot.getEnd() > aPosition) {
                // Found the annotation at position "aPosition"
//                if (checkbox != null && checkbox.isSelected()) {
//                    // addAnnotationToTree(annot);
//                }
                // Don't show DocumentAnnotation !!!
                if (annot.getType().getName().compareTo("uima.tcas.DocumentAnnotation") != 0) {
                    // Trace.trace(annot.getCoveredText());
                    Trace.trace(annot.getType().getShortName());
                    list.add(new UFeatureStructure(annot, -1));
                    // list.add(annot);
                    // break; // process only 1
                }
            } else if (annot.getBegin() > aPosition) {
                break;
            }
            annotIter.moveToNext();
        }        
//        if (list.size()>0) {
//            FSSectionPart.getInstance().setInput(list, "");
//        }
    }
        
    // Called from FSIndexSectionPart.widgetSelected 
    // and from switchViewForAnnotations
//    protected void showFSIndexAnnotation (List<AnnotationObject> fsList)
//    {
//        annotatedTextComposite.showFSIndexAnnotation(fsList);
//        
//    } // showFSIndexAnnotation
    
    /**
     *  Show/hide annotations of selected type in type system tree
     * 
     * @param typeName
     * @param show
     * @return void
     */
    public void showAnnotationsForTypeName (String typeName, boolean show, boolean refresh) 
    {
        // Get list of "AnnotationObject" for type "selectedType"
        List<AnnotationObject> annotList = (List<AnnotationObject>) casViewObject.getAnnotationsForTypeName(typeName);
        // Set the specified type's annotations to be showed/hidden
        if (annotList != null && annotList.size() > 0) {
            // Set flag of each "AnnotationObject" to "show"
            for (AnnotationObject annot: annotList) {
                annot.show = show;
            }
        } else {
            // No annotation for this type
            return;
        }
        if (refresh) {
            showAnnotationsForView (TAB_VIEW_ANNOTATIONS);
        }
    } // showAnnotationsForType
      
    public List<AnnotationObject> getAnnotationsForView (int forView)
    {
        // long start = System.currentTimeMillis();
        List<AnnotationObject> annotationList = null;
        if (forView == TAB_VIEW_ANNOTATIONS) { 
            annotationList = sortedAnnotationList;
        } else if (forView == TAB_VIEW_INDEX_REPO) {
            annotationList = list4fsIndex;
        } else if (forView == TAB_VIEW_CASDIFF) {
            annotationList = list4CasDiffAnnotations;
        }        

        return annotationList;
    }
    
    public void showAnnotationsForView (int forView)
    {
        // long start = System.currentTimeMillis();
        List<AnnotationObject> annotationList = null;
        if (forView == TAB_VIEW_ANNOTATIONS) { 
            annotationList = sortedAnnotationList;
        } else if (forView == TAB_VIEW_INDEX_REPO) {
            annotationList = list4fsIndex;
        } else if (forView == TAB_VIEW_CASDIFF) {
            annotationList = list4CasDiffAnnotations;
        }        
        if (annotationList == null) {
            return;
        }
        
        if (currentView == forView) {
            annotatedTextComposite.showAnnotations(annotationList, true);
        }
    } // showAnnotationsForView
    
    /**
     * Show "additional" annotations for the current view
     * 
     * @param viewIndex
     * @param list
     * @return void
     */
    public void showMoreAnnotationObjects (int viewIndex, List<AnnotationObject> list) {
        annotatedTextComposite.showMoreAnnotations(list);
    }
    
    /**
     * 
     * Note:
     *  - called from CASViewControl.setTypeSelectionByName() when "deselect all"
     *  is selected from TypesTreeSectionPart
     * 
     * 
     */
    public void hideAllAnnotationsForView (int forView, boolean refreshSourceViewer)
    {
        List<AnnotationObject>    list = null;
        if (forView == TAB_VIEW_ANNOTATIONS) { 
            list = sortedAnnotationList;
        } else if (forView == TAB_VIEW_INDEX_REPO) {
            list = list4fsIndex;
        } else if (forView == TAB_VIEW_CASDIFF) {
            list = list4CasDiffAnnotations;
        }
        // Hide ALL AnnotationObject
        for (AnnotationObject annot: list) {
            annot.show = false;
        }

        // Refresh current view ?
        if (currentView == forView) {
            annotatedTextComposite.hideAllAnnotationsForView(refreshSourceViewer);
        }
    }

    /*************************************************************************/
    
    public void mouseDoubleClick(MouseEvent e) {
    }

    public void mouseDown(MouseEvent e) {
    }

    public void mouseUp(MouseEvent e) {
        if (currentView == TAB_VIEW_ANNOTATIONS
                || currentView == TAB_VIEW_CASDIFF) {
            final List list = getAnnotationsAtPosition(styledTextDoc.getCaretOffset());
            // Let the listener decide what to do when list == null.
            // (may decide to clear the viewer)               
            Object[] listeners = selectionListeners.getListeners();
            for (int i = 0; i < listeners.length; ++i) {
                final IObjectSelectionListener l = (IObjectSelectionListener) listeners[i];
                
                Display.getCurrent().asyncExec(new Runnable() {
                    public void run() {
                        // see CASViewControl
                        l.objectSelected(AnnotatedTextSectionPart.this, list);
                    }
                });               
            }
        }
        // Update Status Line
        if (statusLine != null) {
            statusLine.setMessage(annotatedTextComposite.getCursorPosition());
        }       
    }    
    
    /*************************************************************************/
    /*                     IObjectSelectionListener                          */
    /*************************************************************************/
    
    private ListenerList selectionListeners = new ListenerList(ListenerList.IDENTITY);
    
    /**
     * Note: CASViewControl is the listener
     */
    public void addSelectionListener(IObjectSelectionListener listener)
    {
        selectionListeners.add(listener);
    }
    
    public void removeSelectionListener(IObjectSelectionListener listener)
    {
        selectionListeners.remove(listener);
    }
        
    /*************************************************************************/


    
}
