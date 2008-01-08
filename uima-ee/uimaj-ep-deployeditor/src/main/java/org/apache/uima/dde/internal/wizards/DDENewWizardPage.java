
package org.apache.uima.dde.internal.wizards;

import org.apache.uima.taeconfigurator.wizards.AbstractNewWizardPage;
import org.eclipse.jface.viewers.ISelection;

/**
 * The "New" wizard page allows setting the container for the new file as well as the file name.
 * Will only accept file name without the extension OR with the extension that matches the expected
 * one (xml).
 */

public class DDENewWizardPage extends AbstractNewWizardPage {

  public DDENewWizardPage(ISelection selection) {
    super(selection, "big_ae.gif", "Deployment Descriptor File",
            "Create a new Deployment Descriptor File", "deploymentDescriptor.xml");
  }
}
