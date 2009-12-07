package org.apache.uima.casviewer.ui.internal.util;

import org.eclipse.swt.widgets.Composite;

/**
 * 3 element return value for 2 panel forms
 */
public class Form2Panel {

	public Composite form;

	public Composite left;

	public Composite right;

	public Form2Panel(Composite form, Composite left, Composite right) {
		this.form = form;
		this.left = left;
		this.right = right;
	}
}
