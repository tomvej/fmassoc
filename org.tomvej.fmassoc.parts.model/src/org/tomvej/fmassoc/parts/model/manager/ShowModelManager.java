package org.tomvej.fmassoc.parts.model.manager;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;

/**
 * Creates and displays dialog model manager.
 * 
 * @author Tomáš Vejpustek
 * @see ModelManagerDialog
 *
 */
public class ShowModelManager {
	/**
	 * Create and display model manager.
	 */
	@Execute
	public void execute(IEclipseContext context) {
		ModelManagerDialog dialog = ContextInjectionFactory.make(ModelManagerDialog.class, context);
		dialog.open();
	}
}