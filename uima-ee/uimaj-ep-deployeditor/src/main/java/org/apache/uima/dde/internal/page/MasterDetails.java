package org.apache.uima.dde.internal.page;

import org.apache.uima.aae.deployment.AEDeploymentDescription;
import org.apache.uima.dde.internal.DeploymentDescriptorEditor;
import org.apache.uima.dde.internal.Messages;
import org.apache.uima.dde.internal.details.DetailsPageProvider;
import org.apache.uima.dde.internal.hover.HoverManager;
import org.apache.uima.dde.internal.hover.DDEInformationControl;
import org.apache.uima.dde.internal.provider.DDTreeContentProvider;
import org.apache.uima.dde.internal.provider.DDTreeLabelProvider;
import org.apache.uima.taeconfigurator.TAEConfiguratorPlugin;
import org.apache.uima.tools.internal.ui.forms.FormSection;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;


public class MasterDetails extends MasterDetailsBlock { // implements Listener {

  private AEDeploymentDescription aeDD;

  private TreeViewer aeConfigViewer;

  private DeploymentDescriptorEditor multiPageEditor;

  private IManagedForm managedForm = null;

  private ScrolledForm form;

  public MasterDetails(DeploymentDescriptorEditor editor, IManagedForm managedForm,
          IWorkbenchPartSite workbenchPartSite) {
    multiPageEditor = editor;
    this.managedForm = managedForm;
    this.form = managedForm.getForm();
//    _workbenchPartSite = workbenchPartSite;
    aeDD = multiPageEditor.getAEDeploymentDescription();
  }

  public void refresh() {
    TreeItem selectedItem = null;
    TreeItem[] selection = aeConfigViewer.getTree().getSelection();
    if (selection.length > 0) {
      selectedItem = selection[0];
    } else {
      selectFirstElement();
    }
    aeConfigViewer.getTree().setRedraw(false);
    aeConfigViewer.refresh();
    if (selectedItem != null) {
      // Restore selection
      aeConfigViewer.setSelection(new StructuredSelection(selectedItem.getData()));
      selection = aeConfigViewer.getTree().getSelection();
      if (selection != null) {
        selection[0].setExpanded(true);
      }
      aeConfigViewer.refresh();
    }
    aeConfigViewer.getTree().setRedraw(true);
  }

  public void setInput(Object input) {
    if (input instanceof AEDeploymentDescription) {
      aeDD = (AEDeploymentDescription) input;
      aeConfigViewer.setInput(aeDD);
      refresh();
    }
  }

  public void init() {
    selectFirstElement();
  }

  @Override
  protected void createMasterPart(IManagedForm managedForm, Composite parentSashForm) {
    FormToolkit toolkit = managedForm.getToolkit();
    Composite body = managedForm.getForm().getBody();
    body.setLayout(new GridLayout(1, false)); // this is required !
    toolkit.paintBordersFor(body);

    ScrolledForm form = managedForm.getForm();
    FormColors colors = toolkit.getColors();
    colors.initializeSectionToolBarColors();
    Color gbg = colors.getColor(FormColors.TB_GBG);
    Color bg = colors.getBackground();
    form.getForm().setTextBackground(new Color[] { bg, gbg }, new int[] { 100 }, true);
    // form.getForm().setSeparatorVisible(true);

    createAEConfigSection(parentSashForm, toolkit);

    sashForm.setBackground(sashForm.getDisplay().getSystemColor(SWT.COLOR_GRAY));
  }

  @Override
  protected void createToolBarActions(IManagedForm managedForm) {
    final ScrolledForm form = managedForm.getForm();

    Action haction = new Action("hor", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
      public void run() {
        sashForm.setOrientation(SWT.HORIZONTAL);
        form.reflow(true);
      }
    };
    haction.setChecked(true);
    haction.setToolTipText("Horizontal Orientation");
    haction.setImageDescriptor(TAEConfiguratorPlugin.getImageDescriptor(TAEConfiguratorPlugin.IMAGE_TH_HORIZONTAL));
    haction.setDisabledImageDescriptor(TAEConfiguratorPlugin
            .getImageDescriptor(TAEConfiguratorPlugin.IMAGE_TH_HORIZONTAL));

    Action vaction = new Action("ver", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
      public void run() {
        sashForm.setOrientation(SWT.VERTICAL);
        form.reflow(true);
      }
    };
    vaction.setChecked(false);
    vaction.setToolTipText("Vertical Orientation");
    vaction.setImageDescriptor(TAEConfiguratorPlugin.getImageDescriptor(TAEConfiguratorPlugin.IMAGE_TH_VERTICAL));
    vaction.setDisabledImageDescriptor(TAEConfiguratorPlugin
            .getImageDescriptor(TAEConfiguratorPlugin.IMAGE_TH_VERTICAL));
    form.getToolBarManager().add(haction);
    form.getToolBarManager().add(vaction);
    form.updateToolBar();
  }

  @Override
  protected void registerPages(DetailsPart detailsPart) {
    detailsPart.setPageLimit(10);
    // register static page for the extensions
    // detailsPart.registerPage(AEDeploymentMetaData_Impl.class, new
    // AEMetaDataDetailsPage_V1(managedForm));
    // detailsPart.registerPage(RemoteAEDeploymentMetaData_Impl.class, new
    // RemoteAEMetaDataDetails_V1(managedForm));
    // detailsPart.registerPage(AsyncAggregateErrorConfiguration_Impl.class, new
    // AsyncAEErrorConfigDetailsPage_V0(managedForm));
    // detailsPart.registerPage(AsyncPrimitiveErrorConfiguration_Impl.class, new
    // AsyncAEErrorConfigDetailsPage_V0(managedForm));

    // register a dynamic provider for elements
    detailsPart.setPageProvider(new DetailsPageProvider(multiPageEditor, managedForm, this, detailsPart));
  }

  /** ********************************************************************** */
  private SectionPart spart;

  private Section createAEConfigSection(Composite parent, FormToolkit toolkit) {
    TableWrapData td;

    Section section = FormSection.createGridDataSection(toolkit, parent, SWT.NONE,
            Messages.DDE_AEConfigPage_AEConfigTree_Section_Title,
            Messages.DDE_AEConfigPage_AEConfigTree_Section_Description, 10, 10, GridData.FILL_BOTH
                    | GridData.VERTICAL_ALIGN_BEGINNING, 1, 1);
    section.clientVerticalSpacing = 10;
    section.setExpanded(true);
    section.addExpansionListener(new ExpansionAdapter() {
      public void expansionStateChanged(ExpansionEvent e) {
        form.reflow(true);
      }
    });
    spart = new SectionPart(section);
    managedForm.addPart(spart);

    // Create ToolBar
    Composite sectionToolbarComposite = FormSection.createGridLayoutContainer(toolkit, section, 4,
            0, 0);
    section.setTextClient(sectionToolbarComposite);
    toolkit.createCompositeSeparator(section);

    // //////////////////

    Composite sectionClient = toolkit.createComposite(section);
    section.setClient(sectionClient);
    GridLayout layout = new GridLayout(1, false);
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    sectionClient.setLayout(layout);
    GridData gd = new GridData(GridData.FILL_BOTH);
    sectionClient.setLayoutData(gd);
    toolkit.paintBordersFor(sectionClient);

    // /////////////////////////////////////////////////////////////////////

    final Tree tree = toolkit.createTree(sectionClient, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    gd = new GridData(GridData.FILL_BOTH);
    gd.grabExcessVerticalSpace = true;
    tree.setLayoutData(gd);
    tree.setLinesVisible(true);
    tree.setHeaderVisible(true);
    String headers[] = { "Element", "Remote", "Instances" };
    final TreeColumn columns[] = new TreeColumn[headers.length];
    for (int i = 0; i < headers.length; i++) {
      int style;
      if (i==0) {
        style = SWT.NONE;
      } else {
        style = SWT.CENTER;
      }
      TreeColumn tc = new TreeColumn(tree, style, i);
      tc.setResizable(true);
      tc.setText(headers[i]);
      if (i == 0) {
        tc.setWidth(200);
        // } else {
        // tc.setWidth(100);
      }
      columns[i] = tc;
      if (i == 1 || i == 2) {
        tc.pack();
        // Trace.err("pack: " + tc.getWidth());
      }
    }
    // tree.addListener(SWT.MouseHover, this);
    tree.addControlListener(new ControlAdapter() {
      public void controlResized(ControlEvent e) {
        // int width = tree.getSize().x;
        // ScrollBar vBar = tree.getVerticalBar();
        // if (vBar.isEnabled()) {
        // // Trace.err("vBar.getSize().x: " + vBar.getSize().x);
        // width -= vBar.getSize().x/2;
        // } else {
        // Trace.err("NO vBar");
        // }
        // width = width - tree.computeTrim(0,0,0,0).width ;
        // columns[0].setWidth(width-140);
        // columns[1].setWidth(70);
        // columns[2].setWidth(70);
      }
    });
    /*
     * sectionClient.addControlListener(new ControlAdapter() { public void
     * controlResized(ControlEvent e) { Rectangle area = sectionClient.getClientArea(); Point size =
     * tree.computeSize(SWT.DEFAULT, SWT.DEFAULT); ScrollBar vBar = tree.getVerticalBar(); //
     * Trace.err("vBar.getSize().x: " + vBar.getSize().x); int width = area.width -
     * tree.computeTrim(0,0,0,0).width - vBar.getSize().x; if (size.y > area.height +
     * tree.getHeaderHeight()) { // Subtract the scrollbar width from the total column width // if a
     * vertical scrollbar will be required Point vBarSize = vBar.getSize(); width -= vBarSize.x; }
     * Point oldSize = tree.getSize(); if (oldSize.x > area.width) { // table is getting smaller so
     * make the columns // smaller first and then resize the table to // match the client area width
     * columns[0].setWidth(width-140); columns[1].setWidth(70); columns[2].setWidth(70);
     * tree.setSize(area.width, area.height); } else { // table is getting bigger so make the table //
     * bigger first and then make the columns wider // to match the client area width
     * tree.setSize(area.width, area.height); columns[0].setWidth(width-140);
     * columns[1].setWidth(70); columns[2].setWidth(70); } } });
     */

    aeConfigViewer = new TreeViewer(tree);
    // AEConfigContentProvider_V1 contentProvider = new AEConfigContentProvider_V1();
    // AEConfigLabelProvider_V0 labelProvider = new AEConfigLabelProvider_V0();
    DDTreeContentProvider contentProvider = new DDTreeContentProvider();
    DDTreeLabelProvider labelProvider = new DDTreeLabelProvider();
    aeConfigViewer.setContentProvider(contentProvider);
    aeConfigViewer.setLabelProvider(labelProvider);
    aeConfigViewer.addSelectionChangedListener(new ISelectionChangedListener() {
      public void selectionChanged(SelectionChangedEvent event) {
        ISelection selection = event.getSelection();
        if (selection != null && selection instanceof IStructuredSelection) {
          IStructuredSelection ssel = (IStructuredSelection) selection;
          if (ssel.size() > 0) {
            // Trace.err(ssel.getFirstElement().getClass().getName());
          }
        }
        managedForm.fireSelectionChanged(spart, event.getSelection());
      }
    });

    AbstractHeaderPage.createExpandOrCollapseAllMenu(toolkit, section, sectionToolbarComposite,
            aeConfigViewer, true);
    AbstractHeaderPage.createExpandOrCollapseAllMenu(toolkit, section, sectionToolbarComposite,
            aeConfigViewer, false);

//    ImageHyperlink info = new ImageHyperlink(sectionToolbarComposite, SWT.NULL);
//    toolkit.adapt(info, true, true);
//    info.setImage(ImageLoader.getInstance().getImage(ImageLoader.ICON_UI_IDE_FILTER));
//    info.setToolTipText("Filters");
//    // info.setBackground(section.getTitleBarGradientBackground());
//    info.addHyperlinkListener(new HyperlinkAdapter() {
//      public void linkActivated(HyperlinkEvent e) {
//        String[] ids = new String[0];
//        FilterDialog dialog = new FilterDialog(aeConfigViewer.getControl().getShell(), ids);
//        if (dialog.open() == Window.OK) {
//
//        }
//
//      }
//    });

    ///////////////////////////////////////////////////////////////////////////

    HoverManager hover = new HoverManager(aeConfigViewer, getPresenterControlCreator("commandId"));
    hover.install(aeConfigViewer.getTree());

    if (aeDD != null) {
      aeConfigViewer.setInput(aeDD);
    }

    return section;
  } // createDelegatesConfigSection

  private IInformationControlCreator getPresenterControlCreator(final String commandId) {
    return new IInformationControlCreator() {
      public IInformationControl createInformationControl(Shell parent) {
        int shellStyle = SWT.RESIZE;
        int treeStyle = SWT.V_SCROLL | SWT.H_SCROLL;
        return new DDEInformationControl(parent, SWT.TOOL | SWT.NO_TRIM, 0);
        // DefaultInformationControl(parent, null);
      }
    };
  }

  private void createButtons(Composite parent, FormToolkit toolkit) {
    Composite buttonContainer = toolkit.createComposite(parent);
    GridData gd = new GridData(GridData.FILL_VERTICAL);
    buttonContainer.setLayoutData(gd);
    GridLayout layout = new GridLayout(1, false);
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    buttonContainer.setLayout(layout);
    toolkit.paintBordersFor(buttonContainer);

    createVerticalSpace(toolkit, buttonContainer, 1, 24);
    createButton(toolkit, buttonContainer, "Add...", 1);
    createButton(toolkit, buttonContainer, "Remove", 2);

  }

  protected Button createButton(FormToolkit toolkit, Composite parent, String label, int index) {
    Button button;
    if (toolkit != null)
      button = toolkit.createButton(parent, label, SWT.PUSH);
    else {
      button = new Button(parent, SWT.PUSH);
      button.setText(label);
    }
    GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    button.setLayoutData(gd);
    // button.setData(new Integer(index));
    button.setData(Integer.valueOf(index));
    return button;
  }

  protected Label createVerticalSpace(FormToolkit toolkit, Composite parent, int span, int spacing) {
    Label label;
    if (toolkit != null) {
      label = toolkit.createLabel(parent, null);
    } else {
      label = new Label(parent, SWT.NULL);
    }
    GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
    gd.verticalIndent = spacing;
    gd.horizontalSpan = span;
    gd.widthHint = 0;
    gd.heightHint = 0;
    label.setLayoutData(gd);
    return label;
  }

  /** ********************************************************************** */

  private void selectFirstElement() {
    Tree tree = aeConfigViewer.getTree();
    TreeItem[] items = tree.getItems();
    if (items.length == 0) {
      // Tree is EMPTY
      detailsPart.selectionChanged(spart, new StructuredSelection(aeConfigViewer));
      return;
    }
    TreeItem firstItem = items[0];
    Object obj = firstItem.getData();
    aeConfigViewer.setSelection(new StructuredSelection(obj));
  }

  void fireSelection() {
    aeConfigViewer.setSelection(aeConfigViewer.getSelection());
  }

//  public void handleEvent(Event event) {
//    if (event.type == SWT.MouseHover) {
//      handleHover(event);
//    }
//  }
/*
  // From CDE
  public void handleHover(Event event) {
    Tree tree = aeConfigViewer.getTree();
    // next getItem call requires that table have SWT.FULL_SELECTION Style
    TreeItem item = tree.getItem(new Point(event.x, event.y));
    if (null != item) {
      Object o = item.getData();
      if (null == o)
        throw new InternalErrorCDE("invalid state");

      if (o instanceof AEDeploymentMetaData) {
        setToolTipText(tree, "AEDeploymentMetaData");
      } else if (o instanceof FeatureDescription) {
      }
    } else
      tree.setToolTipText("");
  }

  public static void setToolTipText(Control w, String text) {
    if (null != text)
      w.setToolTipText(parseToFitInToolTips(text));
  }

  // tool tips seem to require no blanks following /n on Windows.
  protected static String parseToFitInToolTips(String text) {
    if (null == text)
      return "";
    StringBuffer buffer = new StringBuffer();
    final int MAGIC_LENGTH = 65;
    StringTokenizer tokenizer = new StringTokenizer(text);
    int lengthAccumulator = 0;
    while (tokenizer.hasMoreTokens()) {
      if (lengthAccumulator > 0) {
        buffer.append(' ');
      }
      String nextToken = tokenizer.nextToken();
      buffer.append(nextToken);
      lengthAccumulator += (nextToken.length() + 1);
      if (lengthAccumulator > MAGIC_LENGTH && tokenizer.hasMoreTokens()) {
        // this is to avoid a final blank line
        buffer.append("\n"); //$NON-NLS-1$
        lengthAccumulator = 0;
      }
    }
    return new String(buffer);
  }
*/
}
