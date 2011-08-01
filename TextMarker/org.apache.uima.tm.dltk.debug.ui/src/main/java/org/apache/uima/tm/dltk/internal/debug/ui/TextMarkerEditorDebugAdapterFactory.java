package org.apache.uima.tm.dltk.internal.debug.ui;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.ui.actions.IRunToLineTarget;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.dltk.internal.debug.ui.ScriptRunToLineAdapter;

public class TextMarkerEditorDebugAdapterFactory implements IAdapterFactory {
  public Object getAdapter(Object adaptableObject, Class adapterType) {
    if (adapterType == IRunToLineTarget.class) {
      return new ScriptRunToLineAdapter();
    } else if (adapterType == IToggleBreakpointsTarget.class) {
      return new TextMarkerToggleBreakpointAdapter();
    }

    return null;
  }

  public Class[] getAdapterList() {
    return new Class[] { IRunToLineTarget.class, IToggleBreakpointsTarget.class };
  }
}
