/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jun 23, 2006, 12:40:24 PM
 * source:  TemplatePage.java
 */
package org.apache.uima.dde.internal.page;

import org.apache.uima.aae.deployment.AEDeploymentDescription;
import org.apache.uima.dde.internal.DeploymentDescriptorEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;


/**
 * 
 * 
 */
public class AEConfigurationPage extends AbstractHeaderPage {

  static public final String PAGE_TITLE = "Deployment Configurations";

//  static protected AEConfigurationPage instance = null;

  private ScrolledForm form;

//  private TreeViewer aeConfigViewer;

  private MasterDetails md;

  private DeploymentDescriptorEditor multiPageEditor;

  private AEDeploymentDescription input;

  /**
   * @param editor
   * @param id
   * @param title
   */
  public AEConfigurationPage(DeploymentDescriptorEditor editor, String id, String title) {
    super(editor.cde, id, title);
    multiPageEditor = editor;

  }

  /**
   * @param id
   * @param title
   */
  public AEConfigurationPage(String id, String title) {
    super(id, title);
    // TODO Auto-generated constructor stub
  }

//  static public AEConfigurationPage getInstance() {
//    return instance;
//  }

  public void refresh() {
    md.refresh();
  }

  public void setInput(Object input) {
    if (input instanceof AEDeploymentDescription) {
      if (md != null) {
        md.setInput(input);
      } else {
        this.input = (AEDeploymentDescription) input;
      }
    }
  }

  /**
   * Called by the framework to fill in the contents
   */
  protected void createFormContent(IManagedForm managedForm) {
    super.createFormContent(managedForm);
    form = managedForm.getForm();
    form.setText(PAGE_TITLE);

    md = new MasterDetails(multiPageEditor, managedForm, getSite());
    md.createContent(managedForm);
    md.init(); // will trigger the display of the details page
    if (input != null) {
      md.setInput(input);
    }
  }

  /** ********************************************************************** */

}
