package org.apache.uima.casviewer.ui.internal.util;

import org.apache.uima.tools.common.internal.images.ImageLoader;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;

public abstract class AbstractSectionPart extends SectionPart {

    public static int SECTION_STYLE_COMMON  = Section.TWISTIE | Section.EXPANDED;
    
    protected IManagedForm          managedForm;
    protected Composite             parent;
    protected Section               section;
    protected String                title;
    protected String                description;
    
    /**
     * @param section
     */
    public AbstractSectionPart(Section section) {
        super(section);
    }

    public AbstractSectionPart(IManagedForm managedForm, Composite parent, 
                               Section section, int style, String title, String description) 
    {
        super(section);
        this.managedForm    = managedForm;
        this.parent         = parent;
        this.section        = section;
        this.title          = title;
        this.description    = description;
    }
    
    public AbstractSectionPart(IManagedForm managedForm, FormToolkit toolkit,
            Composite parent, int style) {
        super(parent, toolkit, style);
        this.managedForm = managedForm;
    }

    public void setSectionTitle (String title)
    {
        if (title == null) return;
        section.setText(title);
    }
    
    static protected ImageHyperlink createToolbarItem (FormToolkit toolkit, Composite toolbarComposite, 
            ImageDescriptor imgDesc, String toolTipText, Color bgColor)
    {
        ImageHyperlink info = new ImageHyperlink(toolbarComposite, SWT.NULL);
        toolkit.adapt(info, true, true);
        info.setImage(imgDesc.createImage());
        info.setToolTipText(toolTipText);
        info.setBackground(bgColor);

        return info;
    } // createToolbarItem    
    
    static protected ImageHyperlink createExpandAllMenu (FormToolkit toolkit, Section section, Composite toolbarComposite, 
            final TreeViewer treeViewer)
    {
        ImageHyperlink info = new ImageHyperlink(toolbarComposite, SWT.NULL);
        toolkit.adapt(info, true, true);
        ImageDescriptor descriptor = ImageLoader.getInstance().getImageDescriptor(ImageLoader.ICON_SEARCH_EXPANDALL);
        Image image = descriptor.createImage();
        info.setImage(image);
        info.setToolTipText("Expand All");
        info.setBackground(section.getTitleBarGradientBackground());
        info.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent e) {
                treeViewer.expandAll();
            }
        });        
        return info;
    } // createExpandAllMenu

    static protected ImageHyperlink createCollapseAllMenu (FormToolkit toolkit, Section section, Composite toolbarComposite, 
            final TreeViewer treeViewer)
    {
        ImageHyperlink info = new ImageHyperlink(toolbarComposite, SWT.NULL);
        toolkit.adapt(info, true, true);
        ImageDescriptor descriptor = ImageLoader.getInstance().getImageDescriptor(ImageLoader.ICON_SEARCH_COLLAPSEALL);
        Image image = descriptor.createImage();
        info.setImage(image);
        info.setToolTipText("Collapse All");
        info.setBackground(section.getTitleBarGradientBackground());
        info.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent e) {
                treeViewer.collapseAll();
            }
        });
        return info;
    } // createCollapseAllMenu
    
}
