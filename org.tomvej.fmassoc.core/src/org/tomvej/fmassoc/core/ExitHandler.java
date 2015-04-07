package org.tomvej.fmassoc.core;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;

/**
 * Closes application.
 * 
 * @author Tomáš Vejpustek
 */
public class ExitHandler {
	/**
	 * Close application.
	 */
	@Execute
	public void execute(IWorkbench workbench) {
		workbench.close();
	}
}
