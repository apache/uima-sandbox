package org.apache.uima.tm.dltk.internal.debug.ui.console;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IPatternMatchListenerDelegate;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;

public class TextMarkerConsoleTracker implements IPatternMatchListenerDelegate {
  private TextConsole console;

  public void connect(TextConsole console) {
    this.console = console;
  }

  public void disconnect() {
    console = null;
  }

  protected TextConsole getConsole() {
    return console;
  }

  public void matchFound(PatternMatchEvent event) {
    try {
      int offset = event.getOffset();
      int length = event.getLength();
      IHyperlink link = new TextMarkerFileHyperlink(console);
      console.addHyperlink(link, offset + 1, length - 2);
    } catch (BadLocationException e) {
    }
  }
}