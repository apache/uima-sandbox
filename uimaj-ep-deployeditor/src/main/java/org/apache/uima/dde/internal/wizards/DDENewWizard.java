package org.apache.uima.dde.internal.wizards;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import org.apache.uima.taeconfigurator.InternalErrorCDE;
import org.apache.uima.taeconfigurator.wizards.AbstractNewWizard;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class DDENewWizard extends AbstractNewWizard  {

  static public final String EDITOR_ID="com.ibm.apache.uima.dde.internal.MultiPageEditor";
  static protected String editorId = EDITOR_ID;
  
  public DDENewWizard() {
    super("New Analysis Engine Descriptor File");
  }

  public void addPages() {
    page = new DDENewWizardPage(selection);
    addPage(page);
  }

  public String getPrototypeDescriptor(String name) {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + "<analysisEngineDeploymentDescription "
    + XMLNS_PART
    + "<name>" + name + "</name>\n"
    + "<description></description>\n" + "<version>1.0</version>\n" + "<vendor></vendor>\n"

    + "<deployment protocol=\"jms\" provider=\"activemq\">\n"
    + "<casPool numberOfCASes=\"5\"/>\n"
    + "<service>"
    + "<inputQueue endpoint=\"myQueueName\" brokerURL=\"tcp://localhost:61616\"/>\n"
    // + "<topDescriptor>\n"
    // + "<import location=\"\"/>\n"
    // + "</topDescriptor>\n"
    + "</service>"
    + "</deployment>\n"

    + "</analysisEngineDeploymentDescription>\n";
  }

}
