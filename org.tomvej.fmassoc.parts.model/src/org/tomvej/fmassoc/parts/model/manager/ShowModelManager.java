package org.tomvej.fmassoc.parts.model.manager;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.tomvej.fmassoc.parts.model.core.ModelChooser;

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
	public void execute(IEclipseContext context, EModelService model, MApplication app) {
		ModelManagerDialog dialog = ContextInjectionFactory.make(ModelManagerDialog.class, context);
		dialog.open();

		// reload the model
		MToolControl control = (MToolControl) model.find("org.tomvej.fmassoc.parts.model.toolcontrol.chooser", app);
		((ModelChooser) control.getObject()).loadSelectedModel();
	}
}