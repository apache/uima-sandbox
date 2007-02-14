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

package org.apache.uima.caseditor.ui;


import org.apache.uima.caseditor.ui.corpusview.CorpusExplorerView;
import org.apache.uima.caseditor.ui.wizards.NewCorpusWizard;
import org.apache.uima.caseditor.ui.wizards.NlpProjectWizard;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * This <code>PerspectiveFactory</code> generates the intial layout
 * for the NLP perspective.
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.4.2.1 $, $Date: 2007/01/04 14:37:53 $
 */
public class NlpPerspectiveFactory implements IPerspectiveFactory
{
    /**
     * ID of the perpective factory. Use this ID for example in the plugin.xml
     * file.
     */
    public static String ID = "Annotator.perspective.NLP";
    
    /**
     * Define the initial layout of the nlp perspective
     */
    public void createInitialLayout(IPageLayout layout)
    {
        defineActions(layout);
        defineLayout(layout);
    }
    
    private void defineActions(IPageLayout layout)
    {
        // add "new wizards"
        layout.addNewWizardShortcut(NlpProjectWizard.ID);
        layout.addNewWizardShortcut(NewCorpusWizard.ID);
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
        
        // layout.addNewWizardShortcut("Annotator.NewDocumentWizard");
        
        // add "show views"
        layout.addShowViewShortcut(CorpusExplorerView.ID);
        layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
        
        // add "open perpective"
        layout.addPerspectiveShortcut(NlpPerspectiveFactory.ID);
    }
    
    private void defineLayout(IPageLayout layout)
    {
        String editorArea = layout.getEditorArea();
        
        // left views
        IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT,
                0.19f, editorArea);
        left.addView(CorpusExplorerView.ID);
        
        // right views
        IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT,
                0.70f, editorArea);
        
        right.addView(IPageLayout.ID_OUTLINE);
        
        // bottom views
        IFolderLayout bottom = layout.createFolder("rightBottom",
                IPageLayout.BOTTOM, 0.75f, editorArea);
        
        bottom.addView(IPageLayout.ID_PROP_SHEET);
    }
}