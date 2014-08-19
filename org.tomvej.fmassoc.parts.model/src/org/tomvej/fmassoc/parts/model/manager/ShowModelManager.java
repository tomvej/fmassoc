package org.tomvej.fmassoc.parts.model.manager;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;

public class ShowModelManager {
	@Execute
	public void execute(IEclipseContext context) {
		ModelManagerDialog dialog = ContextInjectionFactory.make(ModelManagerDialog.class, context);
		dialog.open();
	}
}