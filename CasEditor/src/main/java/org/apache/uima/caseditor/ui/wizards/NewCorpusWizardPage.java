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

package org.apache.uima.caseditor.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * TODO: add javadoc here
 */
final class NewCorpusWizardPage extends WizardPage
{
    private Text mProjectText;
    
    private Text mCorpusText;
    
    NewCorpusWizardPage()
    {
        super("pagename2");
    }
    
    public void createControl(Composite parent)
    {
        // initializeDialogUnits(parent);
        
        Composite composite = new Composite(parent, SWT.NONE);
        
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.numColumns = 3;
        composite.setLayout(layout);
        
        // Project
        Label projectLabel = new Label(composite, SWT.NONE);
        projectLabel.setText("Project");
        
        mProjectText = new Text(composite, SWT.NONE);
        
        Button browseProjectButton = new Button(composite, SWT.NONE);
        browseProjectButton.setText("Browse");
        
        // Corpus
        Label corpusLabel = new Label(composite, SWT.NONE);
        corpusLabel.setText("Corpus");
        
        mCorpusText = new Text(composite, SWT.NONE);
        
        Button browseFodlerButton = new Button(composite, SWT.NONE);
        browseFodlerButton.setText("Browse");
        
        setControl(composite);
    }
    
    String getProject()
    {
        return mProjectText.getText();
    }
    
    String getCorpus()
    {
        return mCorpusText.getText();
    }
    
}
