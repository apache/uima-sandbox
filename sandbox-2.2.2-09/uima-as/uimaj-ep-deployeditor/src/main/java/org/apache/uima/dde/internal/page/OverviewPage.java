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

package org.apache.uima.dde.internal.page;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.deployment.AEDeploymentDescription;
import org.apache.uima.aae.deployment.AEService;
import org.apache.uima.aae.deployment.AsyncAEErrorConfiguration;
import org.apache.uima.aae.deployment.AsyncAggregateErrorConfiguration;
import org.apache.uima.aae.deployment.impl.AEDelegates_Impl;
import org.apache.uima.aae.deployment.impl.AEDeploymentMetaData_Impl;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.dde.internal.DeploymentDescriptorEditor;
import org.apache.uima.dde.internal.Messages;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.URISpecifier;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.taeconfigurator.editors.ui.FileAndShortName;
import org.apache.uima.taeconfigurator.editors.ui.Utility;
import org.apache.uima.taeconfigurator.files.MultiResourceSelectionDialog;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.tools.internal.cde.uima.util.UimaDescriptionUtils;
import org.apache.uima.tools.internal.ui.forms.FormSection;
import org.apache.uima.tools.internal.ui.forms.FormSection2;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLizable;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PopupList;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;


/**
 * 
 * 
 */
public class OverviewPage extends AbstractHeaderPage {

  static public final String PAGE_TITLE = "Overview";

//  static protected OverviewPage instance = null;

  private ScrolledForm form;

//  private FormToolkit toolkit;
  
  private DecoratedField brokerUrlDecoField;
  
  private DecoratedField endPointDecoField;

  private DecoratedField topDescriptorDecoField;

  private FieldDecoration decorationBrokerUrl;

  private FieldDecoration decorationEndPoint;

  private FieldDecoration decorationTopDescriptor;
  
  private Text brokerUrl;

  private Text endPoint;
  
  private Spinner initialFsHeapSize;

  private Spinner casPoolSize;

  private Text topDescriptorField;

  private Label importByNameOrLocation;
  
//  private Button importByLocation;

//  private Button importByName;
  
  private boolean isImportByLocation = true; // default

  private DeploymentDescriptorEditor multiPageEditor;

  private AEDeploymentDescription aeDeploymentDescription;

  private AEService aeService;
    
  private Text name;
  private Text description;
  private Text version;
  private Text vendor;
  private boolean ignoreUpdate = false; // Used to update Text without setting dirty flag
  private Action openAction;
  
  /** ********************************************************************** */

  private ModifyListener fModifyListener = new ModifyListener() {

    public void modifyText(ModifyEvent e) {
      Object source = e.getSource();

      if (source == brokerUrl) {
        aeService.setBrokerURL(brokerUrl.getText().trim());

      } else if (source == endPoint) {
        aeService.setEndPoint(endPoint.getText().trim());

      } else if (source == topDescriptorField) {

      } else if (source == name) {
        aeDeploymentDescription.setName(name.getText().trim());
        
      } else if (source == description) {
        aeDeploymentDescription.setDescription(description.getText().trim());
        
      } else if (source == version) {
        aeDeploymentDescription.setVersion(version.getText().trim());
        
      } else if (source == vendor) {
        aeDeploymentDescription.setVendor(vendor.getText().trim());       
      }
      isValid();

      if (!ignoreUpdate) {
        multiPageEditor.setFileDirty();
      }
    }
  };
  
  static public String fromAbsoluteUrl2Location (IFile parentIFile, String fileFullPath) {
    // Import by Location
    try {
      int index= fileFullPath.indexOf("file:/");
      if (index != -1) {
        fileFullPath = fileFullPath.substring(6);
        // Trace.err("fileFullPath0: " + fileFullPath);
      }
      // Trace.err("fileFullPath: " + fileFullPath);
      Import importDescriptor = UimaDescriptionUtils.createByLocationImport(parentIFile,
            fileFullPath);
      return importDescriptor.getLocation();
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  protected SelectionListener asynAggregateListener = new SelectionAdapter() {

    public void widgetSelected(SelectionEvent event) {
      if (event.getSource() == casPoolSize) {
        aeDeploymentDescription.setCasPoolSize(casPoolSize.getSelection());
        multiPageEditor.setFileDirty();
      } else if (event.getSource() == initialFsHeapSize) {
        aeDeploymentDescription.setInitialFsHeapSize(initialFsHeapSize.getSelection());
        multiPageEditor.setFileDirty();
      }
    }
  };

  /** ********************************************************************** */

  /**
   * @param editor
   * @param id
   * @param title
   */
  public OverviewPage(DeploymentDescriptorEditor editor, String id, String title) {
    super(editor.cde, id, title);
    multiPageEditor = editor;
    aeDeploymentDescription = multiPageEditor.getAEDeploymentDescription();
    aeService = aeDeploymentDescription.getAeService();
  }

//  static public OverviewPage getInstance() {
//    return instance;
//  }
  
  /**
   * Called by the framework to fill in the contents
   */
  protected void createFormContent(IManagedForm managedForm) {
    super.createFormContent(managedForm);
    form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    form.setText(PAGE_TITLE);

    ScrolledForm form = managedForm.getForm();
    FormColors colors = toolkit.getColors();
    colors.initializeSectionToolBarColors();
    Color gbg = colors.getColor(FormColors.TB_GBG);
    Color bg = colors.getBackground();
    form.getForm().setTextBackground(new Color[] { bg, gbg }, new int[] { 100 }, true);
    // form.getForm().setSeparatorVisible(true);

    fillBody(managedForm, toolkit);
  }

  private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
    Composite body = managedForm.getForm().getBody();
    body.setLayout(new GridLayout(1, false)); // this is required !
    
    Composite compTop = toolkit.createComposite(body);
    compTop.setLayout(new GridLayout(1, false));
    compTop.setLayoutData(new GridData(GridData.FILL_BOTH));
    Control c = compTop.getParent();
    while (!(c instanceof ScrolledComposite)) {
      c = c.getParent();
    }
    ((GridData) compTop.getLayoutData()).widthHint = c.getSize().x;
    ((GridData) compTop.getLayoutData()).heightHint = c.getSize().y;

    SashForm sashFormOverview = new SashForm(compTop, SWT.VERTICAL);
    // toolkit.adapt(sashForm, true, true); // makes the bar invisible (white)
    sashFormOverview.setLayoutData(new GridData(GridData.FILL_BOTH)); // needed

    createToolBarActions(managedForm, sashFormOverview, false);

    ScrolledComposite leftScrolled = new ScrolledComposite(sashFormOverview, SWT.H_SCROLL | SWT.V_SCROLL); // toolkit.createComposite(sashFormOverview);
    leftScrolled.setExpandHorizontal(true);
    leftScrolled.setExpandVertical(true);
    leftScrolled.setLayoutData(new GridData(GridData.FILL_BOTH));
    leftScrolled.setLayout(new GridLayout(1, false));
    Composite leftPane = toolkit.createComposite(leftScrolled);
    TableWrapLayout layout = new TableWrapLayout();
    layout.verticalSpacing = 20;
    leftPane.setLayout(layout);
    leftPane.setLayoutData(new GridData(GridData.FILL_BOTH));
    leftScrolled.setContent(leftPane);

    ScrolledComposite rightScrolled = new ScrolledComposite(sashFormOverview, SWT.H_SCROLL | SWT.V_SCROLL); // toolkit.createComposite(sashFormOverview);
    rightScrolled.setExpandHorizontal(true);
    rightScrolled.setExpandVertical(true);
    rightScrolled.setLayoutData(new GridData(GridData.FILL_BOTH));
    rightScrolled.setLayout(new GridLayout(1, false));
    Composite rightPane = toolkit.createComposite(rightScrolled);
    layout = new TableWrapLayout();
    layout.verticalSpacing = 20;
    rightPane.setLayout(layout);
    rightPane.setLayoutData(new GridData(GridData.FILL_BOTH));
    rightScrolled.setContent(rightPane);
    
    createIdentitySection(rightPane, toolkit);
    createServiceSection(leftPane, toolkit);
    leftScrolled.setMinSize(leftPane.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    rightScrolled.setMinSize(rightPane.computeSize(SWT.DEFAULT, SWT.DEFAULT));

  }

  /** ********************************************************************** */

  private Section createIdentitySection(Composite parent, FormToolkit toolkit) {
    Section section = FormSection.createTableWrapDataSection(toolkit, parent, Section.TREE_NODE,
            Messages.DDE_OverviewPage_General_Section_Title,
            Messages.DDE_OverviewPage_General_Section_Description, 10, 3, TableWrapData.FILL_GRAB,
            TableWrapData.FILL_GRAB, 1, 1);

    section.setExpanded(true);
    section.addExpansionListener(new ExpansionAdapter() {
      public void expansionStateChanged(ExpansionEvent e) {
        form.reflow(true);
      }
    });

    // //////////////////

    Composite sectionClient = toolkit.createComposite(section);
    section.setClient(sectionClient);
    TableWrapLayout tl = new TableWrapLayout();
    tl.numColumns = 2;
    // gl.makeColumnsEqualWidth = true;
    tl.bottomMargin = 10;
    sectionClient.setLayout(tl);

    // Name
    name = FormSection.createLabelAndText(toolkit, sectionClient, "Name:",
            aeDeploymentDescription.getName(), SWT.WRAP, 10, 0);

    // Description
    description = FormSection.createLabelAndText(toolkit, sectionClient, "Description:",
            aeDeploymentDescription.getDescription(), SWT.V_SCROLL | SWT.MULTI | SWT.WRAP, 10, 60);

    version = FormSection.createLabelAndText(toolkit, sectionClient, "Version:",
            aeDeploymentDescription.getVersion(), SWT.WRAP, 10, 0);

    vendor = FormSection.createLabelAndText(toolkit, sectionClient, "Vendor:",
            aeDeploymentDescription.getVendor(), SWT.WRAP, 10, 0);
    
    name.addModifyListener(fModifyListener);
    description.addModifyListener(fModifyListener);
    version.addModifyListener(fModifyListener);
    vendor.addModifyListener(fModifyListener);

    // ////////////////////////////////////////

    toolkit.paintBordersFor(sectionClient);

    return section;
  } // createIdentitySection

  private Section createServiceSection(Composite parent, final FormToolkit toolkit) {

    Section section = FormSection.createTableWrapDataSection(toolkit, parent, Section.TREE_NODE
            | Section.DESCRIPTION, Messages.DDE_OverviewPage_Service_Section_Title,
            Messages.DDE_OverviewPage_Service_Section_Description, 10, 3, TableWrapData.FILL_GRAB,
            TableWrapData.FILL_GRAB, 1, 1);

    // final SectionPart spart = new SectionPart(section);
    // mform.addPart(spart);
    // spart.initialize (mform); // Need this code. Otherwise, exception in SectionPart !!!

    section.setExpanded(true);
    section.addExpansionListener(new ExpansionAdapter() {
      public void expansionStateChanged(ExpansionEvent e) {
        form.reflow(true);
      }
    });

    // //////////////////

    Composite sectionClient = toolkit.createComposite(section);
    section.setClient(sectionClient);
    TableWrapLayout tl = new TableWrapLayout();
    tl.numColumns = 2;
    // gl.makeColumnsEqualWidth = true;
    tl.bottomMargin = 10;
    sectionClient.setLayout(tl);

    // <deployment protocol="jms" provider="activemq">
    brokerUrlDecoField = FormSection2.createLabelAndDecoratedText(toolkit, sectionClient, 
            "Broker URL for input queue:", 
            aeService.getBrokerURL(), SWT.WRAP, 10, 0);
    
    // Create an error decoration
    decorationBrokerUrl = FormSection2.registerFieldDecoration("brokerUrl",
                  "The broker URL cannot be empty");
    brokerUrlDecoField.addFieldDecoration(decorationBrokerUrl, SWT.LEFT | SWT.TOP, false);    
    
    brokerUrl = (Text) brokerUrlDecoField.getControl();
    brokerUrl.setToolTipText("Enter the URL for the input queue");
    brokerUrl.addModifyListener(fModifyListener);    

    endPointDecoField = FormSection2.createLabelAndDecoratedText(toolkit, sectionClient, 
            "Name for input queue:", aeService.getEndPoint(), SWT.WRAP, 10, 0);
    endPoint = (Text) endPointDecoField.getControl();
    endPoint.setToolTipText("Enter the name for the input queue");
    endPoint.addModifyListener(fModifyListener);
    decorationEndPoint =
      FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
    decorationEndPoint.setDescription("The name for the queue cannot be empty");
    endPointDecoField.addFieldDecoration(decorationEndPoint, SWT.LEFT | SWT.TOP, false);    
    
    // <casPool numberOfCASes="3"/> <!-- optional -->
//    prefetch = FormSection2.createLabelAndSpinner(toolkit, sectionClient,
//            "Number of requests that might be prefetched:", SWT.BORDER, 1, 
//            Integer.MAX_VALUE, false, FormSection2.MAX_DECORATION_WIDTH);
//    prefetch.setSelection(aeService.getPrefetch());
//    prefetch.addSelectionListener(asynAggregateListener);

    // <casPool numberOfCASes="3"/> <!-- optional -->
    casPoolSize = FormSection2.createLabelAndSpinner(toolkit, sectionClient,
            "Number of CASes in CAS pool:", SWT.BORDER, 1, 
            Integer.MAX_VALUE, false, FormSection2.MAX_DECORATION_WIDTH);
    casPoolSize.setSelection(aeDeploymentDescription.getCasPoolSize());
    casPoolSize.addSelectionListener(asynAggregateListener);

    // initialFsHeapSize (default size is 2M)
    initialFsHeapSize = FormSection2.createLabelAndSpinner(toolkit, sectionClient,
            "Initial size of CAS heap (in bytes):", SWT.BORDER, 1, 
            Integer.MAX_VALUE, false, FormSection2.MAX_DECORATION_WIDTH);
    initialFsHeapSize.setSelection(aeDeploymentDescription.getInitialFsHeapSize());
    initialFsHeapSize.addSelectionListener(asynAggregateListener);
    
    // ////////////////////////////////////////

    // Top descriptor
    topDescriptorDecoField = FormSection2.createLabelAndGridLayoutDecoratedContainer(toolkit, 
            sectionClient, "Top analysis engine descriptor", 2, 0, 0);
    Composite browsingGroup = (Composite) topDescriptorDecoField.getControl();
    decorationTopDescriptor = FormSection2.registerFieldDecoration("topDescriptorField",
        "Top descriptor cannot be empty");
    topDescriptorDecoField.addFieldDecoration(decorationTopDescriptor, SWT.LEFT | SWT.TOP, false);    

    toolkit.paintBordersFor(browsingGroup);

    topDescriptorField = toolkit.createText(browsingGroup, "", SWT.READ_ONLY);
    topDescriptorField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
            | GridData.GRAB_HORIZONTAL));
    String topDescriptor = null;
    Import imp = aeService.getImportDescriptor();
    if (imp != null) {
      String descr = imp.getLocation();
      if (descr == null) {
        isImportByLocation = false;
        descr = imp.getName();
      } else {
        isImportByLocation = true;
      }
      topDescriptor = descr;
    }
    topDescriptorField.setText(topDescriptor != null ? topDescriptor : "");
    topDescriptorField.addModifyListener(fModifyListener);

    // browse button
    Button browseDirectoriesButton = toolkit.createButton(browsingGroup, "B&rowse...", SWT.PUSH);
    browseDirectoriesButton.setLayoutData(new GridData());
    browseDirectoriesButton.addSelectionListener(new SelectionAdapter() {

      public void widgetSelected(SelectionEvent e) {
        MultiResourceSelectionDialog dialog = new MultiResourceSelectionDialog(
                form.getShell(), multiPageEditor.getFile().getProject().getParent(),
                "Component Engine Selection", multiPageEditor.getFile().getLocation(), multiPageEditor.cde);
        dialog.setTitle("Component Engine Selection");
        dialog.setMessage("Select one component engine from the workspace:");
        dialog.open();
        Object[] files = dialog.getResult();

        if (files != null && files.length > 0) {
          for (int i = 0; i < files.length; i++) {
            FileAndShortName fsn = new FileAndShortName(files[i]);
            // produceKeyAddDelegate(fsn.shortName, fsn.fileName, dialog.getAutoAddToFlow(),
            //         dialog.isImportByName);
            updateTopDescriptor(fsn.fileName, !dialog.isImportByName);
          }
        }
      }
    });

    topDescriptorField.addTraverseListener(new TraverseListener() {

      public void keyTraversed(TraverseEvent e) {
        if (e.detail == SWT.TRAVERSE_RETURN) {
          e.doit = false;
          // updateFileList(inputDocPathField.getText().trim());
        }
      }
    });

    topDescriptorField.addFocusListener(new FocusAdapter() {
      public void focusLost(org.eclipse.swt.events.FocusEvent e) {
        // updateFileList(inputDocPathField.getText().trim());
      }
    });
    
    createContextMenu();

    // Name or Location
    toolkit.createLabel(sectionClient, "", SWT.NONE);
    Composite nameOrLocation = toolkit.createComposite(sectionClient, SWT.NONE);
    nameOrLocation.setLayout(new GridLayout(2, false));
    TableWrapData td = new TableWrapData();
    td.colspan = 1;
    td.grabHorizontal = true;
    td.indent = 0;
    nameOrLocation.setLayoutData(td);
    toolkit.paintBordersFor(nameOrLocation);
    
    importByNameOrLocation = FormSection.createLabelAndLabel(toolkit, nameOrLocation, "Import by:",
            "(Name or Location)", 200, 20);
    if (imp != null) {
      if (isImportByLocation) {
        importByNameOrLocation.setText("Location");
      } else {
        importByNameOrLocation.setText("Name");
      }
    }
    toolkit.paintBordersFor(sectionClient);

    return section;
  } // createServiceSection

  private void createContextMenu() {
    // Create Open Action
    openAction = new Action("Open in new window") {
      public void run() {
        String xml = topDescriptorField.getText().trim();
        // Trace.err("Xml: " + xml);
        multiPageEditor.openTopLevelXmlDescriptor(aeService.getImportDescriptor());
      }
    };
    
    //  Create menu manager.
    MenuManager menuMgr = new MenuManager();
    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener() {
      public void menuAboutToShow(IMenuManager mgr) {
        fillContextMenu(mgr);
      }
    });
    //  Create menu.
    Menu menu = menuMgr.createContextMenu(topDescriptorField);
    topDescriptorField.setMenu(menu);
    
    //  Register menu for extension.
    // getSite().registerContextMenu(menuMgr, topDescriptorField);
  }
 
  private void fillContextMenu(IMenuManager mgr) {
    String xml = topDescriptorField.getText().trim();
    if (xml.length() > 0) {
      mgr.add(openAction);
    }
    // mgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
  }
  
  /** ********************************************************************** */

  protected void updateTopDescriptor(String fileFullPath, boolean byLocation) {
    // Import by Location or by Name ?
    Import importDescriptor = null;
    try {
      if (byLocation) {
        // Import by Location
        importDescriptor = UimaDescriptionUtils.createByLocationImport(multiPageEditor.getFile(),
              fileFullPath);
      } else {
        // Import by Name
        importDescriptor = UimaDescriptionUtils.createByNameImport(fileFullPath,
                multiPageEditor.cde.createResourceManager());        
      }
    } catch (MalformedURLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      return;
    }
    aeService.setImportDescriptor(importDescriptor);
    String relativeFile = UimaDescriptionUtils.getDescriptorFromImport(importDescriptor);
    try {
      ResourceSpecifier rs = aeService.resolveTopAnalysisEngineDescription(multiPageEditor.cde.createResourceManager(), false);
      if (rs != null) { 
        if (rs instanceof AnalysisEngineDescription) {
          AnalysisEngineDescription aed = (AnalysisEngineDescription) rs;
          
          AEDelegates_Impl delegates = new AEDelegates_Impl(aeService
                  .getAnalysisEngineDeploymentMetaData(multiPageEditor.cde.createResourceManager()));
          Map map = aed.getDelegateAnalysisEngineSpecifiers(multiPageEditor.cde.createResourceManager());
          for (Object m : map.entrySet()) {
            Map.Entry entry = (Map.Entry) m;
            // Trace.err("key: " + entry.getKey() + " ; " + entry.getValue().getClass().getName());
            AEDeploymentMetaData_Impl meta = new AEDeploymentMetaData_Impl();
            meta.setKey((String) entry.getKey());
  
            // Create a new Error Config
            if (entry.getValue() instanceof ResourceSpecifier) {
                Object obj = UIMAFramework.getResourceSpecifierFactory()
                                .createObject(AsyncAggregateErrorConfiguration.class);
                ((AsyncAEErrorConfiguration) obj).getGetMetadataErrors().setTimeout(0);
                meta.setAsyncAEErrorConfiguration((AsyncAEErrorConfiguration) obj);
            } else {
              Trace.bug("ResourceSpecifier == null for " + meta.getKey());
            }
  
            delegates.addDelegate(meta);
          }
  
          aeService.getAnalysisEngineDeploymentMetaData(multiPageEditor
                  .cde.createResourceManager()).setDelegates(delegates);
        }
      } else {
        Trace.err("Cannot resolve: " + relativeFile);
      }
    } catch (InvalidXMLException e) {
      // e.printStackTrace();
      Utility.popMessage(Messages.getString("InvalidXMLException"), //$NON-NLS-1$
      // Utility.popMessage("InvalidXMLException", //$NON-NLS-1$
              multiPageEditor.cde.getMessagesToRootCause(e), MessageDialog.ERROR);
      multiPageEditor.switchToBadSource(true);
      return;
    }

    topDescriptorField.setText(relativeFile);
    isImportByLocation = byLocation;
    if (isImportByLocation) {
      importByNameOrLocation.setText("Location");
    } else {
      importByNameOrLocation.setText("Name");
    }

    multiPageEditor.refresh();
    multiPageEditor.setFileDirty();
  }

  /** ********************************************************************** */

  public void setActive(boolean active) {
    if (active) {
      super.setActive(active);
      // Trace.err("Activate Overview");
      isValid();
    } else {
      // Trace.err("De-Activate Overview");
    }
  }

  protected void isValid() {
    // if (true) return;
    if (brokerUrl.getText().trim().length() == 0) {
      showStatus("The queue Broker Url is not specified", IMessageProvider.ERROR);
      brokerUrlDecoField.showDecoration(decorationBrokerUrl);
      // return;
    } else {
      brokerUrlDecoField.hideDecoration(decorationBrokerUrl);      
    }

    if (endPoint.getText().trim().length() == 0) {
      showStatus("The queue name is not specified", IMessageProvider.ERROR);
      endPointDecoField.showDecoration(decorationEndPoint);
      // return;
    } else {
      endPointDecoField.hideDecoration(decorationEndPoint);
    }

    if (topDescriptorField.getText().trim().length() == 0) {
      showStatus("The top-level analysis engine is not specified", IMessageProvider.ERROR);
      topDescriptorDecoField.showDecoration(decorationTopDescriptor);
      // return;
    } else {
      topDescriptorDecoField.hideDecoration(decorationTopDescriptor);
      // int err = validateFilePath(topDescriptorField.getText().trim());
      // if (err != 0) {
      // Trace.err("validateFilePath");
      // if (err == STATUS_FILE_NOT_SPECIFIED) {
      // // setErrorMessage("Analysis Engine's xml descriptor is not specified");
      // } else {
      // // setErrorMessage("Analysis Engine's xml descriptor does not exist");
      // showStatus("The specified descriptor \"" + topDescriptorField.getText().trim()
      // + "\" does not exist.", IMessageProvider.ERROR);
      // }
      // }
    }
    // showStatus(null, IMessageProvider.ERROR);
  }

  protected void showStatus(String msg, int msgType) {
    // Message.setMessage(form.getForm(), msg, msgType);
  }

  static public int validateFilePath(String filePath) {
    String path = filePath == null ? "" : filePath.trim();
    if (path.length() > 0) {
      File f = new File(path);
      if (f.exists() && f.isFile()) {
        return STATUS_IS_VALID;
      }
      // setErrorMessage("Working directory does not exist");
      return STATUS_FILE_NOT_EXIST;
    } else if (path.length() == 0) {
      return STATUS_FILE_NOT_SPECIFIED;
    }
    return STATUS_IS_VALID;
  }
  
  public void setInput (AEDeploymentDescription aeDD) {
    ignoreUpdate = true;
    aeDeploymentDescription = aeDD;
    aeService = aeDeploymentDescription.getAeService();
    
    name.setText(aeDeploymentDescription.getName());
    description.setText(aeDeploymentDescription.getDescription());
    version.setText(aeDeploymentDescription.getVersion());
    vendor.setText(aeDeploymentDescription.getVendor());

    brokerUrl.setText(aeService.getBrokerURL());
    endPoint.setText(aeService.getEndPoint());
    
    casPoolSize.setSelection(aeDeploymentDescription.getCasPoolSize());
    initialFsHeapSize.setSelection(aeDeploymentDescription.getInitialFsHeapSize());
    
    String topDescriptor = null;
    Import imp = aeService.getImportDescriptor();
    if (imp != null) {
      isImportByLocation = true;
      String descr = imp.getLocation();
      if (descr == null) {
        isImportByLocation = false;
        descr = imp.getName();
      }
      topDescriptor = descr;
      if (isImportByLocation) {
        importByNameOrLocation.setText("Location");
      } else {
        importByNameOrLocation.setText("Name");
      }
    } else {
      // Import is NOT defined
      importByNameOrLocation.setText("(Name or Location)");
    
    }

    topDescriptorField.setText(topDescriptor != null ? topDescriptor : "");
    ignoreUpdate = false;
  }
}
