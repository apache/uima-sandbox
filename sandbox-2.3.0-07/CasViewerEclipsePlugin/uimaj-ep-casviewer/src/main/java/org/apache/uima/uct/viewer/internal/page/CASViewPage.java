package org.apache.uima.uct.viewer.internal.page;

import java.util.List;

import org.apache.uima.casviewer.core.internal.ICASObjectView;
import org.apache.uima.casviewer.ui.internal.model.ICASViewControl;
import org.apache.uima.casviewer.ui.internal.model.ICASViewerListener;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.casviewer.viewer.internal.Messages;
import org.apache.uima.tools.common.internal.images.ImageLoader;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.texteditor.IUpdate;


/**
 * 
 * 
 * Note: Need to support Status Line
 * 
 */
public class CASViewPage extends HeaderPage implements ICASViewPage, IAdaptable
{
    static private final String     PAGE_TITLE = Messages.getString("Page.Title");
    static private final String     DEFAULT_SOFA_TITLE = Messages.getString("DefaultSofa.Title");
    
    // Preferences (allow access from subclass)
    protected boolean                 prefHideFeaturesInTypeSystem = true;
    protected boolean                 prefHideNoValueFeature = true;
    protected boolean                 prefFlatLayout4Types;
    protected boolean                 prefFlatLayout4FS;    
    
    private IEditorSite             editorSite;

    private ICASViewControl          casViewControl;
//    private CasSourceViewer         sourceViewer;

//    private IManagedForm            managedForm = null;
//    private SashForm                sashForm = null;
//    private Composite               browseDirectoryComposite = null;

    private String                  title = null;
    private String                  inputFileName;
    private ICASObjectView          aCASObjectView;
    private TypeSystemStyle         typesystemStyle;
    private String                  typesystemStyleFile;
    private IStatusLineManager      statusLine;
    private List<String>            preSelectedTypeNames;

    /*************************************************************************/
    /*          The following methods can be override by subclass            */
    /*************************************************************************/
    
    protected void createFormMenu (IManagedForm managedForm)
    {
        Action refreshAction = new Action("Refresh", Action.AS_PUSH_BUTTON) {
            public void run() {
                // Trace.trace();
//                if (ICASViewerListener.class.isAssignableFrom(editor.getClass().getInterfaces())) {
                    ((ICASViewerListener) editor).onActionInvocation(this, 
                            ICASViewerListener.PAGE_EVENT_REFRESH_INPUT, null);
//                }
//                if (inputFileName != null && inputFileName.trim().length()>0 ) {
//                    if (inputFileName.endsWith("xmi")) {
//                        // openXmiFile(inputFileName);
//                    } else if (inputFileName.endsWith("xcas")) {
//                        // openXcasFile(inputFileName);
//                    } else {
//                        Trace.bug("Cannot open this unknown file: " + inputFileName);
//                    }
//                }
            }
        };
        refreshAction.setDescription("Refresh current input file");
        refreshAction.setText("Refresh");
        refreshAction.setChecked(false);
        refreshAction.setToolTipText("Refresh current input file");
        refreshAction.setImageDescriptor(ImageLoader.getInstance()
                .getImageDescriptor(ImageLoader.ICON_PDE_UI_REFRESH));
        managedForm.getForm().getToolBarManager().add(refreshAction);                
        managedForm.getForm().getToolBarManager().update(true);
    }

    protected ICASViewControl createCASViewControl (IWorkbenchPart part,
            IWorkbenchPartSite site, IManagedForm managedForm,
            Composite parent, ICASObjectView aCASViewObject,
            TypeSystemStyle tsStyle, String tsStyleFileName) {
        
        ICASViewControl control = CASViewControl.createInstance (part, site, managedForm, 
                parent, aCASViewObject, 
                tsStyle, tsStyleFileName);
        control.setPreference(ICASViewControl.PREF_HIDE_FEATURES_IN_TYPE_SYSTEM,
                prefHideFeaturesInTypeSystem);
        control.setPreference(ICASViewControl.PREF_HIDE_FEATURE_NO_VALUE,
                prefHideNoValueFeature);
        control.setPreference(ICASViewControl.PREF_FLAT_LAYOUT_FOR_TYPES,
                prefFlatLayout4Types);
        control.setPreference(ICASViewControl.PREF_FLAT_LAYOUT_FOR_FS,
                prefFlatLayout4FS);

        return control;
    }
        
    public void refreshInput() {
        if (casViewControl != null) {
            casViewControl.setInput(aCASObjectView, title);
        }
    }
    
    /*************************************************************************/
    
	public CASViewPage(FormEditor editor, ICASObjectView aCASViewObject, 
                            String inputFileName, String title,
                            TypeSystemStyle tsStyle,
                            String tsStyleFileName) {
		super(editor, aCASViewObject.getSofaId());
        this.aCASObjectView = aCASViewObject;
        this.inputFileName  = inputFileName;
        this.title          = title;
        this.editorSite = editor.getEditorSite();
        this.typesystemStyle = tsStyle;
        this.typesystemStyleFile = tsStyleFileName;
	}
    
    public void dispose () {
        if (casViewControl != null) {
            casViewControl.dispose();
            casViewControl = null;
        }
        super.dispose();
    }
    
    public void setActive (boolean active) {
        super.setActive(active);
//        if (casViewControl != null && (casViewControl instanceof CASViewControl) 
//                && ((CASViewControl) casViewControl).typesystemTree != null) {
//            CollectionChartView instance = CollectionChartView.getInstance();
//            if (instance != null) {
//                List<TypeNode> list = ((CASViewControl) casViewControl).typesystemTree.getNodeListFromHierarchy(false, false);
//                // ONLY nodes having annotations
//                List<TypeNode>   typesHavingAnnotations = new ArrayList<TypeNode>();
//                for (TypeNode node: list) {
//                    // Has annotations ?
//                    if ( !node.isHidden() && node.getFsCount() > 0 ) {
//                        typesHavingAnnotations.add(node);
//                    }  
//                }
//                TypeStatistics[] stat = new TypeStatistics[typesHavingAnnotations.size()];
//                int i =0;
//                for (TypeNode node: typesHavingAnnotations) {
//                    stat[i++] = new TypeStatistics(node.getLabel(), 
//                            node.getFsCount(), node.getFsCount(),
//                            (Color) node.getBgColor());
//                }
//                instance.setInput(stat);
//            }
//        }
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.IPage#setFocus()
     */
    public void setFocus() {
      Trace.err(getPartName());
    }

    public IAction getGlobalAction(String id) {
      IAction action = casViewControl.getGlobalAction(id);
      if (action instanceof IUpdate) {
        Trace.err("actionId: " + id);
        ((IUpdate) action).update();
      }
      return action;
    }

    public void setPreference (int attribute, boolean value)
    {
        if (attribute == ICASViewControl.PREF_HIDE_FEATURES_IN_TYPE_SYSTEM) {
            // Hide features in type system's tree
            prefHideFeaturesInTypeSystem = value;
        } else if (attribute == ICASViewControl.PREF_HIDE_FEATURE_NO_VALUE) {
            // Hide feature having no value
            prefHideNoValueFeature = value;
        } else if (attribute == ICASViewControl.PREF_FLAT_LAYOUT_FOR_TYPES) {
            prefFlatLayout4Types = value;
        } else if (attribute == ICASViewControl.PREF_FLAT_LAYOUT_FOR_FS) {
            prefFlatLayout4FS = value;
        }        
        if (casViewControl != null) {
            casViewControl.setPreference(attribute, value);
        }
    }

//    public void setSourceViewer (CasSourceViewer sourceViewer) {
//      this.sourceViewer = sourceViewer;
//    }

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    // Called by FindReplaceAction.update
//    public Object getAdapter(Class required) {
//        // Trace.err();
//        if (IFindReplaceTarget.class.equals(required)) {
//            Trace.err(5, getPartName() + " - IFindReplaceTarget");
//            return sourceViewer.getFindReplaceTarget();
//        }
//        Trace.err();
//        return super.getAdapter(required);
//    }
    
    /*************************************************************************/
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
     */
    protected void createFormContent (IManagedForm managedForm) 
    {
//        this.managedForm = managedForm;
        this.toolkit = managedForm.getToolkit();
        // createToolBarActions(managedForm);
        managedForm.getForm().setText(PAGE_TITLE);
        if (inputFileName != null) {
            createFormMenu (managedForm);
        }
        
        managedForm.getForm().getBody().setLayout(new GridLayout(1, false)); // this is required !
        
        casViewControl = createCASViewControl (this, getSite(), managedForm, 
                managedForm.getForm().getBody(), aCASObjectView, 
                typesystemStyle, typesystemStyleFile);        
        casViewControl.createContents();

        if (statusLine != null) {
            casViewControl.setStatusLineManager(statusLine);
        }
        
        if (preSelectedTypeNames != null) {
            selectTypesByName(preSelectedTypeNames);
        }
        
        // Set page title
        String text;
        if (title != null) {
            text = title;
        } else {
            String extra = "";
//            if ( ! DEFAULT_SOFA_TITLE.equalsIgnoreCase(aCASViewObject.getSofaId()) ) {
//                extra = " - " + aCASViewObject.getSofaId();
//            }
            text = PAGE_TITLE;
            if (inputFileName != null) {
                text = inputFileName.substring(1+inputFileName.lastIndexOf("\\"))
                    + extra;
            }
        }
        managedForm.getForm().setText(text);            
    }
    

    public void refreshTypeSystemStyle ()
    {
        if (casViewControl != null) {
            casViewControl.refreshTypeSystemStyle();
        }
    }

    /*************************************************************************/
    
    public void setStatusLineManager (IStatusLineManager statusLine)
    {
        this.statusLine = statusLine;
        if (casViewControl != null) {
            casViewControl.setStatusLineManager(statusLine);
        }
    }
    
    public void selectTypesByName (List typeNames)
    {
        if (casViewControl != null) {
            casViewControl.setTypeSelectionByName(typeNames, true);        
        } else {
            preSelectedTypeNames = typeNames;
        }
    }

    public void deselectTypesByName (List typeNames)
    {
        if (casViewControl != null) {
            casViewControl.setTypeSelectionByName(typeNames, false);   
        }
    }
    
    public List getSelectedTypeNames() {
        if (casViewControl != null) {
            return casViewControl.getSelectedTypeNames();
        }
        return null;
    }


}
