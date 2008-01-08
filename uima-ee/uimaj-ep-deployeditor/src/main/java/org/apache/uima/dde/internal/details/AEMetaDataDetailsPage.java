package org.apache.uima.dde.internal.details;

import org.apache.uima.aae.deployment.AEDeploymentMetaData;
import org.apache.uima.aae.deployment.AEService;
import org.apache.uima.aae.deployment.AsyncAEErrorConfiguration;
import org.apache.uima.aae.deployment.RemoteAEDeploymentMetaData;
import org.apache.uima.aae.deployment.impl.AEDeploymentDescription_Impl;
import org.apache.uima.aae.deployment.impl.DeploymentMetaData_Impl;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.application.metadata.impl.UimaApplication_Impl;
import org.apache.uima.dde.internal.DeploymentDescriptorEditor;
import org.apache.uima.dde.internal.Messages;
import org.apache.uima.dde.internal.page.MasterDetails;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.MetaDataObject;
import org.apache.uima.taeconfigurator.editors.ui.Utility;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.tools.internal.ui.forms.FormSection;
import org.apache.uima.tools.internal.ui.forms.FormSection2;
import org.apache.uima.tools.internal.uima.util.AETreeBuilder;
import org.apache.uima.tools.internal.uima.util.FormMessage;
import org.apache.uima.tools.internal.uima.util.WorkspaceResourceDialog;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLizable;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledPageBook;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;


public class AEMetaDataDetailsPage extends AbstractFormPart implements IDetailsPage {
  private DeploymentDescriptorEditor multiPageEditor;
  
  private MasterDetails masterPart;

  private boolean isDelegate;

  private Object selectedObject = null;

  private IManagedForm mform;

//  private ScrolledPageBook myScrolledPageBook = null; // used to support Section
  
  private Section sectionAEMetaDataDetails;

  private Button asMode;

  private Spinner scaleout;

  private Label casMultiplierLabelRemote;

  private Spinner casMultiplierRemote;

  private Label casMultiplierLabel;

  private Spinner casMultiplier;

  private ErrorConfigDetailsPage errorConfigDetails;

  private Button deploymentCoLocated;

  private Button deploymentRemote;

  private StackLayout stackLayout;

  private Composite stackLayoutComposite;

  private Composite compositeCoLocatedSetting;

  private Composite compositeRemoteSetting;

  private MetaDataObject currentMetaDataObject;

  // For Remote Deployment

  private DecoratedField brokerUrlDecoField;
  
  private DecoratedField endPointDecoField;

  private FieldDecoration decorationBrokerUrl;

  private FieldDecoration decorationEndPoint;

  protected Text brokerUrl;

  protected Text endPoint;

  protected CCombo remoteQueueLocation;

  protected Label serializerMethod;

  private boolean ignoreUpdate = false; // Used to update Text without setting dirty flag
  
  /** ********************************************************************** */

  private ModifyListener fModifyListener = new ModifyListener() {

    public void modifyText(ModifyEvent e) {
      Object source = e.getSource();

      if (source == brokerUrl) {
        ((RemoteAEDeploymentMetaData) currentMetaDataObject).getInputQueue().setBrokerURL(brokerUrl.getText().trim());

      } else if (source == endPoint) {
        ((RemoteAEDeploymentMetaData) currentMetaDataObject).getInputQueue().setEndPoint(endPoint.getText().trim());

      }
      isValid();
      if (!ignoreUpdate) {
        multiPageEditor.setFileDirty();
      }
    }
  };

  protected SelectionListener asynAggregateListener = new SelectionAdapter() {

    public void widgetSelected(SelectionEvent e) {
      if (e.getSource() == asMode) {
        if (asMode.getSelection()) {
          changeToAsyncAggregate(true);
        } else {
          changeToAsyncAggregate(false);
        }
      } else if (e.getSource() == scaleout) {
        // Trace.err("scaleout: " + scaleout.getSelection());
        updateScaleOut(scaleout.getSelection());

      } else if (e.getSource() == casMultiplier) {
        updateCasMultiplierPoolSize(casMultiplier.getSelection());

      } else if (e.getSource() == casMultiplierRemote) {
        updateCasMultiplierPoolSize(casMultiplierRemote.getSelection());

      } else if (e.getSource() == remoteQueueLocation) {
        String location;
        if (remoteQueueLocation.getText().equals("no")) {
          location = "local";
        } else {
          location = "remote";
        }
        ((RemoteAEDeploymentMetaData) currentMetaDataObject).setReplyQueueLocation(location);
      }
      multiPageEditor.setFileDirty();
    }
  };

  protected SelectionListener deploymentListener = new SelectionAdapter() {

    public void widgetSelected(SelectionEvent e) {
      Composite switchTo = null;
      if (e.getSource() == deploymentCoLocated) {
        if (deploymentCoLocated.getSelection()) {
          // Trace.err("deploymentCoLocated");
          switchTo = compositeCoLocatedSetting;
        }
      } else if (e.getSource() == deploymentRemote) {
        if (deploymentRemote.getSelection()) {
          // Trace.err("deploymentRemote");
          switchTo = compositeRemoteSetting;
        }
      }
      if (switchTo != null && stackLayout.topControl != switchTo) {
        if (currentMetaDataObject instanceof AEDeploymentMetaData) {
          // Switch to Remote
          AEDeploymentMetaData parent = ((AEDeploymentMetaData) currentMetaDataObject).getParent();
          if (parent == null) {
            Trace.err("parent == null");
            return;
          }
          int index = parent.getDelegates()
                  .indexOf((DeploymentMetaData_Impl) currentMetaDataObject);

          currentMetaDataObject = (MetaDataObject) AETreeBuilder
                  .createRemoteAEDeploymentMetaData((AEDeploymentMetaData) currentMetaDataObject);
          index = parent.getDelegates().replaceDelegate(index,
                  (DeploymentMetaData_Impl) currentMetaDataObject);

        } else if (currentMetaDataObject instanceof RemoteAEDeploymentMetaData) {
          AEDeploymentMetaData parent = ((RemoteAEDeploymentMetaData) currentMetaDataObject)
                  .getParent();
          if (parent == null) {
            Trace.err("parent == null");
            return;
          }
          int index = parent.getDelegates()
                  .indexOf((DeploymentMetaData_Impl) currentMetaDataObject);

          currentMetaDataObject = (MetaDataObject) AETreeBuilder
                  .createAEDeploymentMetaData((RemoteAEDeploymentMetaData) currentMetaDataObject);
          index = parent.getDelegates().replaceDelegate(index,
                  (DeploymentMetaData_Impl) currentMetaDataObject);
        }
        masterPart.refresh();

        stackLayout.topControl = switchTo;
        stackLayoutComposite.layout();

        multiPageEditor.setFileDirty();
      }
    }

  };

  /** ********************************************************************** */

  public AEMetaDataDetailsPage(DeploymentDescriptorEditor editor, IManagedForm mform,
          MasterDetails master, boolean isDelegate) {
    this.multiPageEditor = editor;
    this.mform = mform;
    this.masterPart = master;
    this.isDelegate = isDelegate;
  }

  /** ********************************************************************** */

  //
  // Note: "parent" is a "LayoutComposite" created by "ScrolledPageBook pageBook"
  // in "DetailsPart".
  // The parent of "parent" is "WrappedPageBook pageBook".
  // The grand-parent of "parent" is ScrolledPageBook which needs to be
  // "reflowed" when Section is expanded/collapsed.
  public void createContents(Composite parent) {
    // Get ScrolledPageBook
    Control c = parent.getParent();
    while (!(c instanceof ScrolledPageBook)) {
      c = c.getParent();
    }
//    myScrolledPageBook = (ScrolledPageBook) c;

    // Set Layout for "parent"
    TableWrapLayout layout = new TableWrapLayout();
    layout.topMargin = 0;
    layout.leftMargin = 5;
    layout.rightMargin = 2;
    layout.bottomMargin = 2;
    parent.setLayout(layout);

    FormToolkit toolkit = mform.getToolkit();

    createIdentitySection(parent, toolkit);
    errorConfigDetails = new ErrorConfigDetailsPage(multiPageEditor, mform, isDelegate);
    errorConfigDetails.createConfigurationsSection(parent, toolkit);
  }

  private Section createIdentitySection(Composite parent, FormToolkit toolkit) {
    TableWrapData td;

    sectionAEMetaDataDetails = FormSection.createTableWrapDataSection(toolkit, parent, 
            Section.TWISTIE,
            Messages.DDE_AEConfigPage_AEConfig_Section_Title, 
            "Set the properties of ...", 10, 5,
            TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1);
    final SectionPart spart = new SectionPart(sectionAEMetaDataDetails);
    mform.addPart(spart);
    spart.initialize(mform); // Need this code. Otherwise, exception in SectionPart !!!
    sectionAEMetaDataDetails.setExpanded(true);

    // /////////////////////////////////////////////////////////////////////

    Composite sectionClient = toolkit.createComposite(sectionAEMetaDataDetails);
    TableWrapLayout tl = new TableWrapLayout();
    tl.numColumns = 2;
    tl.leftMargin = 0;
    tl.rightMargin = 0;
    tl.topMargin = 10;
    tl.bottomMargin = 0;
    sectionClient.setLayout(tl);
    sectionAEMetaDataDetails.setClient(sectionClient);

    // Co-located or Remote
    // toolkit.createLabel(sectionClient, "", SWT.NONE);
    Composite colocatedOrRemote = toolkit.createComposite(sectionClient, SWT.NONE);
    GridLayout gl = new GridLayout(3, true);
    gl.marginWidth = 0;
    colocatedOrRemote.setLayout(gl);
    td = new TableWrapData(TableWrapData.FILL);
    td.colspan = 2;
    td.grabHorizontal = true;
    td.indent = 0;
    colocatedOrRemote.setLayoutData(td);
    Label label = toolkit.createLabel(colocatedOrRemote, "Deployment:", SWT.NONE);
    label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
    deploymentCoLocated = toolkit.createButton(colocatedOrRemote, "Co-located", SWT.RADIO);
    deploymentRemote = toolkit.createButton(colocatedOrRemote, "Remote", SWT.RADIO);
    deploymentCoLocated.setSelection(true);
    deploymentCoLocated.addSelectionListener(deploymentListener);
    deploymentRemote.addSelectionListener(deploymentListener);

    stackLayoutComposite = new Composite(sectionClient, SWT.NONE);
    stackLayout = new StackLayout();
    stackLayoutComposite.setLayout(stackLayout);
    td = new TableWrapData(TableWrapData.FILL);
    td.colspan = 2;
    td.grabHorizontal = true;
    td.indent = 0;
    stackLayoutComposite.setLayoutData(td);

    compositeCoLocatedSetting = toolkit.createComposite(stackLayoutComposite, SWT.NONE);
    gl = new GridLayout(2, false);
    gl.marginWidth = 0;
    compositeCoLocatedSetting.setLayout(gl);
    GridData gd = new GridData(GridData.FILL_BOTH);
    compositeCoLocatedSetting.setLayoutData(gd);

    compositeRemoteSetting = toolkit.createComposite(stackLayoutComposite, SWT.NONE);
    gl = new GridLayout(2, false);
    gl.marginWidth = 0;
    compositeRemoteSetting.setLayout(gl);
    gd = new GridData(GridData.FILL_BOTH);
    compositeRemoteSetting.setLayoutData(gd);

    // /////////////////////////////////////////////////////////////////////

    // Run in AS mode
    asMode = toolkit.createButton(compositeCoLocatedSetting, "Run as AS aggregate", SWT.CHECK);
    gd = new GridData();
    gd.horizontalSpan = 2;
    asMode.setLayoutData(gd);
    asMode.addSelectionListener(asynAggregateListener);

    // <scaleout numberOfInstances="1"/> <!-- optional -->
    scaleout = FormSection2.createLabelAndSpinner(toolkit, compositeCoLocatedSetting,
            "Number of replicated instances:", SWT.BORDER, 1, Integer.MAX_VALUE, false);
    scaleout.setSelection(1);
    scaleout.addSelectionListener(asynAggregateListener);

    // <casMultiplier poolSize="5"/> <!-- optional -->
    casMultiplierLabel = toolkit.createLabel(compositeCoLocatedSetting,
            "Pool size for CAS Multiplier:");
    casMultiplier = FormSection2.createLabelAndSpinner(toolkit, compositeCoLocatedSetting,
            casMultiplierLabel, SWT.BORDER, 0, Integer.MAX_VALUE, false);
    casMultiplier.setSelection(0);
    casMultiplier.addSelectionListener(asynAggregateListener);

    // /////////////////////////////////////////////////////////////////////

    brokerUrlDecoField = FormSection2.createLabelAndDecoratedText(toolkit, 
            compositeRemoteSetting, "Broker URL for remote service:", 
            currentMetaDataObject == null ?
                    "":((RemoteAEDeploymentMetaData) currentMetaDataObject).getInputQueue().getBrokerURL(), SWT.WRAP, 10, 0);
    
    // Create an error decoration
    decorationBrokerUrl = FormSection2.registerFieldDecoration("brokerUrl2",
                  "The broker URL cannot be empty");
    brokerUrlDecoField.addFieldDecoration(decorationBrokerUrl, SWT.LEFT | SWT.TOP, false);    
    brokerUrl = (Text) brokerUrlDecoField.getControl();
    brokerUrl.addModifyListener(fModifyListener);  

    endPointDecoField = FormSection2.createLabelAndDecoratedText(toolkit, 
            compositeRemoteSetting, "Queue name for remote service:", 
            currentMetaDataObject == null ?
                    "":((RemoteAEDeploymentMetaData) currentMetaDataObject).getInputQueue().getEndPoint(), SWT.WRAP, 10, 0);
    endPoint = (Text) endPointDecoField.getControl();
    endPoint.addModifyListener(fModifyListener);
    decorationEndPoint =
      FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
    decorationEndPoint.setDescription("The name of the queue cannot be empty");
    endPointDecoField.addFieldDecoration(decorationEndPoint, SWT.LEFT | SWT.TOP, false);    

    // Create pop-up context menu
//    createContextMenu(brokerUrl);
//    createContextMenu(endPoint);
    
    remoteQueueLocation = FormSection.createLabelAndCCombo(toolkit, compositeRemoteSetting,
            "Service client is inside firewall:", SWT.BORDER | SWT.FLAT | SWT.READ_ONLY);
    remoteQueueLocation.add("no");  // local
    remoteQueueLocation.add("yes"); // remote
    remoteQueueLocation.select(0);
    remoteQueueLocation.addSelectionListener(asynAggregateListener);

    // <casMultiplier poolSize="5"/> <!-- optional -->
    casMultiplierLabelRemote = toolkit.createLabel(compositeRemoteSetting,
            "Pool size of CasMultiplier:");
    casMultiplierRemote = FormSection2.createLabelAndSpinner(toolkit, compositeRemoteSetting,
            casMultiplierLabelRemote, SWT.BORDER, 0, Integer.MAX_VALUE, false);
    casMultiplierRemote.setSelection(0);
    casMultiplierRemote.addSelectionListener(asynAggregateListener);

    stackLayout.topControl = compositeCoLocatedSetting;

    return sectionAEMetaDataDetails;
  }

  protected void displayDetails(AEDeploymentMetaData obj) {    
    sectionAEMetaDataDetails.setRedraw(false);
    sectionAEMetaDataDetails.setText(Messages.DDE_AEConfigPage_AEConfig_Section_Title
            + " - " + obj.getKey());
    sectionAEMetaDataDetails.setExpanded(false);
    sectionAEMetaDataDetails.setExpanded(true);
    sectionAEMetaDataDetails.setRedraw(true);
    
    if (obj.isTopAnalysisEngine()) {
      deploymentRemote.setEnabled(false);
      deploymentCoLocated.setEnabled(false);
    } else {
      deploymentRemote.setEnabled(true);
      deploymentCoLocated.setEnabled(true);
      if (!deploymentCoLocated.getSelection()) {
        deploymentRemote.setSelection(false);
        deploymentCoLocated.setSelection(true);
      }
    }

    if (stackLayout.topControl != compositeCoLocatedSetting) {
      stackLayout.topControl = compositeCoLocatedSetting;
      stackLayoutComposite.layout();
    }
    if (obj.isAsync()) {
      scaleout.setSelection(1);
      scaleout.setEnabled(false);
    } else {
      scaleout.setEnabled(true);
      scaleout.setSelection(obj.getNumberOfInstances());
    }
    
    ResourceSpecifier rs = obj.getResourceSpecifier();
    showStatus(null, IMessageProvider.ERROR);
    if (rs != null) {
      // Is AnalysisEngineDescription ?
      if (rs instanceof AnalysisEngineDescription) {
        AnalysisEngineDescription aed = (AnalysisEngineDescription) rs;

        // Is CAs Multiplier ?
        if (AETreeBuilder.isCASMultiplier(aed)) {
          // Trace.err("CASMultiplier pool size: " + obj.getCasMultiplierPoolSize());
          casMultiplierLabel.setVisible(true);
          casMultiplier.setVisible(true);
          casMultiplier.setSelection(obj.getCasMultiplierPoolSize());
        } else {
          casMultiplierLabel.setVisible(false);
          casMultiplier.setVisible(false);
        }
        // Is Primitive ?
        if (aed.isPrimitive()) {
          asMode.setEnabled(false);
          asMode.setSelection(false);
        } else {
          asMode.setEnabled(true);
          asMode.setSelection(obj.isAsync());
        }
      } else {
        // CAS Consumer, ...
        casMultiplierLabel.setVisible(false);
        casMultiplier.setVisible(false);
        asMode.setEnabled(false);
        asMode.setSelection(false);
      }
    } else {
      String parentKey = null;
      if (obj.getParent() != null) {
        parentKey = obj.getParent().getKey();          
      } else {
        if (obj.isTopAnalysisEngine()) {
          showStatus("The top descriptor is not specified.", IMessageProvider.ERROR);
          return;
        }
      }
      if (parentKey == null) {
        parentKey = "Top Analysis Engine";
      }
      Trace.err("The analysis engine's key=\"" + obj.getKey()
              + "\" is not valid for the aggregate \"" + parentKey + "\"");
      showStatus("The analysis engine's key=\"" + obj.getKey()
              + "\" is not valid for the aggregate \"" + parentKey + "\"", IMessageProvider.ERROR);
    }
    // casMultiplier.setVisible(show);

    // Error Config
    AsyncAEErrorConfiguration errorConfig = obj.getAsyncAEErrorConfiguration();
    if (errorConfig != null) {
      errorConfigDetails.displayDetails((DeploymentMetaData_Impl)obj, errorConfig);
    } else {
      Trace.bug("errorConfig == null");
    }
    
  }

  protected void displayDetails(RemoteAEDeploymentMetaData obj) {
    ignoreUpdate = true;
    sectionAEMetaDataDetails.setRedraw(false);
    sectionAEMetaDataDetails.setText(Messages.DDE_AEConfigPage_AEConfig_Section_Title
            + " - " + obj.getKey());
    sectionAEMetaDataDetails.setExpanded(false);
    sectionAEMetaDataDetails.setExpanded(true);
    sectionAEMetaDataDetails.setRedraw(true);
    
    deploymentRemote.setEnabled(true);
    deploymentCoLocated.setEnabled(true);
    if (!deploymentRemote.getSelection()) {
      deploymentCoLocated.setSelection(false);
      deploymentRemote.setSelection(true);
    }

    if (stackLayout.topControl != compositeRemoteSetting) {
      stackLayout.topControl = compositeRemoteSetting;
      stackLayoutComposite.layout();
    }
    if (obj.getInputQueue() != null) {
      brokerUrl.setText(obj.getInputQueue().getBrokerURL());
      endPoint.setText(obj.getInputQueue().getEndPoint());
    }

    if (obj.getReplyQueueLocation() != null) {
      String yesOrno;
      if (obj.getReplyQueueLocation().equals("local")) {
        yesOrno = "no";
      } else {
        yesOrno = "yes";
      }

      int i = remoteQueueLocation.indexOf(yesOrno);
      if (i >= 0) {
        remoteQueueLocation.select(i);
      }
    }

    if (obj.getResourceSpecifier() != null) {
      // Is AnalysisEngineDescription ?
      if (obj.getResourceSpecifier() instanceof AnalysisEngineDescription) {
        showStatus(null, IMessageProvider.ERROR); // clear error msg
        AnalysisEngineDescription aed = (AnalysisEngineDescription) obj.getResourceSpecifier();
        // Is CAs Multiplier ?
        if (AETreeBuilder.isCASMultiplier(aed)) {
          // Trace.err("CASMultiplier");
          casMultiplierLabelRemote.setVisible(true);
          casMultiplierRemote.setVisible(true);
          casMultiplierRemote.setSelection(obj.getCasMultiplierPoolSize());
        } else {
          casMultiplierLabelRemote.setVisible(false);
          casMultiplierRemote.setVisible(false);
        }
      }
    }

    // Error Config
    AsyncAEErrorConfiguration errorConfig = obj.getAsyncAEErrorConfiguration();
    if (errorConfig != null) {
      errorConfigDetails.displayDetails((DeploymentMetaData_Impl) obj, errorConfig);
    }
    ignoreUpdate = false;
  }

  protected void isValid() {
    if (brokerUrl.getText().trim().length() == 0) {
      brokerUrlDecoField.showDecoration(decorationBrokerUrl);
      // return;
    } else {
      brokerUrlDecoField.hideDecoration(decorationBrokerUrl);      
    }

    if (endPoint.getText().trim().length() == 0) {
      endPointDecoField.showDecoration(decorationEndPoint);
      // return;
    } else {
      endPointDecoField.hideDecoration(decorationEndPoint);
    }
  }

  protected void showStatus(String msg, int msgType) {
    Trace.err(msg==null? "Clear msg": msg);
    FormMessage.setMessage(mform.getForm().getForm(), msg, msgType);    
  }

  /** ********************************************************************** */

  public void commit(boolean onSave) {

    super.commit(onSave);
  }

  public void dispose() {
    // TODO Auto-generated method stub

  }

  public void initialize(IManagedForm form) {
    this.mform = form;
  }

  public boolean isDirty() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isStale() {
    // TODO Auto-generated method stub
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#refresh()
   */
  public void refresh() {
    update();
    super.refresh();
  }

  public void setFocus() {
    // TODO Auto-generated method stub

  }

  public boolean setFormInput(Object input) {
    // TODO Auto-generated method stub
    return false;
  }

  public void selectionChanged(IFormPart part, ISelection selection) {
    Trace.err();
    if (selection == null || !(selection instanceof IStructuredSelection)) {
      return;
    }
    IStructuredSelection ssel = (IStructuredSelection) selection;
    if (ssel.size() != 1) {
      return;
    }

    selectedObject = ssel.getFirstElement();
    if (selectedObject instanceof AEDeploymentMetaData) {
      currentMetaDataObject = (MetaDataObject) selectedObject;
      displayDetails((AEDeploymentMetaData) selectedObject);
    } else if (selectedObject instanceof RemoteAEDeploymentMetaData) {
      currentMetaDataObject = (MetaDataObject) selectedObject;
      displayDetails((RemoteAEDeploymentMetaData) selectedObject);
    } else {
      selectedObject = null;
    }
  }

  private void updateScaleOut(int value) {
    if (currentMetaDataObject instanceof AEDeploymentMetaData) {
      ((AEDeploymentMetaData) currentMetaDataObject).setNumberOfInstances(value);

    } else if (currentMetaDataObject instanceof RemoteAEDeploymentMetaData) {
      // ((RemoteAEDeploymentMetaData) currentMetaDataObject).setNumberOfInstances(value);

    }
    masterPart.refresh();
    multiPageEditor.setFileDirty();
  }

  private void updateCasMultiplierPoolSize(int value) {
    if (currentMetaDataObject instanceof AEDeploymentMetaData) {
      ((AEDeploymentMetaData) currentMetaDataObject).setCasMultiplierPoolSize(value);

    } else if (currentMetaDataObject instanceof RemoteAEDeploymentMetaData) {
      ((RemoteAEDeploymentMetaData) currentMetaDataObject).setCasMultiplierPoolSize(value);

    }
    multiPageEditor.setFileDirty();
  }

  private void update() {
    // id.setValue(input!=null?input.getId():null, true);
    // name.setValue(input!=null?input.getName():null, true);
    // point.setValue(input!=null?input.getPoint():null, true);
  }

  protected void changeToAsyncAggregate(boolean toAsyncAggreagte) {
    AEDeploymentMetaData metaData = (AEDeploymentMetaData) currentMetaDataObject;
    if (toAsyncAggreagte) {
      // Resolve delegates
      try {
        metaData.resolveDelegates(multiPageEditor.cde.createResourceManager(), false);
      } catch (InvalidXMLException e) {
        e.printStackTrace();
        Utility.popMessage(Messages.getString("InvalidXMLException"), //$NON-NLS-1$
                multiPageEditor.cde.getMessagesToRootCause(e), MessageDialog.ERROR);
        multiPageEditor.switchToBadSource(false);
        return;
      }
      if (metaData.getNumberOfInstances() != 1) {
        metaData.setNumberOfInstances(1);
      }
    }
    metaData.setAsync(toAsyncAggreagte);

    masterPart.refresh();
    multiPageEditor.setFileDirty();
  }
  
  /** ********************************************************************** */
  /*                        Context Sensitive Pop-up Menu                    */
  /** ********************************************************************** */

  /**
   * Creates a pop-up menu on the given control
   * 
   * @param menuControl
   *          the control with which the pop-up menu will be associated
   */
  private void createContextMenu(Control menuControl) {
    MenuManager menuMgr = new MenuManager("#PopUp"); //$NON-NLS-1$
    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener() {
      public void menuAboutToShow(IMenuManager mgr) {
        fillContextMenu(mgr);
      }
    });
    Menu menu = menuMgr.createContextMenu(menuControl);
    menuControl.setMenu(menu);

    // register the context menu such that other plugins may contribute to it
    // 
    // __workbenchPartSite.registerContextMenu(menuMgr, _catTreeViewer);
  }

  /**
   * Adds items to the context menu
   * 
   * @param menu
   *          The menu to contribute to
   */
  protected void fillContextMenu(IMenuManager menu) {
    Action action = new Action() {
      public void run() {
        handleBrowseDD();
      }
    };
    action.setText("Browse DD ...");
    action.setEnabled(true);
    menu.add(action);

  } // fillContextMenu
  
  protected void handleBrowseDD() {
    IResource resource = WorkspaceResourceDialog.getWorkspaceResourceElement(multiPageEditor.cde.getEditorSite().getShell(),
            getWorkspaceRoot(), "Select UIMA deployment descriptor",
            "Select Analysis Engine's deployment descriptor file");
    if (resource != null) {
      XMLizable xmlizable = UimaApplication_Impl.parseUimaXmlDescriptor(resource.getLocation().toOSString());
      if (xmlizable == null || !(xmlizable instanceof AEDeploymentDescription_Impl)) {
          // Trace.err("xmlizable: " + xmlizable.getClass().getName());
          return;
      }
      AEService aeService = ((AEDeploymentDescription_Impl) xmlizable).getAeService();
      brokerUrl.setText(aeService.getBrokerURL());
      endPoint.setText(aeService.getEndPoint());          
    }
  }
    /**
     * Convenience method to get the workspace root.
     */
    private IWorkspaceRoot getWorkspaceRoot() {
        return ResourcesPlugin.getWorkspace().getRoot();
    }

  /** ********************************************************************** */

//  public void modelChanged(IModelChangedEvent event) {
//    if (event.getChangeType() == IModelChangedEvent.CHANGE) {
//      Object obj = event.getChangedObjects()[0];
//      if (obj.equals(selectedObject))
//        refresh();
//    }
//  }

}
