package org.apache.uima.tm.dltk.textmarker.console;

import java.io.IOException;

import org.apache.uima.tm.dltk.core.TextMarkerNature;
import org.apache.uima.tm.dltk.textmarker.launching.TextMarkerLaunchingPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.console.ScriptConsoleServer;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.launching.ScriptLaunchUtil;


public class TextMarkerConsoleUtil {

  public static void runDefaultTextMarkerInterpreter(TextMarkerInterpreter interpreter)
          throws CoreException, IOException {
    ScriptConsoleServer server = ScriptConsoleServer.getInstance();

    String id = server.register(interpreter);
    String port = Integer.toString(server.getPort());

    String[] args = new String[] { "127.0.0.1", port, id };

    IExecutionEnvironment exeEnv = (IExecutionEnvironment) EnvironmentManager.getLocalEnvironment()
            .getAdapter(IExecutionEnvironment.class);
    IFileHandle scriptFile = TextMarkerLaunchingPlugin.getDefault().getConsoleProxy(exeEnv);
    ScriptLaunchUtil.runScript(TextMarkerNature.NATURE_ID, scriptFile, null, null, args, null);
  }
}
