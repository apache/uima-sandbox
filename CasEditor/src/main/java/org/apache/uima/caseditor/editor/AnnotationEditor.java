/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.caseditor.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.caseditor.core.AbstractAnnotationDocumentListener;
import org.apache.uima.caseditor.core.model.dotcorpus.AnnotationStyle;
import org.apache.uima.caseditor.core.model.dotcorpus.EditorAnnotationStatus;
import org.apache.uima.caseditor.core.uima.AnnotationComparator;
import org.apache.uima.caseditor.core.util.Span;
import org.apache.uima.caseditor.editor.action.DeleteFeatureStructureAction;
import org.apache.uima.caseditor.editor.annotation.DrawingStyle;
import org.apache.uima.caseditor.editor.context.AnnotationEditingControlCreator;
import org.apache.uima.caseditor.editor.outline.AnnotationOutline;
import org.apache.uima.caseditor.ui.FeatureStructureTransfer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.text.information.InformationPresenter;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.IStatusField;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;
import org.eclipse.ui.texteditor.StatusTextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * An editor to annotate text.
 * 
 * TODO: 
 * add an action to increase left side of an annotation 
 * add an action to increase right side of an annotation 
 * add an action to decrease left side on an annotation 
 * add an action to decrease right side on an annotation
 */
public final class AnnotationEditor extends StatusTextEditor implements ISelectionListener {
  /**
   * This action annotates the selected text with a defined tag.
   */
  private class AnnotateAction extends Action {
    private StyledText mTextWidget;

    /**
     * Initializes a new instance.
     * 
     * @param textWidget
     */
    AnnotateAction(StyledText textWidget) {
      mTextWidget = textWidget;
    }

    /**
     * Executes this action, adds an annotation of the marked span.
     */
    @Override
    public void run() {
      if (isSomethingSelected()) {
        Point selection = mTextWidget.getSelectionRange();

        // get old annotations of current type for this area
        // if there is something ... the delete them and add
        Collection<AnnotationFS> oldAnnotations = getDocument().getAnnotation(
                mCurrentAnnotationType, new Span(selection.x, selection.y));

        if (!oldAnnotations.isEmpty()) {
          getDocument().removeAnnotations(oldAnnotations);
        }

        int start = selection.x;
        int end = start + selection.y;

        AnnotationFS annotation = getDocument().getCAS().createAnnotation(mCurrentAnnotationType,
                start, end);

        getDocument().addFeatureStructure(annotation);

        mFeatureStructureSelectionProvider.setSelection(getDocument(), annotation);
      }
    }

    AnnotationDocument getDocument() {
      return AnnotationEditor.this.getDocument();
    }
  }

  /**
   * Shows the annotation editing context for the active annoation.
   */
  private class ShowAnnotationContextEditAction extends Action {
    private InformationPresenter mPresenter;

    /**
     * Initializes a new instance.
     */
    ShowAnnotationContextEditAction() {
      if (mPresenter == null) {
        mPresenter = new InformationPresenter(new AnnotationEditingControlCreator());

        mPresenter.setInformationProvider(new AnnotationInformationProvider(AnnotationEditor.this),
                org.eclipse.jface.text.IDocument.DEFAULT_CONTENT_TYPE);
        mPresenter.setDocumentPartitioning(org.eclipse.jface.text.IDocument.DEFAULT_CONTENT_TYPE);
        mPresenter.install(getSourceViewer());
      }
    }

    /**
     * Executes this action, showse context information.
     */
    @Override
    public void run() {
      mPresenter.showInformation();

      // the information presenter closes ... if
      // the subject control sends a keyDown event

      // this action is triggered with
      // a keyEvent send by the subject control

      // inside this handler the presenter gets installed on
      // the subject control during that the presenter registers
      // a key listener on the subject control

      // the presenter closes now because the keyEvent to trigger
      // this action is also sent to the presenter

      // to avoid this behavior this action is reposted
      // to the END of the ui thread queue

      // this does not happen if for the action is no key explict
      // configured with setActivationKey(...)

      // TODO:
      // This does not work for mac or linux
    }
  }

  /**
   * This <code>IDocumentChangeListener</code> is responsible to synchronize annotation in the
   * document with the annotations in eclipse.
   */
  private class DocumentListener extends AbstractAnnotationDocumentListener {
    /**
     * Adds a collection of annotations.
     * 
     * @param annotations
     */
    public void addedAnnotation(Collection<AnnotationFS> annotations) {    	
    	mPainter.paint(AnnotationPainter.CONFIGURATION);
    }

    /**
     * Removes a collection of annotations.
     * 
     * @param deletedAnnotations
     */
    public void removedAnnotation(Collection<AnnotationFS> deletedAnnotations) {
    	
      if (getSite().getPage().getActivePart() == AnnotationEditor.this) {
        mFeatureStructureSelectionProvider.clearSelection();
      } else {
        mFeatureStructureSelectionProvider.clearSelectionSilently();
      }

      highlight(0, 0); // TODO: only if removed annotation was selected
      
      mPainter.paint(AnnotationPainter.CONFIGURATION);
    }

    /**
     * 
     * @param annotations
     */
    public void updatedAnnotation(Collection<AnnotationFS> annotations) {
      
      removedAnnotation(annotations);
      addedAnnotation(annotations);
      
      List<ModelFeatureStructure> structures = new LinkedList<ModelFeatureStructure>();
      
      for (AnnotationFS annotation : annotations) {
        structures.add((new ModelFeatureStructure(getDocument(), annotation)));
      }
      
      selectionChanged(getSite().getPage().getActivePart(), new StructuredSelection(structures));
    }
  }

  /**
   */
  private static abstract class TypeMenu extends ContributionItem {
    private Type mParentType;

    private TypeSystem mTypeSystem;

    /**
     * Initializes a new instance.
     * 
     * @param parentType
     * @param typeSystem
     */
    TypeMenu(Type parentType, TypeSystem typeSystem) {
      mParentType = parentType;
      mTypeSystem = typeSystem;
    }

    /**
     * Fills the menu with type entries.
     * 
     * @param menu
     * @param index
     */
    @Override
    public void fill(Menu menu, int index) {
      fillTypeMenu(mParentType, menu);
    }

    private void fillTypeMenu(Type parentType, Menu parentMenu) {
      Vector childs = mTypeSystem.getDirectlySubsumedTypes(parentType);

      Menu newSubMenu;

      // has this type subtypes ?
      // yes
      if (childs.size() != 0) {
        MenuItem subMenuItem = new MenuItem(parentMenu, SWT.CASCADE);
        subMenuItem.setText(parentType.getShortName());

        newSubMenu = new Menu(subMenuItem);
        subMenuItem.setMenu(newSubMenu);

        insertAction(parentType, newSubMenu);

        Iterator childsIterator = childs.iterator();

        while (childsIterator.hasNext()) {
          Type child = (Type) childsIterator.next();

          fillTypeMenu(child, newSubMenu);
        }
      }
      // no
      else {
        insertAction(parentType, parentMenu);
      }
    }

    protected abstract void insertAction(final Type type, Menu parentMenu);
  }

  /**
   * Creates the mode context submenu.
   */
  private class ModeMenu extends TypeMenu {
    /**
     * Initializes a new instance.
     * 
     * @param type
     * @param typeSystem
     */
    ModeMenu(TypeSystem typeSystem) {
      super(typeSystem.getType(CAS.TYPE_NAME_ANNOTATION), typeSystem);
    }

    @Override
    protected void insertAction(final Type type, Menu parentMenu) {
      MenuItem actionItem = new MenuItem(parentMenu, SWT.PUSH);
      actionItem.setText(type.getShortName());

      actionItem.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {

          IAction actionToExecute = new ChangeModeAction(type, type.getShortName(),
                  AnnotationEditor.this);

          actionToExecute.run();
        }
      });
    }
  }

  /**
   * Creates the show annotations context submenu.
   */
  private class ShowAnnotationsMenu extends TypeMenu {
	 
	 private Collection<Type> mTypesToDisplay = new HashSet<Type>();
	  
    /**
     * Initializes a new instance.
     * 
     * @param type
     * @param typeSystem
     */
    ShowAnnotationsMenu(EditorAnnotationStatus status, TypeSystem typeSystem) {
      super(typeSystem.getType(CAS.TYPE_NAME_ANNOTATION), typeSystem);
      
      mTypesToDisplay.addAll(status.getDisplayAnnotations());
    }

    @Override
    protected void insertAction(final Type type, Menu parentMenu) {
      final MenuItem actionItem = new MenuItem(parentMenu, SWT.CHECK);
      actionItem.setText(type.getShortName());

      if (mTypesToDisplay.contains(type)) {
        actionItem.setSelection(true);
      }

      // TODO: move this to an action
      // do not access mTypesToDisplay directly !!!
      actionItem.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
          if (actionItem.getSelection()) {
            mTypesToDisplay.add(type);

          } else {
            mTypesToDisplay.remove(type);
          }

          // TODO: only synchronize annotation which
          // must be removed/addeded
          syncAnnotations();
        }
      });
    }
    
    Collection<Type> getSelectedTypes() {
    	return Collections.unmodifiableCollection(mTypesToDisplay);
    }
  }

  /**
   * Sometimes the wrong annoation is selected ... ????
   */
  private class FeatureStructureDragListener implements DragSourceListener {
    private boolean mIsActive;

    private AnnotationFS mCandidate;

    FeatureStructureDragListener(final StyledText textWidget) {
      textWidget.addKeyListener(new KeyListener() {
        public void keyPressed(KeyEvent e) {
          if (e.keyCode == SWT.ALT) {
            mIsActive = true;

            textWidget.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_HAND));
          }
        }

        public void keyReleased(KeyEvent e) {
          if (e.stateMask == SWT.ALT) {
            mIsActive = false;
            textWidget.setCursor(null);
          }
        }
      });

      textWidget.addMouseMoveListener(new MouseMoveListener() {

        public void mouseMove(MouseEvent e) {
          if (mIsActive) {
            // try to get the position inside the text
            int offset = textWidget.getOffsetAtLocation(new Point(e.x, e.y));

            Map<Integer, AnnotationFS> view = getDocument().getView(mCurrentAnnotationType);

            mCandidate = view.get(offset);

            if (mCandidate != null) {
              textWidget.setSelectionRange(mCandidate.getBegin(), mCandidate.getEnd()
                      - mCandidate.getBegin());
            }
          }
        }

      });
    }

    public void dragStart(DragSourceEvent event) {
      if (mIsActive) {
        event.doit = mCandidate != null;
      } else {
        event.doit = false;
      }
    }

    public void dragSetData(DragSourceEvent event) {
      event.data = mCandidate;
    }

    public void dragFinished(DragSourceEvent event) {
    }
  }
  
  private Type mCurrentAnnotationType;

  /**
   * The outline page belonging to this editor.
   */
  private IContentOutlinePage mOutlinePage;

  private IAnnotationEditorModifyListener mEditorListener;

  /**
   * TODO: Do we realy need this pos variable ?
   */
  private int mCursorPosition;

  private AnnotationDocument mDocument;

  boolean mIsSomethingHighlighted = false;

  private StyleRange mCurrentStyleRange;

  private FeatureStructureSelectionProvider mFeatureStructureSelectionProvider;

  private AnnotationPainter mPainter;

//  private IAnnotationModel mAnnotationModel;

  private ShowAnnotationsMenu mShowAnnotationsMenu;
  
  /**
   * Creates an new AnnotationEditor object.
   */
  public AnnotationEditor() {
    setDocumentProvider(new CasDocumentProvider());
  }

  /**
   * Retrives annotation editor adapters.
   * 
   * @param adapter
   * @return an adapter or null
   */
  @Override
  public Object getAdapter(Class adapter) {
    if (IContentOutlinePage.class.equals(adapter)) {
      if (mOutlinePage == null) {
        mOutlinePage = new AnnotationOutline(this);
      }

      return mOutlinePage;
    } else if (CAS.class.equals(adapter)) {
      return getDocument().getCAS();
    } else {
      return super.getAdapter(adapter);
    }
  }

  @Override
  protected ISourceViewer createSourceViewer(Composite parent,
          org.eclipse.jface.text.source.IVerticalRuler ruler, int styles) {
	  SourceViewer sourceViewer = new SourceViewer(parent, ruler, styles);
    
    sourceViewer.setEditable(false);
    
	mPainter = new AnnotationPainter(sourceViewer, new IAnnotationAccess() {

		public Object getType(Annotation annotation) {
			return null;
		}

		public boolean isMultiLine(Annotation annotation) {
			return false;
		}

		public boolean isTemporary(Annotation annotation) {
			return false;
		}
		
	});
	sourceViewer.addPainter(mPainter);
    
	// mPainter.modelChanged(mAnnotationModel); // realy nessesary ?
	
    return sourceViewer;
  }

  /**
   * Configures the editor.
   * 
   * @param parent
   */
  @Override
  public void createPartControl(Composite parent) {
    super.createPartControl(parent);
    
    /*
     * this is a workaround for the quickdiff assertion if nothing was changed, how to do this
     * better ? is this the right way ?
     */
    showChangeInformation(false);

    getSourceViewer().getTextWidget().addKeyListener(new KeyListener() {
      public void keyPressed(KeyEvent e) {
        int newCaretOffset = getSourceViewer().getTextWidget().getCaretOffset();

        if (newCaretOffset != mCursorPosition) {
          mCursorPosition = newCaretOffset;
          cursorPositionChanged();
        }
      }

      public void keyReleased(KeyEvent e) {
        // not implemented
      }

    });

    getSourceViewer().getTextWidget().addMouseListener(new MouseListener() {
      public void mouseDown(MouseEvent e) {
        int newCaretOffset = getSourceViewer().getTextWidget().getCaretOffset();

        if (newCaretOffset != mCursorPosition) {
          mCursorPosition = newCaretOffset;
          cursorPositionChanged();
        }
      }

      public void mouseDoubleClick(MouseEvent e) {
        // not needed
      }

      public void mouseUp(MouseEvent e) {
        // not needed
      }

    });

    DragSource dragSource = new DragSource(getSourceViewer().getTextWidget(), DND.DROP_COPY);

    Transfer[] types = new Transfer[] { FeatureStructureTransfer.getInstance() };

    dragSource.setTransfer(types);

    dragSource.addDragListener(new FeatureStructureDragListener(getSourceViewer().getTextWidget()));

    getSite().getPage().addSelectionListener(this);

    getSourceViewer().getTextWidget().setEditable(false);
    getSourceViewer().setEditable(false);

    getSite().setSelectionProvider(mFeatureStructureSelectionProvider);
    
    if (mDocument != null) {
	    mShowAnnotationsMenu = new ShowAnnotationsMenu(
	    		mDocument.getProject().getEditorAnnotationStatus(), 
	    		getDocument().getCAS().getTypeSystem());
	    
		EditorAnnotationStatus status = mDocument.getProject().getEditorAnnotationStatus();
		setAnnotationType(status.getMode());
    }
    

  }

  // TODO: still not called always, e.g. on mouse selection
  private void cursorPositionChanged() {
    mFeatureStructureSelectionProvider.setSelection(getDocument(), getSelectedAnnotations());
  }

  /**
   * Checks if the current instance is editable.
   * 
   * @return false
   */
  @Override
  public boolean isEditable() {
    return false;
  }

  @Override
  protected void doSetInput(IEditorInput input) throws CoreException {
    super.doSetInput(input);

    mDocument = (AnnotationDocument) getDocumentProvider().getDocument(input);

    if (mDocument != null) {
    	// mAnnotationModel = getDocumentProvider().getAnnotationModel(input);
    	
    	getDocument().addChangeListener(new DocumentListener());
    }
  }

  @Override
  protected void editorContextMenuAboutToShow(IMenuManager menu) {
    super.editorContextMenuAboutToShow(menu);

    TypeSystem typeSytem = getDocument().getCAS().getTypeSystem();

    // mode menu
    MenuManager modeMenu = new MenuManager("Mode");
    menu.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, modeMenu);
    modeMenu.add(new ModeMenu(typeSytem));

    // annotation menu
    MenuManager showAnnotationMenu = new MenuManager("Show Annotations");
    menu.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, showAnnotationMenu);
    showAnnotationMenu.add(mShowAnnotationsMenu);
  }

  /**
   * Updates the status line.
   */
  private void updateStatusLineModeItem() {
    // TODO: refactore this
    IStatusField statusField = getStatusField(AnnotationEditorActionContributor.ID);

    // can be null directly after doSetInput()
    if (statusField != null) {
      statusField.setText(mCurrentAnnotationType.getShortName());
    }
  }

  /**
   * Returns the current <code>AnnotationDocument</code> of this editor.
   * 
   * @return current <code>AnnotationDocument</code>
   */
  public AnnotationDocument getDocument() {
    return mDocument;
  }

  /**
   * Returns the current annotation type.
   * 
   * @return - current annotation type
   */
  public Type getAnnotationMode() {
    return mCurrentAnnotationType;
  }

  /**
   * Sets the new annotation type.
   * 
   * @param type
   */
  protected void setAnnotationType(Type type) {
    // TODO: check if this type is a subtype of Annotation

    mCurrentAnnotationType = type;

    setProjectEditorStatus();

    updateStatusLineModeItem();

    syncAnnotations();

    fireAnnotationTypeChanged(mCurrentAnnotationType);
  }

  /**
   * @param type
   */
  private void fireAnnotationTypeChanged(Type type) {
    if (mEditorListener == null) {
      return;
    }

    mEditorListener.annotationModeChanged(type);
  }

  private void showAnnotationType(Type type) {
		AnnotationStyle style = mDocument.getProject().getDotCorpus().getAnnotation(type);
		mPainter.addDrawingStrategy(type.getName(), 
				DrawingStyle.valueOf(style.getStyle().name()).getStrategy());
		mPainter.addAnnotationType(type.getName(), type.getName());
		mPainter.setAnnotationTypeColor(type.getName(), style.getColor());
  }
  
  /**
   * 
   */
  private void syncAnnotations() {

	mPainter.removeAllAnnotationTypes();
	
	for (Type displayType : mShowAnnotationsMenu.getSelectedTypes()) {
		showAnnotationType(displayType);
	}

    // if not contained in types add current mode annotations
    if (!mShowAnnotationsMenu.getSelectedTypes().contains(mCurrentAnnotationType)) {
    	showAnnotationType(mCurrentAnnotationType);
    }

	mPainter.paint(AnnotationPainter.CONFIGURATION);
  }

  /**
   * @param listener
   */
  public void addAnnotationListener(IAnnotationEditorModifyListener listener) {
    mEditorListener = listener;
  }

  /**
   * Returns the selection.
   * 
   * @return - the selection
   */
  public Point getSelection() {
    return getSourceViewer().getTextWidget().getSelection();
  }

  /**
   * Hightligts the given range in the editor.
   * 
   * @param start
   * @param length
   */
  private void highlight(int start, int length) {
    ISourceViewer sourceViewer = getSourceViewer();

    assert sourceViewer != null;

    StyledText text = sourceViewer.getTextWidget();

    if (mCurrentStyleRange != null) {
      // reset current style range
      StyleRange resetedStyleRange = new StyleRange(mCurrentStyleRange.start,
              mCurrentStyleRange.length, null, null);

      text.setStyleRange(resetedStyleRange);
    }

    if (length != 0) {
      mCurrentStyleRange = new StyleRange(start, length, text.getSelectionForeground(), text
              .getSelectionBackground());

      text.setStyleRange(mCurrentStyleRange);
    }
  }

  /**
   * Retrives the currently selected annotation.
   * 
   * TODO: make this private ??? clients can use selections for this ...
   * 
   * @return the selected anotation or null if none
   */
  public List<AnnotationFS> getSelectedAnnotations() {
    List<AnnotationFS> selection = new ArrayList<AnnotationFS>();

    if (isSomethingSelected()) {
      Point selectedText = getSourceViewer().getTextWidget().getSelectionRange();

      Span selecectedSpan = new Span(selectedText.x, selectedText.y);

      Collection<AnnotationFS> selectedAnnotations = getDocument().getAnnotation(
              mCurrentAnnotationType, selecectedSpan);

      for (AnnotationFS annotation : selectedAnnotations) {
        selection.add(annotation);
      }

      Collections.sort(selection, new AnnotationComparator());
    } else {
      Map<Integer, AnnotationFS> view = getDocument().getView(mCurrentAnnotationType);

      AnnotationFS annotation = view.get(mCursorPosition);

      if (annotation == null) {
        annotation = view.get(mCursorPosition - 1);
      }

      if (annotation != null) {
        selection.add(annotation);
      }
    }

    return selection;
  }

  /**
   * Text is not editable, cause of the nature of the annotation editor. This does not mean, that
   * the annotations are not editable.
   * 
   * @return false
   */
  @Override
  public boolean isEditorInputModifiable() {
    return false;
  }

  /**
   * Notifies the current instance about selection changes in the workbench.
   * 
   * @param part
   * @param selection
   */
  public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    if (selection instanceof StructuredSelection) {
      AnnotationSelection annotations = new AnnotationSelection((StructuredSelection) selection);

      // only process these selection if the annotations belong
      // to the current editor instance
      if (getSite().getPage().getActiveEditor() == this && !annotations.isEmpty()) {
        highlight(annotations.getFirst().getBegin(), annotations.getLast().getEnd()
                - annotations.getFirst().getBegin());

        // move caret to new positon when selected outside of the editor
        if (AnnotationEditor.this != part)
        // TODO: replace with this == part and test it
        {
          getSourceViewer().getTextWidget().setCaretOffset(annotations.getLast().getEnd());
        }
      }
    }
  }

  private boolean isSomethingSelected() {
    // TODO: sometimes we get a NPE here ... mh
    // getSourceViewer() returns null here ... but why ?
    return getSourceViewer().getTextWidget().getSelectionCount() != 0;
  }

  private void setProjectEditorStatus() {
    // TODO: do not replace if equal ... check this
    EditorAnnotationStatus status = new EditorAnnotationStatus(getAnnotationMode(), 
            mShowAnnotationsMenu.getSelectedTypes());
    getDocument().getProject().setEditorAnnotationStatus(status);
  }

  /**
   * Creates custom annoation actions.
   */
  @Override
  protected void createActions() {
    super.createActions();
    
    
    mFeatureStructureSelectionProvider = new FeatureStructureSelectionProvider();
    getSite().setSelectionProvider(mFeatureStructureSelectionProvider);

    // create annotate action
    AnnotateAction annotateAction = new AnnotateAction(getSourceViewer().getTextWidget());

    annotateAction.setActionDefinitionId(ITextEditorActionDefinitionIds.SMART_ENTER);

    setAction(ITextEditorActionDefinitionIds.SMART_ENTER, annotateAction);
    setActionActivationCode(ITextEditorActionDefinitionIds.SMART_ENTER, (char) '\r', SWT.CR,
            SWT.DEFAULT);

    // create delete action
    DeleteFeatureStructureAction deleteAnnotationAction = new DeleteFeatureStructureAction(
            getDocument());
    
    getSite().getSelectionProvider().addSelectionChangedListener(deleteAnnotationAction);
    
    deleteAnnotationAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.DELETE);

    setAction(IWorkbenchActionDefinitionIds.DELETE, deleteAnnotationAction);
    setActionActivationCode(IWorkbenchActionDefinitionIds.DELETE, (char) 0, SWT.CR, SWT.NONE);

    // create show annotation context editing action
    ShowAnnotationContextEditAction annotationContextEditAction = 
            new ShowAnnotationContextEditAction();

    annotationContextEditAction.setActionDefinitionId(ITextEditorActionDefinitionIds.QUICK_ASSIST);

    setAction(ITextEditorActionDefinitionIds.QUICK_ASSIST, annotationContextEditAction);
  }
  
  @Override
  public void dispose() {
    // remove selection listener
    getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
  }
}