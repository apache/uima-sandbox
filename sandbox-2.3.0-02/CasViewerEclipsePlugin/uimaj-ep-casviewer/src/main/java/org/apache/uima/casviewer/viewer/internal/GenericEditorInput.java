
package org.apache.uima.casviewer.viewer.internal;

import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class GenericEditorInput implements IEditorInput {
    private String         title = ""; // title to be displayed
	private String         xcasOrxmiFileName = null;
	private String         typesystemStyleFile = null;
    private CAS            cas = null;
    private boolean        inputIsFile = true;
    private TypeSystemDescription      typeSystem = null;
    private List<String> preSelectedTypeNames = null;
    
	public GenericEditorInput(String name) {
        if (name == null || name.trim().length()==0) {
            this.xcasOrxmiFileName = "";
        } else {
            this.xcasOrxmiFileName = name;
        }
	}

    public GenericEditorInput(String name, TypeSystemDescription aTypeSystemDescription) {
        this(name, aTypeSystemDescription, null);
    }
    
    public GenericEditorInput(String name, TypeSystemDescription aTypeSystemDescription,
            List<String> preSelectedTypeNames) {
        this(name);
        this.typeSystem = aTypeSystemDescription;
        this.preSelectedTypeNames = preSelectedTypeNames;
    }

    public GenericEditorInput (CAS aCAS, String title) {
        this(aCAS, title, null);
    }
    
    public GenericEditorInput (CAS aCAS, String title, String styleFile) {
        cas = aCAS;
        this.title = title;
        this.xcasOrxmiFileName = "";
        this.typesystemStyleFile = styleFile;
        inputIsFile = false;
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return true;
	}
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
				ISharedImages.IMG_OBJ_ELEMENT);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
    public String getName () {
        return xcasOrxmiFileName;
    }
    
    public String getTitle () {
        return title;
    }
    
    public CAS getInputCAS () {
        return cas;
    }
	public Object getInput() {
        if (inputIsFile) {
            return xcasOrxmiFileName;
        } else {
            return cas;
        }
	}
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return null;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return getName();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof GenericEditorInput))
            return false;
        GenericEditorInput other = (GenericEditorInput) obj;
        if (inputIsFile) {
            if ( !(other.getInput() instanceof String) ) {
                return false;
            }
            if (xcasOrxmiFileName.trim().length()==0) {
                if (other.getName() != null) {
                    xcasOrxmiFileName = new String(other.getName());
                    Trace.trace("Update name to:" + xcasOrxmiFileName);
                }
                return true;
            }
            return xcasOrxmiFileName.equals(other.getName());
        } else {
            return cas.equals(other.cas);
        }
    }

    /**
     * @return the typeSystem
     */
    public TypeSystemDescription getTypeSystemDescription() {
        return typeSystem;
    }

    /**
     * @return the preSelectedTypeNames
     */
    public List<String> getPreSelectedTypeNames() {
        return preSelectedTypeNames;
    }

    /**
     * @return the typesystemStyleFile
     */
    public String getTypesystemStyleFile() {
        return typesystemStyleFile;
    }
} // GenericEditorInput